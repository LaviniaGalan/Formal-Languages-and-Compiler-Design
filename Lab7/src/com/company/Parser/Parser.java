package com.company.Parser;

import com.company.Grammar.Grammar;

import java.lang.reflect.Array;
import java.util.*;

public class Parser {

    private Grammar grammar;
    private List<String> w = new ArrayList<>();

    private String state;
    private int i;
    private Stack<String> alpha;
    private Stack<String> beta;

    List<String> alphaList = new ArrayList<>();

    public Parser(Grammar grammar, String w) {
        this.grammar = grammar;
        String[] wAsList = w.split(" ");
        this.w.addAll(Arrays.asList(wAsList));

        state = "q";
        i = 0;
        alpha = new Stack<>();
        beta = new Stack<>();
        beta.push(grammar.getStartingSymbol());
    }

    public void descendingRecursiveParsing(){

        grammar.solveLeftRecursivity();
        System.out.println(grammar);

        while (!state.equals("f") && !state.equals("e")){
            if(state.equals("q")){
                if(i == w.size() && beta.empty()){
                    success();
                }
                else if(beta.empty()){
                    back();
                }
                else if(grammar.getNonTerminals().contains(beta.peek())){
                    expand();
                }
                else if(i < w.size() && (beta.peek().equals(w.get(i)) || beta.peek().equals("epsilon"))){
                    advance();
                }
                else {
                    momentaryInsuccess();
                }
            }
            else if(state.equals("b")){
                if(grammar.getTerminals().contains(alpha.peek())){
                    back();
                }
                else {
                    anotherTry();
                }
            }
        }

        if(state.equals("f")){
            System.out.println("Sequence accepted.");
            getAlphaAsList();
            //System.out.println(alphaList);
            getProductionsString();
            getDerivationsString();
        }
        else{
            System.out.println("Error.");
        }
    }

    public void success(){
        state = "f";
        beta.push("epsilon");
    }

    public void expand(){

        String currentNonTerminal = beta.pop();
        alpha.push(currentNonTerminal + " 0");
        List<List<String>> productionsOfCurrentNonTerminal = grammar.getProductionsForNonTerminal(currentNonTerminal);
        List<String> production = productionsOfCurrentNonTerminal.get(0);
        for(int j = production.size() - 1; j >= 0; j--){
            beta.push(production.get(j));
        }

    }

    public void advance(){

        if(! beta.peek().equals("epsilon")){
            i = i + 1;
        }
        alpha.push(beta.pop());

    }

    public void momentaryInsuccess(){

        state = "b";

    }

    public void back(){
        state = "b";
        if(! alpha.peek().equals("epsilon"))
        {
            i = i - 1;
        }
        beta.push(alpha.pop());
    }

    public void anotherTry(){

        String currentProduction = alpha.pop();
        String[] nonTerminalAndProductionNumber = currentProduction.split(" ");
        String currentNonTerminal = nonTerminalAndProductionNumber[0];
        int productionNumber = Integer.parseInt(nonTerminalAndProductionNumber[1]);

        List<List<String>> allProductions = grammar.getProductionsForNonTerminal(currentNonTerminal);

        eliminateProductionsFromBeta(currentNonTerminal, productionNumber);

        if(productionNumber < allProductions.size() - 1){
            int newProductionNumber = productionNumber + 1;
            alpha.push(currentNonTerminal + " " + newProductionNumber);

            List<String> production = allProductions.get(newProductionNumber);
            for(int j = production.size() - 1; j >= 0; j--){
                beta.push(production.get(j));
            }

            state = "q";
        }
        else{
            if(i == 0 && currentNonTerminal.equals(grammar.getStartingSymbol())){
                state = "e";
            }
            else {
                beta.push(currentNonTerminal);
            }
        }

    }

    public void eliminateProductionsFromBeta(String currentNonTerminal, int productionNumber){
        List<String> currentProductionInBeta = grammar.getProductionsForNonTerminal(currentNonTerminal).get(productionNumber);
        int j = 0;
        while (j < currentProductionInBeta.size() && beta.pop().equals(currentProductionInBeta.get(j))){
            j++;
        }
    }


    public void getAlphaAsList(){
        while(! alpha.empty()){
            String element = alpha.pop();
            alphaList.add(element);
        }
        Collections.reverse(alphaList);
    }

    public void getProductionsString(){
        List<String> productions = new ArrayList<>();

        for(String element: alphaList){
            if(! grammar.getTerminals().contains(element)){
                String[] nonTerminalAndProductionNumber = element.split(" ");
                String currentNonTerminal = nonTerminalAndProductionNumber[0];
                int productionNumber = Integer.parseInt(nonTerminalAndProductionNumber[1]);
                List<String> usedProduction = grammar.getProductionsForNonTerminal(currentNonTerminal).get(productionNumber);

                String production = currentNonTerminal + " -> ";
                for(String s: usedProduction){
                    production += s + " ";
                }

                productions.add(production);
            }
        }
        productions.forEach(System.out::println);
    }

    public void getDerivationsString(){
        List<String> derivations = new ArrayList<>();
        derivations.add(grammar.getStartingSymbol());

        for(String element: alphaList) {
            String derivation = derivations.get(derivations.size() - 1);
            if(derivation.contains("epsilon")){
                derivation = derivation.replaceFirst("epsilon ", "").replaceFirst("epsilon", "");
                derivations.add(derivation);
            }

            if (!grammar.getTerminals().contains(element)) {
                String[] nonTerminalAndProductionNumber = element.split(" ");
                String nonTerminal = nonTerminalAndProductionNumber[0];
                int productionNumber = Integer.parseInt(nonTerminalAndProductionNumber[1]);

                if(! nonTerminal.equals("")){
                    List<String> usedProduction = grammar.getProductionsForNonTerminal(nonTerminal).get(productionNumber);

                    String production = "";
                    for(String s: usedProduction){
                        production += s + " ";
                    }
                    production = production.trim();

                    derivation = derivations.get(derivations.size() - 1);
                    derivation = derivation.replaceFirst(nonTerminal, production);
                    derivations.add(derivation);
                }
            }
        }

        for(int i = 0; i < derivations.size() - 1; i++){
            System.out.print(derivations.get(i) + " => ");
        }
        System.out.print(derivations.get(derivations.size() - 1) + "\n");
    }

    public void getParsingTable(){
        List<TableRow> tree = new ArrayList<>();

        int i;

        int tableIndex = 0;

        for(i = 0; i < alphaList.size(); i++){
            TableRow row = new TableRow();
            row.setIndex(tableIndex);

            String symbol = "";
            if(grammar.getTerminals().contains(alphaList.get(i))){
                symbol = alphaList.get(i);
            }
            else{
                String[] nonTerminalAndProductionNumber = alphaList.get(i).split(" ");
                symbol = nonTerminalAndProductionNumber[0];
            }
            row.setSymbol(symbol);
        }

    }


    public void log(String operation) {
        System.out.println("> " + operation);
        System.out.println("Alpha = " + alpha);
        System.out.println("Beta = " + beta);
        System.out.println("state = " + state);
        System.out.println("i = " + i);
        System.out.println("\n\n");
    }
}
