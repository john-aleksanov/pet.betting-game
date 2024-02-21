package dev.marvel.scratch.domain.scorer;

import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.wincombination.MatchResult;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;

import java.util.Map;
import java.util.Set;

/**
 * Provides functionality for calculating the total score in a game based on the matched winning combinations associated with each symbol.
 * It considers the collective impact of all winning combinations matched to a symbol, applying their reward multipliers to the symbol's
 * base value to determine the symbol's total contribution to the score.
 */
public class Scorer {

  /**
   * Scores a single game's outcome represented as {@link MatchResult} - a set of matched win combinations for each standard symbol plus a
   * set of matched bonus symbols. It is the result of applying the
   * {@link dev.marvel.scratch.domain.wincombination.WinCombinationMatcher#match(Game)} method on an initialized {@link Game} instance.
   * <p>
   * The exact scoring rules are as follows:
   * <ul>
   *   <li>If one symbols matches more than winning combinations then reward should be multiplied. formula: (SYMBOL_1 *
   *   WIN_COMBINATION_1_FOR_SYMBOL_1 * WIN_COMBINATION_2_FOR_SYMBOL_1)</li>
   *   <li>If more than one symbols matches any winning combinations then reward should be summed. formula: (SYMBOL_1 *
   *   WIN_COMBINATION_1_FOR_SYMBOL_1 * WIN_COMBINATION_2_FOR_SYMBOL_1) + (SYMBOL_2 * WIN_COMBINATION_1_FOR_SYMBOL_2)</li>
   * </ul>
   *
   * @param matchResult the outcome of a single game
   * @return game score
   */
  public double score(int bet, MatchResult matchResult) {
    double standardSymbolsMultiplier = matchResult.matchedWinCombinations().entrySet().stream()
        .filter(entry -> entry.getKey().isStandard())
        .map(this::scoreStandardSymbol)
        .reduce(0d, Double::sum);
    return standardSymbolsMultiplier == 0d ? 0d : applyBonusSymbols(bet * standardSymbolsMultiplier, matchResult.matchedBonusSymbols());
  }

  private double scoreStandardSymbol(Map.Entry<Symbol, Set<WinCombination>> symbolWinCombinations) {
    var matchedWinCombinationsForSymbol = symbolWinCombinations.getValue();
    if (matchedWinCombinationsForSymbol.isEmpty()) return 0d;
    var totalMultiplier = matchedWinCombinationsForSymbol.stream()
        .map(WinCombination::getRewardMultiplier)
        .reduce(1d, (rm1, rm2) -> rm1 * rm2);
    var symbol = symbolWinCombinations.getKey();

    return symbol.apply(totalMultiplier);
  }

  private double applyBonusSymbols(double baseScore, Set<Symbol> matchedBonusSymbols) {
    return matchedBonusSymbols.stream()
        .reduce(baseScore, (currentScore, symbol) -> symbol.apply(currentScore), Double::sum);

  }
}
