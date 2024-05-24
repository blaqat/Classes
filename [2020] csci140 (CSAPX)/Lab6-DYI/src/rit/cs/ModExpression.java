package rit.cs;

public class ModExpression implements Expression {
    private Expression left;
    private Expression right;
    public ModExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate() {
        return left.evaluate() % right.evaluate();
    }

    @Override
    public String emit() {
        return "(" + left.emit() + " % " + right.emit() + ")";
    }
}