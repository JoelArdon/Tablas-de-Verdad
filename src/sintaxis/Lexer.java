/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sintaxis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import truthtable.Token;

/**
 *      
 * @author Luver
 */
/**
* Lexer es una clase que se utiliza para verificar la sintaxis
*/
public class Lexer {
 

    private String expresion;
 
    private Stack<Character> pila;
    private HashMap<Character, Integer> precedenciaOperadores;
    public static ArrayList<String> preposiciones;
    public static int NUM_VARIABLES;
    public static int NUM_CONSTANTES ;
    public static int NUM_CONECTORES_LOGICOS;
    private  String validCharactersRegex;
      
    
   public Lexer(){
	   
   }
    /**
     * 
     * @param expresion que se va a evaluar
     * 
     */
 
    public Lexer(String expresion){
        this.expresion = expresion;
        pila = new Stack();
        this.validCharactersRegex = "[a-z]|[~*+><VF]";
        this.precedenciaOperadores = new HashMap<>();
        this.precedenciaOperadores.put('~', 1);
        this.precedenciaOperadores.put('*', 2);
        this.precedenciaOperadores.put('+', 3);
        this.precedenciaOperadores.put('>', 4);
        this.precedenciaOperadores.put('<', 5); 
	preposiciones = new ArrayList<>();
	NUM_VARIABLES = 0;
	NUM_CONSTANTES = 0;
	NUM_CONECTORES_LOGICOS = 0;
    }

    public String getExpresion(){
        return expresion;
    }
    public Stack<Character> getPila()
    {
        
       return pila;
    }
    public void setExpresion(String expresion){
        this.expresion = expresion;
        
    }
    
    /**
     * Metodo para balancear los parentesis igual cantidad de parentesis cerrados o abiertos
     * @return true si los parentesis estan correctamente balanceados false en caso contrario
     */
    public boolean validaParentesis() {
        for (char token : expresion.toCharArray()) {
            if (token == '(') {
                pila.push(token);
            }
            if (token == ')') {
                if (pila.empty()) {	
                    return false;
                }
                pila.pop();
            }
        }
        return pila.empty();

    }
    
   /**
    * metodo que revisa que no vengan 2 operadores continuos
    * @return false si se encuentran 2 operadores seguidos
    */
    public boolean validaSignoMasMulti(){
        for(int i = 0; i<expresion.length();i++){
            if(validaFinal() && (esMasOMulti(expresion.charAt(i))&& esOperadorOParentesisCerrado(expresion.charAt(i+1))))
                return false;
        }
        
        
        return true;
    }
    
    /**
     * Devuelve true si es un mas una multiplicacion o un implica
     * @param token el caracter a evaluar
     * @return 
     */
    public boolean esMasOMulti(char token){
        return token == '+'|| token =='*'||token=='>';
    }
   /**
    * Metodo para evaluar si el token es un operador o un parentesis abierto
    * @param token el caracter a evaluar
    * @return 
    */
    public boolean esOperadorOParentesisAbierto(char token){
        return token =='+' || token =='-' || token == '=' || token == '<' || token=='>' || token=='*' || token=='(' || token=='~';
    }
     /**
    * Metodo para evaluar si el token es un operador o un parentesis cerrado
    * @param token el caracter a evaluar
    * @return 
    */
    public boolean esOperadorOParentesisCerrado(char token){
       return token == '+'|| token =='*' || token ==')' || token =='=' || token=='<'|| token=='>';
      
    }
  
    /**
     * Revisa que la expresion no éste vacia y que empiece con un caracter valido
     * @return true si la expresion empieza correctamente
     */
    public boolean validaInicio(){
        if(expresion.isEmpty()){
            return false;
        }
        return !esOperadorOParentesisCerrado(expresion.charAt(0));
    }
    /**
     * Valida que el token sea una letra
     * @param token
     * @return true si el token es una letra
     */
    
