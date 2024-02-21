package dev.marvel.scratch.domain.symbol.parsers;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.TestUtils;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SymbolParserTest {

  private SymbolParser uut = new SymbolParser(new ObjectMapper());

  @Test
  void whenMissingSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/missing-symbols.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("The supplied configuration file should have an object 'symbols' node.");
  }

  @Test
  void whenNonObjectSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/non-object-symbols.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("The supplied configuration file should have an object 'symbols' node.");
  }

  @Test
  void whenValidJsonThenParsedCorrectly() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/valid.json");
    var configuration = new Configuration();

    // WHEN
    uut.parse(configRoot, configuration);

    // THEN
    var symbols = configuration.getSymbols();
    assertThat(symbols.size()).isEqualTo(11);
  }

  @Test
  void whenMultiplyRewardSymbolAndNoRewardMultiplierThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/missing-reward-multiplier-bonus-symbol.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("Could not parse configuration node")
        .hasRootCauseMessage("'multiply_reward' symbols should have a 'reward_multiplier' attribute.");
  }

  @Test
  void whenExtraBonusSymbolAndNoExtraThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/missing-extra.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("Could not parse configuration node")
        .hasRootCauseMessage("'extra_bonus' symbols should have an 'extra' attribute.");
  }

  @Test
  void whenBonusSymbolAndNoImpactThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/missing-impact.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("Could not parse configuration node")
        .hasRootCauseMessage("'bonus' symbols should have an 'impact' attribute.");
  }

  @Test
  void whenStandardSymbolAndNoRewardMultiplierThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/symbol/parsers/missing-reward-multiplier-standard-symbol.json");
    var configuration = new Configuration();

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessage("Could not parse configuration node")
        .hasRootCauseMessage("'standard' symbols should have a 'reward_multiplier' attribute.");
  }
}