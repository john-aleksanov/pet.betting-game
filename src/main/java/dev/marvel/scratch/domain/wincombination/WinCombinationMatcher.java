package dev.marvel.scratch.domain.wincombination;

import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.symbol.model.BonusSymbol;
import dev.marvel.scratch.domain.symbol.model.Symbol;
import dev.marvel.scratch.domain.wincombination.model.Group;
import dev.marvel.scratch.domain.wincombination.model.WinCombination;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matches symbols against defined win combinations in a game to determine winning outcomes. This class encapsulates the logic for
 * identifying which symbols have met the criteria of winning combinations and which bonus symbols have been activated based on the
 * game's current state.
 * <p>
 * Win combinations are grouped by their category (e.g., linear, same symbols) to streamline the matching process. The matcher considers
 * each symbol's presence in the game and evaluates it against all applicable win combinations.
 */
public class WinCombinationMatcher {

  private final Set<Symbol> symbols;
  private final Map<Group, Set<WinCombination>> winCombinationsByGroup;

  /**
   * Constructs a WinCombinationMatcher from a game configuration, initializing symbols and win combinations.
   *
   * @param configuration The game configuration containing symbols and their associated win combinations.
   */
  public WinCombinationMatcher(Configuration configuration) {
    this.symbols = configuration.getSymbols();
    this.winCombinationsByGroup = configuration.getWinCombinations().stream()
        .collect(Collectors.groupingBy(WinCombination::getGroup, Collectors.toSet()));
  }

  /**
   * Constructs a WinCombinationMatcher with a specific set of symbols and a mapping of win combinations by group. This constructor is
   * used in testing for easier instantiation.
   *
   * @param symbols The set of symbols to be matched.
   * @param winCombinationsByGroup The mapping of win combinations grouped by their type.
   */
  public WinCombinationMatcher(Set<Symbol> symbols, Map<Group, Set<WinCombination>> winCombinationsByGroup) {
    this.symbols = symbols;
    this.winCombinationsByGroup = winCombinationsByGroup;
  }

  /**
   * Matches symbols in a game against the win combinations to identify winning outcomes.
   *<p>
   * For standard symbols, it evaluates each symbol against all win combinations, recording matches. A symbol matches a win combination
   * if it fulfills the specific criteria outlined in the combination's logic, such as occupying a certain pattern of cells in the game
   * matrix.
   * <p>
   * Bonus symbols are checked for their presence in the game. If a bonus symbol is present and not a 'miss' type, it is considered
   * activated and recorded (but only if there is at least one standard symbol match).
   *
   * @param game The current game state, including the placement of symbols.
   * @return A {@link MatchResult} capturing matched win combinations and activated bonus symbols.
   */
  public MatchResult match(Game game) {
    Map<Symbol, Set<WinCombination>> matchedWinCombinations = new HashMap<>();
    symbols.stream()
        .filter(Symbol::isStandard)
        .forEach(symbol -> {
          var wcForSymbol = matchStandardSymbol(symbol, game);
          if (!wcForSymbol.isEmpty()) matchedWinCombinations.put(symbol, wcForSymbol);
        });
    if (matchedWinCombinations.isEmpty()) return new MatchResult(matchedWinCombinations, Collections.emptySet());

    Set<Symbol> matchedBonusSymbols = symbols.stream()
        .filter(Symbol::isBonus)
        .map(symbol -> (BonusSymbol) symbol)
        .filter(symbol -> bonusSymbolMatches(symbol, game))
        .filter(BonusSymbol::isNotMiss)
        .collect(Collectors.toSet());

    return new MatchResult(matchedWinCombinations, matchedBonusSymbols);
  }

  private Set<WinCombination> matchStandardSymbol(Symbol symbol, Game game) {
    var wcForSymbol = new HashSet<WinCombination>();
    winCombinationsByGroup.values()
        .forEach(wcGroup -> wcGroup.stream()
            .filter(wc -> wc.matches(symbol, game))
            .findAny()
            .ifPresent(wcForSymbol::add));

    return wcForSymbol;
  }

  private boolean bonusSymbolMatches(Symbol symbol, Game game) {
    return game.getCells().containsValue(symbol);
  }
}
