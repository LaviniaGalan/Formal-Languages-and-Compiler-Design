package com.company.SymbolTable;

import com.company.Pair;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    private static final int M = 7;

    private List<Node> hashTable = new ArrayList<>();

    public SymbolTable() {
        for(int i = 0; i < M; i++) {
            hashTable.add(null);
        }
    }

    private int hashFunction(String value){
        int sum = 0;
        for(int i = 0; i < value.length(); i++) {
            sum += value.charAt(i);
        }
        return sum % M;
    }

    public void add(String value){

        int position = this.hashFunction(value); // computing the position in the hash table

        Node toInsert = new Node(value);  // creating a new Node with the added value
        Node current = hashTable.get(position);
        Node previous = current;

        if(current == null) {
            hashTable.set(position, toInsert);  // if there is no element at the computed position, the Node is added at that position in the array
        }
        else {  // else we parse the linked list from that position until its end, and insert the new Node there

                while(current != null) {
                    if(current.getValue().equals(value)){
                        return;
                    }
                    previous = current;
                    current = current.getNext();
                }
                previous.setNext(toInsert);

        }
    }


    public Pair<Integer, Integer> getPosition(String value) {
        int positionInArray = this.hashFunction(value); // we compute the position where the element should be
        int positionInLinkedList = 0;

        Node current = hashTable.get(positionInArray);  // we start searching for the element in the linked list associated with the computed position
        while(current != null && !current.getValue().equals(value)) {
            positionInLinkedList++;
            current = current.getNext();
        }

        if(current == null) {  // if we reached the end of the linked list, this means that the element is not in the symbol table
            return new Pair<>(-1, -1);
        }

        return new Pair<>(positionInArray, positionInLinkedList);
    }


    @Override
    public String toString() {
        String result = "";
        for(int i = 0; i < M; i++) {
            result += i + " -- ";
            Node current = hashTable.get(i);
            while(current != null) {
                result += current.getValue() + " -> ";
                current = current.getNext();
            }
            result += "\n";
        }
        return result;
    }


}
