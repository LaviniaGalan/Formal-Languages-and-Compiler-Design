package com.company.Parser;

public class TableRow {
    int index;

    String symbol;

    int parent;

    int rightSibling;

    public TableRow(int index, String symbol, int parent, int rightSibling) {
        this.index = index;
        this.symbol = symbol;
        this.parent = parent;
        this.rightSibling = rightSibling;
    }

    public TableRow() {
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setRightSibling(int rightSibling) {
        this.rightSibling = rightSibling;
    }

    public int getIndex() {
        return index;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getParent() {
        return parent;
    }

    public int getRightSibling() {
        return rightSibling;
    }

    @Override
    public String toString() {
        return String.format("%-7d%-15s%-7d%-7d", index, symbol, parent, rightSibling);
    }
}
