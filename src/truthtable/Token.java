package truthtable;

/**
* Token es una clase que contiene informacion sobre los tokens que son aceptados para las funciones logicas
*/
public class Token {
    
    /**
    * <p>Este método es para obtener el tipo del token</p> 
    * @return Tipo del token
    * @since 1.0
    */
    public Tipo getTipo() {
        return tipo;
    }
    
    /**
     * <p>Este método es para modificar el tipo de token</p> 
     * @param tipo nuevo tipo de token
     * @since 1.0
     */
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }
    
    /**
    * <p>Este método es para obtener el valor del token</p> 
    * @return Valor del token
    * @since 1.0
    */
    public String getValor() {
        return valor;
    }
    
    /**
     * <p>Este método es para modificar el valor de token</p> 
     * @param valor nuevo tipo de token
     * @since 1.0
     */
    public void setValor(String valor) {
        this.valor = valor;
    }
    
    /**
    * Atributo para almacenar el tipo de token
    */
    public Tipo tipo;
    
    /**
    * Atributo para almacenar el valor del token
    */
    public String valor;

    /**
    * Tipos de token que son aceptados en la expresion logica.
    */
    public enum Tipo {
        PROPOSICION ("[a-z]"), 
        NEGACION ("[~]"),
        CONECTOR_LOGICO ("[+*><]"),
        PARENTESIS ("[)(]"),
        CONSTANTES ("[VF]");

        /**
        * Almacena una expresion regular para aceptar un caracter (token)
        */
        public final String patron;
        
        /**
        * Constructor del enum Tipo
        */
        Tipo(String patron) {
            this.patron = patron;
        }
        
    }

}
