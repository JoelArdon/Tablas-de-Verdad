/*
 * To lexerange this license header, lexeroose License Headers in Project Properties.
 * To lexerange this template file, lexeroose Tools | Templates
 * and open the template in the editor.
 */
package proyectoparadigmas;

import Control.ControlVentanaPrincipal;
import Eval.Eval;
import sintaxis.Lexer;
import truthtable.TruthTable;
import views.VentanaExprecion;
import views.VentanaPrincipal;

/**
 *
 * @author Leiner
 */
public class ProyectoParadigmas {

   
      public static void main(String[] args) throws Exception {
    VentanaPrincipal VE = new VentanaPrincipal();
       ControlVentanaPrincipal ctrlP = new  ControlVentanaPrincipal(VE);
       ctrlP.IniciarVista();
  
	
      }

}

