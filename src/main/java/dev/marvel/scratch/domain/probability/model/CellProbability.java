package dev.marvel.scratch.domain.probability.model;

import dev.marvel.scratch.domain.symbol.model.Symbol;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.Map;

/**
 * Represents the probability distribution of symbols for a single cell in the scratch game matrix. Cell probabilities are defined in
 * the 'probabilities' attribute of the configuration JSON and encapsulate the logic for determining which symbol could appear in a cell
 * based on the defined probabilities for each symbol.
 *
 * @see Symbol for details on the symbols that can be assigned to cells.
 * @see dev.marvel.scratch.domain.probability.parsers.ProbabilitiesParserDelegate for how probabilities are parsed
 */
@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class CellProbability {

  private static final SecureRandom RANDOM = new SecureRandom();

  private final Integer row;
  private final Integer column;

  @EqualsAndHashCode.Exclude
  private final Map<Symbol, Integer> probabilities;

  /**
   * Determines and returns a symbol for the cell based on the defined probabilities. This method simulates a "spin" by randomly
   * selecting a symbol according to the probability distribution specified for the cell.
   *
   * @return The symbol selected based on the cell's probability distribution.
   * @throws IllegalStateException if there is an error in calculating the symbol due to an incorrect or inconsistent probability
   *                               distribution.
   */
  public Symbol spin() {
    int totalProbability = probabilities.values().stream()
        .reduce(0, Integer::sum);
    var random = RANDOM.nextInt(totalProbability);
    for (var probability : probabilities.entrySet()) {
      totalProbability = totalProbability - probability.getValue();
      if (totalProbability <= random) {
        return probability.getKey();
      }
    }
    throw new IllegalStateException(String.format("Internal error when spinning symbol for cell %s:%s", row, column));
  }

  public void addProbability(Symbol symbol, Integer value) {
    probabilities.put(symbol, value);
  }
}
