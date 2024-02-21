package dev.marvel.scratch.domain.core;

import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;

import java.util.Map;
import java.util.Set;

/**
 * Represents the outcome of a game, encapsulating the total reward earned and the set of winning combinations matched for each symbol.
 * This record is immutable and serves as a compact way to transfer game result information.
 *
 * @param reward The total reward earned in the game, reflecting the cumulative impact of all matched winning combinations and any
 *               additional bonuses.
 * @param matchedWinCombinations A map where each key is a {@link Symbol} that participated in forming winning combinations, and the
 *                               value is a set of {@link WinCombination} objects that were matched by that symbol.
 */
public record GameResult(double reward, Map<Symbol, Set<WinCombination>> matchedWinCombinations) {

}
