package com.ajlopez.blockchain.test;

import com.ajlopez.blockchain.core.Account;
import com.ajlopez.blockchain.core.Block;
import com.ajlopez.blockchain.core.types.Address;
import com.ajlopez.blockchain.encoding.AccountEncoder;
import com.ajlopez.blockchain.encoding.BlockEncoder;
import com.ajlopez.blockchain.test.utils.FactoryHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ajlopez on 12/05/2019.
 */
public class WorldTest {
    @Test
    public void getUnknownAccount() {
        World world = new World();

        Assert.assertNull(world.getAccount("foo"));
    }

    @Test
    public void setAndGetAccount() {
        World world = new World();
        Account account = new Account();

        world.setAccount("acc1", account);

        Account result = world.getAccount("acc1");

        Assert.assertNotNull(result);
        Assert.assertArrayEquals(AccountEncoder.encode(account), AccountEncoder.encode(result));
    }

    @Test
    public void getAccountAddress() {
        World world = new World();
        Account account = new Account();

        world.setAccount("acc1", account);

        Address result = world.getAccountAddress("acc1");

        Assert.assertNotNull(result);
    }

    @Test
    public void getUnknownAccountAddress() {
        World world = new World();
        Account account = new Account();

        Address result = world.getAccountAddress("acc1");

        Assert.assertNull(result);
    }

    @Test
    public void getUnknownBlock() {
        World world = new World();

        Assert.assertNull(world.getBlock("blk1"));
    }

    @Test
    public void setAndGetBlock() {
        World world = new World();
        Block block = FactoryHelper.createBlocks(1).get(0);

        world.setBlock("blk1", block);

        Block result = world.getBlock("blk1");

        Assert.assertNotNull(result);
        Assert.assertArrayEquals(BlockEncoder.encode(block), BlockEncoder.encode(result));
    }
}
