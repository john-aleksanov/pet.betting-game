package dev.marvel.scratch.domain.scorer;

import dev.marvel.scratch.domain.wincombination.MatchResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static dev.marvel.scratch.domain.TestUtils.HLS_WC;
import static dev.marvel.scratch.domain.TestUtils.SAME3WC;
import static dev.marvel.scratch.domain.TestUtils.SAME5WC;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_1000;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_A;
import static dev.marvel.scratch.domain.TestUtils.SYMBOL_B;
import static dev.marvel.scratch.domain.TestUtils.VLS_WC;
import static org.assertj.core.api.Assertions.assertThat;

class ScorerTest {

  private final Scorer uut = new Scorer();

  private static Stream<Arguments> argumentsProvider() {
    return Stream.of(
        Arguments.of(new MatchResult(Collections.emptyMap(), Set.of(SYMBOL_1000)), 0),
        Arguments.of(
            new MatchResult(Map.of(SYMBOL_A, Set.of(SAME5WC, VLS_WC), SYMBOL_B, Set.of(SAME3WC, VLS_WC)), Set.of(SYMBOL_1000)),
            26_000),
        Arguments.of(
            new MatchResult(Map.of(SYMBOL_A, Set.of(SAME5WC), SYMBOL_B, Set.of(HLS_WC)), Collections.emptySet()),
            15_000),
        Arguments.of(new MatchResult(Map.of(SYMBOL_A, Set.of(SAME5WC, HLS_WC)), Collections.emptySet()), 20_000));
  }

  @ParameterizedTest
  @MethodSource("argumentsProvider")
  void test(MatchResult matchResult, int expectedScore) {
    // GIVEN-WHEN
    var score = uut.score(100, matchResult);

    // THEN
    assertThat(score).isEqualTo(expectedScore);
  }
}