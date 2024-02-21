package dev.marvel.scratch.domain.probability.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.domain.probability.model.CellProbability;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

/**
 * Parses bonus symbol probabilities from the game configuration and integrates them into the provided {@link Configuration} instance.
 * This parser specifically handles the 'bonus_symbols' section of the 'probabilities' configuration node, assigning probabilities to
 * bonus symbols within each cell's probability distribution.
 * <p>
 * This involves reading the bonus symbols and their associated probabilities, ensuring they are defined within the overall symbols
 * configuration, and updating each cell's probability model with these bonus symbol probabilities.
 *
 * @see Configuration for how parsed probabilities are integrated into the game configuration.
 * @see CellProbability for details on how probabilities are managed at the cell level.
 * @see Symbol for understanding the symbol model, including bonus symbols.
 */
public class BonusProbabilitiesParser implements Parser {

  /**
   * Parses the 'bonus_symbols' attribute from the 'probabilities' configuration node, updating the provided {@link Configuration} with
   * bonus symbol probabilities. Validates that each bonus symbol defined within 'bonus_symbols' is also defined
   * within the global symbols configuration. It then distributes the parsed probabilities to the appropriate cells within the game
   * configuration.
   *
   * @param probabilitiesRoot The root node of the 'probabilities' section of the game configuration.
   * @param configuration     The {@link Configuration} instance to be updated with parsed probabilities.
   * @throws ConfigurationParseException if 'bonus_symbols' is missing, malformed, or contains undefined symbols.
   */
  @Override
  public void parse(JsonNode probabilitiesRoot, Configuration configuration) {
    var bonusSymbols = probabilitiesRoot.get("bonus_symbols");
    if (bonusSymbols == null || !bonusSymbols.isObject()) {
      throw new ConfigurationParseException("'probabilities' should have an object 'bonus_symbols' attribute.");
    }
    var symbolsNode = bonusSymbols.get("symbols");
    if (symbolsNode == null || !symbolsNode.isObject()) {
      throw new ConfigurationParseException("'probabilities.bonus_symbols' should have an object 'symbols' attribute.");
    }
    StreamSupport.stream(Spliterators.spliteratorUnknownSize(symbolsNode.fields(), Spliterator.ORDERED), false)
        .map(symbolNode -> parseSymbolNode(symbolNode, configuration.getSymbols()))
        .forEach(entry -> configuration.getCellProbabilities()
            .forEach(cp -> cp.addProbability(entry.getKey(), entry.getValue())));
    validateProbabilities(configuration.getSymbols(), configuration);
  }

  private Map.Entry<Symbol, Integer> parseSymbolNode(Map.Entry<String, JsonNode> symbolNode, Set<Symbol> symbols) {
    var symbolName = symbolNode.getKey();
    var symbol = Symbol.findInCollection(symbols, symbolName).orElseThrow(() -> new ConfigurationParseException(
        "Symbol " + symbolName + " in 'probabilities.bonus_symbols.symbols' should be defined in 'symbols'."
    ));
    var probability = symbolNode.getValue();
    if (!probability.isInt()) {
      throw new ConfigurationParseException("Probability for bonus symbol " + symbolName + " should be an integer.");
    }
    return new AbstractMap.SimpleEntry<>(symbol, probability.asInt());
  }

  private void validateProbabilities(Set<Symbol> symbols, Configuration configuration) {
    var expectedBonusProbabilitiesCount = symbols.stream()
        .filter(Symbol::isBonus)
        .count();
    configuration.getCellProbabilities().stream()
        .map(CellProbability::getProbabilities)
        .map(Map::keySet)
        .forEach(smbls -> {
          var actualBonusSymbolsCount = smbls.stream()
              .filter(Symbol::isBonus)
              .count();
          if (expectedBonusProbabilitiesCount != actualBonusSymbolsCount) {
            throw new ConfigurationParseException(
                "'probabilities.bonus_symbols.symbols' should contain " + expectedBonusProbabilitiesCount +
                    "nodes");
          }
        });
  }
}
