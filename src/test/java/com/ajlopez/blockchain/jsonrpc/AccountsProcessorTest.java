package com.ajlopez.blockchain.jsonrpc;

import com.ajlopez.blockchain.bc.BlockChain;
import com.ajlopez.blockchain.core.Account;
import com.ajlopez.blockchain.core.Block;
import com.ajlopez.blockchain.core.Transaction;
import com.ajlopez.blockchain.core.types.Address;
import com.ajlopez.blockchain.execution.TransactionExecutor;
import com.ajlopez.blockchain.json.JsonStringValue;
import com.ajlopez.blockchain.json.JsonValue;
import com.ajlopez.blockchain.json.JsonValueType;
import com.ajlopez.blockchain.state.Trie;
import com.ajlopez.blockchain.store.AccountStore;
import com.ajlopez.blockchain.store.AccountStoreProvider;
import com.ajlopez.blockchain.store.HashMapStore;
import com.ajlopez.blockchain.store.TrieStore;
import com.ajlopez.blockchain.test.utils.FactoryHelper;
import com.ajlopez.blockchain.utils.HexUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ajlopez on 02/12/2018.
 */
public class AccountsProcessorTest {
    // https://www.infoq.com/news/2009/07/junit-4.7-rules
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void unknownMethod() throws JsonRpcException {
        List<JsonValue> params = new ArrayList<>();
        JsonRpcRequest request =  new JsonRpcRequest("1", "2.0", "eth_foo", params);

        AccountsProcessor processor = new AccountsProcessor(null, null);

        exception.expect(JsonRpcException.class);
        exception.expectMessage("Unknown method 'eth_foo'");
        processor.processRequest(request);
    }

    @Test
    public void getBalanceWithNoParameter() throws JsonRpcException {
        List<JsonValue> params = Collections.emptyList();
        JsonRpcRequest request =  new JsonRpcRequest("1", "2.0", "eth_getBalance", params);

        AccountsProcessor processor = new AccountsProcessor(null, null);

        exception.expect(JsonRpcException.class);
        exception.expectMessage("Invalid number of parameters: expected 1 thru 2 found 0");
        processor.processRequest(request);
    }

    @Test
    public void getBalanceWithThreeParameters() throws JsonRpcException {
        List<JsonValue> params = new ArrayList();
        params.add(new JsonStringValue("foo"));
        params.add(new JsonStringValue("bar"));
        params.add(new JsonStringValue("foobar"));

        JsonRpcRequest request =  new JsonRpcRequest("1", "2.0", "eth_getBalance", params);

        AccountsProcessor processor = new AccountsProcessor(null, null);

        exception.expect(JsonRpcException.class);
        exception.expectMessage("Invalid number of parameters: expected 1 thru 2 found 3");
        processor.processRequest(request);
    }

    @Test
    public void getBalances() throws JsonRpcException {
        Address sender = FactoryHelper.createRandomAddress();
        Address receiver = FactoryHelper.createRandomAddress();

        long initialBalance = 1000000;
        long transferAmount = 1000;
        int nblocks = 10;

        AccountsProcessor processor = createProcessor(sender, receiver, initialBalance, transferAmount, nblocks);

        checkBalance(processor, sender, "earliest", initialBalance);
        checkBalance(processor, receiver, "earliest", 0);

        checkBalance(processor, sender, "latest", initialBalance - transferAmount * nblocks);
        checkBalance(processor, receiver, "latest", transferAmount * nblocks);

        checkBalance(processor, sender,initialBalance - transferAmount * nblocks);
        checkBalance(processor, receiver,transferAmount * nblocks);

        for (int k = 0; k <= nblocks; k++) {
            String decimalBlockId = Integer.toString(k);
            String hexadecimalBlockId = "0x" + Integer.toString(k, 16);

            checkBalance(processor, sender, decimalBlockId, initialBalance - k * transferAmount);
            checkBalance(processor, receiver, decimalBlockId, k * transferAmount);

            checkBalance(processor, sender, hexadecimalBlockId, initialBalance - k * transferAmount);
            checkBalance(processor, receiver, hexadecimalBlockId, k * transferAmount);
        }
    }

