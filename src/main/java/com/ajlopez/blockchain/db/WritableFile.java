package com.ajlopez.blockchain.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by abiegun
 */
public class WritableFile {
    protected final RandomAccessFile file;

    public WritableFile(String name) throws FileNotFoundException {
        file = new RandomAccessFile(name, "rw");
    }

    public void close() throws IOException {
        file.close();
    }

}
