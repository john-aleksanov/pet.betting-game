package dev.marvel.scratch.domain.probability.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.domain.probability.model.CellProbability;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Parses the standard symbol probabilities from the game configuration JSON and integrates them into the provided {@link Configuration}
 * instance. This parser specifically handles the 'standard_symbols' section within the 'probabilities' node, assigning probabilities
 * to standard symbols for each cell within the game matrix.
 * <p>
 * It validates the structure and completeness of the standard symbols' probabilities data, ensuring that each cell defined in the game
 * matrix has an associated probability distribution for the standard symbols.
 *
 * @see Configuration for how parsed probabilities are integrated into the game configuration.
 * @see CellProbability for details on managing probabilities at the cell level.
 * @see Symbol for understanding the symbol model.
 */
public class StandardProbabilitiesParser implements Parser {

  /**
   * Parses the 'standard_symbols' node from the 'probabilities' configuration section, creating and assigning {@link CellProbability}
   * instances based on the provided data.
   * <p>
   * This method ensures that:
   * <ul>
   *   <li>Each 'standard_symbols' entry has an integer 'row' and 'column' attribute.</li>
   *   <li>Each 'standard_symbols' entry contains an 'symbols' object with integer probabilities.</li>
   *   <li>Every symbol referenced within 'standard_symbols' is defined within the global 'symbols' configuration.</li>
   *   <li>The total number of 'standard_symbols' entries matches the expected count based on the game's dimensions.</li>
   *   <li>Each cell in the game configuration has probabilities defined for all standard symbols.</li>
   * </ul>
   *
   * @param probabilitiesRoot The root node of the 'probabilities' section of the game configuration.
   * @param configuration     The {@link Configuration} instance to be updated with parsed probabilities.
   * @throws ConfigurationParseException if 'standard_symbols' is missing, malformed, or contains undefined symbols.
   */
  @Override
  public void parse(JsonNode probabilitiesRoot, Configuration configuration) {
    var standardSymbols = probabilitiesRoot.get("standard_symbols");
    if (standardSymbols == null || !standardSymbols.isArray()) {
      throw new ConfigurationParseException("'probabilities' should have an array 'standard_symbols' attribute.");
    }
    StreamSupport.stream(standardSymbols.spliterator(), true)
        .map(standardSymbolNode -> mapProbability(standardSymbolNode, configuration.getSymbols()))
        .forEach(configuration::addProbability);
    validateProbabilities(configuration);
  }

  private CellProbability mapProbability(JsonNode standardSymbolsNode, Set<Symbol> symbols) {
    var row = parseRowAttribute(standardSymbolsNode);
    var column = parseColumnAttribute(standardSymbolsNode);
    var symbolProbabilities = parseSymbolProbabilities(standardSymbolsNode, symbols);
    return new CellProbability(row, column, symbolProbabilities);
  }

  private int parseRowAttribute(JsonNode standardSymbolsNode) {
    var row = standardSymbolsNode.get("row");
    if (row == null || !row.isInt()) {
      throw new ConfigurationParseException("'standard_symbols.[]' should have an int 'row' attribute.");
    }
    return row.asInt();
  }

  private int parseColumnAttribute(JsonNode standardSymbolsNode) {
    var column = standardSymbolsNode.get("column");
    if (column == null || !column.isInt()) {
      throw new ConfigurationParseException("'standard_symbols.[]' should have an int 'column' attribute.");
    }
    return column.asInt();
  }

  private Map<Symbol, Integer> parseSymbolProbabilities(JsonNode standardSymbolsNode, Set<Symbol> symbols) {
    var symbolsNode = standardSymbolsNode.get("symbols");
    if (symbolsNode == null || !symbolsNode.isObject()) {
      throw new ConfigurationParseException("'standard_symbols' should have an object 'symbols' attribute.");
    }
    var symbolProbabilities = new HashMap<Symbol, Integer>();
    StreamSupport.stream(Spliterators.spliteratorUnknownSize(symbolsNode.fields(), Spliterator.ORDERED), false)
        .map(symbolNode -> parseSymbolNode(symbolNode, symbols))
        .forEach(entry -> symbolProbabilities.put(entry.getKey(), entry.getValue()));
    return symbolProbabilities;
  }

  private Map.Entry<Symbol, Integer> parseSymbolNode(Map.Entry<String, JsonNode> symbolNode, Set<Symbol> symbols) {
    var symbolName = symbolNode.getKey();
    var symbol = Symbol.findInCollection(symbols, symbolName).orElseThrow(() -> new ConfigurationParseException(
        "Symbol " + symbolName + " in 'probabilities.standard_symbols.[].symbols' should be defined in 'symbols'."
    ));
    var probability = symbolNode.getValue();
    if (!probability.isInt()) {
      throw new ConfigurationParseException("Probability for standard symbol " + symbolName + " should be an integer.");
    }
    return new AbstractMap.SimpleEntry<>(symbol, probability.asInt());
  }

  private void validateProbabilities(Configuration configuration) {
    var expectedProbabilities = configuration.getRows() * configuration.getColumns();
    if (configuration.getCellProbabilities().size() != expectedProbabilities) {
      throw new ConfigurationParseException("'probabilities.standard_symbols' should contain " + expectedProbabilities + " nodes.");
    }
    var standardProbabilitySymbols = configuration.getSymbols().stream()
        .filter(Symbol::isStandard)
        .collect(Collectors.toSet());
    configuration.getCellProbabilities()
        .forEach(cellProbability -> {
          var probabilitySymbols = cellProbability.getProbabilities().keySet();
          if (!probabilitySymbols.containsAll(standardProbabilitySymbols)) {
            throw new ConfigurationParseException("'probabilities.standard_symbols.[].symbols' should contain probabilities for all " +
                "standard symbols defined in 'symbols'.");
          }
        });
  }

}
