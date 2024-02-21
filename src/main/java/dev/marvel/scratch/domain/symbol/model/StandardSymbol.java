package dev.marvel.scratch.domain.symbol.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.experimental.SuperBuilder;

/**
 * Represents a standard symbol in the scratch game, derived from {@link AbstractSymbol}. Standard symbols affect the game's outcome by
 * applying a multiplier to the base score.
 */
@SuperBuilder
public class StandardSymbol extends AbstractSymbol {

  /**
   * Constructs a new StandardSymbol with a specified reward multiplier.
   *
   * @param rewardMultiplier The multiplier applied to the base score for calculating the reward.
   * @throws ConfigurationParseException if the 'reward_multiplier' attribute is not provided in the configuration.
   */
  @JsonCreator
  public StandardSymbol(@JsonProperty("reward_multiplier") Double rewardMultiplier) {
    this.type = Type.STANDARD;
    if (rewardMultiplier == null) {
      throw new ConfigurationParseException("'standard' symbols should have a 'reward_multiplier' attribute.");
    }
    this.value = rewardMultiplier;
  }

  /**
   * Applies the standard symbol's reward multiplier to the base score.
   *
   * @param base The base score to which the standard symbol's multiplier will be applied.
   * @return The modified score after applying the multiplier.
   */
  @Override
  public double apply(double base) {
    return value * base;
  }
}
