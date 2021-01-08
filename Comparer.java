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
        ArrayList<String> stringDumb = readFile(file);
        String[] stringIntended = readClipboard();
        printProblems(stringDumb, stringIntended);
    }

    /**
     * Reads the input file.
     * 
     * @param file the input file to read
     * @return the contents of the file as an ArrayList of Strings
     * @throws IOException if the file is invalid
     */
    private ArrayList<String> readFile(File file) throws IOException {
        Scanner fileScanner = new Scanner(file);
        ArrayList<String> contents = new ArrayList<String>();
        while(fileScanner.hasNext()) {
            contents.add(fileScanner.nextLine());
        }
        return contents;
    }

    /**
     * Reads whatever was last copied to clipboard. Shamelessly stolen from
     * https://stackoverflow.com/questions/43550974/how-to-access-clipboard-data-using-java-in-windows/43550998
     * 
     * @return the contents of the clipboard as an array of Strings
     * @throws Exception if the clipboard contents are invalid
     */
    private String[] readClipboard() throws Exception {
        String clipboard = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        String[] contents = clipboard.split("\\r?\\n");
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
    private ArrayList<Character> compareLine(String file, String clipboard) throws IOException, Exception {
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
        
        // See if either string is longer
        while(i < file.length()) {
            badIndices.add('^');
            i++;
        }
        while(i < clipboard.length()) {
            badIndices.add('^');
            i++;
        }

        return badIndices;
    }

    /**
     * Prints whether the strings match.
     * 
     * @param file the ArrayList of Strings in the input file
     * @param clipboard the array of Strings on the clipboard
     * @throws IOException if the file is invalid
     * @throws Exception if the clipboard contents is invalid
     */
    private void printProblems(ArrayList<String> file, String[] clipboard) throws IOException, Exception {
        System.out.println();

        // Whether strings match:
        String match = "It's a match!";

        // Print line by line
        int i = 0;
        while(i < file.size() && i < clipboard.length) {
            // The lines to compare
            String fileString = file.get(i);
            String clipString = clipboard[i];
            System.out.println(fileString);
            System.out.println(clipString);

            // Whether they're correct
            ArrayList<Character> indices = new ArrayList<Character>();
            indices = compareLine(fileString, clipString);
            for(char index : indices) {
                System.out.print(index);
                if(index == '^') {
                    match = "See errors indicated by ^";
                }
            }
            System.out.println("\n");

            i++;
        }

        // Check for extra lines
        if(i < file.size()) {
            System.out.println("Input file has extra lines");
        }
        if(i < clipboard.length) {
            System.out.println("Clipboard contents has extra lines");
        }

        System.out.println();
        System.out.println(match);
    }
}