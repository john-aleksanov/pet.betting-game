package dev.marvel.scratch.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.domain.core.Cell;
import dev.marvel.scratch.domain.symbol.model.BonusSymbol;
import dev.marvel.scratch.domain.symbol.model.Impact;
import dev.marvel.scratch.domain.symbol.model.StandardSymbol;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.symbol.model.Type;
import dev.marvel.scratch.domain.wincombination.model.LinearWinCombination;
import dev.marvel.scratch.domain.wincombination.model.SameSymbolWinCombination;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;

import java.io.IOException;
import java.util.Set;

import static dev.marvel.scratch.domain.wincombination.model.Group.HORIZONTALLY_LINEAR_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.LTR_DIAGONALLY_LINEAR_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.RTL_DIAGONALLY_LINEAR_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.SAME_SYMBOLS;
import static dev.marvel.scratch.domain.wincombination.model.Group.VERTICALLY_LINEAR_SYMBOLS;

public class TestUtils {

  public static final WinCombination SAME3WC =
      SameSymbolWinCombination.builder().name("same_symbol_3_times").group(SAME_SYMBOLS).rewardMultiplier(1.0).count(3).build();
  public static final WinCombination SAME4WC =
      SameSymbolWinCombination.builder().name("same_symbol_4_times").group(SAME_SYMBOLS).rewardMultiplier(1.5).count(4).build();
  public static final WinCombination SAME5WC =
      SameSymbolWinCombination.builder().name("same_symbol_5_times").group(SAME_SYMBOLS).rewardMultiplier(2.0).count(5).build();
  public static final WinCombination SAME6WC =
      SameSymbolWinCombination.builder().name("same_symbol_6_times").group(SAME_SYMBOLS).rewardMultiplier(3.0).count(6).build();
  public static final WinCombination SAME7WC =
      SameSymbolWinCombination.builder().name("same_symbol_7_times").group(SAME_SYMBOLS).rewardMultiplier(5.0).count(7).build();
  public static final WinCombination SAME8WC =
      SameSymbolWinCombination.builder().name("same_symbol_8_times").group(SAME_SYMBOLS).rewardMultiplier(10.0).count(8).build();
  public static final WinCombination SAME9WC =
      SameSymbolWinCombination.builder().name("same_symbol_9_times").group(SAME_SYMBOLS).rewardMultiplier(20.0).count(9).build();

  public static final WinCombination HLS_WC = LinearWinCombination.builder()
      .name("horizontally_linear_symbols")
      .group(HORIZONTALLY_LINEAR_SYMBOLS)
      .rewardMultiplier(2.0)
      .coveredAreas(Set.of(
          Set.of(new Cell(0, 0), new Cell(0, 1), new Cell(0, 2)),
          Set.of(new Cell(1, 0), new Cell(1, 1), new Cell(1, 2)),
          Set.of(new Cell(2, 0), new Cell(2, 1), new Cell(2, 2))
      ))
      .build();

  public static final WinCombination VLS_WC = LinearWinCombination.builder()
      .name("vertically_linear_symbols")
      .group(VERTICALLY_LINEAR_SYMBOLS)
      .rewardMultiplier(2.0)
      .coveredAreas(Set.of(
          Set.of(new Cell(0, 0), new Cell(1, 0), new Cell(2, 0)),
          Set.of(new Cell(0, 1), new Cell(1, 1), new Cell(2, 1)),
          Set.of(new Cell(0, 2), new Cell(1, 2), new Cell(2, 2))
      ))
      .build();

  public static final WinCombination LTR_WC = LinearWinCombination.builder()
      .name("ltr_diagonally_linear_symbols")
      .group(LTR_DIAGONALLY_LINEAR_SYMBOLS)
      .rewardMultiplier(5.0)
      .coveredAreas(Set.of(
          Set.of(new Cell(0, 0), new Cell(1, 1), new Cell(2, 2))
      ))
      .build();

  public static final WinCombination RTL_WC = LinearWinCombination.builder()
      .name("ltr_diagonally_linear_symbols")
      .group(RTL_DIAGONALLY_LINEAR_SYMBOLS)
      .rewardMultiplier(5.0)
      .coveredAreas(Set.of(
          Set.of(new Cell(0, 2), new Cell(1, 1), new Cell(2, 0))
      ))
      .build();

  public static final Symbol SYMBOL_A = StandardSymbol.builder().name("A").type(Type.STANDARD).value(50.0).build();
  public static final Symbol SYMBOL_B = StandardSymbol.builder().name("B").type(Type.STANDARD).value(25.0).build();
  public static final Symbol SYMBOL_C = StandardSymbol.builder().name("C").type(Type.STANDARD).value(10.0).build();
  public static final Symbol SYMBOL_D = StandardSymbol.builder().name("D").type(Type.STANDARD).value(5.0).build();
  public static final Symbol SYMBOL_E = StandardSymbol.builder().name("E").type(Type.STANDARD).value(3.0).build();
  public static final Symbol SYMBOL_F = StandardSymbol.builder().name("F").type(Type.STANDARD).value(1.5).build();
  public static final Symbol SYMBOL_1000 =
      BonusSymbol.builder().name("+1000").type(Type.BONUS).impact(Impact.EXTRA_BONUS).value(1000.0).build();
  public static final Symbol SYMBOL_500 =
      BonusSymbol.builder().name("+500").type(Type.BONUS).impact(Impact.EXTRA_BONUS).value(500.0).build();
  public static final Symbol SYMBOL_10x =
      BonusSymbol.builder().name("10x").type(Type.BONUS).impact(Impact.MULTIPLY_REWARD).value(10.0).build();
  public static final Symbol SYMBOL_5x =
      BonusSymbol.builder().name("5x").type(Type.BONUS).impact(Impact.MULTIPLY_REWARD).value(5.0).build();
  public static final Symbol SYMBOL_MISS = BonusSymbol.builder().name("MISS").type(Type.BONUS).impact(Impact.MISS).build();

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static JsonNode readJsonNode(String resourcePath) {
    try {
      var resource = TestUtils.class.getResourceAsStream(resourcePath);
      return MAPPER.readTree(resource);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read JSON resource: " + resourcePath, e);
    }
  }
}
