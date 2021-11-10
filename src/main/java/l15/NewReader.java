package l15;

import java.util.stream.Collector;
import java.util.stream.Stream;

public interface NewReader {

    final class Tokens {
        private static Collector<Scanner, Tokens, SExpr> toSymbols() {
            return Collector.of(
                    Tokens::new,
                    Tokens::parse,
                    Tokens::combine,
                    Tokens::asSymbols);
        }

        private SExpr symbols;

        private Tokens() {}

        private SExpr asSymbols() {
            return symbols;
        }

        private Tokens combine(Tokens other) {
            return this;
        }

        private void parse(Scanner tokens) {
            symbols = readFrom(tokens);
        }

        private SExpr readFrom(Scanner tokens) {
            return tokens
                    .empty(unexpectedEof())
                    .compl(unexpectedEos())
                    .start(read)
                    .symbl(symbol());
        }
    }

    static SExpr sexpr(CharSequence text) {
        return Stream.of(text)
                .map(Scanner::asTokens)
                .collect(Tokens.toSymbols());
    }

}
