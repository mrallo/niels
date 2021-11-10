package niels;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface Atom extends SExpr {

    enum Bool {
        FALSE(value -> Optional.empty()),
        TRUE(value -> Optional.of(value));

        private Function<? super Object, Optional<?>> evaluator;

        Bool(Function<? super Object, Optional<?>> evaluator) {
            this.evaluator = evaluator;
        }

        @SuppressWarnings("unchecked")
        <T> Optional<T> ifTrue(T value) {
           return (Optional<T>) evaluator.apply(value); 
        }
    }

    static Atom atom(final CharSequence symbol) {
        return new Atom() {
            @Override public SExpr append(SExpr tail) {
                return wrongTypeArgument();
            }

            @Override public boolean atom() {
                return true;
            }

            @Override public SExpr cons(SExpr into) {
                return SExpr.of(this, into);
            }

            @Override public SExpr consRest(SExpr rest) {
                return SExpr.of(this, rest);
            }

            @Override public boolean eq(Atom other) {
                return Objects.nonNull(other)
                        && this == other
                        || this.toString().equals(other.toString());
            }

            @Override public boolean equals(Object other) {
                return other instanceof Atom
                        && this.eq(Atom.class.cast(other));
            }

            @Override public boolean equals(SExpr other) {
                return other instanceof Atom
                        && this.eq(Atom.class.cast(other));
            }

            @Override public SExpr first() {
                return wrongTypeArgument();
            }

            @Override public boolean isNil() {
                return false;
            }

            @Override public boolean nonNil() {
                return true;
            }

            @Override public SExpr pairlis(SExpr values) {
                return wrongTypeArgument();
            }

            @Override public SExpr rest() {
                return wrongTypeArgument();
            }

            @Override public SExpr subst(SExpr subst, Atom occur) {
                return this.equals(occur)
                        ? subst
                        : this;
            }

            @Override public String toString() {
                return symbol.toString();
            }
        };
    }

    boolean eq(Atom other);

    default Atom wrongTypeArgument() {
        throw new IllegalArgumentException(
                String.format("wrong type argument: %s", this));
    }

}

