package dev.marvel.scratch.domain.wincombination.parsers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.TestUtils;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class WinCombinationsParserTest {

  private WinCombinationsParser uut = new WinCombinationsParser(new ObjectMapper());

  @Test
  void whenMissingWinCombinationsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/wincombination/parsers/missing-win-combinations.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("Config should have a 'win_combinations' attribute.");
  }

  @Test
  void whenNonObjectWinCombinationsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/wincombination/parsers/non-object-win-combinations.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("Config should have a 'win_combinations' attribute.");
  }

  @Test
  void whenValidJsonThenParsedCorrectly() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/wincombination/parsers/valid.json");
    var configuration = new Configuration();

    // WHEN
    uut.parse(configRoot, configuration);

    // THEN
    assertThat(configuration.getWinCombinations().size()).isEqualTo(11);
  }
}