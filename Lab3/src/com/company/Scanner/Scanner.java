package com.company.Scanner;

import com.company.PIF.PIF;
import com.company.Pair;
import com.company.SymbolTable.SymbolTable;

import java.io.*;
import java.util.*;

public class Scanner {

    Classification classification = new Classification();

    SymbolTable symbolTable = new SymbolTable();
    PIF pif = new PIF();

    public void scanFile(String fileName) throws Exception {
        List<Pair<String, Integer>> tokensWithLineNumber = new ArrayList<>();
        List<String> tokens = new ArrayList<>();  // doar pentru afisare
        try
        {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line;
            int i = 1;
            while((line = br.readLine()) != null)
            {
                List<String> tokensFromLine = tokenize(line);
                for(String token: tokensFromLine){
                    tokensWithLineNumber.add(new Pair<String, Integer>(token, i));
                    tokens.add(token);
                }

            }
            fr.close();

            System.out.println(tokens);
            buildPIF(tokensWithLineNumber);
            System.out.println(pif);
            i++;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<String> tokenize(String line) {

        List<String> tokensList = new ArrayList<>();

        int currentPosition = 0;

        while (currentPosition != line.length()) {
            if(line.charAt(currentPosition) == '"') {
                String stringConstant = extractStringConstant(line, currentPosition);
                tokensList.add(stringConstant);
                currentPosition += stringConstant.length();
            }
            else if(classification.isSeparator(line.charAt(currentPosition)+"")) {
                if(! (line.charAt(currentPosition)+"").equals(" ")) {
                    tokensList.add(line.charAt(currentPosition)+"");
                }
                currentPosition++;
            }
            else if(! extractOperator(line, currentPosition).equals("")) {
                String operator = extractOperator(line, currentPosition);
                tokensList.add(operator);
                currentPosition = currentPosition + operator.length();
            }
            else {
                String token = extractOtherToken(line, currentPosition);
                tokensList.add(token);
                currentPosition = currentPosition + token.length();
            }
        }

        return tokensList;
    }

    public String extractStringConstant(String line, int currentPosition){
        String token = "\"";
        currentPosition++;

        while (currentPosition < line.length() && line.charAt(currentPosition) != '"'){
            token += line.charAt(currentPosition);
            currentPosition++;
        }
        token += "\"";

        return token;
    }


    public String extractOperator(String line, int currentPosition){
        if(currentPosition < line.length() - 1) {
            String possibleOperator = line.charAt(currentPosition) + "" + line.charAt(currentPosition + 1);
            if(classification.isOperator(possibleOperator)){
                return possibleOperator;
            }
        }
        if (classification.isOperator(line.charAt(currentPosition) + "")){
            return line.charAt(currentPosition) + "";
        }
        return "";
    }

    public String extractOtherToken(String line, int currentPosition){
        String token = "";

        while (currentPosition < line.length() &&
                ! classification.isSeparator(line.charAt(currentPosition) + "")
                && extractOperator(line, currentPosition).equals("")) {

            token += line.charAt(currentPosition) + "";
            currentPosition++;
        }
        return token;
    }


    public void buildPIF(List<Pair<String, Integer>> tokens) throws Exception {
        PIF pif = new PIF();
        for(Pair<String, Integer> tokenWithLineNumber: tokens){
            String token = tokenWithLineNumber.getKey();

            if(classification.isReservedWord(token) ||
               classification.isOperator(token) ||
               classification.isSeparator(token)) {

                int code = classification.getCode(token);
                pif.add(code, new Pair<>(-1, -1));
            }

            else if(classification.isIdentifier(token)){

                try {
                    symbolTable.add(token);
                }
                catch (Exception e){
                    continue;
                }

                Pair<Integer, Integer> position = symbolTable.getPosition(token);
                pif.add(0, position);
            }
            else if(classification.isConstant(token)){
                try {
                    symbolTable.add(token);
                }
                catch (Exception e){
                    continue;
                }

                Pair<Integer, Integer> position = symbolTable.getPosition(token);
                pif.add(1, position);
            }
            else {
                System.out.println("Error at line " + tokenWithLineNumber.getValue() + ": invalid token " + token);
            }

        }
        System.out.println(pif);
    }
}
