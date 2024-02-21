package dev.marvel.scratch.domain.probability.parsers;

import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.TestUtils;
import dev.marvel.scratch.domain.symbol.model.StandardSymbol;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StandardProbabilitiesParserTest {

  private final StandardProbabilitiesParser uut = new StandardProbabilitiesParser();
  private Configuration configuration;

  @BeforeEach
  void setUp() {
    configuration = new Configuration();
    var symbolA = new StandardSymbol(50d);
    symbolA.setName("A");
    var symbolB = new StandardSymbol(25d);
    symbolB.setName("B");
    configuration.setSymbols(Set.of(symbolA, symbolB));
    configuration.setRows(2);
    configuration.setColumns(2);
  }

  @Test
  void whenValidJsonThenStandardProbabilitiesParsed() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/valid.json");

    // WHEN
    uut.parse(configRoot, configuration);

    // THEN
    assertThat(configuration.getCellProbabilities()).hasSize(4);
  }

  @Test
  void whenNoStandardSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/missing-standard-symbols.json");

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'probabilities' should have an array 'standard_symbols' attribute.");
  }

  @Test
  void whenNoRowsInStandardSymbolsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/missing-rows.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'standard_symbols.[]' should have an int 'row' attribute.");
  }

  @Test
  void whenNonIntegerRowsInStandardSymbolsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/non-integer-rows.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'standard_symbols.[]' should have an int 'row' attribute.");
  }

  @Test
  void whenNoColumnsInStandardSymbolsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/missing-columns.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'standard_symbols.[]' should have an int 'column' attribute.");
  }

  @Test
  void whenNonIntegerColumnsInStandardSymbolsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/non-integer-columns.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'standard_symbols.[]' should have an int 'column' attribute.");
  }

  @Test
  void whenMissingSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/missing-symbols.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'standard_symbols' should have an object 'symbols' attribute.");
  }

  @Test
  void whenNonObjectSymbolsAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/non-object-symbols.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'standard_symbols' should have an object 'symbols' attribute.");
  }

  @Test
  void whenProbabilityForSymbolNotDefinedInSymbolsThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/valid.json");
    configuration.setSymbols(new HashSet<>());

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Symbol A in 'probabilities.standard_symbols.[].symbols' should be defined in 'symbols'.");
  }

  @Test
  void whenNonIntegerSymbolProbabilityThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/non-integer-probability.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("Probability for standard symbol A should be an integer.");
  }

  @Test
  @DisplayName("Checks that the 'probabilities.standard_symbols' array contains exactly {rows * columns} probability objects.")
  void whenProbabilityObjectMissingThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/missing-probabilities.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'probabilities.standard_symbols' should contain 4 nodes.");
  }

  @Test
  @DisplayName("Checks that the 'probabilities.standard_symbols.[].symbols' object contains probabilities for all symbols defined in " +
      "'symbols'.")
  void whenIndividualProbabilityMissingThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/standard/missing-symbol-probability.json");

    // WHEN & THEN
    assertThatThrownBy(() -> uut.parse(configRoot, configuration))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining(
            "probabilities.standard_symbols.[].symbols' should contain probabilities for all standard symbols defined in 'symbols'.");
  }
}