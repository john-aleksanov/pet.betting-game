package dev.marvel.scratch.domain.wincombination.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.domain.wincombination.model.AbstractWinCombination;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Parses winning combinations ('win_combinations') from the game configuration JSON and updates the provided {@link Configuration}
 * instance accordingly.
 * <p>
 * This parser is responsible for interpreting the 'win_combinations' attribute of the configuration JSON, converting it into a set of
 * {@link WinCombination} instances that define the criteria for winning in the game. Each winning combination specifies a pattern of
 * symbols on the game matrix that, if matched, contributes to the player's score.
 *
 * @see WinCombination for details on the structure of a winning combination.
 * @see Configuration for how the parsed winning combinations are integrated into the game configuration.
 */
@RequiredArgsConstructor
public class WinCombinationsParser implements Parser {

  private final ObjectMapper mapper;

  /**
   * Parses the 'win_combinations' attribute from the provided game configuration JSON node and populates the {@link Configuration}
   * instance with the parsed winning combinations.
   *
   * @param configRoot    The root node of the game configuration JSON.
   * @param configuration The {@link Configuration} instance to be updated with the parsed winning combinations.
   * @throws ConfigurationParseException if the 'win_combinations' attribute is missing, malformed, or cannot be processed.
   */
  @Override
  public void parse(JsonNode configRoot, Configuration configuration) {
    var winCombinationsNode = configRoot.get("win_combinations");
    if (winCombinationsNode == null || !winCombinationsNode.isObject()) {
      throw new ConfigurationParseException("Config should have a 'win_combinations' attribute.");
    }
    var spliterator = Spliterators.spliteratorUnknownSize(winCombinationsNode.fields(), Spliterator.IMMUTABLE);
    var winCombinations = StreamSupport.stream(spliterator, true)
        .map(this::parseInternal)
        .collect(Collectors.toSet());
    configuration.setWinCombinations(winCombinations);
  }

  private WinCombination parseInternal(Map.Entry<String, JsonNode> winCombinationNode) {
    try {
      var winCombination = mapper.treeToValue(winCombinationNode.getValue(), AbstractWinCombination.class);
      var name = winCombinationNode.getKey();
      winCombination.setName(name);
      return winCombination;
    } catch (JsonProcessingException e) {
      throw new ConfigurationParseException("Could not parse configuration node ");
    }
  }
}
