package minesweeper.server;

import static junit.framework.TestCase.assertTrue;
import static minesweeper.server.Square.SquareStatus.DUG;
import static minesweeper.server.Square.SquareStatus.FLAGGED;
import static minesweeper.server.Square.SquareStatus.UNTOUCHED;
import static org.junit.Assert.assertFalse;

import minesweeper.server.Square;
import org.junit.Test;

public class SquareTest {

 /* TEST STRATEGY:

      constructor
        squareStatus is SquareStatus.UNTOUCHED

      getSquareStatus
        squareStatus is SquareStatus.UNTOUCHED
        squareStatus is SquareStatus.DUG

      setSquareStatus
        change status from SquareStatus.UNTOUCHED to SquareStatus.DUG

      hasBomb
        default value is false

      removeBomb
        remove bomb

      placeBomb
        place bomb

      flagSquare
        flag square that is UNTOUCHED
        try to flag square that is DUG

      deflagSquare
        deflag a square that is flagged

      digSquare
        dig a square that is untouched and has a bomb
 */


  @Test(expected = AssertionError.class)
  public void testAssertionsEnabled() {
    assert false; // make sure assertions are enabled with VM argument: -ea
  }


  /* Constructor -------------------------------------------------------------------------------------- */

  /* squareStatus is SquareStatus.UNTOUCHED */

  @Test
  public void testConstructor_squareStatusIsUntouched() {
    Square square = new Square();

    assertTrue(square.getSquareStatus().equals(UNTOUCHED));
  }

  /* getSquareStatus ----------------------------------------------------------------------------------- */

  @Test
  public void testGetSquareStatus_squareStatusIsUntouched() {
    Square square = new Square();

    assertTrue(square.getSquareStatus().equals(UNTOUCHED));
  }

  @Test
  public void testGetSquareStatus_squareStatusIsDug() {
    Square square = new Square();
    square.setSquareStatus(DUG);

    assertTrue(square.getSquareStatus().equals(DUG));
  }

  /* setSquareStatus ----------------------------------------------------------------------------------- */

  @Test
  public void testSetSquareStatus_fromUntouchedToDug() {
    Square square = new Square();

    square.setSquareStatus(DUG);

    assertTrue(square.getSquareStatus().equals(DUG));
  }


  /* hasBomb -------------------------------------------------------------------------------------------- */

  @Test
  public void testHasBomb_defaultIsFalse() {
    Square square = new Square();

    assertFalse(square.hasBomb());
  }

  /* removeBomb -------------------------------------------------------------------------------------------- */

  @Test
  public void testRemoveBomb() {
    Square square = new Square();

    square.removeBomb();

    assertFalse(square.hasBomb());
  }

  /* placeBomb  -------------------------------------------------------------------------------------------- */

  @Test
  public void testPlaceBomb() {
    Square square = new Square();

    square.placeBomb();

    assertTrue(square.hasBomb());
  }

  /* flag -------------------------------------------------------------------------------------------------- */

  @Test
  public void testFlagSquare_squareStatusIsUntouched() {
    Square square = new Square();

    square.flagSquare();

    assertTrue(square.getSquareStatus() == FLAGGED);
  }

  @Test
  public void testFlagSquare_squareStatusIsDug() {
    Square square = new Square();

    square.flagSquare();

    assertTrue(square.getSquareStatus() == FLAGGED);
  }

  /* deflag -------------------------------------------------------------------------------------------------- */

  @Test
  public void testDeflagSquare_squareIsFlagged() {
    Square square = new Square();
    square.flagSquare();

    square.deflagSquare();

    assertTrue(square.getSquareStatus() == UNTOUCHED);

  }

  /* digSquare ----------------------------------------------------------------------------------------------- */

  @Test
  public void testDigSquare_squareIsUntouchedAndHasBomb() {
    Square square = new Square();
    square.placeBomb();

    square.digSquare();

    assertTrue(square.getSquareStatus() == DUG);
    assertTrue(square.hasBomb() == false);
  }

}



