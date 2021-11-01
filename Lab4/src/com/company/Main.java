package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String, Map.Entry<String, Method>> commandToMethod = new HashMap<>();

    static FiniteAutomata fa;

    public static void main(String[] args) {

        fa = new FiniteAutomata();
        //fa.readFAFromFile("E:\\CS\\An 3\\FLCD\\Lab4\\src\\com\\company\\in\\nfa.txt");
        fa.readFAFromFile("E:\\CS\\An 3\\FLCD\\Lab4\\src\\com\\company\\in\\fa.txt");

        initializeCommands();

        try (BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                printMenu();
                String command = bufferRead.readLine();
                try {
                    commandToMethod.get(command).getValue().invoke(fa);
                } catch (Exception e) {
                    System.out.println("Invalid option!");
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public static void checkToken(){
        System.out.println("Enter the token: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String token = bufferRead.readLine();
            fa.sequenceIsAcceptedPrint(token);

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void initializeCommands(){
        try {
            commandToMethod.put("1",
                    new AbstractMap.SimpleEntry<>("Show all states.", fa.getClass().getMethod("showAllStates")));
            commandToMethod.put("2",
                    new AbstractMap.SimpleEntry<>("Show alphabet.", fa.getClass().getMethod("showAlphabet")));
            commandToMethod.put("3",
                    new AbstractMap.SimpleEntry<>("Show transitions.", fa.getClass().getMethod("showTransitions")));
            commandToMethod.put("4",
                    new AbstractMap.SimpleEntry<>("Show initial state.", fa.getClass().getMethod("showInitialState")));
            commandToMethod.put("5",
                    new AbstractMap.SimpleEntry<>("Show final states.", fa.getClass().getMethod("showFinalStates")));
            commandToMethod.put("6",
                    new AbstractMap.SimpleEntry<>("Check if the FA is deterministic.", fa.getClass().getMethod("isDFAPrint")));
            commandToMethod.put("7",
                    new AbstractMap.SimpleEntry<>("Check if a token is accepted by FA.", Main.class.getMethod("checkToken")));

            commandToMethod.put("0",
                    new AbstractMap.SimpleEntry<>("Exit.", Main.class.getMethod("exit")));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void printMenu(){
        commandToMethod.entrySet().stream().forEach(
                item -> {System.out.println(item.getKey() + ". " + item.getValue().getKey());}
        );
        System.out.println("Enter your command: >> ");
    }

    public static void exit(){
        System.exit(0);
    }
}
