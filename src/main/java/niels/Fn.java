package niels;

public interface Fn extends SExpr {

    SExpr eval(SExpr arguments);

}
