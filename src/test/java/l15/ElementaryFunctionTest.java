package l15;

import static l15.Atom.atom;
import static l15.Reader.sexpr;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ElementaryFunctionTest {

    @Test
    @DisplayName("A S-expression is an atomic symbol")
    void atomicSymbol() {
        var symbol = sexpr("atomic-symbol?");
        assertAll("atom",
                () -> assertTrue(symbol.atom()),
                () -> assertTrue(symbol.eq(atom("atomic-symbol?"))));
    }

    @Test
    @DisplayName("A S-expression is a list of S-expressions")
    void symbolicExpression() {
        var sexpr = (ConsExpr) sexpr("(quote (one two))");
        assertAll("symbolic expression",
                () -> assertEquals(atom("quote"), sexpr.first()),
                () -> assertEquals(sexpr("((one two))"), sexpr.rest()));
    }

}
