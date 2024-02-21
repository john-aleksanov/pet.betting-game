## Notes

### Stack and dependencies
1. Java 17 (although this would also compile with JDK 16 as no sealed types were used in development)
2. Gradle
3. Lombok to avoid boilerplate code
4. Jackson for JSON marshalling / unmarshalling
5. JUnit 5 for testing
6. Mockito for mocking
7. AssertJ for fluent test assertions

### Task
See [task.md](./task.md).

### How to run
Two main options:
1. Either run directly from your favorite IDE; or
2. Build a JAR and execute:
```shell
java -jar <jar-name>
```
In any case, the application expects two arguments:
```shell
--config <config-file-name>
```
for the configuration file (relative to you current working directory) and
```shell
--betting-amount <bet>
```
for an integer bet value.

The output is saved to a `result.json` file in the current working directory.

### Implementation details
1. I'm a huge fan of [test-driven development](https://martinfowler.com/bliki/TestDrivenDevelopment.html), so TDD was used in the course 
   of this project.
2. I'm an even more avid supporter of [domain-driven design](https://martinfowler.com/bliki/DomainDrivenDesign.html) and clean code, so 
   I followed what I believe to be a successful implementation of DDD and clean code mindset. A [hexagonal architecture](https://alistair.cockburn.us/hexagonal-architecture) would be an overkill for this project, but I nevertheless implemented an 
   expressive package structure, where each package and class speaks for itself, and an extensible class structure.
3. It might seem that the Javadocs were prepared with some help of an LLM - and you are not wrong. :) I believe in two things: a) 
   appropriate Javadocs are indispensable for classes and public APIs. They make reading and comprehending a project so much easier; b) 
   LLMs are rapidly changing our approach to work and are very good at analyzing and synthesizing information, so every modern developer 
   should know how to use them and actually use them for their benefit (adhering to any governing legal provisions). So I used an LLM to 
   form the basis of the Javadocs and then edited them appropriately.
4. I made some extensions to the task at hand:
   a) first and foremost, I implemented all optional requirements;
   b) on top of that, the game board does not have to be square: the game supports any rectangular setup. The only requirement is that 
   the row and column attributes for a set of `Cell`s defining the game matrix be contiguous integers starting at `0`;
   c) although the json output in [task.md](task.md) says `applied_bonus_symbol`, I didn't see a requirement that there be a single 
   bonus symbol in the game matrix. So this implementation may generate multiple bonus symbols in a single game. If we require a 
   single bonus symbol max, changes need to be made to the `Game(Configuration configuration, int bet)` constructor to analyze each `var 
   symbol = cp.spin();` call and re-spin any additional bonus symbols.
5. This codebase could have probably been half its current size had I not implemented a robust configuration validation mechanism 
   and expressive error messages (their necessity in every project is something I also believe in).
6. Another reason why the codebase is so large is the configuration json structure. JSON nodes like: `{"symbols": {"A": 
   {"reward_multiplier": 50,"type": "standard"}, ...}}` don't lend themselves to a simple `mapper.readValue(json, Configuration.java)` call.
7. In `CellProbability`, I'm using a `SecureRandom` class to generate game matrix cells. There are many holy wars on using `Random` vs 
   `SecureRandom`. One might argue that, although `Random` uses D. Lehmer's linear congruential generator and is not cryptographically 
   secure, our purposes are not secure-critical, but I disagree with this assessment. Betting / gambling is exactly where `SecureRandom` 
   should be used.
8. Since this is not a production project, certain shortcuts have been taken and certain values have been hardcoded. In a production 
   setting, the following improvements would have been made:
   1. A logging framework like Logback or Log4J2 would have been implemented and meaningful logging messages provided together with an 
      exception handling mechanism.
   2. A DI framework would have been used. Spring would be an overkill, but Dagger (compile-time) or Guice (runtime) would do just fine 
      here.
   3. Tests would be extended to provide more coverage and test for more edge use cases. The `CellProbability` class could have been 
      reworked to get the `SecureRandom` dependency through a constructor to lend itself to unit testing without the need to mock the 
      currently static `SecureRandom` class member.
   4. Depending on further requirements and extensions, certain classes might have been reworked into an interface and additional 
      implementations. For example, `FileResultPrinter` could be made to implement a `ResultPrinter` interface, and another 
      implementation could be provided that'd print the results on the screen / console.