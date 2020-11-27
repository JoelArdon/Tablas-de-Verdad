/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eval;

import sintaxis.Lexer;
import truthtable.TruthTable;
import javax.xml.bind.annotation.XmlAttribute;  
import javax.xml.bind.annotation.XmlElement;  
import javax.xml.bind.annotation.XmlRootElement;  
/**
 *
 * @author Luver
 */
@XmlRootElement  
public class Eval {
       
	private Lexer lexer;
	private TruthTable tt;
	private String posfix;

	public Eval(String expresion) {
		this.lexer = new Lexer(expresion);
	

	}
	public Eval() {
	}

	/**
	 * @return the posfix
	 */
	@XmlAttribute  
	public String getPosfix() {
		return posfix;
	}

	/**
	 * @param posfix the posfix to set
	 */
	public void setPosfix(String posfix) {
		this.posfix = posfix;
	}

	public void cleanExpresion() {
		lexer.ReemplzaDobleImplicaMayor();
		lexer.RemplazaImplicaPorMenor();
		lexer.SimplificaNegativo();
	}

	/**
	 * @return the lexer
	 */
	@XmlElement  
	public Lexer getLex() {
		return lexer;
	}

	/**
	 * @param lexer the lexer to set
	 */
	public void setLex(Lexer lexer) {
		this.lexer = lexer;
	}

	/**
	 * @return the tt
	 */
	@XmlElement  
	public TruthTable getTt() {
		return tt;
	}

	/**
	 * @param tt the tt to set
	 */
	public void setTt(TruthTable tt) {
		this.tt = tt;
	}

	public void convertToPost() throws Exception {
	          lexer.ReemplzaDobleImplicaMayor();
                  lexer.RemplazaImplicaPorMenor();
	          lexer.SimplificaNegativo();
		  lexer.lex(lexer.getExpresion());
	
		  
		tt = new TruthTable(Lexer.NUM_VARIABLES, Lexer.NUM_CONECTORES_LOGICOS, Lexer.NUM_CONSTANTES);
		posfix = lexer.infixToPostfix(lexer.getExpresion());
	}

	public void printTable() {
		tt.printTruthTable(posfix);
	}

	public String CheckSintax() {
		lexer.SimplificaNegativo();
		return lexer.validaExpresion();
	}
	public void PrintCanonicas(){
	System.out.println(tt.getMinTerms());
        System.out.println(tt.getMaxTerms());
	}
}
