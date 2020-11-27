package truthtable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * TruthTable es una clase que se encarga de crear la estructura (matriz) de una tabla de verdad, calcula 
 * los valores de verdad para cada columna segun corresponda y tambien puede obtener las fromas canónicas 
 * (Conjuntiva y Disyuntiva) de la expresión regular evaluada.
 */
public class TruthTable {

  
public TruthTable(){
	

}

  /**
    * <p>Constructor de la clase TruthTable.</p> 
    * @param  num_variables Cantidad de proposiciones simples (p, q, ... , w) que tiene la expresión a evaluar.
    * @param num_conectoresLogicos Cantidad de operadores o conectores lógicos (~, +, *, etc) que tiene la expresión 
    * @param num_constantes Cantidad de constantes (V, F) que tiene la expresión 
    * a evaluar.
    * @since 1.0
    */
    public TruthTable(int num_variables, int num_conectoresLogicos, int num_constantes) {
        this.MAXterms = new ArrayList<>();
        this.MINterms = new ArrayList<>();
        this.num_columns = 0;
        this.rows = (int) Math.pow(2, num_variables);
        this.truthTable = new Column[num_variables + num_conectoresLogicos + num_constantes];     
    }

    /**
     * <p>Este método se encarga de llenar el vector "truthTable" de objectos tipo Column.</p>
     * Básicamente elavula una expresión lógica postfija para ir creando las columnas (Objetos Column) 
     * de la tabla de verdad. Dependiendo del tipo de token que este evaluando en el momento crea el objeto Column
     * con distintos parametros. Hace uso de dos pilas y 3 variables String para poder crear los encabezados de las 
     * columnas, es decir, la proposición a evaluar.
     * @param postfija Expresión en notación polaca inversa (notación postfija) de la expresión a evaluar.
     * @since 1.0
     */
    private void truthTableGenerator(String postfija) { 
        Stack<String> stack1 = new Stack<>();
        Stack<String> stack2 = new Stack<>();
        String prop1 = null, prop2 = null, aux = null;
        int i = 0;
	num_columns = 0;
        char tokenAux = ' ';

        while (i < postfija.length()) {
            for (Token.Tipo value : Token.Tipo.values()) {
                String token = String.valueOf(postfija.charAt(i));
                Pattern patron = Pattern.compile(value.patron);
                Matcher matcher = patron.matcher(token);

                if (matcher.find()) {
                    if (value.equals(Token.Tipo.PROPOSICION)) {
                        stack1.add(token);
                        if (!this.truthTableContains(token)) {
                            Column column = new Column(token, -1, -1, '?', rows); //proposiciones simples (p, q, r, ... , w)
                            truthTable[num_columns] = column;
                            num_columns++;
                        }

                    } else if(value.equals(Token.Tipo.CONSTANTES)){
                        stack1.add(token);
                        if (!this.truthTableContains(token)) {
                            Column column = new Column(token, -1, -1, 'C', rows); //proposiciones simples (p, q, r, ... , w)
                            truthTable[num_columns] = column;
                            num_columns++;
                        }
                        
                    }else if (value.equals(Token.Tipo.CONECTOR_LOGICO) && !stack1.isEmpty()) {
                        tokenAux = token.charAt(0);
                        if (token.equals("<")) token = "<=>";  //doble implicacion                       
                        if (token.equals(">")) token = "=>";   //implicacion     
                        
                        prop2 = stack1.pop();

                        if (!stack1.isEmpty()) {
                            prop1 = stack1.pop();
                        } else {
                            prop1 = stack2.pop();
                        }

                        aux = "(" + prop1 + token + prop2 + ")";
                        stack1.push(aux);
                        if (tokenAux == '>' || tokenAux == '<') {
                            truthTable[num_columns] = new Column(aux, getColumnReferenceIndex(prop1), getColumnReferenceIndex(prop2), tokenAux, rows);
                        } else {
                            truthTable[num_columns] = new Column(aux, getColumnReferenceIndex(prop1), getColumnReferenceIndex(prop2), token.charAt(0), rows);
                        }

                        num_columns++;

                    } else if (value.equals(Token.Tipo.CONECTOR_LOGICO) && stack1.isEmpty()) {
                        tokenAux = token.charAt(0);
                        if (token.equals("<")) token = "<=>";  //doble implicacion         
                        if (token.equals(">")) token = "=>";   //implicacion     
                        
                        prop2 = stack2.pop();
                        prop1 = stack2.pop();
                        aux = "(" + prop1 + token + prop2 + ")";
                        stack2.push(aux);
                        if (tokenAux == '>' || tokenAux == '<') {
                            truthTable[num_columns] = new Column(aux, getColumnReferenceIndex(prop1), getColumnReferenceIndex(prop2), tokenAux, rows);
                        } else {
                            truthTable[num_columns] = new Column(aux, getColumnReferenceIndex(prop1), getColumnReferenceIndex(prop2), token.charAt(0), rows);
                        }
                        num_columns++;

                    } else if (value.equals(Token.Tipo.NEGACION) && !stack1.isEmpty()) {
                        prop1 = stack1.pop();
                        aux = token + prop1;
                        stack1.add(aux);
                        truthTable[num_columns] = new Column(aux, -1, getColumnReferenceIndex(prop1), token.charAt(0), rows);
                        num_columns++;

                    } else if (value.equals(Token.Tipo.NEGACION) && stack1.isEmpty()) {
                        prop1 = stack2.pop();
                        aux = token + prop1;
                        stack2.add(aux);
                        truthTable[num_columns] = new Column(aux, -1, getColumnReferenceIndex(prop1), token.charAt(0), rows);
                        num_columns++;

                    } 
                    i++;
                    break;
                }
            }
        }      
    }

  
    public void printTruthTable(String expression) {
        truthTableGenerator(expression); 
        fillTruthTable();
        calculateMINAndMAXterms();
        
        //imprime encabezados de las columnas
        for (int i = 0; i < num_columns; i++) {
            System.out.print(truthTable[i].getExpression() + " | ");
        }
        System.out.println("\n");

        //imprime contenido de la tabla de verdad
        int x = 0;
        while (x < truthTable[0].getValues().length) {
            for (int i = 0; i < num_columns; i++) {

                if (!truthTable[i].getValues()[x]) {
                    System.out.print("F  |  ");
                } else {
                    System.out.print("V  |  ");
                }

            }
            System.out.println();
            x++;
        }
        System.out.println("\n");
        
    }
    
