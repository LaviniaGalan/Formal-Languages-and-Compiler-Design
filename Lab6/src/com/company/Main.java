package com.company;

import com.company.Grammar.Grammar;
import com.company.Parser.Parser;


public class Main {

    public static void main(String[] args) {

        Grammar g = new Grammar("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\grammars_files\\g3.txt");

        Parser parser = new Parser(g, "x1 c c s s a");
        parser.descendingRecursiveParsing();


//        Grammar g = new Grammar("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\grammars_files\\g1.txt");
//
//        Parser parser = new Parser(g, "b b b b v v v c v");
//        parser.descendingRecursiveParsing();
    }
}
