package com.company.FA;

public class Transition {

    String sourceState;
    String processedSymbol;
    String targetState;

    public Transition(String sourceState, String processedSymbol, String targetState) {
        this.sourceState = sourceState;
        this.processedSymbol = processedSymbol;
        this.targetState = targetState;
    }

    @Override
    public String toString() {
        return "d(" + this.sourceState + ", " + this.processedSymbol + ") = " + this.targetState;
    }


}
