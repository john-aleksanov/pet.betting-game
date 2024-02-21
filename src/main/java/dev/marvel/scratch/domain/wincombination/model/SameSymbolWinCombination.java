package dev.marvel.scratch.domain.wincombination.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * Represents a winning combination where the same symbol must appear a specified number of times within the game matrix. This class
 * extends {@link AbstractWinCombination} to specifically handle win combinations based on the repetition of a single symbol.
 * <p>
 * Instances of this class are created based on configuration data, specifying the required count of the same symbol for the combination
 * to be considered a match.
 */
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class SameSymbolWinCombination extends AbstractWinCombination {

  private int count;

  /**
   * Constructs a SameSymbolWinCombination with a specific count requirement and reward multiplier.
   *
   * @param count The number of times the same symbol must appear for this combination to match.
   * @param value The reward multiplier applied when this winning combination is matched.
   * @throws ConfigurationParseException if the 'count' attribute is missing or not an integer in the configuration.
   */
  @JsonCreator
  public SameSymbolWinCombination(@JsonProperty("count") Integer count, @JsonProperty("reward_multiplier") Double value) {
    super(value, Group.SAME_SYMBOLS);
    this.count = Optional.of(count)
        .orElseThrow(() -> new ConfigurationParseException("Each same symbols 'win_combination' should have an integer 'count' attribute" +
            "."));
  }

  /**
   * Determines whether this winning combination matches based on the current game state and the specified symbol. A match occurs when the
   * number of appearances of the symbol in the game's cells equals the 'count' requirement.
   *
   * @param symbol The symbol to check for a winning match.
   * @param game The current game state, including the placement of symbols.
   * @return {@code true} if the symbol appears the required number of times, {@code false} otherwise.
   */
  @Override
  public boolean matches(Symbol symbol, Game game) {
    var symbolCount = game.getCells().values().stream()
        .filter(symbol::equals)
        .count();
    return symbolCount == count;
  }
}
