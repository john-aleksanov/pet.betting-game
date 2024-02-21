package dev.marvel.scratch.domain.symbol.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;


/**
 * Represents a symbol in the scratch game, which can be either a standard or bonus type. Symbols are defined in the 'symbols' attribute
 * of the game configuration JSON and have associated properties such as a name, type, value, and impact, which determine how it
 * contributes to the game's mechanics, especially in calculating rewards.
 *
 * @see Type for the enumeration of symbol types (STANDARD, BONUS).
 * @see Impact for the enumeration of symbol impacts in the game.
 * @see dev.marvel.scratch.domain.symbol.parsers.SymbolParser for parsing 'symbols' from the configuration JSON.
 */
@Getter
@SuperBuilder
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractSymbol implements Symbol {

  @Setter
  @ToString.Include
  @EqualsAndHashCode.Include
  protected String name;
  protected Type type;
  protected Double value;
}
