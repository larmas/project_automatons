package automata;

import java.util.Set;
import java.util.HashSet;
import java.util.Stack;
import java.util.Iterator;
import java.util.regex.*;
import java.io.*;

import utils.Quintuple;

public final class NFAPila extends AP{

  private Stack<Character> stack; //the stack of the automaton
  // Elementos de una GLC
  private Set<Character> notTerminals;
  private Set<Character> terminals;
  private Character initialSymbol;
  //private Set<> productions;


  /**
   * Constructor of the class - returns a NFAPila object
   * @param states - states of the NFAPila
   * @param alphabet - the alphabet of the automaton
   * @param stackAlphabet - the alphabet of the stack
   * @param transitions - transitions of the automaton
   * @param stackInitial - a Character which represents the initial element of the stack
   * @param initial - initial State of the automaton
   * @param final_states - acceptance states of the automaton
   * @throws IllegalArgumentException
   */
  public NFAPila(
          Set<State> states,
          Set<Character> alphabet,
          Set<Character> stackAlphabet,
          Set<Quintuple<State,Character,Character,String,State>> transitions,
          Character stackInitial,
          State initial,
          Set<State> final_states)
          throws IllegalArgumentException
  {
    this.states = states;
    this.alphabet = alphabet;
    this.stackAlphabet = stackAlphabet;
    stackAlphabet.add(Lambda); //the character lambda is used in the stack to know when do a pop
    stackAlphabet.add(Joker); //the mark of the stack
    this.transitions = transitions; 
    this.stackInitial = stackInitial;
    this.initial = initial;
    this.finalStates = final_states;
    stack = new Stack<Character>();
    stack.add(Joker); //insert the mark in the stack
    if (!rep_ok()){
        throw new  IllegalArgumentException();
    }
    System.out.println("Is a NFA Pila");
  }

  public void get_GLC(File file){

    FileReader fr;
    BufferedReader br = null;
    try{
      
      fr = new FileReader (file);
      br = new BufferedReader(fr);
    
    }catch(Exception e){
         e.printStackTrace();
    }
    try{
    
      String currentLine = br.readLine();


      Pattern not_terminal = Pattern.compile("[AZ]");
      Matcher mNot_terminal;

      Pattern terminal = Pattern.compile("[az]|[09]|[\\%\\&\\(\\)\\*\\+\\-\\/\\<\\=\\>\\[\\]\\.]");
      Matcher mTerminal;

      while (currentLine!=null){


        String[] result = currentLine.split("\\s*→\\s*|\\s*(\\|)\\s*");
        //for(String i : result){
          //System.out.print(i+" ; ");
        //}
        
        for(int i=0; i<result.length; i++){
          
          String aux = result[i].trim();
          //System.out.println(aux);
          for (int j=0; j<aux.length(); j++){
            
            Character newChar = aux.charAt(j);
            System.out.println(newChar);
            mNot_terminal = not_terminal.matcher(aux);
            if(mNot_terminal.matches()){
                          
              
              if(!notTerminals.contains(newChar)){
            
                notTerminals.add(newChar);
            
              }
            
            }

            mTerminal = terminal.matcher(aux);
            if(mTerminal.matches()){
              if(!terminals.contains(newChar)){
            
                terminals.add(newChar);
            
              }

            }
          
          }


        
        }


        currentLine = br.readLine();
      }

      

      br.close();
    }catch(Exception e){
         e.printStackTrace();
    }
  }

  public Set<Quintuple<State,Character,Character,String,State>> delta(State from, Character c){
    Set<Quintuple<State,Character,Character,String,State>> result = new HashSet<Quintuple<State,Character,Character,String,State>>();
		Iterator itTransitions = transitions.iterator();
		Quintuple<State,Character,Character,String,State> transitionCurrent;
    //System.out.println("("+from+", "+c+")");
		while (itTransitions.hasNext()){
		
    	transitionCurrent = (Quintuple)itTransitions.next();
			if (transitionCurrent.first().equals(from) && transitionCurrent.second() == c){
          
				  result.add(transitionCurrent);
      
      }
		
    }
  	return result;
  }

  public State apply_delta(Quintuple<State,Character,Character,String,State> transition){
    if(transition.third()!= Joker && transition.third()!=Lambda)
    
            stack.pop();

    for(int index=0; index<transition.fourth().length(); index++){
    
      char currentChar = transition.fourth().charAt(index);
      if(currentChar!=Joker && currentChar!= Lambda)

        stack.push(currentChar);

    }
    return transition.fifth();
  }


  @Override
  public boolean accepts(String string) {
    return false;
  }

