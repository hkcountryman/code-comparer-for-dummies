import java.lang.*;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;

public class Comparer {

    // Array to store args[1] and [2], if they exist
    private static int[] lineNums = new int[2];

    /**
     * Getter method for lineNums, so we may use args[1] and [2] in methods besides main.
     */
    public static int[] getLines() {
        return lineNums;
    }

    // Boolean to indicate existence of args[1] and [2]
    private static boolean moreArgs = false;

    /**
     * Getter method for moreArgs, so we may use it in methods besides main.
     */
    public static boolean getMoreArgs() {
        return moreArgs;
    }

    /**
     * Entry point.
     * 
     * @param args array of arguments: must include one file, plus two optional ints
     * @throws FileNotFoundException if first argument is not a valid filename
     */
    public static void main(String[] args) throws IOException, Exception {
        // Right number of arguments?
        if(args.length <= 3) {
            // Mandatory first argument is file to read
            File file = new File(args[0]);
            if(! file.exists() || file.isDirectory()) {
                System.err.println(args[0] + " is not a valid filename.");
                System.exit(128);
            }
            // Check for 2 extra arguments
            if(args.length == 3) {
                // Confirm both are integers
                try {
                    lineNums[0] = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Argument " + args[1] + " must be a positive integer.");
                    System.exit(128);
                }
                try {
                    lineNums[1] = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    System.err.println("Argument " + args[2] + " must be a positive integer.");
                    System.exit(128);
                }
                // Confirm both are positive
                if(lineNums[0] < 1) {
                    System.err.println("Argument " + lineNums[0] + " must be a positive integer.");
                    System.exit(128);
                }
                if(lineNums[1] < 1) {
                    System.err.println("Argument " + lineNums[1] + " must be a positive integer.");
                    System.exit(128);
                }
                // Confirm args[1] <= args[2]
                if(lineNums[0] > lineNums[1]) {
                    System.err.println("Argument " + lineNums[0] + " must not exceed argument " + lineNums[1] + ".");
                    System.exit(128);
                }
                // If we get this far moreArgs must be true
                moreArgs = true;
            }
            // NOW we're ready to execute program...
            new Comparer().run(file);
        } else { // Wrong number of arguments!
            System.out.print("You must supply one argument in the form of a file to read. ");
            System.out.println("You may supply two more arguments specifying lines to read, e.g.:");
            System.out.println("\tjava Comparer myFile.txt 12 14");
            System.out.println("would read from lines 12-14.");
            System.exit(128);
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

        // If we used optional line number args...
        int[] lines = getLines();
        int begin = lines[0] - 1;
        int end = lines[1];
        if(getMoreArgs()) {
            contents = new ArrayList<String>(contents.subList(begin, end));
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
            System.out.println("File: " + fileString);
            System.out.println("Clip: " + clipString);

            // Whether they're correct
            ArrayList<Character> indices = new ArrayList<Character>();
            indices = compareLine(fileString, clipString);
            System.out.print("      ");
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
            match = "Input file has extra lines";
        }
        if(i < clipboard.length) {
            match = "Clipboard contents has extra lines";
        }

        System.out.println();
        System.out.println(match);
    }
}