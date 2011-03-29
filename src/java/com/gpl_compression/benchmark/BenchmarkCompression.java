package com.gpl_compression.benchmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.quicklz.QuickLZ15;
import com.quicklz.QuickLZ15Optimized;

public class BenchmarkCompression {

  private static final int NUM_REPS = 20000;

  public static void main(String[] args) throws Exception {
    byte[] data = new byte[32*1024];
    Random r = new Random(17);
    int startingPoint = r.nextInt();
    for (int i = 0; i < data.length / 4; i+=4) {
      encode(data, i, startingPoint);
      startingPoint += r.nextInt(255);
    }

    for (int trial = 0; trial < 1; trial++) {
      long start = System.currentTimeMillis();
      long end = System.currentTimeMillis();

      List<byte[]> compressed = new ArrayList<byte[]>();
      start = System.currentTimeMillis();
      for (int i = 0; i < NUM_REPS; i++) {
        compressed.add(QuickLZ15.compress(data, 1));
      }
      end = System.currentTimeMillis();
      System.err.println(compressed.size());
      System.out.println(trial + "\tQuickLZ15(1)\t" + throughput(data, start, end));

      List<Integer> compressedLengths = new ArrayList<Integer>();
      start = System.currentTimeMillis();
      byte[] outBuf = new byte[data.length + 400];
      for (int i = 0; i < NUM_REPS; i++) {
        compressedLengths.add(QuickLZ15Optimized.compress(data, outBuf, 1));
        System.out.println(compressedLengths.get(0));
      }
      end = System.currentTimeMillis();
      System.err.println(compressedLengths.size());
      System.out.println(trial + "\tQuickLZ15Optimized(1)\t" + throughput(data, start, end));
    }
  }

  private static String throughput(byte[] data, long start, long end) {
    long elapsedMs = end-start;
    double elapsedS = elapsedMs / 1000.0;
    return String.format("%f", data.length * NUM_REPS / elapsedS / 1024.0 / 1024.0);
  }

  private static void encode(byte[] data, int i, int v) {
    data[i] = (byte)((v >> 24) & 0xff);
    data[i+1] = (byte)((v >> 16) & 0xff);
    data[i+2] = (byte)((v >>  8) & 0xff);
    data[i+3] = (byte)((v >>  0) & 0xff);
  }

}
