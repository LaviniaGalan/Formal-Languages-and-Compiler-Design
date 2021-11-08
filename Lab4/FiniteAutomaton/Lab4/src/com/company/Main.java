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

    static FiniteAutomaton fa;

    public static void main(String[] args) {

        fa = new FiniteAutomaton("E:\\CS\\An 3\\FLCD\\Lab4\\src\\com\\company\\in\\fa.txt");
        //fa = new FiniteAutomaton("E:\\CS\\An 3\\FLCD\\Lab4\\src\\com\\company\\in\\nfa.txt");

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
        System.out.println("Enter the sequence: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            String token = bufferRead.readLine();
            showIfSequenceIsAccepted(token);

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public static void showIfSequenceIsAccepted(String token){
        if(!fa.isDFA()){
            System.out.println("Not DFA.");
            return;
        }
        if(fa.sequenceIsAccepted(token)){
            System.out.println("Sequence accepted!");
        }
        else{
            System.out.println("Sequence NOT accepted!");
        }
    }

    public static void initializeCommands(){
        try {
            commandToMethod.put("1",
                    new AbstractMap.SimpleEntry<>("Show all states.", Main.class.getMethod("showAllStates")));
            commandToMethod.put("2",
                    new AbstractMap.SimpleEntry<>("Show alphabet.", Main.class.getMethod("showAlphabet")));
            commandToMethod.put("3",
                    new AbstractMap.SimpleEntry<>("Show transitions.", Main.class.getMethod("showTransitions")));
            commandToMethod.put("4",
                    new AbstractMap.SimpleEntry<>("Show initial state.", Main.class.getMethod("showInitialState")));
            commandToMethod.put("5",
                    new AbstractMap.SimpleEntry<>("Show final states.", Main.class.getMethod("showFinalStates")));
            commandToMethod.put("6",
                    new AbstractMap.SimpleEntry<>("Check if the FA is deterministic.", Main.class.getMethod("showIsDFA")));
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

    public static void showAllStates(){
        System.out.println(fa.getStates());
    }

    public static void showAlphabet(){
        System.out.println(fa.getAlphabet());
    }

    public static void showInitialState(){
        System.out.println(fa.getInitialState());
    }

    public static void showFinalStates(){
        System.out.println(fa.getFinalStates());
    }

    public static void showTransitions(){
        for(Transition t: fa.getTransitions()){
            System.out.println(t + ";");
        }
    }

    public static void showIsDFA(){
        if(fa.isDFA())
            System.out.println("The FA is deterministic.");
        else
            System.out.println("The FA is NOT deterministic.");
    }

    public static void exit(){
        System.exit(0);
    }
}
