package com.company.Parser;

import com.company.Grammar.Grammar;

import java.util.*;

public class Parser {

    private Grammar grammar;
    private List<String> w = new ArrayList<>();

    private String state;
    private int i;
    private Stack<String> alpha;
    private Stack<String> beta;


    private boolean onPif;
    private ParserOutput parserOutput;



    public Parser(Grammar grammar, String w, boolean onPif) {
        this.grammar = grammar;
        String[] wAsList = w.split(" ");
        this.w.addAll(Arrays.asList(wAsList));

        state = "q";
        i = 0;
        alpha = new Stack<>();
        beta = new Stack<>();
        beta.push(grammar.getStartingSymbol());

        this.onPif = onPif;
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
                    momentaryInsuccess();
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

            if(onPif){
                parserOutput = new ParserOutput(grammar, alpha, "E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\out\\out2.txt");
            }
            else {
                parserOutput = new ParserOutput(grammar, alpha, "E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\out\\out1.txt");
            }

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
        //state = "b";
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




}
