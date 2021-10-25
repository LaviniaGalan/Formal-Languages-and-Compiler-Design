package com.company.PIF;

import com.company.Pair;

import java.util.ArrayList;
import java.util.List;

public class PIF {

    private List<Pair<Integer, Pair<Integer, Integer>>> pif = new ArrayList<>();

    public void add(Integer code, Pair<Integer, Integer> position) {
        Pair<Integer, Pair<Integer, Integer>> pair = new Pair<>(code, position);
        pif.add(pair);
    }

    @Override
    public String toString() {
        String result = "";
        for(Pair<Integer, Pair<Integer, Integer>> pair: pif){
            result += "\n _______________________________________ \n";
            result += pair.getKey() + " -> (" + pair.getValue().getKey() + ", " + pair.getValue().getValue() + ") ";

        }
        return result;
    }
}
