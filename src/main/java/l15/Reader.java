package l15;

import static l15.SExpr.nil;
import static niels.Cond.cond;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public interface Reader {

    final class Tokens {
        private static final String SEXPR_COMPLETE = "\\)";
        private static final String SEXPR_NOT_COMP = "[^\\)]+";
        private static final String SEXPR_START = "\\(";

        private static Collector<Scanner, Tokens, SExpr> toSymbols() {
            return Collector.of(
                    Tokens::new,
                    Tokens::parse,
                    Tokens::combine,
                    Tokens::asSymbols);
        }

        private static String whitespaceDelimiter(String text) {
            return text.replaceAll(
                    String.format("(%s|%s)", SEXPR_COMPLETE, SEXPR_START),
                    " $1 ");
        }

        private SExpr sexpr;

        private Tokens() {}

        private SExpr asSymbols() {
            return sexpr;
        }

        private Consumer<? super Scanner> close() {
            return tokens -> {
                try (tokens) {
                    var symbol = tokens.next();
                    syntaxError("unexpected symbol %s", symbol);
                } catch (NoSuchElementException expected) {}
            };
        }

        private Tokens combine(Tokens other) {
            return this;
        }

        private BooleanSupplier completeSExpr(Scanner tokens) {
            return () -> tokens.hasNext(SEXPR_COMPLETE);
        }

        private BooleanSupplier hasNext(Scanner tokens) {
            return () -> tokens.hasNext();
        }

        private BooleanSupplier isEmpty(Scanner tokens) {
            return () -> !tokens.hasNext();
        }


        private void parse(Scanner tokens) {
            Stream.of(tokens)
                  .peek(readFromTokens())
                  .forEach(close());
        }

        private SExpr readFrom(Scanner tokens) {
            return cond(
                    isEmpty(tokens), unexpectedEof(),
                    startSExpr(tokens), readSExpr(tokens),
                    completeSExpr(tokens), unexpectedComplete(),
                    hasNext(tokens), readSymbol(tokens));
        }

        private Consumer<? super Scanner> readFromTokens() {
            return tokens -> sexpr = readFrom(tokens);
        }

        private Supplier<SExpr> readSExpr(Scanner tokens) {
            return () -> {
                var sexpr = nil();
                tokens.next(SEXPR_START);
                while (tokens.hasNext(SEXPR_NOT_COMP)) {
                    sexpr = sexpr.consRest(readFrom(tokens));
                }
                try { tokens.next(SEXPR_COMPLETE); } catch (Exception e) {
                    syntaxError("unbalanced (");
                }
                return sexpr;
            };
        }

        private Supplier<SExpr> readSymbol(Scanner tokens) {
            return () -> Atom.atom(tokens.next());
        }

        private BooleanSupplier startSExpr(Scanner tokens) {
            return () -> tokens.hasNext(SEXPR_START);
        }

        private SExpr syntaxError(String format, Object... args) {
            throw new IllegalArgumentException(String.format(format, args));
        }

        private Supplier<SExpr> unexpectedComplete() {
            return () -> syntaxError("unexpected %s", SEXPR_COMPLETE);
        }

        private Supplier<SExpr> unexpectedEof() {
            return () -> syntaxError("unexpected EOF");
        }
    }

    static SExpr sexpr(CharSequence text) {
        return Stream.of(text)
                .map(String::valueOf)
                .map(Tokens::whitespaceDelimiter)
                .map(Scanner::new)
                .collect(Tokens.toSymbols());
    }

}
