package dev.marvel.scratch.domain.symbol.model;

/**
 * Enumerates the types of symbols that can appear in the scratch game. Symbols are categorized into types to differentiate their roles and
 * effects within the game, such as standard symbols that contribute to basic winning combinations and bonus symbols that trigger special
 * effects.
 */
public enum Type {

  /**
   * Symbol type for symbols that offer bonus effects or additional rewards.
   */
  BONUS,

  /**
   * Symbol type for the standard gameplay symbols used in forming winning combinations.
   */
  STANDARD;
}
