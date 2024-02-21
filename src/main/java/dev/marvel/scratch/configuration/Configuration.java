package dev.marvel.scratch.configuration;

import dev.marvel.scratch.domain.probability.model.CellProbability;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * This class serves as a central repository for all configuration data required to initialize and run a game. It includes the setup for
 * the game board (rows and columns), the symbols used within the game, the probabilities for symbols to appear in each cell, and the
 * criteria for winning combinations.
 *
 * @see Symbol for the symbols used in the game.
 * @see CellProbability for the probabilities of symbols appearing in each cell.
 * @see WinCombination for the criteria that define winning combinations.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

  private int rows;
  private int columns;
  private Set<Symbol> symbols;
  private Set<CellProbability> cellProbabilities = new HashSet<>();
  private Set<WinCombination> winCombinations;

  public void addProbability(CellProbability probability) {
    cellProbabilities.add(probability);
  }
}
