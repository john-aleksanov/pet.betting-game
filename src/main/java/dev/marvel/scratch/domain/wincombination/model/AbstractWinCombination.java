package dev.marvel.scratch.domain.wincombination.model;

import dev.marvel.scratch.domain.core.Cell;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.wincombination.parsers.WinCombinationsParser;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

/**
 * Represents a winning combination within the scratch game, characterized by a group of cells and a reward multiplier. Winning
 * combinations are defined in the 'win_combinations' attribute of the game configuration JSON and represent predefined patterns in the
 * game matrix that, when matched by the player's symbols, yield rewards based on their multiplier.
 * <p>
 * Win combinations can be categorized into different groups, such as horizontally linear, vertically linear, and diagonal patterns, each
 * with specific criteria for matching and associated rewards. The creation and parsing of these win combinations from the configuration
 * JSON are handled by the {@code WinCombinationsParser} class.
 * <p>
 * Examples of winning combinations:
 * 1) {X} same symbols in the game board matrix;
 * 2) same symbols vertically (in a single column).
 *
 * @see Cell for details on the game matrix's cells.
 * @see WinCombinationsParser for parsing 'win_combinations' from the configuration JSON.
 */
@SuperBuilder
@ToString(onlyExplicitlyIncluded = true)
public abstract class AbstractWinCombination implements WinCombination {

  @Getter
  protected final double rewardMultiplier;

  @Getter
  protected final Group group;

  @Getter
  @Setter
  @ToString.Include
  private String name;

  public AbstractWinCombination(Double rewardMultiplier, Group group) {
    this.rewardMultiplier = Optional.of(rewardMultiplier)
        .orElseThrow(() -> new ConfigurationParseException("Each 'win_combination' should have a double 'reward_multiplier' attribute."));
    this.group = Optional.of(group)
        .orElseThrow(() -> new ConfigurationParseException("Each 'win_combination' should have a 'group' attribute."));
  }

  public abstract boolean matches(Symbol symbol, Game game);

}