    private static AccountsProcessor createProcessor(Address sender, Address receiver, long initialBalance, long transferAmount, int nblocks) {
        TrieStore accountTrieStore = new TrieStore(new HashMapStore());
        AccountStore accountStore = new AccountStore(accountTrieStore.retrieve(Trie.EMPTY_TRIE_HASH));

        Account senderAccount = new Account(BigInteger.valueOf(initialBalance), 0);

        accountStore.putAccount(sender, senderAccount);
        accountStore.save();

        BlockChain blockChain = FactoryHelper.createBlockChainWithGenesis(accountStore);

        for (int k = 0; k < nblocks; k++) {
            Transaction transaction = new Transaction(sender, receiver, BigInteger.valueOf(transferAmount), k);
            TransactionExecutor transactionExecutor = new TransactionExecutor(accountStore);
            List<Transaction> transactions = Collections.singletonList(transaction);

            transactions = transactionExecutor.executeTransactions(transactions);

            Block parent = blockChain.getBestBlock();
            Block block = new Block(parent.getNumber() + 1, parent.getHash(), transactions, transactionExecutor.getHashRoot());
            accountStore.save();

            blockChain.connectBlock(block);
        }

        BlocksProvider blocksProvider = new BlocksProvider(blockChain);
        AccountStoreProvider accountStoreProvider = new AccountStoreProvider(accountTrieStore);

        return new AccountsProcessor(accountStoreProvider, blocksProvider);
    }

    private static void checkBalance(AccountsProcessor processor, Address address, String blockId, long expectedBalance) throws JsonRpcException {
        JsonStringValue addressValue = new JsonStringValue(address.toString());
        JsonStringValue blockIdValue = new JsonStringValue(blockId);

        List<JsonValue> params = new ArrayList<>();
        params.add(addressValue);
        params.add(blockIdValue);

        JsonRpcRequest request = new JsonRpcRequest("1", "2.0", "eth_getBalance", params);

        checkResponse(processor.processRequest(request), expectedBalance);
    }

    private static void checkBalance(AccountsProcessor processor, Address address, long expectedBalance) throws JsonRpcException {
        JsonStringValue addressValue = new JsonStringValue(address.toString());

        List<JsonValue> params = new ArrayList<>();
        params.add(addressValue);

        JsonRpcRequest request = new JsonRpcRequest("1", "2.0", "eth_getBalance", params);

        checkResponse(processor.processRequest(request), expectedBalance);
    }

    private static void checkResponse(JsonRpcResponse response, long expectedBalance) {
        Assert.assertNotNull(response);

        JsonValue result = response.getResult();
        Assert.assertNotNull(result);

        Assert.assertEquals(JsonValueType.STRING, result.getType());

        String value = (String)result.getValue();

        Assert.assertTrue(value.startsWith("0x"));

        byte[] bytes = HexUtils.hexStringToBytes(value);

        Assert.assertEquals(expectedBalance, (new BigInteger(1, bytes)).longValue());
    }

    @Test
    public void getTransactionCountWithNoParameter() throws JsonRpcException {
        List<JsonValue> params = Collections.emptyList();
        JsonRpcRequest request =  new JsonRpcRequest("1", "2.0", "eth_getTransactionCount", params);

        AccountsProcessor processor = new AccountsProcessor(null, null);

        exception.expect(JsonRpcException.class);
        exception.expectMessage("Invalid number of parameters: expected 1 thru 2 found 0");
        processor.processRequest(request);
    }

    @Test
    public void getTransactionCountWithThreeParameters() throws JsonRpcException {
        List<JsonValue> params = new ArrayList();
        params.add(new JsonStringValue("foo"));
        params.add(new JsonStringValue("bar"));
        params.add(new JsonStringValue("foobar"));

        JsonRpcRequest request =  new JsonRpcRequest("1", "2.0", "eth_getTransactionCount", params);

        AccountsProcessor processor = new AccountsProcessor(null, null);

        exception.expect(JsonRpcException.class);
        exception.expectMessage("Invalid number of parameters: expected 1 thru 2 found 3");
        processor.processRequest(request);
    }
}
