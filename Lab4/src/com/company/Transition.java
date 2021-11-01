package com.company;

public class Transition {
    String state1;
    String processedSymbol;
    String state2;

    public Transition(String state1, String processedSymbol, String state2) {
        this.state1 = state1;
        this.processedSymbol = processedSymbol;
        this.state2 = state2;
    }

    @Override
    public String toString() {
        return "d(" + this.state1 + ", " + this.processedSymbol + ") = " + this.state2;
    }
}
