package org.myorg.conexion;

import java.sql.*;
import javax.swing.JOptionPane;


/**
 * Clase que permite conectar con la base de datos
 * @author jalbarracin
 *
 */
public class Conexion {
   static String bd = "saepdb"; 
   static String login = "admin03";
   static String password = "admin03";
   static String url = "jdbc:mysql://192.168.1.111/"+bd;

   Connection conn = null;

   /** Constructor de DbConnection */
 //  public Conexion(String Str1,String Str2,String Str3,String Str4) {
    public Conexion() {
      try{            
        /*  
         login      =  Str1;
         password   =  Str2;
         bd         =  Str3;
         url        =  Str4+bd;
         */
          
         //obtenemos el driver de para mysql
         Class.forName("com.mysql.jdbc.Driver");
         //obtenemos la conexión
         conn = DriverManager.getConnection(url,login,password);

       
      }
      catch(SQLException e){
         System.out.println(e);
      }catch(ClassNotFoundException e){
         System.out.println(e);
      }catch(Exception e){
         System.out.println(e);
      }
   }
   /**Permite retornar la conexión*/
   public Connection getConnection(){
      return conn;
   }

   public void desconectar(){
       System.out.println("--------------------------> DESCONECTANDO DE MYSQL");
       try {
           conn.close();
       } catch (SQLException ex) {
           System.out.println("Error cerrando conexión =>");
           
       }
      conn = null;
   }

}