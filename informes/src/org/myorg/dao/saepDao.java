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

import java.text.SimpleDateFormat;


import org.myorg.conexion.Conexion;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Clase que permite el acceso a la base de datos
 * 
 * @author chenao
 * 
 */
public class saepDao {
 
	
        public String clientes[][]                 = new String[500][5];  
        public String datosPuntoSum[][]            = new String[1000][25]; 
        public String contratosPuntos[][]          = new String[1000][15]; 
        public String condicionesSimulacion[][]    = new String[1000][30]; 
        
        public String lhistoricoAhorros[][]        = new String[5000][20];
        
        public int lhistoricoCalculos[][]          = new int[5000][7];
        public String lDatosCalculos[][]           = new String[5000][30];
        
        public int nClientes = 0; 
        public int nPuntos= 0; 
        public int nRegAhorros= 0; 
        public int id =0 ;
        public int nCalculosPunto=0;
        public int nPuntosCalAhorro=0 ;
        public int nPuntosCalAhorroDetalle=0;
        public int nPuntosAlertaFinServicio=0;
        
        public String fechaUltimoCalculo="" ;
        
        public int nCalculos=0;
        public int nCalculosDetalle=0;
        
        public double ahorroTotal = 0 ;
        public double pAhorro=0;
        
  // ------------------------------------------------------------------------------------------------------------------       
        public String fecha_inicio="" ;
        public String fecha_fin   ="" ;
 // ------------------------------------------------------------------------------------------------------------------
        public double p1=0,p2=0,p3=0 ;
        public double e1=0,e2=0,e3=0,ep=0,e1s=0,e2s=0,e3s=0;              // energias
        
        public double pp=0,pll=0,pv=0;                                  // potencias facturadas 
        public double pcp=0,pcll=0,pcv=0;                               // potencias contratadas  
        public double ppp=0,ppll=0,ppv=0;                               // precio potencias por dia
    
        public double pe1=0,pe2=0,pe3=0,pep=0;                          // precio de energias
        public double r1=0,r2=0,r3=0,TR=0;                              // Reactiva
        public double alquiler=0, descuento=0;          
        
        public double pF1,pF2,pF3,rF1,rF2,rr1,rr2,rr3, rs1,rs2, pAlq ; 
        
 // ------------------------------------------------------------------------------------------------------------------       
         public String clientesSimulacion[] = new String[100] ;     
         public int nClienteSimulacion = 0 ;
        
         
         public String simulacionPuntos[][][] = new String[100][50][3] ;
         public int nPuntosSimulacion   = 0 ;
         
         public String condicionesContratoSimulacion[]    = new String[30]; 
//         
  	public int consultaClientes() {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0 ;
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
          //  String sqlStr="SELECT * FROM t_clientes ORDER BY idT_Clientes asc";
            String sqlStr="SELECT * FROM v_consulta_lista_clientes ORDER BY nombre asc";
                try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                        this.nClientes = contador;
                        
                        
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                    clientes[contador][0] = rs.getString("idT_Clientes");    
                                    clientes[contador][1] = rs.getString("nombre");    clientes[contador][1] = clientes[contador][1].toUpperCase();   
                                    clientes[contador][2] = rs.getString("alias");     clientes[contador][2] = clientes[contador][1].toUpperCase();  
                                    clientes[contador][3] = formatDateJava.format(rs.getDate("fecha_alta")) ;    
                                    clientes[contador][4] = Integer.toString(rs.getInt(5)) ;    
                               //     System.out.println("Añadiendo cliente :"+rs.getString("nombre"));
                                    contador++;
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+nClientes+" Clientes");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}

