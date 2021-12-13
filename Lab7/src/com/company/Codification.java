package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Codification {
    private List<String> reservedWords = Arrays.asList("start", "int", "char", "string", "if", "then",
            "else", "while", "do", "read", "write", "exit");

    private List<String> operators = Arrays.asList("+", "-", "*", "/", "%", "=", "==",
            "<=", "<", ">", ">=", "!=", "&&", "||");

    private List<String> separators = Arrays.asList(";", ",", " ", "(", ")", "{", "}", "[", "]");

    private Map<Integer, String> codificationTable = new HashMap<>();

    public Codification() {
        codificationTable.put(0, "identifier");
        codificationTable.put(1, "constant");

        int i = 2;

        for (String reservedWord : reservedWords) {
            codificationTable.put(i, reservedWord);
            i++;
        }

        for (String operator : operators) {
            codificationTable.put(i, operator);
            i++;
        }

        for (String separator : separators) {
            codificationTable.put(i, separator);
            i++;
        }
    }

    public String getTokenByCode(int code){
        return this.codificationTable.get(code);
    }
}
