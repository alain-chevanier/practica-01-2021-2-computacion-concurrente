package unam.ciencias.computoconcurrente;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultiThreadedWordSearch implements WordSearch {

  private final int threads;
  private final ArrayList<List<WordSearchAnswer>> answers;

  public MultiThreadedWordSearch() {
    this(1);
  }

  public MultiThreadedWordSearch(int threads) {
    this.threads = threads;
    answers = new ArrayList<>(threads);
    for (int i = 0; i < threads; i++) {
      answers.add(null);
    }
  }

  @Override
  public List<WordSearchAnswer> solve(char[][] board, List<String> words) throws InterruptedException {
    runThreads(board, words);
    return joinSearchResult();
  }

  private void runThreads(char[][] board, List<String> words) throws InterruptedException {
    List<Thread> threadList = IntStream.range(0, threads)
      .mapToObj(i -> new Thread(() -> searchTask(i, board, words)))
      .collect(Collectors.toList());
    threadList.forEach(Thread::start);
    for (Thread t : threadList) {
      t.join();
    }
  }

  private List<WordSearchAnswer> joinSearchResult() {
    List<WordSearchAnswer> searchResult = new ArrayList<>();
    for (List<WordSearchAnswer> l : answers) {
      searchResult.addAll(l);
    }
    searchResult.sort(WordSearchAnswer::compareTo);
    return searchResult;
  }

  private void searchTask(int index, char[][] board, List<String> words) {
    List<WordSearchAnswer> searchResults = new ArrayList<>();
    searchResults.addAll(horizontalTask(index, board, words));
    searchResults.addAll(verticalTask(index, board, words));
    searchResults.addAll(rightDiagonalTask(index, board, words));
    searchResults.addAll(leftDiagonalTask(index, board, words));
    answers.set(index, searchResults);
  }

  private List<WordSearchAnswer> horizontalTask(int index, char[][] board, List<String> words) {
    List<WordSearchAnswer> searchResults = new ArrayList<>();
    for (int rowNum = index; rowNum < board.length; rowNum += threads) {
      String row = new String(board[rowNum]);
      int finalElem = rowNum;
      searchResults.addAll(
        searchWord(
            row,
            words,
            "E",
            "O",
            i -> finalElem,
            i -> i
          )
      );
    }
    return searchResults;
  }

  private List<WordSearchAnswer> verticalTask(int index, char[][] board, List<String> words) {
    List<WordSearchAnswer> searchResults = new ArrayList<>();
    for (int colNum = index; colNum < board[0].length; colNum += threads) {
      String row = getColumnString(board, colNum);
      int finalColNum = colNum;
      searchResults.addAll(
        searchWord(
          row,
          words,
          "S",
          "N",
          i -> i,
          i -> finalColNum
        )
      );
    }
    return searchResults;
  }

  private String getColumnString(char[][] board, int column) {
    StringBuilder builder = new StringBuilder(board.length);
    for (char[] chars : board) {
      builder.append(chars[column]);
    }
    return builder.toString();
  }

  private List<WordSearchAnswer> rightDiagonalTask(int index, char[][] board, List<String> words) {
    return diagonalTask(index, board, words, false);
  }

  private List<WordSearchAnswer> leftDiagonalTask(int index, char[][] board, List<String> words) {
    return diagonalTask(index, board, words, true);
  }

  private List<WordSearchAnswer> diagonalTask(int index, char[][] board, List<String> words, boolean isLeft) {
    List<WordSearchAnswer> searchResults = new ArrayList<>();

    for (int diagonalNum = index; diagonalNum < board.length + board[0].length - 1; diagonalNum += threads) {
      int rowNum = calcDiagonalStartingRowNum(board, diagonalNum, isLeft ? 0 : board.length - 1);
      int colNum = calcDiagonalStartingColumnNum(board, diagonalNum);
      String row = getDiagonalString(board, rowNum, colNum, isLeft ? 1 : -1);
      searchResults.addAll(
        searchWord(
          row,
          words,
          isLeft ? "SE" : "NE",
          isLeft ? "NO" : "SO",
          i -> isLeft ? rowNum + i : rowNum - i,
          i -> colNum + i
        )
      );
    }

    return searchResults;
  }

  private int calcDiagonalStartingRowNum(char[][] board, int diagonalNum, int defaultRow) {
    return diagonalNum < board.length ? diagonalNum : defaultRow;
  }

  private int calcDiagonalStartingColumnNum(char[][] board, int diagonalNum) {
    return diagonalNum < board.length ? 0 : diagonalNum - board.length + 1;
  }

  private String getDiagonalString(char[][] board, int rowNum, int colNum, int rowAcu) {
    StringBuilder builder = new StringBuilder(board.length);
    while (rowNum >= 0 && rowNum < board.length && colNum < board[0].length) {
      builder.append(board[rowNum][colNum]);
      rowNum += rowAcu;
      colNum++;
    }
    return builder.toString();
  }

  private List<WordSearchAnswer> searchWord(
    String row,
    List<String> words,
    String direction,
    String reversedDirection,
    Function<Integer,Integer> transformRow,
    Function<Integer,Integer> transformColumn
  ) {
    List<WordSearchAnswer> results = new ArrayList<>();
    String reversedRow = new StringBuffer(row).reverse().toString();

    words.forEach(word -> {
      int i = row.indexOf(word);
      if (i != -1) {
        results.add(new WordSearchAnswer(word, transformRow.apply(i), transformColumn.apply(i), direction));
      }

      int iReversed = reversedRow.indexOf(word);
      if (iReversed != -1) {
        int j = row.length() - iReversed - 1;
        results.add(new WordSearchAnswer(word, transformRow.apply(j), transformColumn.apply(j), reversedDirection));
      }
    });

    return results;
  }
}