    public boolean esLetra(char token){
	        Pattern pat = Pattern.compile("[a-z]");
		String Stoken = String.valueOf(token);
     Matcher mat = pat.matcher(Stoken); 
	    return mat.matches() || token =='V'||token =='F';
    }
    
    public boolean ContieneLetra(){
	      Pattern pat = Pattern.compile("[a-z]");
	      for(char c:expresion.toCharArray()){
		      String Stoken = String.valueOf(c);
     Matcher mat = pat.matcher(Stoken); 
                 if(mat.matches()){
			 return true;
		 }
	      }
	     return false;
	
    }
 
        /*return token == 'p' || token=='q' || token=='r' || token =='s'|| token=='t'||token=='u'||token=='v'||token=='w'||token == 'V'|| token=='x'||token == 'z'||token =='y'||token =='F';*
    }
    
    /**
     * Revisa que la expresion no tenga dos letras consecutivas
     * @return devuelve false si se encuentran  2 letras consecutivas
     */
    public boolean validaDobleLetra(){
          for(int i = 0; i<expresion.length();i++){
		    if(i==expresion.length()-1&&esLetra(expresion.charAt(expresion.length()-1))){
                    return true;
                }
		    else{
			    
		  
		  
            if(validaFinal() && (esLetra(expresion.charAt(i))&& esLetra(expresion.charAt(i+1)))){
                return false;
            }
           
              
               if(validaFinal()&&(esLetra(expresion.charAt(i))&& esNegativoParentesisAbierto(expresion.charAt(i+1)))){
                   return false;
               }
            }
        }
     return true;   
    }
    /**
     * valida que el token sea un parentesis abierto o un negativo
     * @param token a evaluar
     * @return true si es un parentesis o un negativo
     */
    public boolean esNegativoParentesisAbierto(char token){
        return token=='~'|| token=='(';
    }
    /**
     * Evalua la implicacion
     * @param token a evaluar
     * @return true si es una implicacion
     */
    public boolean esImplicacion(char token){
        return token =='=' || token == '<';  
    }
    /**
     * valida la sintaxis de la implicacion
     * @return  true si tiene la sintaxis correcta
     */
 public boolean validaImplicacion(){
     for(int i = 0;i<expresion.length();i++){
         if(validaFinal()&&expresion.charAt(i)=='='&&expresion.charAt(i+1)!='>'){
             return false;
         }
     } 
     return true;
}
 /**
  * valida la sintaxis de la doble impliacion
  * @return true si tiene la sintaxis correcta 
  */
 public boolean validaDobleImplicacion(){
        for(int i = 0;i<expresion.length();i++){
         if(validaFinal()&&expresion.charAt(i)=='<'&&expresion.charAt(i+1)!='='&&expresion.charAt(i+2)!='>'){
             return false;
         }
     } 
        return true;
 }
  
 /**
  * Revisa que el final de la expresion sea correcto
  * @return  true si el final de la expresion esta correcto
  */
    public boolean validaFinal(){
	   
      return !esOperadorOParentesisAbierto(expresion.charAt(expresion.length()-1));
    }

    
    

