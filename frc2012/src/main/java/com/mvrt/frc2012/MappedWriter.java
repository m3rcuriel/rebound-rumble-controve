package com.mvrt.frc2012;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by lee on 12/17/15.
 */
public final class MappedWriter implements AutoCloseable {
  private final File outputFile;
  private final FileChannel channel;
  private final MappedByteBuffer buffer;

  public MappedWriter(String filename, long size) throws IOException {
    outputFile = new File(filename);
    channel = new RandomAccessFile(outputFile, "rw").getChannel();
    buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
  }

  public void write(byte data) {
    buffer.put(data);
  }

  public void write(byte[] data) {
    buffer.put(data);
  }

  public void writeShort(short data) {
    buffer.putShort(data);
  }

  public void writeInt(int data) {
    buffer.putInt(data);
  }

  public long getRemaining() {
    return buffer.remaining();
  }

  @Override
  public void close() {
    try {
      buffer.putInt(0xFFFFFFFF);
    } finally {
      try {
        buffer.force();
      } finally {
        try {
          channel.close();
        } catch (IOException e) {
          throw new RuntimeException("Failed to close channel", e);
        }
      }
    }
  }
}
