package minesweeper.server;

import static minesweeper.server.Square.SquareStatus.DUG;
import static minesweeper.server.Square.SquareStatus.FLAGGED;
import static minesweeper.server.Square.SquareStatus.UNTOUCHED;

/**
 * A not threadsafe mutable datatype that represents a square from a Minesweeper board.
 */

public class Square {

  private SquareStatus squareStatus;
  private boolean isBomb;

  //Abstraction function
  //   Represents a square of a minesweeper board.
  //Rep invariant
  //   SquareStatus is non-null.
  //Safety from exposure
  //   All fields are private and can only be changed by methods on the Square object itself.

  // Thread safety argument
  //    Square is not threadsafe.


  enum SquareStatus {
    UNTOUCHED, FLAGGED, DUG
  }

  /**
   * Constructor
   */
  Square() {
    this.squareStatus = UNTOUCHED;
    this.isBomb = false;
    checkRep();
  }

  /**
   * @return the square status
   */
  synchronized SquareStatus getSquareStatus() {
    checkRep();
    return squareStatus;
  }

  /**
   * Set the square status
   */
  synchronized void setSquareStatus(SquareStatus squareStatus) {
    this.squareStatus = squareStatus;
    checkRep();

  }

  /**
   * Check if a square has a isBomb.
   *
   * @return true or false
   */
  synchronized boolean hasBomb() {
    return isBomb;
  }

  /**
   * Remove isBomb from square.
   */
  synchronized void removeBomb() {
    isBomb = false;
  }

  /**
   * Place isBomb in square.
   */
  synchronized void placeBomb() {
    isBomb = true;
  }

  /**
   * Flag square.
   */
  synchronized void flagSquare() {
    if (squareStatus == UNTOUCHED) {
      squareStatus = FLAGGED;
    }
  }

  /**
   * Deflag square.
   */
  synchronized void deflagSquare() {
    if (squareStatus == FLAGGED) {
      squareStatus = UNTOUCHED;
    }
  }

  /**
   * Digs square and removes a bomb if present.
   */
  synchronized void digSquare() {
    if (squareStatus == DUG) {
      return;
    }
    if (squareStatus == UNTOUCHED && hasBomb() == true) {
      setSquareStatus(DUG);
      removeBomb();
    } else {
      setSquareStatus(DUG);
    }
    checkRep();
  }

  private void checkRep() {
    assert squareStatus != null : "square status cannot be null";
  }

}
