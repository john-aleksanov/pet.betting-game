package dev.marvel.scratch.out;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.domain.core.Cell;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.wincombination.MatchResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static dev.marvel.scratch.domain.TestUtils.SYMBOL_A;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_B;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_C;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_D;
import static dev.marvel.scratch.domain.TestUtils.VLS_WC;
import static org.assertj.core.api.Assertions.assertThat;

class FileResultPrinterTest {

  private final Path resultFile = Paths.get("result.json");
  private final ObjectMapper mapper = new ObjectMapper();
  private final FileResultPrinter uut = new FileResultPrinter(mapper);

  @AfterEach
  void tearDown() throws Exception {
    Files.deleteIfExists(resultFile);
  }

  @Test
  void whenValidArgumentsThenCorrectOutputCreated() throws Exception {
    // GIVEN
    var game = new Game(Map.of(
        new Cell(0, 0), SYMBOL_A, new Cell(0, 1), SYMBOL_B, new Cell(0, 2), SYMBOL_C,
        new Cell(1, 0), SYMBOL_A, new Cell(1, 1), SYMBOL_B, new Cell(1, 2), SYMBOL_C,
        new Cell(2, 0), SYMBOL_D, new Cell(2, 1), SYMBOL_B, new Cell(2, 2), SYMBOL_D
    ), 100);
    var matchResult = new MatchResult(Map.of(
        SYMBOL_B, Set.of(VLS_WC)
    ), Collections.emptySet());
    var score = 5_000;
    var expected =
        "{\"matrix\":[[\"A\",\"B\",\"C\"],[\"A\",\"B\",\"C\"],[\"D\",\"B\",\"D\"]]," +
            "\"applied_winning_combinations\":{\"B\":[\"vertically_linear_symbols\"]},\"applied_bonus_symbols\":[],\"reward\":5000.0}";

    // WHEN
    uut.print(game, matchResult, score);

    // THEN
    assertThat(Files.readString(resultFile)).isEqualTo(expected);
  }
}