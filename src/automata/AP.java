package automata;

import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.regex.*;
import java.io.*;


import utils.Quintuple;



public abstract class AP {

	public static final Character Lambda = '_';
	public static final Character Joker = '@'; // Marca de la pila
	public static final Character Initial = 'Z'; 

	protected State initial;
  protected Character stackInitial;
  protected Stack<Character> stack; //stack of the automaton
  protected Set<State> states;
  protected Set<Character> alphabet;
  protected Set<Character> stackAlphabet; //Alphabet of the stack
  protected Set<Quintuple<State,Character,Character,String,State>> transitions; //delta function
  protected Set<State> finalStates;


    /*
     * A static constructor should be implemented depending on the final design of the automaton
     * */

  public static State getElemFromSet(Set<State> q,State o){
	 	Iterator states = q.iterator();
		while( states.hasNext()){
			State current = (State) states.next();
			if(current.equals(o)){
				return current;
			}
		}
  	return null;
  }

  public Set<State> final_states(){
      return finalStates;
  }

  public State initial_state(){
      return initial;
  }

  public Set<Character> alphabet(){
      return alphabet;
  }

  public Set<State> states(){
      return states;
  }

  public final void from_dot(File file){
    FileReader fr;
    BufferedReader br = null;
    stackInitial = Joker;
    try{
      fr = new FileReader (file);
      br = new BufferedReader(fr);
    }catch(Exception e){
         e.printStackTrace();
    }
    try{
      String currentLine = br.readLine();
      // Regular expression that defines a initial state
      Pattern initialState = Pattern.compile("^(inic->).*");
      Matcher mInitialState;

      // Regular expression that defines a transition
      // Para diferenciar las transiciones del nombre del estado inicial
      // (inic -> q0) se asume que el nombre de todos los estados 
      // comienza con la letra q
      Pattern transition = Pattern.compile("^(q).*(->)(q).*");
      Matcher mTransition;

      // Regular expression that defines a final state
      Pattern finalState = Pattern.compile("^.*(\\[)(shape=doublecircle)(\\])");
      Matcher mFinalState;
      
      while(currentLine!=null){
                ////The line defines a initial state 
        mInitialState = initialState.matcher(currentLine);
        if(mInitialState.matches()){
          String[] result = currentLine.split("(inic->)");
          State state = new State(result[0]);
          initial = state;
          if(getElemFromSet(states,initial) == null){
            states.add(initial);
          }
        }

        //The line defines a transition
        mTransition = transition.matcher(currentLine);
        if(mTransition.matches()){
          String[] result = currentLine.split("->|\\s(\\[)(label=)\"|\\/|\"(\\[)");
          State fromState = new State(result[0]);
          State toState = new State(result[1]);
          char alphabetChar = result[2].charAt(0);
          char stackChar = result[3].charAt(0);
          String stackString = result[4];

          Quintuple<State,Character,Character,String,State> newTransition;
          newTransition = new Quintuple<State,Character,Character,String,State>(fromState,alphabetChar,stackChar,stackString,toState);

          transitions.add(newTransition);
          
          if(getElemFromSet(states,fromState)==null)
            states.add(fromState);
          
          if(getElemFromSet(states,toState)==null)
            states.add(toState);
          
          if(!stackAlphabet.contains(stackChar))
            stackAlphabet.add(stackChar);
          
          if(!alphabet.contains(alphabetChar))
            alphabet.add(alphabetChar);
        }

        //The line defines a final state 
        mFinalState = finalState.matcher(currentLine);
        if(mFinalState.matches()){
          String[] result = currentLine.split("(\\[)(shape=doublecircle)(\\])");
          State state = new State(result[0]);
          if(getElemFromSet(finalStates,state) == null){
            finalStates.add(state);
            states.add(state);
          }
        }
        //Get next line
        currentLine = br.readLine();
      }// End while

      br.close();
    }catch(Exception e){
         e.printStackTrace();
    }
    //System.out.println("OK...");
  }// End of method

  public final String to_dot(){
      //assert rep_ok();
      char quotes= '"';
      Iterator i;
      String aux;
      aux = "digraph{\n";
      aux = aux + "inic[shape=point];\n" + "inic->" + this.initial.name() + ";\n";
      i = this.transitions.iterator();
      while (i.hasNext()) {
         Quintuple quintuple =(Quintuple) i.next();
         aux = aux + quintuple.first().toString() + "->" + quintuple.fifth().toString() + " [label=" +quotes+ quintuple.second().toString() +"/"+ quintuple.third()+"/"+quintuple.fourth()+ quotes+ "];\n";
      }
      aux = aux+ "\n";
      i = this.finalStates.iterator();
      while (i.hasNext()){
          State estado = (State) i.next();
          aux = aux + estado.name() + "[shape=doublecircle];\n";
      }
      aux = aux + "}";
      return aux;
  }

  /**
  * this methods should be implemented in DFAPila
  */
  public abstract boolean accepts(String string);

  public abstract Object delta(State from, Character c);


}
