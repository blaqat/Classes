package rit.cs;

public class SubExpression implements Expression {
    private Expression left;
    private Expression right;
    public SubExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int evaluate() {
        return left.evaluate() - right.evaluate();
    }

    @Override
    public String emit() {
        return "(" + left.emit() + " - " + right.emit() + ")";
    }
}