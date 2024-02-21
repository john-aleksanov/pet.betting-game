package dev.marvel.scratch.domain.wincombination.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.marvel.scratch.domain.core.Cell;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.experimental.SuperBuilder;

import java.util.Optional;
import java.util.Set;

/**
 * Defines a linear winning combination in the scratch game, which is a specific type of {@link AbstractWinCombination}. A linear
 * combination is defined by a set of cell groups where each group represents a line (horizontal, vertical, or diagonal) that symbols must
 * match to constitute a win.
 * <p>
 * This class handles the deserialization of such combinations from the game's configuration JSON, including their reward multiplier,
 * grouping criteria, and the specific cells that form each linear pattern.
 */
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinearWinCombination extends AbstractWinCombination {

  private Set<Set<Cell>> coveredAreas;

  /**
   * Constructs a LinearWinCombination with defined reward multiplier, group, and covered areas.
   *
   * @param value The reward multiplier for this winning combination.
   * @param group The group identifier that categorizes the win combination.
   * @param coveredAreas The sets of cell groups, each representing a linear pattern necessary for a match.
   * @throws ConfigurationParseException if the 'covered_areas' attribute is missing from the JSON configuration.
   */
  @JsonCreator
  public LinearWinCombination(@JsonProperty("reward_multiplier") double value, @JsonProperty("group") String group,
                              @JsonProperty("covered_areas") Set<Set<Cell>> coveredAreas) {
    super(value, Group.of(group));
    this.coveredAreas = Optional.of(coveredAreas)
        .orElseThrow(() -> new ConfigurationParseException("Each linear 'win_combination' should have a 'covered_areas' attribute."));
  }

  /**
   * Determines if this linear win combination matches the current game state for the given symbol. A match occurs if the symbol occupies
   * all cells in any one of the specified linear patterns.
   *
   * @param symbol The symbol to check for a winning match.
   * @param game The current game state, including the placement of symbols.
   * @return {@code true} if the symbol matches this linear pattern, {@code false} otherwise.
   */
  @Override
  public boolean matches(Symbol symbol, Game game) {
    return coveredAreas.stream()
        .anyMatch(ca -> ca.stream()
            .allMatch(cell -> game.getCells().get(cell).equals(symbol)));
  }
}
