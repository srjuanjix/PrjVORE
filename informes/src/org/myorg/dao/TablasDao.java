package org.myorg.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


import org.myorg.conexion.Conexion;

/**
 * Clase que permite el acceso a la base de datos
 * 
 * @author chenao
 * 
 */
public class TablasDao {
 
        public String sStr="";
    
	/**
	 * Usa el objeto enviado para almacenar la persona
	 * @param miPyme
	 */
	public int registrarFila(Conexion conex,String str1,String str2,String str3,String str4,String sqlStr) {
		 
            System.out.println("DATOS (String str1,String str2,String str3,String str4) ="+str1+" - "+str2+" - "+str3+" - "+str4);
            
            int estadoInsert = 0 ;

		try {
			Statement estatuto = conex.getConnection().createStatement();
			estatuto.executeUpdate(sqlStr);
			this.sStr = "Registro insertado!!!";
			estatuto.close();
			
                       

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        this.sStr = e.getMessage();
			 estadoInsert = 2 ;
		}
                
                return estadoInsert;
	}


}
