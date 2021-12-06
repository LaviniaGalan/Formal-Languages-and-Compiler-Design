package com.company.Grammar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Grammar {

    Set<String> nonTerminals = new HashSet<>();
    Set<String> terminals = new HashSet<>();
    String startingSymbol;
    Map<List<String>, List<List<String>>> productions = new HashMap<>();

    String fileName;

    public Grammar(String fileName){
        this.fileName = fileName;
        readGrammarFromFile();
    }

    public void readGrammarFromFile(){
        try{
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();

            int index = 0;
            while(line != null){
                if(! line.equals("")){
                    if(index == 0){
                        String[] nonTerminals = line.split(" ");
                        initializeNonTerminals(nonTerminals);
                    }
                    else if(index == 1){
                        String[] terminals = line.split(" ");
                        initializeTerminals(terminals);
                    }
                    else if(index == 2){
                        String[] startingSymbol = line.split(" ");
                        this.startingSymbol = startingSymbol[0];
                    }
                    else{
                        this.addProductions(line);
                    }
                    index += 1;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeTerminals(String[] terminals){
        this.terminals.addAll(Arrays.asList(terminals));
    }

    public void initializeNonTerminals(String[] nonTerminals){
        this.nonTerminals.addAll(Arrays.asList(nonTerminals));
    }


    public void addProductions(String line){
        String[] sides = line.split("->");
        String lhs = sides[0];
        String rhs = sides[1];

        String[] lhsSymbols = lhs.split(" ");
        List<String> lhsList = new ArrayList<>(Arrays.asList(lhsSymbols));


        String[] rhsProductions = rhs.split(" \\| ");
        List<List<String>> rhsProd = new ArrayList<>();
        for(String p: rhsProductions){
            String[] rhsSymbols = p.split(" ");
            List<String> rhsList = new ArrayList<>();
            for(String s: rhsSymbols){
                if(! s.equals("")){
                    rhsList.add(s);
                }
            }
            if(!rhsProd.contains(rhsList)){
                rhsProd.add(rhsList);
            }
        }
        if(! this.productions.containsKey(lhsList)){
            this.productions.put(lhsList, rhsProd);
        }
        else{
            List<List<String>> prod = this.productions.get(lhsList);
            for(List<String> newRhs: rhsProd){
                if(! prod.contains(newRhs)){
                    prod.add(newRhs);
                }
            }
        }
    }


    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public String getStartingSymbol() {
        return startingSymbol;
    }

    public Map<List<String>, List<List<String>>> getProductions() {
        return productions;
    }

    public List<List<String>> getProductionsForNonTerminal(String nonT){
        List<String> nonTerminal = Arrays.asList(nonT);
        return this.productions.get(nonTerminal);
    }

    public boolean isCFG(){
        for(List<String> lhs : this.productions.keySet()){
            if(lhs.size() > 1)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String grammar = "---------------------------------------------\n";
        grammar += "GRAMMAR: \n";
        grammar += this.getNonTerminalsAsString();
        grammar += this.getTerminalsAsString();

        grammar += "Starting symbol: " + this.startingSymbol + "\n";
        grammar += "Productions: \n";
        for(List<String> lhs: this.productions.keySet()){
            for(String s: lhs){
                grammar += s;
            }
            grammar += "->";
            for(List<String> p: this.productions.get(lhs)){
                for(String sym: p){
                    grammar += sym + " ";
                }
                grammar += "| ";
            }
            grammar += "\n";
        }
        grammar += "---------------------------------------------";

        return grammar;
    }

    public String getNonTerminalsAsString(){
        String nonTerminals = "";
        nonTerminals += "NonTerminals: ";
        for(String nt: this.nonTerminals){
            nonTerminals += nt + " ";
        }
        nonTerminals += "\n";
        return nonTerminals;
    }

    public String getTerminalsAsString(){
        String terminals = "";
        terminals += "Terminals: ";
        for(String nt: this.terminals){
            terminals += nt + " ";
        }
        terminals += "\n";
        return terminals;
    }


    public void solveLeftRecursivity(){
        Map<List<String>, List<List<String>>> auxProductions = new HashMap<>();

        for(List<String> lhs: productions.keySet()){
            List<List<String>> leftRecursiveProductions = productions.get(lhs)
                    .stream().filter(p -> p.get(0).equals(lhs.get(0)))
                    .collect(Collectors.toList());

            if(leftRecursiveProductions.size() > 0){
                List<List<String>> newProductionsForLHS = new ArrayList<>();
                String auxNonTerminal = lhs.get(0) + "Aux";
                List<List<String>> newProductionsForAux = new ArrayList<>();

                boolean hasBeta = false;

                for(List<String> rhs: productions.get(lhs)){
                    if(! leftRecursiveProductions.contains(rhs)){
                        newProductionsForLHS.add(rhs);
                        newProductionsForLHS.get(newProductionsForLHS.size() - 1).add(auxNonTerminal);
                        hasBeta = true;
                    }
                    else{
                        List<String> newProd = new ArrayList<>();
                        for(int i = 1; i < rhs.size(); i++){
                            newProd.add(rhs.get(i));
                        }
                        newProd.add(auxNonTerminal);
                        newProductionsForAux.add(newProd);
                    }
                }
                newProductionsForAux.add(Arrays.asList("epsilon"));

                if(!hasBeta){
                    throw new RuntimeException("Left recursive production without beta!");
                }
                auxProductions.put(Arrays.asList(auxNonTerminal), newProductionsForAux);
                nonTerminals.add(auxNonTerminal);
                terminals.add("epsilon");
                productions.put(lhs, newProductionsForLHS);
            }
        }

        for(List<String> lhs: auxProductions.keySet()){
            productions.put(lhs, auxProductions.get(lhs));
        }

    }

}
