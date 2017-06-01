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
          Set<Quintuple<State, Character,Character,String, State>> transitions,
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
		Quintuple<State, Character,Character,String, State> transitionCurrent;
		while (transitionsIt.hasNext()){
			transitionCurrent = (Quintuple)transitionsIt.next();
			if (transitionCurrent.first() == from && transitionCurrent.second() == c){
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
				currentState = delta(currentState,string.charAt(index));
				if(currentState!=null)
					index++;
				else
					break;
			}
			if (currentState!=null && finalStates.isEmpty() && stack.peek()==Joker)
				return true;
			if (currentState!=null && !finalStates.isEmpty() && finalStates.contains(currentState))
				return true;

			return false;
  }
  
  public boolean rep_ok() {
    if(!this.transitionConditions() && !this.unreachableState())
        return true;
  	return false;
  }

  // Recorrer transiciones observando no determinismo, alfabeto de stack y
  // transiciones lambda
  public boolean transitionConditions(){
    //TODO this method have to be implemented
    return false;
  }

  //Recorrer conjunto de estados observando si son alcanzables
  public boolean unreachableState(){
    //TODO this method have to be implemented
    return false;
  }


}
