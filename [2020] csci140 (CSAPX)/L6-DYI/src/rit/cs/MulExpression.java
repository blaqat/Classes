package rit.cs;

public class MulExpression implements Expression {
    private Expression left;
    private Expression right;
    public MulExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate() {
        return left.evaluate() * right.evaluate();
    }

    @Override
    public String emit() {
        return "(" + left.emit() + " * " + right.emit() + ")";
    }
}