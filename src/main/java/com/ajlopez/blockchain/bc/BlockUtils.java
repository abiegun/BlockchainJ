package com.ajlopez.blockchain.bc;

import com.ajlopez.blockchain.core.Block;
import com.ajlopez.blockchain.core.BlockHeader;
import com.ajlopez.blockchain.core.types.BlockHash;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ajlopez on 19/01/2021.
 */
public class BlockUtils {
    private BlockUtils() {

    }

    public static Set<BlockHeader> getAncestorsHeaders(Block block, int depth, BlockStore blockStore) throws IOException {
        Set<BlockHeader> ancestors = new HashSet<>();
        BlockHash parentHash = block.getParentHash();

        for (int k = 0; k < depth; k++) {
            Block parent = blockStore.getBlock(parentHash);
            ancestors.add(parent.getHeader());

            if (parent.getNumber() == 0)
                break;

            parentHash = parent.getParentHash();
        }

        return ancestors;
    }

    public static Set<Block> getAncestorsBlocks(Block block, int depth, BlockStore blockStore) throws IOException {
        Set<Block> ancestors = new HashSet<>();
        BlockHash parentHash = block.getParentHash();

        for (int k = 0; k < depth; k++) {
            Block parent = blockStore.getBlock(parentHash);
            ancestors.add(parent);

            if (parent.getNumber() == 0)
                break;

            parentHash = parent.getParentHash();
        }

        return ancestors;
    }

    public static Set<BlockHeader> getAncestorsUncles(Block block, int depth, BlockStore blockStore) throws IOException {
        Set<BlockHeader> uncles = new HashSet<>();

        Set<Block> ancestors = getAncestorsBlocks(block, depth, blockStore);

        for (Block ancestor : ancestors)
            uncles.addAll(ancestor.getUncles());

        return uncles;
    }
}