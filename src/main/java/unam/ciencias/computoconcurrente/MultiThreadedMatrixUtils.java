package unam.ciencias.computoconcurrente;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class MultiThreadedMatrixUtils implements MatrixUtils {
  private final int threads;

  public MultiThreadedMatrixUtils() {
    this(1);
  }

  public MultiThreadedMatrixUtils(int threads) {
    this.threads = threads;
  }

  @Override
  public int findMinimum(int[][] matrix) throws InterruptedException {
    int[] minimums = new int[this.threads];
    List<Thread> threadList = new ArrayList<>(this.threads);
    for (int i = 0; i < threads; i++) {
      int threadId = i;
            /*
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    taskFindMinimum(finalI, matrix);
                }
            };
             */
      threadList.add(new Thread(() -> taskFindMinimum(threadId, minimums, matrix)));
    }
    threadList.forEach(Thread::start);
        /*
        threadList.forEach(t -> t.start());
        for (Thread t : threadList) {
            t.start();
        }
        */
    for (Thread t : threadList) {
      t.join();
    }

    return Arrays.stream(minimums).min().orElseThrow(NoSuchElementException::new);
  }

  private void taskFindMinimum(int threadId, int[] minimums, int[][] matrix) {
    int min = Integer.MAX_VALUE;
    for (int i = threadId; i < matrix.length; i += this.threads) {
      min = Math.min(
        Arrays.stream(matrix[i]).min().orElse(Integer.MAX_VALUE),
        min
      );
    }
    minimums[threadId] = min;
  }

  @Override
  public int[][] multiply(int[][] matrixA) throws InterruptedException {
    int[][] matrixC = new int[matrixA.length][matrixA.length];
    List<Thread> threadList = new ArrayList<>(this.threads);
    for (int i = 0; i < threads; i++) {
      int threadId = i;
      threadList.add(new Thread(() -> taskMultiply(threadId, matrixA, matrixC)));
    }
    threadList.forEach(Thread::start);
    for (Thread t : threadList) {
      t.join();
    }
    return matrixC;
  }

  private void taskMultiply(int threadId, int[][] matrixIn, int[][] matrixOut) {
    int filas = matrixIn.length;
    int[] vector = new int[filas];
    for (int numFila = threadId; numFila < matrixIn.length; numFila += this.threads) {
      for (int col0 = 0; col0 < filas; col0++) {
        int c = 0;
        for (int filaB = 0; filaB < filas; filaB++) {
          c += (matrixIn[numFila][filaB] * matrixIn[filaB][col0]);
        }
        vector[col0] = c;
      }
      for (int i = 0; i < matrixOut.length; i++) {
        matrixOut[numFila][i] = vector[i];
      }

    }

  }
}
