
package org.myorg.informes;

/**
 *
 * @author jab7b7
 */

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

//Implementa la interfaz TableCellRenderer que servirá para dar formato a la tabla
public class FormatoTablaPuntos implements TableCellRenderer{
    
    public int fuente ;
    
    //Sobreescribimos el método getTableCellRendererComponent para poder formatear los componentes
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
  //      System.out.println("--------- ESTOY CAMBIANDO FORMATO TABLA ------------ row="+row+" y column="+column);
        
        //campoTexto nos sirve como componente dentro de la tabla, es cada una de las celdas de la misma
        JFormattedTextField campoTexto = new JFormattedTextField();
        //Se establece primeramente un borde vacío o sin borde
        campoTexto.setBorder(BorderFactory.createEmptyBorder());
        
        //Si el valor que contiene actualmente es cadena se establece el valor sin formatear en ningún sentido
        
        // .................................................................................................... tamaño de fuente
        
        if ( fuente == 1)  campoTexto.setFont(new java.awt.Font("Tahoma", 0, 16)); else campoTexto.setFont(new java.awt.Font("Tahoma", 0, 12));
        
// ....................................................................................................
        
        if(value instanceof String){
   //         System.out.println("String value = " + value);
            campoTexto.setText((String)value);
            
    //         campoTexto.setBackground(new Color(0x33CC33)); 
             campoTexto.setOpaque(true); 
            
        }
        
        //Si el valor que contiene actualmente es entero se establece el valor y se alinea al centro 
        if(value instanceof Integer){
     //       System.out.println("Integer value = " + value);
            campoTexto.setText(value+"");
            campoTexto.setHorizontalAlignment(SwingConstants.CENTER);
        }
          
        
        //Si el valor que contiene actualmente es Double se establece el valor
        //y se le aplica un formato para agregar comas cada tres dítigos
        if(value instanceof Double){
       //     System.out.println("Double value = " + value);
            Double valor = (Double)value;
            campoTexto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,####0.00000")))); 
            //Se aplica alineación a la izquierda
            campoTexto.setHorizontalAlignment(SwingConstants.TRAILING); 
            campoTexto.setValue(valor);
            
            //Si el valor es negativo se elimina el símbolo - y se establece un fondo de color rojo
            if(valor.compareTo(new Double(0))==-1) { 
             // campoTexto.setText(campoTexto.getText().replace("-", "")); 
              campoTexto.setBackground(new Color(0xFF9999)); 
              campoTexto.setOpaque(true); 
            }
                        
        }
        /*
        if (column == 1) {
          if(((Integer)table.getValueAt(row, 1)).compareTo(new Integer(0))== 0){ 
             campoTexto.setText("");
             campoTexto.setOpaque(true); 
         } else {
             campoTexto.setText("X");
             campoTexto.setHorizontalAlignment(SwingConstants.CENTER);
             campoTexto.setOpaque(true); 
          }
        }
        if (column == 6) {
          if(((Integer)table.getValueAt(row, 6)).compareTo(new Integer(0))== 0){ 
             campoTexto.setText("");
             campoTexto.setOpaque(true); 
         } else {
             campoTexto.setText("X");
             campoTexto.setHorizontalAlignment(SwingConstants.CENTER);
             campoTexto.setOpaque(true); 
          }
        }
        */
        
        
        //Por último se devuelve el componente ya con el formato establecido para mostrarlo en la tabla
        return campoTexto;
    }
    
}