/********************************************************************************
 * CruiseControl, a Continuous Integration Toolkit
 * Copyright (c) 2005, ThoughtWorks, Inc.
 * 200 E. Randolph, 25th Floor
 * Chicago, IL 60601 USA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     + Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     + Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     + Neither the name of ThoughtWorks, Inc., CruiseControl, nor the
 *       names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ********************************************************************************/
package net.sourceforge.cruisecontrol.sampleproject.connectfour;

import junit.framework.TestCase;

public class PlayingStandTest extends TestCase {

    public void testFourConnected() throws GameOverException {
        PlayingStand stand = new PlayingStand();
        assertFalse(stand.areFourConnected());

        stand.dropRed(0);
        stand.dropBlack(6);
        assertFalse(stand.areFourConnected());

        stand.dropRed(0);
        stand.dropBlack(6);
        assertFalse(stand.areFourConnected());

        stand.dropRed(0);
        stand.dropBlack(6);
        assertFalse(stand.areFourConnected());

        stand.dropRed(0);
        assertTrue(stand.areFourConnected());

        try {
            stand.dropBlack(6);
            fail("Game is over, should be an exception");
        } catch (GameOverException expected) {
        }
    }

    public void testFourConnectedHorizontally() throws GameOverException {
        PlayingStand stand = new PlayingStand();
        createRedWinsHorizontally(stand);
        assertTrue(stand.areFourConnected());

        try {
            stand.dropBlack(6);
            fail("Game is over, should be an exception");
        } catch (GameOverException expected) {
        }
    }

    private void createRedWinsHorizontally(PlayingStand stand) {
        assertFalse(stand.areFourConnected());

        stand.dropRed(0);
        stand.dropBlack(6);
        assertFalse(stand.areFourConnected());

        stand.dropRed(1);
        stand.dropBlack(6);
        assertFalse(stand.areFourConnected());

        stand.dropRed(2);
        stand.dropBlack(6);
        assertFalse(stand.areFourConnected());

        stand.dropRed(3);
    }

    public void testFourConnectedDiagonally() throws GameOverException {
        PlayingStand stand = new PlayingStand();
        createRedWinsDiagonallyUpward(stand);
        assertTrue(stand.areFourConnected());

        try {
            stand.dropBlack(6);
            fail("Game is over, should be an exception");
        } catch (GameOverException expected) {
        }

        assertEquals(Chip.RED, stand.getWinner());
    }

    public void testStandControlsTurns() throws GameOverException {
        PlayingStand stand = new PlayingStand();
        stand.dropRed(1);
        try {
            stand.dropRed(1);
            fail("Expected an exception");
        } catch (OutOfTurnException expected) {
        }

        stand.dropBlack(1);
        try {
            stand.dropBlack(1);
            fail("Expected an exception");
        } catch (OutOfTurnException expected) {
        }

        stand.dropRed(1);
        stand.dropBlack(2);
    }

    public void testFullColumn() {
        PlayingStand stand = new PlayingStand();
        stand.dropRed(0);
        stand.dropBlack(0);
        stand.dropRed(0);
        stand.dropBlack(0);
        stand.dropRed(0);
        stand.dropBlack(0);

        try {
            stand.dropRed(0);
            fail("Expected an exception");
        } catch (FullColumnException expected) {
        }
    }

    public void testNonExistentColumn() {
        PlayingStand stand = new PlayingStand();
        try {
            stand.dropRed(-1);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }

        try {
            stand.dropBlack(-1);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }

        try {
            stand.dropRed(7);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }

        try {
            stand.dropRed(8);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }

        try {
            stand.dropRed(10000);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }

        try {
            stand.dropRed(Integer.MAX_VALUE);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }

        try {
            stand.dropRed(Integer.MIN_VALUE);
            fail("Expected an exception");
        } catch (InvalidColumnException expected) {
        }
    }

    public void testNoWinner() {
        PlayingStand stand = new PlayingStand();

        fillWholeStandWithoutWinner(stand);

        assertFalse(stand.areFourConnected());  
        assertTrue(stand.isGameOver());
        assertNull(stand.getWinner());

        try {
            stand.dropRed(0);
            fail("Expected an exception");
        } catch (GameOverException expected) {
        }
    }

