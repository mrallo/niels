package l15;

public interface SExpr {

    static SExpr nil() {
        return Nil.INSTANCE;
    }

    @ElementaryFunction
    boolean atom();

    @ElementaryFunction
    ConsExpr cons(SExpr into);

    @AuxiliaryFunction
    ConsExpr consRest(SExpr into);

    CharSequence value();
}
