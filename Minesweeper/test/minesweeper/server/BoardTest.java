/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static junit.framework.TestCase.assertTrue;
import static minesweeper.server.Square.SquareStatus.DUG;
import static minesweeper.server.Square.SquareStatus.FLAGGED;
import static minesweeper.server.Square.SquareStatus.UNTOUCHED;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import minesweeper.server.Square.SquareStatus;
import org.junit.Test;

/**
 * Unit tests for the minesweeper.server.Board class
 */

public class BoardTest {

  /* TEST STRATEGY:

      constructor
        check size
        check percentage of squares with bombs

      flag
        flag untouched square
        try to flag dug square
        try to flag with invalid x coordinate
        try to flag with invalid y coordinate

      deflag
        deflag flagged square
        try to deflag dug square
        try to deflag with x < 0
        try to deflag with y > board sizeY

      dig
        square has bomb
        board with no bombs
        board with one bomb
        board with two bombs
        square is flagged and board with no bombs
        square is dug
        try to dig with invalid x coordinate
        try to dig with invalid y coordinate

      createBoard
        create 15 by 15 board
        create default size board

      createCustomBoard
        create board with configuration file
        create board with configuration file and check bomb placement

 */

  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }

  /* Constructor -------------------------------------------------------------------------------------- */

  @Test
  public void testConstructor_checkSize() {
    Board board = new Board(5, 7);
    assertTrue(board.getSquaresArray()[0].length == 5);
    assertTrue(board.getSquaresArray().length == 7);
  }

  @Test
  public void testConstructor_numberOfBombs() {
    double bombsPercentage = 0.25;
    int sizeX = 5;
    int sizeY = 7;
    Board board = new Board(sizeX, sizeY);
    int expectedNumberOfBombs = (int) (sizeX * sizeY * bombsPercentage);
    int actualNumberOfBombs = 0;
    for (Square[] array : board.getSquaresArray()) {
      for (Square square : array) {
        if (square.hasBomb()) {
          actualNumberOfBombs++;
        }
      }

    }
    assertTrue(expectedNumberOfBombs == actualNumberOfBombs);
  }

  /* flag ------------------------------------------------------------------------------------------------- */

  @Test
  public void testFlag_squareIsUntouched() {
    Board board = new Board(5, 7);
    int x = 2;
    int y = 3;
    Square[][] squaresArray = board.getSquaresArray();

    board.flag(2, 3);

    SquareStatus actualSquareStatus = squaresArray[y][x].getSquareStatus();
    assertTrue(FLAGGED.equals(actualSquareStatus));
  }

  @Test
  public void testFlag_squareIsDug() {
    Board board = new Board(5, 7);
    int x = 2;
    int y = 3;
    Square[][] squaresArray = board.getSquaresArray();
    squaresArray[y][x].setSquareStatus(DUG);

    board.flag(x, y);

    SquareStatus actualSquareStatus = squaresArray[y][x].getSquareStatus();
    assertTrue(DUG.equals(actualSquareStatus));
  }

  @Test
  public void testFlag_xSmallerThan0() {
    Board board = new Board(3, 4);
    String expected = "---\r\n---\r\n---\r\n---\r\n";

    String actual = board.flag(-1, 1);

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testFlag_yGreaterThanLength() {
    Board board = new Board(4, 3);
    String expected = "----\r\n----\r\n----\r\n";

    String actual = board.flag(1, 5);

    assertTrue(expected.equals(actual));
  }

  /* deflag ------------------------------------------------------------------------------------------------- */

  @Test
  public void testDeflag_squareIsFlagged() {
    Board board = new Board(8, 6);
    int x = 2;
    int y = 3;
    Square[][] squaresArray = board.getSquaresArray();
    squaresArray[y][x].setSquareStatus(FLAGGED);

    board.deflag(x, y);

    SquareStatus actualSquareStatus = squaresArray[y][x].getSquareStatus();
    assertTrue(UNTOUCHED.equals(actualSquareStatus));
  }

  @Test
  public void testDeflag_squareIsDug() {
    Board board = new Board(5, 7);
    int x = 3;
    int y = 4;
    Square[][] squaresArray = board.getSquaresArray();
    squaresArray[y][x].setSquareStatus(DUG);

    board.deflag(x, y);

    SquareStatus actualSquareStatus = squaresArray[y][x].getSquareStatus();
    assertTrue(DUG.equals(actualSquareStatus));
  }

  @Test
  public void testDeflag_xSmallerThan0() {
    Board board = new Board(3, 3);
    Square[][] squares = board.getSquaresArray();
    squares[0][1].flagSquare();
    String expected = "-F-\r\n---\r\n---\r\n";

    String actual = board.deflag(-1, 1);

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testDeflag_yGreaterThanLength() {
    Board board = new Board(3, 2);
    Square[][] squares = board.getSquaresArray();
    String expected = "---\r\n---\r\n";

    String actual = board.deflag(1, 5);

    assertTrue(expected.equals(actual));
  }

  /* dig ------------------------------------------------------------------------------------------------- */

  @Test
  public void testDig_squareHasBomb() {
    Board board = new Board(5, 6);
    int x = 3;
    int y = 4;
    Square[][] squaresArray = board.getSquaresArray();
    squaresArray[y][x].placeBomb();

    String result = board.dig(x, y);

    assertTrue("BOOM".equals(result));
  }

  @Test
  public void testDig_boardWithNoBombs() {
    int sizeX = 3;
    int sizeY = 4;
    Board board = new Board(sizeX, sizeY);
    Square[][] squaresArray = board.getSquaresArray();
    for (int y = 0; y < sizeY; y++) {
      for (int x = 0; x < sizeX; x++) {
        squaresArray[y][x].removeBomb();
      }
    }
    String lookExpected = "   \r\n   \r\n   \r\n   \r\n";

    board.dig(2, 3);
    System.out.println(board.look());

    assertTrue(lookExpected.equals(board.look()));
  }

  @Test
  public void testDig_boardWithOneBomb() {
    int sizeX = 3;
    int sizeY = 4;
    Board board = new Board(sizeX, sizeY);
    Square[][] squaresArray = board.getSquaresArray();
    for (int y = 0; y < sizeY; y++) {
      for (int x = 0; x < sizeX; x++) {
        squaresArray[y][x].removeBomb();
      }
    }
    squaresArray[0][0].placeBomb();
    String lookExpected = "-1 \r\n11 \r\n   \r\n   \r\n";

    board.dig(2, 3);

    assertTrue(lookExpected.equals(board.look()));
  }

  @Test
  public void testDig_boardWithTwoBombs() {
    int sizeX = 3;
    int sizeY = 4;
    Board board = new Board(sizeX, sizeY);
    Square[][] squaresArray = board.getSquaresArray();
    for (int y = 0; y < sizeY; y++) {
      for (int x = 0; x < sizeX; x++) {
        squaresArray[y][x].removeBomb();
      }
    }
    squaresArray[0][0].placeBomb();
    squaresArray[1][1].placeBomb();
    String lookExpected = "-21\r\n2-1\r\n111\r\n   \r\n";

    board.dig(2, 3);

    assertTrue(lookExpected.equals(board.look()));
  }

  @Test
  public void testDig_squareFlaggedAndNoBombs() {
    int sizeX = 3;
    int sizeY = 4;
    Board board = new Board(sizeX, sizeY);
    Square[][] squaresArray = board.getSquaresArray();
    for (int y = 0; y < sizeY; y++) {
      for (int x = 0; x < sizeX; x++) {
        squaresArray[y][x].removeBomb();
      }
    }
    squaresArray[0][0].flagSquare();
    String lookExpected = "   \r\n   \r\n   \r\n   \r\n";

    board.dig(0, 0);

    assertTrue(lookExpected.equals(board.look()));
  }

  @Test
  public void testDig_squareIsDug() {
    Board board = new Board(9, 6);
    int x = 3;
    int y = 4;
    Square[][] squaresArray = board.getSquaresArray();
    squaresArray[y][x].removeBomb();
    squaresArray[y][x].digSquare();
    String expected = board.look();

    String result = board.dig(x, y);

    assertTrue(expected.equals(result));
  }

  @Test
  public void testDig_xSmallerThan0() {
    Board board = new Board(3, 3);
    Square[][] squares = board.getSquaresArray();
    squares[0][1].flagSquare();
    String expected = "-F-\r\n---\r\n---\r\n";

    String actual = board.dig(-1, 1);
    System.out.println(actual);

    assertTrue(expected.equals(actual));
  }

  @Test
  public void testDig_yGreaterThanLength() {
    Board board = new Board(3, 3);
    String expected = "---\r\n---\r\n---\r\n";

    String actual = board.dig(1, 5);

    assertTrue(expected.equals(actual));
  }

  /* createBoard  -------------------------------------------------------------------------------------- */

  @Test
  public void testCreateBoard_15By15() {
    Optional<File> file = Optional.empty();

    Board board = Board
        .createBoard(file, 15, 15);
    Square[][] squares = board.getSquaresArray();

    assertTrue(squares.length == 15);
    assertTrue(squares[0].length == 15);
  }

  @Test
  public void testCreateBoard_defaultSize() {
    Optional<File> file = Optional.empty();
    int defaultSize = MinesweeperServer.DEFAULT_SIZE;

    Board board = Board
        .createBoard(file, defaultSize, defaultSize);
    Square[][] squares = board.getSquaresArray();

    assertTrue(squares.length == defaultSize);
    assertTrue(squares[0].length == defaultSize);
  }

  /* createCustomBoard  -------------------------------------------------------------------------------------- */
  @Test
  public void testCreateCustomBoard() throws IOException {
    String boardConfig = "3 3\r\n1 0 0\r\n0 0 0\r\n0 0 0\r\n";
    BufferedReader br = new BufferedReader(new StringReader(boardConfig));
    Board board = Board.createCustomBoard(br);
    String lookExpected = "---\r\n---\r\n---\r\n";

    String lookActual = board.look();

    assertTrue(lookExpected.equals(lookActual));
  }

  @Test
  public void testCreateCustomBoard_checkBombPlacement() throws IOException {
    String boardConfig = "3 3\r\n1 0 0\r\n0 1 0\r\n0 0 1\r\n";
    BufferedReader br = new BufferedReader(new StringReader(boardConfig));
    Board board = Board.createCustomBoard(br);
    Square[][] squares = board.getSquaresArray();

    assertTrue(squares[0][0].hasBomb());
    assertTrue(squares[1][1].hasBomb());
    assertTrue(squares[2][2].hasBomb());
  }

}



























