package dev.marvel.scratch.domain.symbol.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.marvel.scratch.exceptions.ConfigurationParseException;
import lombok.experimental.SuperBuilder;

import static dev.marvel.scratch.domain.symbol.model.Impact.MISS;
import static dev.marvel.scratch.domain.symbol.model.Type.BONUS;

/**
 * Represents a bonus symbol in the scratch game, extending the {@link AbstractSymbol} with specific behaviors for bonus effects. Bonus
 * symbols have an impact that alters the game's outcome beyond the standard reward calculation.
 *
 * @implNote Bonus symbols require an 'impact' attribute in the JSON configuration to specify their effect on the game.
 *           The impact dictates whether the symbol adds to, multiplies, or does not alter the base reward.
 */
@SuperBuilder
public class BonusSymbol extends AbstractSymbol {

  private Impact impact;

  /**
   * Constructs a new BonusSymbol with specified reward multipliers, extra values, and impact type.
   *
   * @param rewardMultiplier The multiplier value to be applied to the base reward. Required for MULTIPLY_REWARD impact.
   * @param extra The extra bonus amount to be added to the base reward. Required for EXTRA_BONUS impact.
   * @param impactString The type of impact this bonus symbol has on the game, specified as a string.
   * @throws ConfigurationParseException if required attributes are missing or invalid based on the impact type.
   */
  @JsonCreator
  public BonusSymbol(@JsonProperty("reward_multiplier") Double rewardMultiplier, @JsonProperty("extra") Double extra,
                     @JsonProperty("impact") String impactString) {
    this.type = BONUS;
    if (impactString == null) {
      throw new ConfigurationParseException("'bonus' symbols should have an 'impact' attribute.");
    }
    var impact = Impact.of(impactString);
    if (impact == MISS) {
      this.impact = impact;
      return;
    }
    if (impact == Impact.MULTIPLY_REWARD && rewardMultiplier == null) {
      throw new ConfigurationParseException("'multiply_reward' symbols should have a 'reward_multiplier' attribute.");
    }
    if (impact == Impact.EXTRA_BONUS && extra == null) {
      throw new ConfigurationParseException("'extra_bonus' symbols should have an 'extra' attribute.");
    }
    this.impact = impact;
    this.value = impact == Impact.MULTIPLY_REWARD ? rewardMultiplier : extra;
  }

  /**
   * Applies the bonus symbol's impact to the base score of the game.
   *
   * @param base The base score to which the bonus symbol's impact will be applied.
   * @return The modified score after applying the bonus symbol's impact.
   * @throws IllegalStateException if the impact is unknown, indicating a configuration or logic error.
   */
  @Override
  public double apply(double base) {
    if (impact == Impact.MISS) return base;
    if (impact == Impact.EXTRA_BONUS) return base + value;
    if (impact == Impact.MULTIPLY_REWARD) return base * value;
    throw new IllegalStateException("Unknown impact " + impact);
  }

  public boolean isNotMiss() {
    return impact != MISS;
  }
}