  public boolean new_accepts(String string, State currentState, boolean accept) {
    if (string.length()==0) {
      accept = true;
    
    }else{

      Set<Quintuple<State,Character,Character,String,State>> resultTransitions = delta(currentState,string.charAt(0));
      //System.out.println("Caracter : "+string.charAt(0));
      //System.out.println("Tamaño de delta: "+ resultTransitions.size());
      Iterator itTransitions = resultTransitions.iterator();
      Quintuple<State,Character,Character,String,State> currentTransition;

      if(!resultTransitions.isEmpty()){
        //System.out.println("OK...");
        while(itTransitions.hasNext()){

          currentTransition = (Quintuple)itTransitions.next();
          State resultState = apply_delta(currentTransition);
          //System.out.println("Me movi de "+currentState.name()+" a "+resultState.name());
          String newString = new String();
          for (int i=1; i<string.length(); i++) {
           
            newString = newString+string.charAt(i);
          
          }
          accept = accept || this.new_accepts(newString,resultState,accept);
          
        }
      }else{
        accept = accept || false;
      }

    }
	   return accept;
  	
  }
  
  public boolean rep_ok() {
    
    if(this.transitionConditions() && this.reachableState())
        
        return true;
  	
    return false;
  }

  public boolean transitionConditions(){
    Iterator itTransitions = transitions.iterator();
    Quintuple<State,Character,Character,String,State> cTransition;
    boolean belongsStackPop = true;
    boolean belongsStackAdd = true;

    while(itTransitions.hasNext()){
      
      cTransition = (Quintuple) itTransitions.next();
      // Desapilo algo que pertenezca al alfabeto del stack
      belongsStackPop = belongsStackPop && stackAlphabet.contains(cTransition.third());
      // Apilo cosas que pertenezcan al alfabeto del stack
      String auxString = cTransition.fourth();
      for(int index=0; index<auxString.length(); index++){
      
        belongsStackAdd = belongsStackAdd && stackAlphabet.contains(auxString.charAt(index));
      
      }
    
    }
    return belongsStackPop && belongsStackAdd;
  }

  //There are no unreachable states
  public boolean reachableState(){
    Iterator itStates = states.iterator();
    State currentState;
    boolean reachable=true;
    while(itStates.hasNext()){
      
      currentState = (State)itStates.next();
      if(currentState.equals(initial))
      
        continue;
      
      Iterator itTransitions = transitions.iterator();
      Quintuple<State,Character,Character,String,State> currentTransition;
      boolean result = false;
      while(itTransitions.hasNext()){
      
        currentTransition = (Quintuple)itTransitions.next();
        result = result || currentState.equals(currentTransition.fifth()); 
      
      }
      reachable = reachable && result;
    
    }
    return reachable;
  }

  public void to_empty_stack(){
    String aux = ""+Joker+Mark;
    String lambdaString = ""+Lambda;
    
    // Nuevo estado inicial
    State oldInitial = initial;
    initial = new State("q1000");

    //Nueva transicion de nuevo estado inicial al antiguo estado inicial
    Quintuple<State,Character,Character,String,State> transitionPN;
    transitionPN = new Quintuple<State,Character,Character,String,State>(initial,Lambda,Mark,aux,oldInitial);
    transitions.add(transitionPN);

    //Reinicio de stack 
    stack = new Stack<Character>();
    stack.add(Mark);
    stack.add(Joker);

    //Nuevo estado final
    State newState = new State("q2000");

    //Para cada estado final del automata se crea una transicion al estado que vacia la pila
    Iterator itFinalStates = finalStates.iterator();
    while(itFinalStates.hasNext()){

      State currentState = (State) itFinalStates.next();
      Quintuple<State,Character,Character,String,State> newTransition;
      newTransition = new Quintuple<State,Character,Character,String,State>(currentState,Lambda,'$',lambdaString,newState);
      transitions.add(newTransition);

    }
    Quintuple<State,Character,Character,String,State> newTransition;
    newTransition = new Quintuple<State,Character,Character,String,State>(newState,Lambda,'$',lambdaString,newState);
    transitions.add(newTransition);
    states.add(initial);
    states.add(newState);
    finalStates.clear();

  }

  public void to_final_state(){
    String aux = ""+Mark+Joker;
    String lambdaString = ""+Lambda;
    // Nuevo estado inicial
    State oldInitial = initial;
    initial = new State("q1000");

    //Nueva transicion de nuevo estado inicial al antiguo estado inicial
    Quintuple<State,Character,Character,String,State> transitionPN;
    transitionPN = new Quintuple<State,Character,Character,String,State>(initial,Lambda,Mark,aux,oldInitial);
    transitions.add(transitionPN);
    //Reinicio de stack 
    stack = new Stack<Character>();
    stack.add(Mark);
    stack.add(Joker);

    //Nuevo estado final
    State newFinalState = new State("q2000");
    finalStates.add(newFinalState);

    //Para cada estado del automata se crea una transicion al estado final
    Iterator itStates = states.iterator();
    while(itStates.hasNext()){

      State currentState = (State) itStates.next();
      Quintuple<State,Character,Character,String,State> newTransition;
      newTransition = new Quintuple<State,Character,Character,String,State>(currentState,Lambda,Mark,lambdaString,newFinalState);
      transitions.add(newTransition);
    }

    states.add(initial);

  }

  





















}
