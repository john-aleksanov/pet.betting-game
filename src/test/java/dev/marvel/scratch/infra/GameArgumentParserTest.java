package dev.marvel.scratch.infra;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


class GameArgumentParserTest {

  private final GameArgumentParser uut = new GameArgumentParser();

  @Test
  void whenValidArgsProvidedThenParsedSuccessfully() {
    // GIVEN
    var args = new String[]{"--config", "gameConfig.json", "--betting-amount", "100"};
    var expected = new GameArguments("gameConfig.json", 100);

    // WHEN
    var result = uut.parse(args);

    // THEN
    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void whenIncorrectArgsCountThenExceptionThrown() {
    // GIVEN
    var args = new String[]{"--config", "gameConfig.json"};

    // WHEN-THEN
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> uut.parse(args))
        .withMessageContaining("Please specify exactly two arguments");
  }

  @Test
  void whenInvalidBettingAmount_thenThrowIllegalArgumentException() {
    // GIVEN
    var args = new String[]{"--config", "gameConfig.json", "--betting-amount", "notAnInteger"};

    // WHEN-THEN
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> uut.parse(args))
        .withMessageContaining("Please specify a valid integer for --betting-amount");
  }

  @Test
  void whenUnknownArgument_thenThrowIllegalArgumentException() {
    // GIVEN
    var args = new String[]{"--unknown", "value", "--config", "gameConfig.json"};
    var uut = new GameArgumentParser();

    // WHEN-THEN
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> uut.parse(args))
        .withMessageContaining("Unknown argument");
  }
}
