package dev.marvel.scratch.domain.symbol.model;

import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.RequiredArgsConstructor;

/**
 * Enumerates the possible impacts that a symbol can have in the scratch game. Each impact type defines a different effect that a symbol
 * can trigger when it forms part of a winning combination.
 *
 * @see AbstractSymbol for the encompassing object
 */
@RequiredArgsConstructor
public enum Impact {

  /**
   * Indicates that the symbol adds an extra bonus amount to the final reward.
   */
  EXTRA_BONUS("extra_bonus"),

  /**
   * Indicates that the symbol multiplies the final reward by a specific multiplier.
   */
  MULTIPLY_REWARD("multiply_reward"),

  /**
   * Indicates that the symbol does not have any special impact on the reward.
   */
  MISS("miss");

  private final String value;

  /**
   * Converts a string value to its corresponding {@code Impact} enum constant.
   *
   * @param value The string identifier of the impact.
   * @return The {@code Impact} enum constant corresponding to the provided string value.
   * @throws ConfigurationParseException if the provided value does not match any known impact type.
   */
  public static Impact of(String value) {
    for (var element : Impact.values()) {
      if (value.equals(element.value)) {
        return element;
      }
    }
    throw new ConfigurationParseException("Symbol impact " + value + " not supported");
  }
}
