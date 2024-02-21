package dev.marvel.scratch.domain.symbol.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.domain.symbol.model.AbstractSymbol;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Responsible for parsing symbol configurations from a JSON configuration node and updating the {@link Configuration} instance with the
 * parsed symbols. It reads the 'symbols' node of the game configuration JSON, converting each symbol definition into a
 * {@link AbstractSymbol}
 * instance. These symbols are then integrated into the overall game {@link Configuration} object, ready for use in game logic.
 * </p>
 *
 * @see AbstractSymbol for details on the symbol model.
 * @see Configuration for how symbols are integrated into the game configuration.
 */
@RequiredArgsConstructor
public class SymbolParser implements Parser {

  private final ObjectMapper mapper;

  /**
   * Parses the 'symbols' node from the game configuration JSON, creating and collecting {@link AbstractSymbol} instances based on the
   * provided data.
   *
   * @param configRoot    The root node of the game configuration JSON.
   * @param configuration The {@link Configuration} instance to be updated with the parsed symbols.
   * @throws ConfigurationParseException if the 'symbols' node is missing or malformed, or if any symbol cannot be properly parsed.
   */
  public void parse(JsonNode configRoot, Configuration configuration) {
    var symbolsNode = configRoot.get("symbols");
    if (symbolsNode == null || !symbolsNode.isObject()) {
      throw new ConfigurationParseException("The supplied configuration file should have an object 'symbols' node.");
    }

    var spliterator = Spliterators.spliteratorUnknownSize(symbolsNode.fields(), Spliterator.IMMUTABLE);
    var symbols = StreamSupport.stream(spliterator, true)
        .map(this::parseInternal)
        .collect(Collectors.toSet());
    configuration.setSymbols(symbols);
  }

  private Symbol parseInternal(Map.Entry<String, JsonNode> symbolNode) {
    try {
      var symbol = mapper.treeToValue(symbolNode.getValue(), Symbol.class);
      var name = symbolNode.getKey();
      symbol.setName(name);
      return symbol;
    } catch (JsonProcessingException e) {
      throw new ConfigurationParseException("Could not parse configuration node", e.getCause());
    }
  }
}
