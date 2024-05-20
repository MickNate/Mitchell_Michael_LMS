import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    // Michael N. Mitchell. Software Development I. May 19, 2024.
    // Main class
    // This class contains everything that the user needs to see and interact with.
    // The purpose of this program is to create a library management system.
    // A user will be able to input a file, and the program will be able to remove and add onto such.
    // The program is designed to be usable by the average person.
    public static void main(String[] args) {
        // main function
        // this function will open to the introductory page where the user inputs files
        // There are no arguments or return values for this method

        intro(); //goes to the file input
    }

    static void intro() {
        // the intro function
        // this function will be where the user inputs file information and sees if anything returns
        // to other functions it will be sending a list of strings, scanner, and string with the filename

        Scanner scanner = new Scanner(System.in); //creates scanner for user input
        boolean nameStatus = false; //will only become true when file is found that exists
        File file = null;
        String filename = null;
        System.out.println("Welcome to the Library Management System!\n");
        while (!nameStatus) //loops until a real file is input
        {
            System.out.println("Please input a file name ");
            filename = scanner.nextLine();
            file = new File(filename);
            if (!file.exists()) //if file not found, lets user know and gives some tips
            {
                System.out.println("File does not exist.");
                System.out.println("Make sure you're typing the name correctly and including the extension.");
                System.out.println("Example. If it ends in .txt, include the .txt\n");
            } else {
                System.out.println("File found\n");
                nameStatus = true; //if found, becomes true so while loop can be exited.
            }
        }
            List<String> listOfStrings = new ArrayList<String>(); //creates a list of strings to save file into

            try {
                listOfStrings = Files.readAllLines(Paths.get(filename));
                //receives all lines from file. Each line being a different string
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        interaction(listOfStrings, scanner, filename); //opens the main user interface
        scanner.close(); //closes scanner as there is no more need for input
    }

    static void interaction(List<String> library, Scanner scanner, String filename) {
        // the interaction function.
        // this function is here to allow the user to see and make choices related to the library
        // It takes in a list of strings that contain the library, a scanner, and a string with the filename
        // To other functions it sends out the list of strings containing the library and the scanner.

        String input = null;
        while(!Objects.equals(input, "q")){
            //program will loop continuously until an error or user inputs a quit option.
            System.out.println("\nThe current books in the library are: ");
            for(String eachstring : library) //displays each entry of library file.
            {
                System.out.println(eachstring);
            }
            System.out.println();

            //explains the user options
            System.out.println("Please type the letter and hit enter for the action you wish to do!");
            System.out.println("A: Add a book to library");
            System.out.println("R: Remove a book from library");
            System.out.println("Q: Exit program");
            input = scanner.nextLine();
            switch (input)
            //will take user input and attempt to match with one of the options to interact with library
            {
                case "A":
                case "a":
                    library = addBook(library, scanner); //calls function to add books
                    break;
                case "Q":
                case "q":
                    break;
                case "R":
                case "r":
                    library = removeBook(library, scanner); //calls function to remove books
                    break;

            }
            FileWriter writer = getFileWriter(library, filename); //calls function to write updated library to file
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static FileWriter getFileWriter(List<String> library, String filename) {
        // the get file writer function
        // this function was made to save the updated library to the text file
        // it receives the list of strings with the library entries and the string with the file name
        // the function returns to the filewriter
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(String str: library) {
            try {
                writer.write(str + System.lineSeparator());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return writer;
    }

    static List <String> addBook(List<String> library, Scanner scanner) {
        // the add book function
        // this function is here for the user to add more entries to the library
        // it takes in the library in the form of a list of strings and also takes in a scanner
        // the function returns either the updated library or the previous version

        boolean commaCheck = false;
        String bookName = null;
        String author = null;

        System.out.println("NOTE: Do not input any commas");
        while(!commaCheck)
        // to avoid errors, will not allow user to have a comma in entry.
            // Will not continue until no commas found in inputs.
        {
            System.out.println("Please enter the name of the book you wish to add");
            bookName = scanner.nextLine();
            System.out.println("Please enter the author of the book you wish to add");
            author = scanner.nextLine();
            if(!bookName.contains(",") && !author.contains(",")) {
                commaCheck = true;
            }
            else{
                System.out.println("At least one input has a comma, please retry without the comma");
            }
        }

        String temp = library.get(library.size()-1); //takes the most recent library book
        String previousId = temp.split(",")[0]; //returns the id number of most recent
        int id = Integer.parseInt(previousId) + 1; //converts id number to int and iterates
        String newAddition = id + "," + bookName + "," + author; //creates line for new book
        library.add(newAddition); //adds new line to library

        return library; //returns updated library
    }

    static List <String> removeBook(List<String> library, Scanner scanner){
        // The remove book function
        // The purpose of this function is to let the user be able to remove entries from the library
        // the function takes in the list of strings that is the library and also a scanner to allow user input
        // the function will return the library whether it has been updated or not

        System.out.println("Please enter the id number of the book you wish to remove");
        String bookId = scanner.nextLine();
        System.out.println("Are you sure you wish to remove the book? (y/n)");
        String confirmation = scanner.nextLine();
        switch (confirmation){
            case "Y":
            case "y":
                boolean exists = false;
                String target = null;
                for (String eachstring : library){
                    String beginning = eachstring.split(",")[0]; //gets the id number of each row
                    if (beginning.contains(bookId)) //see if id number of each line matches user input
                    {
                        target = eachstring;
                        exists = true;
                    }
                }
                if(exists) {
                    //removes book from library
                    library.remove(target);
                    System.out.println("Book has been removed");
                }
                else {
                    //will inform user if book id not found and give tips
                    System.out.println("Book not found.");
                    System.out.println("The ID number is at the beginning of a book's line.");
                    System.out.println("Make sure you're inputting the correct number.\n");
                }
                break;
            case "N":
            case "n":
                System.out.println("Removal cancelled");
                break;
            default:
                System.out.println("Input not recognized.");

        }

        return library;
    }
}



