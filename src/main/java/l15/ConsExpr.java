package l15;

public interface ConsExpr extends SExpr {

    final class ConsCell implements ConsExpr {
        private final SExpr first, rest;

        private ConsCell(SExpr first, SExpr rest) {
            this.first = first;
            this.rest = rest;
        }

        @Override
        public boolean atom() {
            return false;
        }

        @Override
        public ConsExpr cons(SExpr into) {
            return new ConsCell(this, into);
        }

        @Override
        public ConsExpr consRest(SExpr into) {
            return new ConsCell(first, rest.consRest(into));
        }

        @Override
        public boolean equals(ConsExpr other) {
            return first.equals(other.first())
                    && rest.equals(other.rest());
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof ConsExpr
                    && equals(ConsExpr.class.cast(other));
        }

        @Override
        public SExpr first() {
            return first;
        }

        @Override
        public int hashCode() {
            return first.hashCode() * rest.hashCode();
        }

        @Override
        public SExpr rest() {
            return rest;
        }

        @Override
        public CharSequence value() {
            return String.format("(%s . %s)", first.value(), rest.value());
        }
    }

    static ConsExpr of(SExpr first, SExpr rest) {
        return new ConsCell(first, rest);
    }

    @AuxiliaryFunction
    boolean equals(ConsExpr other);

    @ElementaryFunction
    SExpr first();

    @ElementaryFunction
    SExpr rest();

}
