package automata;

import java.util.Set;
import java.util.Stack;
import java.util.Iterator;

import utils.Quintuple;

public final class DFAPila extends AP{

	private   Object nroStates[] ;
  private Stack<Character> stack; //the stack of the automaton


  /**
   * Constructor of the class - returns a DFAPila object
   * @param states - states of the DFAPila
   * @param alphabet - the alphabet of the automaton
   * @param stackAlphabet - the alphabet of the stack
   * @param transitions - transitions of the automaton
   * @param stackInitial - a Character which represents the initial element of the stack
   * @param initial - initial State of the automaton
   * @param final_states - acceptance states of the automaton
   * @throws IllegalArgumentException
   */
  public DFAPila(
          Set<State> states,
          Set<Character> alphabet,
          Set<Character> stackAlphabet,
          Set<Quintuple<State,Character,Character,String,State>> transitions,
          Character stackInitial,
          State initial,
          Set<State> final_states)
          throws IllegalArgumentException
  {
    //Se borro  
    //lo mismo en el perfil
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

  // Delta debe realizar las acciones relacionadas con la pila de cada transicion
  @Override
  public State delta(State from, Character c){
		Iterator transitionsIt = transitions.iterator();
		Quintuple<State,Character,Character,String,State> transitionCurrent;
    System.out.println("("+from+", "+c+")");
		while (transitionsIt.hasNext()){
			transitionCurrent = (Quintuple)transitionsIt.next();
      System.out.println(transitionCurrent.toString());
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
			int index = 0;
			State currentState = initial;
			while(index<string.length()){
				State resultState = delta(currentState,string.charAt(index));
        System.out.println("DEBUG TRANSITION "+index+":");
        System.out.println("FROM: "+currentState+" FOR: "+string.charAt(index)+" TO: "+resultState);
				if(resultState!=null){
          currentState = resultState;
					index++;
        }
				else
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
  }
  
  public boolean rep_ok() {
    if(this.transitionConditions() && this.reachableState())
        return true;
  	return false;
  }

  // Recorrer transiciones observando no determinismo, alfabeto de stack y
  // transiciones lambda
  public boolean transitionConditions(){
    //TODO this method have to be implemented
    Iterator itTransitions1 = transitions.iterator();
    Iterator itTransitions2 = transitions.iterator();
    Quintuple<State,Character,Character,String,State> cTransition1;
    Quintuple<State,Character,Character,String,State> cTransition2;
    boolean deterministic = true;
    boolean conditionsOK = true;
    while(itTransitions1.hasNext()){
      cTransition1 = (Quintuple) itTransitions1.next();
      // Desapilo algo que pertenezca al alfabeto del stack
      conditionsOK = conditionsOK && stackAlphabet.contains(cTransition1.third());
      // Apilo cosas que pertenezcan al alfabeto del stack
      String auxString = cTransition1.fourth();
      for(int index=0; index<auxString.length(); index++){
        conditionsOK = conditionsOK && stackAlphabet.contains(auxString.charAt(index));
      }
      // No existen transiciones lambda
      conditionsOK = conditionsOK && cTransition1.second()!= Lambda;
      // Se mantiene el determinismo
      while(itTransitions2.hasNext()){
        cTransition2 = (Quintuple) itTransitions2.next();
        if(cTransition1.first().equals(cTransition2.first()) &&
          cTransition1.second()==cTransition2.second() &&
          cTransition1.third()==cTransition2.third())
            conditionsOK = conditionsOK && 
                           cTransition1.fourth().equals(cTransition2.fourth()) &&
                           cTransition1.fifth().equals(cTransition2.fifth());
      }
    }
    return conditionsOK;
  }

  //There are no unreachable states
  public boolean reachableState(){
    Iterator itStates = states.iterator();
    State currentState;
    boolean reachable=true;
    while(itStates.hasNext()){
      currentState = (State)itStates.next();
      Iterator itTransitions = transitions.iterator();
      Quintuple<State,Character,Character,String,State> currentTransition;
      boolean result = false;
      while(itTransitions.hasNext()){
        currentTransition = (Quintuple)itTransitions.next();
        result = result || currentTransition.fifth() == currentState; 
      }
      reachable = reachable && result;
    }
    return reachable;
  }


}
