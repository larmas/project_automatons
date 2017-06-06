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

		dfap.from_dot(new File("text/sample.txt"));

		boolean result = dfap.accepts("ab");
		System.out.println("Automata acepta la cadena: "+result);
		//dfap.to_empty_stack();
		dfap.to_final_state();
		String to_dot = dfap.to_dot();

		try{
			String newFileDot = new String("text/automaton.txt");
			File file = new File(newFileDot);

			BufferedWriter bw = new BufferedWriter(new FileWriter(newFileDot));
			bw.write(to_dot);

			bw.close();
		}catch(Exception e){
         e.printStackTrace();
    }
    try{
    	String dotPath = "/usr/local/bin/dot";
	    String fileInputPath = "text/automaton.txt";
	    String fileOutputPath = "output/automaton.jpg";
	    String tParam = "-Tjpg";
	    String tOParam = "-o";

	    String[] cmd = new String[5];
	    cmd[0] = dotPath;
	    cmd[1] = tParam;
	    cmd[2] = fileInputPath;
	    cmd[3] = tOParam;
	    cmd[4] = fileOutputPath;
	                
	    Runtime rt = Runtime.getRuntime();
	    
	    rt.exec( cmd );
    }catch(Exception ex) {
      ex.printStackTrace();
    }
    




		

	}
}