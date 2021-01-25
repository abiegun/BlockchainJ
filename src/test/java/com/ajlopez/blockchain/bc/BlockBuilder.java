package com.ajlopez.blockchain.bc;

import com.ajlopez.blockchain.core.Block;
import com.ajlopez.blockchain.core.BlockHeader;
import com.ajlopez.blockchain.test.utils.FactoryHelper;

import java.util.List;

/**
 * Created by ajlopez on 23/01/2021.
 */
public class BlockBuilder {
    private long number;
    private Block parent;
    private List<BlockHeader> uncles;

    public BlockBuilder number(long number) {
        this.number = number;

        return this;
    }

    public BlockBuilder parent(Block parent) {
        this.parent = parent;

        return this;
    }

    public BlockBuilder uncles(List<BlockHeader> uncles) {
        this.uncles = uncles;

        return this;
    }

    public Block build() {
        if (this.parent != null)
            return new Block(FactoryHelper.createBlockHeader(this.parent), this.uncles, null);

        return new Block(FactoryHelper.createBlockHeader(this.number), this.uncles, null);
    }
}