    /**
     * Revisa que el siguiente despues de un parentesis abierto sea valido
     * @return devuelve true si la expresion no contiene errores despues de un parentesis abierto
     */
    public boolean ValidaParentesisAbierto(){
        for(int i = 0; i<expresion.length();i++ ){
            if(validaFinal()&&(expresion.charAt(i)=='(')&&(!esLetra(expresion.charAt(i+1)))&&(!esNegativoParentesisAbierto(expresion.charAt(i+1)))){
                return false;
            }
        }
       return  true;
    }
     /**
     * Revisa que el siguiente despues de un parentesis cerrado sea valido
     * @return devuelve true si la expresion no contiene errores despues de un parentesis cerrado
     */
public boolean ValidaParentesisCerrado(){
    for(int i =0; i<expresion.length();i++){
        if(expresion.charAt(expresion.length()-1)==')'){
            return true;
        }
        if(validaFinal()&&expresion.charAt(i)==')'&& expresion.charAt(i+1)=='('){
            return false;
        }
        if(validaFinal()&&expresion.charAt(i)==')'&& esLetra(expresion.charAt(i+1))){
            return false;
        }
         if(validaFinal()&&expresion.charAt(i)==')'&& expresion.charAt(i+1)=='~'){
            return false;
        }
    }
    return true;
}
/**
 * verifica la expresion minima
 * @return devuelve true si la expresion minima esta correcta
 */
    public boolean ValidaExpresionMinima(){
         if(expresion.length()<3){
             return false;
         }
         if(expresion.length()==3){
        if( !esLetra(expresion.charAt(0))||!esMasOMulti(expresion.charAt(1))||!esLetra(expresion.charAt(2))){
            return false;
 
      
        }
         }
         
    return true;
    }
    /**
     * Valida que se utilicen simbolos correctos
     * @param token
     * @return true  si el token es un simbolo valida
     */
    public boolean verificaSimbolos(char token){
     return esLetra(token) || esOperadorOParentesisAbierto(token)|| token==')';
        
    }
    /**
     * Valida que la expresion solo contenga simbolos admitibles
     * @return true si la expresion contiene simbolos admitidos unicamente
     */
    public boolean validaSimbolos(){
        for(char token: expresion.toCharArray()){
            if(!verificaSimbolos(token)){
                return false;
            }
        }
        return true;
    }
    /**
     * remplaza los diferentes tipos de implica
     */
      public void RemplazaImplicaPorNormal(){
          expresion = expresion.replace("->", "=>");
          expresion = expresion.replace("=}", "=>");
      }  
      
         public void cambiaSimbolos(){
	     expresion = expresion.replaceAll("->", "=>");
	     expresion = expresion.replaceAll("<->", "<=>");
	     expresion = expresion.replaceAll("-", "~");
    }
      /**
       * reemplza los implica para ser evaluados
       */
      public void RemplazaImplicaPorMenor(){
        expresion = expresion.replace("=>", ">");
    
      }
     /**
      * metodo para validar la expresion
      * @return true si la expresion esta validada correctamente
      */
    public String validaExpresion(){
      //ValidaExpresionMinima()&&
          if(!ContieneLetra()){
		       return "La expresion debe contener al menos una letra";
	      } 
              if (!validaParentesis()){
	         return "Los parentesis no se encuentran balanceados";
	      }  
		if(!validaSignoMasMulti()){
		     return"No se permiten operadores seguidos";
		}
		if(!validaFinal()){
			return"Revisar el final de la expresion";
		}
                if(!validaInicio()){
			return"Revisar el inicio de la expresion";
		}
		if(!validaDobleLetra()){
			return "No puede haber letras seguidas";
		}
		if(!validaImplicacion()){
			return "La impicacion debe escribirse  =>";
		}
		if(! validaDobleImplicacion()){
			return "La dobleimpicacion debe escribirse  <=>";
		}
               if(  !ValidaParentesisAbierto()){
		       return "Revisar la sintaxis de la expresion";
	       }
              if(!ValidaParentesisCerrado()){
		       return "Revisar la sintaxis de la expresion";
	      }
                 if(!validaSimbolos()){
		       return "La expresion contiene simbolos no admitidos";
	      } 
		
		 return "true";
          
    }
    /**
     * reemplza el doble implica
     */
    public void ReemplzaDobleImplicaMayor(){
        expresion = expresion.replace("<=>", "<");
    }
 
