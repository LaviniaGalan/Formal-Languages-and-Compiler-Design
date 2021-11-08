package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FiniteAutomaton {
    private List<String> states = new ArrayList<>();
    private List<String> alphabet = new ArrayList<>();
    private List<Transition> transitions = new ArrayList<>();
    private String initialState = "";
    private List<String> finalStates = new ArrayList<>();

    String fileName;

    public FiniteAutomaton(String fileName){
        this.fileName = fileName;
        readFAFromFile();
    }

    public void readFAFromFile(){
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();

            int index = 0;
            while(line != null){
                if(index == 0){
                    String[] states = line.split(" ");
                    initializeStatesSet(states);
                }
                else if(index == 1){
                    String[] symbols = line.split(" ");
                    initializeAlphabet(symbols);
                }
                else if(index == 2){
                    String[] initialState = line.split(" ");
                    this.initialState = initialState[0];
                }
                else if(index == 3){
                    String[] finalStates = line.split(" ");
                    initializeFinalStates(finalStates);
                }
                else{
                    addTransition(createTransition(line));
                }

                line = reader.readLine();
                index += 1;
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
        for(String symbol: symbols){
            if (symbol.equals("blank")){
                this.alphabet.add(" ");
            }
            else{
                this.alphabet.add(symbol);
            }
        }
    }

    public void initializeFinalStates(String[] finalStates){
        this.finalStates.addAll(Arrays.asList(finalStates));
    }

    public void addTransition(Transition transition){
        if(! this.transitions.contains(transition)){
            this.transitions.add(transition);
        }
    }

    public Transition createTransition(String t){
        String[] components = t.split(" ");
        String sourceState = components[0];
        String symbol = components[1];
        String targetState = components[2];

        if (symbol.equals("blank")){
            symbol = " ";
        }

        return new Transition(sourceState, symbol, targetState);
    }

    public boolean isDFA(){
        for(int i = 0; i < this.transitions.size() - 1; i++){
            for(int j = i + 1; j < this.transitions.size(); j++){
                Transition t1 = this.transitions.get(i);
                Transition t2 = this.transitions.get(j);
                if(t1.sourceState.equals(t2.sourceState) && t1.processedSymbol.equals(t2.processedSymbol) &&
                        ! t1.targetState.equals(t2.targetState)){
                    return false;
                }
            }
        }
        return true;
    }


    public boolean sequenceIsAccepted(String token){
        String currentState = initialState;
        if(token.equals("") && ! finalStates.contains(currentState)){
            return false;
        }

        while (! (finalStates.contains(currentState) && token.equals(""))){
            boolean found = false;
            for(Transition t: transitions){
                if(t.sourceState.equals(currentState) && t.processedSymbol.equals(token.charAt(0)+"")){
                    token = token.substring(1);
                    currentState = t.targetState;
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

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
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
