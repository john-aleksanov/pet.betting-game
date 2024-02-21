package dev.marvel.scratch.domain.wincombination.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.RequiredArgsConstructor;

/**
 * Enumerates the groups of win combinations used in the scratch game to categorize different types of winning patterns. Each group
 * represents a specific pattern that symbols can form on the game matrix to qualify as a winning combination. Specifically, all winning
 * combinations in a single group have the same reward multiplier and, if a symbol matches multiple winning combinations in a single
 * group, only one is applied.
 * <p>
 * Example: symbol 'B' matches two winning combinations from the {@code VERTICALLY_LINEAR_SYMBOLS} group being present in all cells of
 * the first and third column of the game board. In this case, the corresponding reward multiplier is applied only once.
 *
 * @see WinCombination for how these groups are utilized in defining winning combinations.
 */
@RequiredArgsConstructor
public enum Group {

  /**
   * Represents win combinations where the same symbol appears on the board multiple times in any cell.
   */
  SAME_SYMBOLS("same_symbols"),

  /**
   * Represents win combinations formed by the same symbol appearing consecutively along a horizontal line.
   */
  HORIZONTALLY_LINEAR_SYMBOLS("horizontally_linear_symbols"),

  /**
   * Represents win combinations formed by the same symbol appearing consecutively along a vertical line.
   */
  VERTICALLY_LINEAR_SYMBOLS("vertically_linear_symbols"),

  /**
   * Represents win combinations formed by the same symbol appearing consecutively along a diagonal line from the left-top to the
   * right-bottom of the game matrix.
   */
  LTR_DIAGONALLY_LINEAR_SYMBOLS("ltr_diagonally_linear_symbols"),

  /**
   * Represents win combinations formed by the same symbol appearing consecutively along a diagonal line from the right-top to the
   * left-bottom of the game matrix.
   */
  RTL_DIAGONALLY_LINEAR_SYMBOLS("rtl_diagonally_linear_symbols");

  private final String value;

  /**
   * Factory method for converting a string value to its corresponding {@code Group} enum.
   * <p>
   * This method facilitates the parsing of win combination groups from configuration data, allowing for dynamic construction of game
   * rules based on JSON configuration.
   *
   * @param value The string identifier of the group.
   * @return The {@code Group} enum corresponding to the provided string value.
   * @throws ConfigurationParseException if the provided value does not match any known group.
   */
  @JsonCreator
  public static Group of(String value) {
    for (var element : Group.values()) {
      if (value.equals(element.value)) {
        return element;
      }
    }
    throw new ConfigurationParseException("Win combination group type " + value + " not supported");
  }

}
