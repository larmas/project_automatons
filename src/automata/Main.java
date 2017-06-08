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
		//-------------Test DFAPila-------------------//
	  Set<State> statesDFAP = new HashSet<State>();
    Set<Character> alphabetDFAP = new HashSet<Character>();
    Set<Character> stackAlphabetDFAP = new HashSet<Character>();
    Set<Quintuple<State, Character,Character,String, State>> transitionsDFAP = new HashSet<Quintuple<State, Character,Character,String, State>>();
    Character stackInitialDFAP = null;
    State initialDFAP = null;
    Set<State> final_statesDFAP = new HashSet<State>();
    
		DFAPila dfap = new DFAPila(statesDFAP,alphabetDFAP,stackAlphabetDFAP,transitionsDFAP,stackInitialDFAP,initialDFAP,final_statesDFAP);
		
		//El archivo dfapFinalState.txt contiene un automata con estado final
		//El archivo dfapEmptyStack.txt contiene un automata sin estado final
		//El archivo sampleDeterminist.txt contiene un automata que no cumple con
		//el determinismo
		//El archivo sampleReachable.txt contiene un automata que tiene estados 
		//inalcanzables
		dfap.from_dot(new File("text/dfapFinalState.txt"));
		//dfap.from_dot(new File("text/dfapEmptyStack.txt"));
		//dfap.from_dot(new File("text/sampleDeterminist.txt")); //Debe lanzar excepcion por rep_ok
		//dfap.from_dot(new File("text/sampleReachable.txt"));	//Debe lanzar excepcion por rep_ok


		//Se evalua la cadena "ab" y "ba" con el automata dfap
		boolean resultDFAP1 = dfap.accepts("ab");
		System.out.println("El automata aceptó la cadena 'ab': "+resultDFAP1);
		boolean resultDFAP2 = dfap.accepts("ba");
		System.out.println("El automata aceptó la cadena 'ba': "+resultDFAP2);
		
		//------Algoritmos de aceptacion--------
		dfap.to_empty_stack();
		//dfap.to_final_state();
		
		//Se almacena en un string el resultados del algoritmo ejecutado
		String to_dotDFAP = dfap.to_dot();

		try{
			//Se pasa el resultado del algoritmos a un archivo dot
			String newFileDot = new String("text/automatonDFAP.txt");

			File file = new File(newFileDot);

			BufferedWriter bw = new BufferedWriter(new FileWriter(newFileDot));
			bw.write(to_dotDFAP);

			bw.close();
		}catch(Exception e){
         e.printStackTrace();
    }
    try{
    	//Inicializacion necesaria para la libreria "graphviz"
    	String dotPath = "/usr/local/bin/dot";
	    String fileInputPath = "text/automatonDFAP.txt";
	    String fileOutputPath = "output/automatonDFAP.jpg";
	    String tParam = "-Tjpg";
	    String tOParam = "-o";

	    String[] cmd = new String[5];
	    cmd[0] = dotPath;
	    cmd[1] = tParam;
	    cmd[2] = fileInputPath;
	    cmd[3] = tOParam;
	    cmd[4] = fileOutputPath;
	                
	    Runtime rt = Runtime.getRuntime();
	    
	    //Se crea el grafico del automata obtenido
	    rt.exec(cmd);

    }catch(Exception ex) {
      ex.printStackTrace();
    }


    //------------TEST NFAPila----------------//
    
    Set<State> statesNFAP = new HashSet<State>();
    Set<Character> alphabetNFAP = new HashSet<Character>();
    Set<Character> stackAlphabetNFAP = new HashSet<Character>();
    Set<Quintuple<State, Character,Character,String, State>> transitionsNFAP = new HashSet<Quintuple<State, Character,Character,String, State>>();
    Character stackInitialNFAP = null;
    State initialNFAP = null;
    Set<State> final_statesNFAP = new HashSet<State>();

    NFAPila nfap = new NFAPila(statesNFAP,alphabetNFAP,stackAlphabetNFAP,transitionsNFAP,stackInitialNFAP,initialNFAP,final_statesNFAP);
		nfap.from_dot(new File("text/noDeterminista.txt"));

		boolean accept = false;
		boolean resultNFAP1 = nfap.new_accepts("ab",nfap.initial,accept);
		System.out.println("El automata aceptó la cadena 'ab': "+resultNFAP1);
		boolean resultNFAP2 = nfap.new_accepts("ba",nfap.initial,accept);
		System.out.println("El automata aceptó la cadena 'ba': "+resultNFAP2);

		//------Algoritmos de aceptacion--------
		nfap.to_final_state();
		//nfap.to_empty_stack();
		String to_dotNFAP = nfap.to_dot();

		try{
			//Se pasa el resultado del algoritmo a un archivo dot
			String newFileDot = new String("text/automatonNFAP.txt");

			File file = new File(newFileDot);

			BufferedWriter bw = new BufferedWriter(new FileWriter(newFileDot));
			bw.write(to_dotNFAP);

			bw.close();
		}catch(Exception e){
         e.printStackTrace();
    }
    try{
    	//Inicializacion necesaria para la libreria "graphviz"
    	String dotPath = "/usr/local/bin/dot";
	    String fileInputPath = "text/automatonNFAP.txt";
	    String fileOutputPath = "output/automatonNFAP.jpg";
	    String tParam = "-Tjpg";
	    String tOParam = "-o";

	    String[] cmd = new String[5];
	    cmd[0] = dotPath;
	    cmd[1] = tParam;
	    cmd[2] = fileInputPath;
	    cmd[3] = tOParam;
	    cmd[4] = fileOutputPath;
	                
	    Runtime rt = Runtime.getRuntime();
	    
	    //Se crea el grafico del automata obtenido
	    rt.exec(cmd);

    }catch(Exception ex) {
      ex.printStackTrace();
    }

    //------------TEST Gramatica----------------//
    
    Set<State> states = new HashSet<State>();
    Set<Character> alphabet = new HashSet<Character>();
    Set<Character> stackAlphabet = new HashSet<Character>();
    Set<Quintuple<State, Character,Character,String, State>> transitions = new HashSet<Quintuple<State, Character,Character,String, State>>();
    Character stackInitial = null;
    State initial = null;
    Set<State> final_states = new HashSet<State>();
    
    NFAPila gramm = new NFAPila(states,alphabet,stackAlphabet,transitions,stackInitial,initial,final_states);
		gramm.get_GLC(new File("text/glc.txt"));


	}
}