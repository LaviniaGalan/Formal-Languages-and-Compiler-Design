package com.company.Parser;

import com.company.Grammar.Grammar;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class Parser {

    private Grammar grammar;
    private List<String> w;

    private String state;
    private int i;
    private Stack<String> alpha;
    private Stack<String> beta;

    public Parser(Grammar grammar, List<String> w) {
        this.grammar = grammar;
        this.w = w;

        state = "q";
        i = 0;
        alpha = new Stack<>();
        alpha.push("epsilon");
        beta = new Stack<>();
        beta.push(grammar.getStartingSymbol());
    }

    public void descendingRecursiveParsing(){
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
                else if( i < w.size() && beta.peek().equals(w.get(i))){
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
        log("Before expand");

        String currentNonTerminal = beta.pop();
        alpha.push(currentNonTerminal + " 0");
        List<List<String>> productionsOfCurrentNonTerminal = grammar.getProductionsForNonTerminal(currentNonTerminal);
        List<String> production = productionsOfCurrentNonTerminal.get(0);
        for(int j = production.size() - 1; j >= 0; j--){
            beta.push(production.get(j));
        }

        log("After expand");
    }

    public void advance(){
        log("Before advance");

        i = i + 1;
        alpha.push(beta.pop());

        log("After advance");
    }

    public void momentaryInsuccess(){
        log("Before momentary insuccess");

        state = "b";

        log("After momentary insuccess");
    }

    public void back(){
        log("Before back");
        state = "b";
        i = i - 1;
        beta.push(alpha.pop());
        log("After back");
    }

    public void anotherTry(){
        log("Before another try");

        String currentProduction = alpha.pop();
        String[] nonTerminalAndProductionNumber = currentProduction.split(" ");
        String currentNonTerminal = nonTerminalAndProductionNumber[0];
        int productionNumber = Integer.parseInt(nonTerminalAndProductionNumber[1]);

        List<List<String>> allProductions = grammar.getProductionsForNonTerminal(currentNonTerminal);

        // Eliminam din beta elementele productiei curente:
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

        log("After another try");
    }

    public void eliminateProductionsFromBeta(String currentNonTerminal, int productionNumber){
        List<String> currentProductionInBeta = grammar.getProductionsForNonTerminal(currentNonTerminal).get(productionNumber);
        int j = 0;
        while (j < currentProductionInBeta.size() && beta.pop().equals(currentProductionInBeta.get(j))){
            j++;
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
