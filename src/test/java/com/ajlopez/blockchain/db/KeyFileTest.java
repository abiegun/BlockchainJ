package com.ajlopez.blockchain.db;

import com.ajlopez.blockchain.test.utils.FactoryHelper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

/**
 * Created by ajlopez on 21/10/2019.
 */
public class KeyFileTest {
    // https://www.infoq.com/news/2009/07/junit-4.7-rules
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void writeAndReadKey() throws IOException {
        final String NAME = "kftest1.data";
        KeyFile keyFile = new KeyFile(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(32);

            Assert.assertFalse(keyFile.containsKey(key));

            keyFile.writeKey(key, 0L, 42);

            ValueInfo result = keyFile.readKey(key);

            Assert.assertNotNull(result);
            Assert.assertEquals(0L, result.position);
            Assert.assertEquals(42, result.length);
            Assert.assertTrue(keyFile.containsKey(key));
        } finally {
            keyFile.close();
            new File(NAME).delete();
        }

    }

    @Test
    public void cannotWriteSameKeyTwice() throws IOException {
        final String NAME = "kftest2.data";
        KeyFile keyFile = new KeyFile(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(32);

            keyFile.writeKey(key, 0L, 42);
            exception.expect(IllegalStateException.class);
            exception.expectMessage("key already exists");
            keyFile.writeKey(key, 0L, 42);
        } finally {
            keyFile.close();
            new File(NAME).delete();
        }
    }

    @Test
    public void writeAndReadThreeKeys() throws IOException {
        final String NAME = "kftest3.data";
        KeyFile keyFile = new KeyFile(NAME, 32);
        try {
            byte[] key1 = FactoryHelper.createRandomBytes(32);
            byte[] key2 = FactoryHelper.createRandomBytes(32);
            byte[] key3 = FactoryHelper.createRandomBytes(32);

            keyFile.writeKey(key1, 0L, 42);
            keyFile.writeKey(key2, 42L, 42 * 2);

            ValueInfo result1 = keyFile.readKey(key1);

            Assert.assertNotNull(result1);
            Assert.assertEquals(0L, result1.position);
            Assert.assertEquals(42, result1.length);

            keyFile.writeKey(key3, 42L * 2, 42 * 3);

            ValueInfo result2 = keyFile.readKey(key2);

            Assert.assertNotNull(result2);
            Assert.assertEquals(42L, result2.position);
            Assert.assertEquals(42 * 2, result2.length);

            ValueInfo result3 = keyFile.readKey(key3);

            Assert.assertNotNull(result3);
            Assert.assertEquals(42L * 2, result3.position);
            Assert.assertEquals(42 * 3, result3.length);
        } finally {
            keyFile.close();
            new File(NAME).delete();
        }
    }

    @Test
    public void writeThreeKeysCloseAndReopenFileAndReadTheKeys() throws IOException {
        final String NAME = "kftest4.data";
        KeyFile keyFile = new KeyFile(NAME, 32);
        try {
            byte[] key1 = FactoryHelper.createRandomBytes(32);
            byte[] key2 = FactoryHelper.createRandomBytes(32);
            byte[] key3 = FactoryHelper.createRandomBytes(32);

            keyFile.writeKey(key1, 0L, 42);
            keyFile.writeKey(key2, 42L, 42 * 2);
            keyFile.writeKey(key3, 42L * 2, 42 * 3);

            keyFile.close();

            keyFile = new KeyFile(NAME, 32);

            ValueInfo result1 = keyFile.readKey(key1);

            Assert.assertNotNull(result1);
            Assert.assertEquals(0L, result1.position);
            Assert.assertEquals(42, result1.length);

            ValueInfo result2 = keyFile.readKey(key2);

            Assert.assertNotNull(result2);
            Assert.assertEquals(42L, result2.position);
            Assert.assertEquals(42 * 2, result2.length);

            ValueInfo result3 = keyFile.readKey(key3);

            Assert.assertNotNull(result3);
            Assert.assertEquals(42L * 2, result3.position);
            Assert.assertEquals(42 * 3, result3.length);
        } finally {
            keyFile.close();
            new File(NAME).delete();
        }
    }

    @Test
    public void cannotWriteNullKey() throws IOException {
        final String NAME = "kftest5.data";
        KeyFile keyFile = new KeyFile(NAME, 32);
        try {
            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("invalid key");

            keyFile.writeKey(null, 0L, 42);
        } finally {
            keyFile.close();
            new File(NAME).delete();
        }
    }

    @Test
    public void cannotWriteKeyWithInvalidLength() throws IOException {
        final String NAME = "kftest6.data";
        KeyFile keyFile = new KeyFile(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(42);

            exception.expect(IllegalArgumentException.class);
            exception.expectMessage("invalid key");

            keyFile.writeKey(key, 0L, 42);
        } finally {
            keyFile.close();
            new File(NAME).delete();
        }
    }
}
