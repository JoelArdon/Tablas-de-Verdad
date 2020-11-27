/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Eval.Eval;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import truthtable.TruthTable;
import views.VentanaExprecion;

/**
 *
 * @author Luver
 */
public class ControlVentanaExpresion implements ActionListener {
	VentanaExprecion ventanaExpresion;
	Eval evaluador;
	boolean flag = false;
	
	public ControlVentanaExpresion(VentanaExprecion ventanaExpresion){
		this.ventanaExpresion = ventanaExpresion;
		this.ventanaExpresion.BtnVerificar.addActionListener(this);
		this.ventanaExpresion.BtnGuardar.addActionListener(this);
		IniciaVista();
	}
	public void IniciaVista(){
		this.ventanaExpresion.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource().equals(ventanaExpresion.BtnVerificar)) {
			verifica();
		}
		if (ae.getSource().equals(ventanaExpresion.BtnGuardar)) {
			try {
				guardarArchivo();
			} catch (IOException ex) {
				Logger.getLogger(ControlVentanaExpresion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	
	public void guardarArchivo() throws IOException  {
		// Se crea el file chooser y se filtra para solo mostrar 
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
			"xml files (*.xml)", "xml");
		fileChooser.setFileFilter(xmlfilter);
		
		

		if (fileChooser.showSaveDialog(ventanaExpresion) == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fileChooser.getSelectedFile();
				
				
				System.out.print(file.getName());
				
				
				
				System.out.print(file.getName());
				//Se convierte el archivo a 
				JAXBContext contextObj = JAXBContext.newInstance(Eval.class);
				Marshaller marshallerObj = contextObj.createMarshaller();
				marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
       
				marshallerObj.marshal(evaluador, file);
					Path source = Paths.get(file.getCanonicalPath());
			         Files.move(source,source.resolveSibling(file.getName()+".xml"));
				
				
				JOptionPane.showMessageDialog(ventanaExpresion, "Se guardo el archivo correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE);
			} catch (JAXBException ex) {
				Logger.getLogger(ControlVentanaExpresion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

	}
	
	
	
	public void verifica(){
       
            
		String input = ventanaExpresion.TxtExpresion.getText();
                input = input.replace(" ","");
                if(input.isEmpty()){         
                     JOptionPane.showMessageDialog(ventanaExpresion, "Debe ingresar una expresió para poder evaluarla","Error",JOptionPane.ERROR_MESSAGE);
        } else {
                       
        		 evaluador = new Eval(input);
			 evaluador.getLex().cambiaSimbolos();
			 evaluador.getLex().SimplificaNegativo();
		
	
		if(evaluador.CheckSintax().equals("true")){
			try {			
				evaluador.convertToPost();	
		                evaluador.printTable();
				ventanaExpresion.txtMinTerms.setText(evaluador.getTt().getMinTerms());
				ventanaExpresion.txtMaxTerm.setText(evaluador.getTt().getMaxTerms());
				evaluador.PrintCanonicas();
				 mostrarTablaDeVerdad(evaluador.getTt());
				 flag = true;
			} catch (Exception ex) {
				Logger.getLogger(ControlVentanaExpresion.class.getName()).log(Level.SEVERE, null, ex);
			}
		
	}
	else{
		JOptionPane.showMessageDialog(ventanaExpresion, evaluador.CheckSintax(),"Error",JOptionPane.ERROR_MESSAGE);
	}
                }

	}
	public void CargaArchivo(){
		JFileChooser fileChooser = new JFileChooser();
		
		FileNameExtensionFilter xmlfilter = new FileNameExtensionFilter(
			"xml files (*.xml)", "xml");
		fileChooser.setFileFilter(xmlfilter);


		if (fileChooser.showSaveDialog(ventanaExpresion) == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fileChooser.getSelectedFile();
				if (!file.getName().endsWith(".xml")) {
					file = new File(file.getName() + ".xml");
				}
				//Se convierte el archivo a 
			        JAXBContext jaxbContext = JAXBContext.newInstance(Eval.class);  
				    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
                                 evaluador = (Eval) jaxbUnmarshaller.unmarshal(file);  
				 ventanaExpresion.TxtExpresion.setText(evaluador.getLex().getExpresion());
				 verifica();
			} catch (JAXBException ex) {
				Logger.getLogger(ControlVentanaExpresion.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}
	private void mostrarTablaDeVerdad(TruthTable truthTable) {
        //Va creando los encabezados que va insertando en la tabla para mostrar en la UI para cada columna
        String[] encabezados = new String[truthTable.getNum_columns()];    
	//String encabezados2[] = {"abc","acd","abd"};
	
        for(int i=0; i<truthTable.getTruthTable().length; i++){
		
           encabezados[i] = truthTable.getTruthTable()[i].getExpression();
	  
		
	    
        }
        
        //Va creando filas que va insertando en la tabla para mostrar en la UI
        String[][] matriz = new String[truthTable.getRows()][truthTable.getNum_columns()];
       int x = 0;
        while (x < truthTable.getTruthTable()[0].getValues().length) {
            for (int i = 0; i < truthTable.getNum_columns(); i++) {
                if (!truthTable.getTruthTable()[i].getValues()[x]) {
                    matriz[x][i] = "F";
                } else {
                    matriz[x][i] = "V";
                }
            }
            x++;
        }

        JTable tabla = crearTabla(matriz, encabezados);
        ventanaExpresion.jScrollPane1.add(tabla);
        ventanaExpresion.jScrollPane1.setViewportView(tabla);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tabla.repaint();
    }
    
    private JTable crearTabla(Object[][] matriz, Object[] encabezados){
        JTable tabla = new JTable(matriz, encabezados);
        
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setOpaque(false);
        tabla.getTableHeader().setBackground(new Color(32, 136, 203));
        tabla.getTableHeader().setForeground(new Color(255, 255, 255));
        tabla.setRowHeight(25);
             /* DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
centerRenderer.setHorizontalAlignment( JLabel.RIGHT );
tabla.setDefaultRenderer(String.class, centerRenderer);  */
        tabla.setEnabled(false);
        
        return tabla;
    }

		
}
