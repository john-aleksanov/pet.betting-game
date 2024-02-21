package dev.marvel.scratch.infra;


/**
 * Parses command-line arguments provided to the game application, specifically looking for configuration file information and betting
 * amount. This parser ensures that the necessary arguments for running the game are provided and correctly formatted.
 */
public class GameArgumentParser {

  /**
   * Parses the input arguments from the command line to extract and validate the game configuration file name and the betting amount.
   * <p>
   * The method expects exactly two arguments: {@code --config} followed by the configuration file name, and {@code --betting-amount}
   * followed by an integer representing the betting amount. It validates the presence and format of these arguments, throwing
   * {@link IllegalArgumentException} for any discrepancies.
   *
   * @param args The command-line arguments provided to the game application.
   * @return A {@link GameArguments} object containing the parsed configuration file name and betting amount.
   * @throws IllegalArgumentException If the arguments are missing, exceed the expected count, are in an incorrect format, or if the
   * betting amount is not a valid integer.
   */
  public GameArguments parse(String[] args) {
    if (args.length != 4) {
      throw new IllegalArgumentException("Please specify exactly two arguments for the jar command, including: " +
          "'--config <filename>' and '--betting-amount <amount>'");
    }

    var configFilename = "";
    var bettingAmount = 0;

    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "--config":
          configFilename = args[++i];
          break;
        case "--betting-amount":
          try {
            bettingAmount = Integer.parseInt(args[++i]);
          } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please specify a valid integer for --betting-amount");
          }
          break;
        default:
          throw new IllegalArgumentException(String.format("Unknown argument: %s. Please specify exactly two arguments " +
              "for the jar command, including: '--config <filename>' and '--betting-amount <amount>'", args[i]));
      }
    }
    return new GameArguments(configFilename, bettingAmount);
  }
}