// ------------------------------------------------------------------------------------------------------------------
 public int cargarDatosCliente(int id_cliente) {
		 
             Conexion conex = new Conexion();
            int estadoInsert = 0 ;
            
             SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
             
            
            String sqlStr="SELECT * FROM t_datos_puntos_suministro WHERE id_cliente="+id_cliente+" ORDER BY idd asc";

		try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                        this.nPuntos = contador;
                        
                        
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                    datosPuntoSum[contador][0] = Integer.toString(rs.getInt("idd")) ;   
                                    datosPuntoSum[contador][1] = rs.getString("nombre");     
                                    datosPuntoSum[contador][2] = rs.getString("cups");  
                                    datosPuntoSum[contador][3] = rs.getString("cif");  
                                    datosPuntoSum[contador][4] = rs.getString("tarifa_actual");  
                                    datosPuntoSum[contador][5] = Integer.toString(rs.getInt("id_tarifa_actual")) ;  
                                    datosPuntoSum[contador][6] = rs.getString("mercado");  
                                    datosPuntoSum[contador][7] = rs.getString("codpostal");  
                                    datosPuntoSum[contador][8] = rs.getString("localidad");  
                                    datosPuntoSum[contador][9] = rs.getString("provincia");
                                    datosPuntoSum[contador][10] = rs.getString("direccion");  
                                    datosPuntoSum[contador][11] = rs.getString("notas");  
                                    datosPuntoSum[contador][12] = Integer.toString(rs.getInt("fBateriaC")) ; 
                                    datosPuntoSum[contador][13] = Integer.toString(rs.getInt("TrfP")) ;  
                                    datosPuntoSum[contador][14] = Integer.toString(rs.getInt("TrfS")) ;   
                                    datosPuntoSum[contador][15] = Integer.toString(rs.getInt("TrfMax")) ;
                                    datosPuntoSum[contador][16] = Integer.toString(rs.getInt("fMedida")) ;  
                                    datosPuntoSum[contador][17] = Integer.toString(rs.getInt("fCT")) ;  
                                    datosPuntoSum[contador][18] = Integer.toString(rs.getInt("bloque")) ; 
                                    // .............................
                                    try {
                                           datosPuntoSum[contador][19] = formatDateJava.format(rs.getDate("batDesde")) ; 
                                            
                                        } catch (NullPointerException nfe){
                                            datosPuntoSum[contador][19] =  ""; 
                                    }
                                    try {
                                           datosPuntoSum[contador][20] = formatDateJava.format(rs.getDate("batHasta")) ;
                                            
                                        } catch (NullPointerException nfe){
                                            datosPuntoSum[contador][20] = ""; 
                                     }
                                     
                                    datosPuntoSum[contador][21] = Double.toString(rs.getDouble("cosfiP1")) ; 
                                    datosPuntoSum[contador][22] = Double.toString(rs.getDouble("cosfiP2")) ; 
                                    datosPuntoSum[contador][23] =  rs.getString("batDescripcion");  
                                    // .............................
                                   
                          //          System.out.println("Añadiendo punto ("+contador+"): id="+datosPuntoSum[contador][0]+" --- "+rs.getString("nombre")+" id_tarifa_actual="+datosPuntoSum[contador][5]);
                                    contador++;
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+nPuntos+" P");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
	}
 // ------------------------------------------------------------------------------------------------------------------
 public int cargarContratosPuntos(String datosPuntoSum[][],int nTotal,int id_cliente,int id_estado) throws SQLException {
		 
             Conexion conex = new Conexion();
            int estadoInsert = 0 ;
            int i,id_condiciones;
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
              
            
            System.out.println("ESTOY APUNTO DE cargar :"+nTotal+" datos para id_estado="+id_estado);
            Statement estatuto = conex.getConnection().createStatement();
            
   
             String sqlStr="SELECT idcp FROM t_contratos_puntos WHERE id_estado="+id_estado+" ORDER BY idcp DESC";
                        //    System.out.println("sqlstr ="+sqlStr);
			
                            ResultSet rs = estatuto.executeQuery(sqlStr);
                
		try {
                      for (i=0; i<nTotal; i++) {
                
                
                             id_condiciones  = Integer.parseInt(datosPuntoSum[i][0]) ;
                
               
                
                            sqlStr="SELECT * FROM t_contratos_puntos WHERE id_cliente="+id_cliente+" AND id_punto="+id_condiciones+" AND id_estado="+id_estado+" ORDER BY idcp Desc";
                           
			
                            rs = estatuto.executeQuery(sqlStr);

                        int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                        this.nPuntos = contador;                        
                        
                        if (contador >0){
                           
                            rs.beforeFirst();
                            rs.next();
                            contador = 0;
                          //  while (rs.next()) {
                                    contratosPuntos[i][0] = Integer.toString(rs.getInt("id_punto")) ;   
                                    contratosPuntos[i][1] = formatDateJava.format(rs.getDate("fecha_contrato")) ;       
                                    contratosPuntos[i][2] = formatDateJava.format(rs.getDate("fecha_fin")) ; 
                                    contratosPuntos[i][3] = Integer.toString(rs.getInt("id_tarifa")) ;                                   
                                    contratosPuntos[i][4] = Integer.toString(rs.getInt("id_condiciones_contrato")) ;  
                                    contratosPuntos[i][5] = rs.getString("descripcion");  
                                    contratosPuntos[i][6] = rs.getString("fecha_realizacion_cambio");  
                                    contratosPuntos[i][7] = rs.getString("observaciones");  
                                    contratosPuntos[i][8] = rs.getString("compañia");  
                                                                     
                              //      System.out.println("Añadiendo información punto ("+i+"):"+rs.getString("id_punto")+" con tarifa tipo:"+rs.getString("descripcion")+" y id_tarifa="+contratosPuntos[i][3]+" - compañia="+contratosPuntos[i][8]);
                                 
                         //   }
                        } else {
                             System.out.println("NO EXISTEN CONTRATOS DEFINIDOS PARA ESTE CLIENTE cargo a CERO PARA idp="+id_condiciones);
                              contratosPuntos[i][0] = Integer.toString(id_condiciones) ;   
                              contratosPuntos[i][1] = "01-01-2015";       
                              contratosPuntos[i][2] = "01-01-2015"; 
                              contratosPuntos[i][3] = "0" ;                                   
                              contratosPuntos[i][4] = "0" ;  
                              contratosPuntos[i][5] = "";  
                              contratosPuntos[i][6] = "01-01-2015";  
                              contratosPuntos[i][7] = "";  
                              contratosPuntos[i][8] = "";  
                        }
                        }
			rs.close();
			estatuto.close();
			conex.desconectar();
                                

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                      

		}
            
                       
                return 1;
	}
 // ------------------------------------------------------------------------------------------------------------------
 public int cargarCondicionesSimulacion(String listaContratosPuntos[][],int nTotal,int id_cliente,int id_estado) throws SQLException {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0 ;
            int i,id_tarifa,id_punto;
            int flagAlta = 0;
            
            System.out.println("ESTOY APUNTO DE cargar :"+nTotal+" datos de contratos de simulacion con id_estado="+id_estado);
            Statement estatuto = conex.getConnection().createStatement();
            
           
                        String sqlStr="SELECT id FROM t_c20a";
                         //   System.out.println("sqlstr ="+sqlStr);
			
                            ResultSet rs = estatuto.executeQuery(sqlStr);
              
		try {
                      for (i=0; i<nTotal; i++) {
                        
              //          System.out.println("listaContratosPuntos["+i+"][3]="+listaContratosPuntos[i][3]);
                        try {  
                        id_tarifa  = Integer.parseInt(listaContratosPuntos[i][3]) ;                             // identificador de tarifa
                        id_punto   = Integer.parseInt(listaContratosPuntos[i][4]) ; 
                        } catch (NumberFormatException e) {
                            id_tarifa  = 1 ;
                            id_punto   = 0 ; 
                            System.out.println("Error en carga de información de tarifas para punto ("+i+") id_punto="+id_punto);
                            flagAlta    = 1 ;           // no tiene dado de alta nada.
                              System.out.println("Añadiendo CONDICIONES NULAS :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                condicionesSimulacion[i][0] = "01-01-2015";  
                                condicionesSimulacion[i][1] = "31-12-2015";     
                                condicionesSimulacion[i][2] = "0"; 
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = "0";   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = "0";   
                                condicionesSimulacion[i][15] = "0";     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = "";  
                                condicionesSimulacion[i][21] = "0"  ; 
                                condicionesSimulacion[i][22] = "1"  ;
                                condicionesSimulacion[i][23] = "0" ; 
                                condicionesSimulacion[i][24] = "0" ; 
                                condicionesSimulacion[i][25] = "0" ; 
                        }
                        
                        // .......................................................... 
                        switch (id_tarifa) {
                                case 1:
                                    sqlStr="SELECT * FROM t_c20a WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT * FROM t_c20dha WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                              //      System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT * FROM t_c21a WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT * FROM t_c21dha WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT * FROM t_c30a WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT * FROM t_c31a WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 7:
                                    sqlStr="SELECT * FROM t_c61a WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT * FROM t_c20dhaindx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT * FROM t_c21dhaindx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT * FROM t_c30aindx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc ";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT * FROM t_c20indx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr); 
                                break;
                                case 12:
                                    sqlStr="SELECT * FROM t_c21indx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr); 
                                break;
                                case 13:
                                    sqlStr="SELECT * FROM t_c31aindx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 14:
                                    sqlStr="SELECT * FROM t_c61aindx WHERE id_cliente="+id_cliente+" AND id="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                        // .......................................................... 
                            
                        rs = estatuto.executeQuery(sqlStr);

                        int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                        this.nPuntos = contador;                        
                        
                        if (contador >0){
                           
                            rs.beforeFirst();
                            rs.next();
                            contador = 0;
                            switch (id_tarifa) {
                                case 1:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ; // System.out.println(condicionesSimulacion[i][2]);
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia")) ;   
                                condicionesSimulacion[i][15] = "0";     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");  
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "1"  ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                               
                         //       System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;
                                case 2:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");  
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "2"  ;
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                                
                            //    System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                case 3:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia")) ;   
                                condicionesSimulacion[i][15] = "0";     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "3"  ;
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                                // System.out.println("Añadiendo información punto ("+i+") :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                case 4:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "4"  ;
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                                // System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                case 5:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                condicionesSimulacion[i][3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                condicionesSimulacion[i][4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                condicionesSimulacion[i][9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                condicionesSimulacion[i][10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ;
                                condicionesSimulacion[i][22] = "5"  ;
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                           //     System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                case 6:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                condicionesSimulacion[i][3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                condicionesSimulacion[i][4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                condicionesSimulacion[i][9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                condicionesSimulacion[i][10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ;
                                condicionesSimulacion[i][22] = "6"  ;
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                           //     System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                case 7:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                condicionesSimulacion[i][3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                condicionesSimulacion[i][4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                condicionesSimulacion[i][5] = Double.toString(rs.getDouble("potencia_contratada_p4")) ;   
                                condicionesSimulacion[i][6] = Double.toString(rs.getDouble("potencia_contratada_p5")) ;   
                                condicionesSimulacion[i][7] = Double.toString(rs.getDouble("potencia_contratada_p6")) ;   
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                condicionesSimulacion[i][9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                condicionesSimulacion[i][10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                condicionesSimulacion[i][11] = Double.toString(rs.getDouble("precio_potencia_p4")) ; 
                                condicionesSimulacion[i][12] = Double.toString(rs.getDouble("precio_potencia_p5")) ;   
                                condicionesSimulacion[i][13] = Double.toString(rs.getDouble("precio_potencia_p6")) ;    
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_VAR_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_VAR_p2")) ;     
                                condicionesSimulacion[i][16] = Double.toString(rs.getDouble("precio_energia_VAR_p3")) ;    
                                condicionesSimulacion[i][17] = Double.toString(rs.getDouble("precio_energia_VAR_p4")) ;   
                                condicionesSimulacion[i][18] = Double.toString(rs.getDouble("precio_energia_VAR_p5")) ;   
                                condicionesSimulacion[i][19] = Double.toString(rs.getDouble("precio_energia_VAR_p6")) ;       
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ;
                                condicionesSimulacion[i][22] = "7"  ;
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                          
                           //     System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                case 8:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");  
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "2"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                               
                                System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;  
                                case 9:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");  
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "2"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                               System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break; 
                               case 10:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                condicionesSimulacion[i][3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                condicionesSimulacion[i][4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                condicionesSimulacion[i][9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                condicionesSimulacion[i][10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ;
                                condicionesSimulacion[i][22] = "10"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ; 
                                break;   
                                case 11:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = "0";     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");  
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "11"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0" ;
                                System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;  
                                 case 12:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada")) ;
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia")) ;   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = "0";     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");  
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ; 
                                condicionesSimulacion[i][22] = "12"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = "0";
                                System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;  
                                 case 13:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                condicionesSimulacion[i][3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                condicionesSimulacion[i][4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                condicionesSimulacion[i][9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                condicionesSimulacion[i][10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                condicionesSimulacion[i][16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ;
                                condicionesSimulacion[i][22] = "13"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                break;   
                                case 14:
                                condicionesSimulacion[i][0] = rs.getString("fecha_inicio");  
                                condicionesSimulacion[i][1] = rs.getString("fecha_fin");     
                                condicionesSimulacion[i][2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                condicionesSimulacion[i][3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                condicionesSimulacion[i][4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                condicionesSimulacion[i][5] = Double.toString(rs.getDouble("potencia_contratada_p4")) ;   
                                condicionesSimulacion[i][6] = Double.toString(rs.getDouble("potencia_contratada_p5")) ;   
                                condicionesSimulacion[i][7] = Double.toString(rs.getDouble("potencia_contratada_p6")) ;   
                                condicionesSimulacion[i][8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                condicionesSimulacion[i][9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                condicionesSimulacion[i][10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                condicionesSimulacion[i][11] = Double.toString(rs.getDouble("precio_potencia_p4")) ; 
                                condicionesSimulacion[i][12] = Double.toString(rs.getDouble("precio_potencia_p5")) ;   
                                condicionesSimulacion[i][13] = Double.toString(rs.getDouble("precio_potencia_p6")) ;    
                                condicionesSimulacion[i][14] = Double.toString(rs.getDouble("precio_energia_VAR_p1")) ;   
                                condicionesSimulacion[i][15] = Double.toString(rs.getDouble("precio_energia_VAR_p2")) ;     
                                condicionesSimulacion[i][16] = Double.toString(rs.getDouble("precio_energia_VAR_p3")) ;    
                                condicionesSimulacion[i][17] = Double.toString(rs.getDouble("precio_energia_VAR_p4")) ;   
                                condicionesSimulacion[i][18] = Double.toString(rs.getDouble("precio_energia_VAR_p5")) ;   
                                condicionesSimulacion[i][19] = Double.toString(rs.getDouble("precio_energia_VAR_p6")) ;       
                                condicionesSimulacion[i][20] = rs.getString("observaciones");
                                condicionesSimulacion[i][21] = String.valueOf(rs.getInt("id"))  ;
                                condicionesSimulacion[i][22] = "14"  ;
                                condicionesSimulacion[i][23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                condicionesSimulacion[i][24] = Double.toString(rs.getDouble("alquiler")) ; 
                                condicionesSimulacion[i][25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                          
                                System.out.println("Añadiendo información punto :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                break;    
                                
                            }
                            // .................................................................... No está dado de alta .....
                        }  else {
                                 System.out.println("Añadiendo CONDICIONES NULAS :"+rs.getString("id_punto")+" con tarifa tipo:"+id_tarifa);
                                condicionesSimulacion[i][0] = "01-01-2015";  
                                condicionesSimulacion[i][1] = "31-12-2015";     
                                condicionesSimulacion[i][2] = "0"; 
                                condicionesSimulacion[i][3] = "0";    
                                condicionesSimulacion[i][4] = "0"; 
                                condicionesSimulacion[i][5] = "0";    
                                condicionesSimulacion[i][6] = "0";    
                                condicionesSimulacion[i][7] = "0";    
                                condicionesSimulacion[i][8] = "0";   
                                condicionesSimulacion[i][9] = "0";     
                                condicionesSimulacion[i][10] = "0";   
                                condicionesSimulacion[i][11] = "0";   
                                condicionesSimulacion[i][12] = "0";   
                                condicionesSimulacion[i][13] = "0";   
                                condicionesSimulacion[i][14] = "0";   
                                condicionesSimulacion[i][15] = "0";     
                                condicionesSimulacion[i][16] = "0";   
                                condicionesSimulacion[i][17] = "0";   
                                condicionesSimulacion[i][18] = "0";   
                                condicionesSimulacion[i][19] = "0";      
                                condicionesSimulacion[i][20] = "";  
                                condicionesSimulacion[i][21] = "0"  ; 
                                condicionesSimulacion[i][22] = "1"  ;
                                condicionesSimulacion[i][23] = "0" ; 
                                condicionesSimulacion[i][24] = "0" ; 
                                condicionesSimulacion[i][25] = "0" ;
                        }
                        }
			rs.close();
			estatuto.close();
			conex.desconectar();
                                

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                      

		}
            
                       
                return 1;
	}
 // ------------------------------------------------------------------------------------------------------------------
  	public int consultaHistoricoAhorrosPunto(DefaultTableModel model,int id_punto,int id_cliente) {
		 
             Conexion conex = new Conexion();
            int estadoInsert = 0;
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            String sqlStr ="SELECT fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual,coste_simulado,porcentaje";
                   sqlStr +=" FROM t_ahorros_historico WHERE id_punto="+id_punto+" AND id_cliente="+id_cliente+" ORDER BY id DESC";
       //      System.out.println("sqlstr ="+sqlStr); 
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[7];
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 0; i < 7; i++) 
					fila[i] = rs.getObject(i + 1);          // es para cargar los datos en filas a la tabla modelo
                                       
                                        try {
                                            fila[0]  = formatDateJava.format(rs.getDate("fecha")) ;      // fecha   
                                            
                                        } catch (NullPointerException nfe){
                                            fila[0]  = ""; 
                                        }
                                                                                                     
                                    contador++;
                                    model.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos del punto");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
        // -----------------------------------------------------------------------------------
    public int registrarFila(String sqlStr) {
		 
          
            int estadoInsert = 0 ;
            Conexion conex = new Conexion();

		try {
			Statement estatuto = conex.getConnection().createStatement();
			estatuto.executeUpdate(sqlStr);
			
			estatuto.close();
			conex.desconectar();
                       

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			estadoInsert = 2 ;
		}
                
                return estadoInsert;
	}
    // -----------------------------------------------------------------------------------
    public int ultimoIdentificador(String sqlStr) {
		 
            int id = 0 ;
            int estadoInsert = 0 ;
            Conexion conex = new Conexion();

		try {
			Statement estatuto = conex.getConnection().createStatement();
			
			ResultSet rs = estatuto.executeQuery(sqlStr);

                        
                        rs.next();
                        id = rs.getInt(1);

                        
                        
			estatuto.close();
                        conex.desconectar();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			estadoInsert = 1 ;
		}
                
               //  System.out.println("El ultimo identificador es id="+id);
                this.id = id ;
                
                return estadoInsert;
	}
    // ------------------------------------------------------------------------------------------------------------------
  	public int consultaHistoricoAhorrosCliente(DefaultTableModel model,int id_cliente) {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0;
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            String sqlStr ="SELECT id,fecha,nombre,cups,tarifa_actual,mercado,direccion,provincia,cif,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual,coste_simulado,porcentaje" ;
                   sqlStr+=" FROM v_lista_ahorros_punto WHERE ";
                   sqlStr +="id_cliente="+id_cliente+" ORDER BY id DESC";
 //           System.out.println("sqlstr ="+sqlStr); 
            
            
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[15];
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 0; i < 15; i++) 
					fila[i] = rs.getObject(i + 1);          // es para cargar los datos en filas a la tabla modelo
                                       
                                        fila[0] = rs.getInt(1);         //     System.out.println("id="+rs.getInt(1));
                                        try {
                                            fila[1]  = formatDateJava.format(rs.getDate("fecha")) ;             // fecha   
                                           
                                                    
                                        } catch (NullPointerException nfe){
                                            fila[1]  = ""; 
                                        }
                                                                                                     
                                    contador++;
                                    model.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" calculos de ahorro en lista historico detalle ");   
                        
                        this.nPuntosCalAhorroDetalle = contador ;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
            
            
                return 1;
		
	}
// ------------------------------------------------------------------------------------------------------------------
  	public int consultaResumenAhorrosCliente(DefaultTableModel model,int id_cliente,String sFecha,int respuesta) {
            int estadoInsert = 0; 
            double costeSim=0, pAhorroTotal=0, porcentajeAhorroSub=0,costeSimSubt=0,costeActSubt=0, costeActual=0;
            String sqlStr="";
            int id_punto =0, id_tipo=0, diasSubTotal=0 ;
            String fechaCal = "", sFecha1="", sFecha2="";
            
            int cups1=0, cups2=0, cntR=0, dias=0;
            double ahorroAcum=0 ;
            double ahorroSubTotal=0;
            int diasAcum=0, fExisten=0;
           
            
            Conexion conex = new Conexion();
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            if (respuesta == 0 ) {
            
            sqlStr="SELECT fecha,nombre,direccion,localidad,cups,tarifa_actual,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,porcentaje,id_punto,id_tipo_actual,id,coste_simulado,coste_actual ";
            sqlStr+=" FROM v_lista_ahorros_punto WHERE ";                   
            sqlStr+=" fecha LIKE '"+sFecha+"' AND "; 
            sqlStr +="id_cliente="+id_cliente+" GROUP BY id_punto order by id_punto ASC,id DESC";
           // sqlStr +="id_cliente="+id_cliente+" AND fUltimCalc=1 order by id_punto ASC";
            
             System.out.println("RESPUESTA = 0 ----> sqlstr ="+sqlStr);  
            } else {
             
            sqlStr="SELECT fecha,nombre,direccion,localidad,cups,tarifa_actual,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,porcentaje,id_punto,id_tipo_actual,coste_simulado,coste_actual ";
            sqlStr+=" FROM v_lista_ahorros_punto WHERE ";                   
            sqlStr+=" fecha LIKE '"+sFecha+"' AND "; 
            sqlStr +="id_cliente="+id_cliente+" order by id_punto ASC";    
            
             System.out.println("RESPUESTA = 1 o 2----> sqlstr ="+sqlStr);  
                
      //      System.out.println("sqlstr ="+sqlStr);     
            }
            
          //  System.out.println("sqlstr ="+sqlStr); 
            
            this.ahorroTotal =0 ;
            cntR = 0 ;
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;
                            fExisten = 1;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[12];
                                   
                                                                       
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 0; i < 10; i++) 
					fila[i+2] = rs.getObject(i+1);                      // es para cargar los datos en filas a la tabla modelo
                                        
                                        id_punto= rs.getInt("id_punto");
                                        
                                      
                                        
                                       
                                        id_tipo  = rs.getInt("id_tipo_actual");
                                       
                                        fila[0]  = formatDateJava.format(rs.getDate("fecha")) ;                     // fecha  
                                        fechaCal = dateToMySQLDate(formatDateJava.format(rs.getDate("fecha"))) ;
                                                                               
                                        this.ahorroTotal +=  rs.getDouble("ahorro_conseguido");              // Suma de ahorro para la consulta
                                        
                                        // costeSim += rs.getDouble("ahorro_conseguido")*(1+rs.getDouble("porcentaje"));   // Coste simulado
                                        costeSim    += rs.getDouble("coste_simulado");
                                        costeActual += rs.getDouble("coste_actual");
                                         // .............................................................. Obtenemos las fechas iniciales y finales
                                        fila[1] = BuscaFechaInicialCalculo(fechaCal,id_punto,id_tipo,id_cliente,conex); sFecha1 = BuscaFechaInicialCalculo(fechaCal,id_punto,id_tipo,id_cliente,conex);
                                        fila[2] = BuscaFechaFinalCalculo(fechaCal,id_punto,id_tipo,id_cliente,conex);   sFecha2 = BuscaFechaFinalCalculo(fechaCal,id_punto,id_tipo,id_cliente,conex);
                                             
                                        sFecha1.trim() ;
                                        sFecha2.trim() ;
                                                                                
                                        dias = diferenciaFechas(sFecha1, sFecha2 ,1); // System.out.println("-- DIAS DE CALCULO entre"+sFecha1+" y "+sFecha2+" son:"+dias);
                                      
                                  
                                        // ..............................................................
                                        if (respuesta == 2 && fExisten == 1) { 
                                            
                                            ahorroSubTotal = BuscarAhorroTotal(id_punto,fechaCal,conex,id_cliente) ;
                                            diasSubTotal   = BuscarDiasTotal(id_punto,fechaCal,conex,id_cliente) ;
                                            
                                            if (contador  == 0 ) { 
                                                   cups1 = rs.getInt("id_punto"); 
                                                   ahorroAcum = rs.getDouble("ahorro_conseguido");   
                                                   model.setValueAt(ahorroSubTotal, cntR, 10); 
                                                   model.setValueAt(diasSubTotal, cntR, 12); 
                                                   costeSimSubt = rs.getDouble("coste_simulado");      // Coste simulado
                                                   costeActSubt  = rs.getDouble("coste_actual");      // Coste actual
                                                //   System.out.println(cntR+" ---- Porcentaje Ahorro = "+rs.getDouble("porcentaje")+"    | AhorroAcum ="+ahorroAcum+" / costeSimSubt ="+costeSimSubt+ " ----> PorcentajeAhorroSub ="+porcentajeAhorroSub);
                                                                       
                                            } else {

                                               cups2 = rs.getInt("id_punto");   
                                               if ( cups1 == cups2 ) {  
                                                                    ahorroAcum += rs.getDouble("ahorro_conseguido") ;
                                                                    costeSimSubt += rs.getDouble("coste_simulado"); 
                                                                    costeActSubt += rs.getDouble("coste_actual");   
                                                                      
                                               }
                                               else 
                                               {
                                                                   //    System.out.println(contador+" -> El AHORRO para el punto id:"+cups1+" es de ="+ahorroAcum);
                                                                       model.setValueAt(ahorroAcum, cntR, 9); 
                                                                       model.setValueAt(ahorroSubTotal, cntR+1, 10); 
                                                                       model.setValueAt(diasSubTotal, cntR+1, 12); 
                                                                       porcentajeAhorroSub = 1 - (costeActSubt/costeSimSubt) ;
                                                                       
                                                                       model.setValueAt(porcentajeAhorroSub, cntR, 11);
                                                                    //   model.setValueAt(costeSimSubt, cntR, 11);
                                                                       
                                                                       
                                                                       ahorroAcum = rs.getDouble("ahorro_conseguido") ;
                                                                       costeSimSubt = rs.getDouble("coste_simulado");    // Coste simulado
                                                                       costeActSubt  = rs.getDouble("coste_actual");    
                                                                       cups1 = cups2 ;
                                                                       cntR ++ ;
                                               }

                                           }
                                        }   
                                         // ..............................................................
                                        
                                        contador++;
                                    
                                    if (respuesta == 0)  {
                                            model.addRow(fila);
                                           
                                            
                                    }
                                    
                                    model.setValueAt(dias, cntR, 8);
                                    
                            }
                            if (respuesta == 2 && fExisten == 1)  {   
                                System.out.println(contador+" -> El AHORRO para el punto id:"+cups1+" es de ="+ahorroAcum+" --- cntR="+cntR);
                                porcentajeAhorroSub = 1 - (costeActSubt/costeSimSubt) ;
                                model.setValueAt(ahorroAcum, cntR, 9);
                                model.setValueAt(ahorroSubTotal, cntR, 10);
                                model.setValueAt(diasSubTotal, cntR, 12);
                                model.setValueAt(porcentajeAhorroSub, cntR, 11);
                                                                      
                            }
               
                        } 
                      
                                                                   
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos del resumen de ahorros");      
                        
                        if (respuesta == 0) this.nPuntosCalAhorro = contador ;
                        if (respuesta == 1) this.pAhorro = 1-(costeActual / costeSim) ;
                        
                     //  
		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
               
                    
                return 1;
		
	}
    // ------------------------------------------------------------------------------------------------------------------
  	public int consultaHistoricoAhorrosPuntoDetalle(int id_punto,int id_cliente) {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0;
            int contador = 0;
            int i=0;
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            String sqlStr="SELECT * FROM t_ahorros_historico WHERE id_punto="+id_punto+" AND id_cliente="+id_cliente+" ORDER BY id DESC";
            System.out.println("sqlstr ="+sqlStr); 
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                        while (rs.next()) {

                            contador++;

                        }
                                                  
                        if (contador >0){
                           
                            rs.beforeFirst();
                            i = 0;
                            while (rs.next()) {
                                    lhistoricoAhorros[i][0] = Integer.toString(rs.getInt("id"));  
                                    lhistoricoAhorros[i][1] = Integer.toString(rs.getInt("id_cliente")); 
                                    lhistoricoAhorros[i][2] = Integer.toString(rs.getInt("id_punto")); 
                                     try {
                                    lhistoricoAhorros[i][3] = formatDateJava.format(rs.getDate("fecha")) ;    
                                     } catch (NullPointerException nfe){
                                    lhistoricoAhorros[i][3] = ""; 
                                     }
                                    lhistoricoAhorros[i][4] = Integer.toString(rs.getInt("dias_facturacion_optimizada"));  
                                    lhistoricoAhorros[i][5] = Double.toString(rs.getDouble("ahorro_conseguido")) ;
                                    lhistoricoAhorros[i][6] = Double.toString(rs.getDouble("ahorro_total")) ;    
                                    lhistoricoAhorros[i][7] = Double.toString(rs.getDouble("coste_actual")) ; 
                                    lhistoricoAhorros[i][8] = Double.toString(rs.getDouble("coste_simulado")) ; 
                                    lhistoricoAhorros[i][9] = Double.toString(rs.getDouble("porcentaje")) ; 
                                    lhistoricoAhorros[i][10] = Integer.toString(rs.getInt("id_factura"));    
                                    lhistoricoAhorros[i][11] = Integer.toString(rs.getInt("id_cond_actual"));   
                                    lhistoricoAhorros[i][12] = Integer.toString(rs.getInt("id_cond_sim"));    
                                    lhistoricoAhorros[i][13] = Integer.toString(rs.getInt("id_tipo_actual"));   
                                    lhistoricoAhorros[i][14] = Integer.toString(rs.getInt("id_tipo_sim"));     
                                                                                          
                                    i++;
                                   
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        
                        this.nCalculosPunto = i ;          //     System.out.println("Este punto tiene "+i+" registros de cálculo");
                              

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
    public int consultaDatosCondiciones(DefaultTableModel model,int idCA,int idTCA) throws SQLException {
        
        String sqlStr="" ;
        int estadoInsert=0 ;
        SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
        
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

        NumberFormat formatoNumero = NumberFormat.getNumberInstance();

           
        
                        // .......................................................... 
                        switch (idTCA) {
                                case 1:
                                    sqlStr="SELECT * FROM t_c20a WHERE id="+idCA+" ORDER BY id Desc";
                                 //   System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT * FROM t_c20dha WHERE  id="+idCA+" ORDER BY id Desc";
                                  //  System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT * FROM t_c21a WHERE  id="+idCA+" ORDER BY id Desc";
                                   // System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                   sqlStr="SELECT * FROM t_c21dha WHERE id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT * FROM t_c30a WHERE id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT * FROM t_c31a WHERE id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 7:
                                   
                                      sqlStr="SELECT * FROM t_c61a WHERE  id="+idCA+" ORDER BY id Desc";
                              
                                break;
                                case 8:
                                    sqlStr="SELECT * FROM t_c20dhaindx WHERE  id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT * FROM t_c21dhaindx WHERE  id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT * FROM t_c30aindx WHERE  id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT * FROM t_c20indx WHERE id="+idCA+" ORDER BY id Desc";
                                 //   System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 12:
                                    sqlStr="SELECT * FROM t_c21indx WHERE id="+idCA+" ORDER BY id Desc";
                                 //   System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 13:
                                    sqlStr="SELECT * FROM t_c31aindx WHERE  id="+idCA+" ORDER BY id Desc";
                                    System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                        // .......................................................... 
                            
                       try {
                           
			Conexion conex = new Conexion();
                        Statement estatuto = conex.getConnection().createStatement();			
			ResultSet rs = estatuto.executeQuery(sqlStr);
    
                        rs.next();
                        
                        // .......................................................... 
                        
                         Object[] fila = new Object[2];
                        
                        switch (idTCA) {
                                case 1:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;                      fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía (€/kWh)" ;        fila[1] = rs.getDouble("precio_energia");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    
                                    this.pe1             =  rs.getDouble("precio_energia");                                   
                                    this.pe2             =  0 ;
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  0 ;
                                    
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                break;
                                case 2:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);
                                     fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    
                                    
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  0 ;
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                break;
                                case 3:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;                      fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía (€/kWh)" ;        fila[1] = rs.getDouble("precio_energia");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);  
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia");                                   
                                    this.pe2             =  0;
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  0 ;
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                break;
                                case 4:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila); 
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  0 ;
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    break;
                                case 5:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P1" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P1 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P2 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P3 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P3(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);    
                                    fila[0] = "Descuento sobre consumo %" ;     fila[1] = rs.getDouble("descuento");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  rs.getDouble("precio_energia_p3");
                                    this.ppp             =  rs.getDouble("precio_potencia_p1"); 
                                    this.ppll            =  rs.getDouble("precio_potencia_p2"); 
                                    this.ppv             =  rs.getDouble("precio_potencia_p3"); 
                                    this.pep             =  0 ;
                                    this.pcp             = rs.getDouble("potencia_contratada_p1");
                                    this.pcll            = rs.getDouble("potencia_contratada_p2"); 
                                    this.pcv             = rs.getDouble("potencia_contratada_p3"); 
                                    this.descuento       = rs.getDouble("descuento"); 
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                    break;
                                case 6:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P1" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P1 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P2 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P3 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P3(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);    
                                    fila[0] = "Descuento sobre consumo %" ;     fila[1] = rs.getDouble("descuento");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  rs.getDouble("precio_energia_p3");
                                    this.ppp             =  rs.getDouble("precio_potencia_p1"); 
                                    this.ppll            =  rs.getDouble("precio_potencia_p2"); 
                                    this.ppv             =  rs.getDouble("precio_potencia_p3"); 
                                    this.pep             =  0 ;
                                    this.pcp             = rs.getDouble("potencia_contratada_p1");
                                    this.pcll            = rs.getDouble("potencia_contratada_p2"); 
                                    this.pcv             = rs.getDouble("potencia_contratada_p3"); 
                                    this.descuento       = rs.getDouble("descuento"); 
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                    break;
                                case 8:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía Peaje" ;          fila[1] = rs.getDouble("precio_energia_peaje");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);  
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  rs.getDouble("precio_energia_peaje");
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    break;
                                case 9:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía Peaje" ;          fila[1] = rs.getDouble("precio_energia_peaje");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);  
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  rs.getDouble("precio_energia_peaje");
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                    break;
                                case 10:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P1" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P1 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P2 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P3 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P3(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía Peaje(€/kWh)" ;    fila[1] = rs.getDouble("precio_energia_peaje");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);    
                                    fila[0] = "Descuento sobre consumo %" ;     fila[1] = rs.getDouble("descuento");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  rs.getDouble("precio_energia_p3");
                                    this.ppp             =  rs.getDouble("precio_potencia_p1"); 
                                    this.ppll            =  rs.getDouble("precio_potencia_p2"); 
                                    this.ppv             =  rs.getDouble("precio_potencia_p3"); 
                                    this.pep             =  rs.getDouble("precio_energia_peaje"); 
                                    this.pcp             =  rs.getDouble("potencia_contratada_p1");
                                    this.pcll            =  rs.getDouble("potencia_contratada_p2"); 
                                    this.pcv             =  rs.getDouble("potencia_contratada_p3"); 
                                    this.descuento       =  rs.getDouble("descuento"); 
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                    break;
                                case 11:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1"); 
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía Peaje" ;          fila[1] = rs.getDouble("precio_energia_peaje");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);   
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  0;
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  rs.getDouble("precio_energia_peaje");
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    break;
                                case 12:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1"); 
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía Peaje" ;          fila[1] = rs.getDouble("precio_energia_peaje");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);   
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  0;
                                    this.pe3             =  0 ;
                                    this.ppp             =  rs.getDouble("precio_potencia");
                                    this.ppll            =  0 ;
                                    this.ppv             =  0 ;
                                    this.pep             =  rs.getDouble("precio_energia_peaje");
                                    this.pcp             = rs.getDouble("potencia_contratada");
                                    this.pcll            = 0 ;
                                    this.pcv             = 0 ;
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    break;
                                    case 13:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "Comercializadora" ;              fila[1] = rs.getString("compañia");  
                                    model.addRow(fila);
                                    fila[0] = "Descripción" ;                   fila[1] = rs.getString("descripcion");  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P1" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Potencia Contratada P2" ;           fila[1] = formatoNumero.format(rs.getDouble("potencia_contratada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P1 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P2 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Potencia P3 (€/kW·dia)" ;    fila[1] = rs.getDouble("precio_potencia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P1(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p1");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P2(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p2");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía P3(€/kWh)" ;       fila[1] = rs.getDouble("precio_energia_p3");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Energía Peaje(€/kWh)" ;    fila[1] = rs.getDouble("precio_energia_peaje");  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Observaciones" ;                 fila[1] = rs.getString("observaciones");  
                                    model.addRow(fila);    
                                    fila[0] = "Descuento sobre consumo %" ;     fila[1] = rs.getDouble("descuento");  
                                    model.addRow(fila);
                                    fila[0] = "Precio Alquiler (€/dia)" ;       fila[1] = rs.getDouble("alquiler");  
                                    model.addRow(fila);
                                    
                                    this.pe1             =  rs.getDouble("precio_energia_p1");                                   
                                    this.pe2             =  rs.getDouble("precio_energia_p2");
                                    this.pe3             =  rs.getDouble("precio_energia_p3");
                                    this.ppp             =  rs.getDouble("precio_potencia_p1"); 
                                    this.ppll            =  rs.getDouble("precio_potencia_p2"); 
                                    this.ppv             =  rs.getDouble("precio_potencia_p3"); 
                                    this.pep             =  rs.getDouble("precio_energia_peaje"); 
                                    this.pcp             =  rs.getDouble("potencia_contratada_p1");
                                    this.pcll            =  rs.getDouble("potencia_contratada_p2"); 
                                    this.pcv             =  rs.getDouble("potencia_contratada_p3"); 
                                    this.descuento       =  rs.getDouble("descuento");
                                    this.pAlq            = rs.getDouble("alquiler");  
                                    
                                    break;
                        }
                        // .......................................................... 
                        
			estatuto.close();
			conex.desconectar();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                estadoInsert = 1 ;
                        }

                        return estadoInsert;

    }
 // -----------------------------------------------------------------------------------------------------------------------------
 public int consultaDatosFacturas(DefaultTableModel model,int idCA,int idTCA) throws SQLException {
        
        String sqlStr="" ;
        int estadoInsert=0 ;
        SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
        
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

        NumberFormat formatoNumero = NumberFormat.getNumberInstance();

        formatoNumero.setMaximumFractionDigits(2);
           
        
                        // .......................................................... 
                        switch (idTCA) {
                                case 1:
                                    sqlStr="SELECT * FROM t_f20a WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT * FROM t_f20dha WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT * FROM t_f21a WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT * FROM t_f21dha WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT * FROM t_f30a WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT * FROM t_f31a WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT * FROM t_f20dhaindx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT * FROM t_f21dhaindx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT * FROM t_f30aindx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT * FROM t_f20indx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 12:
                                    sqlStr="SELECT * FROM t_f21indx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 13:
                                    sqlStr="SELECT * FROM t_f31aindx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                        // .......................................................... 
                            
                       try {
                           
			Conexion conex = new Conexion();
                        Statement estatuto = conex.getConnection().createStatement();			
			ResultSet rs = estatuto.executeQuery(sqlStr);
    
                        rs.next();
                        
                        // .......................................................... 
                        
                        this.fecha_inicio= formatDateJava.format(rs.getDate("fecha_inicio")) ;
                        this.fecha_fin   = formatDateJava.format(rs.getDate("fecha_fin")) ;
                        
                        
                         Object[] fila = new Object[2];
                        
                        switch (idTCA) {
                                case 1:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila);
                                    fila[0] = "Recargos" ;                      fila[1] = formatoImporte.format(rs.getDouble("recargos"));  
                                    model.addRow(fila);
                                    fila[0] = "Alquiler contador" ;              fila[1] = formatoImporte.format(rs.getDouble("alquiler"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  0 ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  0 ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                    this.pF1            =  rs.getDouble("p1_potencia") ;
                                    this.TR             =  rs.getDouble("recargos");
                                break;
                                case 2:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila); 
                                    fila[0] = "Recargos" ;                      fila[1] = formatoImporte.format(rs.getDouble("recargos"));  
                                    model.addRow(fila);
                                    fila[0] = "Alquiler contador" ;              fila[1] = formatoImporte.format(rs.getDouble("alquiler"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  0 ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                    this.pF1            =  rs.getDouble("p1_potencia") ;
                                    this.TR             =  rs.getDouble("recargos");
                                    
                                break;
                                case 3:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila);  
                                    fila[0] = "Recargos" ;                      fila[1] = formatoImporte.format(rs.getDouble("recargos"));  
                                    model.addRow(fila);
                                    fila[0] = "Alquiler contador" ;             fila[1] = formatoImporte.format(rs.getDouble("alquiler"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia Simulada kWh" ;       fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  0 ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  0 ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                    this.pF1            =  rs.getDouble("p1_potencia") ;
                                    this.TR             =  rs.getDouble("recargos");
                                break;
                                case 4:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila);    
                                    fila[0] = "P1 Energia Simulada kWh" ;       fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p2_energia_simulada"));  
                                    model.addRow(fila);
                                   
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  0 ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                   
                                    
                                break;
                                case 5:
                                     fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p3_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia Reg. kW" ;           fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Potencia Reg. kW" ;           fila[1] = formatoNumero.format(rs.getDouble("p2_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Potencia Reg. kW" ;           fila[1] = formatoNumero.format(rs.getDouble("p3_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p3")); 
                                    model.addRow(fila);
                                    fila[0] = "P1 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p1_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p2_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p3_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Reactiva Facturada kVAR" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_facturada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Reactiva Facturada kVAR" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_facturada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Penalizacion Reactiva" ;         fila[1] = formatoNumero.format(rs.getDouble("penalizacion_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Alquiler" ;                      fila[1] = formatoImporte.format(rs.getDouble("alquiler"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila);     
                                    fila[0] = "Reactiva simulada Punta P1" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_simulada_p1"));  
                                    model.addRow(fila);    
                                    fila[0] = "Reactiva simulada Llano P2" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_simulada_p2"));  
                                    model.addRow(fila);    
                                    fila[0] = "P1 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p2_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p3_energia_simulada"));  
                                    model.addRow(fila);
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  rs.getDouble("p3_energia") ;
                                    this.p2             =  rs.getDouble("p2_potencia") ;
                                    this.p3             =  rs.getDouble("p3_potencia") ;
                                    this.ep             =  0 ;
                                    this.r1             =  rs.getDouble("p1_reactiva");
                                    this.r2             =  rs.getDouble("p2_reactiva");
                                    this.r3             =  rs.getDouble("p3_reactiva");
                                    this.TR             =  rs.getDouble("penalizacion_reactiva");
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    
                                       
                                    this.pF1            =  rs.getDouble("potencia_facturada_p1") ;
                                    this.pF2            =  rs.getDouble("potencia_facturada_p2") ;
                                    this.pF3            =  rs.getDouble("potencia_facturada_p3") ;
                                    
                                    this.rF1            =  rs.getDouble("reactiva_facturada_p1") ;
                                    this.rF2            =  rs.getDouble("reactiva_facturada_p2") ;
                                    
                                    this.rr1            =  rs.getDouble("reactiva_registrada_p1") ;
                                    this.rr2            =  rs.getDouble("reactiva_registrada_p2") ;
                                    this.rr2            =  rs.getDouble("reactiva_registrada_p3") ;
                                    
                                    this.rs1            =  rs.getDouble("reactiva_simulada_p1") ;
                                    this.rs2            =  rs.getDouble("reactiva_simulada_p2") ;
                                    
                                    this.e1s             =  rs.getDouble("p1_energia_simulada");
                                    this.e2s             =  rs.getDouble("p2_energia_simulada");
                                    this.e3s             =  rs.getDouble("p3_energia_simulada") ;
                                    
                                break;
                                case 8:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "Energia de peaje kWh" ;          fila[1] = formatoNumero.format(rs.getDouble("energia_peaje"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila); 
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  rs.getDouble("energia_peaje") ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                   
                                break;
                                case 9:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "Energia de peaje kWh" ;          fila[1] = formatoNumero.format(rs.getDouble("energia_peaje"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila); 
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  rs.getDouble("energia_peaje") ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                   
                                break;
                                case 10:
                                     fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p3_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "Energia Peaje kWh" ;             fila[1] = formatoNumero.format(rs.getDouble("energia_peaje"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p3_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p3")); 
                                    model.addRow(fila);
                                    fila[0] = "P1 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p1_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p2_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p3_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Reactiva Facturada kVAR" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_facturada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Reactiva Facturada kVAR" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_facturada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Penalizacion Reactiva" ;         fila[1] = formatoNumero.format(rs.getDouble("penalizacion_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Alquiler" ;                      fila[1] = formatoImporte.format(rs.getDouble("alquiler"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila);     
                                    fila[0] = "Reactiva simulada Punta P1" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_simulada_p1"));  
                                    model.addRow(fila);    
                                    fila[0] = "Reactiva simulada Llano P2" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_simulada_p2"));  
                                    model.addRow(fila);    
                                    fila[0] = "P1 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p2_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p3_energia_simulada"));  
                                    model.addRow(fila);
                                    
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  rs.getDouble("p3_energia") ;
                                    this.p2             =  rs.getDouble("p2_potencia") ;
                                    this.p3             =  rs.getDouble("p3_potencia") ;
                                    this.ep             =  rs.getDouble("energia_peaje") ;
                                    this.r1             =  rs.getDouble("p1_reactiva");
                                    this.r2             =  rs.getDouble("p2_reactiva");
                                    this.r3             =  rs.getDouble("p3_reactiva");
                                    this.TR             =  rs.getDouble("penalizacion_reactiva");
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    
                                    this.pF1            =  rs.getDouble("potencia_facturada_p1") ;
                                    this.pF2            =  rs.getDouble("potencia_facturada_p2") ;
                                    this.pF3            =  rs.getDouble("potencia_facturada_p3") ;
                                    
                                    this.rF1            =  rs.getDouble("reactiva_facturada_p1") ;
                                    this.rF2            =  rs.getDouble("reactiva_facturada_p2") ;
                                    
                                    this.rr1            =  rs.getDouble("reactiva_registrada_p1") ;
                                    this.rr2            =  rs.getDouble("reactiva_registrada_p2") ;
                                    this.rr2            =  rs.getDouble("reactiva_registrada_p3") ;
                                    
                                    this.rs1            =  rs.getDouble("reactiva_simulada_p1") ;
                                    this.rs2            =  rs.getDouble("reactiva_simulada_p2") ;
                                    
                                    this.e1s             =  rs.getDouble("p1_energia_simulada");
                                    this.e2s             =  rs.getDouble("p2_energia_simulada");
                                    this.e3s             =  rs.getDouble("p3_energia_simulada") ;
                                    
                                break;
                                case 11:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "Energia de peaje kWh" ;          fila[1] = formatoNumero.format(rs.getDouble("energia_peaje"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila); 
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  0 ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  rs.getDouble("energia_peaje") ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                break;
                                 case 12:
                                    fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "Energia de peaje kWh" ;          fila[1] = formatoNumero.format(rs.getDouble("energia_peaje"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila); 
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  0 ;
                                    this.e3             =  0 ;
                                    this.p2             =  0 ;
                                    this.p3             =  0 ;
                                    this.ep             =  rs.getDouble("energia_peaje") ;
                                    this.r1             =  0 ;
                                    this.r2             =  0 ;
                                    this.r3             =  0 ;
                                    this.TR             =  0 ;
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    this.rs1            = 0 ;
                                    this.rs2            = 0 ;
                                break;
                                case 13:
                                     fila[0] = "Id" ;                            fila[1] = rs.getInt("id");
                                    model.addRow(fila);
                                    fila[0] = "P1 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Energia kWh" ;                fila[1] = formatoNumero.format(rs.getDouble("p3_energia"));  
                                    model.addRow(fila);
                                    fila[0] = "Energia Peaje kWh" ;             fila[1] = formatoNumero.format(rs.getDouble("energia_peaje"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p1_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p2_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Potencia kW" ;                fila[1] = formatoNumero.format(rs.getDouble("p3_potencia"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Potencia Facturada kW" ;      fila[1] = formatoNumero.format(rs.getDouble("potencia_facturada_p3")); 
                                    model.addRow(fila);
                                    fila[0] = "P1 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p1_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p2_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Reactiva kVAR" ;              fila[1] = formatoNumero.format(rs.getDouble("p3_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "P1 Reactiva Facturada kVAR" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_facturada_p1"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Reactiva Facturada kVAR" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_facturada_p2"));  
                                    model.addRow(fila);
                                    fila[0] = "Penalizacion Reactiva" ;         fila[1] = formatoNumero.format(rs.getDouble("penalizacion_reactiva"));  
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Factura" ;              fila[1] = formatDateJava.format(rs.getDate("fecha_factura")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de Inicio" ;               fila[1] = formatDateJava.format(rs.getDate("fecha_inicio")) ;      // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Fecha de fin" ;                  fila[1] = formatDateJava.format(rs.getDate("fecha_fin")) ;          // fecha   
                                    model.addRow(fila);
                                    fila[0] = "Alquiler" ;                      fila[1] = formatoImporte.format(rs.getDouble("alquiler"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe antes de Impuestos" ;    fila[1] = formatoImporte.format(rs.getDouble("importe_AI"));  
                                    model.addRow(fila);
                                    fila[0] = "Importe despues de Impuestos" ;  fila[1] = formatoImporte.format(rs.getDouble("importe_DI"));  
                                    model.addRow(fila);     
                                    fila[0] = "Reactiva simulada Punta P1" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_simulada_p1"));  
                                    model.addRow(fila);    
                                    fila[0] = "Reactiva simulada Llano P2" ;    fila[1] = formatoNumero.format(rs.getDouble("reactiva_simulada_p2"));  
                                    model.addRow(fila);    
                                    fila[0] = "P1 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p1_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P2 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p2_energia_simulada"));  
                                    model.addRow(fila);
                                    fila[0] = "P3 Energia Simulada kWh" ;        fila[1] = formatoNumero.format(rs.getDouble("p3_energia_simulada"));  
                                    model.addRow(fila);
                                    
                                    this.fecha_inicio   =  formatDateJava.format(rs.getDate("fecha_inicio")) ; 
                                    this.fecha_fin      =  formatDateJava.format(rs.getDate("fecha_fin")) ;
                                    this.e1             =  rs.getDouble("p1_energia");
                                    this.p1             =  rs.getDouble("p1_potencia");
                                    this.e2             =  rs.getDouble("p2_energia") ;
                                    this.e3             =  rs.getDouble("p3_energia") ;
                                    this.p2             =  rs.getDouble("p2_potencia") ;
                                    this.p3             =  rs.getDouble("p3_potencia") ;
                                    this.ep             =  rs.getDouble("energia_peaje") ;
                                    this.r1             =  rs.getDouble("p1_reactiva");
                                    this.r2             =  rs.getDouble("p2_reactiva");
                                    this.r3             =  rs.getDouble("p3_reactiva");
                                    this.TR             =  rs.getDouble("penalizacion_reactiva");
                                    this.alquiler       =  rs.getDouble("alquiler") ;
                                    this.descuento      =  rs.getDouble("descuento") ;
                                    
                                    this.pF1            =  rs.getDouble("potencia_facturada_p1") ;
                                    this.pF2            =  rs.getDouble("potencia_facturada_p2") ;
                                    this.pF3            =  rs.getDouble("potencia_facturada_p3") ;
                                    
                                    this.rF1            =  rs.getDouble("reactiva_facturada_p1") ;
                                    this.rF2            =  rs.getDouble("reactiva_facturada_p2") ;
                                    
                                    this.rr1            =  rs.getDouble("reactiva_registrada_p1") ;
                                    this.rr2            =  rs.getDouble("reactiva_registrada_p2") ;
                                    this.rr2            =  rs.getDouble("reactiva_registrada_p3") ;
                                    
                                    this.rs1            =  rs.getDouble("reactiva_simulada_p1") ;
                                    this.rs2            =  rs.getDouble("reactiva_simulada_p2") ;
                                    
                                    this.e1s             =  rs.getDouble("p1_energia_simulada");
                                    this.e2s             =  rs.getDouble("p2_energia_simulada");
                                    this.e3s             =  rs.getDouble("p3_energia_simulada") ;
                                break;
                        }
                        // .......................................................... 
                        
			estatuto.close();
			conex.desconectar();
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                estadoInsert = 1 ;
                        }

                        return estadoInsert;

    }
 
    // -----------------------------------------------------------------------------------
    public int FechaultimoCalculo(int id_cliente) {
		 
      
       SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
       
        
        String sqlStr = "SELECT fecha FROM t_ahorros_historico WHERE id_cliente="+id_cliente+" ORDER BY ID DESC LIMIT 1"  ;
        
            int estadoInsert = 0 ;
            Conexion conex = new Conexion();

		try {
			Statement estatuto = conex.getConnection().createStatement();
			
			ResultSet rs = estatuto.executeQuery(sqlStr);

                        
                        rs.next();
                        try {
                            this.fechaUltimoCalculo = formatDateJava.format(rs.getDate("fecha"));
                        } catch (NullPointerException e) {
                            this.fechaUltimoCalculo = "01-01-1900";
                        }
                        
                        
			estatuto.close();
			conex.desconectar();
                       

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			estadoInsert = 2 ;
		}
                
                return estadoInsert;
	}
   // ------------------------------------------------------------------------------------------------------------------
  	public int consultaCalculoDetalles(DefaultTableModel model,int id_cliente,String sFecha) {
            int estadoInsert = 0; 
            double costeSim=0, pAhorroTotal=0;
            String sqlStr="";
            
            
            Conexion conex = new Conexion();
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            
            sqlStr="SELECT nombre,direccion,localidad,cups,tarifa_actual,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,id_tipo_sim FROM v_lista_ahorros_punto WHERE";         
            sqlStr+=" id_cliente="+id_cliente+" AND ";  
            sqlStr +=" fecha LIKE '"+sFecha+"'  order by id_punto asc" ; 
           
           
          
            
            this.ahorroTotal =0 ;
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                                   
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[35];
                                    // para llenar cada columna con lo datos almacenados
                                    
                                        fila[0]  = rs.getString("nombre") ;                 // nombre  
                                        fila[1]  = rs.getString("direccion") ;              // direccion  
                                        fila[2]  = rs.getString("localidad") ;  
                                        fila[3]  = rs.getString("cups") ;  
                                        fila[4]  = rs.getString("tarifa_actual") ;  
                                        fila[5]  = "" ;                                         // fecha inicial
                                        fila[6]  = "" ;                                         // fecha final
                                        fila[7]  = "" ;                                         // dias
                                        fila[8]  = rs.getInt("dias_facturacion_optimizada");
                                        fila[9]  = rs.getDouble("ahorro_conseguido");
                                        fila[10] = rs.getDouble("ahorro_total");
                                        fila[11]  = "" ;  
                                        fila[12]  = "" ;  
                                        fila[13]  = "" ;  
                                        fila[14]  = "" ;  
                                        fila[15]  = "" ;  
                                        fila[16]  = "" ;  
                                        fila[17]  = "" ;  
                                        fila[18]  = "" ;  
                                        fila[19]  = "" ;  
                                        fila[20]  = "" ;  
                                        fila[21]  = "" ;  
                                        fila[22]  = "" ;  
                                        fila[23]  = "" ;  
                                        fila[24]  = "" ;  
                                        fila[25]  = "" ;  
                                        fila[26]  = "" ;  
                                        fila[27]  = "" ;  
                                        fila[28]  = "" ;  
                                        fila[29]  = "" ;  
                                        fila[30]  = "" ;  
                                        fila[31]  = "" ;  
                                        fila[32]  = "" ;  
                                        fila[33]  = "" ;  
                                        fila[34]  = "" ; 
                         
                                        this.lhistoricoCalculos[contador][0] = rs.getInt("id_factura");         
                                        this.lhistoricoCalculos[contador][1] = rs.getInt("id_tipo_actual"); 
                                        
                                        this.lhistoricoCalculos[contador][2] = rs.getInt("id_cond_actual");         
                                        this.lhistoricoCalculos[contador][3] = rs.getInt("id_cond_sim"); 
                                        this.lhistoricoCalculos[contador][4] = rs.getInt("id_tipo_sim"); 
                                     
                                        
                                        
                                    contador++;
                                    model.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos del listado de cálculos superdetallado");      
                        
                        this.nCalculosDetalle = contador;
                        this.nCalculos        = contador;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        System.out.println("sqlstr ="+sqlStr); 
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
           
                return 1;
		
	}
 // -----------------------------------------------------------------------------------------------------------------------------
 public int consultaFechasFacturas() throws SQLException {
        
        String sqlStr="" ;
        int i,idTCA,idCA, estadoInsert=0 ;
        SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
        
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

        NumberFormat formatoNumero = NumberFormat.getNumberInstance();

        formatoNumero.setMaximumFractionDigits(2);
        
        try {
                           
			Conexion conex = new Conexion();
                        Statement estatuto = conex.getConnection().createStatement();	   
        
        System.out.println("TENEMOS QUE BUSCAR nCalculos="+this.nCalculos);
                        
        for (i=0; i < this.nCalculos; i++) {
        
            idTCA = this.lhistoricoCalculos[i][1];  //System.out.println("idTCA="+idTCA);
            idCA  = this.lhistoricoCalculos[i][0];
        
                        // .......................................................... 
                        switch (idTCA) {
                                case 1:
                                    sqlStr="SELECT * FROM t_f20a WHERE id="+idCA+" ORDER BY id Desc";
                     //               System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT * FROM t_f20dha WHERE  id="+idCA+" ORDER BY id Desc";
                      //              System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT * FROM t_f21a WHERE  id="+idCA+" ORDER BY id Desc";
                      //              System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT * FROM t_f21dha WHERE id="+idCA+" ORDER BY id Desc";
                      //              System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT * FROM t_f30a WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT * FROM t_f31a WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT * FROM t_f20dhaindx WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT * FROM t_f21dhaindx WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT * FROM  t_f30aindx WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT * FROM t_f20indx WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 12:
                                    sqlStr="SELECT * FROM t_f21indx WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 13:
                                    sqlStr="SELECT * FROM  t_f31aindx WHERE id="+idCA+" ORDER BY id Desc";
                       //             System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                        // .......................................................... 
                        
                 //       System.out.println("CONSULTO FECHAS PARA sqlStr="+sqlStr);
                      		
			ResultSet rs = estatuto.executeQuery(sqlStr);
    
                        rs.next();
                        
                        // .......................................................... 
                        
                        
                        lDatosCalculos[i][0] = formatDateJava.format(rs.getDate("fecha_inicio")) ;
                        lDatosCalculos[i][1] = formatDateJava.format(rs.getDate("fecha_fin")) ;
                                         
                        
                        switch (idTCA) {
                                case 1:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = "0" ;                                                // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = "0" ;                                                // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                    
                                    
                                break;
                                case 2:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = "0" ;                                                // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                break;
                                case 3:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = "0" ;                                                // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = "0" ;                                                // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                break;
                                case 4:
                                  
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = "0" ;                                                // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                break;
                                case 5:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = formatoNumero.format(rs.getDouble("p3_energia"));    // p3 energia
                                    lDatosCalculos[i][5] = "0" ;                                                // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("p2_potencia"));   // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("p3_potencia"));   // p3 potencia  
                                    
                                break;
                                case 6:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = formatoNumero.format(rs.getDouble("p3_energia"));    // p3 energia
                                    lDatosCalculos[i][5] = "0" ;                                                // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("p2_potencia"));   // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("p3_potencia"));   // p3 potencia  
                                    
                                break;
                                case 8:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = formatoNumero.format(rs.getDouble("energia_peaje")); // energia peaje                                              // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                break;
                                case 9:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = formatoNumero.format(rs.getDouble("energia_peaje")); // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                break;
                                case 10:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = formatoNumero.format(rs.getDouble("p3_energia"));    // p3 energia
                                    lDatosCalculos[i][5] = formatoNumero.format(rs.getDouble("energia_peaje")); // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("p2_potencia"));   // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("p3_potencia"));   // p3 potencia  
                                    
                                break;
                                case 11:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = "0" ;                                                // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = formatoNumero.format(rs.getDouble("energia_peaje")); // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                    
                                break;
                                case 12:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = "0" ;                                                // p2 energia
                                    lDatosCalculos[i][4] = "0" ;                                                // p3 energia
                                    lDatosCalculos[i][5] = formatoNumero.format(rs.getDouble("energia_peaje")); // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                // p3 potencia  
                                    
                                break;
                                 case 13:
                                   
                                    lDatosCalculos[i][2] = formatoNumero.format(rs.getDouble("p1_energia"));    // p1 energia
                                    lDatosCalculos[i][3] = formatoNumero.format(rs.getDouble("p2_energia"));    // p2 energia
                                    lDatosCalculos[i][4] = formatoNumero.format(rs.getDouble("p3_energia"));    // p3 energia
                                    lDatosCalculos[i][5] = formatoNumero.format(rs.getDouble("energia_peaje")); // P energia de peaje 
                                    lDatosCalculos[i][6] = "0" ;  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("p1_potencia"));   // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("p2_potencia"));   // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("p3_potencia"));   // p3 potencia  
                                    
                                break;
                                    
                                    
                        }
                       
                        
			
                       
        }
                         // .......................................................... 
                        estatuto.close();
			conex.desconectar();
         } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                estadoInsert = 1 ;
                        }
                        return estadoInsert;

    }
 // -----------------------------------------------------------------------------------------------------------------------------
 public int consultaDatosActualesDetalle() throws SQLException {
        
        String sqlStr="" ;
        int i,idTCA,idCA, estadoInsert=0 ;
        SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
        
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

        NumberFormat formatoNumero = NumberFormat.getNumberInstance();

        formatoNumero.setMaximumFractionDigits(6);
        
        try {
                           
			Conexion conex = new Conexion();
                        Statement estatuto = conex.getConnection().createStatement();	   
        
        System.out.println("TENEMOS QUE BUSCAR Detalles para nCalculos="+this.nCalculos);
                        
        for (i=0; i < this.nCalculos; i++) {
        
            idTCA = this.lhistoricoCalculos[i][1];  System.out.println("idTCA="+idTCA);
            idCA  = this.lhistoricoCalculos[i][2];
        
                        // .......................................................... 
                        switch (idTCA) {
                                case 1:
                                    sqlStr="SELECT * FROM t_c20a WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT * FROM t_c20dha WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT * FROM t_c21a WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT * FROM t_c21dha WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT * FROM t_c30a WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT * FROM t_c31a WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT * FROM t_c20dhaindx WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT * FROM t_c21dhaindx WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT * FROM t_c30aindx WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT * FROM t_c20indx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 12:
                                    sqlStr="SELECT * FROM t_c21indx WHERE id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 13:
                                    sqlStr="SELECT * FROM t_c31aindx WHERE  id="+idCA+" ORDER BY id Desc";
                         //           System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                        // .......................................................... 
                        
             //           System.out.println("CONSULTO DATOS PARA sqlStr="+sqlStr);
                      		
			ResultSet rs = estatuto.executeQuery(sqlStr);
    
                        rs.next();
                        
                        // .......................................................... 
                        
                       
                                         
                        
                        switch (idTCA) {
                                case 1:
                                   
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia"));       // p1 €/kWh
                                    lDatosCalculos[i][14] = "0" ;                                                       // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                       // p3 €/kWh
                                     lDatosCalculos[i][25] = "" ;    // energia de peaje €/kWh
                                    
                                break;
                                case 2:
                                   
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                       // p3 €/kWh
                                     lDatosCalculos[i][25] = "" ;    // energia de peaje €/kWh
                                break;
                                case 3:
                                  
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia"));       // p1 €/kWh
                                    lDatosCalculos[i][14] = "0" ;                                                       // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                       // p3 €/kWh
                                     lDatosCalculos[i][25] = "" ;    // energia de peaje €/kWh
                                break;
                                case 4:
                                   
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                     // p3 €/kWh
                                     lDatosCalculos[i][25] = "" ;    // energia de peaje €/kWh
                                break;
                                case 5:
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][15] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    lDatosCalculos[i][25] = "" ;    // energia de peaje €/kWh
                                break;
                                 case 6:
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][15] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    lDatosCalculos[i][25] = "" ;    // energia de peaje €/kWh
                                break;
                                case 8:
                                   
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje"));    // energia de peaje €/kWh
                                break;
                                case 9:
                                   
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje"));    // energia de peaje €/kWh
                                break;
                                case 10:
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][15] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje"));    // energia de peaje €/kWh
                                    
                                break;
                                case 11:
                                   
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = "0" ;                                                        // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                        // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje")); // energia de peaje €/kWh
                                    
                                break;
                                case 12:
                                   
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][8] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][9] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));      // p1 €/kWh
                                    lDatosCalculos[i][14] = "0" ;                                                        // p2 €/kWh
                                    lDatosCalculos[i][15] = "0" ;                                                        // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje")); // energia de peaje €/kWh
                                    
                                break;
                                 case 13:
                                    
                                    lDatosCalculos[i][7] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][8] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][9] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][10] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][11] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][12] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][13] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][14] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][15] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje"));    // energia de peaje €/kWh
                                    
                                break;
                        }
                       
        }
                         // .......................................................... 
                        estatuto.close();
			conex.desconectar();
         } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                estadoInsert = 1 ;
                        }
                        return estadoInsert;

    }
 // -----------------------------------------------------------------------------------------------------------------------------
 public int consultaDatosSimulacionDetalle() throws SQLException {
        
        String sqlStr="" ;
        int i,idTCA,idCA, estadoInsert=0 ;
        SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
        
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

        NumberFormat formatoNumero = NumberFormat.getNumberInstance();

        formatoNumero.setMaximumFractionDigits(6);
        
        try {
                           
			Conexion conex = new Conexion();
                        Statement estatuto = conex.getConnection().createStatement();	   
        
        System.out.println("TENEMOS QUE BUSCAR Detalles para nCalculos Simulacion="+this.nCalculos);
                        
        for (i=0; i < this.nCalculos; i++) {
        
            idTCA = this.lhistoricoCalculos[i][4];   System.out.println("idTCA="+idTCA);
            idCA  = this.lhistoricoCalculos[i][3];   System.out.println("idCA="+idCA);
        
                        // .......................................................... 
                        switch (idTCA) {
                                case 1:
                                    sqlStr="SELECT * FROM t_c20a WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT * FROM t_c20dha WHERE  id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT * FROM t_c21a WHERE  id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT * FROM t_c21dha WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT * FROM t_c30a WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT * FROM t_c31a WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT * FROM t_c20dhaindx WHERE  id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT * FROM t_c21dhaindx WHERE  id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT * FROM t_c30aindx WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT * FROM t_c20indx WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 12:
                                    sqlStr="SELECT * FROM t_c21indx WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 13:
                                    sqlStr="SELECT * FROM t_c31aindx WHERE id="+idCA+" ORDER BY id Desc";
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                        // .......................................................... 
                        
                   //     System.out.println("CONSULTO FECHAS PARA sqlStr="+sqlStr);
                      		
			ResultSet rs = estatuto.executeQuery(sqlStr);
    
                        rs.next();
                        
                        // .......................................................... 
                        
                       
                                         
                        
                        switch (idTCA) {
                                case 1:
                                   
                                    
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia"));       // p1 €/kWh
                                    lDatosCalculos[i][23] = "0" ;                                                       // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                   
                                break;
                                case 2:
                                   
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                     lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                   
                                    break;
                                case 3:
                                  
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia"));       // p1 €/kWh
                                    lDatosCalculos[i][23] = "0" ;                                                       // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                    break;
                                case 4:
                                   
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                       // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                      // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                    break;
                                case 5:
                                    
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][17] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][18] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][24] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                break;
                                case 6:
                                    
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][17] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][18] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][24] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                break;
                                case 8:
                                   
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                    break;
                                case 9:
                                   
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));    // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = "0" ;                                                       // Energia de peaje
                                    break;
                                case 10:
                                    
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][17] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][18] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][24] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje"));  // energia de peaje €/kWh
                                    
                                break;
                                case 11:
                                   
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));       // p1 €/kWh
                                    lDatosCalculos[i][23] = "0" ;                                                       // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje")); // energia de peaje €/kWh
                                    
                                break;
                                case 12:
                                   
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada"));   // p1 potencia  
                                    lDatosCalculos[i][17] = "0" ;                                                        // p2 potencia  
                                    lDatosCalculos[i][18] = "0" ;                                                        // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia"));      // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = "0" ;                                                       // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = "0" ;                                                        // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));       // p1 €/kWh
                                    lDatosCalculos[i][23] = "0" ;                                                       // p2 €/kWh
                                    lDatosCalculos[i][24] = "0" ;                                                       // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje")); // energia de peaje €/kWh
                                    
                                break;
                                case 13:
                                    
                                    lDatosCalculos[i][16] = formatoNumero.format(rs.getDouble("potencia_contratada_p1")); // p1 potencia  
                                    lDatosCalculos[i][17] = formatoNumero.format(rs.getDouble("potencia_contratada_p2")); // p2 potencia  
                                    lDatosCalculos[i][18] = formatoNumero.format(rs.getDouble("potencia_contratada_p3")); // p3 potencia  
                                    
                                    lDatosCalculos[i][19] = formatoNumero.format(rs.getDouble("precio_potencia_p1"));    // p1 €/kW·dia   
                                    lDatosCalculos[i][20] = formatoNumero.format(rs.getDouble("precio_potencia_p2"));    // p2 €/kW·dia   
                                    lDatosCalculos[i][21] = formatoNumero.format(rs.getDouble("precio_potencia_p3"));    // p3 €/kW·dia   
                                   
                                    lDatosCalculos[i][22] = formatoNumero.format(rs.getDouble("precio_energia_p1"));     // p1 €/kWh
                                    lDatosCalculos[i][23] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p2 €/kWh
                                    lDatosCalculos[i][24] = formatoNumero.format(rs.getDouble("precio_energia_p2"));     // p3 €/kWh
                                    lDatosCalculos[i][25] = formatoNumero.format(rs.getDouble("precio_energia_peaje"));  // energia de peaje €/kWh
                                    
                                break;
                        }
                       
        }
                         // .......................................................... 
                        estatuto.close();
			conex.desconectar();
         } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                estadoInsert = 1 ;
                        }
                        return estadoInsert;

    }
 // ----------------------------------------------------------------------------------------------------------------------------------------
 String  BuscaFechaInicialCalculo(String fechaCal,int id_punto, int id_tipo,int id_cliente,Conexion conex ) {
     String fechaIni="";
     String sqlStr="" ; 
     SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
                    // .......................................................... 
                     switch (id_tipo) {
                                case 1:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f20a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f20dha WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f21a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f21dha WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f30a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f31a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f20dhaindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f21dhaindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                 case 10:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f30aindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f20indx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 12:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f21indx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 13:
                                    sqlStr="SELECT min(fecha_inicio) as fechaIni FROM v_fechas_calculo_f31aindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                      // .......................................................... 
                     
 // System.out.println("sqlstr ="+sqlStr);    
                        try {
                                                       
                                Statement estatuto = conex.getConnection().createStatement();

                                ResultSet rs = estatuto.executeQuery(sqlStr);

                                rs.next();
                                fechaIni = formatDateJava.format(rs.getDate("fechaIni"));        
                                estatuto.close();
                                

                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                
                        }
     
     
     
     
     return fechaIni;
 }
 // ----------------------------------------------------------------------------------------------------------------------------------------
 String  BuscaFechaFinalCalculo(String fechaCal,int id_punto, int id_tipo,int id_cliente,Conexion conex ) {
     String fechaMax="";
     String sqlStr="" ; 
     SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
                    // .......................................................... 
                     switch (id_tipo) {
                                case 1:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f20a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 2:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f20dha WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 3:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f21a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 4:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f21dha WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 5:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f30a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 6:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f31a WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 8:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f20dhaindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 9:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f21dhaindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                        //            System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 10:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f30aindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                                case 11:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f20indx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 12:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f21indx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                              //      System.out.println("sqlstr ="+sqlStr);
                                break;
                                case 13:
                                    sqlStr="SELECT max(fecha_fin) as fechaMax FROM v_fechas_calculo_f31aindx WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_tipo_actual="+id_tipo+" AND id_cliente="+id_cliente;
                               //     System.out.println("sqlstr ="+sqlStr);    
                                break;
                        }
                      // .......................................................... 
     //    System.out.println("sqlstr ="+sqlStr);                  

                        try {
                                Statement estatuto = conex.getConnection().createStatement();

                                ResultSet rs = estatuto.executeQuery(sqlStr);

                                rs.next();
                                fechaMax = formatDateJava.format(rs.getDate("fechaMax"));        
                                estatuto.close();
                                  
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                
                        }
     
     
     
     
     return fechaMax;
 }
 
 
  // ------------------------------------------------------------------------------------------------------------------
  	public int consultaListaAlertasClientes(DefaultTableModel model2) {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0;
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            String sqlStr  ="SELECT idT_Clientes,idd,alias,nombre,cups,tarifa_actual,fecha_venc_servicio,fecha_cambio_comercializadora,fecha_cambio_potencia,fecha_cambio_comerc_gas,fecha_instala_bateria,fActiva" ;
                   sqlStr +=" FROM v_lista_alertas_servicios_puntos_clientes ";
                   sqlStr +=" ORDER BY alias,nombre ";
             System.out.println("sqlstr ="+sqlStr); 
            
            
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[15];
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 0; i < 5; i++) 
					fila[i] = rs.getObject(i + 1);              // es para cargar los datos en filas a la tabla modelo
                                       
                                      //  fila[0] = rs.getInt(1);                     //     System.out.println("id="+rs.getInt(1));
                                        try {
                                            fila[6]  = formatDateJava.format(rs.getDate("fecha_venc_servicio")) ;             // fecha   
                                           
                                                    
                                        } catch (NullPointerException nfe){
                                            fila[6]  = "NO definida"; 
                                        }
                                         try {
                                            fila[7]  = formatDateJava.format(rs.getDate("fecha_cambio_comercializadora")) ;             // fecha   
                                           
                                                    
                                        } catch (NullPointerException nfe){
                                            fila[7]  = "NO definida"; 
                                        }
                                        try {
                                            fila[8]  = formatDateJava.format(rs.getDate("fecha_cambio_potencia")) ;             // fecha   
                                           
                                                    
                                        } catch (NullPointerException nfe){
                                            fila[8]  = "NO definida"; 
                                        }
                                        try {
                                            fila[9]  = formatDateJava.format(rs.getDate("fecha_cambio_comerc_gas")) ;             // fecha   
                                           
                                                    
                                        } catch (NullPointerException nfe){
                                            fila[9]  = "NO definida"; 
                                        }
                                         try {
                                            fila[10]  = formatDateJava.format(rs.getDate("fecha_instala_bateria")) ;             // fecha   
                                           
                                                    
                                        } catch (NullPointerException nfe){
                                            fila[10]  = "NO definida"; 
                                        }
                                        
                                        if (rs.getInt("fActiva")==1) {
                                            fila[11]  = "ACTIVA";
                                        } else {
                                            fila[11]  = "INACTIVA";
                                        }
                                         
                                    contador++;
                                    model2.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" alertas de fin de servicios ");   
                        
                        this.nPuntosAlertaFinServicio = contador ;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
            
            
                return 1;
		
	}
 // ------------------------------------------------------------------------------------------------------------------------
  public String dateToMySQLDate(String fecha)
    {
        String df,y,m,d;
        
        fecha=fecha.trim();
        
        d = fecha.substring(0, 2) ; // System.out.println("dia ="+d);
        m = fecha.substring(3,5) ;  // System.out.println("mes ="+m);
        y = fecha.substring(6,10) ;  // System.out.println("año ="+y);
        
        df = y+"-"+m+"-"+d+ " 00:00:00";
       
        return df;
        
       
    }
  // ------------------------------------------------------------------------------------------------------------------------
 public int diferenciaFechas(String fec1, String fec2,int valor){
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    int retorno=0;
    java.util.Date date1 = null;
    java.util.Date date2 = null;
    try
    {
        Calendar cal1 = null;
        date1=df.parse(fec1);
        cal1=Calendar.getInstance();

        Calendar cal2 = null;
        date2=df.parse(fec2);
        cal2=Calendar.getInstance();

        // different date might have different offset
        cal1.setTime(date1);
        long ldate1 = date1.getTime() + cal1.get(Calendar.ZONE_OFFSET) + cal1.get(Calendar.DST_OFFSET);

        cal2.setTime(date2);
        long ldate2 = date2.getTime() + cal2.get(Calendar.ZONE_OFFSET) + cal2.get(Calendar.DST_OFFSET);

        // Use integer calculation, truncate the decimals
        int hr1 = (int)(ldate1/3600000); //60*60*1000
        int hr2 = (int)(ldate2/3600000);

        int days1 = (int)hr1/24;
        int days2 = (int)hr2/24;

        int dateDiff = days2 - days1;
        int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);
        int monthDiff = yearDiff * 12 + cal2.get(Calendar.MONTH) - cal1.get(Calendar.MONTH);

        if(valor==1) {
          //  if (dateDiff<0) dateDiff=dateDiff*(-1);
            retorno=dateDiff;
            }else if(valor==2){
            if (monthDiff<0) monthDiff=monthDiff*(-1);
            retorno=monthDiff;
        }else if(valor==3){
                if (yearDiff<0) yearDiff=yearDiff*(-1);
                retorno=yearDiff;
        }
    }
    catch (ParseException pe)
    {
        pe.printStackTrace();
    }
    return retorno;
} 
 // ------------------------------------------------------------------------------------------------------------------------
  public double BuscarAhorroTotal(int id_punto,String fechaCal,Conexion conex, int id_cliente) {
     double ahorro=0;
     
     String  sqlStr="SELECT ahorro_total FROM t_ahorros_historico WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_cliente="+id_cliente+" ORDER BY id DESC";
                        
      try {
                                Statement estatuto = conex.getConnection().createStatement();

                                ResultSet rs = estatuto.executeQuery(sqlStr);

                                rs.next();
                                ahorro = rs.getDouble("ahorro_total");  
                                
                                estatuto.close();
                                  
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                
                        }
  //   System.out.println(sqlStr+" --> "+ahorro)    ;   
     return ahorro; 
  }
  // ------------------------------------------------------------------------------------------------------------------------
  // ------------------------------------------------------------------------------------------------------------------------
  public int BuscarDiasTotal(int id_punto,String fechaCal,Conexion conex, int id_cliente) {
     int diasTot=0;
     
     String  sqlStr="SELECT dias_facturacion_optimizada FROM t_ahorros_historico WHERE id_punto="+id_punto+" AND fecha LIKE '"+fechaCal+"' AND id_cliente="+id_cliente+" ORDER BY id DESC";
                        
      try {
                                Statement estatuto = conex.getConnection().createStatement();

                                ResultSet rs = estatuto.executeQuery(sqlStr);

                                rs.next();
                                diasTot = rs.getInt("dias_facturacion_optimizada");  
                                
                                estatuto.close();
                                  
                        } catch (SQLException e) {
                                System.out.println(e.getMessage());
                                
                        }
 //    System.out.println(sqlStr+" --> "+diasTot)    ;   
     return diasTot; 
  }
  // ------------------------------------------------------------------------------------------------------------------------
  
  // ------------------------------------------------------------------------------------------------------------------
  	public int consultaTablaPreciosOmie(DefaultTableModel model,int mes,int year) {
		 
             Conexion conex = new Conexion();
            int estadoInsert = 0;
            
            // .......................................................................
     
            NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

            formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

            NumberFormat formatoNumero = NumberFormat.getNumberInstance();

            formatoNumero.setMaximumFractionDigits(2);
            
            // .......................................................................
            
            
            
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            String sqlStr ="SELECT dia,h01,h02,h03,h04,h05,h06,h07,h08,h09,h10,h11,h12,h13,h14,h15,h16,h17,h18,h19,h20,h21,h22,h23,h24";
                   sqlStr +=" FROM t_datos_mercado_omie WHERE mes="+mes+" AND year="+year+" ORDER BY dia ASC";
       //      System.out.println("sqlstr ="+sqlStr); 
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[25];
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 0; i <= 24; i++) 
					fila[i] = redondear(rs.getDouble(i + 1),2);          // es para cargar los datos en filas a la tabla modelo
                                       
                                       
                                                                                                     
                                    contador++;
                                    model.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos del OMIE");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
        // -----------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------
  	public int consultaTablaEnergia(DefaultTableModel model,int mes,int year,int id_punto) {
		 
             Conexion conex = new Conexion();
            int estadoInsert = 0;
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            
            String sqlStr ="SELECT dia,eh01,eh02,eh03,eh04,eh05,eh06,eh07,eh08,eh09,eh10,eh11,eh12,eh13,eh14,eh15,eh16,eh17,eh18,eh19,eh20,eh21,eh22,eh23,eh24";
                   sqlStr +=" FROM t_datos_consumos_hr WHERE mes="+mes+" AND year="+year+" AND id_punto="+id_punto+" ORDER BY dia ASC";
       //      System.out.println("sqlstr ="+sqlStr); 
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[25];
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 0; i <= 24; i++) 
					fila[i] = redondear(rs.getDouble(i + 1),2);          // es para cargar los datos en filas a la tabla modelo
                                       
                                       
                                                                                                     
                                    contador++;
                                    model.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos Energia");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
        // -----------------------------------------------------------------------------------
         // ------------------------------------------------------------------------------------------------------------------
  	public int consultaTablaPotencia(DefaultTableModel model,int id_cliente,int id_punto,String desde,String hasta) {
		 
             Conexion conex = new Conexion();
            int estadoInsert = 0;
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            
            String sqlStr ="SELECT fecha,consumo,tipo_dia";
                   sqlStr +=" FROM t_datos_potencia_6PQ WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" ";
                   sqlStr +="AND fecha >='"+desde+" 00:00:00' AND fecha <='"+hasta+" 23:59:59' ORDER BY id ASC LIMIT 3000" ;
                         
             System.out.println("sqlstr ="+sqlStr); 
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                   Object[] fila = new Object[3];
                                    // para llenar cada columna con lo datos almacenados
                                    for (int i = 1; i < 3; i++) 
					fila[i] = rs.getObject(i + 1);          // es para cargar los datos en filas a la tabla modelo
                                       
                              
                                    try {
                                        fila[0] = formatDateJava.format(rs.getObject("fecha"));
                                    } catch (NullPointerException e) {
                                        fila[0] = "01-01-2000";
                                    } 
                                    
                                                                                                     
                                    contador++;
                                    model.addRow(fila);
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos POTENCIA");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
        // -----------------------------------------------------------------------------------
        
        // ------------------------------------------------------------------------------------------------------------------
  	public int consultaDatosPuntosSimulacion(DefaultTableModel model,int id_cliente,int id_punto) {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0;
            SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            
            String sqlStr ="SELECT nombre,cups,cif,tarifa_actual,fBateriaC,TrfP,TrfS,TrfMax,fMedida,batDesde,batHasta,cosfiP1,cosfiP2,batDescripcion,notas,id_tarifa_actual";
                   sqlStr +=" FROM t_datos_puntos_suministro WHERE id_cliente="+id_cliente+" AND idd="+id_punto+" ";
                 
                         
             System.out.println("sqlstr ="+sqlStr); 
            
            try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                                                
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            Object[] fila = new Object[2];
                            rs.next(); 
                                   
                                         // para llenar cada columna con lo datos almacenados
                                        fila[0] = "ID PUNTO";           fila[1] = id_punto   ;                      model.addRow(fila);   
                                        fila[0] = "NOMBRE";             fila[1] = rs.getObject("nombre");           model.addRow(fila);   
                                        fila[0] = "CUPS  ";             fila[1] = rs.getObject("cups");             model.addRow(fila);   
                                        fila[0] = "CIF   ";             fila[1] = rs.getObject("cif");              model.addRow(fila);
                                        fila[0] = "TARIFA ACTUAL";      fila[1] = rs.getObject("tarifa_actual");    model.addRow(fila);
                                        fila[0] = "ID TARIFA ACTUAL";   fila[1] = rs.getObject("id_tarifa_actual"); model.addRow(fila);
                                        fila[0] = "NOTAS   ";           fila[1] = rs.getObject("notas");            model.addRow(fila);
                                        
                                        
                                        System.out.println("Cargando datos de nombre:"+rs.getObject("nombre"));
                                        
                                        if (rs.getInt("fBateriaC") == 1) {
                                            
                                            fila[0] = "BATERIA DE CONDENSADORES";       fila[1] = "SI";        model.addRow(fila);
                                           
                                            try {
                                                
                                                fila[0] = "INSTALADA DESDE";       fila[1] = formatDateJava.format(rs.getObject("batDesde"));        model.addRow(fila);
                                                fila[0] = "SERVICIO HASTA ";       fila[1] = formatDateJava.format(rs.getObject("batHasta"));        model.addRow(fila);
                                        
                                            } catch (NullPointerException e) {
                                                fila[0] = "---------"; model.addRow(fila);
                                            }
                                            
                                            fila[0] = "Coseno Fi medio P1   ";       fila[1] = rs.getObject("cosfiP1");        model.addRow(fila);
                                            fila[0] = "Coseno Fi medio P2   ";       fila[1] = rs.getObject("cosfiP2");        model.addRow(fila);
                                        
                                            fila[0] = "DESCRIPCIÓN BATERIA  ";       fila[1] = rs.getObject("batDescripcion"); model.addRow(fila);
                                        }
                                    
                                                                                                     
                                    contador++;
                                    
                        }
                        
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+contador+" datos DEL PUNTO DE SUMINISTRO");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
   // ------------------------------------------------------------------------------------------------------------------------
   public double redondear( double numero, int decimales ) {
    return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
  }
   // ------------------------------------------------------------------------------------------------------------------        
  	public int consultaClientesSimulacion() {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0 ;
          
            String sqlStr="SELECT DISTINCT id_cliente,nombre FROM v_clientes_simulacion  ORDER BY nombre asc";
                try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {

                            contador++;

                        }
                        
                        this.nClienteSimulacion = contador;
                        
                        
                        if (contador >0){
                           
                            rs.beforeFirst();
                            contador = 0;
                            while (rs.next()) {
                                    clientesSimulacion[contador] = rs.getString("nombre");    
                                    contador++;
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+nClienteSimulacion+" Clientes con Simulacion activa de facturas");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}

// ------------------------------------------------------------------------------------------------------------------
     // ------------------------------------------------------------------------------------------------------------------        
  	public int consultaPuntosSimulacion() {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0 ;
            int contador1=0, contador2=0, idcActual=0, idc=0;
          
            String sqlStr="SELECT DISTINCT nombre,nombre_punto,id_punto,id_cliente FROM v_clientes_simulacion  ORDER BY nombre asc";
                try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {
                            
                            if (contador == 0) idc = rs.getInt("id_cliente") ;                      // guardamos el primer id
                            contador++;

                        }
                        
                        this.nPuntosSimulacion = contador;
                        
                        
                        if (contador >0){
                        
                            
                            
                            rs.beforeFirst();
                            contador = 0;
                            contador1= 0;
                            contador2= 0;
                                                       
                            while (rs.next()) {
                                
                                    idcActual = rs.getInt("id_cliente") ;
                                    if ( idcActual != idc) {                    // Control de cambio de cliente
                                        
                                        idc = idcActual   ;
                                        contador1 ++ ;
                                        contador2 = 0 ;
                                        
                                    }  
                                
                                    simulacionPuntos[contador1][contador2][0] = String.valueOf(rs.getInt("id_punto"));   
                                    simulacionPuntos[contador1][contador2][1] = "10";
                                    simulacionPuntos[contador1][contador2][2] = rs.getString("nombre_punto");
                                    
                                    System.out.println("contador1, contador2 ="+contador1+" , "+contador2+" --->"+ simulacionPuntos[contador1][contador2][2]);
                                    
                                    contador++;
                                    contador2++ ;
                                    
                                    
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println("He cargado "+nPuntosSimulacion+" Puntos de simulación con Simulacion activa de facturas");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return 1;
		
	}
        // ------------------------------------------------------------------------------------------------------------------        
  	public void consultaDatosCondicionesSimulacion(int id_cliente,int id_punto,int id_tarifa_actual) {
		 
            Conexion conex = new Conexion();
            int estadoInsert = 0 ;
            int contador1=0, contador2=0, idcActual=0, idc=0;
            String sqlStr="";
            int id_estado = 2 ;
            
            switch  (id_tarifa_actual) {
                case 5:
                      sqlStr="SELECT * FROM t_c30a WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc"; 
                break;
                case 6:
                      sqlStr="SELECT * FROM t_c31a WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc"; 
                break;
                
                case 7:
                      sqlStr="SELECT * FROM t_c61a WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc"; 
                break;
                case 10:
                      sqlStr="SELECT * FROM t_c30aindx WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc"; 
                break;
                case 13:
                      sqlStr="SELECT * FROM t_c31aindx WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc"; 
                break;
                case 14:
                     sqlStr="SELECT * FROM t_c61aindx WHERE id_cliente="+id_cliente+" AND id_punto="+id_punto+" AND id_estado="+id_estado+" ORDER BY id Desc";                              
                break;                               
                
            }
          
            
                try {
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery(sqlStr);

                         int contador = 0;

                        while (rs.next()) {
                            
                            contador++;

                        }
                                              
                        
                        if (contador >0){
                        
                            
                            
                            rs.beforeFirst();
                                                     
                                                       
                            while (rs.next()) {
                                
                                switch (id_tarifa_actual) { 
                                  case 5:
                                  
                                    this.condicionesContratoSimulacion[2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                    this.condicionesContratoSimulacion[3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                    this.condicionesContratoSimulacion[4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                    this.condicionesContratoSimulacion[5] = "0" ;   
                                    this.condicionesContratoSimulacion[6] = "0" ;   
                                    this.condicionesContratoSimulacion[7] = "0" ;   
                                    this.condicionesContratoSimulacion[8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                    this.condicionesContratoSimulacion[9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                    this.condicionesContratoSimulacion[10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                    this.condicionesContratoSimulacion[11] = "0" ; 
                                    this.condicionesContratoSimulacion[12] = "0" ;   
                                    this.condicionesContratoSimulacion[13] = "0" ;    
                                    this.condicionesContratoSimulacion[14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                    this.condicionesContratoSimulacion[15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                    this.condicionesContratoSimulacion[16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                    this.condicionesContratoSimulacion[17] = "0" ;   
                                    this.condicionesContratoSimulacion[18] = "0" ;   
                                    this.condicionesContratoSimulacion[19] = "0" ;       
                                    this.condicionesContratoSimulacion[20] = rs.getString("observaciones");
                                    this.condicionesContratoSimulacion[21] = String.valueOf(rs.getInt("id"))  ;
                                    this.condicionesContratoSimulacion[22] = "5"  ;
                                    this.condicionesContratoSimulacion[23] = "0" ; 
                                    this.condicionesContratoSimulacion[24] = Double.toString(rs.getDouble("alquiler")) ; 
                                    this.condicionesContratoSimulacion[25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                break;    
                                    case 6:
                                  
                                    this.condicionesContratoSimulacion[2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                    this.condicionesContratoSimulacion[3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                    this.condicionesContratoSimulacion[4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                    this.condicionesContratoSimulacion[5] = "0" ;   
                                    this.condicionesContratoSimulacion[6] = "0" ;   
                                    this.condicionesContratoSimulacion[7] = "0" ;   
                                    this.condicionesContratoSimulacion[8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                    this.condicionesContratoSimulacion[9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                    this.condicionesContratoSimulacion[10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                    this.condicionesContratoSimulacion[11] = "0" ; 
                                    this.condicionesContratoSimulacion[12] = "0" ;   
                                    this.condicionesContratoSimulacion[13] = "0" ;    
                                    this.condicionesContratoSimulacion[14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                    this.condicionesContratoSimulacion[15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                    this.condicionesContratoSimulacion[16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                    this.condicionesContratoSimulacion[17] = "0" ;   
                                    this.condicionesContratoSimulacion[18] = "0" ;   
                                    this.condicionesContratoSimulacion[19] = "0" ;       
                                    this.condicionesContratoSimulacion[20] = rs.getString("observaciones");
                                    this.condicionesContratoSimulacion[21] = String.valueOf(rs.getInt("id"))  ;
                                    this.condicionesContratoSimulacion[22] = "6"  ;
                                    this.condicionesContratoSimulacion[23] = "0" ; 
                                    this.condicionesContratoSimulacion[24] = Double.toString(rs.getDouble("alquiler")) ; 
                                    this.condicionesContratoSimulacion[25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                break;      
                                 case 7:
                                  
                                    this.condicionesContratoSimulacion[2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                    this.condicionesContratoSimulacion[3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                    this.condicionesContratoSimulacion[4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                    this.condicionesContratoSimulacion[5] = Double.toString(rs.getDouble("potencia_contratada_p4")) ;   
                                    this.condicionesContratoSimulacion[6] = Double.toString(rs.getDouble("potencia_contratada_p5")) ;   
                                    this.condicionesContratoSimulacion[7] = Double.toString(rs.getDouble("potencia_contratada_p6")) ;   
                                    this.condicionesContratoSimulacion[8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                    this.condicionesContratoSimulacion[9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                    this.condicionesContratoSimulacion[10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                    this.condicionesContratoSimulacion[11] = Double.toString(rs.getDouble("precio_potencia_p4")) ; 
                                    this.condicionesContratoSimulacion[12] = Double.toString(rs.getDouble("precio_potencia_p5")) ;   
                                    this.condicionesContratoSimulacion[13] = Double.toString(rs.getDouble("precio_potencia_p6")) ;    
                                    this.condicionesContratoSimulacion[14] = Double.toString(rs.getDouble("precio_energia_VAR_p1")) ;   
                                    this.condicionesContratoSimulacion[15] = Double.toString(rs.getDouble("precio_energia_VAR_p2")) ;     
                                    this.condicionesContratoSimulacion[16] = Double.toString(rs.getDouble("precio_energia_VAR_p3")) ;    
                                    this.condicionesContratoSimulacion[17] = Double.toString(rs.getDouble("precio_energia_VAR_p4")) ;   
                                    this.condicionesContratoSimulacion[18] = Double.toString(rs.getDouble("precio_energia_VAR_p5")) ;   
                                    this.condicionesContratoSimulacion[19] = Double.toString(rs.getDouble("precio_energia_VAR_p6")) ;       
                                    this.condicionesContratoSimulacion[20] = rs.getString("observaciones");
                                    this.condicionesContratoSimulacion[21] = String.valueOf(rs.getInt("id"))  ;
                                    this.condicionesContratoSimulacion[22] = "7"  ;
                                    this.condicionesContratoSimulacion[23] = "0" ; 
                                    this.condicionesContratoSimulacion[24] = Double.toString(rs.getDouble("alquiler")) ; 
                                    this.condicionesContratoSimulacion[25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                break; 
                                  case 10:
                                    this.condicionesContratoSimulacion[2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                    this.condicionesContratoSimulacion[3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                    this.condicionesContratoSimulacion[4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                    this.condicionesContratoSimulacion[5] = "0" ;   
                                    this.condicionesContratoSimulacion[6] = "0" ;   
                                    this.condicionesContratoSimulacion[7] = "0" ;   
                                    this.condicionesContratoSimulacion[8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                    this.condicionesContratoSimulacion[9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                    this.condicionesContratoSimulacion[10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                    this.condicionesContratoSimulacion[11] = "0" ; 
                                    this.condicionesContratoSimulacion[12] = "0" ;   
                                    this.condicionesContratoSimulacion[13] = "0" ;    
                                    this.condicionesContratoSimulacion[14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                    this.condicionesContratoSimulacion[15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                    this.condicionesContratoSimulacion[16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                    this.condicionesContratoSimulacion[17] = "0" ;   
                                    this.condicionesContratoSimulacion[18] = "0" ;   
                                    this.condicionesContratoSimulacion[19] = "0" ;       
                                    this.condicionesContratoSimulacion[20] = rs.getString("observaciones");
                                    this.condicionesContratoSimulacion[21] = String.valueOf(rs.getInt("id"))  ;
                                    this.condicionesContratoSimulacion[22] = "10"  ;
                                    this.condicionesContratoSimulacion[23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                    this.condicionesContratoSimulacion[24] = Double.toString(rs.getDouble("alquiler")) ; 
                                    this.condicionesContratoSimulacion[25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                 break;      
                                 case 13:
                                    this.condicionesContratoSimulacion[2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                    this.condicionesContratoSimulacion[3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                    this.condicionesContratoSimulacion[4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                    this.condicionesContratoSimulacion[5] = "0" ;   
                                    this.condicionesContratoSimulacion[6] = "0" ;   
                                    this.condicionesContratoSimulacion[7] = "0" ;   
                                    this.condicionesContratoSimulacion[8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                    this.condicionesContratoSimulacion[9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                    this.condicionesContratoSimulacion[10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                    this.condicionesContratoSimulacion[11] = "0" ; 
                                    this.condicionesContratoSimulacion[12] = "0" ;   
                                    this.condicionesContratoSimulacion[13] = "0" ;    
                                    this.condicionesContratoSimulacion[14] = Double.toString(rs.getDouble("precio_energia_p1")) ;   
                                    this.condicionesContratoSimulacion[15] = Double.toString(rs.getDouble("precio_energia_p2")) ;     
                                    this.condicionesContratoSimulacion[16] = Double.toString(rs.getDouble("precio_energia_p3")) ;    
                                    this.condicionesContratoSimulacion[17] = "0" ;   
                                    this.condicionesContratoSimulacion[18] = "0" ;   
                                    this.condicionesContratoSimulacion[19] = "0" ;       
                                    this.condicionesContratoSimulacion[20] = rs.getString("observaciones");
                                    this.condicionesContratoSimulacion[21] = String.valueOf(rs.getInt("id"))  ;
                                    this.condicionesContratoSimulacion[22] = "13"  ;
                                    this.condicionesContratoSimulacion[23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                    this.condicionesContratoSimulacion[24] = Double.toString(rs.getDouble("alquiler")) ; 
                                    this.condicionesContratoSimulacion[25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                 break;      
                                 case 14:
                                  
                                    this.condicionesContratoSimulacion[2] = Double.toString(rs.getDouble("potencia_contratada_p1")) ;
                                    this.condicionesContratoSimulacion[3] = Double.toString(rs.getDouble("potencia_contratada_p2")) ;    
                                    this.condicionesContratoSimulacion[4] = Double.toString(rs.getDouble("potencia_contratada_p3")) ; 
                                    this.condicionesContratoSimulacion[5] = Double.toString(rs.getDouble("potencia_contratada_p4")) ;   
                                    this.condicionesContratoSimulacion[6] = Double.toString(rs.getDouble("potencia_contratada_p5")) ;   
                                    this.condicionesContratoSimulacion[7] = Double.toString(rs.getDouble("potencia_contratada_p6")) ;   
                                    this.condicionesContratoSimulacion[8] = Double.toString(rs.getDouble("precio_potencia_p1")) ;   
                                    this.condicionesContratoSimulacion[9] = Double.toString(rs.getDouble("precio_potencia_p2")) ;     
                                    this.condicionesContratoSimulacion[10] = Double.toString(rs.getDouble("precio_potencia_p3")) ;   
                                    this.condicionesContratoSimulacion[11] = Double.toString(rs.getDouble("precio_potencia_p4")) ; 
                                    this.condicionesContratoSimulacion[12] = Double.toString(rs.getDouble("precio_potencia_p5")) ;   
                                    this.condicionesContratoSimulacion[13] = Double.toString(rs.getDouble("precio_potencia_p6")) ;    
                                    this.condicionesContratoSimulacion[14] = Double.toString(rs.getDouble("precio_energia_VAR_p1")) ;   
                                    this.condicionesContratoSimulacion[15] = Double.toString(rs.getDouble("precio_energia_VAR_p2")) ;     
                                    this.condicionesContratoSimulacion[16] = Double.toString(rs.getDouble("precio_energia_VAR_p3")) ;    
                                    this.condicionesContratoSimulacion[17] = Double.toString(rs.getDouble("precio_energia_VAR_p4")) ;   
                                    this.condicionesContratoSimulacion[18] = Double.toString(rs.getDouble("precio_energia_VAR_p5")) ;   
                                    this.condicionesContratoSimulacion[19] = Double.toString(rs.getDouble("precio_energia_VAR_p6")) ;       
                                    this.condicionesContratoSimulacion[20] = rs.getString("observaciones");
                                    this.condicionesContratoSimulacion[21] = String.valueOf(rs.getInt("id"))  ;
                                    this.condicionesContratoSimulacion[22] = "14"  ;
                                    this.condicionesContratoSimulacion[23] = Double.toString(rs.getDouble("precio_energia_peaje")) ; 
                                    this.condicionesContratoSimulacion[24] = Double.toString(rs.getDouble("alquiler")) ; 
                                    this.condicionesContratoSimulacion[25] = Integer.toString(rs.getInt("fPotenciaMaxima")) ;
                                break;    
                                }
                                    
                            }
                        } 
			rs.close();
			estatuto.close();
			conex.desconectar();
                        System.out.println(sqlStr+"He cargado "+contador+" Puntos de simulación con Simulacion activa de facturas");             

		} catch (SQLException e) {
			System.out.println(e.getMessage());
                        //                JOptionPane.showMessageDialog(null, "Error al consultar", "Error",JOptionPane.ERROR_MESSAGE);

		}
                return ;
		
	}
 
// ------------------------------------------------------------------------------------------------------------------     
        
}

 
       
      
                        