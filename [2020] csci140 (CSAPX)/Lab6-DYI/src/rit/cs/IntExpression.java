package rit.cs;

public class IntExpression implements Expression {
    private int value;
    public IntExpression(int value){
        this.value = value;
    }

    @Override
    public int evaluate() {
        return this.value;
    }

    @Override
    public String emit() {
        return "" + value;
    }
}

