package com.ajlopez.blockchain.store;

import com.ajlopez.blockchain.core.types.Hash;
import com.ajlopez.blockchain.state.Trie;
import com.ajlopez.blockchain.test.utils.FactoryHelper;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ajlopez on 21/01/2019.
 */
public class CodeStoreTest {
    @Test
    public void getUnknownCode() {
        CodeStore codeStore = new CodeStore(new Trie());

        Assert.assertNull(codeStore.getCode(FactoryHelper.createRandomHash()));
    }

    @Test
    public void putAndGetCode() {
        Hash codeHash = FactoryHelper.createRandomHash();
        byte[] code = FactoryHelper.createRandomBytes(42);

        CodeStore codeStore = new CodeStore(new Trie());

        codeStore.putCode(codeHash, code);
        Assert.assertArrayEquals(code, codeStore.getCode(codeHash));
    }
}
