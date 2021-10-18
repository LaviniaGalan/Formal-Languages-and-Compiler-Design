package com.company.PIF;

import com.company.Pair;

import java.util.ArrayList;
import java.util.List;

public class PIF {

    private List<Pair<Integer, Pair<Integer, Integer>>> pif = new ArrayList<>();

    public void add(Integer code, Pair<Integer, Integer> value) {
        Pair<Integer, Pair<Integer, Integer>> pair = new Pair<>(code, value);
        pif.add(pair);
    }

    @Override
    public String toString() {
        String result = "";
        for(Pair<Integer, Pair<Integer, Integer>> pair: pif){
            result += pair.getKey() + " | " + pair.getValue().getKey() + " | " + pair.getValue().getValue() + " | ";
            result += "\n _______________________________________ \n";

        }
        return result;
    }
}
