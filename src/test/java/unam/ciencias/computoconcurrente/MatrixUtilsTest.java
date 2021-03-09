package unam.ciencias.computoconcurrente;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

class MatrixUtilsTest {
  MultiThreadedMatrixUtils multithreadedMatrixUtils;

  @Test
  void findMinimum() throws Exception {
    multithreadedMatrixUtils = new MultiThreadedMatrixUtils();
    int[][] matrix = {
      {4, 29, -6, 0},
      {15, 6, 0, 4},
      {25, 41, -10, 4},
      {0, 0, -1, 39},
    };

    assertEquals(-10, multithreadedMatrixUtils.findMinimum(matrix));
  }

  @Test
  void findMinimumConcurrent() throws Exception {
    multithreadedMatrixUtils = new MultiThreadedMatrixUtils(2);
    int[][] matrix = {
      {4, 29, -6, 0},
      {15, 6, 0, 4},
      {25, 41, -10, 4},
      {0, 0, -1, 39},
    };

    assertEquals(-10, multithreadedMatrixUtils.findMinimum(matrix));
  }

  @Test
  void findMinimumBigMatrix() throws Exception {
    multithreadedMatrixUtils = new MultiThreadedMatrixUtils();
    int rows = 5000, columns = 1000;
    int[][] matrix = new int[rows][columns];
    int minimum = fillMatrixAndReturnMinimumValue(matrix);

    assertEquals(minimum, multithreadedMatrixUtils.findMinimum(matrix));
  }

  @Test
  void findMinimumConcurrentBigMatrix() throws Exception {
    multithreadedMatrixUtils = new MultiThreadedMatrixUtils(4);
    int rows = 5000, columns = 1000;
    int[][] matrix = new int[rows][columns];
    int minimum = fillMatrixAndReturnMinimumValue(matrix);
    assertEquals(minimum, multithreadedMatrixUtils.findMinimum(matrix));
  }

  @Test
  /**
   *Matriz 2x2
   **/
  void multiply2() throws Exception {
    multithreadedMatrixUtils = new MultiThreadedMatrixUtils(2);
    int[][] matrix = {{2, 4}, {6, 9}};
    String matrixReal = Arrays.deepToString(matrixPow(matrix));
    String matrixProof = Arrays.deepToString(multithreadedMatrixUtils.multiply(matrix));
    assertEquals(matrixReal, matrixProof);
  }

  /**
   * Matriz 4x4
   **/
  @Test
  void multiply4() throws Exception {
    //multithreadedMatrixUtils = new MultiThreadedMatrixUtils(2);
    multithreadedMatrixUtils = new MultiThreadedMatrixUtils(4);

    int[][] matrix = {{2, 4, 8, 5}, {6, 9, 2, 8}, {5, 3, 2, 4}, {10, 2, 1, 7}};
    String matrixReal = Arrays.deepToString(matrixPow(matrix));
    String matrixProof = Arrays.deepToString(multithreadedMatrixUtils.multiply(matrix));

    assertEquals(matrixReal, matrixProof);
  }


  private int fillMatrixAndReturnMinimumValue(int[][] result) {
    int min = 2147483647;
    Random random = new Random();
    for (int[] row : result) {
      for (int c = 0; c < row.length; c++) {
        row[c] = random.nextInt();
        min = row[c] < min ? row[c] : min;
      }
    }

    return min;
  }

  /*
   *Cuadrado de una matriz
   */
  private int[][] matrixPow(int[][] matrixA) {
    int[][] c = new int[matrixA.length][matrixA.length];
    for (int i = 0; i < matrixA.length; i++) {
      for (int j = 0; j < matrixA.length; j++) {
        c[i][j] = 0;
        for (int k = 0; k < matrixA.length; k++) {
          c[i][j] += matrixA[i][k] * matrixA[k][j];
        }
      }
    }
    return c;
  }

}
