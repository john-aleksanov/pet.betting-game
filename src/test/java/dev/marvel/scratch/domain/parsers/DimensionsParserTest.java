package dev.marvel.scratch.domain.parsers;

import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.DimensionsParser;
import dev.marvel.scratch.domain.TestUtils;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DimensionsParserTest {

  private DimensionsParser uut = new DimensionsParser();

  @Test
  void whenValidJsonThenDimensionsParsedSuccessfully() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/parsers/valid.json");
    var configuration = new Configuration();

    // WHEN
    uut.parse(configRoot, configuration);

    // THEN
    assertThat(configuration.getRows()).isEqualTo(10);
    assertThat(configuration.getColumns()).isEqualTo(10);
  }

  @Test
  void whenNoRowsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/parsers/only-columns.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Config file should have an integer 'rows' attribute.");
  }

  @Test
  void whenNonIntegerRowsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/parsers/non-integer-rows.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Config file should have an integer 'rows' attribute.");
  }

  @Test
  void whenNoColumnsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/parsers/only-rows.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Config file should have an integer 'columns' attribute.");
  }

  @Test
  void whenNonIntegerColumnsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/parsers/non-integer-columns.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Config file should have an integer 'columns' attribute.");
  }
}