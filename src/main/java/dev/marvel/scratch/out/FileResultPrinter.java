package dev.marvel.scratch.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.wincombination.MatchResult;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;

/**
 * Writes game results to "result.json" in the current working directory using Jackson for JSON construction. The output includes the
 * game matrix, matched winning combinations, bonus symbols, and the final reward.
 */
@RequiredArgsConstructor
public class FileResultPrinter {

  private final ObjectMapper mapper;

  /**
   * Constructs a JSON object with game results and writes it to "result.json". The JSON includes the game's matrix, matched winning
   * combinations and bonus symbols, and the final reward. Throws a RuntimeException on I/O errors.
   *
   * @param game The game instance containing the matrix.
   * @param matchResult The match result with matched winning combinations and bonus symbols, if any.
   * @param score The final game reward.
   * @throws RuntimeException if unable to write to the file.
   */
  public void print(Game game, MatchResult matchResult, double score) {
    var rootNode = mapper.createObjectNode();
    addMatrixElement(rootNode, game);
    addAppliedWinCombinationsElement(rootNode, matchResult);
    addAppliedBonusSymbolsElement(rootNode, matchResult);
    rootNode.put("reward", score);
    try {
      mapper.writeValue(new File("result.json"), rootNode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void addAppliedBonusSymbolsElement(ObjectNode rootNode, MatchResult matchResult) {
    var bonusSymbolsNode = mapper.createArrayNode();
    matchResult.matchedBonusSymbols().forEach(symbol -> bonusSymbolsNode.add(symbol.getName()));
    rootNode.set("applied_bonus_symbols", bonusSymbolsNode);
  }

  private void addMatrixElement(ObjectNode rootNode, Game game) {
    var matrix = game.asMatrix();
    var matrixNode = mapper.valueToTree(matrix);
    rootNode.set("matrix", matrixNode);
  }

  private void addAppliedWinCombinationsElement(ObjectNode rootNode, MatchResult matchResult) {
    ObjectNode winningCombinationsNode = mapper.createObjectNode();
    matchResult.matchedWinCombinations().forEach((symbol, winCombinations) -> {
      ArrayNode winComboArray = mapper.createArrayNode();
      winCombinations.forEach(winCombination -> winComboArray.add(winCombination.getName()));
      winningCombinationsNode.set(symbol.getName(), winComboArray);
    });
    rootNode.set("applied_winning_combinations", winningCombinationsNode);
  }
}
