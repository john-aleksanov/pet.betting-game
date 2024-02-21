package dev.marvel.scratch.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marvel.scratch.exceptions.ConfigurationParseException;

/**
 * Parses the dimensions of a game configuration, specifically the number of rows and columns, from a JSON config file and updates the
 * provided {@link Configuration} object accordingly.
 */
public class DimensionsParser implements Parser {

  /**
   * Parses the 'rows' and 'columns' from the provided JSON root node and updates the given {@link Configuration} instance with these
   * values. This method ensures that both 'rows' and 'columns' are present and are integers; otherwise, it throws a
   * {@link ConfigurationParseException}.
   *
   * @param configRoot The root JSON node of the configuration from which to parse the dimensions.
   * @param configuration The {@link Configuration} instance to be updated with the parsed values.
   * @throws ConfigurationParseException if either 'rows' or 'columns' is missing or is not an integer.
   */
  @Override
  public void parse(JsonNode configRoot, Configuration configuration) {
    var rows = parseRows(configRoot);
    configuration.setRows(rows);
    var columns = parseColumns(configRoot);
    configuration.setColumns(columns);
  }

  private int parseRows(JsonNode configRoot) {
    var rowsNode = configRoot.get("rows");
    if (rowsNode == null || !rowsNode.isInt()) {
      throw new ConfigurationParseException("Config file should have an integer 'rows' attribute.");
    }
    return rowsNode.asInt();
  }

  private int parseColumns(JsonNode configRoot) {
    var columnsNode = configRoot.get("columns");
    if (columnsNode == null || !columnsNode.isInt()) {
      throw new ConfigurationParseException("Config file should have an integer 'columns' attribute.");
    }
    return columnsNode.asInt();
  }
}
