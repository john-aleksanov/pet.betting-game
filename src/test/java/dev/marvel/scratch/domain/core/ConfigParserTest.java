package dev.marvel.scratch.domain.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.configuration.ConfigParser;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

class ConfigParserTest {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final Parser parser = Mockito.mock(Parser.class);
  private final ConfigParser uut = new ConfigParser(Set.of(parser));

  @Test
  void whenConfigFileNameValidThenConfigurationParsed() throws Exception {
    // GIVEN
    var configFileName = "src/test/resources/core/valid-config.json";
    var expected = Configuration.builder()
        .rows(2)
        .columns(2)
        .cellProbabilities(new HashSet<>())
        .build();
    doAnswer(invocation -> {
      var config = (Configuration) invocation.getArgument(1);
      config.setColumns(2);
      config.setRows(2);
      return null;
    }).when(parser).parse(any(JsonNode.class), any(Configuration.class));

    // WHEN
    var actual = uut.parse(configFileName);

    // THEN
    assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    verify(parser).parse(eq(MAPPER.readTree(new FileInputStream(configFileName))), any(Configuration.class));
  }

  @Test
  void whenBlankFileNameThenExceptionThrown() {
    // GIVEN
    var blankFileName = " ";

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(blankFileName))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("The config filename cannot be null, empty or blank.");
  }

  @Test
  void whenFileDoesNotExistThenExceptionThrown() {
    // GIVEN
    var configFileName = "non-existing.json";

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configFileName))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("non-existing.json not found. Provide a path relative to the current working directory.");
  }

  @Test
  void whenInvalidJsonThenExceptionThrown() {
    // GIVEN
    var configFileName = "src/test/resources/core/invalid-config.json";

    // WHEN-THEN
    assertThatThrownBy(() -> uut.parse(configFileName))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("The supplied configuration file is not a valid JSON.");
  }
}