package com.ajlopez.blockchain.db;

import com.ajlopez.blockchain.test.utils.FactoryHelper;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

/**
 * Created by ajlopez on 30/10/2019.
 */
public class KeyValueDbTest {
    // https://www.infoq.com/news/2009/07/junit-4.7-rules
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private void deleteFiles(String name) {
        new File(name+".values").delete();
        new File(name+".keys").delete();
    }
    @Test
    public void saveAndRetrieveKeyValue() throws IOException {
        String NAME = "data0";
        KeyValueDb keyValueDb = new KeyValueDb(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(32);
            byte[] value = FactoryHelper.createRandomBytes(42);

            keyValueDb.setValue(key, value);

            byte[] result = keyValueDb.getValue(key);

            Assert.assertNotNull(result);
            Assert.assertArrayEquals(value, result);
        } finally {
            keyValueDb.close();
            deleteFiles(NAME);
        }
    }

    @Test
    public void saveTwiceAndRetrieveKeyValue() throws IOException {
        String NAME = "data1";
        KeyValueDb keyValueDb = new KeyValueDb(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(32);
            byte[] value = FactoryHelper.createRandomBytes(42);

            keyValueDb.setValue(key, value);
            keyValueDb.setValue(key, value);

            byte[] result = keyValueDb.getValue(key);

            Assert.assertNotNull(result);
            Assert.assertArrayEquals(value, result);
        } finally {
            keyValueDb.close();
            deleteFiles(NAME);
        }
    }

    @Test
    public void cannotChangeValueForKey() throws IOException {
        String NAME = "data2";
        KeyValueDb keyValueDb = new KeyValueDb(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(32);
            byte[] value = FactoryHelper.createRandomBytes(42);
            byte[] value2 = FactoryHelper.createRandomBytes(42);

            keyValueDb.setValue(key, value);

            exception.expect(IllegalStateException.class);
            exception.expectMessage("cannot change value for key");
            keyValueDb.setValue(key, value2);
        } finally {
            keyValueDb.close();
            deleteFiles(NAME);
        }

    }

    @Test
    public void retrieveUnknownValueAsNull() throws IOException {
        String NAME = "data3";
        KeyValueDb keyValueDb = new KeyValueDb(NAME, 32);
        try {
            byte[] key = FactoryHelper.createRandomBytes(32);

            Assert.assertNull(keyValueDb.getValue(key));
        } finally {
            keyValueDb.close();
            deleteFiles(NAME);
        }

    }

}
