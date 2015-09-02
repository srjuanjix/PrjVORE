/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.informes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.soap.Text;

import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.ContentAccessor;

import org.slf4j.LoggerFactory;

/**
 *
 * @author acondaqua
 */
public class miJDialogInformeSimple extends javax.swing.JDialog {

    public String filePath;
    public String file;
    public File guarda;
    public int ahorro=0,dias=0;
    public String lTiposTarifas[] = {"","Tarifa 2.0A","Tarifa 2.0DH","Tarifa 2.1A","Tarifa 2.1DH","Tarifa 3.0A","Tarifa 3.1A","Tarifa 6.1A","Tarifa 2.0DH INDX","Tarifa 2.1DH INDX","Tarifa 3.0A INDX","Tarifa 2.0 INDX","Tarifa 2.1 INDX","Tarifa 3.1 INDX"} ;
   
    
    /**
     * Creates new form miJDialogInformeSimple
     */
    public miJDialogInformeSimple(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    miJDialogInformeSimple(informesTopComponent aThis, boolean b, informeSimpleFactura misDatos) {
        
        String str="", se1="",se2="",se3="";
        double subTotalPotencia=0;
        double subTotalEnergia=0;
        double subTotalImpuesto=0;
        double TotalEnergia=0;
        double ImporteTotal=0;
        double iva=0;
        double importeTotalFactura=0 ;
        double EnergiaPeaje=0;
        double PenalizacionReactiva=0;
        double precioR1=0, precioR2=0;
        double restaDescuento=0;
             
        
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
         
        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
     
        NumberFormat formatoNumero2 = NumberFormat.getNumberInstance();
        NumberFormat formatoNumero6 = NumberFormat.getNumberInstance();
     
        formatoNumero2.setMaximumFractionDigits(2);
        formatoNumero6.setMaximumFractionDigits(6);
         
         initComponents();   
      
                 
        if (misDatos.idTipo == 1 || misDatos.idTipo == 3 ) {
            
            jPanel45.setVisible(false);
            jPanel2.setVisible(false);
            jPanel46.setVisible(false);
            jPanel3.setVisible(false);
            jPanel5.setVisible(false);
            jPanel6.setVisible(false);
            jTextField33.setVisible(false);
            jTextField34.setVisible(false);
            
            
        }
        if (misDatos.idTipo == 2 || misDatos.idTipo == 4 || misDatos.idTipo == 8 || misDatos.idTipo == 9 ) {
            
            jPanel46.setVisible(false);
            jPanel3.setVisible(false);
            jPanel5.setVisible(false);
            jPanel6.setVisible(false);
        }
        
        System.out.println("misDatos.psalquiler= "+misDatos.psalquiler) ;
        
        if (misDatos.psalquiler != 0) {
            
            misDatos.alquiler = misDatos.dias * misDatos.psalquiler ;                           // El precio de alquiler varia seguramente.
            
        }
        
        
        str = this.lTiposTarifas[misDatos.idTipo] ; jTextField21.setText(str) ;
        
       // ---------------------------------------------------------------------------------------------------------------------------
       // RECALCULAMOS LA FACTURA VIRTUAL A PARTIR DE TODAS LAS PIEZAS DE INFORMACIÓN SEGUN TIPO DE TARIFA
       // ---------------------------------------------------------------------------------------------------------------------------
        
         switch (misDatos.idTipo) {       // ........................................................................................
                                case 5:   // TARIFA 3.0 A
                                   
                                    EnergiaPeaje     =misDatos.calculaEnergiaPeaje(misDatos.e1,misDatos.e2,misDatos.e3);    // Energia de peaje
                                    precioR1  = Double.valueOf(jTextField13.getText()) ;                                    // Precio penalización para P1 reactiva
                                    precioR2  = Double.valueOf(jTextField16.getText()) ;                                    // Precio penalización para P2 reactiva

                                    System.out.println(" voy a calcular la penalizacion de reactiva");
                                    
                                    if (misDatos.TR > 0)    PenalizacionReactiva    = misDatos.TR  ;
                                    else 
                                                            PenalizacionReactiva    = misDatos.calculaPenalizacionReactiva(misDatos.rs1,misDatos.rs2,precioR1,precioR2) ;

                                    //........................................................ 

                                    subTotalPotencia  =misDatos.calculaSubTotalPotencia();

                                    subTotalEnergia  =misDatos.calculaSubTotalEnergia();

                                    restaDescuento = misDatos.calculaDescuento(subTotalEnergia,misDatos.descuento) ; 

                                    subTotalImpuesto  =misDatos.calculaSubTotalImpuesto(subTotalPotencia, subTotalEnergia, PenalizacionReactiva,restaDescuento );

                                    //........................................................

                                    TotalEnergia  =misDatos.calculaTotalEnergia(subTotalPotencia, subTotalEnergia, PenalizacionReactiva, subTotalImpuesto,restaDescuento);

                                    ImporteTotal  =misDatos.calculaImporteTotal(TotalEnergia,misDatos.alquiler);

                                    iva  =misDatos.calculaImporteIVA(ImporteTotal);

                                    importeTotalFactura  =misDatos.calculaImporteTotalFactura(ImporteTotal,iva);

                                    se1 = formatoNumero2.format(misDatos.e1) ;
                                    se2 = formatoNumero2.format(misDatos.e2) ;
                                    se3 = formatoNumero2.format(misDatos.e3) ;
                                                                       
                                break;
                                  case 6:   // TARIFA 3.1 A
                                   
                                    EnergiaPeaje     =misDatos.calculaEnergiaPeaje(misDatos.e1,misDatos.e2,misDatos.e3);    // Energia de peaje
                                    precioR1  = Double.valueOf(jTextField13.getText()) ;                                    // Precio penalización para P1 reactiva
                                    precioR2  = Double.valueOf(jTextField16.getText()) ;                                    // Precio penalización para P2 reactiva

                                    System.out.println(" voy a calcular la penalizacion de reactiva");
                                    if (misDatos.TR > 0)    PenalizacionReactiva    = misDatos.TR  ;
                                    else 
                                                            PenalizacionReactiva    = misDatos.calculaPenalizacionReactiva(misDatos.rs1,misDatos.rs2,precioR1,precioR2) ;

                                    //........................................................ 

                                    subTotalPotencia  =misDatos.calculaSubTotalPotencia();

                                    subTotalEnergia  =misDatos.calculaSubTotalEnergia();

                                    restaDescuento = misDatos.calculaDescuento(subTotalEnergia,misDatos.descuento) ; 

                                    subTotalImpuesto  =misDatos.calculaSubTotalImpuesto(subTotalPotencia, subTotalEnergia, PenalizacionReactiva,restaDescuento );

                                    //........................................................

                                    TotalEnergia  =misDatos.calculaTotalEnergia(subTotalPotencia, subTotalEnergia, PenalizacionReactiva, subTotalImpuesto,restaDescuento);

                                    ImporteTotal  =misDatos.calculaImporteTotal(TotalEnergia,misDatos.alquiler);

                                    iva  =misDatos.calculaImporteIVA(ImporteTotal);

                                    importeTotalFactura  =misDatos.calculaImporteTotalFactura(ImporteTotal,iva);
                                    
                                    se1 = formatoNumero2.format(misDatos.e1) ;
                                    se2 = formatoNumero2.format(misDatos.e2) ;
                                    se3 = formatoNumero2.format(misDatos.e3) ;

                                                                       
                                break;
                                             // ........................................................................................
                                 case 10:    // TARIFA 3.0 INDEXADO
                                   
                                    EnergiaPeaje     =misDatos.calculaEnergiaPeaje(misDatos.e1,misDatos.e2,misDatos.e3);    // Energia de peaje
                                    precioR1  = Double.valueOf(jTextField13.getText()) ;                                    // Precio penalización para P1 reactiva
                                    precioR2  = Double.valueOf(jTextField16.getText()) ;                                    // Precio penalización para P2 reactiva

                                    System.out.println(" voy a calcular la penalizacion de reactiva");
                                   
                                    if (misDatos.TR > 0)    PenalizacionReactiva    = misDatos.TR  ;
                                    else 
                                                            PenalizacionReactiva    = misDatos.calculaPenalizacionReactiva(misDatos.rs1,misDatos.rs2,precioR1,precioR2) ;

                                    //........................................................ 

                                    subTotalPotencia  =misDatos.calculaSubTotalPotencia();

                                    subTotalEnergia  =misDatos.calculaSubTotalEnergia();

                                    restaDescuento = misDatos.calculaDescuento(subTotalEnergia,misDatos.descuento) ; 

                                    subTotalImpuesto  =misDatos.calculaSubTotalImpuesto(subTotalPotencia, subTotalEnergia, PenalizacionReactiva,restaDescuento );

                                    //........................................................

                                    TotalEnergia  =misDatos.calculaTotalEnergia(subTotalPotencia, subTotalEnergia, PenalizacionReactiva, subTotalImpuesto,restaDescuento);

                                    ImporteTotal  =misDatos.calculaImporteTotal(TotalEnergia,misDatos.alquiler);

                                    iva  =misDatos.calculaImporteIVA(ImporteTotal);

                                    importeTotalFactura  =misDatos.calculaImporteTotalFactura(ImporteTotal,iva);
                                    
                                    se1 = formatoNumero2.format(misDatos.e1) ;
                                    se2 = formatoNumero2.format(misDatos.e2) ;
                                    se3 = formatoNumero2.format(misDatos.e3) ;
                                     
                                    break;
                                              // ........................................................................................
                                 case 13:    // TARIFA 3.1 INDEXADO
                                   
                                    if ( (misDatos.e1s !=0 ) ){                                      // si son diferentes tenemos energía simulada 
             
                                    EnergiaPeaje     =misDatos.calculaEnergiaPeaje(misDatos.e1s,misDatos.e2s,misDatos.e3s);     // Energia de peaje
                                    
                                    } else {
                                        
                                    EnergiaPeaje     =misDatos.calculaEnergiaPeaje(misDatos.e1,misDatos.e2,misDatos.e3);        // Energia de peaje    
                                        
                                    }
                                    
                                    precioR1  = Double.valueOf(jTextField13.getText()) ;                                    // Precio penalización para P1 reactiva
                                    precioR2  = Double.valueOf(jTextField16.getText()) ;                                    // Precio penalización para P2 reactiva

                                    System.out.println(" voy a calcular la penalizacion de reactiva");
                                   
                                    if (misDatos.TR > 0)    PenalizacionReactiva    = misDatos.TR  ;
                                    else 
                                                            PenalizacionReactiva    = misDatos.calculaPenalizacionReactiva(misDatos.rs1,misDatos.rs2,precioR1,precioR2) ;

                                    //........................................................ 

                                    subTotalPotencia  =misDatos.calculaSubTotalPotencia();

                                    subTotalEnergia  =misDatos.calculaSubTotalEnergia();

                                    restaDescuento = misDatos.calculaDescuento(subTotalEnergia,misDatos.descuento) ; 

                                    subTotalImpuesto  =misDatos.calculaSubTotalImpuesto(subTotalPotencia, subTotalEnergia, PenalizacionReactiva,restaDescuento );

                                    //........................................................

                                    TotalEnergia  =misDatos.calculaTotalEnergia(subTotalPotencia, subTotalEnergia, PenalizacionReactiva, subTotalImpuesto,restaDescuento);

                                    ImporteTotal  =misDatos.calculaImporteTotal(TotalEnergia,misDatos.alquiler);

                                    iva  =misDatos.calculaImporteIVA(ImporteTotal);

                                    importeTotalFactura  =misDatos.calculaImporteTotalFactura(ImporteTotal,iva);
                                    
                                    se1 = formatoNumero2.format(misDatos.e1) ;
                                    se2 = formatoNumero2.format(misDatos.e2) ;
                                    se3 = formatoNumero2.format(misDatos.e3) ;
                                   
                                    break;
                                            // ........................................................................................
                                 case 3:    // TARIFA 2.X A 
                                   
                                    EnergiaPeaje     = 0;                                                                     // Energia de peaje
                                    precioR1  = Double.valueOf(jTextField13.getText()) ;                                    // Precio penalización para P1 reactiva
                                    precioR2  = Double.valueOf(jTextField16.getText()) ;                                    // Precio penalización para P2 reactiva

                                    System.out.println(" voy a calcular la penalizacion de reactiva");
                                    PenalizacionReactiva    = misDatos.calculaPenalizacionReactiva(misDatos.rs1,misDatos.rs2,precioR1,precioR2) ;

                                    //........................................................ 

                                    subTotalPotencia  =misDatos.calculaSubTotalPotenciaT02();

                                    subTotalEnergia  =misDatos.calculaSubTotalEnergiaT02();

                                    restaDescuento = misDatos.calculaDescuento(subTotalEnergia,misDatos.descuento) ; 

                                    subTotalImpuesto  =misDatos.calculaSubTotalImpuesto(subTotalPotencia, subTotalEnergia, PenalizacionReactiva,restaDescuento );

                                    //........................................................

                                    TotalEnergia  =misDatos.calculaTotalEnergia(subTotalPotencia, subTotalEnergia, PenalizacionReactiva, subTotalImpuesto,restaDescuento);

                                    ImporteTotal  =misDatos.calculaImporteTotal(TotalEnergia,misDatos.alquiler);

                                    iva  =misDatos.calculaImporteIVA(ImporteTotal);

                                    importeTotalFactura  =misDatos.calculaImporteTotalFactura(ImporteTotal,iva);
                                     
                                    se1 = formatoNumero2.format(misDatos.e1+misDatos.e2+misDatos.e3) ;
                                    
                                    
                                    break;       
                                    case 1:    // TARIFA 2.X A 
                                   
                                    EnergiaPeaje     = 0;                                                                     // Energia de peaje
                                    precioR1  = Double.valueOf(jTextField13.getText()) ;                                    // Precio penalización para P1 reactiva
                                    precioR2  = Double.valueOf(jTextField16.getText()) ;                                    // Precio penalización para P2 reactiva

                                    System.out.println(" voy a calcular la penalizacion de reactiva");
                                    PenalizacionReactiva    = misDatos.TR ;

                                    //........................................................ 

                                    subTotalPotencia  =misDatos.calculaSubTotalPotenciaT02();

                                    subTotalEnergia  =misDatos.calculaSubTotalEnergiaT02();

                                    restaDescuento = misDatos.calculaDescuento(subTotalEnergia,misDatos.descuento) ; 

                                    subTotalImpuesto  =misDatos.calculaSubTotalImpuesto(subTotalPotencia, subTotalEnergia, PenalizacionReactiva,restaDescuento );

                                    //........................................................

                                    TotalEnergia  =misDatos.calculaTotalEnergia(subTotalPotencia, subTotalEnergia, PenalizacionReactiva, subTotalImpuesto,restaDescuento);

                                    ImporteTotal  =misDatos.calculaImporteTotal(TotalEnergia,misDatos.alquiler);

                                    iva  =misDatos.calculaImporteIVA(ImporteTotal);

                                    importeTotalFactura  =misDatos.calculaImporteTotalFactura(ImporteTotal,iva);
                                    
                                
                                    
                                    se1 = formatoNumero2.format(misDatos.e1+misDatos.e2+misDatos.e3) ;
                                    
                                    break;       
         }
        
        
        
        // ---------------------------------------------------------------------------------------------------------------------------
       
        jTextField106.setText(misDatos.sFecha1) ;
        jTextField107.setText(misDatos.sFecha2) ;
         
        jTextField28.setText(String.valueOf(misDatos.dias)) ;
        jTextField110.setText(String.valueOf(misDatos.dias)) ;
        jTextField114.setText(String.valueOf(misDatos.dias)) ;
        jTextField118.setText(String.valueOf(misDatos.dias)) ;
        if ( (misDatos.e1s !=0 ) ){                                      // si son diferentes tenemos energía simulada 
            
            if (misDatos.idTipo == 3 ||  misDatos.idTipo == 1  ) {
                
                jTextField1.setText(formatoNumero2.format(misDatos.e1s+misDatos.e2s+misDatos.e3s)) ;
                
            } else {
                
                if (misDatos.idTipo == 2 ||  misDatos.idTipo == 4  ) {
                    
                    jTextField1.setText(formatoNumero2.format(misDatos.e1s+misDatos.e2s)) ;       // energia consumida e1 simulada
                    jTextField4.setText(formatoNumero2.format(misDatos.e3s)) ;                    // energia consumida e2 simulada
                    
                } else {
            
                    jTextField1.setText(formatoNumero2.format(misDatos.e1s)) ;                    // energia consumida e1 simulada
                    jTextField4.setText(formatoNumero2.format(misDatos.e2s)) ;                    // energia consumida e2 simulada
                    jTextField7.setText(formatoNumero2.format(misDatos.e3s)) ;                    // energia consumida e3 simulada
                
                }
            }
       
        } else {
            
                        
            jTextField1.setText(se1) ;                    // energia consumida e1
            jTextField4.setText(se2) ;                    // energia consumida e2
            jTextField7.setText(se3) ;                    // energia consumida e3
        
        }
       
        
        jTextField29.setText(formatoNumero2.format(EnergiaPeaje)) ;                    // Energia de peaje
        jTextField10.setText(formatoNumero2.format(EnergiaPeaje)) ;                    // Energia de peaje (energia total)
         
        jTextField108.setText(formatoNumero2.format(misDatos.pp)) ;                 // potencia facturada p1
        jTextField112.setText(formatoNumero2.format(misDatos.pll)) ;                // potencia facturada p2
        jTextField116.setText(formatoNumero2.format(misDatos.pv)) ;                 // potencia facturada p3
        
        jTextField32.setText(String.valueOf(misDatos.pcp)) ;                 // potencia contratada p1
        jTextField33.setText(String.valueOf(misDatos.pcll)) ;                // potencia contratada p2
        jTextField34.setText(String.valueOf(misDatos.pcv)) ;                 // potencia contratada p3
        
        jTextField109.setText(formatoNumero6.format(misDatos.ppp)) ;         // precio potencia  p1
        jTextField113.setText(formatoNumero6.format(misDatos.ppll)) ;        // precio potencia p2
        jTextField117.setText(formatoNumero6.format(misDatos.ppv)) ;         // precio potencia p3
        
        jTextField2.setText(formatoNumero6.format(misDatos.pe1)) ;          // precio energia p1
        jTextField5.setText(formatoNumero6.format(misDatos.pe2)) ;          // precio energia  p2
        jTextField8.setText(formatoNumero6.format(misDatos.pe3)) ;          // precio energia p3
        
        jTextField12.setText(formatoNumero2.format(misDatos.rs1)) ;                  // energia reactiva  p1
        jTextField15.setText(formatoNumero2.format(misDatos.rs2)) ;                  // energia reactiva  p2
        
        //........................................................
        jTextField14.setText(formatoImporte.format(misDatos.rTF1)) ;            // subtotal reactiva P1
        jTextField17.setText(formatoImporte.format(misDatos.rTF2)) ;            // subtotal reactiva P2
        
        jTextField18.setText(formatoImporte.format(PenalizacionReactiva)) ;     // subtotal penalizacion reactiva
        //........................................................
        
        jTextField23.setText(formatoImporte.format(misDatos.alquiler)) ;             // alquiler
       
        //........................................................
              
        jTextField111.setText(formatoImporte.format(misDatos.sppp)) ;                 // potencia facturada p1
        jTextField115.setText(formatoImporte.format(misDatos.sppll)) ;                // potencia facturada p2
        jTextField119.setText(formatoImporte.format(misDatos.sppv)) ;                 // potencia facturada p3
        jTextField120.setText(formatoImporte.format(subTotalPotencia)) ;              // potencia facturada total
        
        //........................................................
              
        jTextField3.setText(formatoImporte.format(misDatos.spe1)) ;                 // energia facturada p1
        jTextField6.setText(formatoImporte.format(misDatos.spe2)) ;                 // energia facturada p2
        jTextField9.setText(formatoImporte.format(misDatos.spe3)) ;                 // energia facturada p3
        jTextField31.setText(formatoImporte.format(misDatos.spep)) ;                 // energia facturada p3
        jTextField11.setText(formatoImporte.format(subTotalEnergia)) ;               // potencia facturada total
        
               
        //........................................................
        
         jTextField20.setText(formatoImporte.format(subTotalImpuesto)) ;               // subtotal de constes impuesto energia
         
        //........................................................
        
           
         jTextField22.setText(formatoImporte.format(TotalEnergia)) ;               // total energia
         
         jTextField24.setText(formatoImporte.format(misDatos.alquiler+misDatos.TR)) ;          // total servicios y otros conceptos
         
         //........................................................
        
        
        jTextField25.setText(formatoImporte.format(ImporteTotal)) ;             // importe total
        
         //........................................................
        
         
        jTextField26.setText(formatoImporte.format(iva)) ;             // importe iva
        
         //........................................................
        
        
        jTextField27.setText(formatoImporte.format(importeTotalFactura)) ;             // importe total factura
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        jLabel145 = new javax.swing.JLabel();
        jLabel147 = new javax.swing.JLabel();
        jTextField108 = new javax.swing.JTextField();
        jLabel148 = new javax.swing.JLabel();
        jTextField109 = new javax.swing.JTextField();
        jLabel149 = new javax.swing.JLabel();
        jTextField110 = new javax.swing.JTextField();
        jLabel150 = new javax.swing.JLabel();
        jTextField111 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jLabel160 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel161 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jLabel162 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        jLabel151 = new javax.swing.JLabel();
        jTextField112 = new javax.swing.JTextField();
        jLabel152 = new javax.swing.JLabel();
        jTextField113 = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        jTextField114 = new javax.swing.JTextField();
        jLabel154 = new javax.swing.JLabel();
        jTextField115 = new javax.swing.JTextField();
        jPanel46 = new javax.swing.JPanel();
        jLabel155 = new javax.swing.JLabel();
        jTextField116 = new javax.swing.JTextField();
        jLabel156 = new javax.swing.JLabel();
        jTextField117 = new javax.swing.JTextField();
        jLabel157 = new javax.swing.JLabel();
        jTextField118 = new javax.swing.JTextField();
        jLabel158 = new javax.swing.JLabel();
        jTextField119 = new javax.swing.JTextField();
        jPanel47 = new javax.swing.JPanel();
        jLabel159 = new javax.swing.JLabel();
        jTextField120 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jTextField31 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jTextField35 = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jTextField106 = new javax.swing.JTextField();
        jTextField107 = new javax.swing.JTextField();
        jLabel146 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel44.setBackground(new java.awt.Color(204, 204, 204));

        jLabel144.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel144.setText("POTENCIA");

        jLabel145.setText("Potencia facturada ");

        jLabel147.setText("PP ");

        jTextField108.setText("0");

        jLabel148.setText("kW x");

        jLabel149.setText("€/kW·dia x");

        jLabel150.setText("días");

        jLabel38.setText("Contratadas:");

        jLabel160.setText("PP kW");

        jLabel161.setText("PLL kW");

        jLabel162.setText("PV kW");

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(jLabel144, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel160, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel161, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel162, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(408, Short.MAX_VALUE))
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(jLabel145, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel147, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField108, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel148, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField109, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel149, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField110, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel150, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField111, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel144)
                    .addComponent(jLabel38)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel160)
                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel161)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel162))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel145)
                    .addComponent(jLabel147)
                    .addComponent(jTextField108, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel148)
                    .addComponent(jTextField109, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel149)
                    .addComponent(jTextField110, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel150)
                    .addComponent(jTextField111, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel45.setBackground(new java.awt.Color(204, 204, 204));

        jLabel151.setText("PLL");

        jTextField112.setText("0");

        jLabel152.setText("kW x");

        jLabel153.setText("€/kW·dia x");

        jLabel154.setText("días");

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap(680, Short.MAX_VALUE)
                .addComponent(jLabel151, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField112, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel152, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField113, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel153, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField114, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel154, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField115, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel151)
                    .addComponent(jTextField112, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField113, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel153)
                    .addComponent(jTextField114, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel154)
                    .addComponent(jLabel152)
                    .addComponent(jTextField115, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel46.setBackground(new java.awt.Color(204, 204, 204));

        jLabel155.setText("PV");

        jTextField116.setText("0");

        jLabel156.setText("kW x");

        jLabel157.setText("€/kW·dia x");

        jTextField118.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField118ActionPerformed(evt);
            }
        });

        jLabel158.setText("días");

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap(683, Short.MAX_VALUE)
                .addComponent(jLabel155, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField116, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel156, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField117, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel157, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField118, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel158, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField119, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel155)
                    .addComponent(jTextField116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel156)
                    .addComponent(jTextField117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel157)
                    .addComponent(jTextField118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel158)
                    .addComponent(jTextField119)))
        );

        jPanel47.setBackground(new java.awt.Color(204, 204, 255));

        jLabel159.setText("Total importe potencia ");

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel159, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField120, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel159)
                    .addComponent(jTextField120, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton1.setText("CERRAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("ENERGIA");

        jLabel2.setText("Energía facturada");

        jLabel3.setText("P");

        jTextField1.setText("0");

        jLabel4.setText("kWh x");

        jTextField2.setText("0");

        jLabel5.setText("€/kWh ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 556, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel6.setText("LL");

        jTextField4.setText("0");

        jLabel7.setText("kWh x");

        jTextField5.setText("0");

        jLabel8.setText("€/kWh ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(669, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel9.setText("V");

        jTextField7.setText("0");

        jLabel10.setText("kWh x ");

        jTextField8.setText("0");
        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });

        jLabel11.setText("€/kWh");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(671, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 255));

        jLabel12.setText("Total ");

        jLabel33.setText("kWh");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));

        jLabel13.setText("Energía reactiva");

        jLabel14.setText("P1");

        jLabel15.setText("kVArh x");

        jTextField13.setText("0.041554");

        jLabel16.setText("€/kVArh");

        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });

        jLabel17.setText("P2");

        jLabel18.setText("kVArh x");

        jTextField16.setText("0.041554");

        jLabel19.setText("€/kVArh");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField15, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                    .addComponent(jTextField12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField16)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField17, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(jTextField14)))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel6.setBackground(new java.awt.Color(204, 204, 255));

        jLabel20.setText("Total energía reactiva");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton2.setText("GENERAR DOCUMENTO");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(204, 204, 204));

        jLabel23.setText("Impuesto sobre electricidad");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel25.setText("TOTAL ENERGIA");

        jTextField22.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField22.setForeground(new java.awt.Color(0, 102, 51));

        jLabel26.setText("SERVICIOS Y OTROS CONCEPTOS");

        jLabel27.setText("Alquiler equipos de medida");

        jLabel28.setText("€");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(829, 829, 829)
                        .addComponent(jTextField23, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel28)
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel25))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField20)
                            .addComponent(jTextField22, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addContainerGap())
        );

        jPanel9.setBackground(new java.awt.Color(204, 204, 255));

        jLabel29.setText("TOTAL SERVICIOS Y OTROS CONCEPTOS");

        jLabel30.setText("IMPORTE TOTAL");

        jLabel31.setText("IVA");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel32.setText("TOTAL IMPORTE FACTURA");

        jTextField27.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTextField27.setForeground(new java.awt.Color(0, 102, 51));
        jTextField27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField27ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                    .addComponent(jTextField25, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField26, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField27, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));

        jLabel35.setText("Energia de peaje");

        jTextField29.setText("0");

        jLabel36.setText("kWh x ");

        jTextField30.setText("0");
        jTextField30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField30ActionPerformed(evt);
            }
        });

        jLabel37.setText("€/kWh");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel36)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel36)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jButton4.setText("BUSCAR PLANTILLA");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jTextField35.setText("Plantilla.doc");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel39.setText("PLANTILLA DOC");

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel43Layout.createSequentialGroup()
                        .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(251, 251, 251)
                        .addComponent(jButton1)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel43Layout.createSequentialGroup()
                        .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel47, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel46, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel45, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel44, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 34, Short.MAX_VALUE))))
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel43Layout.createSequentialGroup()
                .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel46, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton1)
                    .addComponent(jButton4)
                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel24.setText("TARIFA SIMULADA ");

        jTextField21.setText("jTextField21");

        jLabel146.setText("-");

        jLabel34.setText("Dias");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jTextField106, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel146, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField107, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel34)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(335, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 3, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField106, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField107, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel146)
                    .addComponent(jLabel34)
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField118ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField118ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField118ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField30ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField27ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            generarWord() ;
        } catch (Docx4JException ex) {
           // Logger.getLogger(miJDialogGenerarInforme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
          //  Logger.getLogger(miJDialogGenerarInforme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
          //  Logger.getLogger(miJDialogGenerarInforme.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
          //  Logger.getLogger(miJDialogGenerarInforme.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        buscarPlantilla();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(miJDialogInformeSimple.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(miJDialogInformeSimple.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(miJDialogInformeSimple.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(miJDialogInformeSimple.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                miJDialogInformeSimple dialog = new miJDialogInformeSimple(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel144;
    private javax.swing.JLabel jLabel145;
    private javax.swing.JLabel jLabel146;
    private javax.swing.JLabel jLabel147;
    private javax.swing.JLabel jLabel148;
    private javax.swing.JLabel jLabel149;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel150;
    private javax.swing.JLabel jLabel151;
    private javax.swing.JLabel jLabel152;
    private javax.swing.JLabel jLabel153;
    private javax.swing.JLabel jLabel154;
    private javax.swing.JLabel jLabel155;
    private javax.swing.JLabel jLabel156;
    private javax.swing.JLabel jLabel157;
    private javax.swing.JLabel jLabel158;
    private javax.swing.JLabel jLabel159;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel160;
    private javax.swing.JLabel jLabel161;
    private javax.swing.JLabel jLabel162;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField106;
    private javax.swing.JTextField jTextField107;
    private javax.swing.JTextField jTextField108;
    private javax.swing.JTextField jTextField109;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField110;
    private javax.swing.JTextField jTextField111;
    private javax.swing.JTextField jTextField112;
    private javax.swing.JTextField jTextField113;
    private javax.swing.JTextField jTextField114;
    private javax.swing.JTextField jTextField115;
    private javax.swing.JTextField jTextField116;
    private javax.swing.JTextField jTextField117;
    private javax.swing.JTextField jTextField118;
    private javax.swing.JTextField jTextField119;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField120;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField28;
    private javax.swing.JTextField jTextField29;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField31;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField33;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
// ------------------------------------------------------------------------------------------------------------------------
  public void generarWord() throws Docx4JException, FileNotFoundException, IOException, JAXBException {
       
        String sP1, sP2, sP3;
        String sDias;
        String sPP1, sPP2, sPP3;
        String sSP1, sSP2, sSP3;
        String sE1, sE2, sE3, sEP;
        String spE1, spE2, spE3, spEP;
        String sSP,sSE,sFecha1,sFecha2 ;
        String sR1,sR2,spR1,spR2,ssR1,ssR2,sPR ;
        String sDC,sIE,sTE,sAM,sTS,sIT,sIVA,sITF ;
        String sSE1,sSE2,sSE3,sSEP;
        String filePathName ;
        
        
         JOptionPane.showMessageDialog(null,
                "\nVoy a generar el informe con los datos actuales ",
                "AVISO",JOptionPane.WARNING_MESSAGE);
         
       
       String filePath = "C:\\Users\\acondaqua\\Documents\\";
       String file = "Plantilla .docx";
         
      // .........................................................................   
       
    
      
    
      sFecha1 =  jTextField106.getText();
      sFecha2 =  jTextField107.getText();
       
        
      sP1  = jTextField108.getText();
      sP2  = jTextField112.getText();
      sP3  = jTextField116.getText();
      
      sDias = jTextField28.getText();
      
      sPP1  = jTextField109.getText();
      sPP2  = jTextField113.getText();
      sPP3  = jTextField117.getText();
      
      sSP1  = jTextField111.getText();
      sSP2  = jTextField115.getText();
      sSP3  = jTextField119.getText();
      
      sSP   = jTextField120.getText();
     
      sE1  = jTextField1.getText();
      sE2  = jTextField4.getText();
      sE3  = jTextField7.getText();
      sEP  = jTextField29.getText();
      
      sSE1  = jTextField3.getText();
      sSE2  = jTextField6.getText();
      sSE3  = jTextField9.getText();
      sSEP  = jTextField31.getText();
      
      
      sSE   = jTextField11.getText();
      
      spE1  = jTextField2.getText();
      spE2  = jTextField5.getText();
      spE3  = jTextField8.getText();
      spEP  = jTextField30.getText();
      
      
      sR1   = jTextField12.getText();
      sR2   = jTextField15.getText();
      spR1  = jTextField13.getText();
      spR2  = jTextField16.getText();
      ssR1  = jTextField14.getText();
      ssR2  = jTextField17.getText();
      sPR   = jTextField18.getText();
     
      
     
      sIE    = jTextField20.getText();
      sTE    = jTextField22.getText();
      sAM    = jTextField23.getText();
      sTS    = jTextField24.getText();
      sIT    = jTextField25.getText();
      sIVA   = jTextField26.getText();
      sITF   = jTextField27.getText();
      
      
      // .........................................................................  
      filePathName =this.guarda.getPath();
      System.out.println("Filepath:"+filePathName);
      WordprocessingMLPackage wordMLPackage = getTemplate2(this.guarda) ;     
      
    
      List<Map<DataFieldName, String>> data = new ArrayList<Map<DataFieldName, String>>();

      Map<DataFieldName, String> map1 = new HashMap<DataFieldName, String>();
      
       
      // --------------------------------------------------------------------
      
      map1.put(new DataFieldName("SP_Fecha2"), sFecha2 );
      
      // --------------------------------------------------------------------
      map1.put(new DataFieldName("SP_P1"), sP1 );
      map1.put(new DataFieldName("SP_P2"), sP2);         
      map1.put(new DataFieldName("SP_P3"), sP3);
      // --------------------------------------------------------------------
      map1.put(new DataFieldName("SP_PP1"), sPP1 );
      map1.put(new DataFieldName("SP_PP2"), sPP2);         
      map1.put(new DataFieldName("SP_PP3"), sPP3);
      // --------------------------------------------------------------------
      
       map1.put(new DataFieldName("SP_DIAS"), sDias);
      
      // --------------------------------------------------------------------
      map1.put(new DataFieldName("SP_SP1"), sSP1);
      map1.put(new DataFieldName("SP_SP2"), sSP2);         
      map1.put(new DataFieldName("SP_SP3"), sSP3);
      
      // --------------------------------------------------------------------
      
      map1.put(new DataFieldName("SP_SP"), sSP);
      
      // --------------------------------------------------------------------
      map1.put(new DataFieldName("SP_E1"), sE1 );
      map1.put(new DataFieldName("SP_E2"), sE2 );         
      map1.put(new DataFieldName("SP_E3"), sE3 );
      map1.put(new DataFieldName("SP_EP"), sEP );
      
      // --------------------------------------------------------------------
      
      map1.put(new DataFieldName("SP_pE1"), spE1 );
      map1.put(new DataFieldName("SP_pE2"), spE2 );         
      map1.put(new DataFieldName("SP_pE3"), spE3 );
      map1.put(new DataFieldName("SP_pEP"), spEP );
      
      // --------------------------------------------------------------------
      
      map1.put(new DataFieldName("SP_SE1"), sSE1 );
      map1.put(new DataFieldName("SP_SE2"), sSE2 );         
      map1.put(new DataFieldName("SP_SE3"), sSE3 );
       
      // --------------------------------------------------------------------
      
       map1.put(new DataFieldName("SP_SE"), sSE);
      
      // --------------------------------------------------------------------
      
      map1.put(new DataFieldName("SP_R1"), sR1 );
      map1.put(new DataFieldName("SP_R2"), sR2 );         
      map1.put(new DataFieldName("SP_PR1"), spR1 );
      map1.put(new DataFieldName("SP_PR2"), spR2 );
      map1.put(new DataFieldName("SP_SPR1"), ssR1 );
      map1.put(new DataFieldName("SP_SPR2"), ssR2 );
      map1.put(new DataFieldName("SP_PR"), sPR );
      
      // --------------------------------------------------------------------
            
     
      map1.put(new DataFieldName("SP_IE"), sIE );         
      map1.put(new DataFieldName("SP_TE"), sTE );
      map1.put(new DataFieldName("SP_AM"), sAM );
      map1.put(new DataFieldName("SP_TS"), sTS );
      map1.put(new DataFieldName("SP_IT"), sIT );
      map1.put(new DataFieldName("SP_IVA"), sIVA );
      map1.put(new DataFieldName("SP_ITF"), sITF );
      
      // --------------------------------------------------------------------
      data.add(map1);
       
    // ......................................................................... 
     
      org.docx4j.model.fields.merge.MailMerger.setMERGEFIELDInOutput(MailMerger.OutputField.KEEP_MERGEFIELD);

      int x=0;
      String ruta;
      
      JFileChooser file2=new JFileChooser();
      file2.showSaveDialog(file2);
      File guarda =file2.getSelectedFile();
      if(guarda !=null)
        {
            
            ruta    = file2.getSelectedFile().getPath();                        
            for(Map<DataFieldName, String> docMapping: data) 
            {
              org.docx4j.model.fields.merge.MailMerger.performMerge(wordMLPackage, docMapping, true);
              wordMLPackage.save(new java.io.File(ruta) );
            }
            
        }
            
         JOptionPane.showMessageDialog(null,
                "\nEl informe se ha generado correctamente.",
                "AVISO",JOptionPane.WARNING_MESSAGE);
         
     
      
    }
    
    // ---------------------------------------------------------------------------------------------------------------
    public static void searchAndReplace(List<Object> texts, Map<String, String> values){

        // -- scan all expressions  
        // Will later contain all the expressions used though not used at the moment
        List<String> els = new ArrayList<String>(); 

        StringBuilder sb = new StringBuilder();
        int PASS = 0;
        int PREPARE = 1;
        int READ = 2;
        int mode = PASS;

        // to nullify
        List<int[]> toNullify = new ArrayList<int[]>();
        int[] currentNullifyProps = new int[4];

        // Do scan of els and immediately insert value
        for(int i = 0; i<texts.size(); i++){
            Object text = texts.get(i);
            Text textElement = (Text) text;
            String newVal = "";
            String v = textElement.getValue();
//          System.out.println("text: "+v);
            StringBuilder textSofar = new StringBuilder();
            int extra = 0;
            char[] vchars = v.toCharArray();
            for(int col = 0; col<vchars.length; col++){
                char c = vchars[col];
                textSofar.append(c);
                switch(c){
                case '$': {
                    mode=PREPARE;
                    sb.append(c);
//                  extra = 0;
                } break;
                case '{': {
                    if(mode==PREPARE){
                        sb.append(c);
                        mode=READ;
                        currentNullifyProps[0]=i;
                        currentNullifyProps[1]=col+extra-1;
                        System.out.println("extra-- "+extra);
                    } else {
                        if(mode==READ){
                            // consecutive opening curl found. just read it
                            // but supposedly throw error
                            sb = new StringBuilder();
                            mode=PASS;
                        }
                    }
                } break;
                case '}': {
                    if(mode==READ){
                        mode=PASS;
                        sb.append(c);
                        els.add(sb.toString());
                        newVal +=textSofar.toString()
                                +(null==values.get(sb.toString())?sb.toString():values.get(sb.toString()));
                        textSofar = new StringBuilder();
                        currentNullifyProps[2]=i;
                        currentNullifyProps[3]=col+extra;
                        toNullify.add(currentNullifyProps);
                        currentNullifyProps = new int[4];
                        extra += sb.toString().length();
                        sb = new StringBuilder();
                    } else if(mode==PREPARE){
                        mode = PASS;
                        sb = new StringBuilder();
                    }
                }
                default: {
                    if(mode==READ) sb.append(c);
                    else if(mode==PREPARE){
                        mode=PASS;
                        sb = new StringBuilder();
                    }
                }
                }
            }
            newVal +=textSofar.toString();
            textElement.setValue(newVal);
        }

        // remove original expressions
        if(toNullify.size()>0)
        for(int i = 0; i<texts.size(); i++){
            if(toNullify.size()==0) break;
            currentNullifyProps = toNullify.get(0);
            Object text = texts.get(i);
            Text textElement = (Text) text;
            String v = textElement.getValue();
            StringBuilder nvalSB = new StringBuilder();
            char[] textChars = v.toCharArray();
            for(int j = 0; j<textChars.length; j++){
                char c = textChars[j];
                if(null==currentNullifyProps) {
                    nvalSB.append(c);
                    continue;
                }
                // I know 100000 is too much!!! And so what???
                int floor = currentNullifyProps[0]*100000+currentNullifyProps[1];
                int ceil = currentNullifyProps[2]*100000+currentNullifyProps[3];
                int head = i*100000+j;
                if(!(head>=floor && head<=ceil)){
                    nvalSB.append(c);
                } 

                if(j>currentNullifyProps[3] && i>=currentNullifyProps[2]){
                    toNullify.remove(0);
                    if(toNullify.size()==0) {
                        currentNullifyProps = null;
                        continue;
                    }
                    currentNullifyProps = toNullify.get(0);
                }
            }
            textElement.setValue(nvalSB.toString());
        }
    }

    private WordprocessingMLPackage getTemplate(String name)
            throws Docx4JException, FileNotFoundException {
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(name)));
        return template;
    }
    private WordprocessingMLPackage getTemplate2(File guarda)
            throws Docx4JException, FileNotFoundException {
        
        WordprocessingMLPackage template = WordprocessingMLPackage.load(new FileInputStream(new File(guarda.getPath())));
        return template;
    }
    private static List<Object> getAllElementFromObject(Object obj,
            Class<?> toSearch) {
        List<Object> result = new ArrayList<Object>();
        if (obj instanceof JAXBElement)
            obj = ((JAXBElement<?>) obj).getValue();

        if (obj.getClass().equals(toSearch))
            result.add(obj);
        else if (obj instanceof ContentAccessor) {
            List<?> children = ((ContentAccessor) obj).getContent();
            for (Object child : children) {
                result.addAll(getAllElementFromObject(child, toSearch));
            }

        }
        return result;
    }

    private void replacePlaceholder(WordprocessingMLPackage template,
            String name, String placeholder) {
        List<Object> texts = getAllElementFromObject(
                template.getMainDocumentPart(), Text.class);

        for (Object text : texts) {
            Text textElement = (Text) text;
            if (textElement.getValue().equals(placeholder)) {
                textElement.setValue(name);
            }
        }
    }

    private void writeDocxToStream(WordprocessingMLPackage template,
            String target) throws IOException, Docx4JException {
        File f = new File(target);
        template.save(f);
    }
    
    public void buscarPlantilla(){
        
        String nombre="", ruta="";
        
        JFileChooser file=new JFileChooser();
        file.showOpenDialog(this);
        File guarda =file.getSelectedFile();
        this.guarda = guarda;
        if(guarda !=null)
        {
            nombre=file.getSelectedFile().getName();
            ruta    = file.getSelectedFile().getPath();                        
            
            System.out.println("- Cargo el documento plantilla :"+guarda);
            this.file= nombre ;
            this.filePath = ruta ; System.out.println("LA ruta="+ruta);
            
            jTextField35.setText(nombre);
            
        }
       
        
        
    }
 
}
