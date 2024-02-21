package dev.marvel.scratch.domain.probability.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.RequiredArgsConstructor;

import java.util.Set;

/**
 * This class acts as an orchestrator for the parsing process, ensuring that each section of the 'probabilities' node in the game
 * configuration is processed by an appropriate {@link Parser} implementations. It aggregates the parsing logic for different types of
 * probabilities into a single entry point, simplifying the overall parsing structure.
 *
 * @see Parser for the general interface that each probabilities parser implements.
 * @see Configuration for how parsed probabilities are integrated into the game configuration.
 */
@RequiredArgsConstructor
public class ProbabilitiesParserDelegate implements Parser {

  public final Set<Parser> probabilitiesParsers;


  /**
   * Parses the 'probabilities' node of the game configuration JSON, delegating to each registered parser for processing specific types
   * of probabilities within the node.
   * <p>
   * This method ensures that the game configuration contains a valid 'probabilities' object node and then iterates over the set of
   * registered parsers, each handling a segment of the probabilities' configuration.
   *
   * @param configRoot    The root node of the game configuration JSON.
   * @param configuration The {@link Configuration} instance to be updated with the parsed probabilities.
   * @throws ConfigurationParseException if the 'probabilities' node is missing or not structured as expected.
   */
  @Override
  public void parse(JsonNode configRoot, Configuration configuration) {
    var probabilitiesRoot = configRoot.get("probabilities");
    if (probabilitiesRoot == null || !probabilitiesRoot.isObject()) {
      throw new ConfigurationParseException("Config file should have an object 'probabilities' attribute.");
    }

    for (var parser : probabilitiesParsers) {
      parser.parse(probabilitiesRoot, configuration);
    }
  }

}
