package dev.marvel.scratch.domain.wincombination;


import dev.marvel.scratch.domain.core.Cell;
import dev.marvel.scratch.domain.core.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static dev.marvel.scratch.domain.TestUtils.HLS_WC;
import static dev.marvel.scratch.domain.TestUtils.LTR_WC;
import static dev.marvel.scratch.domain.TestUtils.RTL_WC;
import static dev.marvel.scratch.domain.TestUtils.SAME3WC;
import static dev.marvel.scratch.domain.TestUtils.SAME4WC;
import static dev.marvel.scratch.domain.TestUtils.SAME5WC;
import static dev.marvel.scratch.domain.TestUtils.SAME6WC;
import static dev.marvel.scratch.domain.TestUtils.SAME7WC;
import static dev.marvel.scratch.domain.TestUtils.SAME8WC;
import static dev.marvel.scratch.domain.TestUtils.SAME9WC;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_1000;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_10x;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_500;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_5x;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_A;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_B;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_C;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_D;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_E;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_F;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_MISS;
import static dev.marvel.scratch.domain.TestUtils.VLS_WC;
import static dev.marvel.scratch.domain.wincombination.model.Group.HORIZONTALLY_LINEAR_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.LTR_DIAGONALLY_LINEAR_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.RTL_DIAGONALLY_LINEAR_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.SAME_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.VERTICALLY_LINEAR_SYMBOLS;
import static org.assertj.core.api.Assertions.assertThat;

class WinCombinationMatcherTest {

  private WinCombinationMatcher uut;

  @BeforeEach
  void setUp() {
    var symbols = Set.of(SYMBOL_A, SYMBOL_B, SYMBOL_C, SYMBOL_D, SYMBOL_E, SYMBOL_F,
        SYMBOL_10x, SYMBOL_5x, SYMBOL_1000, SYMBOL_500, SYMBOL_MISS);
    var winCombinationsByGroup = Map.of(
        SAME_SYMBOLS, Set.of(SAME3WC, SAME4WC, SAME5WC, SAME6WC, SAME7WC, SAME8WC, SAME9WC),
        HORIZONTALLY_LINEAR_SYMBOLS, Set.of(HLS_WC),
        VERTICALLY_LINEAR_SYMBOLS, Set.of(VLS_WC),
        LTR_DIAGONALLY_LINEAR_SYMBOLS, Set.of(LTR_WC),
        RTL_DIAGONALLY_LINEAR_SYMBOLS, Set.of(RTL_WC)
    );
    uut = new WinCombinationMatcher(symbols, winCombinationsByGroup);
  }

  @Test
  void test() {
    // GIVEN
    var game = new Game(Map.of(
        new Cell(0, 0), SYMBOL_A, new Cell(0, 1), SYMBOL_A, new Cell(0, 2), SYMBOL_B,
        new Cell(1, 0), SYMBOL_A, new Cell(1, 1), SYMBOL_1000, new Cell(1, 2), SYMBOL_B,
        new Cell(2, 0), SYMBOL_A, new Cell(2, 1), SYMBOL_A, new Cell(2, 2), SYMBOL_B
    ), 100);
    var expected = new MatchResult(
        Map.of(SYMBOL_A, Set.of(SAME5WC, VLS_WC), SYMBOL_B, Set.of(SAME3WC, VLS_WC)),
        Set.of(SYMBOL_1000)
    );

    // WHEN
    var result = uut.match(game);

    // THEN
    assertThat(result).isEqualTo(expected);
  }
}