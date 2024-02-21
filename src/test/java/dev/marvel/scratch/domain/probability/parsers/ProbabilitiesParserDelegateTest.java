package dev.marvel.scratch.domain.probability.parsers;

import com.fasterxml.jackson.databind.JsonNode;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.domain.TestUtils;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProbabilitiesParserDelegateTest {

  private ProbabilitiesParserDelegate uut;

  @Mock
  private Parser parserOne;
  @Mock
  private Parser parserTwo;
  @Mock
  private Configuration configuration;

  @BeforeEach
  void setUp() {
    uut = new ProbabilitiesParserDelegate(Set.of(parserOne, parserTwo));
  }

  @Test
  void whenValidConfigThenSuccessfulParsing() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/delegate/valid.json");

    // WHEN
    uut.parse(configRoot, configuration);

    // THEN
    verify(parserOne).parse(any(JsonNode.class), eq(configuration));
    verify(parserTwo).parse(any(JsonNode.class), eq(configuration));
  }

  @Test
  void whenNoProbabilitiesAttributeThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/delegate/missing-probabilities.json");

    // WHEN-THEN
    assertThatExceptionOfType(ConfigurationParseException.class)
        .isThrownBy(() -> uut.parse(configRoot, configuration))
        .withMessageContaining("Config file should have an object 'probabilities' attribute.");
  }

  @Test
  void whenProbabilitiesNotObjectThenExceptionThrown() {
    // GIVEN
    var configRoot = TestUtils.readJsonNode("/probability/parsers/delegate/non-object-probabilities.json");

    // WHEN-THEN
    assertThatExceptionOfType(ConfigurationParseException.class)
        .isThrownBy(() -> uut.parse(configRoot, configuration))
        .withMessageContaining("Config file should have an object 'probabilities' attribute.");
  }
}