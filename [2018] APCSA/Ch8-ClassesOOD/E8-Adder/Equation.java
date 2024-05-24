public class Equation
{
    private char mode;
    private int answer;
    private String equation;

    public Equation(){
        setMode('+');
        generateEquation();  
    }

    public Equation(char mo){
        setMode(mo);
        generateEquation();
    }

    public void setEquation(int n1, int n2){
        equation = "" + n1 + " " + mode + " " + n2;
        switch(mode){
            case '+': setAnswer(n1+n2); break;
            case '-': setAnswer(n1-n2); break;
            case '/': setAnswer(n1/n2); break;
            case '*': setAnswer(n1*n2); break;
            default : setAnswer(n1+n2); break;
        }
    }

    public void setAnswer(int ans){
        answer = ans;
    }

    public int getAnswer(){
        return answer; 
    }

    public void setMode(){
        char[] modes = {'+','-','/','*'};
        setMode(modes[Misc.random(1,3)]);
    }

    public void setMode(char mo){
        mode = mo;
    }

    public void generateEquation(){
        setEquation(Misc.random(0,20),Misc.random(0,20));
    }

    public String toString(){
        return equation;
    }
    
    public boolean equals(int num){
        return num == answer;
    }
}
