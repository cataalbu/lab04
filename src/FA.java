import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class FA {

    private HashSet<String> states;
    private HashSet<String> alphabet;
    private HashMap<String, HashMap<String, String>> transitions;
    private String initialState;
    private HashSet<String> finalStates;

    public FA() throws FileNotFoundException, NonDeterministicAutomatonException {
        states = new HashSet<>();
        alphabet = new HashSet<>();
        transitions = new HashMap<>();
        finalStates = new HashSet<>();
        readFA();
    }

    public void processStates(String statesString) {
        statesString = statesString.substring(1, statesString.length() - 1);
        String[] statesArray = statesString.split(",");
        for (String state : statesArray) {
            states.add(state);
        }
    }


    private void cleanStrings(String[] strings) {
        for(String s : strings) {
            s = s.trim();
        }
    }

    private void processAlphabet(String alphabetString) {
        alphabetString = alphabetString.substring(1, alphabetString.length() - 1);
        String[] alphabetArray = alphabetString.split(",");
        cleanStrings(alphabetArray);
        for (String a : alphabetArray) {
            alphabet.add(a.trim());
        }
    }

    private void processTransitions(String transitionsString) throws NonDeterministicAutomatonException {
        transitionsString = transitionsString.substring(1, transitionsString.length() - 1);
        String[] transitionsArray = transitionsString.split(",");
        cleanStrings(transitionsArray);
        for (String transition : transitionsArray) {
            transition = transition.substring(1, transition.length() - 1);
            String[] transitionParts = transition.split(" ");
            String fromState = transitionParts[0];
            String label = transitionParts[1];
            String toState = transitionParts[2];
            if (!transitions.containsKey(fromState)) {
                transitions.put(fromState, new HashMap<>());
            }
            var containsKey = transitions.get(fromState).containsKey(label);
            if(containsKey) {
                throw new NonDeterministicAutomatonException("Automaton is not deterministic");
            }
            transitions.get(fromState).put(label, toState);
        }
    }

    private void processInitialState(String initalStateString) {
        initialState = initalStateString;
    }

    private void processFinalStates(String statesString) {
        statesString = statesString.substring(1, statesString.length() - 1);
        String[] statesArray = statesString.split(",");
        cleanStrings(statesArray);
        for (String state : statesArray) {
            finalStates.add(state.trim());
        }
    }

    private void readFA() throws FileNotFoundException, NonDeterministicAutomatonException {
        Scanner sc = new Scanner(new File("src/fa.in"));
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] words = line.split("=");
            switch (words[0]) {
                case "Q":
                    processStates(words[1]);
                    break;
                case "sigma":
                    processAlphabet(words[1]);
                    break;
                case "delta":
                    processTransitions(words[1]);
                    break;
                case "q0":
                    processInitialState(words[1]);
                    break;
                case "F":
                    processFinalStates(words[1]);
                    break;
                default:
                    break;
            }

        }
        sc.close();
    }

    public boolean isSequenceValid(ArrayList<String> sequence) {
        String currentState = initialState;
        if(sequence.get(0) == "")
            return finalStates.contains(currentState);
        for (String symbol : sequence) {
            if (transitions.containsKey(currentState)) {
                if (transitions.get(currentState).containsKey(symbol)) {
                    currentState = transitions.get(currentState).get(symbol);
                } else {
                    return false;
                }
            }
            else {
                return false;
            }

        }
        return finalStates.contains(currentState);
    }

    private ArrayList<String> readSequence() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the sequence (symbols separated by spaces): ");
        String line = scanner.nextLine();
        String[] symbols = line.split("\\s+");
        ArrayList<String> sequence = new ArrayList<>();
        for (String symbol : symbols) {
            sequence.add(symbol);
        }
        return sequence;
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                System.out.println("Finite Automaton Menu:");
                System.out.println("1. Display States");
                System.out.println("2. Display Alphabet");
                System.out.println("3. Display Transitions");
                System.out.println("4. Display Initial State");
                System.out.println("5. Display Final States");
                System.out.println("6. Check if sequence is valid");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");

                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("States: " + states);
                        break;
                    case 2:
                        System.out.println("Alphabet: " + alphabet);
                        break;
                    case 3:
                        System.out.println("Transitions: " + transitions);
                        break;
                    case 4:
                        System.out.println("Initial State: " + initialState);
                        break;
                    case 5:
                        System.out.println("Final States: " + finalStates);
                        break;
                    case 6:
                        scanner.nextLine();
                        ArrayList<String> sequence = readSequence();
                        boolean isValid = isSequenceValid(sequence);
                        if (isValid) {
                            System.out.println("The sequence is valid.");
                        } else {
                            System.out.println("The sequence is not valid.");
                        }
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
                System.out.println();
            } while (choice != 7);

            scanner.close();
        }
}
