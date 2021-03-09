package unam.ciencias.computoconcurrente;

import java.util.LinkedList;
import java.util.List;

public class MultiThreadedWordSearch implements WordSearch {

  private final int threads;

  public MultiThreadedWordSearch() {
    this(1);
  }

  public MultiThreadedWordSearch(int threads) {
    this.threads = threads;

  }

  @Override
  public List<WordSearchAnswer> solve(char[][] board, List<String> words) throws InterruptedException {
    return new LinkedList<>();
  }


}
