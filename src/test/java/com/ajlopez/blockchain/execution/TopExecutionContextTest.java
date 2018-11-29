package com.ajlopez.blockchain.execution;

import com.ajlopez.blockchain.core.Account;
import com.ajlopez.blockchain.core.types.Address;
import com.ajlopez.blockchain.core.types.Hash;
import com.ajlopez.blockchain.state.Trie;
import com.ajlopez.blockchain.store.AccountStore;
import com.ajlopez.blockchain.test.utils.FactoryHelper;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;

/**
 * Created by ajlopez on 26/11/2018.
 */
public class TopExecutionContextTest {
    @Test
    public void getZeroBalanceFromNewAccount() {
        AccountStore accountStore = new AccountStore(new Trie());

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        BigInteger result = executionContext.getBalance(new Address(new byte[] { 0x01, 0x02 }));
        
        Assert.assertNotNull(result);
        Assert.assertEquals(BigInteger.ZERO, result);
    }

    @Test
    public void getBalanceFromAccountAndCommitDoesNotChangeStore() {
        AccountStore accountStore = new AccountStore(new Trie());
        Account account = new Account(BigInteger.TEN, 42);
        Address address = FactoryHelper.createRandomAddress();

        accountStore.putAccount(address, account);

        Hash originalHash = accountStore.getRootHash();

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        BigInteger result = executionContext.getBalance(address);

        Assert.assertNotNull(result);
        Assert.assertEquals(BigInteger.TEN, result);

        executionContext.commit();

        BigInteger result2 = accountStore.getAccount(address).getBalance();

        Assert.assertNotNull(result2);
        Assert.assertEquals(BigInteger.TEN, result2);

        Assert.assertEquals(originalHash, accountStore.getRootHash());
    }

    @Test
    public void getZeroBalanceFromAccountAndCommitDoesNotChangeStore() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address address = FactoryHelper.createRandomAddress();

        Hash originalHash = accountStore.getRootHash();

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        BigInteger result = executionContext.getBalance(address);

        Assert.assertNotNull(result);
        Assert.assertEquals(BigInteger.ZERO, result);

        executionContext.commit();

        BigInteger result2 = accountStore.getAccount(address).getBalance();

        Assert.assertNotNull(result2);
        Assert.assertEquals(BigInteger.ZERO, result2);

