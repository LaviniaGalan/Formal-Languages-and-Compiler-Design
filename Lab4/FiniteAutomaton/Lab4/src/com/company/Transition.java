package com.company;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return Objects.equals(sourceState, that.sourceState) && Objects.equals(processedSymbol, that.processedSymbol) && Objects.equals(targetState, that.targetState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceState, processedSymbol, targetState);
    }
}
