package dev.marvel.scratch.domain.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import dev.marvel.scratch.domain.symbol.model.AbstractSymbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;


/**
 * Represents a single cell within the game's matrix, identified by its row and column.
 * <p>
 * A cell is a fundamental unit in the game board matrix where each cell can hold a {@code Symbol} (either standard or bonus). Symbols
 * within cells are used to determine winning combinations and calculate the final reward based on predefined probabilities and
 * combinations.
 *
 * @see Game
 * @see AbstractSymbol
 */
public record Cell(int row, int column) {

  /**
   * Creates a {@code Cell} instance from a string value representing the cell's coordinates.
   * <p>
   * The input string should follow the format "{row}:{column}", where {row} and {column} are integers representing the cell's position in
   * the game matrix. This method is used to parse cell coordinates from configurations defining winning combinations and their covered
   * areas within the game matrix.
   * <p>
   * Example of a valid input string: "0:1", which represents the cell at row 0, column 1.
   *
   * @param value A string representation of the cell's coordinates, formatted as "{row}:{column}".
   * @return A new {@code Cell} instance with the specified row and column.
   * @throws ConfigurationParseException if the input string does not follow the required format or cannot be parsed into integer values.
   */
  @JsonCreator
  public static Cell of(String value) {
    var parsed = value.split(":");
    try {
      return new Cell(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]));
    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
      throw new ConfigurationParseException("'probabilities.win_combinations.{}.covered_areas' should be string attributes of " +
          "format'%d:%d'");
    }
  }
}
