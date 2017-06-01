package automata;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.*;
import java.io.*;

import automata.*;
import utils.Quintuple;

public class Main{

	public static void main(String[] args) {
		
	  Set<State> states = new HashSet<State>();
    Set<Character> alphabet = new HashSet<Character>();
    Set<Character> stackAlphabet = new HashSet<Character>();
    Set<Quintuple<State, Character,Character,String, State>> transitions = new HashSet<Quintuple<State, Character,Character,String, State>>();
    Character stackInitial = null;
    State initial = null;
    Set<State> final_states = new HashSet<State>();
		
		DFAPila dfap = new DFAPila(states,alphabet,stackAlphabet,transitions,stackInitial,initial,final_states);

		dfap.from_dot(new File("automata/sample.txt"));
		System.out.println(dfap.initial_state().name());

	}
}