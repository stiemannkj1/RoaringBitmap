// https://github.com/RoaringBitmap/RoaringBitmap/issues/160
package org.roaringbitmap.map;

import org.openjdk.jmh.annotations.*;
import org.roaringbitmap.*;
import org.roaringbitmap.buffer.*;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class MapBenchmark {

   public static int inttointmap(int x) {
     return (x * 3) % 77_333_333;
   }

   @BenchmarkMode(Mode.AverageTime)
   @Benchmark
   public RoaringBitmap testMap(BenchmarkState benchmarkState) {
     final RoaringBitmap answer = new RoaringBitmap();
     benchmarkState.bitmap.forEach(new IntConsumer() {@Override
	public void accept(int value) {
      answer.add(inttointmap(value));
     }});
    return answer;
   }


   @BenchmarkMode(Mode.AverageTime)
   @Benchmark
   public RoaringBitmap testMapViaBitset(BenchmarkState benchmarkState) {
      final BitSet altRes = new java.util.BitSet();
      benchmarkState.bitmap.forEach(new IntConsumer() {@Override
	public void accept(int value) {
       altRes.set(inttointmap(value));
      }});
      return BitSetUtil.bitmapOf(altRes);
   }


   @State(Scope.Benchmark)
   public static class BenchmarkState {

      final RoaringBitmap bitmap  = new RoaringBitmap();

      public BenchmarkState() {
         bitmap.add(10L,100000L);
         for(long k = 100000L ; k < 2 * 100000L; k+= 2)
           bitmap.add((int)k);
         for(long k = 2 * 100000L ; k < 200 * 100000L; k+= 100000L)
           bitmap.add((int)k);
         bitmap.runOptimize();
      }
  }

}
