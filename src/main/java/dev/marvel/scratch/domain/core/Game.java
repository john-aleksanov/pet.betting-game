package dev.marvel.scratch.domain.core;

import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Represents a game instance, including the game's current state such as the matrix of symbols and the betting amount placed by the user.
 * <p>
 * This class is responsible for initializing the game board based on the provided configuration, which includes generating symbols for
 * each cell in the game matrix according to predefined probabilities. Winning combinations are evaluated against this matrix to
 * determine the outcome of the game.
 * </p>
 *
 * @see Symbol
 * @see Cell
 * @see Configuration
 */
@Getter
public class Game {

  private final Map<Cell, Symbol> cells;
  private final int bet;

  /**
   * Constructs a new game instance using the specified configuration and betting amount.
   * <p>
   * During construction, the game matrix is initialized by assigning symbols to each cell based on the cell probabilities defined in the
   * configuration. Each cell's symbol is determined through a randomized process that respects the standard probabilities associated with
   * each symbol for that specific cell and the bonus probabilities, which are the same for all cells.
   * <p>
   * The betting amount is set at the beginning and affects the calculation of the final reward if winning combinations are matched.
   *
   * @param configuration The game configuration specifying cell probabilities, symbols, and winning combinations.
   * @param bet           The betting amount placed by the user for this game instance.
   */
  public Game(Configuration configuration, int bet) {
    this.bet = bet;
    cells = new HashMap<>();
    var cellProbabilities = configuration.getCellProbabilities();
    cellProbabilities.forEach(cp -> {
      var cell = new Cell(cp.getRow(), cp.getColumn());
      var symbol = cp.spin();
      cells.put(cell, symbol);
    });
  }

  public Game(Map<Cell, Symbol> cells, int bet) {
    this.cells = cells;
    this.bet = bet;
  }

  /**
   * Converts the current cell-to-symbol mapping into a two-dimensional list representation of the game matrix. Each sublist represents a
   * row in the matrix, and each element within these sub-lists is the name of the symbol at the corresponding cell position.
   * <p>
   * Note: This method assumes that the row and column indices start from 1 and are contiguous, which should hold true if the
   * configuration JSON is valid.
   *
   * @return A {@code List<List<String>>} representing the game matrix
   */
  public List<List<String>> asMatrix() {
    int maxRow = cells.keySet().stream()
        .map(Cell::row)
        .max(Integer::compareTo)
        .orElse(-1);
    int maxCol = cells.keySet().stream()
        .map(Cell::column)
        .max(Integer::compareTo)
        .orElse(-1);

    return IntStream.rangeClosed(0, maxRow)
        .mapToObj(row -> IntStream.rangeClosed(0, maxCol)
            .mapToObj(col -> new Cell(row, col))
            .map(cells::get)
            .map(Symbol::getName)
            .collect(Collectors.toList()))
        .collect(Collectors.toList());
  }
}
