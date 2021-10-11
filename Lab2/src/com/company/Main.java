package com.company;

public class Main {

    public static void main(String[] args) {
		SymbolTable st = new SymbolTable();

		try {
			st.add("ab");
			st.add("a");
			st.add("bca");
			st.add("c");
			st.add("ba");
			st.add("abc");
			st.add("bac");
		}
		catch (Exception e) {
			System.out.println(e);
		}

		try {
			st.add("a");
		}
		catch (Exception e) {
			System.out.println(e);
		}


		System.out.println(st);
		try {
			System.out.println("bca - " + st.getPosition("bca"));
			System.out.println("abc - " +st.getPosition("abc"));
			System.out.println("bac - " +st.getPosition("bac"));
			System.out.println("ab - " +st.getPosition("ab"));
			System.out.println("ba - " +st.getPosition("ba"));
			System.out.println("a - " +st.getPosition("a"));
		}
		catch (Exception e){
			System.out.println(e);
		}

		try {
			System.out.println("cba - " + st.getPosition("cba"));
		}
		catch (Exception e){
			System.out.println("cba - " + e);
		}

    }
}
