/* Copyright (c) 2007-2017 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import static minesweeper.server.Square.SquareStatus.DUG;
import static minesweeper.server.Square.SquareStatus.FLAGGED;
import static minesweeper.server.Square.SquareStatus.UNTOUCHED;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import minesweeper.server.Square.SquareStatus;

/**
 * A threadsafe mutable data type that represents a Minesweeper board.
 */

public class Board {

  final private Square[][] squaresArray;
  final private int sizeY;
  final private int sizeX;
  final private int[][] neighbors = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0},
      {1, 1}};

  // Representation invariant
  //    squaresArray is not null.
  //    sizeY > 0.
  //    sizeX > 0.

  // Abstraction function
  //    Represents a Minesweeper board.

  // Safety from representation exposure
  //    sizeY and sizeX fields are final private and immutable.
  //    squaresArray is final private but the Squares objects it contains are mutable but they are only accessed by
  //    Board methods.
  //    The getSquaresArray method returns a reference to squaresArray but the reference is only used by Board methods and
  //    within unit tests and never returned to other classes.
  //

  // Thread safety argument
  //    All accesses to squaresArray happen within Board methods, which are guarded by Board's lock.
  //    sizeX and sizeY are final private immutable types and therefore threadsafe.
  //    neighbors is never mutated and is only referenced from Board methods for read access.

  private void checkRep() {
    assert sizeY > 0 : "Board sizeY should be greater than 0.";
    assert sizeX > 0 : "Board sizeX should be greater than 0.";
    assert squaresArray != null : "squaresArray should be not be null.";
  }

  /**
   * Constructor
   *
   * @param sizeX width of the board.
   * @param sizeY length of the board.
   */
  Board(final int sizeX, final int sizeY) {
    assert sizeY > 0 : "Board sizeY should be greater than 0.";
    assert sizeX > 0 : "Board sizeX should be greater than 0.";
    this.sizeY = sizeY;
    this.sizeX = sizeX;
    squaresArray = new Square[sizeY][sizeX];
    for (int y = 0; y < sizeY; y++) {
      for (int x = 0; x < sizeX; x++) {
        squaresArray[y][x] = new Square();
      }
    }
    placeBombsRandomly();
    checkRep();
  }

  /**
   * Constructor helper method.
   * Place bombs randomly on 25% of the squares of the board.
   */
  private void placeBombsRandomly() {
    final double bombsPercentage = 0.25;
    final int numberOfSquares = sizeY * sizeX;
    final int maxNumberOfBombs = (int) (numberOfSquares * bombsPercentage);
    for (int placedBombs = 0; placedBombs < maxNumberOfBombs; ) {
      int randomXcoordinate = new Random().nextInt(sizeX);
      int randomYcoordinate = new Random().nextInt(sizeY);
      if (!squaresArray[randomYcoordinate][randomXcoordinate].hasBomb()) {
        squaresArray[randomYcoordinate][randomXcoordinate].placeBomb();
        placedBombs++;
      }
    }
    checkRep();
  }

  /**
   * Create a Minesweeper board which can be:
   * 1) done by using a board configuration file.
   * 2) A board with default dimensions and randomly placed bombs.
   * 3) A board with user defined dimensions and randomly placed bombs.
   *
   * @param file is a board configuration file
   * @param sizeX is the width of the board in squares
   * @param sizeY is the length of the board in squares
   * @return a Minesweeper board
   */
  public static Board createBoard(final Optional<File> file, final int sizeX, final int sizeY) {
    if (!file.isPresent() && sizeX == -1 && sizeY == -1) {
      return new Board(MinesweeperServer.DEFAULT_SIZE, MinesweeperServer.DEFAULT_SIZE);
    } else if (file.isPresent()) {
      try (BufferedReader br = new BufferedReader(new FileReader(file.get()))) {
        return createCustomBoard(br);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    } else {
      return new Board(sizeX, sizeY);
    }
    throw new RuntimeException("createBoard() should never get down here.");
  }

  /**
   * Create a Minesweeper board using a board configuration file.
   *
   * @param br is a buffered character stream from which a pre-configured board is read
   * @return a pre-configured Minesweeper board
   * @throws IOException if a problem occurs reading a board configuration file
   */
  static Board createCustomBoard(BufferedReader br) throws IOException {
    Optional<Board> board = Optional.empty();
    String line = br.readLine();
    String[] boardDimensions = line.split(" ");
    int x = Integer.parseInt(boardDimensions[0]);
    int y = Integer.parseInt(boardDimensions[1]);
    board = Optional.of(new Board(x, y));
    int lineNumber = 0;
    while ((line = br.readLine()) != null) {
      placeBombsOnCustomBoard(board.get(), line, lineNumber);
      lineNumber++;
    }
    return board.orElseThrow(() -> new RuntimeException("No board was created"));
  }

  /**
   * Place bombs on a custom board.
   *
   * @param line is a line from a pre-configured board file
   * @param lineNumber is the linenumber from a pre-configured board file
   */
  private static void placeBombsOnCustomBoard(Board board, String line, int lineNumber) {
    Square[][] squaresArray = board.getSquaresArray();
    String[] bombInfo = line.split(" ");
    for (int i = 0; i < bombInfo.length; i++) {
      if (Integer.parseInt(bombInfo[i]) == 1) {
        squaresArray[lineNumber][i].placeBomb();
      } else {
        squaresArray[lineNumber][i].removeBomb();
      }
    }
  }

  /**
   * Flags a square if it's state is untouched
   *
   * @param x x-coordinate of square
   * @param y y-coordinate of square
   * @return a string representation of the board.
   */
  synchronized String flag(int x, int y) {
    if (!validateCoordinates(x, y)) {
      return look();
    }
    Square square = squaresArray[y][x];
    if (square.getSquareStatus() == UNTOUCHED) {
      square.setSquareStatus(FLAGGED);
    }
    checkRep();
    return look();
  }

  /**
   * Deflags a square if it's state is flagged.
   *
   * @param x x-coordinate of square
   * @param y y-coordinate of square
   * @return a string representation of the board.
   */
  synchronized String deflag(int x, int y) {
    if (!validateCoordinates(x, y)) {
      return look();
    }
    Square square = squaresArray[y][x];
    if (square.getSquareStatus() == FLAGGED) {
      square.setSquareStatus(UNTOUCHED);
    }
    checkRep();
    return look();
  }

  /**
   * Digs a square.
   *
   * @param x x-coordinate of square
   * @param y y-coordinate of square
   * @return a string representation of the board if the dug square does not contain a bomb,
   * otherwise returns "BOOM"
   */
  synchronized String dig(int x, int y) {
    if (!validateCoordinates(x, y)) {
      return look();
    }
    Square square = squaresArray[y][x];
    Square.SquareStatus squareStatus = square.getSquareStatus();
    if (square.hasBomb()) {
      square.removeBomb();
      square.setSquareStatus(DUG);
      checkRep();
      return "BOOM";
    } else if (squareStatus == UNTOUCHED || squareStatus == FLAGGED) {
      square.setSquareStatus(DUG);
      digNeighborsWithoutBombs(x, y);
    }
    checkRep();
    return look();
  }

  /**
   * Digs the neighbors (max. 8) of a square recursively if the don't have a bomb.
   *
   * @param x x-coordinate of square
   * @param y y-coordinate of square
   */
  private void digNeighborsWithoutBombs(int x, int y) {
    for (int i = 0; i < 8; i++) {
      int xNeighbor = x + neighbors[i][0];
      int yNeighbor = y + neighbors[i][1];
      if (validateCoordinates(xNeighbor, yNeighbor)) {
        Square square = squaresArray[yNeighbor][xNeighbor];
        if (!square.hasBomb() && square.getSquareStatus() != DUG) {
          square.setSquareStatus(DUG);
          digNeighborsWithoutBombs(xNeighbor, yNeighbor);
        }
      }
    }
  }

  /**
   * Converts board state to a string.
   *
   * @return a string representation of the board state.
   */
  synchronized String look() {
    StringBuilder sb = new StringBuilder(sizeY * sizeX + sizeX);
    for (int y = 0; y < sizeY; y++) {
      for (int x = 0; x < sizeX; x++) {
        SquareStatus squareStatus = squaresArray[y][x].getSquareStatus();
        if (squareStatus.equals(UNTOUCHED)) {
          sb.append("-");
        } else if (squareStatus.equals(FLAGGED)) {
          sb.append("F");
        } else {
          sb.append(countBombs(x, y));
        }
        if (x == sizeX - 1) {
          sb.append("\r\n");
        }
      }
    }
    checkRep();
    return sb.toString();
  }

  /**
   * Counts number of bombs that surround a dug square.
   * If the dug square has no neighbors with bombs then the
   * square is left empty.
   *
   * @param x x-coordinate of dug square
   * @param y y-coordinate of dug square
   * @return the number of bombs that surround a dug square but if the dug square has no neighbors
   * with bombs then the square is left empty.
   */
  private String countBombs(int x, int y) {
    int bombCount = 0;
    for (int i = 0; i < 8; i++) {
      int xNeighbor = x + neighbors[i][0];
      int yNeighbor = y + neighbors[i][1];
      if (validateCoordinates(xNeighbor, yNeighbor)) {
        if (squaresArray[yNeighbor][xNeighbor].hasBomb()) {
          bombCount++;
        }
      }
    }
    checkRep();
    return bombCount > 0 ? "" + bombCount : " ";
  }

  /**
   * See if the coordinates are within the bounds of the board.
   *
   * @param x x-coordinate
   * @param y y-coordinate
   * @return true in case the coordinates are within bounds else false
   */
  private boolean validateCoordinates(int x, int y) {
    return x >= 0 && x < sizeX && y >= 0 && y < sizeY;
  }

  /**
   * Get all the boards squares.
   */

  Square[][] getSquaresArray() {
    return squaresArray;
  }


}





















