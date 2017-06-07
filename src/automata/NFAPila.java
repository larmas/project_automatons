package automata;

import java.util.Set;
import java.util.Stack;
import java.util.Iterator;

import utils.Quintuple;

public final class NFAPila extends AP{

	private   Object nroStates[] ;
  private Stack<Character> stack; //the stack of the automaton


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
    nroStates =  states.toArray();
    stack = new Stack<Character>();
    stack.add(Joker); //insert the mark in the stack
    if (!rep_ok()){
        throw new  IllegalArgumentException();
    }
    System.out.println("Is a DFA Pila");
  }

  @Override
  public State delta(State from, Character c){
		Iterator transitionsIt = transitions.iterator();
		Quintuple<State,Character,Character,String,State> transitionCurrent;
    System.out.println("("+from+", "+c+")");
		while (transitionsIt.hasNext()){
		
    	transitionCurrent = (Quintuple)transitionsIt.next();
      //System.out.println(transitionCurrent.toString());
			if (transitionCurrent.first().equals(from) && transitionCurrent.second() == c){
        // Es necesario controlar que el caracter a consumir este en alphabet??
        // Si el elemento a desapilar coincide con el tope del stack
        if(transitionCurrent.third()==stack.peek()){
          // Desapilo siempre que no sea Lambda ni Joker
          if(transitionCurrent.third()!= Joker && transitionCurrent.third()!=Lambda)
    
            stack.pop();
    
          // Apilo el string caracter por caracter
          for(int index=0; index<transitionCurrent.fourth().length(); index++){
    
            char currentChar = transitionCurrent.fourth().charAt(index);
            if(currentChar!=Joker && currentChar!= Lambda)
    
              stack.push(currentChar);
    
          }
				  return transitionCurrent.fifth();
        }
      }
		}
  	return null;
  }

  @Override
  public boolean accepts(String string) {
		if(rep_ok()){	
    
      int index = 0;
			State currentState = initial;
			while(index<string.length()){
		
    		State resultState = delta(currentState,string.charAt(index));
				if(resultState!=null){
    
          currentState = resultState;
					index++;
        }else
					break;
		
    	}
			if (currentState!=null && finalStates.isEmpty() && stack.peek()==Joker){
        
        System.out.println("Empty stack");
        return true;
      
      }
			if (currentState!=null && !finalStates.isEmpty() && getElemFromSet(finalStates,currentState)!= null){
			
      	System.out.println("Final States");
        return true;
      
      }
			return false;
    }else{
      throw new  IllegalArgumentException("Automata no cumple las condiciones");
    
    }
  }
  
  public boolean rep_ok() {
    System.out.println("REP_OK REPORTS: \n");
    System.out.println("*StackAlphabet and LambdaTransitions: " + this.transitionConditions());
    System.out.println("*States: "+ this.reachableState()+"\n");
    
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
    //TODO this method have to be implemented
    String aux = ""+Joker+Mark;
    System.out.println(aux);
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
    //TODO this method have to be implemented
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