    /**
     * <p>Este método se utiliza en la creación de los encabezados de las columnas de proposiciones simples (p, q, ... , w)
     * para no duplicar columnas ya que en la expresion postfija que esta siendo evaluada pueden aparecer.</p> 
     * @param proposition Proposición simple a comprobar si existe ya una columna para ella.   
     * @return Retorna True si el vector truthTable ya la contiene, False si no.
     * @since 1.0
     */
    private boolean truthTableContains(String proposition) {
        for (int i = 0; i < num_columns; i++) {
            if (truthTable[i].getExpression().equals(proposition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * <p>Método encargado de llenar la tabla de verdad con los valores de verdad segun corresponda.</p>
     * Básicamente lo que que se hace es recorrer el vector truthTable que contiene todas las columnas (Objetos Column)
     * en cada iteracion se verifica cual es el operador que el objeto Column almacena en esa casilla y dependiendo de eso
     * con ayuda de los demas atributos del objeto Column se sabe que operación hay que ejecutar a cuales proposiciones. Sabiendo esto
     * se procede a evaluar con una función de negación, disyunción, conjunción, implicación o doble implicación segun corresponda las
     * proposiciones que correspondan. Por lo tanto el bucle de la tabla es: Ver que proposicion tiene la columna (encabezado) y luego
     * se recorren las filas llenando cada casilla con el valor resultante de la operacion. Si el encabezado se trata de una proposicion
     * simple (p, q, ... , w) que se identifica por que el operador es un "?" simplemente se llenan sus filas con auida de la variable "n".
     * @since 1.0
     */
    private void fillTruthTable() { 
        int n = rows / 2;
        int leftIndexReference = -1, rightIndexReference = -1;

        for (int i = 0; i < num_columns; i++) {
            boolean[] aux = truthTable[i].getValues();
            int x = 0;

            switch (truthTable[i].getOperator()) {
                //conjuncion
                case '*':
                    leftIndexReference = truthTable[i].getPos_exprLeft(); //posicion de la proposicion a la izquierza del *
                    rightIndexReference = truthTable[i].getPos_exprRight(); //posicion de la proposicion a la derecha del *
                    for (x = 0; x < aux.length; x++) {
                        aux[x] = conjunction(truthTable[leftIndexReference].getValues()[x], truthTable[rightIndexReference].getValues()[x]);
                    }
                    break;
                //disyuncion
                case '+':
                    leftIndexReference = truthTable[i].getPos_exprLeft(); //posicion de la proposicion a la izquierza del +
                    rightIndexReference = truthTable[i].getPos_exprRight(); //posicion de la proposicion a la derecha del +
                    for (x = 0; x < aux.length; x++) {
                        aux[x] = disjunction(truthTable[leftIndexReference].getValues()[x], truthTable[rightIndexReference].getValues()[x]);
                    }
                    break;
                //negacion
                case '~':
                    rightIndexReference = truthTable[i].getPos_exprRight(); //posicion de la proposicion que hay que negar
                    for (x = 0; x < aux.length; x++) {
                        aux[x] = deMorgan(truthTable[rightIndexReference].getValues()[x]);
                    }
                    break;
                //implicacion
                case '>':
                    leftIndexReference = truthTable[i].getPos_exprLeft(); //posicion de la proposicion a la izquierza del =>
                    rightIndexReference = truthTable[i].getPos_exprRight(); //posicion de la proposicion a la derecha del => 
                    for (x = 0; x < aux.length; x++) {
                        aux[x] = implication(truthTable[leftIndexReference].getValues()[x], truthTable[rightIndexReference].getValues()[x]);
                    }
                    break;
                //doble implicacion
                case '<':
                    leftIndexReference = truthTable[i].getPos_exprLeft(); //posicion de la proposicion a la izquierza del <=>
                    rightIndexReference = truthTable[i].getPos_exprRight(); //posicion de la proposicion a la derecha del <=>
                    for (x = 0; x < aux.length; x++) {
                        aux[x] = doubleImplication(truthTable[leftIndexReference].getValues()[x], truthTable[rightIndexReference].getValues()[x]);
                    }
                    break;
                //proposicion
                case '?':
                    int contT = 0;
                    int contF = 0;

                    for (x = 0; x < aux.length; x++) {
                        if (contF < n) {
                            aux[x] = false;
                            contF++;
                        } else if (contT < n) {
                            aux[x] = true;
                            contT++;

                        } else {
                            contT = 0;
                            contF = 0;
                            x--; //al entrar en este else se utiliza una iteracion que hay que utilizarla aplicando uno de los dos 
                                 //primeros if y aqui solamente se estan seteando los contadores;
                        }
                    }
                    n /= 2;
                    break;
                case 'C': //constantes
                    for (x = 0; x < aux.length; x++) {
                        if(truthTable[i].getExpression().equals("F")){
                            aux[x] = false;
                        }else{
                            aux[x] = true;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * <p>Este método se utiliza en la creación de las columnas para saber entre cuales proposiciones (columnas) 
     * hay que ejecutar la operacion que corresponda.</p>
     * @param proposition Proposición de la cual se desea saber la posición o el index dentro del vector truthTable.
     * @return Posición o index dentro del vector truthTable. Retorna -1 si no la encuentra.
     * @since 1.0
     */
    private int getColumnReferenceIndex(String proposition) { 
        for (int i = 0; i < num_columns; i++) {
            if (truthTable[i].getExpression().equals(proposition)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * <p>Este método es utilizado para aplicar una funcion de conjunción a dos valores de verdad.</p>
     * @param truthValue_left Valor de verdad de la casilla (Posición unica dada por columna-fila) a la izquierda de la conjunción.
     * @param truthValue_right Valor de verdad de la casilla (Posición unica dada por columna-fila) a la derecha de la conjunción.
     * @return Resultado de la conjuncion.
     * @since 1.0
     */
    private boolean conjunction(boolean truthValue_left, boolean truthValue_right) { 
        return truthValue_left && truthValue_right;
    }

     /**
     * <p>Este método es utilizado para aplicar una funcion de disyunción a dos valores de verdad.</p>
     * @param truthValue_left Valor de verdad de la casilla (Posición unica dada por columna-fila) a la izquierda de la disyunción.
     * @param truthValue_right Valor de verdad de la casilla (Posición unica dada por columna-fila) a la derecha de la disyunción.
     * @return Resultado de la disyunción.
     * @since 1.0
     */
    private boolean disjunction(boolean truthValue_left, boolean truthValue_right) {
        return truthValue_left || truthValue_right;
    }

    /**
     * <p>Este método es utilizado para aplicar una funcion de negación a un valor de verdad.</p>
     * @param truthValue_right Valor de verdad de la casilla (Posición unica dada por columna-fila) a la derecha de la negación.
     * @return Resultado de la disyunción.
     * @since 1.0
     */
    private boolean deMorgan(boolean truthValue_right) {
        return !truthValue_right;
    }

    /**
     * <p>Este método es utilizado para aplicar una funcion de implicación a dos valores de verdad.</p>
     * @param truthValue_left Valor de verdad de la casilla (Posición unica dada por columna-fila) a la izquierda de la implicación.
     * @param truthValue_right Valor de verdad de la casilla (Posición unica dada por columna-fila) a la derecha de la implicación.
     * @return Resultado de la implicación.
     * @since 1.0
     */
    private boolean implication(boolean truthValue_left, boolean truthValue_right) { 
        return (truthValue_left && truthValue_right) || (!truthValue_left && !truthValue_right) || (!truthValue_left && truthValue_right);
    }
    
    /**
     * <p>Este método es utilizado para aplicar una funcion de doble implicación a dos valores de verdad.</p>
     * @param truthValue_left Valor de verdad de la casilla (Posición unica dada por columna-fila) a la izquierda de la doble implicación.
     * @param truthValue_right Valor de verdad de la casilla (Posición unica dada por columna-fila) a la derecha de la doble implicación.
     * @return Resultado de la doble implicación.
     * @since 1.0
     */
    private boolean doubleImplication(boolean truthValue_left, boolean truthValue_right) {
        return (truthValue_left == truthValue_right);
    }
    
    /**
     * <p>Este método es utilizado para calcular las Formas Canónicas (Conjuntiva (FCC) y Disyuntiva (FCD)).</p>
     * @since 1.0
     */
    private void calculateMINAndMAXterms(){ 
        for(int i=0; i<rows; i++){  
            String minterm = "";
            String maxterm = "";
            if(truthTable[num_columns-1].getValues()[i] == true){ //MINterms
                for(int x=0; x<num_columns; x++){
                    if(truthTable[x].getOperator() == '?'){
                        if(truthTable[x].getValues()[i] == true){
                            minterm += truthTable[x].getExpression();
                        }else{
                            minterm += truthTable[x].getExpression() + "'";
                        }
                    }
                }
                MINterms.add(minterm);  
                
            }else{ //MAXterms
                for(int x=0; x<num_columns; x++){
                    if(truthTable[x].getOperator() == '?'){
                        if(truthTable[x].getValues()[i] == false){
                            maxterm += truthTable[x].getExpression()+ "+";
                        }else{
                            maxterm += truthTable[x].getExpression() + "'+";
                        }        
                    }
                }
                maxterm = maxterm.substring(0, maxterm.length()-1); //quita el + que se genera siempre al final de cada MAXterm
                MAXterms.add(maxterm);
            }
        }  
    }
    
    /**
     * <p>Método que construye la Forma Canónica Disyuntiva (FCD).</p>
     * @return Hilera con la Forma Canónica Disyuntiva.
     * @since 1.0
     */
    @XmlAttribute 
    public String getMinTerms(){    
        String formaCanonicaDisyuntiva = "";
        for(int i=0; i<MINterms.size(); i++){
            formaCanonicaDisyuntiva += "(" + MINterms.get(i) + ")";
            if(i<MINterms.size()-1){
                formaCanonicaDisyuntiva += "+";
            }
        }
        return  formaCanonicaDisyuntiva;
    }
    
    /**
     * <p>Método que construye la Forma Canónica Conjuntiva (FCC).</p>
     * @return Hilera con la Forma Canónica Conjuntiva.
     * @since 1.0
     */
   
    public Column[] getTruthTable() {
        return truthTable;
    }
 @XmlAttribute 
    public int getNum_columns() {
        return num_columns;
    }
 @XmlAttribute 
    public int getRows() {
        return rows;
    }
    @XmlAttribute 
    public String getMaxTerms(){    
        String formaCanonicaConjuntiva = "";
        for(int i=0; i<MAXterms.size(); i++){
            formaCanonicaConjuntiva += "(" + MAXterms.get(i) + ")";  
        }
        return  formaCanonicaConjuntiva;
    }
    
    /**
     * <p>Atributo que almacena los objetos Column.</p>
     * Por medio de este atributo podemos acceder a los atributos de cada columna.
     */
    private  Column truthTable[];
    
    /**
     * <p>Cantidad de elementos que tiene el vector TruthTable</p>
     */
  
    private int num_columns;
    
    /**
     * <p>Cantidad de filas que se crearon o bien que tiene la tabla</p>
     */

    private int rows;
    
    /**
     * <p>Arraylist que alamacena cada uno de los MAXterms que conforman la Forma Canónica Conjuntiva (FCC)</p> 
     */
    private  ArrayList<String> MAXterms;
    
    /**
     * <p>Arraylist que alamacena cada uno de los MINterms que conforman la Forma Canónica Disyuntiva (FCD)</p>
     */
    private  ArrayList<String> MINterms;
       
}
