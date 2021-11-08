package com.company.Scanner;

import com.company.FA.FiniteAutomaton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassifierWithFA {
    private List<String> reservedWords = Arrays.asList("start", "int", "char", "string", "if", "then",
            "else", "while", "do", "read", "write", "exit");

    private List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "=", "==",
            "<=", "<", ">", ">=", "!=", "&&", "||");

    private List<String> separators = Arrays.asList(";", ",", " ", "(", ")", "{", "}", "[", "]");

    private FiniteAutomaton faIdentifiers;
    private FiniteAutomaton faNumericConstants;

    public ClassifierWithFA() {
        faIdentifiers = new FiniteAutomaton("E:\\CS\\An 3\\FLCD\\Scanner\\src\\com\\company\\FA\\faidentifiers.txt");
        faNumericConstants = new FiniteAutomaton("E:\\CS\\An 3\\FLCD\\Scanner\\src\\com\\company\\FA\\fanumericconstants.txt");


        codificationTable.put("identifier", 0);
        codificationTable.put("constant", 1);

        int i = 2;

        for (String reservedWord: reservedWords) {
            codificationTable.put(reservedWord, i);
            i++;
        }

        for(String operator: operators) {
            codificationTable.put(operator, i);
            i++;
        }

        for(String separator: separators) {
            codificationTable.put(separator, i);
            i++;
        }
    }

    private Map<String, Integer> codificationTable = new HashMap<>();

    public boolean isReservedWord(String token){
        return reservedWords.contains(token);
    }

    public boolean isOperator(String token){
        return operators.contains(token);
    }

    public boolean isSeparator(String token){
        return separators.contains(token);
    }

    public boolean isIdentifier(String token){
//        String pattern = "^[a-zA-Z]([a-zA-Z|0-9|_])*$";
//        return token.matches(pattern);

        return faIdentifiers.sequenceIsAccepted(token);
    }

    public boolean isConstant(String token){
        // String numericPattern = "^0|[-]?[1-9]([0-9])*$";
        String charPattern = "^\'[a-zA-Z0-9_?!#*. ]\'";
        String stringPattern = "^\"[a-zA-Z0-9_?!#*. ]+\"";
        return
                token.matches(charPattern) ||
                token.matches(stringPattern) ||
                        faNumericConstants.sequenceIsAccepted(token);
    }

    public Integer getCode(String token) {
        return codificationTable.get(token);
    }
}
