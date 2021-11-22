package com.company;

import com.company.Grammar.Grammar;
import com.company.Parser.Parser;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {

        Grammar g = new Grammar("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\grammars_files\\g1.txt");

        Parser parser = new Parser(g, Arrays.asList("b", "a", "a", "b"));
        parser.descendingRecursiveParsing();
    }
}
