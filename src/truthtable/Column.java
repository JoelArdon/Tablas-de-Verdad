package truthtable;

/**
 * Column es una clase para almacenar información de cada columna que conforma la tabla de verdad.
 * En la clase TruthTable existe un vector que almacena objetos Column de manera que cada posicion de ese
 * vector implicitamente almacena otro vector (contenido en el objeto Column) que seria la columna como tal. 
 * Creando asi la matriz o bien, la estructura de la tabla de verdad.
 */
public class Column {
    /**
    * <p>Constructor de la clase Column</p> 
    * @param expression Encabezado de la columna, una proposicion como p, q, (p+q)*r, ~s, etc.
    * @param pos_exprLeft Almacena el indice o posicion en el vector truthTable (Atributo de la clase TruthTable) 
    * donde se encuentra la columna de la expresión a la izquierda del operador.(Puede ser -1 para indicar que no hay expresión.
    * a la izquierda del operador).
    * @param pos_exprRight  Almacena el indice o posicion en el vector truthTable (Atributo de la clase TruthTable) 
    * donde se encuentra la columna de la expresión a la derecha del operador. (Puede ser -1 para indicar que no hay expresión
    * a la derecha del operador).
    * @param operator Operador a aplicar en las expresiónes que tiene a la izquierda y a la derecha. Si el operdaor es un ? es porque
    * se trata de una proposicion simple (p, q, ... , w) por lo que pos_exprLeft y pos_exprRight son -1. Si el operdaor es ~ (Negacion o deMorgan)
    * pos_exprLeft sera -1 y pos_exprRight si almacenara un valor valido.
    * @param rows Cantidad de filas para esta columna. Se utiliza por lo tanto para crear el vector 'values' que representa una columna.
    * Deberia ser un valor igual para todas las columnas (Objetos Column) que se ceen.
    * @since 1.0
    */
    public Column(String expression, int pos_exprLeft, int pos_exprRight, char operator, int rows){
        this.expression = expression;
        this.pos_exprLeft = pos_exprLeft;
        this.pos_exprRight = pos_exprRight;
        this.operator = operator;
        this.values = new boolean[rows];
    }

    /**
     * <p>Este método es para obtener la expresion a la izquierda del  de la columna</p> 
     * @return Expresión o encabezado de la columna. 
     * @since 1.0
     */
    public String getExpression() {
        return expression;
    }
    
    /**
     * <p>Este método es para obtener el indice o posición en el vector truthTable (Atributo de la clase TruthTable) 
     * donde se encuentra la columna de la expresión a la izquierda del operador.(Puede ser -1 para indicar que no hay expresión.
     * a la izquierda del operador).</p> 
     * @return Indice o posicion en el vector truthTable (Atributo de la clase TruthTable) 
     * donde se encuentra la columna de la expresión a la izquierda del operador.
     * @since 1.0
     */
    public int getPos_exprLeft() {
        return pos_exprLeft;
    }

    /**
     * <p>Este método es para obtener el indice o posicion en el vector truthTable (Atributo de la clase TruthTable) 
     * donde se encuentra la columna de la expresión a la derecha del operador.(Puede ser -1 para indicar que no hay expresión.
     * a la izquierda del operador).</p> 
     * @return Indice o posición en el vector truthTable (Atributo de la clase TruthTable) 
     * donde se encuentra la columna de la expresión a la derecha del operador.
     * @since 1.0
     */
    public int getPos_exprRight() {
        return pos_exprRight;
    }

    /**
     * <p>Este método es para obtener el vector que es interpretado como columna</p> 
     * @return Retorna un vector que es interpretado como una columna.
     * @since 1.0
     */
    public boolean[] getValues() {
        return values;
    }

    /**
     * <p>Este método es para obtener el operador (Conector lógico) de la expresion (Encabezado) de esta columna.</p> 
     * @return Retorna el operador (Conector lógico) de la expresion (Encabezado) de esta columna.
     * @since 1.0
     */
    public char getOperator() {
        return operator;
    }
    
    /**
     * Atributo que almacena la expresion (Encabezado) de la columna. Ej: p, q, (p+q)*r, ~s => ~w, etc.
     */
    private String expression;
    
    /**
     * Atributo para almacenar la posicion en un vector donde se encuentra la expresion a la izquierda del operador
     */
    private int pos_exprLeft;
    
    /**
     * Atributo para almacenar la posicion en un vector donde se encuentra la expresion a la derecha del operador
     */
    private int pos_exprRight;
    
    /**
     * Un vector para interpretar como columna
     */
    private boolean[] values;
    
    /**
     * Atributo para almacenar un operador (Conector lógico)
     */
    private char operator;
      
}