    /**
     * 5|
     * 4|
     * 3| R
     * 2| B R
     * 1| R B R
     * 0| B R B R B
     * --------------
     * 0 1 2 3 4 5 6
     */
    public void testDownwardDiagonalWins() {
        PlayingStand stand = new PlayingStand();
        stand.dropRed(3);
        stand.dropBlack(2);
        stand.dropRed(2);
        stand.dropBlack(0);
        stand.dropRed(1);
        stand.dropBlack(1);
        stand.dropRed(1);
        stand.dropBlack(4);
        stand.dropRed(0);
        stand.dropBlack(0);
        stand.dropRed(0);

        assertTrue(stand.areFourConnected());
        assertTrue(stand.isGameOver());
    }

    public void testWinningPlacement() {
        PlayingStand stand = new PlayingStand();
        createRedWinsDiagonallyUpward(stand);

        PlayingStand.WinningPlacement placement = stand.getWinningPlacement();
        assertNotNull(placement);

        Cell startCell = placement.getStartingCell();
        assertEquals(0, startCell.getColumn());
        assertEquals(0, startCell.getRow());
        assertEquals(Direction.UPWARD_DIAGONAL, placement.getDirection());

        stand = new PlayingStand();
        createRedWinsHorizontally(stand);

        placement = stand.getWinningPlacement();
        assertEquals(0, placement.getStartingCell().getColumn());
        assertEquals(0, placement.getStartingCell().getRow());
        assertEquals(Direction.HORIZONTAL, placement.getDirection());
    }

    public void testNoWinningPlacementBeforeGameOver() {
        PlayingStand stand = new PlayingStand();

        try {
            stand.getWinningPlacement();
            fail("Expected an exception");
        } catch (GameNotOverException expected) {
        }

        fillWholeStandWithoutWinner(stand);
        try {
            stand.getWinningPlacement();
            fail("Expected an exception");
        } catch (StalemateException expected) {
        }
    }

    private void createRedWinsDiagonallyUpward(PlayingStand stand) {
        assertFalse(stand.areFourConnected());

        stand.dropRed(0);
        stand.dropBlack(1);
        assertFalse(stand.areFourConnected());

        stand.dropRed(1);
        stand.dropBlack(2);
        assertFalse(stand.areFourConnected());

        stand.dropRed(2);
        stand.dropBlack(3);
        stand.dropRed(2);
        assertFalse(stand.areFourConnected());

        stand.dropBlack(5);
        stand.dropRed(3);
        stand.dropBlack(3);
        stand.dropRed(3);
    }

    private void fillWholeStandWithoutWinner(PlayingStand stand) {
        stand.dropRed(0);
        stand.dropBlack(1);
        stand.dropRed(0);
        stand.dropBlack(1);
        stand.dropRed(0);
        stand.dropBlack(1);

        stand.dropRed(1);
        stand.dropBlack(0);
        stand.dropRed(1);
        stand.dropBlack(0);
        stand.dropRed(1);
        stand.dropBlack(0);

        stand.dropRed(2);
        stand.dropBlack(3);
        stand.dropRed(2);
        stand.dropBlack(3);
        stand.dropRed(2);
        stand.dropBlack(3);

        stand.dropRed(3);
        stand.dropBlack(2);
        stand.dropRed(3);
        stand.dropBlack(2);
        stand.dropRed(3);
        stand.dropBlack(2);

        stand.dropRed(4);
        stand.dropBlack(5);
        stand.dropRed(4);
        stand.dropBlack(5);
        stand.dropRed(4);
        stand.dropBlack(5);

        stand.dropRed(5);
        stand.dropBlack(4);
        stand.dropRed(5);
        stand.dropBlack(4);
        stand.dropRed(5);
        stand.dropBlack(4);

        stand.dropRed(6);
        stand.dropBlack(6);
        stand.dropRed(6);
        stand.dropBlack(6);
        stand.dropRed(6);
        stand.dropBlack(6);
    }
}