        Assert.assertEquals(originalHash, accountStore.getRootHash());
    }

    @Test
    public void incrementNonceAccount() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address address = FactoryHelper.createRandomAddress();

        Account account = new Account(BigInteger.valueOf(1000), 41);
        accountStore.putAccount(address, account);

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        executionContext.incrementNonce(address);

        long nonce = executionContext.getNonce(address);
        Assert.assertEquals(42, nonce);

        long originalNonce = accountStore.getAccount(address).getNonce();
        Assert.assertEquals(41, originalNonce);
    }

    @Test
    public void incrementNonceAccountAndCommit() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address address = FactoryHelper.createRandomAddress();

        Account account = new Account(BigInteger.valueOf(1000), 41);
        accountStore.putAccount(address, account);

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        executionContext.incrementNonce(address);
        executionContext.commit();

        long nonce = executionContext.getNonce(address);
        Assert.assertEquals(42, nonce);

        long updatedNonce = accountStore.getAccount(address).getNonce();
        Assert.assertEquals(42, updatedNonce);
    }

    @Test
    public void incrementNonceAccountAndRollback() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address address = FactoryHelper.createRandomAddress();

        Account account = new Account(BigInteger.valueOf(1000), 41);
        accountStore.putAccount(address, account);

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        executionContext.incrementNonce(address);
        executionContext.rollback();

        long nonce = executionContext.getNonce(address);
        Assert.assertEquals(41, nonce);

        long updatedNonce = accountStore.getAccount(address).getNonce();
        Assert.assertEquals(41, updatedNonce);
    }

    @Test
    public void getNonceFromNewAccountAndCommit() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address address = FactoryHelper.createRandomAddress();

        Hash originalHash = accountStore.getRootHash();

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        long nonce = executionContext.getNonce(address);
        Assert.assertEquals(0, nonce);

        executionContext.commit();

        long originalNonce = accountStore.getAccount(address).getNonce();
        Assert.assertEquals(0, originalNonce);

        Assert.assertEquals(originalHash, accountStore.getRootHash());
    }

    @Test
    public void transferToAccount() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address senderAddress = FactoryHelper.createRandomAddress();
        Address receiverAddress = FactoryHelper.createRandomAddress();

        Account sender = new Account(BigInteger.valueOf(1000), 42);
        accountStore.putAccount(senderAddress, sender);

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        executionContext.transfer(senderAddress, receiverAddress, BigInteger.valueOf(100));

        BigInteger senderBalance = executionContext.getBalance(senderAddress);
        Assert.assertNotNull(senderBalance);
        Assert.assertEquals(BigInteger.valueOf(1000 - 100), senderBalance);

        BigInteger receiverBalance = executionContext.getBalance(receiverAddress);
        Assert.assertNotNull(receiverBalance);
        Assert.assertEquals(BigInteger.valueOf(100), receiverBalance);

        Account sender2 = accountStore.getAccount(senderAddress);
        Assert.assertNotNull(sender2);
        Assert.assertEquals(BigInteger.valueOf(1000), sender2.getBalance());

        Account receiver2 = accountStore.getAccount(receiverAddress);
        Assert.assertNotNull(receiver2);
        Assert.assertEquals(BigInteger.ZERO, receiver2.getBalance());
    }

    @Test
    public void transferToAccountAndCommit() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address senderAddress = FactoryHelper.createRandomAddress();
        Address receiverAddress = FactoryHelper.createRandomAddress();

        Account sender = new Account(BigInteger.valueOf(1000), 42);
        accountStore.putAccount(senderAddress, sender);

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        executionContext.transfer(senderAddress, receiverAddress, BigInteger.valueOf(100));
        executionContext.commit();

        BigInteger senderBalance = executionContext.getBalance(senderAddress);
        Assert.assertNotNull(senderBalance);
        Assert.assertEquals(BigInteger.valueOf(1000 - 100), senderBalance);

        BigInteger receiverBalance = executionContext.getBalance(receiverAddress);
        Assert.assertNotNull(receiverBalance);
        Assert.assertEquals(BigInteger.valueOf(100), receiverBalance);

        Account sender2 = accountStore.getAccount(senderAddress);
        Assert.assertNotNull(sender2);
        Assert.assertEquals(BigInteger.valueOf(1000 - 100), sender2.getBalance());

        Account receiver2 = accountStore.getAccount(receiverAddress);
        Assert.assertNotNull(receiver2);
        Assert.assertEquals(BigInteger.valueOf(100), receiver2.getBalance());
    }

    @Test
    public void transferToAccountAndRollback() {
        AccountStore accountStore = new AccountStore(new Trie());
        Address senderAddress = FactoryHelper.createRandomAddress();
        Address receiverAddress = FactoryHelper.createRandomAddress();

        Account sender = new Account(BigInteger.valueOf(1000), 42);
        accountStore.putAccount(senderAddress, sender);

        TopExecutionContext executionContext = new TopExecutionContext(accountStore);

        executionContext.transfer(senderAddress, receiverAddress, BigInteger.valueOf(100));
        executionContext.rollback();

        BigInteger senderBalance = executionContext.getBalance(senderAddress);
        Assert.assertNotNull(senderBalance);
        Assert.assertEquals(BigInteger.valueOf(1000), senderBalance);

        BigInteger receiverBalance = executionContext.getBalance(receiverAddress);
        Assert.assertNotNull(receiverBalance);
        Assert.assertEquals(BigInteger.ZERO, receiverBalance);

        Account sender2 = accountStore.getAccount(senderAddress);
        Assert.assertNotNull(sender2);
        Assert.assertEquals(BigInteger.valueOf(1000), sender2.getBalance());

        Account receiver2 = accountStore.getAccount(receiverAddress);
        Assert.assertNotNull(receiver2);
        Assert.assertEquals(BigInteger.ZERO, receiver2.getBalance());
    }
}