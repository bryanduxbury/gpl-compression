package com.gpl_compression;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;

import com.quicklz.QuickLZ15;
import com.quicklz.QuickLZ15Optimized;

public class TestQuickLZ15Optimized extends TestCase {
  private static final List<byte[]> testCases = Arrays.asList(
      new byte[]{(byte) 175},
      new byte[]{0,1,2,3,4,5},
      randomBytes(32*1024),
      ascendingInts(32*1024)
  );
  private static final int SEED = 17;

  public void testIt() throws Exception {
    for (int i = 0; i < testCases.size(); i++) {
      byte[] testCase = testCases.get(i);

      // first, verify that original impl works
      byte[] origCompressed = QuickLZ15.compress(testCase, 1);
      byte[] origDecompresssed = QuickLZ15.decompress(origCompressed);
      assertEquals("test case #" + i, ByteBuffer.wrap(testCase), ByteBuffer.wrap(origDecompresssed));

      // now check the new impl
      byte[] newCompressed = new byte[testCase.length + 400];
      int lenCompressed = QuickLZ15Optimized.compress(testCase, newCompressed, 1);
      assertEquals("test case #" + i, ByteBuffer.wrap(origCompressed), ByteBuffer.wrap(newCompressed, 0, lenCompressed));
    }
  }


  private static byte[] ascendingInts(int count) {
    Random r = new Random(SEED);
    byte[] bytes = new byte[count];
    int startingPoint = r.nextInt();
    for (int i = 0; i < bytes.length / 4; i+=4) {
      encode(bytes, i, startingPoint);
      startingPoint += r.nextInt(255);
    }
    return bytes;
  }

  private static void encode(byte[] data, int i, int v) {
    data[i] = (byte)((v >> 24) & 0xff);
    data[i+1] = (byte)((v >> 16) & 0xff);
    data[i+2] = (byte)((v >>  8) & 0xff);
    data[i+3] = (byte)((v >>  0) & 0xff);
  }

  private static byte[] randomBytes(int count) {
    Random r = new Random(SEED);
    byte[] bytes = new byte[count];
    r.nextBytes(bytes);
    return bytes;
  }
}
