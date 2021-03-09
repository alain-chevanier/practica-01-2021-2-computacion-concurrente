package unam.ciencias.computoconcurrente;

import java.util.Objects;

public class WordSearchAnswer implements Comparable<WordSearchAnswer> {
  private String word;
  private int row;
  private int column;
  private String direction;

  public WordSearchAnswer() {
  }

  public WordSearchAnswer(String word, int row, int column, String direction) {
    this.word = word;
    this.row = row;
    this.column = column;
    this.direction = direction;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof WordSearchAnswer)) return false;
    WordSearchAnswer that = (WordSearchAnswer) o;
    return row == that.row && column == that.column && Objects.equals(word, that.word) && Objects.equals(direction, that.direction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(word, row, column, direction);
  }

  @Override
  public String toString() {
    return word + " (" + row + ", " + column + ") " + direction;
  }

  @Override
  public int compareTo(WordSearchAnswer o) {
    int diff = this.word.compareTo(o.word);
    if (diff == 0) {
      return this.row == o.row ? this.column - o.column : this.row - o.row;
    }
    return diff;
  }
}
