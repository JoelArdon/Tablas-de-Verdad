/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Eval.Eval;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import views.VentanaExprecion;
import views.VentanaPrincipal;

/**
 *
 * @author Luver
 */
public class ControlVentanaPrincipal implements ActionListener {
	VentanaPrincipal ventanaPrincipal;
	
	
	public ControlVentanaPrincipal(VentanaPrincipal ventanaPrincipal){
		this.ventanaPrincipal = ventanaPrincipal;
		this.ventanaPrincipal.mnuNuevo.addActionListener(this);
//		this.ventanaPrincipal.mnuGuardar.addActionListener(this);
		this.ventanaPrincipal.mnuAbrir.addActionListener(this);
	}
	
	public void IniciarVista(){
		ventanaPrincipal.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource().equals(ventanaPrincipal.mnuNuevo)){
		VentanaExprecion view = new VentanaExprecion();
		ControlVentanaExpresion ctrExp = new ControlVentanaExpresion(view);
		
		}
		if(ae.getSource().equals(ventanaPrincipal.mnuAbrir)){
			VentanaExprecion view = new VentanaExprecion();
		ControlVentanaExpresion ctrExp = new ControlVentanaExpresion(view);
		ctrExp.CargaArchivo();
		}
		
	}


	
}
