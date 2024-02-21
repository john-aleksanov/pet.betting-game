package dev.marvel.scratch.domain.symbol.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Collection;
import java.util.Optional;

/**
 * Defines the common behavior and properties of symbols in the scratch game, including standard and bonus types. This interface uses
 * Jackson annotations to support polymorphic JSON deserialization based on the symbol type.
 *
 * @see StandardSymbol for standard symbols that apply a multiplier to the base score.
 * @see BonusSymbol for bonus symbols that provide special game effects.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = StandardSymbol.class, name = "standard"),
    @JsonSubTypes.Type(value = BonusSymbol.class, name = "bonus")
})
public interface Symbol {

  /**
   * A utility static method to search for a Symbol with the specified name within a collection of Symbols.
   *
   * @param collection The collection of Symbols to search.
   * @param name       The name of the Symbol to find.
   * @return An Optional containing the found Symbol, or an empty Optional if not found.
   */
  static Optional<Symbol> findInCollection(Collection<Symbol> collection, String name) {
    return collection.stream()
        .filter(symbol -> symbol.getName().equals(name))
        .findAny();
  }

  String getName();

  void setName(String name);

  Type getType();

  double apply(double base);

  default boolean isStandard() {
    return getType() == Type.STANDARD;
  }

  default boolean isBonus() {
    return getType() == Type.BONUS;
  }
}
