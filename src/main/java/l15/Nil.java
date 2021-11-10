package l15;

public enum Nil implements Atom, ConsExpr {

    INSTANCE;

    @Override
    public boolean atom() {
        return true;
    }

    @Override
    public ConsExpr cons(SExpr into) {
        return ConsExpr.of(INSTANCE, into);
    }

    @Override
    public ConsExpr consRest(SExpr into) {
        return ConsExpr.of(into, INSTANCE);
    }

    @Override
    public boolean eq(Atom other) {
        return other == this;
    }

    @Override
    public boolean equals(ConsExpr other) {
        return other == this;
    }

    @Override
    public SExpr first() {
        return this;
    }

    @Override
    public SExpr rest() {
        return this;
    }

    @Override
    public CharSequence value() {
        return "nil";
    }

}
