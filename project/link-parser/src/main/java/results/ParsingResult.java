package results;

public sealed interface ParsingResult permits GithubParsingResult, StackOverflowParsingResult { }
