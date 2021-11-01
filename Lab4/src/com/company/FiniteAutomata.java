package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiniteAutomata {
    List<String> states = new ArrayList<>();
    List<String> alphabet = new ArrayList<>();
    List<Transition> transitions = new ArrayList<>();
    String initialState = "";
    List<String> finalStates = new ArrayList<>();

    public void readFAFromFile(String fileName){
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();

            while(line != null){
                line = line.replace(" ", "");
                line = line.replace("{", "");
                line = line.replace("}", "");

                String[] components = line.split("=", 2);

                String component = components[0];
                String set = components[1];

                if(component.equalsIgnoreCase("Q")){
                    String[] states = set.split(",");
                    initializeStatesSet(states);
                }
                else if(component.equalsIgnoreCase("E")){
                    String[] symbols = set.split(",");
                    initializeAlphabet(symbols);
                }
                else if(component.equalsIgnoreCase("Q0")){
                    initialState = set;
                }
                else if(component.equalsIgnoreCase("F")){
                    String[] finalStates = set.split(",");
                    initializeFinalStates(finalStates);
                }
                else if(component.equalsIgnoreCase("D")){
                    String[] transitions = set.split(";");
                    initializeTransitions(transitions);
                }

                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initializeStatesSet(String[] states){
        this.states.addAll(Arrays.asList(states));
    }

    public void initializeAlphabet(String[] symbols){
        this.alphabet.addAll(Arrays.asList(symbols));
    }

    public void initializeFinalStates(String[] finalStates){
        this.finalStates.addAll(Arrays.asList(finalStates));
    }

    public void initializeTransitions(String[] transitions){
        for(String t: transitions){
            this.transitions.add(createTransition(t));
        }
    }

    public Transition createTransition(String t){
        t = t.replace("d(", "").replace(")","");
        String[] elements = t.split("=");
        String resultState = elements[1];
        String[] initialStateAndSymbol = elements[0].split(",");
        String initialState = initialStateAndSymbol[0];
        String symbol = initialStateAndSymbol[1];

        return new Transition(initialState, symbol, resultState);
    }

    public boolean isDFA(){
        for(int i = 0; i < this.transitions.size() - 1; i++){
            for(int j = i + 1; j < this.transitions.size(); j++){
                Transition t1 = this.transitions.get(i);
                Transition t2 = this.transitions.get(j);
                if(t1.state1.equals(t2.state1) && t1.processedSymbol.equals(t2.processedSymbol) &&
                        ! t1.state2.equals(t2.state2)){
                    return false;
                }
            }
        }
        return true;
    }

    public void isDFAPrint(){
        if(isDFA())
            System.out.println("The FA is deterministic.");
        else
            System.out.println("The FA is NOT deterministic.");
    }

    public boolean sequenceIsAccepted(String token){
        String currentState = initialState;
        if(token.equals("") && ! finalStates.contains(currentState)){
            return false;
        }

        while (! (finalStates.contains(currentState) && token.equals(""))){
            boolean found = false;
            for(Transition t: transitions){
                if(t.state1.equals(currentState) && t.processedSymbol.equals(token.charAt(0)+"")){
                    token = token.substring(1);
                    currentState = t.state2;
                    found = true;
                    break;
                }
            }
            if(!found){
                return false;
            }
        }
        return true;
    }

    public void sequenceIsAcceptedPrint(String token){
        if(!isDFA()){
            System.out.println("Not DFA.");
            return;
        }
        if(sequenceIsAccepted(token)){
            System.out.println("Token accepted!");
        }
        else{
            System.out.println("Token NOT accepted!");
        }

    }


    public void showAllStates(){
        System.out.println(this.states);
    }

    public void showAlphabet(){
        System.out.println(this.alphabet);
    }

    public void showTransitions(){
        for(Transition t: transitions){
            System.out.println(t + ";");
        }
    }

    public void showInitialState(){
        System.out.println(this.initialState);
    }

    public void showFinalStates(){
        System.out.println(this.finalStates);
    }


    @Override
    public String toString() {
        return "FiniteAutomata{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", transitions=" + transitions +
                ", initialState='" + initialState + '\'' +
                ", finalStates=" + finalStates +
                '}';
    }
}
