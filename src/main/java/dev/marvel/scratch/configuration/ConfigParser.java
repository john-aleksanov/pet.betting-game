package dev.marvel.scratch.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.exceptions.ConfigurationParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * This class is responsible for reading the game configuration file, parsing its contents into a {@link Configuration} object, and
 * delegating specific sections of the configuration to specialized parsers. Each parser is responsible for a different aspect of the
 * game configuration, such as symbols, cell probabilities, or winning combinations.
 *
 * @see Parser for the interface implemented by parsers for specific configuration sections.
 * @see Configuration for the structure that holds the parsed configuration data.
 */
public class ConfigParser {

  private final Set<Parser> parsers;
  private final ObjectMapper mapper;

  public ConfigParser(Set<Parser> parsers) {
    this.parsers = parsers;
    this.mapper = new ObjectMapper();
  }

  /**
   * Parses the game configuration from the specified JSON file.
   *
   * @param configFileName The name (and path) of  the configuration file to parse.
   * @return A fully populated {@link Configuration} object.
   * @throws ConfigurationParseException if the file cannot be read or does not contain valid JSON.
   * @throws IllegalArgumentException    if the configuration file name is blank.
   */
  public Configuration parse(String configFileName) {
    var configuration = new Configuration();
    var configRoot = readConfigurationFile(configFileName);
    for (var parser : parsers) {
      parser.parse(configRoot, configuration);
    }
    return configuration;
  }

  private JsonNode readConfigurationFile(String configFileName) {
    if (configFileName.isBlank()) {
      throw new ConfigurationParseException("The config filename cannot be null, empty or blank.");
    }
    InputStream configInputStream;
    try {
      configInputStream = new FileInputStream(configFileName);
    } catch (IOException e) {
      throw new ConfigurationParseException(configFileName + " not found. Provide a path relative to the current working directory.");
    }
    try {
      return mapper.readTree(configInputStream);
    } catch (IOException e) {
      throw new ConfigurationParseException("The supplied configuration file is not a valid JSON.");
    }
  }
}
