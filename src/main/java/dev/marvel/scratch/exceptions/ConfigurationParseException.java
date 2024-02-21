package dev.marvel.scratch.exceptions;

public class ConfigurationParseException extends RuntimeException {

  public ConfigurationParseException(String message) {
    super(message);
  }

  public ConfigurationParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
