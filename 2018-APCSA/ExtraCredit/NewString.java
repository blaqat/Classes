
/**
 * Write a description of class NewString here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class NewString
{
    static void main(){
        Misc.print("Enter a sentence: ");
        String string = Misc.keyboard.nextLine();
        Misc.print("Enter the remove: ");
        String remove = Misc.keyboard.nextLine();

        int slength, index = 0, rlength;
        String removedString,restOfString = "";
        while(index!=-1){
            slength = string.length();
            index = string.indexOf(remove);
            rlength = index+remove.length();    
            
            removedString = string.substring(0,index);
            restOfString = string.substring(rlength);
            
            Misc.print(removedString);
            string = restOfString;
        }

    }
}
