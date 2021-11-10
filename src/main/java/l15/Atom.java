package l15;

public interface Atom extends SExpr {

    final class AtomicSymbol implements Atom {
        private final CharSequence symbol;

        private AtomicSymbol(CharSequence symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean atom() {
            return true;
        }

        @Override
        public ConsExpr cons(SExpr into) {
            return ConsExpr.of(this, into);
        }

        @Override
        public ConsExpr consRest(SExpr into) {
            return cons(into);
        }

        @Override
        public boolean eq(Atom other) {
            return value().equals(other.value());
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Atom
                    && eq(Atom.class.cast(other));
        }

        @Override
        public CharSequence value() {
            return symbol;
        }
    }

    static Atom atom(CharSequence symbol) {
        return new AtomicSymbol(symbol);
    }

    @ElementaryFunction
    boolean eq(Atom other);

}
