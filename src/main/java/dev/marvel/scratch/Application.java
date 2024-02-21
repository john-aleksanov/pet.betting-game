package dev.marvel.scratch;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.marvel.scratch.configuration.ConfigParser;
import dev.marvel.scratch.configuration.Configuration;
import dev.marvel.scratch.configuration.DimensionsParser;
import dev.marvel.scratch.configuration.Parser;
import dev.marvel.scratch.domain.core.Game;
import dev.marvel.scratch.domain.probability.parsers.BonusProbabilitiesParser;
import dev.marvel.scratch.domain.probability.parsers.ProbabilitiesParserDelegate;
import dev.marvel.scratch.domain.probability.parsers.StandardProbabilitiesParser;
import dev.marvel.scratch.domain.scorer.Scorer;
import dev.marvel.scratch.domain.symbol.parsers.SymbolParser;
import dev.marvel.scratch.domain.wincombination.WinCombinationMatcher;
import dev.marvel.scratch.domain.wincombination.parsers.WinCombinationsParser;
import dev.marvel.scratch.infra.GameArgumentParser;
import dev.marvel.scratch.out.FileResultPrinter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Main application class for the scratch game, orchestrating the initialization, configuration parsing, game execution, and result output.
 */
public class Application {

  private final Configuration configuration;
  private final int bet;
  private final WinCombinationMatcher winCombinationMatcher;
  private final Scorer scorer;
  private final FileResultPrinter resultPrinter;

  /**
   * Initializes the application with the necessary components, including the game configuration, betting amount, win combination
   * matcher, scorer, and result printer.
   *
   * @param args Command-line arguments specifying the configuration file and betting amount.
   * @see ConfigParser
   */
  public Application(String[] args) {
    var gameArgumentParser = new GameArgumentParser();
    var arguments = gameArgumentParser.parse(args);
    this.bet = arguments.bet();

    var objectMapper = new ObjectMapper();
    var configParser = configureConfigParser(objectMapper);
    this.configuration = configParser.parse(arguments.configFilename());

    this.winCombinationMatcher = new WinCombinationMatcher(configuration);
    this.scorer = new Scorer();
    this.resultPrinter = new FileResultPrinter(objectMapper);
  }

  public static void main(String[] args) {
    var application = new Application(args);
    application.run();
  }

  private static ConfigParser configureConfigParser(ObjectMapper objectMapper) {
    var parsers = new LinkedHashSet<Parser>();
    parsers.add(new DimensionsParser());
    parsers.add(new SymbolParser(objectMapper));
    parsers.add(new ProbabilitiesParserDelegate(Set.of(new StandardProbabilitiesParser(), new BonusProbabilitiesParser())));
    parsers.add(new WinCombinationsParser(objectMapper));
    return new ConfigParser(parsers);
  }

  /**
   * Executes the game logic. This includes creating a new {@link Game} instance, matching win combinations, scoring the game based on
   * those combinations, and printing the results to a file.
   */
  public void run() {
    var game = new Game(configuration, bet);
    var matchedWinCombinations = winCombinationMatcher.match(game);
    var score = scorer.score(bet, matchedWinCombinations);
    resultPrinter.print(game, matchedWinCombinations, score);
  }
}