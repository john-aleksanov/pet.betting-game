package dev.marvel.scratch.domain.core;


import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.probability.model.CellProbability;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import static org.assertj.core.api.Assertions.assertThat;

class GameTest {

  @Test
  void whenValidConfigThenInstantiatedCorrectly() {
    // GIVEN
    var probabilities = new HashMap<Symbol, Integer>();
    probabilities.put(SYMBOL_A, 1);
    probabilities.put(SYMBOL_B, 2);
    probabilities.put(SYMBOL_C, 3);
    probabilities.put(SYMBOL_D, 4);
    probabilities.put(SYMBOL_E, 5);
    probabilities.put(SYMBOL_F, 6);
    probabilities.put(SYMBOL_10x, 1);
    probabilities.put(SYMBOL_5x, 2);
    probabilities.put(SYMBOL_1000, 3);
    probabilities.put(SYMBOL_500, 4);
    probabilities.put(SYMBOL_MISS, 5);
    var cellProbabilities = build3by3CellProbabilities(probabilities);
    var configuration = Configuration.builder().cellProbabilities(cellProbabilities).build();

    // WHEN
    var game = new Game(configuration, 100);

    // THEN
    assertThat(game.getBet()).isEqualTo(100);
    assertThat(game.getCells().size()).isEqualTo(cellProbabilities.size());
  }

  @Test
  void whenSingleSymbolThenInstantiatedCorrectly() {
    // GIVEN
    var probabilities = Map.of(SYMBOL_A, 1);
    var cellProbabilities = build3by3CellProbabilities(probabilities);
    var configuration = Configuration.builder().cellProbabilities(cellProbabilities).build();

    // WHEN
    var game = new Game(configuration, 100);

    // THEN
    assertThat(game.getBet()).isEqualTo(100);
    assertThat(game.getCells().size()).isEqualTo(cellProbabilities.size());
    assertThat(game.getCells().values().stream().allMatch(SYMBOL_A::equals)).isTrue();
  }

  @Test
  void whenSingleSymbolThenMatrixBuiltCorrectly() {
    // GIVEN
    var probabilities = Map.of(SYMBOL_A, 1);
    var cellProbabilities = build3by3CellProbabilities(probabilities);
    var configuration = Configuration.builder().cellProbabilities(cellProbabilities).build();
    var game = new Game(configuration, 100);

    // WHEN
    var result = game.asMatrix();

    // THEN
    assertThat(result).containsExactly(
        List.of("A", "A", "A"),
        List.of("A", "A", "A"),
        List.of("A", "A", "A")
    );
  }

  private Set<CellProbability> build3by3CellProbabilities(Map<Symbol, Integer> probabilities) {
    return Set.of(
        new CellProbability(0, 0, probabilities),
        new CellProbability(0, 1, probabilities),
        new CellProbability(0, 2, probabilities),
        new CellProbability(1, 0, probabilities),
        new CellProbability(1, 1, probabilities),
        new CellProbability(1, 2, probabilities),
        new CellProbability(2, 0, probabilities),
        new CellProbability(2, 1, probabilities),
        new CellProbability(2, 2, probabilities)
    );
  }
}
