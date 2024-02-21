package dev.marvel.scratch.domain.wincombination;

import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;

import java.util.Map;
import java.util.Set;


/**
 * A record that encapsulates the results of matching symbols against winning combinations in a game. It provides a detailed account of
 * which symbols matched specific winning combinations and which bonus symbols were activated.
 *
 * @param matchedWinCombinations A map associating each symbol with a set of winning combinations it has matched. This representation
 *                               allows for a clear understanding of how each symbol contributed to the game's result.
 * @param matchedBonusSymbols A set of symbols that have been identified as bonus symbols during the game, regardless of whether they
 *                            contribute to winning combinations. Bonus symbols may provide additional game mechanics or rewards.
 */
public record MatchResult(Map<Symbol, Set<WinCombination>> matchedWinCombinations,
                          Set<Symbol> matchedBonusSymbols) {

}
