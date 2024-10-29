package edu.lehigh.cse216.team21.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * App is our basic admin app. For now, all it does is connect to the database
 * and then disconnect
 *
 * Default constructor. (For invocation by subclass
 * constructors, typically implicit.)
 */
public class App {
    /*
     * Main of the App that calls mainCliLoop
     */
    public static void main(String[] argv) {
        mainCliLoop(argv);
    }

    /**
     * Entry point for our admin command-line interface program.
     * 
     * Runs a loop that gets a request from the user and processes it
     * using our Database class.
     * 
     * @param argv Command-line options. Ignored by this program.
     */
    public static void mainCliLoop(String[] argv) {
        // Get a fully-configured connection to the database, or exit immediately
        Database db = Database.getDatabase();
        if (db == null) {
            System.err.println("Unable to make database object, exiting.");
            System.exit(1);
        }

        // Start our basic command-line interpreter:
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            // Get the user's request, and do it
            //
            // NB: for better testability, each action should be a separate
            // function call
            char action = prompt(in);
            switch (action) {
                case '?':
                    menu();
                    break;
                case 'q':
                case 'Q':
                    break;
                case 'T':
                case 't':
                    db.createTable();
                    break;
                case 'D':
                case 'd':
                    db.dropTable();
                    break;
                case '1':
                    int id = getInt(in, "Enter the row ID");
                    if (id == -1)
                        continue;
                    Database.RowData res = db.selectOne(id);
                    if (res != null) {
                        System.out.println("--> " + res.mMessage());
                        System.out.println("Likes: " + res.mLikes());
                    }
                    break;
                case '-':
                    int id_1 = getInt(in, "Enter the row ID");
                    if (id_1 == -1)
                        continue;
                    int res_1 = db.deleteRow(id_1);
                    if (res_1 == -1)
                        continue;
                    System.out.println("  " + res_1 + " rows deleted");
                    break;
                case 'f':
                case 'F':
                    db.insertRow("Brownie Bake Fundraiser", 20);
                    db.insertRow("Petition for new chairs for the devlopment department", 44);
                    db.insertRow("Recruit Interns from Lehigh", 89);
                    db.insertRow(
                            "Builds a personal journal app that tracks moods with emojis, daily word counts, and trends over time. It can suggest quotes based on the user's mood, and offer monthly summaries. Users can unlock badges for streaks, and everything stays encrypted to ensure privacy",
                            500);
                    db.insertRow("We should combine Birthday Celebrations per month to save money", 2);
                    System.out.println(5 + " rows added");
                    break;
            }
            if (action == 'q' || action == 'Q') {
                break;
            }
        }
        db.disconnect();
        // Always remember to disconnect from the database when the program
        // exits

    }

    /**
     * Print the menu for our program
     */
    static void menu() {
        System.out.println("Main Menu");
        System.out.println("  [T/t] Create tblData");
        System.out.println("  [D/d] Drop tblData");
        System.out.println("  [F/f] Fill database with test ideas");
        System.out.println("  [1] Query for a specific row");
        System.out.println("  [-] Delete a row");
        System.out.println("  [Q/q] Quit Program");
        System.out.println("  [?] Help (this message)");
    }

    /**
     * Ask the user to enter a menu option; repeat until we get a valid option
     * 
     * @param in A BufferedReader, for reading from the keyboard
     * 
     * @return The character corresponding to the chosen menu option
     */
    static char prompt(BufferedReader in) {
        // The valid actions:
        String actions = "TtDdFf1-Qq?";

        // We repeat until a valid single-character option is selected
        while (true) {
            System.out.print("[" + actions + "] :> ");
            String action;
            try {
                action = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            if (action.length() != 1)
                continue;
            if (actions.contains(action)) {
                return action.charAt(0);
            }
            System.out.println("Invalid Command");
        }
    }

    /**
     * Ask the user to enter a String message
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The string that the user provided. May be "".
     */
    static String getString(BufferedReader in, String message) {
        String s;
        try {
            System.out.print(message + " :> ");
            s = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return s;
    }

    /**
     * Ask the user to enter an integer
     * 
     * @param in      A BufferedReader, for reading from the keyboard
     * @param message A message to display when asking for input
     * 
     * @return The integer that the user provided. On error, it will be -1
     */
    static int getInt(BufferedReader in, String message) {
        int i = -1;
        try {
            System.out.print(message + " :> ");
            i = Integer.parseInt(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }
}