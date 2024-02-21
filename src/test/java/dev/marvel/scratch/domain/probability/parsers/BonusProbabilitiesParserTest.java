package dev.marvel.scratch.domain.probability.parsers;

import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.TestUtils;
import dev.marvel.scratch.domain.probability.model.CellProbability;
import dev.marvel.scratch.domain.symbol.model.BonusSymbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BonusProbabilitiesParserTest {

  private final BonusProbabilitiesParser uut = new BonusProbabilitiesParser();
  private Configuration configuration;

  @BeforeEach
  void setUp() {
    var symbol10x = new BonusSymbol(10d, null, "multiply_reward");
    symbol10x.setName("10x");
    var symbolPlus1000 = new BonusSymbol(null, 1000d, "extra_bonus");
    symbolPlus1000.setName("+1000");
    var symbolMiss = new BonusSymbol(null, null, "miss");
    symbolMiss.setName("MISS");
    configuration = Configuration.builder()
        .rows(2)
        .columns(2)
        .cellProbabilities(Set.of(
            new CellProbability(0, 0, new HashMap<>()),
            new CellProbability(0, 1, new HashMap<>()),
            new CellProbability(1, 0, new HashMap<>()),
            new CellProbability(1, 1, new HashMap<>())
        ))
        .build();
    configuration.setSymbols(Set.of(symbol10x, symbolPlus1000, symbolMiss));
    configuration.setRows(2);
    configuration.setColumns(2);
  }

  @Test
  void whenValidJsonThenBonusProbabilitiesParsed() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/bonus/valid.json");

    // WHEN
    uut.parse(configRoot, configuration);

    // THEN
    assertThat(configuration.getCellProbabilities()).hasSize(4);
    configuration.getCellProbabilities().forEach(cp -> assertThat(cp.getProbabilities()).hasSize(3));
  }

  @Test
  void whenNoBonusSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/bonus/missing-bonus-symbols.json");

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'probabilities' should have an object 'bonus_symbols' attribute.");
  }

  @Test
  void whenNoSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/bonus/missing-symbols.json");

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'probabilities.bonus_symbols' should have an object 'symbols' attribute.");
  }

  @Test
  void whenProbabilityForSymbolNotDefinedInSymbolsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/bonus/valid.json");
    configuration.setSymbols(new HashSet<>());

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Symbol 10x in 'probabilities.bonus_symbols.symbols' should be defined in 'symbols'.");
  }

  @Test
  void whenNonIntegerSymbolProbabilityThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/bonus/non-integer-probability.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Probability for bonus symbol 10x should be an integer.");
  }
}