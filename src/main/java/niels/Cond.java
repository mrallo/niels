package niels;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Cond {

    interface Term<R> {
        static <R> Term<R> of(final BooleanSupplier p, final Supplier<R> e) {
            return new Term<>() {
                @Override public boolean isTrue() { return p.getAsBoolean(); }
                @Override public R value() { return e.get(); }
            };
        }

        boolean isTrue();

        R value();
    }

    static <R> R cond(BooleanSupplier p1, Supplier<R> e1,
                      BooleanSupplier p2, Supplier<R> e2) {
        return cond(
                p1, e1,
                p2, e2,
                () -> false, () -> null,
                () -> false, () -> null);
    }

    static <R> R cond(BooleanSupplier p1, Supplier<R> e1,
                      BooleanSupplier p2, Supplier<R> e2,
                      BooleanSupplier p3, Supplier<R> e3) {
        return cond(
                p1, e1,
                p2, e2,
                p3, e3,
                () -> false, () -> null);
    }

    static <R> R cond(BooleanSupplier p1, Supplier<R> e1,
                      BooleanSupplier p2, Supplier<R> e2,
                      BooleanSupplier p3, Supplier<R> e3,
                      BooleanSupplier p4, Supplier<R> e4) {
        return Stream.of(
                        Term.of(p1, e1),
                        Term.of(p2, e2),
                        Term.of(p3, e3),
                        Term.of(p4, e4))
                .filter(Term::isTrue)
                .findFirst()
                .map(Term::value)
                .orElseThrow();
    }

}
