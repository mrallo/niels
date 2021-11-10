package niels;

public interface SExpr {

    final class ConsExpr implements SExpr {
        private static ConsExpr of(SExpr first, SExpr rest) {
            return new ConsExpr(first, rest);
        }

        private SExpr first, rest;

        private ConsExpr(SExpr first, SExpr rest) {
            this.first = first;
            this.rest = rest;
        }

        @Override public SExpr append(SExpr tail) {
            return first.cons(rest.append(tail));
        }

        @Override public boolean atom() {
            return false;
        }

        @Override public SExpr cons(SExpr into) {
            return ConsExpr.of(this, into);
        }

        @Override public SExpr consRest(SExpr into) {
            return ConsExpr.of(first, rest.consRest(into));
        }

        @Override public boolean equals(Object other) {
            return other instanceof SExpr
                    && equals(SExpr.class.cast(other));
        }

        @Override public boolean equals(SExpr other) {
            return !other.atom()
                    && first.equals(other.first())
                    && rest.equals(other.rest());
        }

        @Override public SExpr first() {
            return first;
        }

        @Override public boolean isNil() {
            return first.isNil() && rest.isNil();
        }

        @Override public boolean nonNil() {
            return first.nonNil() || rest.nonNil();
        }

        @Override public SExpr rest() {
            return rest;
        }

        @Override public SExpr subst(SExpr subst, Atom occur) {
            return first
                    .subst(subst, occur)
                    .cons(rest.subst(subst, occur));
        }

        @Override public String toString() {
            return String.format("(%s . %s)", first, rest);
        }
    }

    enum Single implements SExpr {
        NIL;
    }

    static SExpr nil() {
        return Single.NIL;
    }

    static SExpr of(SExpr first, SExpr rest) {
        return ConsExpr.of(first, rest);
    }

    default SExpr append(SExpr tail) {
        return tail;
    }

    default boolean atom() {
        return false;
    }

    default SExpr cons(SExpr into) {
        return of(nil(), into);
    }

    default SExpr consRest(SExpr into) {
        return of(into, nil());
    }

    default boolean equals(SExpr other) {
        return other == nil();
    }

    default SExpr first() {
        return nil();
    }

    default boolean isNil() {
        return true;
    }

    default boolean member(SExpr sexpr) {
        return sexpr.nonNil()
                && (equals(sexpr.first()) || member(sexpr.rest()));
    }

    default boolean nonNil() {
        return false;
    }

    default SExpr pairlis(SExpr values) {
        return isNil()
                ? nil()
                : of(first(), values.first())
                    .cons(rest().pairlis(values.rest()));
    }

    default SExpr rest() {
        return nil();
    }

    default SExpr subst(SExpr subst, Atom occur) {
        return nil();
    }

}