    /**
     * metodo para simplifcar negativos
     */
    
    
    public void SimplificaNegativo(){	   
	   for(int i = 0; i<expresion.length();i++){
		   if(expresion.charAt(i)=='~'&&expresion.charAt(i+1)=='~'){
			expresion = expresion.replaceAll("~~", "");	
		   }
	   }
    }
    /**
     * metodo que cuenta las variables y los conectores logicos
     * @param input la expresion
     * @return devuelve un arrayList del objeto custom token con los caracteres ya clasificados
     */
     public static ArrayList<Token> lex(String input) {
        final ArrayList<Token> tokens = new ArrayList<>();
      
	
        final StringTokenizer tokenizer = new StringTokenizer(input, "*+()~<>", true);

        while(tokenizer.hasMoreTokens()) {
            String palabra = tokenizer.nextToken();
            boolean matched = false;
             
            for (Token.Tipo tokenTipo : Token.Tipo.values()) {
                Pattern patron = Pattern.compile(tokenTipo.patron);
                Matcher matcher = patron.matcher(palabra);
                if(matcher.find()) {
                    Token tk = new Token();
                    tk.setTipo(tokenTipo);
                    tk.setValor(palabra);
                    tokens.add(tk);
                    matched = true;

                    if(tokenTipo.equals(Token.Tipo.PROPOSICION) && !preposiciones.contains(palabra)){
                        NUM_VARIABLES++;
                        preposiciones.add(palabra);
                    }else if(tokenTipo.equals(Token.Tipo.NEGACION) ||tokenTipo.equals(Token.Tipo.CONECTOR_LOGICO)){
                        NUM_CONECTORES_LOGICOS++;
                    }else if(tokenTipo.equals(Token.Tipo.CONSTANTES)){
                        NUM_CONSTANTES++;
                    }
                }
            }

            if (!matched) {
                throw new RuntimeException("Se encontró un token invalido."+palabra);
            }
        }
        
        return tokens;
    }
     
     /**
      * Metodo que convierte la expresiona  postfija
      * @param infixLogicalExpression la expresion normal
      * @return un string con la expresion en postfija
      * 
      */
     public String infixToPostfix(String infixLogicalExpression) throws Exception {
        Stack<Character> operandStack = new Stack<>();
        StringBuilder output = new StringBuilder(infixLogicalExpression.length()+1);
        for(int i = 0; i < infixLogicalExpression.length(); i++)
        {
            //Caracter (Token) valido
            if (!((""+infixLogicalExpression.charAt(i)).matches(validCharactersRegex) || (""+infixLogicalExpression.charAt(i)).matches("[\\(\\)]")))
            {
                throw new Exception("Caracter (Token) no valido: \'" + infixLogicalExpression.charAt(i) + "\'");
            }

            if (!Character.isAlphabetic(infixLogicalExpression.charAt(i))) {
                char characterInQuestion = infixLogicalExpression.charAt(i);
                if (characterInQuestion == '(')
                {
                    //pusheamos el nuevo parentesis
                    operandStack.push(characterInQuestion);
                }
                else if (characterInQuestion == ')')
                {
                    
	//sacamos todo de la pila hasta que tengamos un parenthesis abierto y no agregamos el parentesis de cierre a la pila
                    while(operandStack.peek() != '(')
                    {
                        output.append(operandStack.pop());
                    }
                    //Pop off the ( itself
                    operandStack.pop();
                }
                else
                {
                    
		    // pusheamos todo con la misma precedencia o mas alta hasta quee encontramos un parentesis o la pila este vacia
                    while (!operandStack.empty() && operandStack.peek() != '(' && this.precedenciaOperadores.get(operandStack.peek()) <= this.precedenciaOperadores.get(characterInQuestion))
                    {
                        output.append(operandStack.pop());
                    }

                    //despues pusheamos el nuevo operador
                    operandStack.push(characterInQuestion);
                }
            }
            else {
                output.append(infixLogicalExpression.charAt(i));
            }
        }

       
	//si la pila no esta totalmente vacia entonce sacamos todo de la pila y lo concatenamos en el output
        while(!operandStack.empty())
        {
            output.append(operandStack.pop());
        }

        return output.toString();
    }
     
}
