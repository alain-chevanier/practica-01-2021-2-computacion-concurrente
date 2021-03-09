package unam.ciencias.computoconcurrente;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordSearchParser {

  public static char[][] loadBoardFromFile(String filename) {
    try (Stream<String> lines = getLinesFromResourceFile(filename)) {
      List<String> rows = lines.collect(Collectors.toList());
      char[][] board = new char[rows.size()][];
      int rowNum = 0;
      for (String row : rows) {
        board[rowNum++] = row.toCharArray();
      }
      return board;
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }
  }

  public static List<String> loadWordsFromFile(String filename) {
    try (Stream<String> lines = getLinesFromResourceFile(filename)) {
      return lines.collect(Collectors.toList());
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }
  }

  public static List<WordSearchAnswer> loadsAnswersFromFile(String filename) {
    try (Stream<String> lines = getLinesFromResourceFile(filename)) {
      return lines.map(line -> {
        Scanner scanner = new Scanner(line);
        WordSearchAnswer answer = new WordSearchAnswer();
        answer.setWord(scanner.next());
        answer.setRow(scanner.nextInt());
        answer.setColumn(scanner.nextInt());
        answer.setDirection(scanner.next());
        return answer;
      }).collect(Collectors.toList());
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }
  }

  private static Stream<String> getLinesFromResourceFile(String fileName) throws IOException {
    ClassLoader classLoader = WordSearchParser.class.getClassLoader();
    URL resource = classLoader.getResource(fileName);
    if (resource == null) {
      throw new IllegalArgumentException("file not found! " + fileName);
    } else {
      try {
        return Files.lines(Paths.get(resource.toURI()));
      } catch (URISyntaxException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
