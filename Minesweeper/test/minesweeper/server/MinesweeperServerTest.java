package minesweeper.server;

import static junit.framework.TestCase.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;

/**
 * Unit tests for the minesweeper.Board class
 */

public class MinesweeperServerTest {


  /* TEST STRATEGY:

      constructor







 */

  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }


  /* Constructor --------------------------------------------------------------------------------------------- */


  /* createBoard --------------------------------------------------------------------------------------------- */
  @Test
  public void testCreateBoard_defaultSizeBoard() throws IOException {
    Optional<File> file = Optional.empty();
    int defaultSize = 10;

    Board board = Board.createBoard(file, -1, -1);

    assertTrue(board.getSquaresArray().length == defaultSize);
  }

  @Test
  public void testCreateBoard_customSizeBoard() throws IOException {
    Optional<File> file = Optional.empty();
    int sizeX = 5;
    int sizeY = 10;

    Board board = Board.createBoard(file, sizeX, sizeY);

    assertTrue(board.getSquaresArray().length == sizeY);
    assertTrue(board.getSquaresArray()[0].length == sizeX);
  }



}
