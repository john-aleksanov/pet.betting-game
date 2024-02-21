package dev.marvel.scratch.domain.wincombination.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.symbol.model.Symbol;

/**
 * Defines the contract for winning combinations in the scratch game. Winning combinations are patterns of symbols that, when matched in
 * a game, contribute to the player's score based on a defined reward multiplier. This interface supports polymorphic JSON
 * deserialization to handle different types of winning combinations, such as those based on the same symbol repetition or specific
 * linear arrangements of symbols.
 * <p>
 * Implementations must specify how a combination matches against the current game state and symbol, the associated reward multiplier,
 * the name identifying the combination, and the group category it belongs to.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "when")
@JsonSubTypes({
    @JsonSubTypes.Type(value = SameSymbolWinCombination.class, name = "same_symbols"),
    @JsonSubTypes.Type(value = LinearWinCombination.class, name = "linear_symbols")
})
public interface WinCombination {

  boolean matches(Symbol symbol, Game game);

  double getRewardMultiplier();

  String getName();

  Group getGroup();
}
