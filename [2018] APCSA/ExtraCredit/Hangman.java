/*
 * Aiden Green
 * Lol ;00000
 * triggered
 * extra stuff here ;)
 */

public class Hangman
{
    static void main(){
        next(-1);
    }  
    
    static int health = 0;
    static int score = 0;
    static String word = "";
    static String guessed[] = new String[100];
    static String[] category = {};
    static String Categories[][] = {
            //Cars
            {
                "automobile","buggy","cruiser","limousine","hatchback","taxi cab","race car",
                "low rider", "hot rod", "hearse"
            },
            //Adjectives
            {
                "angry","anxious","arrogant","bored","clumsy","confused","cruel","depressed","disgusting",
                "embarrassed","envious","fierce","grumpy","lazy","jealous","thoughtful","mysterious","adorable",
                "annoying","excitable","confident","talented","important","glamorous","loud","blaring","quiet","silent","peaceful",
                "gigantic","immense","mommoth","miniature","wide","skinny","agreeable","amused","successful","helpful","enthsiastic",
                "determined","delightful","courageous","cheerful","enthusiastic","gentle","lively"
            },
            //Food
            {
                "bacon","bagel","blueberry","apple","almond","artichoke","cereal","cantaloupe","cauliflower",
                "dragonfruit","digestive system","dessert","eggplant","endive","french fries","fish","fava bans",
                "garlic","gingerbread","guacamole","honey","hot sauce","hamburger","ice tea","ice cream","jalapeno",
                "junk food","jimmies","kidney beans","kettle corn","ketchup","legumes","lemonade","lettuce","lollipop","macaroni",
                "mandarin orange","mashed potatoes","mozzarella","napkin","nourishment","noodles","oats",
                "orange","oyster","pattypan squash","pomegranate","pumpernickel","peanut butter","pepperoni",
                "quiche","raspberry","refreshments","rolling pin","refrigerator","ravioli","saffron",
                "sub sanwich","sunflower","swiss chard","string bean","spaghetti","sesame seed","scallops",
                "sausage","teriyaki","tomato","tangerine","unleavened","vegetable","vitamin","water chestnut",
                "watercress","watermelon","whipped cream","yam","yogurt"
            },
            //Hardmode?
            {
                "awkward","bagpipes","banjo","bungler","croquet","crypt","dwarves","fervid","fishhook","fjord","gazebo","haphazard","ivory","oxygen","numbskull",
                "pixel","rogue","sphinx","pajama","wildebeest","zombie","xerox","boxcar","beekeeper","blizzard",
                "askew","abruptly","buzzing","rhythm","zoologist","laundry","veterinary","vegetables"
            },
            //Phrases
            {
                "A Man Called Horse",  "Bright light!", "I know kung fu."
            }
        };
    //the category menu
    static void intro(){
        //Intro
        Misc.printTitle("\t\t   Welcome to Hangman!!1!1");
        Misc.println("Choose a category: ");
        Misc.println("[1] Cars\t\t[2] Adjectives\n[3] Food\t\t[4] Hardmode?\n\t    [-1] Random");
        Misc.printBar();
        category = getCategory(Misc.keyboard.nextInt());
        Misc.printBar();
        next(-2);
    }
    //gets the category from user input (intro)
    static String[] getCategory(int type){
        if(type==-1)type=Misc.random(1,Categories.length);
        Misc.print("The category is ");
        switch(type){
            case 1: Misc.println("Cars"); break;
            case 2: Misc.println("Adjectives"); break;
            case 3: Misc.println("Food"); break;
            case 4: Misc.println("Hardmode?"); break;
            default: Misc.println("SECRET CATEGORY!?"); break;
        }
        return Categories[type-1];
    }
    //gets the body parts depending on health
    static String[] getBodyParts(){
        String[] parts = {"","","","","",""};
        if(health>0)parts[0] = "o";
        if(health>1)parts[1] = "|";
        if(health>2)parts[2] = "/";
        if(health>3)parts[3] = "\\";
        if(health>4)parts[4] = "/";
        if(health>5)parts[5] = "\\";
        return parts;
    }
    //handles health and round's concurrent guessed letters
    static void newGuess(String guess){
        for(int i = 0;i<guess.length();i++){
            boolean used = false;
            boolean inword = false;
            int index = 0;
            String letter = guess.substring(i,i+1).toUpperCase();
            for(int a = 0;a<guessed.length;a++){
                if(guessed[a] != null){
                    if(guessed[a].equals(letter)){
                        used = true;
                    }
                    index++;
                }
            }
            for(int a = 0;a<word.length();a++){
                if(word.substring(a,a+1).toUpperCase().equals(letter))inword = true;
            }
            if(!used){
                if(!inword){
                    health++;
                }                
                guessed[index] = letter;
            }
        }
    }
    //prints the hang and body parts
    static void printHang(){
        String[] body = getBodyParts();
        Misc.println("------\\");
        Misc.println("  |    |");
        Misc.println("  |    "+body[0]);
        Misc.println("  |   "+body[2]+body[1]+body[3]);
        Misc.println("  |    "+body[1]);
        Misc.println("  |   "+body[4]+" "+body[5]);
        Misc.println(" _|_");
        Misc.println("|   |______");
        Misc.println("|          |");
        Misc.println("|__________|");
        Misc.printBar();
    }
    //handles round's userinput things
    static void nextRound(){
        boolean quit = false;
        Misc.printTitle("\t\t\t  Hangman!\t\tScore: "+score);
        printHang();
        Misc.println("Guessed Letters:");
        for(int i = 0;i<guessed.length;i++){
            if(guessed[i]!=null){
                Misc.print(guessed[i]);
                if(i!=guessed.length-1)Misc.print(", ");
            }
        }
        Misc.println("");
        Misc.printBar();
        Misc.print("Phrase/Word:");
        Misc.print("\t");
        boolean win = true;
        for(int i = 0;i<word.length();i++){
            boolean guess = false;
            String letter = word.substring(i,i+1);
            if(letter.equals(" ")){
                Misc.print("   ");
                guess = true;
            }
            else if(letter.equals(".") || letter.equals(",") || letter.equals("!") || letter.equals("?") || letter.equals("-") || letter.equals("/")){
                Misc.print(letter + " ");
                guess = true;
            }
            if(!guess){
                for(int a = 0;a<guessed.length;a++){
                    if(guessed[a]!=null && guessed[a].equals(letter.toUpperCase())){Misc.print(letter.toUpperCase()+" "); guess = true;}                
                }
            }
            if(!guess){Misc.print("_ "); win = false;}
        }
        Misc.println("");
        Misc.printBar();
        if(health>5)next(4);
        if(!win){
            Misc.println("Guess letters (or enter -1 if you would like to quit)");
            String guess = Misc.keyboard.next();
            if(!guess.equals("-1"))newGuess(guess);
            else next(3);
        }
        //else return -1;
        Misc.printBar();

        if(win)next(1);
        else next(0);

        //if(win)return 1;
        //else return 0;
    }
    /** next function changes the terminal depending on the number entered
     * States:
     * -2.  Continue? Prompt ->> (2)
     * -1.  Resets the game (except score) and goes to category menu
     *  0.  Plays the next round
     *  1.  You Win message, adds points, prompts to either quit (5), reset (-1), or continue the game (0)
     *  2.  Choses new word, redies round for new round, continues the game (0)
     *  3.  Prompt to either quit (5) or chose a new category (-1)
     *  4.  You Lose message,substracts points,prompts to either quit (5), reset (-1), or continue the game (0)
     *  5.  Game Over!
     */
    static void next(int state){
        if(state==-2){
            Misc.print("Enter anything to continue...");
            Misc.keyboard.next();
            Misc.clear();
            next(2);
        }
        else if(state==-1){
            for(int i = 0;i<guessed.length;i++)guessed[i]=null;
            word = "";
            health = 0;
            Misc.clear();
            intro();
        }
        else if(state == 0){
            Misc.clear();
            nextRound();
        }
        else if(state == 1){
            score+=10*word.length();
            Misc.printTitle("YOU WIN!\t\t\t\tScore: "+score);
            Misc.print("Enter -2 to quit, -1 to choose a new category, or anything else to continue");
            String ui = Misc.keyboard.next();
            if(ui.equals("-2"))next(5);
            else if(ui.equals("-1"))next(-1);
            else next(2);
        }
        else if(state == 4){
            score-=50;
            Misc.printTitle("YOU LOSE!   The word/phrase was: "+word+"\tScore: "+score);
            Misc.print("Enter -2 to quit, -1 to choose a new category, or anything else to continue");
            String ui = Misc.keyboard.next();
            if(ui.equals("-2"))next(5);
            else if(ui.equals("-1"))next(-1);
            else next(2);
        }
        else if(state == 2){
            word = category[Misc.random(0,category.length-1)];
            for(int i = 0;i<guessed.length;i++)guessed[i]=null;
            health = 0;
            Misc.clear();
            next(0);
        }
        else if(state == 3){
            Misc.clear();
            Misc.print("Enter -1 to quit or 1 to choose a new category");  
            int ui = Misc.keyboard.nextInt();
            if(ui==1)next(-1);
            else if(ui==-1)next(10);
        }
        else {
            //score+=100;
            Misc.clear();
            Misc.printTitle("GAME OVER\t\t\t\tScore: "+score);
            //Misc.print("Enter -2 to quit, -1 to choose a new category, or anything else to continue");
            System.exit(0);
        }
    }
}