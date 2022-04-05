package com.ajlopez.blockchain.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by ajlopez on 20/10/2019.
 */
public class ValueFile extends WritableFile {

    public ValueFile(String name) throws FileNotFoundException {
        super(name);
    }

    public long writeValue(byte[] value) throws IOException {
        long length = this.file.length();

        this.file.seek(length);
        this.file.write(value);

        return length;
    }

    public int readValue(long position, byte[] buffer) throws IOException {
        this.file.seek(position);

        return this.file.read(buffer);
    }
}
