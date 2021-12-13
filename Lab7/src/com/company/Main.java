package com.company;

import com.company.Grammar.Grammar;
import com.company.Parser.Parser;

import java.io.*;


public class Main {

    public static void main(String[] args) {

        Grammar g = new Grammar("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\grammars_files\\g1.txt");

        Parser parser = new Parser(g, getSequence(), false);
        parser.descendingRecursiveParsing();

//
//        Grammar g = new Grammar("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\grammars_files\\g2.txt");
//
//        String progr = getPif();
//        System.out.println(progr);
//        Parser parser = new Parser(g, progr, true);
//        parser.descendingRecursiveParsing();
    }


    public static String getSequence(){
        File file = new File("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\in\\seq.txt");
        String sequence = "";
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while(line != null && ! line.equals("")){
                sequence += line + " ";
                line = br.readLine();
            }
            return sequence;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return sequence;
    }

    public static String getPif() {

        File file = new File("E:\\CS\\An 3\\FLCD\\Lab6\\src\\com\\company\\in\\pif.out");
        String sequence = "";
        Codification codification = new Codification();
        try{
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            while((line = br.readLine()) != null){
                String[] tokens = line.split(" ");
                if(tokens.length > 0){
                    int code = Integer.parseInt(tokens[0]);
                    sequence += codification.getTokenByCode(code) + " ";
                }
            }
            return sequence;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return sequence;
    }
}

