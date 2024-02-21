package dev.marvel.scratch.domain.core;

import dev.marvel.scratch.exceptions.ConfigurationParseException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CellTest {

  @Test
  void whenInputValidThenValidCellReturned() {
    // GIVEN
    var input = "0:1";

    // WHEN
    var actual = Cell.of(input);

    // THEN
    assertThat(actual.row()).isEqualTo(0);
    assertThat(actual.column()).isEqualTo(1);
  }

  @Test
  void whenNonIntegerPartsThenExceptionThrown() {
    // GIVEN
    var input = "a:b";

    // WHEN-THEN
    assertThatThrownBy(() -> Cell.of(input))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'probabilities.win_combinations.{}.covered_areas' should be string attributes of format'%d:%d'");
  }

  @Test
  void whenSinglePartThenExceptionThrown() {
    // GIVEN
    var input = "0";

    // WHEN-THEN
    assertThatThrownBy(() -> Cell.of(input))
        .isInstanceOf(ConfigurationParseException.class)
        .hasMessageContaining("'probabilities.win_combinations.{}.covered_areas' should be string attributes of format'%d:%d'");
  }
}