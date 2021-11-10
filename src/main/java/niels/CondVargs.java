package niels;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface CondVargs<U, V> {

    interface Test<U, V> {
        static <U, V> Predicate<Test<U, V>> isFalse(U value) {
            return cond -> cond.test(value).isEmpty();
        }

        Optional<Supplier<V>> test(U value);
    }

    @SafeVarargs
    static <U, V> CondVargs<U, V> cond(Test<U, V>... exprs) {
        return value -> Optional.ofNullable(exprs)
                .map(Stream::of)
                .map(seq -> seq.dropWhile(Test.isFalse(value)))
                .flatMap(Stream::findFirst)
                .flatMap(cond -> cond.test(value))
                .map(Supplier::get)
                .orElseThrow(IllegalStateException::new);
    }

    static <U, V> Test<U, V> expr(Predicate<U> cond, Function<U, V> expr) {
        return value -> cond.test(value)
                ? Optional.of(() -> expr.apply(value))
                : Optional.empty();
    }

    V on(U value);

}
