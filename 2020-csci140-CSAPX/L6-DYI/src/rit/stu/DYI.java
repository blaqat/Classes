package rit.stu;
import rit.cs.*;

import java.util.Arrays;
import java.util.Scanner;

public class DYI {
    private String[] baseTokens;
    public DYI() {
    }
    public void setTokens(String expressions){
        this.baseTokens = expressions.split(" ");
    }
    public Expression parse(String[] tokens){
        String token = tokens[0];
        System.arraycopy(tokens, 1, tokens, 0, tokens.length - 1);

        switch(token){
            case "*":
                return new MulExpression(parse(tokens), parse(tokens));
            case "/":
                return new DivExpression(parse(tokens), parse(tokens));
            case "+":
                return new AddExpression(parse(tokens), parse(tokens));
            case "-":
                return new SubExpression(parse(tokens), parse(tokens));
            case "%":
                return new ModExpression(parse(tokens), parse(tokens));
            default:
                return new IntExpression(Integer.parseInt(token));
        }
    }
    public void run(){
        Scanner input = new Scanner(System.in);
        String tokens = "";
        System.out.print("Derp Your Interpreter v1.0 :)\n>\t");
        while(!(tokens = input.nextLine()).equals("quit")) {
            setTokens(tokens);
            Expression e = parse(this.baseTokens);
            System.out.println("Emit:\t" + e.emit());
            System.out.println("Evaluate:\t" + e.evaluate());
            System.out.print(">\t");
        }
        System.out.println("Goodbye! :(");
    }
    public static void main(String[] args) {
        DYI derp = new DYI();
        derp.run();
    }
}
