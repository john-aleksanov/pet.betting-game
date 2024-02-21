package dev.marvel.scratch.configuration;

import com.fasterxml.jackson.databind.JsonNode;

public interface Parser {

  void parse(JsonNode configRoot, Configuration configuration);
}
