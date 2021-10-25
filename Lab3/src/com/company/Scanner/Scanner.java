package com.company.Scanner;

import com.company.PIF.PIF;
import com.company.Pair;
import com.company.SymbolTable.SymbolTable;

import java.io.*;
import java.util.*;

public class Scanner {

    Classifier classifier = new Classifier();

    SymbolTable symbolTable = new SymbolTable();
    PIF pif = new PIF();

    public void scanFile(String fileName){
        List<Pair<String, Integer>> tokensWithLineNumber = new ArrayList<>();
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
                System.out.println(tokensFromLine);
                for(String token: tokensFromLine){
                    tokensWithLineNumber.add(new Pair<String, Integer>(token, i));
                }
                i++;
            }
            fr.close();

            boolean error = buildPIF(tokensWithLineNumber);
            if(! error) {
                System.out.println("Lexically correct!");}
            writeResultsToFile();
            //}

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public List<String> tokenize(String line) {

        List<String> tokensList = new ArrayList<>();

        int currentPosition = 0;

        while (currentPosition < line.length()) {
            if(line.charAt(currentPosition) == '"') {
                String stringConstant = extractStringConstant(line, currentPosition);
                tokensList.add(stringConstant);
                currentPosition += stringConstant.length();
            }
            else if(line.charAt(currentPosition) == '\'') {
                String characterConstant = extractCharacterConstant(line, currentPosition);
                tokensList.add(characterConstant);
                currentPosition += characterConstant.length();
            }
            else if(classifier.isSeparator(line.charAt(currentPosition)+"")) {
                if(! (line.charAt(currentPosition)+"").equals(" ")) {
                    tokensList.add(line.charAt(currentPosition)+"");
                }
                currentPosition++;
            }
            else if(line.charAt(currentPosition) == '-'){
                String token = extractMinus(line, currentPosition);
                tokensList.add(token);
                currentPosition = currentPosition + token.length();
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
        if(currentPosition < line.length() && line.charAt(currentPosition) == '"')
            token += "\"";

        return token;
    }

    public String extractCharacterConstant(String line, int currentPosition){
        String token = "'";
        currentPosition++;

        while (currentPosition < line.length() && line.charAt(currentPosition) != '\''){
            token += line.charAt(currentPosition);
            currentPosition++;
        }
        if(currentPosition < line.length() && line.charAt(currentPosition) == '\'')
            token += "'";

        return token;
    }

    public String extractMinus(String line, int currentPosition){
        if(currentPosition == line.length() - 1)
            return "-";
        int auxPosition = currentPosition + 1;
//        while(auxPosition < line.length() && line.charAt(auxPosition) == ' '){
//            auxPosition++;
//        }
        String token = "-";
        //while(auxPosition < line.length() && Character.isDigit(line.charAt(auxPosition))) {
        while (auxPosition < line.length() &&
                ! classifier.isSeparator(line.charAt(auxPosition) + "")
                && extractOperator(line, auxPosition).equals("")) {
            token += line.charAt(auxPosition);
            auxPosition++;
        }

        auxPosition = currentPosition - 1;
        boolean isConstant = true;
        while(auxPosition >= 0){
            if(line.charAt(auxPosition) != ' '){
                if(!(classifier.isOperator(line.charAt(auxPosition) + "") ||
                        classifier.isSeparator(line.charAt(auxPosition) + ""))){
                    isConstant = false;
                }
                break;
            }
            auxPosition = auxPosition - 1;
        }
        if(isConstant){
            return token;
        }
        else {
            return "-";
        }
    }

    public String extractOperator(String line, int currentPosition){
        if(currentPosition < line.length() - 1) {
            String possibleOperator = line.charAt(currentPosition) + "" + line.charAt(currentPosition + 1);
            if(classifier.isOperator(possibleOperator)){
                return possibleOperator;
            }
        }
        if (classifier.isOperator(line.charAt(currentPosition) + "")){
            return line.charAt(currentPosition) + "";
        }
        return "";
    }

    public String extractOtherToken(String line, int currentPosition){
        String token = "";

        while (currentPosition < line.length() &&
                ! classifier.isSeparator(line.charAt(currentPosition) + "")
                && extractOperator(line, currentPosition).equals("")) {

            token += line.charAt(currentPosition) + "";
            currentPosition++;
        }
        return token;
    }


    public boolean buildPIF(List<Pair<String, Integer>> tokens) {
        boolean error = false;
        for(Pair<String, Integer> tokenWithLineNumber: tokens){
            String token = tokenWithLineNumber.getKey();

            if(classifier.isReservedWord(token) ||
               classifier.isOperator(token) ||
               classifier.isSeparator(token)) {

                int code = classifier.getCode(token);
                pif.add(code, new Pair<>(-1, -1));
            }

            else if(classifier.isIdentifier(token)){
                symbolTable.add(token);
                Pair<Integer, Integer> position = symbolTable.getPosition(token);
                pif.add(0, position);
            }
            else if(classifier.isConstant(token)){
                symbolTable.add(token);
                Pair<Integer, Integer> position = symbolTable.getPosition(token);
                pif.add(1, position);
            }
            else {
                System.out.println("Error at line " + tokenWithLineNumber.getValue() + ": invalid token " + token);
                error = true;
            }
        }
        return error;
    }

    public void writeResultsToFile(){
        try
        {
            File pifFile = new File("E:\\CS\\An 3\\FLCD\\Lab3\\src\\com\\company\\out\\PIF.out");
            FileWriter pifFileWriter = new FileWriter(pifFile);
            BufferedWriter pifWriter = new BufferedWriter(pifFileWriter);
            pifWriter.write(pif.toString());
            pifWriter.close();

            File symbolTableFile = new File("E:\\CS\\An 3\\FLCD\\Lab3\\src\\com\\company\\out\\SymbolTable.out");
            FileWriter symbolTableFileWriter = new FileWriter(symbolTableFile);
            BufferedWriter symbolTableWriter = new BufferedWriter(symbolTableFileWriter);
            symbolTableWriter.write(symbolTable.toString());
            symbolTableWriter.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
