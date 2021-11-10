package niels;

import static niels.Atom.atom;
import static niels.Reader.sexpr;
import static niels.SExpr.nil;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReaderTest {

    @Test
    @DisplayName("Append auxiliary function")
    void append() {
        var sexpr = sexpr("((a 1))").append(sexpr("((b 2))"));
        assertEquals(sexpr("((a 1) (b 2))"), sexpr);
    }

    @Test
    @DisplayName("A S-expression is an atomic symbol")
    void atomicSymbol() {
        var symbol = sexpr("atomic-symbol?");
        assertAll("atom",
                () -> assertTrue(symbol.atom()),
                () -> assertEquals(atom("atomic-symbol?"), symbol));
    }

    @Test
    @DisplayName("Atom occurs in list")
    void atomMemberOfList() {
        assertTrue(atom("quote").member(sexpr("(quote (one two))")));
    }

    @Test
    @DisplayName("The null list () is identical to NIL")
    void nilEmptyListEquivalence() {
        assertAll(
                () -> assertTrue(sexpr("()").equals(nil())),
                () -> assertTrue(sexpr("()").isNil()));
    }

    @Test
    @DisplayName("Gives list of pairs of corresponding elements of two lists")
    void pairlis() {
        var association = sexpr("(a b)").pairlis(sexpr("(1 2)"));
    }

    @Test
    @DisplayName("S-expression occurs in list")
    void sexprMemberOfList() {
        assertTrue(
                sexpr("(quote one)").member(sexpr("(def fn (x) (quote one))")));
    }

    @Test
    @DisplayName("Substituting a S-expression for all occus of atom")
    void subtst() {
        var sexpr = sexpr("((quote one) two)")
                .subst(atom("let"), atom("quote"))
                .subst(sexpr("(one 1)"), atom("one"));
        assertEquals(sexpr("(let (one 1))"), sexpr.first());
    }

    @Test
    @DisplayName("A S-expression is a list of S-expressions")
    void symbolicExpression() {
        var sexpr = sexpr("(quote (one two))");
        assertAll("symbolic expression",
                () -> assertEquals(Atom.atom("quote"), sexpr.first()),
                () -> assertEquals(sexpr("((one two))"), sexpr.rest()));
    }
}
