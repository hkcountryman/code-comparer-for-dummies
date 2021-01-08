import java.lang.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;

public class Comparer {

    public static void main(String[] args) throws IOException, Exception {
        // Right number of arguments?
        if(args.length != 1) {
            System.out.println("You must supply one argument in the form of a file to read.");
            System.exit(128);
        // If so, create file object from argument
        } else {
            File file = new File(args[0]);
            new Comparer().run(file);
        }
    }

    /**
     * Executes the program.
     * 
     * @param file the input file to read
     * @throws IOException if the file is invalid
     * @throws Exception if the user's clipboard contents are invalid
     */
    private void run(File file) throws IOException, Exception {
        String stringDumb = readFile(file);
        String stringIntended = readClipboard();
        ArrayList<Character> problems = compare(stringDumb, stringIntended);
        printProblems(stringDumb, stringIntended, problems);
    }

    /**
     * Reads the input file, creating one big string ending in a newline.
     * 
     * @param file the input file to read
     * @return the contents of the file as one string
     * @throws IOException if the file is invalid
     */
    private String readFile(File file) throws IOException {
        Scanner fileScanner = new Scanner(file);
        String contents = "";
        while(fileScanner.hasNext()) {
            contents = contents + fileScanner.nextLine() + "\n";
        }
        return contents;
    }

    /**
     * Reads whatever was last copied to clipboard, ensuring it ends in a newline. Shamelessly stolen from
     * https://stackoverflow.com/questions/43550974/how-to-access-clipboard-data-using-java-in-windows/43550998
     * 
     * @return the contents of the clipboard as one string
     * @throws Exception if the clipboard contents are invalid
     */
    private String readClipboard() throws Exception {
        String contents = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        char lastChar = contents.charAt(contents.length()-1);
        if(lastChar != '\n') {
            contents = contents + "\n";
        }
        return contents;
    }

    /**
     * Compares two strings and notes indices where they don't match.
     * 
     * @param file string from the input file
     * @param clipboard string from the clipboard
     * @return the ArrayList showing indices where the strings don't match
     * @throws IOException if file isn't valid
     * @throws Exception if clipboard isn't valid
     */
    private ArrayList<Character> compare(String file, String clipboard) throws IOException, Exception {
        // ArrayList to hold any indices where the strings diverge
        ArrayList<Character> badIndices = new ArrayList<Character>();

        // Compare char by char
        int i = 0;
        while(i < file.length() && i < clipboard.length()) {
            if(file.charAt(i) != clipboard.charAt(i)) {
                badIndices.add('^');
            } else {
                badIndices.add(' ');
            }
            i++;
        }
        return badIndices;
    }

    private void printProblems(String file, String clipboard, ArrayList<Character> indices) {
        System.out.println();

        // Print file string and clipboard string on top of each other
        System.out.print(file);
        System.out.print(clipboard);

        // Whether strings match:
        String match = "It's a match!";

        // Print where strings don't match
        for(char index : indices) {
            // Print until newline
            
            System.out.print(index);
            if(index == '^') {
                match = "\nSee errors indicated by ^";
            }
        }

        System.out.println();
        System.out.println(match);
    }
}