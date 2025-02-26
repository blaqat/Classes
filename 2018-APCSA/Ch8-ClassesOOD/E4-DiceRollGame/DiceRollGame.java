public class DiceRollGame
{
    static DRPlayer user = new DRPlayer();
    public static void main(){
        int risk, call;
        do{
            user.displayPoints();
            do{
                Misc.print("How many points do you want to risk? (-1 to quit) ");
                risk = Misc.keyboard.nextInt();
            }
            while(!(risk <= user.getPoints() && risk > 0));

            if(risk != -1){
                Misc.print("Make a call (0 for low, 1 for high): ");
                call = Misc.keyboard.nextInt();

                Misc.println("You rolled: " + user.rollDice());

                if( (user.isHigh() && !(call == 1)) || (user.isLow() && !(call == 0)))
                    user.subtractPoints(risk);
                else if(!user.isLow() && !user.isHigh())
                    user.subtractPoints(risk);
                else
                    user.addPoints(risk*2);
            }
        }
        while(risk != -1);

        if(user.getPoints() > 0){
            Misc.print("\n You've made " + user.getPoints());
        }
        else if(user.getPoints() < 0)
            Misc.print("\n You've lost " + user.getPoints());
        else
            user.displayPoints();
    }
}
