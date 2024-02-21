package dev.marvel.scratch.infra;

/**
 * Holds the arguments necessary for initializing a game session, specifically the configuration file name and the bet amount.
 * This class serves as a data transfer object that encapsulates the command-line parameters parsed by {@link GameArgumentParser}.
 */
public record GameArguments(String configFilename, int bet) {}
