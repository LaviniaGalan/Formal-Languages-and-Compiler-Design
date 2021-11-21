package com.company;

import com.company.Grammar.Grammar;

public class Main {

    public static void main(String[] args) {

        Grammar g = new Grammar("E:\\CS\\An 3\\FLCD\\Lab5\\src\\com\\company\\grammars\\g1.txt");

//        System.out.println(g.getNonTerminals());
//        System.out.println(g.getTerminals());
//        System.out.println(g.getStartingSymbol());
//        System.out.println(g.getProductions());

        System.out.println(g);

        System.out.println("Productions for the nonterminal S: " + g.getProductionsForNonTerminal("S"));
        System.out.println("Productions for the nonterminal A: " + g.getProductionsForNonTerminal("A"));
        System.out.println("Productions for the nonterminal B: " + g.getProductionsForNonTerminal("B"));

        System.out.println("\nIs cfg: " + g.isCFG());
    }
}
