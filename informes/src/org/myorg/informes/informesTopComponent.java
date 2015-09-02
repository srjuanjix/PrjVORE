/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.informes;

import org.myorg.conexion.Conexion;
import org.myorg.dao.TablasDao;
import org.myorg.dao.saepDao;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
// import javafx.scene.control.Cell;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openide.windows.WindowManager;
// import org.osgi.framework.Bundle;


/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.myorg.informes//informes//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "informesTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "org.myorg.informes.informesTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_informesAction",
        preferredID = "INFORMES"
)
@Messages({
    "CTL_informesAction=informes",
    "CTL_informesTopComponent=INFORMES",
    "HINT_informesTopComponent=Gestión de ahorros e informes"
})
public final class informesTopComponent extends TopComponent {

 // ..........................................................
    
    public String clientes[][]                 = new String[500][5];  
    public String listaPuntosSum[][]           = new String[1000][25];  
    public String listaContratosPuntos[][]     = new String[1000][15]; 
    public String listaContratosPuntosAct[][]  = new String[1000][15];
    public String listaCondicionesActuales[][] = new String[1000][15];     
    public String lCondicionesSimulacion[][]   = new String[1000][30]; 
    public String lCondicionesActuales[][]     = new String[1000][30]; 
    
    public String lAhorrosHistorico[][]         = new String[5000][20];
    
    public String listaDatosBase[][]           = new String[500][2];  
    public String listaDatosActual[][]         = new String[500][2];  
    
    public int lhistoricoCalculos[][]           = new int[5000][7];
    
    public static String sMensajes             = "" ;
    // .......................................................... 
//    public FramePrincipal miVentanaPrincipal;    
    // ..........................................................
    
    public int nClientes=0;
    public int nPuntos=0;
    public int nPuntosCalAhorro=0;
    public int nPuntosCalAhorroDetalle=0;
    public int nPuntosAlertaFinServicio=0;
    
    // ..........................................................
    
    public int indGen = 0 ;
    public int tipo_Act, tipo_Sim ;
    public int id_punto_actual;
    public int id_cliente_actual=0;
    public double ahorro_total_actual;
    public double pAhorro=0;
    public int nCalculosPunto ;
    public int id_tipo_Actual, id_tipo_Actual_Anterior;
    public int id_tipo_Sim, id_tipo_Sim_Anterior;
    public int indiceCalculo=0 ;
    
    
    private JDialog ventanaSecundaria;
    
    public String lTiposTarifas[] = {"","Tarifa 2.0A","Tarifa 2.0DH","Tarifa 2.1A","Tarifa 2.1DH","Tarifa 3.0A","Tarifa 3.1A","Tarifa 6.1A","Tarifa 2.0DH INDX","Tarifa 2.1DH INDX","Tarifa 3.0A INDX","Tarifa 2.0 INDX","Tarifa 2.1 INDX","Tarifa 3.1 INDX","TARIFA 6.1 A INDX"} ;
        
    // ..........................................................
    
    public int ftipoMedida=0;
    public int fCT=0;
    public int id_cliente_general=0 ;
    
    // ..........................................................
    
    public int filtrobusca= 0;
    public String FechaUltimoCalculo="06-02-2015";
    public int duracionServicioDias = 365 ;
    // ..........................................................
    
    public int fReglaPotenciaActual     = 1 ;
    public int fReglaPotenciaSimulado   = 1 ;
    public int fPotenciaFacturada       = 0 ;
    public int fEnergiaSimulada         = 0 ;
    
    // ..........................................................    

        
    public informeSimpleFactura misDatos;
    
    // ..........................................................
    
    private static informesTopComponent instance;
    private static final String PREFERRED_ID = "informesTopComponent"; 
    
    // ..........................................................
    
    public informesTopComponent() {
        initComponents();
        setName(Bundle.CTL_informesTopComponent());
        setToolTipText(Bundle.HINT_informesTopComponent());
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        
        crearArbol();
        
        jTextField41.setVisible(false);
        jTextField42.setVisible(false);
        jTextField43.setVisible(false);
        jTextField44.setVisible(false);
        jTextField45.setVisible(false);
        jTextField46.setVisible(false);
        
        botonValidarCAct.setVisible(false);
        botonValidarSim.setVisible(false);
        
        jTextField32.setVisible(false);
        jLabel53.setVisible(false);
        
        this.jPanel26.setVisible(true); this.jPanel50.setVisible(false);
        
        this.misDatos= new informeSimpleFactura();                                  // Clase contenedor de datos para informe simulado factura.

    }
     public static synchronized informesTopComponent getDefault() {
        if (instance == null) {
            instance = new informesTopComponent();
        }
        return instance;
    }

  
    public static synchronized informesTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(informesTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof informesTopComponent) {
            return (informesTopComponent) win;
        }
        Logger.getLogger(informesTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        arbol = new javax.swing.JTree();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel16 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jTextField58 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        jTextField59 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jTextField3 = new javax.swing.JTextField();
        jButton11 = new javax.swing.JButton();
        jTextField264 = new javax.swing.JTextField();
        PDEntrada20A = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jTextField40 = new javax.swing.JTextField();
        jTextField60 = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        botonCalculo = new javax.swing.JButton();
        botonBorrarCampos = new javax.swing.JButton();
        botonDetalles = new javax.swing.JButton();
        botonValidar = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jTextField65 = new javax.swing.JTextField();
        jTextField66 = new javax.swing.JTextField();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jTextField68 = new javax.swing.JTextField();
        jTextField67 = new javax.swing.JTextField();
        jTextField69 = new javax.swing.JTextField();
        jTextField70 = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jLabel95 = new javax.swing.JLabel();
        jTextField72 = new javax.swing.JTextField();
        jTextField71 = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jTextField77 = new javax.swing.JTextField();
        jTextField78 = new javax.swing.JTextField();
        jTextField79 = new javax.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jLabel110 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jPanel28 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField47 = new javax.swing.JTextField();
        jTextField82 = new javax.swing.JTextField();
        jLabel114 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        jTextField44 = new javax.swing.JTextField();
        jTextField41 = new javax.swing.JTextField();
        jTextField42 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jTextField46 = new javax.swing.JTextField();
        jTextField45 = new javax.swing.JTextField();
        jPanel31 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jTextField80 = new javax.swing.JTextField();
        jLabel111 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jLabel144 = new javax.swing.JLabel();
        jTextField106 = new javax.swing.JTextField();
        jTextField107 = new javax.swing.JTextField();
        jTextField108 = new javax.swing.JTextField();
        jLabel156 = new javax.swing.JLabel();
        jTextField113 = new javax.swing.JTextField();
        jTextField114 = new javax.swing.JTextField();
        jTextField115 = new javax.swing.JTextField();
        jLabel153 = new javax.swing.JLabel();
        jPanel45 = new javax.swing.JPanel();
        jLabel145 = new javax.swing.JLabel();
        jTextField109 = new javax.swing.JTextField();
        jLabel146 = new javax.swing.JLabel();
        jTextField110 = new javax.swing.JTextField();
        jLabel147 = new javax.swing.JLabel();
        jLabel148 = new javax.swing.JLabel();
        jPanel48 = new javax.swing.JPanel();
        jLabel161 = new javax.swing.JLabel();
        jTextField121 = new javax.swing.JTextField();
        jLabel162 = new javax.swing.JLabel();
        jTextField122 = new javax.swing.JTextField();
        jLabel163 = new javax.swing.JLabel();
        jLabel164 = new javax.swing.JLabel();
        jTextField123 = new javax.swing.JTextField();
        jPanel49 = new javax.swing.JPanel();
        jLabel112 = new javax.swing.JLabel();
        jTextField81 = new javax.swing.JTextField();
        jLabel113 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jTextField85 = new javax.swing.JTextField();
        jLabel118 = new javax.swing.JLabel();
        jTextField126 = new javax.swing.JTextField();
        jLabel172 = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        jTextField39 = new javax.swing.JTextField();
        jComboBox8 = new javax.swing.JComboBox();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jPanel26 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        energiaP1 = new javax.swing.JTextField();
        energiaP2 = new javax.swing.JTextField();
        energiaP3 = new javax.swing.JTextField();
        jTextField75 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel165 = new javax.swing.JLabel();
        jLabel166 = new javax.swing.JLabel();
        energiaP1s = new javax.swing.JTextField();
        energiaP2s = new javax.swing.JTextField();
        energiaP3s = new javax.swing.JTextField();
        jTextField124 = new javax.swing.JTextField();
        jLabel167 = new javax.swing.JLabel();
        jLabel168 = new javax.swing.JLabel();
        jLabel169 = new javax.swing.JLabel();
        jLabel170 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jTextField29 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        botonValidarSim = new javax.swing.JButton();
        jComboBox4 = new javax.swing.JComboBox();
        jPanel11 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jLabel99 = new javax.swing.JLabel();
        jTextField74 = new javax.swing.JTextField();
        jLabel98 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jTextField37 = new javax.swing.JTextField();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jTextField38 = new javax.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jLabel150 = new javax.swing.JLabel();
        jTextField112 = new javax.swing.JTextField();
        jLabel151 = new javax.swing.JLabel();
        jCheckBox6 = new javax.swing.JCheckBox();
        jPanel32 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jTextField20 = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jTextField22 = new javax.swing.JTextField();
        jPanel33 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jTextField24 = new javax.swing.JTextField();
        jTextField25 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTabbedPane7 = new javax.swing.JTabbedPane();
        jPanel46 = new javax.swing.JPanel();
        tipoMedida = new javax.swing.JComboBox();
        tipoSuministro = new javax.swing.JComboBox();
        jLabel83 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField62 = new javax.swing.JTextField();
        jLabel88 = new javax.swing.JLabel();
        jTextField61 = new javax.swing.JTextField();
        jLabel84 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jLabel154 = new javax.swing.JLabel();
        jTextField116 = new javax.swing.JTextField();
        jLabel155 = new javax.swing.JLabel();
        jTextField117 = new javax.swing.JTextField();
        jLabel157 = new javax.swing.JLabel();
        jTextField118 = new javax.swing.JTextField();
        jLabel158 = new javax.swing.JLabel();
        jTextField119 = new javax.swing.JTextField();
        jLabel159 = new javax.swing.JLabel();
        jTextField120 = new javax.swing.JTextField();
        jLabel160 = new javax.swing.JLabel();
        jTabbedPane8 = new javax.swing.JTabbedPane();
        miBarra01 = new javax.swing.JScrollPane();
        miTabla01 = new javax.swing.JTable();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jTabbedPane11 = new javax.swing.JTabbedPane();
        PCContrato = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jTextField28 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jTextField33 = new javax.swing.JTextField();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jTextField35 = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jTextField36 = new javax.swing.JTextField();
        jLabel60 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        botonValidarCAct = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel96 = new javax.swing.JLabel();
        jTextField73 = new javax.swing.JTextField();
        jLabel97 = new javax.swing.JLabel();
        jLabel149 = new javax.swing.JLabel();
        jTextField111 = new javax.swing.JTextField();
        jLabel152 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jPanel72 = new javax.swing.JPanel();
        jLabel238 = new javax.swing.JLabel();
        botonValidarCAct1 = new javax.swing.JButton();
        jComboBox11 = new javax.swing.JComboBox();
        jTextField243 = new javax.swing.JTextField();
        jLabel239 = new javax.swing.JLabel();
        jLabel240 = new javax.swing.JLabel();
        jTextField244 = new javax.swing.JTextField();
        jLabel241 = new javax.swing.JLabel();
        jTextField245 = new javax.swing.JTextField();
        jLabel242 = new javax.swing.JLabel();
        jTextField246 = new javax.swing.JTextField();
        jTextField247 = new javax.swing.JTextField();
        jLabel244 = new javax.swing.JLabel();
        jTextField248 = new javax.swing.JTextField();
        jLabel245 = new javax.swing.JLabel();
        jTextField249 = new javax.swing.JTextField();
        jTextField250 = new javax.swing.JTextField();
        jTextField251 = new javax.swing.JTextField();
        jLabel248 = new javax.swing.JLabel();
        jLabel249 = new javax.swing.JLabel();
        jLabel250 = new javax.swing.JLabel();
        jLabel251 = new javax.swing.JLabel();
        jLabel252 = new javax.swing.JLabel();
        jLabel253 = new javax.swing.JLabel();
        jTextField252 = new javax.swing.JTextField();
        jTextField253 = new javax.swing.JTextField();
        jTextField254 = new javax.swing.JTextField();
        jTextField255 = new javax.swing.JTextField();
        jTextField256 = new javax.swing.JTextField();
        jTextField257 = new javax.swing.JTextField();
        jTextField258 = new javax.swing.JTextField();
        jTextField259 = new javax.swing.JTextField();
        jTextField260 = new javax.swing.JTextField();
        jTextField261 = new javax.swing.JTextField();
        jTextField262 = new javax.swing.JTextField();
        jTextField263 = new javax.swing.JTextField();
        jLabel243 = new javax.swing.JLabel();
        jTextField265 = new javax.swing.JTextField();
        jLabel246 = new javax.swing.JLabel();
        jLabel247 = new javax.swing.JLabel();
        jTextField266 = new javax.swing.JTextField();
        jLabel254 = new javax.swing.JLabel();
        jCheckBox7 = new javax.swing.JCheckBox();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTextArea5 = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        miBarra03 = new javax.swing.JScrollPane();
        miTabla03 = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        miBarra04 = new javax.swing.JScrollPane();
        miTabla04 = new javax.swing.JTable();
        jLabel52 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        botonActualizaConsultaAnexo = new javax.swing.JButton();
        jTextField32 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        botonGenerarExel = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jTextField63 = new javax.swing.JTextField();
        jLabel89 = new javax.swing.JLabel();
        jTextField64 = new javax.swing.JTextField();
        jTextField76 = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel9 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        arbol02 = new javax.swing.JTree();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablaC01 = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablaC02 = new javax.swing.JTable();
        jLabel76 = new javax.swing.JLabel();
        jTextField56 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jTextField57 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel21 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tablaC03 = new javax.swing.JTable();
        jLabel78 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tablaC04 = new javax.swing.JTable();
        jLabel79 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jTextField31 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        botonActualizar = new javax.swing.JButton();
        miBarra02 = new javax.swing.JScrollPane();
        miTabla02 = new javax.swing.JTable();
        jButton18 = new javax.swing.JButton();
        jPanel35 = new javax.swing.JPanel();
        listaClientes = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));
        jPanel2.setMaximumSize(new java.awt.Dimension(240, 322));

        jScrollPane2.setViewportView(arbol);

        jTabbedPane1.setMaximumSize(new java.awt.Dimension(1500, 900));
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1500, 892));

        jPanel4.setPreferredSize(new java.awt.Dimension(1600, 864));

        jPanel8.setBackground(new java.awt.Color(204, 204, 204));
        jPanel8.setPreferredSize(new java.awt.Dimension(1400, 120));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel3.text")); // NOI18N

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel4.text")); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane3.setViewportView(jTextArea1);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel16, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel16.text")); // NOI18N

        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel81, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel81.text")); // NOI18N

        jTextField59.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField59.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox1, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jCheckBox1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton11, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton11.text")); // NOI18N
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTextField264.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField264.text")); // NOI18N

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField59, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton11))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(62, 62, 62))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel8Layout.createSequentialGroup()
                                        .addComponent(jTextField1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(jTextField264, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15)
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField58, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField264, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jCheckBox1)
                                .addGap(14, 14, 14)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel16)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton11)
                                    .addComponent(jLabel81)
                                    .addComponent(jTextField59, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton5))
                                .addComponent(jLabel3)))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        PDEntrada20A.setMaximumSize(new java.awt.Dimension(530, 370));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel7, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel7.text")); // NOI18N

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel14, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel14.text")); // NOI18N

        jTextField9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jTextField9.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField9.text")); // NOI18N

        jLabel80.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel80, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel80.text")); // NOI18N

        jTextField60.setBackground(new java.awt.Color(204, 255, 204));
        jTextField60.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField60.text")); // NOI18N
        jTextField60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField60ActionPerformed(evt);
            }
        });

        jPanel24.setBackground(new java.awt.Color(204, 204, 204));

        org.openide.awt.Mnemonics.setLocalizedText(botonCalculo, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonCalculo.text")); // NOI18N
        botonCalculo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCalculoActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(botonBorrarCampos, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonBorrarCampos.text")); // NOI18N
        botonBorrarCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonBorrarCamposActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(botonDetalles, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonDetalles.text")); // NOI18N
        botonDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDetallesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(botonValidar, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonValidar.text")); // NOI18N
        botonValidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonValidarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCalculo, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonBorrarCampos, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonValidar, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(botonCalculo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonBorrarCampos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonDetalles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonValidar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextField65.setBackground(new java.awt.Color(255, 255, 204));
        jTextField65.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jTextField66.setBackground(new java.awt.Color(255, 255, 204));
        jTextField66.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel92, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel92.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel93, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel93.text")); // NOI18N

        jTextField68.setBackground(new java.awt.Color(255, 255, 204));
        jTextField68.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jTextField67.setBackground(new java.awt.Color(255, 255, 204));
        jTextField67.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jTextField69.setBackground(new java.awt.Color(255, 255, 204));
        jTextField69.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jTextField70.setBackground(new java.awt.Color(255, 255, 204));
        jTextField70.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel94, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel94.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel95, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel95.text")); // NOI18N

        jTextField72.setBackground(new java.awt.Color(255, 255, 204));
        jTextField72.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jTextField71.setBackground(new java.awt.Color(255, 255, 204));
        jTextField71.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel90.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel90, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel90.text")); // NOI18N

        jLabel91.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel91, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel91.text")); // NOI18N

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jTextField65, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField66, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel92))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField69)
                            .addComponent(jTextField67, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jTextField70)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel94))
                            .addGroup(jPanel25Layout.createSequentialGroup()
                                .addComponent(jTextField68, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel93))))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel90)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel91))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jTextField71, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField72, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel95)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel25Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel90)
                    .addComponent(jLabel91))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField65, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField66, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel92))
                .addGap(10, 10, 10)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField67, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField68, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel93))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField69, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField70, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel94))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField71, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField72, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel95))
                .addGap(36, 36, 36))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabel105, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel105.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel106, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel106.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel107, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel107.text")); // NOI18N

        jTextField77.setBackground(new java.awt.Color(204, 255, 204));

        jTextField78.setBackground(new java.awt.Color(204, 255, 204));

        jTextField79.setBackground(new java.awt.Color(204, 255, 204));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel108, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel108.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel109, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel109.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel110, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel110.text")); // NOI18N

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Potencia de maxímetro", "Potencia facturada" }));
        jComboBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel105)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField77, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel106)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField78))
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel107)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField79)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel108)
                    .addComponent(jLabel109)
                    .addComponent(jLabel110))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel105)
                    .addComponent(jTextField77, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel108))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel106)
                    .addComponent(jTextField78, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel109))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel107)
                    .addComponent(jTextField79, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel110))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel28.setBackground(new java.awt.Color(204, 204, 204));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel50, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel50.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel51, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel51.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel66, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel66.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel69, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel69.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel114, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel114.text")); // NOI18N

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel66, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel51, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                                .addComponent(jLabel50, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                        .addGroup(jPanel28Layout.createSequentialGroup()
                            .addComponent(jLabel69, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGap(6, 6, 6)))
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jLabel114)
                        .addGap(30, 30, 30)))
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField82, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createSequentialGroup()
                        .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel50)
                    .addComponent(jTextField30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField47, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(jTextField27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel69)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel114)
                    .addComponent(jTextField82, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        jTextField41.setEditable(false);

        jTextField46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField46ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField41, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField42, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField43, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField46, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jPanel31.setBackground(new java.awt.Color(204, 204, 204));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel11, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel11.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel12, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel12.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel13, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel13.text")); // NOI18N

        jTextField6.setBackground(new java.awt.Color(204, 255, 204));
        jTextField6.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField6.text")); // NOI18N
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });

        jTextField7.setBackground(new java.awt.Color(204, 255, 204));
        jTextField7.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField7.text")); // NOI18N

        jTextField8.setBackground(new java.awt.Color(204, 255, 204));
        jTextField8.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField8.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jButton14, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton14.text")); // NOI18N
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton15, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton15.text")); // NOI18N
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton16, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton16.text")); // NOI18N
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton17, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton17.text")); // NOI18N
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jLabel46.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel46, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel46.text")); // NOI18N

        jLabel38.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel38, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel38.text")); // NOI18N

        jLabel37.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel37, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel37.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel82, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel82.text")); // NOI18N

        jTextField80.setBackground(new java.awt.Color(204, 255, 204));
        jTextField80.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField80.text")); // NOI18N

        jLabel111.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel111, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel111.text")); // NOI18N

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addComponent(jButton14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton17))
                    .addGroup(jPanel31Layout.createSequentialGroup()
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel82, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel46))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel38))
                            .addGroup(jPanel31Layout.createSequentialGroup()
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField80, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel111, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel31Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel82)
                    .addComponent(jTextField80, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel111))
                .addGap(18, 18, 18)
                .addGroup(jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel44.setBackground(new java.awt.Color(204, 204, 204));

        jLabel144.setFont(new java.awt.Font("Tahoma", 0, 9)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel144, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel144.text")); // NOI18N

        jLabel156.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel156, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel156.text")); // NOI18N

        jTextField113.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField113.text")); // NOI18N

        jLabel153.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel153, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel153.text")); // NOI18N

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel44Layout.createSequentialGroup()
                        .addComponent(jLabel156, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel153)
                        .addGap(31, 31, 31))
                    .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel144)
                        .addGroup(jPanel44Layout.createSequentialGroup()
                            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField108, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                                .addComponent(jTextField107, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField106, javax.swing.GroupLayout.Alignment.LEADING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField113)
                                .addComponent(jTextField114)
                                .addComponent(jTextField115, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel44Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel144)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel156)
                    .addComponent(jLabel153))
                .addGap(18, 18, 18)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField106, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField113, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField107, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField114, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField108, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField115, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel45.setBackground(new java.awt.Color(204, 204, 255));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel145, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel145.text")); // NOI18N

        jTextField109.setBackground(new java.awt.Color(204, 255, 204));
        jTextField109.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField109.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel146, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel146.text")); // NOI18N

        jTextField110.setBackground(new java.awt.Color(204, 255, 204));
        jTextField110.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField110.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel147, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel147.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel148, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel148.text")); // NOI18N

        javax.swing.GroupLayout jPanel45Layout = new javax.swing.GroupLayout(jPanel45);
        jPanel45.setLayout(jPanel45Layout);
        jPanel45Layout.setHorizontalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel145)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTextField110, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addComponent(jTextField109, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel146))
                    .addGroup(jPanel45Layout.createSequentialGroup()
                        .addComponent(jLabel147)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addComponent(jLabel148)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel45Layout.setVerticalGroup(
            jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel45Layout.createSequentialGroup()
                .addComponent(jLabel145)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField109, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel146))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel45Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField110, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel147))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel148)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel48.setBackground(new java.awt.Color(204, 204, 255));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel161, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel161.text")); // NOI18N

        jTextField121.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField121.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel162, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel162.text")); // NOI18N

        jTextField122.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField122.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel163, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel163.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel164, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel164.text")); // NOI18N

        jTextField123.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField123.text")); // NOI18N

        javax.swing.GroupLayout jPanel48Layout = new javax.swing.GroupLayout(jPanel48);
        jPanel48.setLayout(jPanel48Layout);
        jPanel48Layout.setHorizontalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel48Layout.createSequentialGroup()
                        .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel161))
                            .addGroup(jPanel48Layout.createSequentialGroup()
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField122, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                                    .addComponent(jTextField121))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel162)
                                    .addComponent(jLabel163)))
                            .addComponent(jLabel164))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTextField123))
                .addContainerGap())
        );
        jPanel48Layout.setVerticalGroup(
            jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel48Layout.createSequentialGroup()
                .addComponent(jLabel161)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField121, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel162))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel48Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField122, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel163))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel164)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField123, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(jLabel112, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel112.text")); // NOI18N

        jTextField81.setBackground(new java.awt.Color(204, 255, 204));
        jTextField81.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField81.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel113, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel113.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel117, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel117.text")); // NOI18N

        jTextField85.setBackground(new java.awt.Color(204, 255, 204));
        jTextField85.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField85.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel118, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel118.text")); // NOI18N

        jTextField126.setBackground(new java.awt.Color(204, 255, 204));
        jTextField126.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField126.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel172, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel172.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel173, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel173.text")); // NOI18N

        javax.swing.GroupLayout jPanel49Layout = new javax.swing.GroupLayout(jPanel49);
        jPanel49.setLayout(jPanel49Layout);
        jPanel49Layout.setHorizontalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField85, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTextField81, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                .addComponent(jLabel173)
                                .addComponent(jTextField126)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel113, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel118, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addComponent(jLabel172)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel49Layout.createSequentialGroup()
                        .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel117)
                            .addGroup(jPanel49Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel112)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel49Layout.setVerticalGroup(
            jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel49Layout.createSequentialGroup()
                .addComponent(jLabel112)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel113)
                    .addComponent(jTextField81))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel173)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField126, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel172))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel117)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel49Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel118)
                    .addComponent(jTextField85, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ENERGIA FACTURADA", "ENERGÍA SIMULADA" }));
        jComboBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox8ActionPerformed(evt);
            }
        });

        jLayeredPane1.setBackground(new java.awt.Color(255, 204, 255));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel6.text")); // NOI18N

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel21, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel21.text")); // NOI18N

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel25, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel25.text")); // NOI18N

        jLabel100.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel100, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel100.text")); // NOI18N

        energiaP1.setBackground(new java.awt.Color(204, 255, 204));
        energiaP1.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.energiaP1.text")); // NOI18N

        energiaP2.setBackground(new java.awt.Color(204, 255, 204));

        energiaP3.setBackground(new java.awt.Color(204, 255, 204));

        jTextField75.setMinimumSize(new java.awt.Dimension(84, 20));

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel8, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel8.text")); // NOI18N

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel26, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel26.text")); // NOI18N

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel27, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel27.text")); // NOI18N

        jLabel101.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel101, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel101.text")); // NOI18N

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel100, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField75, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(energiaP3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel101)
                    .addComponent(jLabel27))
                .addGap(62, 62, 62))
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(energiaP1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(energiaP2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel26))
                .addGap(64, 64, 64))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(energiaP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(energiaP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(energiaP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField75, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel101))
                    .addComponent(jLabel100))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel50.setBackground(new java.awt.Color(204, 204, 255));

        jLabel103.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel103, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel103.text")); // NOI18N

        jLabel104.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel104, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel104.text")); // NOI18N

        jLabel165.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel165, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel165.text")); // NOI18N

        jLabel166.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel166, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel166.text")); // NOI18N

        energiaP1s.setBackground(new java.awt.Color(204, 255, 204));
        energiaP1s.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.energiaP1s.text")); // NOI18N

        energiaP2s.setBackground(new java.awt.Color(204, 255, 204));

        energiaP3s.setBackground(new java.awt.Color(204, 255, 204));

        jTextField124.setMinimumSize(new java.awt.Dimension(84, 20));

        jLabel167.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel167, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel167.text")); // NOI18N

        jLabel168.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel168, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel168.text")); // NOI18N

        jLabel169.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel169, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel169.text")); // NOI18N

        jLabel170.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel170, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel170.text")); // NOI18N

        javax.swing.GroupLayout jPanel50Layout = new javax.swing.GroupLayout(jPanel50);
        jPanel50.setLayout(jPanel50Layout);
        jPanel50Layout.setHorizontalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel50Layout.createSequentialGroup()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel166, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel165, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(jTextField124, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel170))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(energiaP3s, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel169))))
                    .addGroup(jPanel50Layout.createSequentialGroup()
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel104, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel103, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(energiaP1s, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel167))
                            .addGroup(jPanel50Layout.createSequentialGroup()
                                .addComponent(energiaP2s, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel168)))))
                .addGap(72, 72, 72))
        );
        jPanel50Layout.setVerticalGroup(
            jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel50Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel103)
                    .addComponent(energiaP1s, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel167))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel104)
                    .addComponent(energiaP2s, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel168))
                .addGap(4, 4, 4)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel165, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(energiaP3s, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel169))
                .addGap(4, 4, 4)
                .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel50Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField124, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel170))
                    .addComponent(jLabel166))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel50, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jLayeredPane1.setLayer(jPanel26, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanel50, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout PDEntrada20ALayout = new javax.swing.GroupLayout(PDEntrada20A);
        PDEntrada20A.setLayout(PDEntrada20ALayout);
        PDEntrada20ALayout.setHorizontalGroup(
            PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PDEntrada20ALayout.createSequentialGroup()
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jPanel27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel45, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel48, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel49, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addGap(203, 203, 203)
                        .addComponent(jTextField60, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                                .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel80)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel44, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PDEntrada20ALayout.setVerticalGroup(
            PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jTextField60, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel80)))
                            .addGroup(PDEntrada20ALayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jPanel31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel48, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PDEntrada20ALayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PDEntrada20ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel44, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setMaximumSize(new java.awt.Dimension(581, 286));
        jPanel10.setPreferredSize(new java.awt.Dimension(581, 286));

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel10, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel10.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel49, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel49.text")); // NOI18N

        jTextField29.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField29.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel55, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel55.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(botonValidarSim, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonValidarSim.text")); // NOI18N
        botonValidarSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonValidarSimActionPerformed(evt);
            }
        });

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar....", "TARIFA 2.0 A", "TARIFA 2.0 ADH", "TARIFA 2.1 A", "TARIFA 2.1 ADH", "TARIFA 3.0 A", "TARIFA 3.1 A", "TARIFA 6.1 A", "TARIFA 2.0 DHA INDX", "TARIFA 2.1 DHA INDX", "TARIFA 3.0 A INDX", "TARIFA 2.0  INDX", "TARIFA 2.1  INDX", "TARIFA 3.1 A INDX" }));
        jComboBox4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox4MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jComboBox4MousePressed(evt);
            }
        });
        jComboBox4.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox4PopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                            .addComponent(jLabel49, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonValidarSim)
                        .addGap(55, 55, 55))))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(botonValidarSim))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(jTextField34, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(jTextField29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel30.setBackground(new java.awt.Color(204, 204, 204));

        jTextArea3.setBackground(new java.awt.Color(255, 255, 204));
        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane4.setViewportView(jTextArea3);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel99, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel99.text")); // NOI18N

        jTextField74.setBackground(new java.awt.Color(204, 255, 204));
        jTextField74.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField74.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel98, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel98.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel45, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel45.text")); // NOI18N

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel61, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel61.text")); // NOI18N

        jTextField26.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField26.text")); // NOI18N
        jTextField26.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField26KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel47, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel47.text")); // NOI18N

        jLabel62.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel62, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel62.text")); // NOI18N

        jTextField37.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField37.text")); // NOI18N
        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });
        jTextField37.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField37KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel63, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel63.text")); // NOI18N

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel64, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel64.text")); // NOI18N

        jTextField38.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField38.text")); // NOI18N
        jTextField38.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField38KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel65, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel65.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel150, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel150.text")); // NOI18N

        jTextField112.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField112.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel151, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel151.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox6, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jCheckBox6.text")); // NOI18N

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel30Layout.createSequentialGroup()
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel98)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField74, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel99)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel150, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel30Layout.createSequentialGroup()
                                .addComponent(jLabel45)
                                .addGap(10, 10, 10)
                                .addComponent(jLabel61)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel47)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel62)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel63)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel64)
                        .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(jTextField112, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel151))
                            .addGroup(jPanel30Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel65)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox6)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel30Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47)
                    .addComponent(jLabel62)
                    .addComponent(jTextField37, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel63)
                    .addComponent(jLabel64)
                    .addComponent(jTextField38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65)
                    .addComponent(jCheckBox6)
                    .addComponent(jLabel45)
                    .addComponent(jLabel61))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel99)
                    .addComponent(jTextField74, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel98)
                    .addComponent(jLabel150)
                    .addComponent(jTextField112, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel151))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel32.setBackground(new java.awt.Color(204, 204, 204));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel31, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel31.text")); // NOI18N

        jTextField20.setBackground(new java.awt.Color(255, 255, 204));
        jTextField20.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField20.text")); // NOI18N
        jTextField20.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField20KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel32, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel32.text")); // NOI18N

        jTextField21.setBackground(new java.awt.Color(255, 255, 204));
        jTextField21.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField21.text")); // NOI18N
        jTextField21.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField21KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel33, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel33.text")); // NOI18N

        jTextField22.setBackground(new java.awt.Color(255, 255, 204));
        jTextField22.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField22.text")); // NOI18N
        jTextField22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField22ActionPerformed(evt);
            }
        });
        jTextField22.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField22KeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField21, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jTextField22)))
                    .addGroup(jPanel32Layout.createSequentialGroup()
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel32Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel33.setBackground(new java.awt.Color(204, 204, 204));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel39, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel39.text")); // NOI18N

        jTextField23.setBackground(new java.awt.Color(255, 255, 204));
        jTextField23.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField23.text")); // NOI18N
        jTextField23.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField23KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel40, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel40.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel41, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel41.text")); // NOI18N

        jTextField24.setBackground(new java.awt.Color(255, 255, 204));
        jTextField24.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField24.text")); // NOI18N
        jTextField24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField24ActionPerformed(evt);
            }
        });
        jTextField24.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField24KeyTyped(evt);
            }
        });

        jTextField25.setBackground(new java.awt.Color(255, 255, 204));
        jTextField25.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField25.text")); // NOI18N
        jTextField25.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField25KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel42, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel42.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel43, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel43.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel44, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel44.text")); // NOI18N

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel41, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel42))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel43))
                    .addGroup(jPanel33Layout.createSequentialGroup()
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel44)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel33Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel44))
                    .addComponent(jLabel41))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane7.setBackground(new java.awt.Color(204, 204, 204));

        tipoMedida.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MEDIDA DIRECTA", "MEDIDA INDIRECTA" }));
        tipoMedida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoMedidaActionPerformed(evt);
            }
        });

        tipoSuministro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "CON CT", "SIN CT" }));
        tipoSuministro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoSuministroActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel83, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel83.text")); // NOI18N

        jTextField4.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField4.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel5.text")); // NOI18N

        jTextField62.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField62.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel88, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel88.text")); // NOI18N

        jTextField61.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField61.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel84, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel84.text")); // NOI18N

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(tipoMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(tipoSuministro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel83)
                    .addGroup(jPanel46Layout.createSequentialGroup()
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField62, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel88)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField61, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel84)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel46Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tipoMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tipoSuministro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel83)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField61, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel84)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField62, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel88))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel46.TabConstraints.tabTitle"), jPanel46); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel154, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel154.text")); // NOI18N

        jTextField116.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField116.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel155, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel155.text")); // NOI18N

        jTextField117.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField117.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel157, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel157.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel158, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel158.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel159, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel159.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel160, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel160.text")); // NOI18N

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel157)
                            .addComponent(jLabel154))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addComponent(jTextField116, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel155)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField117, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel47Layout.createSequentialGroup()
                                .addComponent(jTextField118, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel159)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField120, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel160))))
                    .addGroup(jPanel47Layout.createSequentialGroup()
                        .addComponent(jLabel158)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField119, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel47Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel154)
                    .addComponent(jTextField116, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel155)
                    .addComponent(jTextField117, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel157)
                    .addComponent(jTextField118, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel159)
                    .addComponent(jTextField120, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel160))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel158)
                    .addComponent(jTextField119, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane7.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel47.TabConstraints.tabTitle"), jPanel47); // NOI18N

        miBarra01.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        miBarra01.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        miBarra01.setAutoscrolls(true);

        miTabla01.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        miBarra01.setViewportView(miTabla01);

        jTabbedPane8.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.miBarra01.TabConstraints.tabTitle"), miBarra01); // NOI18N

        jScrollPane13.setViewportView(jTextPane1);

        jTabbedPane8.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jScrollPane13.TabConstraints.tabTitle"), jScrollPane13); // NOI18N

        PCContrato.setBackground(new java.awt.Color(204, 204, 255));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel9, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel9.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel17, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel17.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel18, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel18.text")); // NOI18N

        jTextField12.setBackground(new java.awt.Color(204, 255, 204));
        jTextField12.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField12.text")); // NOI18N
        jTextField12.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField12InputMethodTextChanged(evt);
            }
        });
        jTextField12.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField12KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel19, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel19.text")); // NOI18N

        jTextField13.setBackground(new java.awt.Color(204, 255, 204));
        jTextField13.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField13.text")); // NOI18N
        jTextField13.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField13KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel20, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel20.text")); // NOI18N

        jTextField14.setBackground(new java.awt.Color(204, 255, 204));
        jTextField14.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField14.text")); // NOI18N
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jTextField14.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField14KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel22, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel22.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel23, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel23.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel24, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel24.text")); // NOI18N

        jTextField16.setBackground(new java.awt.Color(204, 255, 204));
        jTextField16.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField16.text")); // NOI18N
        jTextField16.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField16KeyTyped(evt);
            }
        });

        jTextField17.setBackground(new java.awt.Color(204, 255, 204));
        jTextField17.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField17.text")); // NOI18N
        jTextField17.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField17KeyTyped(evt);
            }
        });

        jTextField18.setBackground(new java.awt.Color(204, 255, 204));
        jTextField18.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField18.text")); // NOI18N
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });
        jTextField18.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField18KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel28, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel28.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel29, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel29.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel30, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel30.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel34, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel34.text")); // NOI18N

        jTextField19.setBackground(new java.awt.Color(204, 255, 204));
        jTextField19.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField19.text")); // NOI18N
        jTextField19.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField19KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel35, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel35.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel48, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel48.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel54, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel54.text")); // NOI18N

        jLabel56.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel56, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel56.text")); // NOI18N

        jLabel57.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel57, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel57.text")); // NOI18N

        jTextField35.setBackground(new java.awt.Color(204, 255, 204));
        jTextField35.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField35.text")); // NOI18N
        jTextField35.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField35KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel58, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel58.text")); // NOI18N

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel59, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel59.text")); // NOI18N

        jTextField36.setBackground(new java.awt.Color(204, 255, 204));
        jTextField36.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField36.text")); // NOI18N
        jTextField36.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField36KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel60, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel60.text")); // NOI18N

        jTextArea2.setBackground(new java.awt.Color(255, 255, 204));
        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane5.setViewportView(jTextArea2);

        org.openide.awt.Mnemonics.setLocalizedText(botonValidarCAct, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonValidarCAct.text")); // NOI18N
        botonValidarCAct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonValidarCActActionPerformed(evt);
            }
        });

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar....", "TARIFA 2.0 A", "TARIFA 2.0 ADH", "TARIFA 2.1 A", "TARIFA 2.1 ADH", "TARIFA 3.0 A", "TARIFA 3.1 A", "TARIFA 6.1 A", "TARIFA 2.0 DHA INDX", "TARIFA 2.1 DHA INDX", "TARIFA 3.0 A INDX", "TARIFA 2.0  INDX", "TARIFA 2.1  INDX", "TARIFA 3.1 A INDX", "TARIFA 6.1 A INDX" }));
        jComboBox3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox3MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jComboBox3MousePressed(evt);
            }
        });
        jComboBox3.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox3PopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBox3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox3ItemStateChanged(evt);
            }
        });
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });
        jComboBox3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox3FocusGained(evt);
            }
        });
        jComboBox3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox3PropertyChange(evt);
            }
        });
        jComboBox3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jComboBox3KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel96, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel96.text")); // NOI18N

        jTextField73.setBackground(new java.awt.Color(204, 255, 204));
        jTextField73.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField73.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel97, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel97.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel149, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel149.text")); // NOI18N

        jTextField111.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField111.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel152, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel152.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox5, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jCheckBox5.text")); // NOI18N

        javax.swing.GroupLayout PCContratoLayout = new javax.swing.GroupLayout(PCContrato);
        PCContrato.setLayout(PCContratoLayout);
        PCContratoLayout.setHorizontalGroup(
            PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PCContratoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PCContratoLayout.createSequentialGroup()
                        .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PCContratoLayout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addGap(24, 24, 24)
                                .addComponent(jLabel56)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel35)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel57)
                                .addGap(6, 6, 6)
                                .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel58))
                            .addGroup(PCContratoLayout.createSequentialGroup()
                                .addComponent(jLabel96)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField73, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel97)
                                .addGap(48, 48, 48)
                                .addComponent(jLabel149)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PCContratoLayout.createSequentialGroup()
                                .addComponent(jTextField111, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel152))
                            .addGroup(PCContratoLayout.createSequentialGroup()
                                .addComponent(jLabel59)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel60)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox5))))
                    .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(botonValidarCAct)
                        .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(PCContratoLayout.createSequentialGroup()
                                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PCContratoLayout.createSequentialGroup()
                                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PCContratoLayout.createSequentialGroup()
                                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel24)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PCContratoLayout.createSequentialGroup()
                                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel23)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PCContratoLayout.createSequentialGroup()
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel22)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PCContratoLayout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel28)
                                    .addComponent(jLabel30))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PCContratoLayout.setVerticalGroup(
            PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PCContratoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(botonValidarCAct))
                .addGap(4, 4, 4)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jTextField33, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48)
                    .addComponent(jTextField28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23)
                    .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24)
                    .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57)
                    .addComponent(jTextField35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58)
                    .addComponent(jLabel59)
                    .addComponent(jTextField36, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel60)
                    .addComponent(jCheckBox5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PCContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel96)
                    .addComponent(jTextField73, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel97)
                    .addComponent(jLabel149)
                    .addComponent(jTextField111, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel152))
                .addGap(6, 6, 6)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jTabbedPane11.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.PCContrato.TabConstraints.tabTitle"), PCContrato); // NOI18N

        jLabel238.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel238, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel238.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(botonValidarCAct1, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonValidarCAct1.text")); // NOI18N
        botonValidarCAct1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonValidarCAct1ActionPerformed(evt);
            }
        });

        jComboBox11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jComboBox11.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Seleccionar....", "TARIFA 2.0 A", "TARIFA 2.0 ADH", "TARIFA 2.1 A", "TARIFA 2.1 ADH", "TARIFA 3.0 A", "TARIFA 3.1 A", "TARIFA 6.1 A", "TARIFA 2.0 DHA INDX", "TARIFA 2.1 DHA INDX", "TARIFA 3.0 A INDX", "TARIFA 2.0  INDX", "TARIFA 2.1  INDX", "TARIFA 3.1 A INDX", "TARIFA 6.1 A INDX" }));
        jComboBox11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jComboBox11MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jComboBox11MousePressed(evt);
            }
        });
        jComboBox11.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
            }
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
                jComboBox11PopupMenuWillBecomeVisible(evt);
            }
        });
        jComboBox11.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox11ItemStateChanged(evt);
            }
        });
        jComboBox11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox11ActionPerformed(evt);
            }
        });
        jComboBox11.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jComboBox11FocusGained(evt);
            }
        });
        jComboBox11.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jComboBox11PropertyChange(evt);
            }
        });
        jComboBox11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jComboBox11KeyTyped(evt);
            }
        });

        jTextField243.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel239.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel239, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel239.text")); // NOI18N

        jLabel240.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel240, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel240.text")); // NOI18N

        jTextField244.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel241.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel241, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel241.text")); // NOI18N

        jTextField245.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        jLabel242.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel242, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel242.text")); // NOI18N

        jTextField246.setBackground(new java.awt.Color(204, 255, 204));
        jTextField246.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField246.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField246.text")); // NOI18N
        jTextField246.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField246InputMethodTextChanged(evt);
            }
        });
        jTextField246.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField246KeyTyped(evt);
            }
        });

        jTextField247.setBackground(new java.awt.Color(204, 255, 204));
        jTextField247.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField247.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField247.text")); // NOI18N
        jTextField247.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField247KeyTyped(evt);
            }
        });

        jLabel244.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel244, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel244.text")); // NOI18N

        jTextField248.setBackground(new java.awt.Color(204, 255, 204));
        jTextField248.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField248.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField248.text")); // NOI18N
        jTextField248.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField248ActionPerformed(evt);
            }
        });
        jTextField248.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField248KeyTyped(evt);
            }
        });

        jLabel245.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel245, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel245.text")); // NOI18N

        jTextField249.setBackground(new java.awt.Color(204, 255, 204));
        jTextField249.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField249.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField249.text")); // NOI18N
        jTextField249.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField249InputMethodTextChanged(evt);
            }
        });
        jTextField249.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField249KeyTyped(evt);
            }
        });

        jTextField250.setBackground(new java.awt.Color(204, 255, 204));
        jTextField250.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField250.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField250.text")); // NOI18N
        jTextField250.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField250KeyTyped(evt);
            }
        });

        jTextField251.setBackground(new java.awt.Color(204, 255, 204));
        jTextField251.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField251.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField251.text")); // NOI18N
        jTextField251.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField251ActionPerformed(evt);
            }
        });
        jTextField251.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField251KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel248, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel248.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel249, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel249.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel250, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel250.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel251, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel251.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel252, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel252.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel253, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel253.text")); // NOI18N

        jTextField252.setBackground(new java.awt.Color(204, 255, 204));
        jTextField252.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField252.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField252.text")); // NOI18N
        jTextField252.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField252InputMethodTextChanged(evt);
            }
        });
        jTextField252.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField252KeyTyped(evt);
            }
        });

        jTextField253.setBackground(new java.awt.Color(204, 255, 204));
        jTextField253.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField253.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField253.text")); // NOI18N
        jTextField253.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField253KeyTyped(evt);
            }
        });

        jTextField254.setBackground(new java.awt.Color(204, 255, 204));
        jTextField254.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField254.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField254.text")); // NOI18N
        jTextField254.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField254ActionPerformed(evt);
            }
        });
        jTextField254.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField254KeyTyped(evt);
            }
        });

        jTextField255.setBackground(new java.awt.Color(204, 255, 204));
        jTextField255.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField255.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField255.text")); // NOI18N
        jTextField255.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField255InputMethodTextChanged(evt);
            }
        });
        jTextField255.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField255KeyTyped(evt);
            }
        });

        jTextField256.setBackground(new java.awt.Color(204, 255, 204));
        jTextField256.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField256.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField256.text")); // NOI18N
        jTextField256.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField256KeyTyped(evt);
            }
        });

        jTextField257.setBackground(new java.awt.Color(204, 255, 204));
        jTextField257.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField257.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField257.text")); // NOI18N
        jTextField257.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField257ActionPerformed(evt);
            }
        });
        jTextField257.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField257KeyTyped(evt);
            }
        });

        jTextField258.setBackground(new java.awt.Color(204, 255, 204));
        jTextField258.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField258.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField258.text")); // NOI18N
        jTextField258.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField258InputMethodTextChanged(evt);
            }
        });
        jTextField258.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField258KeyTyped(evt);
            }
        });

        jTextField259.setBackground(new java.awt.Color(204, 255, 204));
        jTextField259.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField259.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField259.text")); // NOI18N
        jTextField259.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField259KeyTyped(evt);
            }
        });

        jTextField260.setBackground(new java.awt.Color(204, 255, 204));
        jTextField260.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField260.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField260.text")); // NOI18N
        jTextField260.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField260ActionPerformed(evt);
            }
        });
        jTextField260.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField260KeyTyped(evt);
            }
        });

        jTextField261.setBackground(new java.awt.Color(204, 255, 204));
        jTextField261.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField261.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField261.text")); // NOI18N
        jTextField261.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                jTextField261InputMethodTextChanged(evt);
            }
        });
        jTextField261.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField261KeyTyped(evt);
            }
        });

        jTextField262.setBackground(new java.awt.Color(204, 255, 204));
        jTextField262.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField262.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField262.text")); // NOI18N
        jTextField262.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField262KeyTyped(evt);
            }
        });

        jTextField263.setBackground(new java.awt.Color(204, 255, 204));
        jTextField263.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jTextField263.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField263.text")); // NOI18N
        jTextField263.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField263ActionPerformed(evt);
            }
        });
        jTextField263.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField263KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel243, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel243.text")); // NOI18N

        jTextField265.setBackground(new java.awt.Color(204, 255, 204));
        jTextField265.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField265.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel246, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel246.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel247, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel247.text")); // NOI18N

        jTextField266.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField266.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel254, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel254.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jCheckBox7, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jCheckBox7.text")); // NOI18N

        jTextArea5.setBackground(new java.awt.Color(255, 255, 204));
        jTextArea5.setColumns(20);
        jTextArea5.setRows(5);
        jScrollPane16.setViewportView(jTextArea5);

        javax.swing.GroupLayout jPanel72Layout = new javax.swing.GroupLayout(jPanel72);
        jPanel72.setLayout(jPanel72Layout);
        jPanel72Layout.setHorizontalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel72Layout.createSequentialGroup()
                        .addGap(0, 5, Short.MAX_VALUE)
                        .addComponent(jLabel242, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField246, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel72Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel240)
                            .addGroup(jPanel72Layout.createSequentialGroup()
                                .addComponent(jLabel248, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel72Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel243)
                            .addGroup(jPanel72Layout.createSequentialGroup()
                                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel245, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE)
                                    .addComponent(jLabel244, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField249, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField258, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel72Layout.createSequentialGroup()
                        .addComponent(jTextField244, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel241, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField245, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel72Layout.createSequentialGroup()
                        .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel72Layout.createSequentialGroup()
                                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                                        .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                                                .addComponent(jTextField247, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField248, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField252, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextField253, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                                                .addComponent(jLabel249, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel250, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel251, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel252, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel72Layout.createSequentialGroup()
                                        .addComponent(jTextField250, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField251, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField255, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField256, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(4, 4, 4)))
                                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField257, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel253, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField254, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel72Layout.createSequentialGroup()
                                    .addComponent(jTextField265, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel246)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel247)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField266, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel254)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jCheckBox7))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel72Layout.createSequentialGroup()
                                    .addComponent(jTextField259, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField260, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField261, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField262, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTextField263, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                .addGap(0, 101, Short.MAX_VALUE)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonValidarCAct1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel72Layout.createSequentialGroup()
                        .addComponent(jLabel239, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField243, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))))
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel72Layout.createSequentialGroup()
                        .addComponent(jLabel238)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel72Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane16)))
                .addContainerGap())
        );
        jPanel72Layout.setVerticalGroup(
            jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel72Layout.createSequentialGroup()
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel238)
                    .addComponent(botonValidarCAct1))
                .addGap(4, 4, 4)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel239)
                    .addComponent(jTextField243, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel240)
                        .addComponent(jTextField244, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel241)
                        .addComponent(jTextField245, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel248)
                    .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel253)
                        .addComponent(jLabel252)
                        .addComponent(jLabel251)
                        .addComponent(jLabel250)
                        .addComponent(jLabel249)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField246, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField247, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField248, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField252, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField253, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField254, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel242))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel244, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField249, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField250, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField251, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField255, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField256, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField257, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel245)
                    .addComponent(jTextField258, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField259, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField260, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField261, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField262, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField263, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel72Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel243)
                    .addComponent(jTextField265, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel247)
                    .addComponent(jLabel246)
                    .addComponent(jTextField266, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel254)
                    .addComponent(jCheckBox7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane11.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel72.TabConstraints.tabTitle"), jPanel72); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 939, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE)
                    .addComponent(PDEntrada20A, javax.swing.GroupLayout.PREFERRED_SIZE, 672, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(459, 459, 459)
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jTabbedPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jTabbedPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel33, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(PDEntrada20A, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        miBarra03.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        miBarra03.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        miBarra03.setAutoscrolls(true);

        miTabla03.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        miBarra03.setViewportView(miTabla03);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(miBarra03, javax.swing.GroupLayout.PREFERRED_SIZE, 1334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(miBarra03, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 66, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel12.TabConstraints.tabTitle"), jPanel12); // NOI18N

        miBarra04.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        miTabla04.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        miBarra04.setViewportView(miTabla04);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(miBarra04, javax.swing.GroupLayout.PREFERRED_SIZE, 1334, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 19, Short.MAX_VALUE))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(miBarra04, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 66, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel13.TabConstraints.tabTitle"), jPanel13); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel52, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel52.text")); // NOI18N

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ULTIMO CÁLCULO", "SELECCIONAR FECHA" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(botonActualizaConsultaAnexo, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonActualizaConsultaAnexo.text")); // NOI18N
        botonActualizaConsultaAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizaConsultaAnexoActionPerformed(evt);
            }
        });

        jLabel53.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel53, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel53.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(botonGenerarExel, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonGenerarExel.text")); // NOI18N
        botonGenerarExel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonGenerarExelActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton12, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton12.text")); // NOI18N
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel89, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel89.text")); // NOI18N

        jTextField76.setText(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jTextField76.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel102, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel102.text")); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel53)
                        .addGap(35, 35, 35)
                        .addComponent(botonActualizaConsultaAnexo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField63, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel89)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField64, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField76, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel102)
                        .addGap(18, 18, 18)
                        .addComponent(botonGenerarExel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1358, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonActualizaConsultaAnexo)
                    .addComponent(jTextField32, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53)
                    .addComponent(botonGenerarExel)
                    .addComponent(jButton12)
                    .addComponent(jTextField63, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel89)
                    .addComponent(jTextField64, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField76, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel102))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 808, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        jPanel18.setBackground(new java.awt.Color(204, 204, 204));

        jScrollPane8.setViewportView(arbol02);

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
        );

        jPanel19.setBackground(new java.awt.Color(204, 204, 204));

        tablaC01.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane10.setViewportView(tablaC01);

        tablaC02.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane11.setViewportView(tablaC02);

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel76, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel76.text")); // NOI18N

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel77, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel77.text")); // NOI18N

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11))
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(170, 170, 170)
                .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel19Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76)
                    .addComponent(jTextField56, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77)
                    .addComponent(jTextField57, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jPanel20.setBackground(new java.awt.Color(153, 153, 153));

        org.openide.awt.Mnemonics.setLocalizedText(jButton9, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton9.text")); // NOI18N
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton13, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton13.text")); // NOI18N
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jButton9)
                .addGap(105, 105, 105)
                .addComponent(jButton13)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel21.setBackground(new java.awt.Color(204, 204, 204));

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1051, Short.MAX_VALUE)
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel22.setBackground(new java.awt.Color(204, 204, 204));

        tablaC03.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(tablaC03);

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel78, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel78.text")); // NOI18N

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(153, 153, 153))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel78)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        jPanel23.setBackground(new java.awt.Color(204, 204, 204));

        tablaC04.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(tablaC04);

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel79, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel79.text")); // NOI18N

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane7)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(235, 235, 235)
                .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel79)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel20, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane5.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel9.TabConstraints.tabTitle"), jPanel9); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel36, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel36.text")); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ULTIMO CÁLCULO", "SELECCIONAR", " " }));

        jLabel15.setForeground(new java.awt.Color(153, 153, 153));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel15, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel15.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(botonActualizar, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.botonActualizar.text")); // NOI18N
        botonActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonActualizarActionPerformed(evt);
            }
        });

        miBarra02.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        miBarra02.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        miTabla02.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        miBarra02.setViewportView(miTabla02);

        org.openide.awt.Mnemonics.setLocalizedText(jButton18, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton18.text")); // NOI18N
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel36)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(botonActualizar)
                .addGap(18, 18, 18)
                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(604, Short.MAX_VALUE))
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addComponent(miBarra02, javax.swing.GroupLayout.PREFERRED_SIZE, 1332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField31, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(botonActualizar)
                    .addComponent(jButton18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(miBarra02, javax.swing.GroupLayout.PREFERRED_SIZE, 718, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel17.TabConstraints.tabTitle"), jPanel17); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 1344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane5)
        );

        jTabbedPane1.addTab(org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        listaClientes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECCIONAR CLIENTE" }));
        listaClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listaClientesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(listaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(listaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(0, 11, Short.MAX_VALUE))
        );

        jLabel87.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel87, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel87.text")); // NOI18N

        jLabel85.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel85, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel85.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel86, org.openide.util.NbBundle.getMessage(informesTopComponent.class, "informesTopComponent.jLabel86.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel87))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel35, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel86))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel87, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 112, Short.MAX_VALUE))))
        );

        jScrollPane1.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        buscarCUPSe();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        int resp=JOptionPane.showConfirmDialog(null,"¿ACTUALIZAR LOS DATOS DEL PUNTO DE SUMINISTRO ?");

        if (JOptionPane.OK_OPTION == resp){
            modificarDatosPuntoSuministro();
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jTextField60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField60ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField60ActionPerformed

    private void botonCalculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCalculoActionPerformed
        calculoPreliminarPunto();
    }//GEN-LAST:event_botonCalculoActionPerformed

    private void botonBorrarCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonBorrarCamposActionPerformed
        jTextField30.setText("");
        jTextField47.setText("");
        jTextField15.setText("");
        jTextField27.setText("");
        jTextField5.setText("");

        energiaP1.setText("0");
        energiaP2.setText("0");
        energiaP3.setText("0");
    }//GEN-LAST:event_botonBorrarCamposActionPerformed

    private void botonDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDetallesActionPerformed

     //   new miJDialogDetallesCalculos(this, true).setVisible(true);
    }//GEN-LAST:event_botonDetallesActionPerformed

    private void botonValidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonValidarActionPerformed
        insertarFilaAhorro();
        actualizarTablaHistoricoPuntos(this.id_punto_actual) ;
    }//GEN-LAST:event_botonValidarActionPerformed

    private void jComboBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox7ActionPerformed
        // .................................... Marcar filtro de tipo de medida
        String str;
        str = jComboBox7.getSelectedItem().toString()  ;
        str = str.trim();
        //  System.out.println("Acabo de capturar tipo de suministro Selecciono="+str);

        this.fPotenciaFacturada = 0 ;
        if (str.equals("Potencia de maxímetro"))                   this.fPotenciaFacturada = 0;
        if (str.equals("Potencia facturada"))                      this.fPotenciaFacturada = 1 ;

        System.out.println("Capturo evento cambio ComboBox, fPotenciaFacturada="+fPotenciaFacturada);
    }//GEN-LAST:event_jComboBox7ActionPerformed

    private void jTextField46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField46ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        jTextField7.setText(jTextField65.getText());
        jTextField8.setText(jTextField66.getText());
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        jTextField7.setText(jTextField67.getText());
        jTextField8.setText(jTextField68.getText());
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        jTextField7.setText(jTextField69.getText());
        jTextField8.setText(jTextField70.getText());
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        jTextField7.setText(jTextField71.getText());
        jTextField8.setText(jTextField72.getText());
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jComboBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox8ActionPerformed
        // .................................... Marcar filtro de tipo de medida
        String str;
        str = jComboBox8.getSelectedItem().toString()  ;
        str = str.trim();
        //  System.out.println("Acabo de capturar tipo de suministro Selecciono="+str);

        this.fPotenciaFacturada = 0 ;
        if (str.equals("ENERGIA FACTURADA"))             {      this.fEnergiaSimulada   = 0;   this.jPanel26.setVisible(true); this.jPanel50.setVisible(false); }
        if (str.equals("ENERGÍA SIMULADA"))              {      this.fEnergiaSimulada   = 1 ;  this.jPanel26.setVisible(false); this.jPanel50.setVisible(true); }

        System.out.println("Capturo evento cambio ComboBox, fEnergiaSimulada="+fEnergiaSimulada);
    }//GEN-LAST:event_jComboBox8ActionPerformed

    private void botonValidarSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonValidarSimActionPerformed
        ValidarCondicionesSimuladas();
    }//GEN-LAST:event_botonValidarSimActionPerformed

    private void jComboBox4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox4MouseClicked

    }//GEN-LAST:event_jComboBox4MouseClicked

    private void jComboBox4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox4MousePressed

    }//GEN-LAST:event_jComboBox4MousePressed

    private void jComboBox4PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox4PopupMenuWillBecomeVisible
        botonValidarSim.setVisible(true);
        this.id_tipo_Sim= jComboBox4.getSelectedIndex();
    }//GEN-LAST:event_jComboBox4PopupMenuWillBecomeVisible

    private void jComboBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox4ActionPerformed
        this.id_tipo_Sim_Anterior = Integer.parseInt(this.listaContratosPuntos[this.indGen][3]);
        this.id_tipo_Sim= jComboBox4.getSelectedIndex();
        if (this.id_tipo_Sim_Anterior != this.id_tipo_Sim ){
            System.out.println("He modificado, ahora this.id_tipo_Sim="+this.id_tipo_Sim+" y el anterior era="+this.id_tipo_Sim_Anterior);
            this.listaContratosPuntos[this.indGen][3] =String.valueOf(this.id_tipo_Sim);
            actualizarFormularios(this.indGen);
        }
    }//GEN-LAST:event_jComboBox4ActionPerformed

    private void jTextField26KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField26KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField26KeyTyped

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37ActionPerformed

    private void jTextField37KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField37KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField37KeyTyped

    private void jTextField38KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField38KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField38KeyTyped

    private void jTextField20KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField20KeyTyped

        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField20KeyTyped

    private void jTextField21KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField21KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField21KeyTyped

    private void jTextField22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField22ActionPerformed

    private void jTextField22KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField22KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField22KeyTyped

    private void jTextField23KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField23KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField23KeyTyped

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24ActionPerformed

    private void jTextField24KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField24KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField24KeyTyped

    private void jTextField25KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField25KeyTyped
        botonValidarSim.setVisible(true);
    }//GEN-LAST:event_jTextField25KeyTyped

    private void tipoMedidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoMedidaActionPerformed
        // .................................... Marcar filtro de tipo de medida
        String str;
        str = tipoMedida.getSelectedItem().toString()  ;
        // System.out.println("Acabo de capturar el tipo de medida!!! Selecciono="+str);

        this.ftipoMedida = 0 ;
        if (str.equals("MEDIDA DIRECTA"))            this.ftipoMedida = 1;
        if (str.equals("MEDIDA INDIRECTA"))          this.ftipoMedida = 2 ;
    }//GEN-LAST:event_tipoMedidaActionPerformed

    private void tipoSuministroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoSuministroActionPerformed
        // .................................... Marcar filtro de tipo de medida
        String str;
        str = tipoMedida.getSelectedItem().toString()  ;
        //  System.out.println("Acabo de capturar tipo de suministro Selecciono="+str);

        this.fCT = 0 ;
        if (str.equals("CON CT"))                   this.fCT = 1;
        if (str.equals("SIN CT"))                   this.fCT = 2 ;
    }//GEN-LAST:event_tipoSuministroActionPerformed

    private void jTextField12InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField12InputMethodTextChanged
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField12InputMethodTextChanged

    private void jTextField12KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField12KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField12KeyTyped

    private void jTextField13KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField13KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField13KeyTyped

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField14KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField14KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField14KeyTyped

    private void jTextField16KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField16KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField16KeyTyped

    private void jTextField17KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField17KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField17KeyTyped

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18ActionPerformed

    private void jTextField18KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField18KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField18KeyTyped

    private void jTextField19KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField19KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField19KeyTyped

    private void jTextField35KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField35KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField35KeyTyped

    private void jTextField36KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField36KeyTyped
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jTextField36KeyTyped

    private void botonValidarCActActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonValidarCActActionPerformed
        System.out.println("condiciones actuales cambian a :"+this.tipo_Act );
        ValidarCondicionesActuales();
    }//GEN-LAST:event_botonValidarCActActionPerformed

    private void jComboBox3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox3MouseClicked

    }//GEN-LAST:event_jComboBox3MouseClicked

    private void jComboBox3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox3MousePressed

    }//GEN-LAST:event_jComboBox3MousePressed

    private void jComboBox3PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox3PopupMenuWillBecomeVisible
        botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jComboBox3PopupMenuWillBecomeVisible

    private void jComboBox3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox3ItemStateChanged
        // botonValidarCAct.setVisible(true);
    }//GEN-LAST:event_jComboBox3ItemStateChanged

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        this.id_tipo_Actual_Anterior = Integer.parseInt(this.listaContratosPuntosAct[this.indGen][3]);
        this.id_tipo_Actual= jComboBox3.getSelectedIndex();
        if (this.id_tipo_Actual_Anterior != this.id_tipo_Actual ) {
            System.out.println("He modificado, ahora this.id_tipo_Actual="+this.id_tipo_Actual+" y el anterior era="+this.id_tipo_Actual_Anterior);
            this.listaContratosPuntosAct[this.indGen][3] = String.valueOf(this.id_tipo_Actual);
            actualizarFormularios(this.indGen);
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    private void jComboBox3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox3FocusGained

    }//GEN-LAST:event_jComboBox3FocusGained

    private void jComboBox3PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox3PropertyChange

    }//GEN-LAST:event_jComboBox3PropertyChange

    private void jComboBox3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox3KeyTyped

    }//GEN-LAST:event_jComboBox3KeyTyped

    private void botonValidarCAct1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonValidarCAct1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonValidarCAct1ActionPerformed

    private void jComboBox11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox11MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11MouseClicked

    private void jComboBox11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBox11MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11MousePressed

    private void jComboBox11PopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {//GEN-FIRST:event_jComboBox11PopupMenuWillBecomeVisible
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11PopupMenuWillBecomeVisible

    private void jComboBox11ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox11ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11ItemStateChanged

    private void jComboBox11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11ActionPerformed

    private void jComboBox11FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jComboBox11FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11FocusGained

    private void jComboBox11PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jComboBox11PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11PropertyChange

    private void jComboBox11KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jComboBox11KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox11KeyTyped

    private void jTextField246InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField246InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField246InputMethodTextChanged

    private void jTextField246KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField246KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField246KeyTyped

    private void jTextField247KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField247KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField247KeyTyped

    private void jTextField248ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField248ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField248ActionPerformed

    private void jTextField248KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField248KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField248KeyTyped

    private void jTextField249InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField249InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField249InputMethodTextChanged

    private void jTextField249KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField249KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField249KeyTyped

    private void jTextField250KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField250KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField250KeyTyped

    private void jTextField251ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField251ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField251ActionPerformed

    private void jTextField251KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField251KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField251KeyTyped

    private void jTextField252InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField252InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField252InputMethodTextChanged

    private void jTextField252KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField252KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField252KeyTyped

    private void jTextField253KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField253KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField253KeyTyped

    private void jTextField254ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField254ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField254ActionPerformed

    private void jTextField254KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField254KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField254KeyTyped

    private void jTextField255InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField255InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField255InputMethodTextChanged

    private void jTextField255KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField255KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField255KeyTyped

    private void jTextField256KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField256KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField256KeyTyped

    private void jTextField257ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField257ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField257ActionPerformed

    private void jTextField257KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField257KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField257KeyTyped

    private void jTextField258InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField258InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField258InputMethodTextChanged

    private void jTextField258KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField258KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField258KeyTyped

    private void jTextField259KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField259KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField259KeyTyped

    private void jTextField260ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField260ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField260ActionPerformed

    private void jTextField260KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField260KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField260KeyTyped

    private void jTextField261InputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_jTextField261InputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField261InputMethodTextChanged

    private void jTextField261KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField261KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField261KeyTyped

    private void jTextField262KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField262KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField262KeyTyped

    private void jTextField263ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField263ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField263ActionPerformed

    private void jTextField263KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField263KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField263KeyTyped

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        String str;
        str = jComboBox2.getSelectedItem().toString()  ;
        System.out.println("Acabo de capturar el comboBox TIPO DE FILTRO !!! Selecciono="+str);

        if (str.equals("ULTIMO CÁLCULO"))            { this.filtrobusca = 0 ; jTextField32.setVisible(false); jLabel53.setVisible(false);}
        if (str.equals("SELECCIONAR FECHA"))         { this.filtrobusca = 1 ; jTextField32.setVisible(true); jLabel53.setVisible(true);}
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void botonActualizaConsultaAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizaConsultaAnexoActionPerformed

        actualizarTablaAnexoResumenAhorro(this.id_cliente_actual);

        try {
            actualizarTablaAnexoDetalleAhorro(this.id_cliente_actual);
        } catch (SQLException ex) {
       //     Logger.getLogger(informesTopComponent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_botonActualizaConsultaAnexoActionPerformed

    private void botonGenerarExelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonGenerarExelActionPerformed
        GenerarExelAnexoResumen();
        GenerarExelAnexoResumenDetalle();
    }//GEN-LAST:event_botonGenerarExelActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        int ahorro = 1000 ;

    //    new miJDialogGenerarInforme(this, true, ahorro).setVisible(true);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        // ........................................................

        this.misDatos.idTipo = this.id_tipo_Sim;

        // ........................................................

        new miJDialogInformeSimple(this, true,misDatos).setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        anularLineaAhorro();
    }//GEN-LAST:event_jButton13ActionPerformed

    private void botonActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonActualizarActionPerformed
        actualizarTablaHistoricoAhorroCliente(this.id_cliente_actual) ;
    }//GEN-LAST:event_botonActualizarActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        generarExcelHistoricoCalculos();
    }//GEN-LAST:event_jButton18ActionPerformed

    private void listaClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_listaClientesActionPerformed
        int ind = 0;
        ind = listaClientes.getSelectedIndex();                            // id Cliente
        if (ind>0 )      this.id_cliente_general = Integer.parseInt(this.clientes[ind-1][0]);
        System.out.println("He seleccionado cliente id="+this.id_cliente_general);
    }//GEN-LAST:event_listaClientesActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            cargarDatosCliente();
        } catch (SQLException ex) {
     //       Logger.getLogger(FramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        modificarArbolNuevos() ;
        actualizarTablaAnexoResumenAhorro(this.id_cliente_actual);
        try {
            actualizarTablaAnexoDetalleAhorro(this.id_cliente_actual);
        } catch (SQLException ex) {
        //    Logger.getLogger(FramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarFechaUltimoCalculo(this.id_cliente_actual);
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PCContrato;
    private javax.swing.JPanel PDEntrada20A;
    private javax.swing.JTree arbol;
    private javax.swing.JTree arbol02;
    private javax.swing.JButton botonActualizaConsultaAnexo;
    private javax.swing.JButton botonActualizar;
    private javax.swing.JButton botonBorrarCampos;
    private javax.swing.JButton botonCalculo;
    private javax.swing.JButton botonDetalles;
    private javax.swing.JButton botonGenerarExel;
    private javax.swing.JButton botonValidar;
    private javax.swing.JButton botonValidarCAct;
    private javax.swing.JButton botonValidarCAct1;
    private javax.swing.JButton botonValidarSim;
    private javax.swing.JTextField energiaP1;
    private javax.swing.JTextField energiaP1s;
    private javax.swing.JTextField energiaP2;
    private javax.swing.JTextField energiaP2s;
    private javax.swing.JTextField energiaP3;
    private javax.swing.JTextField energiaP3s;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox11;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
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
    private javax.swing.JLabel jLabel163;
    private javax.swing.JLabel jLabel164;
    private javax.swing.JLabel jLabel165;
    private javax.swing.JLabel jLabel166;
    private javax.swing.JLabel jLabel167;
    private javax.swing.JLabel jLabel168;
    private javax.swing.JLabel jLabel169;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel170;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel238;
    private javax.swing.JLabel jLabel239;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel240;
    private javax.swing.JLabel jLabel241;
    private javax.swing.JLabel jLabel242;
    private javax.swing.JLabel jLabel243;
    private javax.swing.JLabel jLabel244;
    private javax.swing.JLabel jLabel245;
    private javax.swing.JLabel jLabel246;
    private javax.swing.JLabel jLabel247;
    private javax.swing.JLabel jLabel248;
    private javax.swing.JLabel jLabel249;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel250;
    private javax.swing.JLabel jLabel251;
    private javax.swing.JLabel jLabel252;
    private javax.swing.JLabel jLabel253;
    private javax.swing.JLabel jLabel254;
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
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel72;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane11;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane7;
    private javax.swing.JTabbedPane jTabbedPane8;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea5;
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
    private javax.swing.JTextField jTextField121;
    private javax.swing.JTextField jTextField122;
    private javax.swing.JTextField jTextField123;
    private javax.swing.JTextField jTextField124;
    private javax.swing.JTextField jTextField126;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField243;
    private javax.swing.JTextField jTextField244;
    private javax.swing.JTextField jTextField245;
    private javax.swing.JTextField jTextField246;
    private javax.swing.JTextField jTextField247;
    private javax.swing.JTextField jTextField248;
    private javax.swing.JTextField jTextField249;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField250;
    private javax.swing.JTextField jTextField251;
    private javax.swing.JTextField jTextField252;
    private javax.swing.JTextField jTextField253;
    private javax.swing.JTextField jTextField254;
    private javax.swing.JTextField jTextField255;
    private javax.swing.JTextField jTextField256;
    private javax.swing.JTextField jTextField257;
    private javax.swing.JTextField jTextField258;
    private javax.swing.JTextField jTextField259;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField260;
    private javax.swing.JTextField jTextField261;
    private javax.swing.JTextField jTextField262;
    private javax.swing.JTextField jTextField263;
    private javax.swing.JTextField jTextField264;
    private javax.swing.JTextField jTextField265;
    private javax.swing.JTextField jTextField266;
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
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField38;
    private javax.swing.JTextField jTextField39;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField41;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField44;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField47;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField57;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField59;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField60;
    private javax.swing.JTextField jTextField61;
    private javax.swing.JTextField jTextField62;
    private javax.swing.JTextField jTextField63;
    private javax.swing.JTextField jTextField64;
    private javax.swing.JTextField jTextField65;
    private javax.swing.JTextField jTextField66;
    private javax.swing.JTextField jTextField67;
    private javax.swing.JTextField jTextField68;
    private javax.swing.JTextField jTextField69;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField70;
    private javax.swing.JTextField jTextField71;
    private javax.swing.JTextField jTextField72;
    private javax.swing.JTextField jTextField73;
    private javax.swing.JTextField jTextField74;
    private javax.swing.JTextField jTextField75;
    private javax.swing.JTextField jTextField76;
    private javax.swing.JTextField jTextField77;
    private javax.swing.JTextField jTextField78;
    private javax.swing.JTextField jTextField79;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField80;
    private javax.swing.JTextField jTextField81;
    private javax.swing.JTextField jTextField82;
    private javax.swing.JTextField jTextField85;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JComboBox listaClientes;
    private javax.swing.JScrollPane miBarra01;
    private javax.swing.JScrollPane miBarra02;
    private javax.swing.JScrollPane miBarra03;
    private javax.swing.JScrollPane miBarra04;
    private javax.swing.JTable miTabla01;
    private javax.swing.JTable miTabla02;
    private javax.swing.JTable miTabla03;
    private javax.swing.JTable miTabla04;
    private javax.swing.JTable tablaC01;
    private javax.swing.JTable tablaC02;
    private javax.swing.JTable tablaC03;
    private javax.swing.JTable tablaC04;
    private javax.swing.JComboBox tipoMedida;
    private javax.swing.JComboBox tipoSuministro;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
    // ---------------------------------------------------------------------------------------------------------------------------------------    
    // ---------------------------------------------------------------------------------------------------------------------------------------    
  
    public void  conectarBD() {
         int i;
      
         JOptionPane.showMessageDialog(null,
                "\nVoy a conectar con la Base de Datos ",
                "AVISO",JOptionPane.WARNING_MESSAGE);
         
           Conexion conex = new Conexion(); 
           
           if (conex!=null){
            JOptionPane.showMessageDialog(null,
		 "La base de datos se ha conectado exitosamente",
		 "Información",JOptionPane.INFORMATION_MESSAGE);
            
                saepDao misaepDao1 = new saepDao();
	          
             misaepDao1.consultaClientes();
             this.clientes = misaepDao1.clientes ;
             this.nClientes = misaepDao1.nClientes;
            
             System.out.println("Tengo "+this.nClientes+" que procesar...");
             // ............................................................................
             listaClientes.removeAllItems();
             listaClientes.addItem("SELECCIONAR CLIENTE");
             for (i=0; i<this.nClientes; i++) {
                 
                 listaClientes.addItem(this.clientes[i][1]);
                 System.out.println("Estoy añadiendo el cliente ="+this.clientes[i][1]);
             }
             // ............................................................................
     //        listaClientesAdmin.removeAllItems();
     //        listaClientesAdmin.addItem("SELECCIONAR CLIENTE");
             for (i=0; i<this.nClientes; i++) {
                 
      //           listaClientesAdmin.addItem(this.clientes[i][1]);
                 System.out.println("Estoy añadiendo el cliente ="+this.clientes[i][1]);
             }
             // ............................................................................
  //           actualizarListaClientesAdministracion();
             conex.desconectar();
             
              // ............................................................................
             
              misaepDao1.consultaClientesSimulacion();                                      // Lista de clientes con Simulacion de facturas activada 
             
  //           this.nClientesSimulacion = misaepDao1.nClienteSimulacion ;
  //           this.clientesSimulacion  = misaepDao1.clientesSimulacion ;
             
             
       //      misaepDao1.consultaPuntosSimulacion();                                      // Lista de clientes con Simulacion de facturas activada 
           
       //      this.simulacionPuntos  = misaepDao1.simulacionPuntos ;
             
             // ............................................................................
    //         actualizarDatosEnTablasSimulacion(this.indSim);
             // ............................................................................
    //         modificarArbolSimulaciones();
    //         modificarArbolPrecios();
              // ............................................................................
            
     //        this.jLabel200.setVisible(false); this.jLabel202.setVisible(true); this.jLabel220.setVisible(false);
            
              // ............................................................................
     //         actualizarTablaAlertasServicio();
              // ............................................................................
         } 
         
    }
    // ------------------------------------------------------------------------------------------------------------
    public final void crearArbol() {
           
            System.out.println("Voy a crear el arbol (si puedo) ");
             
            /**Construimos los nodos del arbol que seran ramas u hojas*/
            /**Definimos cual será el directorio principal o la raiz de nuestro arbol*/
            
               DefaultMutableTreeNode carpetaRaiz= new DefaultMutableTreeNode("PUNTOS SUMINISTRO");
          
              
             /**Definimos el modelo donde se agregaran los nodos*/
            
              DefaultTreeModel modelo2;
              modelo2 = new DefaultTreeModel(carpetaRaiz);
              
             /**agregamos el modelo al arbol, donde previamente establecimos la raiz*/
              
             arbol = new JTree(modelo2);
             jScrollPane2.setViewportView(arbol);
             
            
             
             
}
      // ------------------------------------------------------------------------------------------------------------
        public void buscarCUPSe() {
        int i, nStr=0, id=-1,fEncuentro=0 ;
        String sBusqueda, sID ;
        sBusqueda = JOptionPane.showInputDialog("Introduce el CUPS electrico de contrato que buscas (completo ó 4 últimas cifras y 2 letras): ");
        
        int resp=JOptionPane.showConfirmDialog(null,"¿Buscar el CUPS de contrato ="+sBusqueda+" ?");
        
        
        if (JOptionPane.OK_OPTION == resp){
        
            sBusqueda    = sBusqueda.trim();
            nStr        = sBusqueda.length() ;
            if ( nStr == 6) {
                
                    for (i=0; i<this.nPuntos; i++){
                       
                        sID = this.listaPuntosSum[i][2].trim();
                        sID = sID.substring(14);                    System.out.println("Busco a:"+sID);
                       
                       if (sID.equals(sBusqueda)) {
                           
                           System.out.println("HE ENCONTRADO EL ID="+sID) ;
                           fEncuentro = 1 ;
                           actualizarFormularios(i) ;
                           break;
                           
                       }

                    }
            }
            if ( nStr == 20) {
                
                    for (i=0; i<this.nPuntos; i++){
                       
                        sID = this.listaPuntosSum[i][2].trim();
                        
                       
                       if (sID.equals(sBusqueda)) {
                           
                           System.out.println("HE ENCONTRADO EL ID="+sID) ;
                           fEncuentro = 1; 
                           actualizarFormularios(i) ;
                           break;
                           
                       }

                    }
            }
            
            if ( fEncuentro == 0) {                                             
                
                 JOptionPane.showMessageDialog(null,
		 "NO ENCUENTRO EL CUPS",
		 "Información",JOptionPane.INFORMATION_MESSAGE);
                 actualizarFormularios(-1) ;
            }
            
        }
        
        }
       // -------------------------------------------------------------------------------------------------  
      // ------------------------------------------------------------------------------------------------------------------------
        // ------------------------------------------------------------------------------------------------------------------------
        //  .......................................         ACTUALIZAR FORMULARIOS       .........................................
        // ------------------------------------------------------------------------------------------------------------------------
       
       
       //  .......................................  
           private void actualizarFormularios(int indice) {
           int ind,ireg, creg, indLoc=-1, id_tipo_S, id_tipo_A,idt=0;
           this.indGen = indice ;
           int id_punto;
           
           this.sMensajes = "";
           
        //  .......................................     
           
           botonValidarCAct.setVisible(false);
           botonValidarSim.setVisible(false);
           this.jPanel50.setVisible(false);                 // Panel con Energia simulada
           
        //  .......................................                                                 condiciones de inicio de formulario
            jTextField29.setVisible(true) ;
            jTextField39.setVisible(false) ;
            
            this.fPotenciaFacturada = 0 ;                                                  // Flag calculo por maximetro con formulas 3.0
        //  .......................................   
            
           if (indice == -1) {
           
            jTextField1.setText(""); jTextField1.setBackground(Color.white);// Nombre
            jTextField2.setText(""); jTextField2.setBackground(Color.white);// cups electrico
         
            jTextField3.setText(""); jTextField3.setBackground(Color.white);// CIF
          
            
            jTextField10.setText(""); jTextField10.setBackground(Color.white);// direccion
            jTextField6.setText("");  jTextField6.setBackground(Color.white);// provincia
            
            jTextField58.setText("") ;
            
            jTextField4.setText("") ;
            jTextField62.setText("") ;
            jTextField61.setText("") ;
            jTextField73.setText("") ;
            jTextField74.setText("") ;
            
            
            jTextField116.setText("01-01-2000") ;
            jTextField117.setText("01-01-2001") ;
            jTextField118.setText("") ;
            jTextField119.setText("") ;
            jTextField120.setText("") ;
            
            this.filtrobusca = 0 ; jTextField32.setVisible(false); jLabel53.setVisible(false);
            
            jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
            jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
            
            jCheckBox1.setSelected(false);
     //       jCheckBox2.setSelected(false);
            jCheckBox5.setSelected(false);      // Se factura siempre la Potencia Maxima en Actual
            jCheckBox6.setSelected(false);      // Se factura siempre la potencia maxima en Simulacion
            
            energiaP1.setText("0") ;
            energiaP2.setText("0") ;
            energiaP3.setText("0") ;
            energiaP1s.setText("0") ;
            energiaP2s.setText("0") ;
            energiaP3s.setText("0") ;
            jTextField80.setText("0") ; jTextField82.setBackground(Color.white);     // Alquiler
            jTextField81.setText("0") ; jTextField81.setBackground(Color.white);     // Base imponible factura 
            jTextField77.setText("0");
            jTextField78.setText("0");
            jTextField79.setText("0");
            
            jTextField121.setText("0");
            jTextField122.setText("0");
            jTextField123.setText("0");
            
            jTextField82.setText("0"); jTextField82.setBackground(Color.white);            // Base imponible
            
            jTextField74.setText("0");                                                     // Precio Energia de peaje simulacion
           
            jTextField106.setText("0");
            jTextField107.setText("0");
            jTextField108.setText("0");
            
            jTextField113.setText("0");
            jTextField114.setText("0");
            jTextField115.setText("0");
         
        //  .......................................  
           } else {
                   
           
            energiaP1s.setText("0") ;
            energiaP2s.setText("0") ;
            energiaP3s.setText("0") ;
            jTextField80.setText("0") ; jTextField82.setBackground(Color.white);     // Alquiler
            jTextField81.setText("0") ; jTextField81.setBackground(Color.white);     // Base imponible factura 
            jTextField77.setText("0");
            jTextField78.setText("0");
            jTextField79.setText("0");
            jTextField121.setText("0");
            jTextField122.setText("0");
            jTextField123.setText("0");
            jTextField126.setText("0");
            
            jTextField82.setText("0"); jTextField82.setBackground(Color.white);       // Base imponible
             
            jTextField74.setText("0");                                                // Precio Energia de peaje simulacion
            
            jTextField106.setText("0");
            jTextField107.setText("0");
            jTextField108.setText("0");
            
            jTextField113.setText("0");
            jTextField114.setText("0");
            jTextField115.setText("0");
            
            id_punto = Integer.parseInt(this.listaContratosPuntos[indice][0]);
           
            // ...............................................
            
            actualizarTablaHistoricoPuntos(id_punto) ;
            
             saepDao misaepDao5 = new saepDao();
        
            // ....................................................... 
        
            misaepDao5. consultaHistoricoAhorrosPuntoDetalle(id_punto,this.id_cliente_actual);                             // Consultamos los registros de cálculo que tiene este punto
       
            this.lAhorrosHistorico  = misaepDao5.lhistoricoAhorros ;
            this.nCalculosPunto     = misaepDao5.nCalculosPunto;
            
            // ...............................................
            
            if (this.listaPuntosSum[indice][12].equals("1")) jCheckBox1.setSelected(true); else jCheckBox1.setSelected(false);
            
            if (this.listaPuntosSum[indice][16].equals("1")) idt = 0; else idt=1; tipoMedida.setSelectedIndex(idt);             // Tipo de medida
            
            if (this.listaPuntosSum[indice][17].equals("1")) idt = 0; else idt=1; tipoSuministro.setSelectedIndex(idt);         // Tipo de suministro
               
            // ...............................................
            
            
            
            
            if (jCheckBox1.isSelected()){
                
               jPanel48.setVisible(true); System.out.println("Tiene Bateria");
                
            } else {
               
               jPanel48.setVisible(false);
                
            }
            
            // ...............................................
            
             
            id_tipo_A =   Integer.parseInt(this.listaContratosPuntosAct[indice][3]) ;  
            id_tipo_S =   Integer.parseInt(this.listaContratosPuntos[indice][3]) ; 
         //   if (id_tipo_A != this.id_tipo_Actual) { id_tipo_A = this.id_tipo_Actual; botonValidarCAct.setVisible(true); this.listaContratosPuntosAct[indice][3]=String.valueOf(id_tipo_A);}
         //   if (id_tipo_S != this.id_tipo_Sim)    { id_tipo_S = this.id_tipo_Actual; botonValidarSim.setVisible(true); this.listaContratosPuntos[indice][3]=String.valueOf(id_tipo_S);}
            
            this.tipo_Act           = id_tipo_A ;
            this.tipo_Sim           = id_tipo_S ;
            this.id_punto_actual    = id_punto ;
            
            // ...............................................
            System.out.println("----------- ACTUALIZO FORMULARIO PARA IDP = "+id_punto+" -----------------");
            System.out.println("id_tipo_A = "+id_tipo_A);
            System.out.println("id_tipo_S = "+id_tipo_S);
            System.out.println("indice = "+indice);
            
            // ...............................................                      DATOS GENERALES DEL PUNTO DE SUMINISTRO
            
            jTextField264.setText(String.valueOf(id_punto));
            
            jTextField1.setText(this.listaPuntosSum[indice][1]);
            jTextField2.setText(this.listaPuntosSum[indice][2]);
            jTextField3.setText(this.listaPuntosSum[indice][3]);
        
            jTextField10.setText(this.listaPuntosSum[indice][10]);
            jTextArea1.setText(this.listaPuntosSum[indice][11]);
            jTextField59.setText(this.listaPuntosSum[indice][18]);
            
            jTextField58.setText(String.valueOf(this.indGen)) ;
            
            jTextField4.setText(String.valueOf(this.listaPuntosSum[indice][13])) ;
            jTextField62.setText(String.valueOf(this.listaPuntosSum[indice][14])) ;
            jTextField61.setText(String.valueOf(this.listaPuntosSum[indice][15])) ;
            
           // ..............................................
            
            jTextField116.setText(this.listaPuntosSum[indice][19]);
            jTextField117.setText(this.listaPuntosSum[indice][20]);
            jTextField118.setText(this.listaPuntosSum[indice][21]);
            jTextField120.setText(this.listaPuntosSum[indice][22]);
            jTextField119.setText(this.listaPuntosSum[indice][23]);
            
           // ..............................................
            
            jTextField30.setText("");
            jTextField47.setText("");
            jTextField15.setText("");
            jTextField27.setText("");
            jTextField5.setText("");
            
            energiaP1.setText("0");
            energiaP2.setText("0");
            energiaP3.setText("0");
            
            jTextField77.setText("0");
            jTextField78.setText("0");
            jTextField79.setText("0");
           
            jComboBox3.setSelectedIndex(id_tipo_A);   jComboBox11.setSelectedIndex(id_tipo_A);    
            jComboBox4.setSelectedIndex(id_tipo_S);        
            
            if (id_tipo_S == 0){
                
                  // ...............................
                    
                    jTextField29.setText("");              // Fecha contrato
                    
                    jTextField34.setText("");              // Compañia
                    jTextField39.setText("");              // Descripcion Tarifa
                    
                    jTextField20.setText("");           // Precio energia P1
                    jTextField21.setText("");           // Precio energia P2
                    jTextField22.setText("");           // Precio energia P3
                    jTextField23.setText("");            // Precio potencia P1
                    jTextField24.setText("");            // Precio potencia P2
                    jTextField25.setText("");            // Precio potencia P3
                    jTextField26.setText("");            // potencia contratada P1
                    jTextField37.setText("");            // potencia contratada P2
                    jTextField38.setText("");            // potencia contratada P3
                    jTextField112.setText("");            // ALQUILER
                    jTextArea3.setText("");              // Observaciones
                   
                    jCheckBox6.setSelected(false);      // Se factura siempre la potencia maxima en Simulacion
                    
                    jTextField74.setText("") ;
            }
            
             if (id_tipo_A == 0){
                
                    jTextField28.setText("");              // Fecha contrato
                    jTextField11.setText("");              // Fecha fin de contrato
                    jTextField33.setText("");              // Compañia
                 
                    
                    jTextField12.setText("");           // Precio energia P1
                    jTextField13.setText("");           // Precio energia P2
                    jTextField14.setText("");           // Precio energia P3
                    jTextField16.setText("");            // Precio potencia P1
                    jTextField17.setText("");            // Precio potencia P2
                    jTextField18.setText("");           // Precio potencia P3
                    jTextField19.setText("");            // potencia contratada P1
                    jTextField35.setText("");            // potencia contratada P2
                    jTextField36.setText("");            // potencia contratada P3
                    jTextField111.setText("");            // ALQUILER    
                    jTextArea2.setText("");              // Observaciones  
                    jCheckBox5.setSelected(false);      // Se factura siempre la Potencia Maxima en Actual
                    jTextField73.setText("") ;
                    
                    jTextField246.setText("");          // Precio energía P1 T6.x
                    jTextField247.setText("");          // Precio energía P2 T6.x
                    jTextField248.setText("");          // Precio energía P3 T6.x
                    jTextField252.setText("");          // Precio energía P4 T6.x
                    jTextField253.setText("");          // Precio energía P5 T6.x
                    jTextField254.setText("");          // Precio energía P6 T6.x
                    
                    jTextField265.setText("");          // Precio energía PEAJE T6.x
                    
                    jTextField258.setText("");          // Precio potencia P1 T6.x
                    jTextField259.setText("");          // Precio potencia P2 T6.x
                    jTextField260.setText("");          // Precio potencia P3 T6.x
                    jTextField261.setText("");          // Precio potencia P4 T6.x
                    jTextField262.setText("");          // Precio potencia P5 T6.x
                    jTextField263.setText("");          // Precio potencia P6 T6.x
                    
                    jTextField249.setText("");          //  potencia P1 T6.x
                    jTextField250.setText("");          //  potencia P2 T6.x
                    jTextField251.setText("");          //  potencia P3 T6.x
                    jTextField255.setText("");          //  potencia P4 T6.x
                    jTextField256.setText("");          //  potencia P5 T6.x
                    jTextField257.setText("");          //  potencia P6 T6.x
                    
                    jTextField243.setText("");          // Nombre comercializadora
                    jTextField244.setText("");          // fecha fin contrato
                    jTextField245.setText("");          // fecha cambio
                    
                    jTextArea5.setText("");              // Observaciones  
                    jCheckBox7.setSelected(false);      // Se factura siempre la Potencia Maxima en Actual en P6
                    
                    
             }
            
            // ...............................................                              FORMULARIO DE LAS CONDICIONES DE SIMULACIÓN
            // ........................................................................................................................................... 
            switch (id_tipo_S) {
                    // _________________________________________________________________________________________________________
                    case 1:                                                                                     // contrato 2.0A
                    
            
                    
                     // ...............................
                    jTextField21.setVisible(false); jTextField24.setVisible(false); jLabel32.setVisible(false);  jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false);  jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    
                    jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
                    
                    jCheckBox6.setVisible(false);           // factura maxima potencia
                    // ...............................
                   
                   System.out.println("Compañia -> lCondicionesSimulacion[indice][8] = "+lCondicionesSimulacion[indice][8]);
                   System.out.println("Precio Energia P1 -> lCondicionesSimulacion[indice][14] = "+lCondicionesSimulacion[indice][14]);
                    
                    // ............................... Condiciones de simulacion
                    
                    jTextField28.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                   
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    
                    break;
                   // _________________________________________________________________________________________________________                        
                   case 2:                                                                                   // contrato 2.0 DHA
                       
                    jPanel27.setVisible(false);             // panel de potencias maximetro medidas
                     // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(false); jLabel32.setVisible(true);   jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false); jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    
                    jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
                    jCheckBox6.setVisible(false);  
                    // ...............................
                    
                    jTextField28.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                  
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
            
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    break;
                 // _________________________________________________________________________________________________________
                   case 3:                                                                                           // contrato 2.1A
                    jPanel27.setVisible(false);             // panel de potencias maximetro medidas
                     // ...............................
                    jTextField21.setVisible(false); jTextField24.setVisible(false); jLabel32.setVisible(false);  jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false);  jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
                    jCheckBox6.setVisible(false);  
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                   
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    break; 
                    // _________________________________________________________________________________________________________
                    case 4:                                                                                  // contrato 2.1DHA
                    jPanel27.setVisible(false);             // panel de potencias maximetro medidas
                    // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(false); jLabel32.setVisible(true);  jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false);  jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                     jCheckBox6.setVisible(false);   
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                 
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    break;
                    // _________________________________________________________________________________________________________
                    case 5:                                                                                      // contrato 3.0A
                    jPanel27.setVisible(true);                                           // panel de potencias maximetro medidas
                    // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(true); jLabel32.setVisible(true);  jLabel40.setVisible(true); jLabel43.setVisible(true);
                    jTextField22.setVisible(true); jTextField25.setVisible(true); jLabel33.setVisible(true);  jLabel41.setVisible(true); jLabel44.setVisible(true);
               
                    jTextField37.setVisible(true); jLabel62.setVisible(true); jLabel63.setVisible(true);
                    jTextField38.setVisible(true); jLabel64.setVisible(true); jLabel65.setVisible(true);
                    jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
                    jCheckBox6.setVisible(true);   
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField22.setText(lCondicionesSimulacion[indice][16]);           // Precio energia P3
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField24.setText(lCondicionesSimulacion[indice][9]);            // Precio potencia P2
                    jTextField25.setText(lCondicionesSimulacion[indice][10]);            // Precio potencia P3
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField37.setText(lCondicionesSimulacion[indice][3]);            // potencia contratada P2
                    jTextField38.setText(lCondicionesSimulacion[indice][4]);            // potencia contratada P3
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    if (lCondicionesSimulacion[indice][25] != null ) {
                        if (lCondicionesSimulacion[indice][25].equals("1") ){
                                jCheckBox6.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox6.setSelected(false);  
                        }
                    }
                    break;
                    // _________________________________________________________________________________________________________
                    case 6:                                                                                      // contrato 3.1A
                    jPanel27.setVisible(true);                                           // panel de potencias maximetro medidas
                    // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(true); jLabel32.setVisible(true);  jLabel40.setVisible(true); jLabel43.setVisible(true);
                    jTextField22.setVisible(true); jTextField25.setVisible(true); jLabel33.setVisible(true);  jLabel41.setVisible(true); jLabel44.setVisible(true);
               
                    jTextField37.setVisible(true); jLabel62.setVisible(true); jLabel63.setVisible(true);
                    jTextField38.setVisible(true); jLabel64.setVisible(true); jLabel65.setVisible(true);
                    jTextField74.setVisible(false); jLabel98.setVisible(false); jLabel99.setVisible(false);
                    jCheckBox6.setVisible(true);   
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField22.setText(lCondicionesSimulacion[indice][16]);           // Precio energia P3
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField24.setText(lCondicionesSimulacion[indice][9]);            // Precio potencia P2
                    jTextField25.setText(lCondicionesSimulacion[indice][10]);            // Precio potencia P3
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField37.setText(lCondicionesSimulacion[indice][3]);            // potencia contratada P2
                    jTextField38.setText(lCondicionesSimulacion[indice][4]);            // potencia contratada P3
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    if (lCondicionesSimulacion[indice][25] != null ) {
                        if (lCondicionesSimulacion[indice][25].equals("1") ){
                                jCheckBox6.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox6.setSelected(false);  
                        }
                    }
                    break;
                  
                    // _________________________________________________________________________________________________________
                    case 8:                                                                                 // contrato 2.0 DH INDX
                     jPanel27.setVisible(false);                                              // panel de potencias maximetro medidas
                    
                     // ...............................
                    jTextField21.setVisible(false); jTextField24.setVisible(false); jLabel32.setVisible(false);  jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false);  jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    
                    jTextField74.setVisible(true); jLabel98.setVisible(true); jLabel99.setVisible(true);
                  
                     jCheckBox6.setVisible(false);  
                    // ............................... Condiciones de simulacion
                    
                    jTextField28.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                   
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                        
                    break;
                     // _________________________________________________________________________________________________________
                    case 9:                                                                                  // contrato 2.1DHA indx
                    jPanel27.setVisible(false);             // panel de potencias maximetro medidas
                    // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(false); jLabel32.setVisible(true);  jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false);  jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                     jTextField74.setVisible(true); jLabel98.setVisible(true); jLabel99.setVisible(true);
                  
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                     jCheckBox6.setVisible(false);   
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                 
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    
                    break;  
                     // _________________________________________________________________________________________________________
                    case 10:                                                                                      // contrato 3.0A indx
                    jPanel27.setVisible(true);                                               // panel de potencias maximetro medidas
                    // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(true); jLabel32.setVisible(true);  jLabel40.setVisible(true); jLabel43.setVisible(true);
                    jTextField22.setVisible(true); jTextField25.setVisible(true); jLabel33.setVisible(true);  jLabel41.setVisible(true); jLabel44.setVisible(true);
               
                    jTextField37.setVisible(true); jLabel62.setVisible(true); jLabel63.setVisible(true);
                    jTextField38.setVisible(true); jLabel64.setVisible(true); jLabel65.setVisible(true);
                    jTextField74.setVisible(true); jLabel98.setVisible(true); jLabel99.setVisible(true);
                     jCheckBox6.setVisible(true);  
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField22.setText(lCondicionesSimulacion[indice][16]);           // Precio energia P3
                    jTextField74.setText(lCondicionesSimulacion[indice][23]);           // Precio energia de peaje
                    
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField24.setText(lCondicionesSimulacion[indice][9]);            // Precio potencia P2
                    jTextField25.setText(lCondicionesSimulacion[indice][10]);            // Precio potencia P3
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField37.setText(lCondicionesSimulacion[indice][3]);            // potencia contratada P2
                    jTextField38.setText(lCondicionesSimulacion[indice][4]);            // potencia contratada P3
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    // ........................................................
                    if (lCondicionesSimulacion[indice][25] != null) {
                        if (lCondicionesSimulacion[indice][25].equals("1") ){
                                jCheckBox6.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox6.setSelected(false);  
                        }
                    }
                    break;
                 // _________________________________________________________________________________________________________                        
                   case 11:                                                                                   // contrato 2.0 INDX
                       
                    jPanel27.setVisible(false);             // panel de potencias maximetro medidas
                     // ...............................
                    jTextField21.setVisible(false); jTextField24.setVisible(false); jLabel32.setVisible(false);   jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false); jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    
                    jTextField74.setVisible(true); jLabel98.setVisible(true); jLabel99.setVisible(true);            // ENERGIA DE PEAJE
                     // ...............................
                     jCheckBox6.setVisible(false);   
                    // ............................... Condiciones de simulacion
                    
                    jTextField28.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                   
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    jTextField74.setText(lCondicionesSimulacion[indice][23]);           // Precio energia de peaje
                    break;
                   // _________________________________________________________________________________________________________                        
                   case 12:                                                                                   // contrato 2.1 INDX
                       
                    jPanel27.setVisible(false);             // panel de potencias maximetro medidas
                     // ...............................
                    jTextField21.setVisible(false); jTextField24.setVisible(false); jLabel32.setVisible(false);   jLabel40.setVisible(false); jLabel43.setVisible(false);
                    jTextField22.setVisible(false); jTextField25.setVisible(false); jLabel33.setVisible(false); jLabel41.setVisible(false); jLabel44.setVisible(false);
               
                    jTextField37.setVisible(false); jLabel62.setVisible(false); jLabel63.setVisible(false);
                    jTextField38.setVisible(false); jLabel64.setVisible(false); jLabel65.setVisible(false);
                    
                    jTextField74.setVisible(true); jLabel98.setVisible(true); jLabel99.setVisible(true);            // ENERGIA DE PEAJE
                     // ...............................
                     jCheckBox6.setVisible(false);  
                    // ............................... Condiciones de simulacion
                    
                    jTextField28.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                   
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    jTextField74.setText(lCondicionesSimulacion[indice][23]);           // Precio energia de peaje
                    break;
                     // _________________________________________________________________________________________________________
                    case 13:                                                                                      // contrato 3.1A indx
                    jPanel27.setVisible(true);                                               // panel de potencias maximetro medidas
                    // ...............................
                    jTextField21.setVisible(true); jTextField24.setVisible(true); jLabel32.setVisible(true);  jLabel40.setVisible(true); jLabel43.setVisible(true);
                    jTextField22.setVisible(true); jTextField25.setVisible(true); jLabel33.setVisible(true);  jLabel41.setVisible(true); jLabel44.setVisible(true);
               
                    jTextField37.setVisible(true); jLabel62.setVisible(true); jLabel63.setVisible(true);
                    jTextField38.setVisible(true); jLabel64.setVisible(true); jLabel65.setVisible(true);
                    jTextField74.setVisible(true); jLabel98.setVisible(true); jLabel99.setVisible(true);
                     jCheckBox6.setVisible(true);  
                    // ...............................
                    
                    jTextField29.setText(listaContratosPuntos[indice][1]);              // Fecha contrato
                    
                    jTextField34.setText(listaContratosPuntos[indice][8]);              // Compañia
                    jTextField39.setText(listaContratosPuntos[indice][5]);              // Descripcion Tarifa
                    
                    jTextField20.setText(lCondicionesSimulacion[indice][14]);           // Precio energia P1
                    jTextField21.setText(lCondicionesSimulacion[indice][15]);           // Precio energia P2
                    jTextField22.setText(lCondicionesSimulacion[indice][16]);           // Precio energia P3
                    jTextField74.setText(lCondicionesSimulacion[indice][23]);           // Precio energia de peaje
                    
                    jTextField23.setText(lCondicionesSimulacion[indice][8]);            // Precio potencia P1
                    jTextField24.setText(lCondicionesSimulacion[indice][9]);            // Precio potencia P2
                    jTextField25.setText(lCondicionesSimulacion[indice][10]);            // Precio potencia P3
                    jTextField26.setText(lCondicionesSimulacion[indice][2]);            // potencia contratada P1
                    jTextField37.setText(lCondicionesSimulacion[indice][3]);            // potencia contratada P2
                    jTextField38.setText(lCondicionesSimulacion[indice][4]);            // potencia contratada P3
                    jTextField112.setText(lCondicionesSimulacion[indice][24]);            // alquiler
                    jTextArea3.setText(lCondicionesSimulacion[indice][20]);              // Observaciones
                    // ........................................................
                    if (lCondicionesSimulacion[indice][25] != null) {
                        if (lCondicionesSimulacion[indice][25].equals("1") ){
                                jCheckBox6.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox6.setSelected(false);  
                        }
                    }
                    break;
                   
            }   
            // ...............................................                              FORMULARIO DE LAS CONDICIONES DE SIMULACIÓN
            // ........................................................................................................................................... 
          
            switch (id_tipo_A) {
                // _________________________________________________________________________________________________________
                case 1:                                                                                         // contrato  ACTUAL 2.0A
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(false);                                              // panel de potencias maximetro medidas
                    jPanel45.setVisible(false);                                              // panel de reactiva
                    jPanel48.setVisible(false);                                              // panel de reactiva sim
                    
                    jTextField13.setVisible(false); jTextField17.setVisible(false); jLabel19.setVisible(false);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(false); jLabel26.setVisible(false); energiaP2.setVisible(false);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                    
                    jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                     jCheckBox5.setVisible(false);  
                    // ............................... Condiciones actuales
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                  
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    
                    break;
                   // _________________________________________________________________________________________________________
                   case 2:                                                                                          // contrato ACTUAL  2.0DHA
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(false);                                              // panel de potencias maximetro medidas
                    jPanel45.setVisible(false);                                              // panel de reactiva
                    jPanel48.setVisible(false);                                              // panel de reactiva sim
                    
                    jTextField13.setVisible(true); jTextField17.setVisible(false); jLabel19.setVisible(true);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                    
                    jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                     jCheckBox5.setVisible(false); 
                     // ............................... Condiciones actuales
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato                    
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                    
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    
                    break;
                 // _________________________________________________________________________________________________________
                 case 3:                                                                                        // contrato ACTUAL  2.1A
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(false);                                              // panel de potencias maximetro medidas
                    jPanel45.setVisible(false);                                              // panel de reactiva
                    jPanel48.setVisible(false);                                              // panel de reactiva sim
                     
                     
                    jTextField13.setVisible(false); jTextField17.setVisible(false); jLabel19.setVisible(false);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(false); jLabel26.setVisible(false); energiaP2.setVisible(false);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                     jCheckBox5.setVisible(false); 
                    // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                    
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    
                    break;
                    // _________________________________________________________________________________________________________
                    case 4:                                                                                        // contrato ACTUAL  2.1DHA
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(true); jTextField17.setVisible(false); jLabel19.setVisible(true);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                     jCheckBox5.setVisible(false); 
                   // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                   
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    
                    break;
                    // _________________________________________________________________________________________________________
                    case 5:                                                                                             // contrato ACTUAL  3.0A
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(true); jTextField17.setVisible(true); jLabel19.setVisible(true);  jLabel23.setVisible(true); jLabel29.setVisible(true);
                    jTextField14.setVisible(true); jTextField18.setVisible(true); jLabel20.setVisible(true);  jLabel24.setVisible(true); jLabel30.setVisible(true);
               
                    jTextField35.setVisible(true); jLabel57.setVisible(true); jLabel58.setVisible(true);
                    jTextField36.setVisible(true); jLabel59.setVisible(true); jLabel60.setVisible(true);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(true); jLabel27.setVisible(true); energiaP3.setVisible(true);
                     jCheckBox5.setVisible(true); 
                     // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                 
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField14.setText(lCondicionesActuales[indice][16]);           // Precio energia P3
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField17.setText(lCondicionesActuales[indice][9]);            // Precio potencia P2
                    jTextField18.setText(lCondicionesActuales[indice][10]);           // Precio potencia P3
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField35.setText(lCondicionesActuales[indice][3]);            // potencia contratada P2
                    jTextField36.setText(lCondicionesActuales[indice][4]);            // potencia contratada P3
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler    
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones  
                   
                    if (lCondicionesActuales[indice][25] != null) {
                        if (lCondicionesActuales[indice][25].equals("1") ){
                                jCheckBox5.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox5.setSelected(false);  
                        }
                    }
                    break;
                    // _________________________________________________________________________________________________________
                    case 6:                                                                                             // contrato ACTUAL  3.1A
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(true); jTextField17.setVisible(true); jLabel19.setVisible(true);  jLabel23.setVisible(true); jLabel29.setVisible(true);
                    jTextField14.setVisible(true); jTextField18.setVisible(true); jLabel20.setVisible(true);  jLabel24.setVisible(true); jLabel30.setVisible(true);
               
                    jTextField35.setVisible(true); jLabel57.setVisible(true); jLabel58.setVisible(true);
                    jTextField36.setVisible(true); jLabel59.setVisible(true); jLabel60.setVisible(true);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(true); jLabel27.setVisible(true); energiaP3.setVisible(true);
                     jCheckBox5.setVisible(true); 
                     // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(false); jLabel96.setVisible(false); jLabel97.setVisible(false);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                 
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField14.setText(lCondicionesActuales[indice][16]);           // Precio energia P3
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField17.setText(lCondicionesActuales[indice][9]);            // Precio potencia P2
                    jTextField18.setText(lCondicionesActuales[indice][10]);           // Precio potencia P3
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField35.setText(lCondicionesActuales[indice][3]);            // potencia contratada P2
                    jTextField36.setText(lCondicionesActuales[indice][4]);            // potencia contratada P3
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler    
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones  
                   
                    if (lCondicionesActuales[indice][25] != null) {
                        if (lCondicionesActuales[indice][25].equals("1") ){
                                jCheckBox5.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox5.setSelected(false);  
                        }
                    }
                    break;
                    // _________________________________________________________________________________________________________
                    case 7:                                                             // tarifa 6.1 
                        jTabbedPane11.setSelectedIndex(1);
                           // ............................... Condiciones actuales
                    
                        jTextField244.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                        jTextField245.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                        jTextField243.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                        
                        jTextField246.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                        jTextField247.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                        jTextField248.setText(lCondicionesActuales[indice][16]);           // Precio energia P3
                        jTextField252.setText(lCondicionesActuales[indice][17]);           // Precio energia P1
                        jTextField253.setText(lCondicionesActuales[indice][18]);           // Precio energia P2
                        jTextField254.setText(lCondicionesActuales[indice][19]);           // Precio energia P3
                        
                        jTextField258.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                        jTextField259.setText(lCondicionesActuales[indice][9]);            // Precio potencia P2
                        jTextField260.setText(lCondicionesActuales[indice][10]);           // Precio potencia P3
                        jTextField261.setText(lCondicionesActuales[indice][11]);           // Precio potencia P4
                        jTextField262.setText(lCondicionesActuales[indice][12]);           // Precio potencia P5
                        jTextField263.setText(lCondicionesActuales[indice][13]);           // Precio potencia P6
                        
                        jTextField249.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                        jTextField250.setText(lCondicionesActuales[indice][3]);            // potencia contratada P2
                        jTextField251.setText(lCondicionesActuales[indice][4]);            // potencia contratada P3
                        jTextField255.setText(lCondicionesActuales[indice][5]);            // potencia contratada P4
                        jTextField256.setText(lCondicionesActuales[indice][6]);            // potencia contratada P5
                        jTextField257.setText(lCondicionesActuales[indice][7]);            // potencia contratada P6
                        
                        jTextField266.setText(lCondicionesActuales[indice][24]);            // alquiler    
                        jTextArea5.setText(lCondicionesActuales[indice][20]);              // Observaciones  

                        if (lCondicionesActuales[indice][25] != null) {
                            if (lCondicionesActuales[indice][25].equals("1") ){
                                    jCheckBox7.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                            } else {

                                    jCheckBox7.setSelected(false);  
                            }
                        }                   
                        
                    break;    
                    // _________________________________________________________________________________________________________    
                    case 8:                                                                             // Tarifa ACTUAL 2.0DHA Indexado
                    jTabbedPane11.setSelectedIndex(0);
                    jTextField13.setVisible(true); jTextField17.setVisible(false); jLabel19.setVisible(true);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                     jCheckBox5.setVisible(false); 
                    // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(true); jLabel96.setVisible(true); jLabel97.setVisible(true);   // Precio energia peaje
                    jTextField75.setVisible(true); jLabel100.setVisible(true); jLabel101.setVisible(true); //  energia peaje
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato                    
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField73.setText(lCondicionesActuales[indice][23]);           // Precio energia de Peaje
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    
                    break;
                    // _________________________________________________________________________________________________________
                    case 9:                                                                                        // contrato ACTUAL  2.1DHA INDX
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(true); jTextField17.setVisible(false); jLabel19.setVisible(true);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                    jCheckBox5.setVisible(false); 
                   // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(true); jLabel96.setVisible(true); jLabel97.setVisible(true);
                    jTextField75.setVisible(true); jLabel100.setVisible(true); jLabel101.setVisible(true);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                   
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField73.setText(lCondicionesActuales[indice][23]);           // Precio energia de Peaje
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    
                    break;
                    // _________________________________________________________________________________________________________
                    case 10:                                                                                             // contrato ACTUAL  3.0A indx
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(true); jTextField17.setVisible(true); jLabel19.setVisible(true);  jLabel23.setVisible(true); jLabel29.setVisible(true);
                    jTextField14.setVisible(true); jTextField18.setVisible(true); jLabel20.setVisible(true);  jLabel24.setVisible(true); jLabel30.setVisible(true);
               
                    jTextField35.setVisible(true); jLabel57.setVisible(true); jLabel58.setVisible(true);
                    jTextField36.setVisible(true); jLabel59.setVisible(true); jLabel60.setVisible(true);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(true); jLabel27.setVisible(true); energiaP3.setVisible(true);
                     jCheckBox5.setVisible(true); 
                     // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(true); jLabel96.setVisible(true); jLabel97.setVisible(true);
                    jTextField75.setVisible(true); jLabel100.setVisible(true); jLabel101.setVisible(true);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                 
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField13.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField14.setText(lCondicionesActuales[indice][16]);           // Precio energia P3
                    jTextField73.setText(lCondicionesActuales[indice][23]);           // Precio energia de peaje
                    
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField17.setText(lCondicionesActuales[indice][9]);            // Precio potencia P2
                    jTextField18.setText(lCondicionesActuales[indice][10]);           // Precio potencia P3
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField35.setText(lCondicionesActuales[indice][3]);            // potencia contratada P2
                    jTextField36.setText(lCondicionesActuales[indice][4]);            // potencia contratada P3
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler    
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones  
                    
                    if (lCondicionesActuales[indice][25] != null) {
                        if (lCondicionesActuales[indice][25].equals("1") ){
                                jCheckBox5.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox5.setSelected(false);  
                        }
                    }
                    break;
                    // _________________________________________________________________________________________________________
                    case 11:                                                                                         // contrato  ACTUAL 2.0A indx
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(false); jTextField17.setVisible(false); jLabel19.setVisible(false);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(false); jLabel26.setVisible(false); energiaP2.setVisible(false);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                    
                    jTextField73.setVisible(true); jLabel96.setVisible(true); jLabel97.setVisible(true);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                     jCheckBox5.setVisible(false);   
                    // ............................... Condiciones actuales
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                  
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    jTextField73.setText(lCondicionesActuales[indice][23]);           // Precio energia de peaje
                    break;
                     // _________________________________________________________________________________________________________
                    case 12:                                                                                         // contrato  ACTUAL 2.1A indx
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(false); jTextField17.setVisible(false); jLabel19.setVisible(false);  jLabel23.setVisible(false); jLabel29.setVisible(false);
                    jTextField14.setVisible(false); jTextField18.setVisible(false); jLabel20.setVisible(false);  jLabel24.setVisible(false); jLabel30.setVisible(false);
               
                    jTextField35.setVisible(false); jLabel57.setVisible(false); jLabel58.setVisible(false);
                    jTextField36.setVisible(false); jLabel59.setVisible(false); jLabel60.setVisible(false);
                    
                    jLabel21.setVisible(false); jLabel26.setVisible(false); energiaP2.setVisible(false);
                    jLabel25.setVisible(false); jLabel27.setVisible(false); energiaP3.setVisible(false);
                    
                    jTextField73.setVisible(true); jLabel96.setVisible(true); jLabel97.setVisible(true);
                    jTextField75.setVisible(false); jLabel100.setVisible(false); jLabel101.setVisible(false);
                     jCheckBox5.setVisible(false);  
                    // ............................... Condiciones actuales
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                  
                    
                    jTextField12.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones
                    jTextField73.setText(lCondicionesActuales[indice][23]);           // Precio energia de peaje
                    break;
                    // _________________________________________________________________________________________________________
                    case 13:                                                                                             // contrato ACTUAL  3.1A indx
                    jTabbedPane11.setSelectedIndex(0);
                    jPanel27.setVisible(true);             // panel de potencias maximetro medidas
                    jTextField13.setVisible(true); jTextField17.setVisible(true); jLabel19.setVisible(true);  jLabel23.setVisible(true); jLabel29.setVisible(true);
                    jTextField14.setVisible(true); jTextField18.setVisible(true); jLabel20.setVisible(true);  jLabel24.setVisible(true); jLabel30.setVisible(true);
               
                    jTextField35.setVisible(true); jLabel57.setVisible(true); jLabel58.setVisible(true);
                    jTextField36.setVisible(true); jLabel59.setVisible(true); jLabel60.setVisible(true);
                    
                    jLabel21.setVisible(true); jLabel26.setVisible(true); energiaP2.setVisible(true);
                    jLabel25.setVisible(true); jLabel27.setVisible(true); energiaP3.setVisible(true);
                     jCheckBox5.setVisible(true); 
                     // ............................... Condiciones actuales
                    
                    jTextField73.setVisible(true); jLabel96.setVisible(true); jLabel97.setVisible(true);
                    jTextField75.setVisible(true); jLabel100.setVisible(true); jLabel101.setVisible(true);
                    
                    jTextField28.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                    jTextField11.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                    jTextField33.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                 
                    
                    jTextField246.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                    jTextField247.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                    jTextField248.setText(lCondicionesActuales[indice][16]);           // Precio energia P3
                    jTextField252.setText(lCondicionesActuales[indice][17]);           // Precio energia P4
                    jTextField253.setText(lCondicionesActuales[indice][18]);           // Precio energia P5
                    jTextField254.setText(lCondicionesActuales[indice][19]);           // Precio energia P6
                    
                    jTextField265.setText(lCondicionesActuales[indice][23]);           // Precio energia de peaje
                    
                    jTextField16.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                    jTextField17.setText(lCondicionesActuales[indice][9]);            // Precio potencia P2
                    jTextField18.setText(lCondicionesActuales[indice][10]);           // Precio potencia P3
                    jTextField19.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                    jTextField35.setText(lCondicionesActuales[indice][3]);            // potencia contratada P2
                    jTextField36.setText(lCondicionesActuales[indice][4]);            // potencia contratada P3
                    jTextField111.setText(lCondicionesActuales[indice][24]);            // alquiler    
                    jTextArea2.setText(lCondicionesActuales[indice][20]);              // Observaciones  
                    
                    if (lCondicionesActuales[indice][25] != null) {
                        if (lCondicionesActuales[indice][25].equals("1") ){
                                jCheckBox5.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                        } else {

                                jCheckBox5.setSelected(false);  
                        }
                    }
                    break;
                    // _________________________________________________________________________________________________________
                    case 14:                                                                        // tarifa 6.1 indexado
                        jTabbedPane11.setSelectedIndex(1);
                           // ............................... Condiciones actuales
                    
                        jTextField244.setText(listaContratosPuntosAct[indice][1]);              // Fecha contrato
                        jTextField245.setText(listaContratosPuntosAct[indice][2]);              // Fecha fin de contrato
                        jTextField243.setText(listaContratosPuntosAct[indice][8]);              // Compañia
                        
                        jTextField246.setText(lCondicionesActuales[indice][14]);           // Precio energia P1
                        jTextField247.setText(lCondicionesActuales[indice][15]);           // Precio energia P2
                        jTextField248.setText(lCondicionesActuales[indice][16]);           // Precio energia P3
                        jTextField252.setText(lCondicionesActuales[indice][17]);           // Precio energia P1
                        jTextField253.setText(lCondicionesActuales[indice][18]);           // Precio energia P2
                        jTextField254.setText(lCondicionesActuales[indice][19]);           // Precio energia P3
                        
                        jTextField258.setText(lCondicionesActuales[indice][8]);            // Precio potencia P1
                        jTextField259.setText(lCondicionesActuales[indice][9]);            // Precio potencia P2
                        jTextField260.setText(lCondicionesActuales[indice][10]);           // Precio potencia P3
                        jTextField261.setText(lCondicionesActuales[indice][11]);           // Precio potencia P4
                        jTextField262.setText(lCondicionesActuales[indice][12]);           // Precio potencia P5
                        jTextField263.setText(lCondicionesActuales[indice][13]);           // Precio potencia P6
                        
                        jTextField249.setText(lCondicionesActuales[indice][2]);            // potencia contratada P1
                        jTextField250.setText(lCondicionesActuales[indice][3]);            // potencia contratada P2
                        jTextField251.setText(lCondicionesActuales[indice][4]);            // potencia contratada P3
                        jTextField255.setText(lCondicionesActuales[indice][5]);            // potencia contratada P4
                        jTextField256.setText(lCondicionesActuales[indice][6]);            // potencia contratada P5
                        jTextField257.setText(lCondicionesActuales[indice][7]);            // potencia contratada P6
                        
                        jTextField265.setText(lCondicionesActuales[indice][23]);            // Precio energía de peaje    
                        jTextField266.setText(lCondicionesActuales[indice][24]);            // alquiler    
                        jTextArea5.setText(lCondicionesActuales[indice][20]);              // Observaciones  

                        if (lCondicionesActuales[indice][25] != null) {
                            if (lCondicionesActuales[indice][25].equals("1") ){
                                    jCheckBox7.setSelected(true);                                 // Se factura siempre la Potencia Maxima en Simulacion
                            } else {

                                    jCheckBox7.setSelected(false);  
                            }
                        }                   
                    break;   
                        
            }     
               
               
           }
            
            // .........................................................................................................................
           }
// ------------------------------------------------------------------------------------------------------------------------
 private void actualizarTablaHistoricoPuntos(int id_punto) {
     
                System.out.println("----ACTUALIZO TABLA id_punto="+id_punto+"---");
           
                DefaultTableModel model;
		model = new DefaultTableModel();        // definimos el objeto tableModel
               
		miTabla01 = new JTable();                // creamos la instancia de la tabla
		miTabla01.setModel(model);
                
                                 
		model.addColumn("Fecha");                
		model.addColumn("D.Fct.Opt.");
		model.addColumn("A. Conseguido €");
		model.addColumn("A. Total €");
                model.addColumn("Coste Total €");
                model.addColumn("Coste Simulado €");
                model.addColumn("% Ahorro");
                
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                miTabla01.setDefaultRenderer(Double.class, formato); 
                miTabla01.setDefaultRenderer(String.class, formato); 
                miTabla01.setDefaultRenderer(Integer.class, formato);
                miTabla01.setDefaultRenderer(Object.class, formato);
		
               saepDao misaepDao2 = new saepDao();
                
                
		misaepDao2.consultaHistoricoAhorrosPunto(model,id_punto,this.id_cliente_actual);
              
                
                
                miBarra01.setViewportView(miTabla01);
                
 }    
  // -------------------------------------------------------------------------------------------------  
   public void modificarDatosPuntoSuministro() {
       
       String sqlStr="";
       int ind;
       String sFech1="", sFech2="" ;
       
       saepDao misaepDao3 = new saepDao();
       
       sFech1 = jTextField116.getText(); sFech1.trim();
       sFech1 = dateToMySQLDate(sFech1);       
       if (sFech1.length()==0) sFech1="2000-01-01 00:00";
       
       sFech2 = jTextField117.getText(); sFech2.trim();
       sFech2 = dateToMySQLDate(sFech2);
       if (sFech2.length()==0) sFech2="2001-01-01 00:00";
       
       if (this.id_punto_actual > 0) {
           
           
            ind = tipoMedida.getSelectedIndex() ;   if ( ind==0) ind=1; else ind=2 ;
            this.listaPuntosSum[this.indGen][16] = Integer.toString(ind);                  // Medida
                        
           ind = tipoSuministro.getSelectedIndex() ;  if ( ind==0) ind=1; else ind=2 ;              // Con o sin Centro de Transformacion
           this.listaPuntosSum[this.indGen][17]= Integer.toString(ind); 
           
           if (jCheckBox1.isSelected()) { this.listaPuntosSum[this.indGen][12] = "1";} else { this.listaPuntosSum[this.indGen][12] = "0"; }     //   Gas
           
           
            // .....................................

                 sqlStr  ="UPDATE t_datos_puntos_suministro SET ";
                 sqlStr +="id_cliente='"+this.id_cliente_actual+"',";
                 sqlStr +="nombre='"+jTextField1.getText()+"',"; 
                 sqlStr +="cups='"+jTextField2.getText()+"',"; 
                 sqlStr +="cif='"+jTextField3.getText()+"',"; 
                 sqlStr +="tarifa_actual='"+""+"',"; 
                 sqlStr +="id_tarifa_actual='"+this.listaContratosPuntosAct[this.indGen][3]+"',"; 
                 sqlStr +="bloque='"+jTextField59.getText()+"',"; 
                 sqlStr +="notas='"+jTextArea1.getText()+"',"; 
                 sqlStr +="fBateriaC='"+this.listaPuntosSum[this.indGen][12]+"',"; 
                 sqlStr +="TrfP='"+jTextField4.getText()+"',"; 
                 sqlStr +="TrfS='"+jTextField62.getText()+"',"; 
                 sqlStr +="TrfMax='"+jTextField61.getText()+"',"; 
                 sqlStr +="fMedida='"+this.listaPuntosSum[this.indGen][16]+"',"; 
                 
                 sqlStr +="batDesde='"+sFech1+"',"; 
                 sqlStr +="batHasta='"+sFech2+"',"; 
                 sqlStr +="cosfiP1='"+jTextField118.getText()+"',"; 
                 sqlStr +="cosfiP2='"+jTextField120.getText()+"',";                    
                 sqlStr +="batDescripcion='"+jTextField119.getText()+"',"; 
                 
                 sqlStr +="fCT='"+this.listaPuntosSum[this.indGen][17]+"'"; 
                 sqlStr +=" WHERE idd="+this.listaPuntosSum[this.indGen][0];
                 System.out.println(sqlStr);
                 misaepDao3.registrarFila(sqlStr);

           
       }
       
            this.listaPuntosSum[this.indGen][1]= jTextField1.getText();
            this.listaPuntosSum[this.indGen][2]=jTextField2.getText();
            this.listaPuntosSum[this.indGen][3]=jTextField3.getText();
        
            this.listaPuntosSum[this.indGen][10]=jTextField10.getText();
            this.listaPuntosSum[this.indGen][11]=jTextArea1.getText();
            this.listaPuntosSum[this.indGen][18]=jTextField59.getText();
                    
            this.listaPuntosSum[this.indGen][13]=jTextField4.getText() ;
            this.listaPuntosSum[this.indGen][14]=jTextField62.getText() ;
            this.listaPuntosSum[this.indGen][15]=jTextField61.getText() ;
       
        JOptionPane.showMessageDialog(null,
		 "EL REGISTRO SE HA MODIFICADO CORRECTAMENTE",
		 "Información",JOptionPane.INFORMATION_MESSAGE);
       
       
   }
// ------------------------------------------------------------------------------------------------------------------------
  public String dateToMySQLDate(String fecha)
    {
        String df,y,m,d;
        
               
        fecha=fecha.trim();
        
        if (fecha.length() >0 ){
        
        
        d = fecha.substring(0, 2) ; System.out.println("dia ="+d);
        m = fecha.substring(3,5) ;  System.out.println("mes ="+m);
        y = fecha.substring(6,10) ;  System.out.println("año ="+y);
        
        df = y+"-"+m+"-"+d+ " 00:00:00";
        } else {
            df = "" ;
        }
       
        return df;
        
       
    }
// ------------------------------------------------------------------------------------------------------------------------
 public void insertarFilaAhorro() {
     String sqlStr,sFech1,sFech2,sFech3 ;
     Double importe_AI, importe_DI ;
     int estadoInsert=0, id_tF=0,id_tipo_actual, id_tipo_sim,id_cond_actuales, id_cond_sim  ;
     
       sFech1 = jTextField6.getText(); sFech1.trim();
       sFech1 = dateToMySQLDate(sFech1);       
       
       sFech2 = jTextField7.getText(); sFech2.trim();
       sFech2 = dateToMySQLDate(sFech2);    
       
       sFech3 = jTextField8.getText(); sFech3.trim();
       sFech3 = dateToMySQLDate(sFech3);    
     
       
       
                    
       importe_AI = Double.valueOf(jTextField41.getText());
       importe_DI = Double.valueOf(jTextField42.getText());
       
       saepDao misaepDao3 = new saepDao();
       saepDao misaepDao4 = new saepDao();
     
       // .....................................
       try {
        id_tipo_actual      = Integer.parseInt(this.lCondicionesActuales[this.indGen][22]) ;
        id_tipo_sim         = Integer.parseInt(this.lCondicionesSimulacion[this.indGen][22]) ;
        
        id_cond_actuales    = Integer.parseInt(this.lCondicionesActuales[this.indGen][21]) ;
        id_cond_sim         = Integer.parseInt(this.lCondicionesSimulacion[this.indGen][21]) ; 
        
       } catch (NumberFormatException e) {
           
            id_tipo_actual      = this.id_tipo_Actual;
            id_tipo_sim         = this.id_tipo_Sim ;
            
            this.lCondicionesActuales[this.indGen][22]      = Integer.toString(id_tipo_actual);
            this.lCondicionesSimulacion[this.indGen][22]    = Integer.toString(id_tipo_sim) ;
            
            id_cond_actuales    = Integer.parseInt(this.lCondicionesActuales[this.indGen][21]) ;
            id_cond_sim         = Integer.parseInt(this.lCondicionesSimulacion[this.indGen][21]) ;
                    
       }   
       // .....................................
       int resp=JOptionPane.showConfirmDialog(null,"¿Quieres validar e insertar la línea de Ahorro?");
       
       if (JOptionPane.OK_OPTION == resp){  
        
        // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 2.0A a TARIFA 2.0A
        
       if ( this.tipo_Act == 1 && this.tipo_Sim== 1) {                               // .................................   INSERTAMOS EN t_f_20a
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20a (id_cliente,id_punto,p1_energia,p1_potencia,recargos,alquiler,p1_energia_simulada,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += jTextField19.getText()+",";
            sqlStr += jTextField85.getText()+",";
            sqlStr += jTextField80.getText()+",";
            sqlStr += energiaP1s.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20a ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                  
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
        // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 2.1A a TARIFA 2.1A
        
       if ( this.tipo_Act == 3 && this.tipo_Sim== 3) {                               // .................................   INSERTAMOS EN t_f_20a
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f21a (id_cliente,id_punto,p1_energia,p1_potencia,recargos,alquiler,p1_energia_simulada,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += jTextField19.getText()+",";
            sqlStr += jTextField85.getText()+",";
            sqlStr += jTextField80.getText()+",";
            sqlStr += energiaP1s.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f21a ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }   
            
            
      
     // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 2.0A a TARIFA 2.0ADH
        
       if ( this.tipo_Act == 2 && this.tipo_Sim== 1) {                               // .................................   INSERTAMOS EN t_f_20a
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20dha (id_cliente,id_punto,p1_energia,p2_energia,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20dha ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
       // .......................................................................................................................................  
        // .....................................         INSERTA  TARIFA 2.0DHA INDX
        
       if ( this.tipo_Act == 8 ) {                               // .................................   INSERTAMOS EN t_f_21dhaIndx
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20dhaindx (id_cliente,id_punto,p1_energia,p2_energia,energia_peaje,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += jTextField75.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                  // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                 // .....................................  
                
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20dhaindx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // 
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
      // .......................................................................................................................................  
        // .....................................         INSERTA  TARIFA 2.1DHA INDX
        
       if ( this.tipo_Act == 9 ) {                               // .................................   INSERTAMOS EN t_f_21dhaIndx
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f21dhaindx (id_cliente,id_punto,p1_energia,p2_energia,energia_peaje,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += jTextField75.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                  // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                 // .....................................  
                
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f21dhaindx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
       // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 2.1DHA a TARIFA 2.0DHA
        
       if ( this.tipo_Act == 2 && this.tipo_Sim== 4) {                               // .................................   INSERTAMOS EN t_f_20a
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20DHa (id_cliente,id_punto,p1_energia,p2_energia,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20dha ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
        // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 2.1DHA a TARIFA 2.0DHA INDEX
        
       if ( this.tipo_Act == 8 && this.tipo_Sim== 4) {                               // .................................   INSERTAMOS EN t_f_20a
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20dhaindx (id_cliente,id_punto,p1_energia,p2_energia,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20dhaindx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
        // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 3.0 INDEX a TARIFA 3.0 INDEX
        
       if ( this.tipo_Act == 10 && this.tipo_Sim== 10) {                               // .................................   INSERTAMOS EN t_f_30aindx
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f30aindx (id_cliente,id_punto,p1_energia,p2_energia,p3_energia,p1_potencia,p2_potencia,p3_potencia,energia_peaje,penalizacion_reactiva,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI,alquiler,reactiva_facturada_p1,reactiva_facturada_p2,reactiva_simulada_p1,reactiva_simulada_p2,potencia_facturada_p1,potencia_facturada_p2,potencia_facturada_p3) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += energiaP3.getText()+",";
            sqlStr += jTextField77.getText()+",";
            sqlStr += jTextField78.getText()+",";
            sqlStr += jTextField79.getText()+",";
            sqlStr += jTextField75.getText()+",";
            sqlStr += jTextField85.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+",";
            sqlStr += jTextField80.getText()+",";
            sqlStr += jTextField109.getText()+",";
            sqlStr += jTextField110.getText()+",";
            
            sqlStr += jTextField121.getText()+",";
            sqlStr += jTextField122.getText()+",";
            
            sqlStr += jTextField113.getText()+",";
            sqlStr += jTextField114.getText()+",";
            sqlStr += jTextField115.getText()+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f30aindx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                          // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
         // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 3.0 INDEX a TARIFA 3.0 INDEX 0 DE TARIFA 3.0 A a TARIFA 3.0 INDEX  
        
       if ( this.tipo_Act == 10 && ( this.tipo_Sim== 5   || this.tipo_Sim== 10 || this.tipo_Sim== 3 )) {                               // .................................   INSERTAMOS EN t_f_30aindx
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f30aindx (id_cliente,id_punto,p1_energia,p2_energia,p3_energia,p1_potencia,p2_potencia,p3_potencia,energia_peaje,penalizacion_reactiva,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI,alquiler,reactiva_facturada_p1,reactiva_facturada_p2,reactiva_simulada_p1,reactiva_simulada_p2,p1_energia_simulada,p2_energia_simulada,p3_energia_simulada,potencia_facturada_p1,potencia_facturada_p2,potencia_facturada_p3) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += energiaP3.getText()+",";
            sqlStr += jTextField77.getText()+",";
            sqlStr += jTextField78.getText()+",";
            sqlStr += jTextField79.getText()+",";
            sqlStr += jTextField75.getText()+",";
            sqlStr += jTextField85.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+",";
            sqlStr += jTextField80.getText()+",";
            sqlStr += jTextField109.getText()+",";
            sqlStr += jTextField110.getText()+",";
            
            sqlStr += jTextField121.getText()+",";
            sqlStr += jTextField122.getText()+",";
            
            sqlStr += energiaP1s.getText()+",";
            sqlStr += energiaP2s.getText()+",";
            sqlStr += energiaP3s.getText()+",";
            
            sqlStr += jTextField113.getText()+",";
            sqlStr += jTextField114.getText()+",";
            sqlStr += jTextField115.getText()+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f30aindx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                         // flag ultimo calculo
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
     
      // .......................................................................................................................................  
      // .....................................         INSERTA FILA  DE TARIFA 2.0A a TARIFA 2.0A INDX
        
       if ( this.tipo_Act == 11 && this.tipo_Sim== 1) {                               // .................................   INSERTAMOS EN t_F20INDX
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20indx (id_cliente,id_punto,p1_energia,energia_peaje,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20indx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                  
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                        // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
         // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 2.10A a TARIFA 2.1A INDX
        
       if ( this.tipo_Act == 12 && this.tipo_Sim== 3) {                               // .................................   INSERTAMOS EN t_F20INDX
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f21indx (id_cliente,id_punto,p1_energia,energia_peaje,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f21indx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                  
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                        // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
       // .......................................................................................................................................  
      // .....................................         INSERTA FILA  DE TARIFA 2.1A a TARIFA 2.0A INDX
        
       if ( this.tipo_Act == 11 && this.tipo_Sim== 3) {                               // .................................   INSERTAMOS EN t_F20INDX
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f20indx (id_cliente,id_punto,p1_energia,energia_peaje,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f20indx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                  
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                        // tipo de condiciones actuales
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
         // .......................................................................................................................................  
        // .....................................         INSERTA FILA  DE TARIFA 3.1 INDEX (o 3.0  A) a TARIFA 3.0 INDEX
        
       if ( this.tipo_Act == 13 && (this.tipo_Sim== 13 || this.tipo_Sim== 5 || this.tipo_Sim== 6)) {                               // .................................   INSERTAMOS EN t_f_30aindx
          
            // .....................................
          
            sqlStr  ="INSERT INTO t_f31aindx (id_cliente,id_punto,p1_energia,p2_energia,p3_energia,p1_potencia,p2_potencia,p3_potencia,energia_peaje,penalizacion_reactiva,fecha_factura,fecha_inicio,fecha_fin,importe_AI,importe_DI,alquiler,reactiva_facturada_p1,reactiva_facturada_p2,reactiva_simulada_p1,reactiva_simulada_p2,p1_energia_simulada,p2_energia_simulada,p3_energia_simulada,potencia_facturada_p1,potencia_facturada_p2,potencia_facturada_p3) VALUES (";
            sqlStr += this.id_cliente_actual+",";
            sqlStr += this.id_punto_actual+",";
            sqlStr += energiaP1.getText()+",";
            sqlStr += energiaP2.getText()+",";
            sqlStr += energiaP3.getText()+",";
            sqlStr += jTextField77.getText()+",";
            sqlStr += jTextField78.getText()+",";
            sqlStr += jTextField79.getText()+",";
            sqlStr += jTextField75.getText()+",";
            sqlStr += jTextField85.getText()+",";
            sqlStr += "'"+sFech1+"',";
            sqlStr += "'"+sFech2+"',";
            sqlStr += "'"+sFech3+"',";            
            sqlStr += importe_AI+",";
            sqlStr += importe_DI+",";
            sqlStr += jTextField80.getText()+",";
            sqlStr += jTextField109.getText()+",";
            sqlStr += jTextField110.getText()+",";
            
            sqlStr += jTextField121.getText()+",";
            sqlStr += jTextField122.getText()+",";
            
            sqlStr += energiaP1s.getText()+",";
            sqlStr += energiaP2s.getText()+",";
            sqlStr += energiaP3s.getText()+",";
            
            sqlStr += jTextField113.getText()+",";
            sqlStr += jTextField114.getText()+",";
            sqlStr += jTextField115.getText()+")";
            
            System.out.println(sqlStr);
            estadoInsert= misaepDao3.registrarFila(sqlStr);
            
            // .....................................
            if (estadoInsert==0){
                
                // .....................................                                consultamos el último id que se ha asignado
                
                sqlStr = "SELECT id FROM t_f31aindx ORDER BY id DESC LIMIT 1 ";
                
                estadoInsert= misaepDao3.ultimoIdentificador(sqlStr);
                
                id_tF               = misaepDao3.id ;
               
                
                // .....................................   
                
                if (estadoInsert==0){
                    
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_ahorros_historico SET fUltimCalc=0 WHERE id_punto="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                    // .....................................  
                    
                    
                    sqlStr  ="INSERT INTO t_ahorros_historico (id_cliente,id_punto,fecha,dias_facturacion_optimizada,ahorro_conseguido,ahorro_total,coste_actual," ;
                    sqlStr +="coste_simulado,porcentaje,id_factura,id_cond_actual,id_cond_sim,id_tipo_actual,fUltimCalc,id_tipo_sim) VALUES (" ;
                    
                    sqlStr += this.id_cliente_actual+",";                   // id cliente
                    sqlStr += this.id_punto_actual+",";                     // id punto
                    sqlStr += "'"+sFech1+"',";                              // fecha
                    sqlStr += jTextField40.getText()+",";                    // dias de facturacion
                    sqlStr += jTextField43.getText()+",";                   // ahorro conseguido
                    sqlStr += jTextField44.getText()+",";                   // ahorro total conseguido
                    sqlStr += importe_AI+",";                               // coste actual
                    sqlStr += jTextField45.getText()+",";                   // coste simulado
                    sqlStr += jTextField46.getText()+",";                   // porcentaje
                    sqlStr += id_tF+",";                                    // identificador de factura
                    sqlStr += id_cond_actuales+",";                         // identificador de condiciones actuales
                    sqlStr += id_cond_sim+",";                              // identificador de condiciones simuladas
                    sqlStr += this.tipo_Act+",";                            // tipo de condiciones actuales
                    sqlStr += "1,";                                         // flag ultimo calculo
                    sqlStr += id_tipo_sim+")";                              // tipo de condiciones simuladas

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao4.registrarFila(sqlStr);
                    
                    
                } else {
                JOptionPane.showMessageDialog(null,
                                                     "\nError identificador factura no valido, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);    
                }
                
                // .....................................
                
            }   else {
                JOptionPane.showMessageDialog(null,
                                                     "\nNo se ha realizado validación, error insertado en tabla histórico, revisa los datos de entrada por favor.",
                                                     "ADVERTENCIA!!!",JOptionPane.WARNING_MESSAGE);
                
            }       
            
            // .....................................
            
       }
       }
 }
 // ------------------------------------------------------------------------------------------------------------------------
 public void calculoPreliminarPunto(){
     
     double pEP1,pEP2,pEP3,pPP1,pPP2,pPP3 ;
     double P1M,P2M,P3M, PF1a,PF2a,PF3a,PF1s,PF2s,PF3s ;
     double pEP1s,pEP2s,pEP3s,pPP1s,pPP2s,pPP3s,pEP ;
     double aEP1,aEP2,aEP3,aPP1,aPP2,aPP3,aE,aP ;
     double eEP1,eEP2,eEP3,eEP=0,pEPa =0,pEPs =0,eEP1s,eEP2s,eEP3s,eEPs=0 ;
     double ahorro, ahorro_total, ahorro_acumulado ;
     double coste_actual, coste_simulado, coste_actual_DI, porcentaje;
     double pPC1,pPC2,pPC3 ;
     double psPC1,psPC2,psPC3 ;
     double pAlquiler, pBIF, pBIC=0, pReactiva=0, psAlquiler ;
     double PR=0 ;
     double cosfiP1=0, cosfiP2=0 ;
     double eRSP1=0, eRSP2=0 ;
     String seRSP1="", seRSP2="",sCRS="" ;
     double costeReactivaSim=0 ;
     double pBonificacion=0;
     
     int indice,dias,diasOptimizado,diasCond=0 ;
     
     double impuesto_electrico  = 1.051127 ;
     double impuesto_iva        = 1.21 ;
     double costeReactiva       = 0.041554 ;
     
     String sFecha1="", sFecha2="";
     String sAhorro, sCoste_Actual, sCoste_Simulado, sAhorro_Total, sPorcentaje, spBIC ;
     
     indice = this.indGen ;
     
     this.sMensajes = "";
    
     // .......................................................................
     
     sFecha1    = jTextField7.getText(); sFecha1.trim();
     sFecha2    = jTextField8.getText(); sFecha2.trim();
     
     pAlquiler  = Double.valueOf(jTextField80.getText());                           // precio del alquiler en factura
   
     
     pBIF       = Double.valueOf(jTextField81.getText());                           // precio base imponible de factura
     
     dias = diferenciaFechas(sFecha1, sFecha2 ,1);
     psAlquiler  = Double.valueOf(jTextField112.getText());
     // .......................................................................
     if ( psAlquiler == 0  ) {
            psAlquiler  = pAlquiler ;
     } else {
            psAlquiler  = dias * Double.valueOf(jTextField112.getText());                  // precio del alquiler en simulacion
     }
     // .......................................................................
     try {
         
        ahorro_total       = Double.valueOf(String.valueOf(miTabla01.getValueAt(0, 3))) ;
        ahorro_acumulado   = Double.valueOf(String.valueOf(miTabla01.getValueAt(0, 3))) ;
        diasOptimizado     = Integer.valueOf(String.valueOf(miTabla01.getValueAt(0, 1))) ; System.out.println("diasOptimizado="+diasOptimizado);
     } catch ( ArrayIndexOutOfBoundsException ex) {
        ahorro_total       = 0 ; 
        ahorro_acumulado   = 0 ;
        diasOptimizado     = 0 ;
     }
     // .......................................................................
     String sFecha1Cond=""; 
     
     sFecha1Cond = jTextField117.getText(); sFecha1Cond.trim();
    
     if ( sFecha1Cond.length() > 0) {
           
         diasCond = diferenciaFechas(sFecha1,sFecha1Cond ,1); System.out.println("Dias restantes ("+sFecha1Cond+"-"+sFecha1+") para calcular optimización reactiva:"+diasCond) ;
         
     }
     // .......................................................................
     
     NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

     formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
         
     NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
     
     NumberFormat formatoNumero = NumberFormat.getNumberInstance();
     
     formatoNumero.setMaximumFractionDigits(2);
     
     jTextField40.setText(String.valueOf(diasOptimizado));
     
     jTextField82.setBackground(Color.white);
     jTextField80.setBackground(new Color(0xCCFFCC)); 
     jTextField81.setBackground(new Color(0xCCFFCC));  
     
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.0A a TARIFA 2.0A
            
     // .......................................................................
     if ( this.tipo_Act == 1 && this.tipo_Sim== 1) {
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                 // Energía consumida en P1
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                  // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                // Precio en Energía en P1 Simulado (contrato anterior)
         
        
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                   // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                 // Precio en Potencia en P1 Simulado
                                 
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         pPC1 = Double.parseDouble(jTextField19.getText());                                                // Potencia contratada actual P1
         psPC1= Double.parseDouble(jTextField26.getText());                                                // Potencia contratada actual P1
         pReactiva = Double.parseDouble(jTextField85.getText());                                           // Penalización reactiva 
         
         aEP1 = (eEP1 * (pEP1s-pEP1)) ;                                                 // Ahorro en el consumo de energía
         aPP1 = (dias * ((pPP1s*psPC1)-(pPP1*pPC1))) ;                                                 // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1 ) ;                                  // Ahorro total Energia + Potencia
        
         ahorro_total = ahorro_total + ahorro ;                                         // Ahorro acumulado total
         
         coste_actual   =  ((eEP1 * pEP1) + (dias * pPP1 * pPC1 ) + pReactiva) ;       // Coste con tarifa actual
         
         coste_simulado =  ((eEP1 * pEP1s) + (dias * pPP1s *psPC1 ) + pReactiva);     // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_iva * coste_actual ;                             // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                     // Porcentaje de ahorro
         
        
         
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                         // Precio base de factura calculado
         
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));     
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual (pPC1)= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada (psPC1)= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 (eEP1) = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual (pEP1)= "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) (pEP1s) = "+pEP1s+" €/kWh" +"\n \n" ;        
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual (pPP1)= "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado (pPP1s)= "+pPP1s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Penalizaciones (pReactiva)= "+pReactiva+" €" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ;
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste potencia  ->((dias * pPP1 * pPC1)))= \n"+formatoImporte.format((dias * pPP1 * pPC1 )) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste energia   ->((eEP1 * pEP1) )= \n"+formatoImporte.format((eEP1 * pEP1 )) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 * pPC1 )) + pReactiva)= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1s * psPC1)) + pReactiva)= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.1A a TARIFA 2.1A
            
     // .......................................................................
     if ( this.tipo_Act == 3 && this.tipo_Sim== 3) {
         
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                 // Energía consumida en P1
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                  // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                // Precio en Energía en P1 Simulado (contrato anterior)
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                   // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                 // Precio en Potencia en P1 Simulado
         
         
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         pPC1 = Double.parseDouble(jTextField19.getText());                                                // Potencia contratada actual P1
         psPC1= Double.parseDouble(jTextField26.getText());                                                // Potencia contratada actual P1
         pReactiva = Double.parseDouble(jTextField85.getText());                                           // Penalización reactiva 
        
         
         aEP1 = (eEP1 * (pEP1s-pEP1)) ;                                                 // Ahorro en el consumo de energía
         aPP1 = (dias * ((pPP1s*psPC1)-(pPP1*pPC1)) ) ;                                                 // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                  // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                         // Ahorro acumulado total
         
         coste_actual   =  ((eEP1 * pEP1) + (dias * pPP1 * pPC1 ) + pReactiva) ;       // Coste con tarifa actual
         
         coste_simulado =  ((eEP1 * pEP1s) + (dias * pPP1s *psPC1 ) + pReactiva);     // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_iva * coste_actual ;                             // Coste total de la factura después de impuestos.
         
        porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                     // Porcentaje de ahorro
         
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                    // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................      
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual (pPC1)= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada (psPC1)= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 (eEP1) = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual (pEP1)= "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) (pEP1s) = "+pEP1s+" €/kWh" +"\n \n" ;        
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual (pPP1)= "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado (pPP1s)= "+pPP1s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Penalizaciones (pReactiva)= "+pReactiva+" €" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ;
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste potencia  ->((dias * pPP1 * pPC1)))= \n"+formatoImporte.format((dias * pPP1 * pPC1 )) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste energia   ->((eEP1 * pEP1) )= \n"+formatoImporte.format((eEP1 * pEP1 )) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 * pPC1 )) + pReactiva)= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1s * psPC1)) + pReactiva)= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................        
      // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.1ADH a TARIFA 2.1ADH
            
     // .......................................................................
     if ( this.tipo_Act == 4 && this.tipo_Sim== 4) {
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1
          
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP2s = Double.parseDouble(lCondicionesSimulacion[indice][15]) ;                                   // Precio en Energía en P2 Simulado (contrato anterior)
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
                                            
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
        
         
         
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP2s-pEP2)) ;                                            // Ahorro en el consumo de energía
         aPP1 = (dias * (pPP1s-pPP1)) ;                                                                     // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2)  ) ;         // Coste con tarifa actual
         
         coste_simulado = impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP2s)) ;       // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                     // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                        // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
              
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
         
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP2s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP2s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.0A a TARIFA 2.0DHA
            
     // .......................................................................
     if ( this.tipo_Act == 2 && this.tipo_Sim== 1) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO     DE TARIFA 2.0A a TARIFA 2.0DHA") ;
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                 // Potencia contratada en P1 Simulada
       
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
                                            
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
        
         
         
         
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP1s-pEP2))  ;                                           // Ahorro en el consumo de energía
         aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                             // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (pPC1 * dias * pPP1 ) + (eEP2 * pEP2)  ) ;                       // Coste con tarifa actual
         
         coste_simulado = ((eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) + (eEP2 * pEP1s)) ;                             // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                         // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual = "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada = "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP1s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.0A a TARIFA 2.0DHA INDEXADO
            
     // .......................................................................
      if ( this.tipo_Act == 8 && this.tipo_Sim== 1) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO     DE TARIFA 2.0DHA Indexado a TARIFA 2.0A") ;
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
        
         eEP = eEP1 + eEP2 ;   jTextField75.setText(String.valueOf(eEP));             // Energía de peaje                                                               // Energía de peaje
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP   = Double.parseDouble(jTextField73.getText());                                                // Precio en Energía de Peaje
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
      
                                            
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP1s-pEP2)) - (eEP * pEP) ;                              // Ahorro en el consumo de energía
         aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                             // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (pPC1 * dias * pPP1 ) + (eEP2 * pEP2) + (eEP * pEP) ) ;          // Coste con tarifa actual
        
         coste_simulado = ((eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) + (eEP2 * pEP1s)) ;                     // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................                             // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                         // Precio base de factura calculado
         pBIC    =Math.rint(pBIC*100)/100 ;
         spBIC   = formatoImporte.format(pBIC);
         System.out.println("pBIC  == pBIF ("+pBIC+")=("+pBIF+")");
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía de Peaje = "+formatoNumero.format(eEP)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1-P2= "+formatoNumero.format(pPC1)+" kW" +"\n" ;       
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1 = "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en Energia de Peaje con contrato actual = "+pEP+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1-P2 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste Enegia de peaje ->(eEP * pEP)= \n"+formatoImporte.format(eEP * pEP) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2) +  (eEP * pEP)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP1s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.1A a TARIFA 2.1DHA
            
     // .......................................................................
      if ( this.tipo_Act == 9 && this.tipo_Sim== 3) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO     DE TARIFA 2.1A TARIFA 2.1DHA INDX") ;
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         
         eEP = eEP1 + eEP2 ;   jTextField75.setText(String.valueOf(eEP));             // Energía de peaje                                                                      // Energía de peaje
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP   = Double.parseDouble(jTextField73.getText());                                                // Precio en Energía de Peaje
         
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
   
         
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
                                            
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP1s-pEP2)) - (eEP * pEP) ;                              // Ahorro en el consumo de energía
         aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                             // Ahorro en potencia contratada
         
         
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (pPC1 * dias * pPP1 ) + (eEP2 * pEP2) + (eEP * pEP) ) ;          // Coste con tarifa actual
         
         coste_simulado = ((eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) + (eEP2 * pEP1s)) ;                     // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                            // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                         // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));             // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía de Peaje = "+formatoNumero.format(eEP)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1-P2= "+formatoNumero.format(pPC1)+" kW" +"\n" ;       
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1 = "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en Energia de Peaje con contrato actual = "+pEP+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1-P2 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste Enegia de peaje ->(eEP * pEP)= \n"+formatoImporte.format(eEP * pEP) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2) +  (eEP * pEP)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP1s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
      // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.1A a TARIFA 2.0DHA
            
     // .......................................................................
      if ( this.tipo_Act == 8 && this.tipo_Sim== 3) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO     DE TARIFA 2.1DHA Indexado a TARIFA 2.0A") ;
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         
         eEP = eEP1 + eEP2 ;   jTextField75.setText(String.valueOf(eEP));             // Energía de peaje                                                                      // Energía de peaje
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP   = Double.parseDouble(jTextField73.getText());                                                // Precio en Energía de Peaje
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
      
                                            
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP1s-pEP2)) - (eEP * pEP) ;                              // Ahorro en el consumo de energía
         aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                             // Ahorro en potencia contratada
         
         
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (pPC1 * dias * pPP1 ) + (eEP2 * pEP2) + (eEP * pEP) ) ;          // Coste con tarifa actual
         
         coste_simulado = ((eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) + (eEP2 * pEP1s)) ;                     // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                            // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                         // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));             // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía de Peaje = "+formatoNumero.format(eEP)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1-P2= "+formatoNumero.format(pPC1)+" kW" +"\n" ;       
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1 = "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en Energia de Peaje con contrato actual = "+pEP+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1-P2 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste Enegia de peaje ->(eEP * pEP)= \n"+formatoImporte.format(eEP * pEP) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2) +  (eEP * pEP)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP1s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 3.0A a TARIFA 3.0A
            
     // .......................................................................
     if ( this.tipo_Act == 5 && this.tipo_Sim== 5) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO DE TARIFA 3.0A a TARIFA 3.0A") ;
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         eEP3 = Double.parseDouble(energiaP3.getText());                                                    // Energía consumida en P3
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pPC2 = Double.parseDouble(jTextField35.getText());                                                 // Potencia contratada en P2 Actual
         psPC2 = Double.parseDouble(jTextField37.getText());                                                // Potencia contratada en P2 Simulada
         
         pPC3 = Double.parseDouble(jTextField36.getText());                                                 // Potencia contratada en P3 Actual
         psPC3 = Double.parseDouble(jTextField38.getText());                                                // Potencia contratada en P3 Simulada
         
         // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
         P3M = Double.parseDouble(jTextField79.getText());                                                 // Potencia de maximetro P3
         
         PR  = Double.parseDouble(jTextField85.getText());                                                 // Penalización de reactiva
         // ..................................................................                              COMPENSACIÓN DE REACTIVA
         costeReactivaSim =0 ;
         if (jCheckBox1.isSelected() && diasCond>0) {
           
            cosfiP1 = Double.parseDouble(jTextField118.getText());                                            // cos fi P1
            cosfiP2 = Double.parseDouble(jTextField120.getText());                                            // cos fi P1
            
            eRSP1   = (Math.tan(Math.acos(cosfiP1)) * eEP1) - 0.33 * eEP1 ;                                 // KVAr P1 simulada 
            eRSP2   = (Math.tan(Math.acos(cosfiP2)) * eEP2) - 0.33 * eEP2 ;                                 // KVAr P2 simulada
            
            if (eRSP1 < 0) eRSP1 = 0 ;
            if (eRSP2 < 0) eRSP2 = 0 ;
            
            seRSP1 = formatoNumero.format(eRSP1); System.out.println(String.valueOf(Math.tan(Math.acos(cosfiP1))));
            seRSP2 = formatoNumero.format(eRSP2);
            
            costeReactivaSim = (eRSP1 * costeReactiva) + (eRSP2 * costeReactiva) ; sCRS = formatoImporte.format(costeReactivaSim);
            
            
            jTextField121.setText(String.valueOf(redondear(eRSP1,2))) ;
            jTextField122.setText(String.valueOf(redondear(eRSP2,2))) ;
            jTextField123.setText(sCRS) ;
            
         }
         
         
        // ..................................................................
        if (this.fPotenciaFacturada == 0) {                                      // Si se factura por maximetro, aplicamos cálculos formulas
         
        if ( jCheckBox5.isSelected()) fReglaPotenciaActual=0 ; else fReglaPotenciaActual=1;
         
         
        if (fReglaPotenciaActual == 1 ) {                                                                     // Si la compañia aplica regla de potencia
         
        if ((P1M/pPC1)>= 1.05 ) {   PF1a =  P1M + 2 * ( P1M - 1.05 * pPC1); } else {                   // Potencia a facturar P1 actual
            
                                    PF1a = 0.85 * pPC1 ;
        }   
        
        if ((P2M/pPC2)>= 1.05 ) {   PF2a =  P2M + 2 * ( P2M - 1.05 * pPC2); } else {                   // Potencia a facturar P2 actual
            
                                    PF2a = 0.85 * pPC2 ;
        }
        
        if ((P3M/pPC3)>= 1.05 ) {   PF3a =  P3M + 2 * ( P3M - 1.05 * pPC3); } else {                   // Potencia a facturar P3 actual
            
                                    PF3a = 0.85 * pPC3 ;
        }
        } else {
                                    PF1a = P1M ; PF2a = P2M ; PF3a = P3M ;
        }
        // ...................................................................
                                                                            // Si no se asigna directamente la potencia facturada.
        } else {
            System.out.println("-> Voy a utilizar las potencias tal cual aparecen en la factura... ¡Aviso!");
            PF1a = P1M ;
            PF2a = P2M ;
            PF3a = P3M ;
        }
        
        
        if ( jCheckBox6.isSelected()) fReglaPotenciaSimulado=0 ; else fReglaPotenciaSimulado=1; 
        
        if (fReglaPotenciaSimulado == 1 ) {  
         if ((P1M/pPC1)>= 1.05 ) {  PF1s =  P1M + 2 * ( P1M - 1.05 * psPC1); } else {                   // Potencia a facturar P1 simulada
            
                                    PF1s = 0.85 * psPC1 ;
        }   
        
        if ((P2M/pPC2)>= 1.05 ) {   PF2s =  P2M + 2 * ( P2M - 1.05 * psPC2); } else {                   // Potencia a facturar P2 simulada
            
                                    PF2s = 0.85 * psPC2 ;
        }
        
        if ((P3M/pPC3)>= 1.05 ) {   PF3s =  P3M + 2 * ( P3M - 1.05 * psPC3); } else {                   // Potencia a facturar P3 simulada
            
                                    PF3s = 0.85 * psPC3 ;
        }
        } else {
                                    PF1s = P1M ; PF2s = P2M ; PF3s = P3M ;
        } 
         // .................................................................. 
         
        jTextField106.setText(String.valueOf(PF1a));                                  // Potencia a facturar P1 actual 
        jTextField107.setText(String.valueOf(PF2a));                                  // Potencia a facturar P2 actual
        jTextField108.setText(String.valueOf(PF3a));                                  // Potencia a facturar P3 actual
        jTextField113.setText(String.valueOf(PF1s));                                  // Potencia a facturar P1 simulada 
        jTextField114.setText(String.valueOf(PF2s));                                  // Potencia a facturar P2 simulada
        jTextField115.setText(String.valueOf(PF3s));                                  // Potencia a facturar P3 simulada
        
        
         // .................................................................. 
        
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP2s  = Double.parseDouble(lCondicionesSimulacion[indice][15]) ;                                  // Precio en Energía en P2 con contrato simulado
         
         pEP3  = Double.parseDouble(lCondicionesActuales[indice][16]) ;                                     // Precio en Energía en P3 con contrato actual
         pEP3s  = Double.parseDouble(lCondicionesSimulacion[indice][16]) ;                                  // Precio en Energía en P3 con contrato simulado
         
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
         pPP2  = Double.parseDouble(lCondicionesActuales[indice][9]) ;                                      // Precio en Potencia en P2 Actual
         pPP2s = Double.parseDouble(lCondicionesSimulacion[indice][9]) ;                                    // Precio en Potencia en P2 Simulado
         
         pPP3  = Double.parseDouble(lCondicionesActuales[indice][10]) ;                                      // Precio en Potencia en P3 Actual
         pPP3s = Double.parseDouble(lCondicionesSimulacion[indice][10]) ;                                    // Precio en Potencia en P3 Simulado
                      
         pReactiva = Double.parseDouble(jTextField85.getText());                                            // Coste de penalización REACTIVA
         
         aEP1 = (eEP1 * (pEP1s-pEP1))  ;                                                                     // Ahorro en el consumo de energía P1
         aEP2 = (eEP2 * (pEP2s-pEP2))  ;                                                                     // Ahorro en el consumo de energía P2
         aEP3 = (eEP3 * (pEP3s-pEP3))  ;                                                                     // Ahorro en el consumo de energía P3
         aE   = aEP1 + aEP2 + aEP3 ;
         
         
        // aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                          // Ahorro en potencia contratada P1
        // aPP2 = (psPC2 * dias * pPP2s) - (pPC2 * dias * pPP2) ;                                          // Ahorro en potencia contratada P2
        // aPP3 = (psPC3 * dias * pPP3s) - (pPC3 * dias * pPP3) ;                                          // Ahorro en potencia contratada P3
         
         aPP1 = (PF1s * dias * pPP1s) - (PF1a * dias * pPP1) ;                                             // Ahorro en potencia contratada P1
         aPP2 = (PF2s * dias * pPP2s) - (PF2a * dias * pPP2) ;                                             // Ahorro en potencia contratada P2
         aPP3 = (PF3s * dias * pPP3s) - (PF3a * dias * pPP3) ;                                             // Ahorro en potencia contratada P3
         
         aP   = aPP1+aPP2+aPP3 ;
         
         ahorro = impuesto_electrico * ((aEP1 + aEP2 + aEP3 + aPP1 + aPP2 + aPP3  ) + costeReactivaSim) ;   // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = (eEP1 * pEP1) + (pPC1 * dias * pPP1 )  ;                                          // Coste con tarifa actual 
         coste_actual  += (eEP2 * pEP2) + (pPC2 * dias * pPP2 )  ;
         coste_actual  += (eEP3 * pEP3) + (pPC3 * dias * pPP3 )  ;
         
         
         coste_simulado = (eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) ;                     // Coste con tarifa simulada
         coste_simulado +=(eEP2 * pEP2s) + (psPC2 * dias * pPP2s ) ;
         coste_simulado +=(eEP3 * pEP3s) + (psPC3 * dias * pPP3s ) ;
         
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................                             // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler + pReactiva ;                              // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P3 = "+formatoNumero.format(eEP3)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P2= "+formatoNumero.format(pPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P2= "+formatoNumero.format(psPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P3= "+formatoNumero.format(pPC3)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P3= "+formatoNumero.format(psPC3)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P1= "+formatoNumero.format(P1M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P2= "+formatoNumero.format(P2M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P3= "+formatoNumero.format(P3M)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 ACTUAL= "+formatoNumero.format(PF1a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 ACTUAL= "+formatoNumero.format(PF2a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 ACTUAL= "+formatoNumero.format(PF3a)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 SIMULADA= "+formatoNumero.format(PF1s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 SIMULADA= "+formatoNumero.format(PF2s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 SIMULADA= "+formatoNumero.format(PF3s)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP2s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 con contrato actual = "+pEP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 Simulado (contrato anterior) = "+pEP3s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Actual = "+pPP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Simulado = "+pPP2s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Actual = "+pPP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Simulado = "+pPP3s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual   -> (impuesto_electrico * ((eEP1 * pEP1) + (eEP2 * pEP2) + (eEP3 * pEP3) ) + (dias * pPP1 * pPC1 ) + (dias * pPP2 * pPC2 ) +( dias * pPP3 * pPC3 )))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (eEP2 * pEP2s) + (eEP3 * pEP3s) ) + (dias * pPP1s * pPC1s ) + (dias * pPP2s * pPC2s ) +( dias * pPP3s * pPC3s )))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aE) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aP) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
      // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.1DHA a TARIFA 2.0DHA INDEXADO
            
     // .......................................................................
      if ( this.tipo_Act == 8 && this.tipo_Sim== 4) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO     DE TARIFA 2.0DHA Indexado a TARIFA 2.0A") ;
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
        
         eEP = eEP1 + eEP2 ;   jTextField75.setText(String.valueOf(eEP));             // Energía de peaje                                                               // Energía de peaje
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP   = Double.parseDouble(jTextField73.getText());                                                // Precio en Energía de Peaje
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
                                            
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
         
         
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP1s-pEP2)) - (eEP * pEP) ;                              // Ahorro en el consumo de energía
         aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                             // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (pPC1 * dias * pPP1 ) + (eEP2 * pEP2) + (eEP * pEP) ) ;          // Coste con tarifa actual
        
         coste_simulado = ((eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) + (eEP2 * pEP1s)) ;                             // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
        pBIC    = impuesto_electrico * (coste_actual + PR) + pAlquiler ;                                         // Precio base de factura calculado
        spBIC   = formatoImporte.format(pBIC);
        pBIC    =Math.rint(pBIC*100)/100 ; 
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC));  
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía de Peaje = "+formatoNumero.format(eEP)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1-P2= "+formatoNumero.format(pPC1)+" kW" +"\n" ;       
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1 = "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en Energia de Peaje con contrato actual = "+pEP+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1-P2 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste Enegia de peaje ->(eEP * pEP)= \n"+formatoImporte.format(eEP * pEP) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2) +  (eEP * pEP)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP1s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 3.0A INDX a TARIFA 3.0A INDX
            
     // .......................................................................
     
      
     if ( this.tipo_Act == 10 && this.tipo_Sim== 10) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO DE TARIFA 3.0A INDEX a TARIFA 3.0A INDX") ;
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         eEP3 = Double.parseDouble(energiaP3.getText());                                                    // Energía consumida en P3
         
         eEP = eEP1 + eEP2 + eEP3 ;   jTextField75.setText(String.valueOf(eEP));     // Energía de peaje    
         
          eEP1s= Double.parseDouble(energiaP1s.getText());                                                    // Energía simulada consumida en P1
         eEP2s= Double.parseDouble(energiaP2s.getText());                                                    // Energía simulada consumida en P2
         eEP3s= Double.parseDouble(energiaP3s.getText());                                                    // Energía simulada consumida en P3
       
         eEPs= eEP1s + eEP2s + eEP3s ;   jTextField124.setText(String.valueOf(eEPs));                        // Energía de peaje simulada
         
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pPC2 = Double.parseDouble(jTextField35.getText());                                                 // Potencia contratada en P2 Actual
         psPC2 = Double.parseDouble(jTextField37.getText());                                                // Potencia contratada en P2 Simulada
         
         pPC3 = Double.parseDouble(jTextField36.getText());                                                 // Potencia contratada en P3 Actual
         psPC3 = Double.parseDouble(jTextField38.getText());                                                // Potencia contratada en P3 Simulada
         
          // ..................................................................                              COMPENSACIÓN DE REACTIVA
         costeReactivaSim = 0 ;
         if (jCheckBox1.isSelected() && diasCond>0 ) {
           
            cosfiP1 = Double.parseDouble(jTextField118.getText());                                            // cos fi P1
            cosfiP2 = Double.parseDouble(jTextField120.getText());                                            // cos fi P1
            
            eRSP1   = (Math.tan(Math.acos(cosfiP1)) * eEP1) - 0.33 * eEP1 ;                                 // KVAr P1 simulada 
            eRSP2   = (Math.tan(Math.acos(cosfiP2)) * eEP2) - 0.33 * eEP2 ;                                 // KVAr P2 simulada
            
            if (eRSP1 < 0) eRSP1 = 0 ;
            if (eRSP2 < 0) eRSP2 = 0 ;
            
            seRSP1 = formatoNumero.format(eRSP1); System.out.println(String.valueOf(Math.tan(Math.acos(cosfiP1))));
            seRSP2 = formatoNumero.format(eRSP2);
            
            costeReactivaSim = (eRSP1 * costeReactiva) + (eRSP2 * costeReactiva) ; sCRS = formatoImporte.format(costeReactivaSim);
            
            
            jTextField121.setText(String.valueOf(redondear(eRSP1,2))) ;
            jTextField122.setText(String.valueOf(redondear(eRSP2,2))) ;
            jTextField123.setText(sCRS) ;
            
         }
         
       
         
         // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
         P3M = Double.parseDouble(jTextField79.getText());                                                 // Potencia de maximetro P3
         
         PR  = Double.parseDouble(jTextField85.getText());                                                 // Penalización de reactiva
        
        if (this.fPotenciaFacturada == 0) {                                      // Si se factura por maximetro, aplicamos cálculos formulas 
         
        if ( jCheckBox5.isSelected()) fReglaPotenciaActual=0 ; else fReglaPotenciaActual=1; 
        if (fReglaPotenciaActual == 1 ) {                                                                     // Si la compañia aplica regla de potencia
         
        if ((P1M/pPC1)>= 1.05 ) {   PF1a = P1M + 2 * ( P1M - 1.05 * pPC1); } else {                   // Potencia a facturar P1 actual
        if ((P1M/pPC1)>= 0.85 )   { PF1a =  P1M ;                                } else {
                                    PF1a = 0.85 * pPC1 ;
        } }  
        
        if ((P2M/pPC2)>= 1.05 ) {   PF2a =  P2M + 2 * ( P2M - 1.05 * pPC2); } else {                   // Potencia a facturar P2 actual
        if((P2M/pPC2)>= 0.85 )   {  PF2a =  P2M ;                                 } else {    
                                    PF2a = 0.85 * pPC2 ;
        }}
        
        if ((P3M/pPC3)>= 1.05 ) {   PF3a =  P3M + 2 * ( P3M - 1.05 * pPC3); } else {                   // Potencia a facturar P3 actual
        if ((P3M/pPC3)>= 0.85 )   { PF3a =  P3M ;                                } else {        
                                    PF3a = 0.85 * pPC3 ;
        }}  
        } else {
                                    PF1a = pPC1 ; PF2a = pPC2 ; PF3a = pPC3 ;
        }
          // ...................................................................
                                                                            // Si no se asigna directamente la potencia facturada.
        } else {
            System.out.println("-> Voy a utilizar las potencias tal cual aparecen en la factura... ¡Aviso!");
            PF1a = P1M ;
            PF2a = P2M ;
            PF3a = P3M ;
        }
        
        
        
        if ( jCheckBox6.isSelected()) fReglaPotenciaSimulado=0 ; else fReglaPotenciaSimulado=1; 
         if (fReglaPotenciaSimulado == 1 ) {    
         if ((P1M/psPC1)>= 1.05 ) {  PF1s =  P1M + 2 * ( P1M - 1.05 * psPC1); } else {                   // Potencia a facturar P1 simulada
         if ((P1M/psPC1)>= 0.85 )   {PF1s =  P1M ;                                  } else {   
                                    PF1s = 0.85 * psPC1 ;
        }   }
        
        if ((P2M/psPC2)>= 1.05 ) {   PF2s =  P2M + 2 * ( P2M - 1.05 * psPC2); } else {                   // Potencia a facturar P2 simulada
        if ((P2M/psPC2)>= 0.85 )    {PF2s =  P2M ;                                  } else {    
                                    PF2s = 0.85 * psPC2 ;
        }}
        
        if ((P3M/psPC3)>= 1.05 ) {   PF3s =  P3M + 2 * ( P3M - 1.05 * psPC3); } else {                   // Potencia a facturar P3 simulada
        if ((P3M/psPC3)>= 0.85 )   { PF3s =  P3M ;                                  } else {        
                                     PF3s = 0.85 * psPC3 ;
        }}
        } else {
                                    PF1s = psPC1 ; PF2s = psPC2 ; PF3s = psPC3 ;
        } 
         
        // ..................................................................  
        
        PF1a = redondear(PF1a,2);
        PF2a = redondear(PF2a,2);
        PF3a = redondear(PF3a,2); 
        PF1s = redondear(PF1s,2);
        PF2s = redondear(PF2s,2);
        PF3s = redondear(PF3s,2);
         
        // .................................................................. 
           
        jTextField106.setText(String.valueOf(PF1a));                                  // Potencia a facturar P1 actual 
        jTextField107.setText(String.valueOf(PF2a));                                  // Potencia a facturar P2 actual
        jTextField108.setText(String.valueOf(PF3a));                                  // Potencia a facturar P3 actual
        jTextField113.setText(String.valueOf(PF1s));                                  // Potencia a facturar P1 simulada 
        jTextField114.setText(String.valueOf(PF2s));                                  // Potencia a facturar P2 simulada
        jTextField115.setText(String.valueOf(PF3s));                                  // Potencia a facturar P3 simulada
        
         // .................................................................. 
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP2s  = Double.parseDouble(lCondicionesSimulacion[indice][15]) ;                                  // Precio en Energía en P2 con contrato simulado
         
         pEP3  = Double.parseDouble(lCondicionesActuales[indice][16]) ;                                     // Precio en Energía en P3 con contrato actual
         pEP3s  = Double.parseDouble(lCondicionesSimulacion[indice][16]) ;                                  // Precio en Energía en P3 con contrato simulado
         
         pEPa   = Double.parseDouble(jTextField73.getText());                                               // Precio de energía de peaje ACTUAL
         pEPs   = Double.parseDouble(jTextField74.getText());                                               // Precio de energía de peaje SIMULADO
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
         pPP2  = Double.parseDouble(lCondicionesActuales[indice][9]) ;                                      // Precio en Potencia en P2 Actual
         pPP2s = Double.parseDouble(lCondicionesSimulacion[indice][9]) ;                                    // Precio en Potencia en P2 Simulado
         
         pPP3  = Double.parseDouble(lCondicionesActuales[indice][10]) ;                                      // Precio en Potencia en P3 Actual
         pPP3s = Double.parseDouble(lCondicionesSimulacion[indice][10]) ;                                    // Precio en Potencia en P3 Simulado
         
         
        // .........................................................................
         
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
            
             aEP1 = (eEP1 * (pEP1s-pEP1))  ;                                                                     // Ahorro en el consumo de energía P1
             aEP2 = (eEP2 * (pEP2s-pEP2))  ;                                                                     // Ahorro en el consumo de energía P2
             aEP3 = (eEP3 * (pEP3s-pEP3))  ;                                                                     // Ahorro en el consumo de energía P3
             aE   = aEP1 + aEP2 + aEP3 + (eEP * (pEPs-pEPa));       
         } else {                                                                                            // Sino hay que utilar una energía simulada
            aEP1 = (eEP1s * pEP1s ) - ( pEP1 * eEP1)  ;                                                       // Ahorro en el consumo de energía P1
            aEP2 = (eEP2s * pEP2s ) - ( pEP2 * eEP2)  ;                                                       // Ahorro en el consumo de energía P2
            aEP3 = (eEP3s * pEP3s ) - ( pEP3 * eEP3)  ;                                                       // Ahorro en el consumo de energía P3  
            aE   = aEP1 + aEP2 + aEP3 + ((eEPs * pEPs) -(eEP * pEPa));       
             
         }
        
        
        // aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                          // Ahorro en potencia contratada P1
        // aPP2 = (psPC2 * dias * pPP2s) - (pPC2 * dias * pPP2) ;                                          // Ahorro en potencia contratada P2
        // aPP3 = (psPC3 * dias * pPP3s) - (pPC3 * dias * pPP3) ;                                          // Ahorro en potencia contratada P3
         
         aPP1 = (PF1s * dias * pPP1s) - (PF1a * dias * pPP1) ;                                             // Ahorro en potencia contratada P1
         aPP2 = (PF2s * dias * pPP2s) - (PF2a * dias * pPP2) ;                                             // Ahorro en potencia contratada P2
         aPP3 = (PF3s * dias * pPP3s) - (PF3a * dias * pPP3) ;                                             // Ahorro en potencia contratada P3
         
         aP   = aPP1+aPP2+aPP3 ;
         
         ahorro = impuesto_electrico * ((aE + aP ) + costeReactivaSim);                                      // Ahorro total Energia + Potencia
         
         System.out.println("impuesto electrico="+impuesto_electrico) ;
         System.out.println("aP ="+aP) ;
         System.out.println("(aE + aP )="+aE + aP) ;
         
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = (eEP1 * pEP1) + (PF1a * dias * pPP1 )  ;                                          // Coste con tarifa actual 
         coste_actual  += (eEP2 * pEP2) + (PF2a * dias * pPP2 )  ;
         coste_actual  += (eEP3 * pEP3) + (PF3a * dias * pPP3 )  ;
         coste_actual  += (eEP * pEPa) ;
        
         // ...................................................................
        
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
        
            coste_simulado =  (eEP1 * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2 * pEP2s) + (PF2s * dias * pPP2s ) ;
            coste_simulado += (eEP3 * pEP3s) + (PF3s * dias * pPP3s ) ;
            coste_simulado += (eEP * pEPs) ;
         
         } else {
            coste_simulado =  (eEP1s * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2s * pEP2s) + (PF2s * dias * pPP2s ) ;
            coste_simulado += (eEP3s * pEP3s) + (PF3s * dias * pPP3s ) ;
            coste_simulado += (eEPs * pEPs) ;
             
         }
         
         // ...................................................................
        
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                                  // Porcentaje de ahorro
         // ...................................................................                                      // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual + PR) + pAlquiler ;                                            // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P3 = "+formatoNumero.format(eEP3)+" kWh" +"\n" ;
         
         if (this.fEnergiaSimulada == 1) {    
            this.sMensajes = this.sMensajes + "Energía consumida en P1 SIMULADA = "+formatoNumero.format(eEP1s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P2 SIMULADA = "+formatoNumero.format(eEP2s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P3 SIMULADA = "+formatoNumero.format(eEP3s)+" kWh" +"\n" ;
         }
                 
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P2= "+formatoNumero.format(pPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P2= "+formatoNumero.format(psPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P3= "+formatoNumero.format(pPC3)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P3= "+formatoNumero.format(psPC3)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P1= "+formatoNumero.format(P1M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P2= "+formatoNumero.format(P2M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P3= "+formatoNumero.format(P3M)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 ACTUAL= "+formatoNumero.format(PF1a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 ACTUAL= "+formatoNumero.format(PF2a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 ACTUAL= "+formatoNumero.format(PF3a)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 SIMULADA= "+formatoNumero.format(PF1s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 SIMULADA= "+formatoNumero.format(PF2s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 SIMULADA= "+formatoNumero.format(PF3s)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP2s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 con contrato actual = "+pEP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 Simulado (contrato anterior) = "+pEP3s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Actual = "+pPP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Simulado = "+pPP2s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Actual = "+pPP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Simulado = "+pPP3s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual   -> (impuesto_electrico * ((eEP1 * pEP1) + (eEP2 * pEP2) + (eEP3 * pEP3) ) + (dias * pPP1 * pPC1 ) + (dias * pPP2 * pPC2 ) +( dias * pPP3 * pPC3 )))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (eEP2 * pEP2s) + (eEP3 * pEP3s) ) + (dias * pPP1s * pPC1s ) + (dias * pPP2s * pPC2s ) +( dias * pPP3s * pPC3s )))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aE) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aP) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
      // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 3.0A  a TARIFA 3.0A INDX (CASO 11)
            
     // .......................................................................
     
      
     if ( this.tipo_Act == 10 && this.tipo_Sim== 5) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO DE TARIFA 3.0A a TARIFA 3.0A indx") ;
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         eEP3 = Double.parseDouble(energiaP3.getText());                                                    // Energía consumida en P3
         
         eEP = eEP1 + eEP2 + eEP3 ;   jTextField75.setText(String.valueOf(eEP));     // Energía de peaje    
         
         eEP1s= Double.parseDouble(energiaP1s.getText());                                                    // Energía simulada consumida en P1
         eEP2s= Double.parseDouble(energiaP2s.getText());                                                    // Energía simulada consumida en P2
         eEP3s= Double.parseDouble(energiaP3s.getText());                                                    // Energía simulada consumida en P3
       
         eEPs= eEP1s + eEP2s + eEP3s ;   jTextField124.setText(String.valueOf(eEPs));                        // Energía de peaje simulada
         
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pPC2 = Double.parseDouble(jTextField35.getText());                                                 // Potencia contratada en P2 Actual
         psPC2 = Double.parseDouble(jTextField37.getText());                                                // Potencia contratada en P2 Simulada
         
         pPC3 = Double.parseDouble(jTextField36.getText());                                                 // Potencia contratada en P3 Actual
         psPC3 = Double.parseDouble(jTextField38.getText());                                                // Potencia contratada en P3 Simulada
         
         // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
         P3M = Double.parseDouble(jTextField79.getText());                                                 // Potencia de maximetro P3
         
         PR  = Double.parseDouble(jTextField85.getText());                                                 // Penalización de reactiva
         
         // ..................................................................
         
         pBonificacion = Double.parseDouble(jTextField126.getText());                                        // Bonficación en IE por ejemplo
         
         
         // ..................................................................                              COMPENSACIÓN DE REACTIVA
         costeReactivaSim = 0 ;
         if (jCheckBox1.isSelected() && diasCond>0) {
           
            cosfiP1 = Double.parseDouble(jTextField118.getText());                                            // cos fi P1
            cosfiP2 = Double.parseDouble(jTextField120.getText());                                            // cos fi P1
            
            eRSP1   = (Math.tan(Math.acos(cosfiP1)) * eEP1) - 0.33 * eEP1 ;                                 // KVAr P1 simulada 
            eRSP2   = (Math.tan(Math.acos(cosfiP2)) * eEP2) - 0.33 * eEP2 ;                                 // KVAr P2 simulada
            
            if (eRSP1 < 0) eRSP1 = 0 ;
            if (eRSP2 < 0) eRSP2 = 0 ;
            
            seRSP1 = formatoNumero.format(eRSP1); System.out.println(String.valueOf(Math.tan(Math.acos(cosfiP1))));
            seRSP2 = formatoNumero.format(eRSP2);
            
            costeReactivaSim = (eRSP1 * costeReactiva) + (eRSP2 * costeReactiva) ; sCRS = formatoImporte.format(costeReactivaSim);
            
            
            jTextField121.setText(String.valueOf(redondear(eRSP1,2))) ;
            jTextField122.setText(String.valueOf(redondear(eRSP2,2))) ;
            jTextField123.setText(sCRS) ;
            
         }
         
        // ..................................................................
        if (this.fPotenciaFacturada == 0) {                                      // Si se factura por maximetro, aplicamos cálculos formulas 
        if ( jCheckBox5.isSelected()) fReglaPotenciaActual=0 ; else fReglaPotenciaActual=1; 
        if (fReglaPotenciaActual == 1 ) {                                                                     // Si la compañia aplica regla de potencia

                if ((P1M/pPC1)>= 1.05 ) {   PF1a =  P1M + 2 * ( P1M - 1.05 * pPC1); } else {                   // Potencia a facturar P1 actual
                if ((P1M/pPC1)>= 0.85 )   { PF1a =  P1M ;                                } else {
                                            PF1a = 0.85 * pPC1 ;
                } }  

                if ((P2M/pPC2)>= 1.05 ) {   PF2a =  P2M + 2 * ( P2M - 1.05 * pPC2); } else {                   // Potencia a facturar P2 actual
                if((P2M/pPC2)>= 0.85 )   {  PF2a =  P2M ;                                 } else {    
                                            PF2a = 0.85 * pPC2 ;
                }}

                if ((P3M/pPC3)>= 1.05 ) {   PF3a =  P3M + 2 * ( P3M - 1.05 * pPC3); } else {                   // Potencia a facturar P3 actual
                if ((P3M/pPC3)>= 0.85 )   { PF3a =  P3M ;                                } else {        
                                            PF3a = 0.85 * pPC3 ;
                }}
            } else {
                                        PF1a = pPC1 ; PF2a = pPC2 ; PF3a = pPC3 ;
            }
         // ...................................................................
                                                                            // Si no se asigna directamente la potencia facturada.
        } else {
            System.out.println("-> Voy a utilizar las potencias tal cual aparecen en la factura... ¡Aviso!");
            PF1a = P1M ;
            PF2a = P2M ;
            PF3a = P3M ;
        }
        
        
        if ( jCheckBox6.isSelected()) fReglaPotenciaSimulado=0 ; else fReglaPotenciaSimulado=1; 
         if (fReglaPotenciaSimulado == 1 ) {    
         if ((P1M/psPC1)>= 1.05 ) {  PF1s =  P1M + 2 * ( P1M - 1.05 * psPC1); } else {                   // Potencia a facturar P1 simulada
         if ((P1M/psPC1)>= 0.85 )   {PF1s =  P1M ;                                  } else {   
                                    PF1s = 0.85 * psPC1 ;
        }   }
        
        if ((P2M/psPC2)>= 1.05 ) {   PF2s =  P2M + 2 * ( P2M - 1.05 * psPC2); } else {                   // Potencia a facturar P2 simulada
        if ((P2M/psPC2)>= 0.85 )    {PF2s =  P2M ;                            } else {    
                                     PF2s = 0.85 * psPC2 ;
        }}
        
        if ((P3M/psPC3)>= 1.05 ) {   PF3s =  P3M + 2 * ( P3M - 1.05 * psPC3); } else {                   // Potencia a facturar P3 simulada
        if ((P3M/psPC3)>= 0.85 )   { PF3s =  P3M ;                                  } else {        
                                     PF3s = 0.85 * psPC3 ;
        }}
        } else {
                                    PF1s = psPC1 ; PF2s = psPC2 ; PF3s = psPC3 ;
        } 
         
        // ..................................................................  
        
        PF1a = redondear(PF1a,2);
        PF2a = redondear(PF2a,2);
        PF3a = redondear(PF3a,2); 
        PF1s = redondear(PF1s,2);
        PF2s = redondear(PF2s,2);
        PF3s = redondear(PF3s,2);
         
         
         // .................................................................. 
           
        jTextField106.setText(String.valueOf(PF1a));                                  // Potencia a facturar P1 actual 
        jTextField107.setText(String.valueOf(PF2a));                                  // Potencia a facturar P2 actual
        jTextField108.setText(String.valueOf(PF3a));                                  // Potencia a facturar P3 actual
        jTextField113.setText(String.valueOf(PF1s));                                  // Potencia a facturar P1 simulada 
        jTextField114.setText(String.valueOf(PF2s));                                  // Potencia a facturar P2 simulada
        jTextField115.setText(String.valueOf(PF3s));                                  // Potencia a facturar P3 simulada
        
        
        
         // .................................................................. 
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP2s  = Double.parseDouble(lCondicionesSimulacion[indice][15]) ;                                  // Precio en Energía en P2 con contrato simulado
         
         pEP3  = Double.parseDouble(lCondicionesActuales[indice][16]) ;                                     // Precio en Energía en P3 con contrato actual
         pEP3s  = Double.parseDouble(lCondicionesSimulacion[indice][16]) ;                                  // Precio en Energía en P3 con contrato simulado
         
         pEPa   = Double.parseDouble(jTextField73.getText());                                               // Precio de energía de peaje ACTUAL
          
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
         pPP2  = Double.parseDouble(lCondicionesActuales[indice][9]) ;                                      // Precio en Potencia en P2 Actual
         pPP2s = Double.parseDouble(lCondicionesSimulacion[indice][9]) ;                                    // Precio en Potencia en P2 Simulado
         
         pPP3  = Double.parseDouble(lCondicionesActuales[indice][10]) ;                                      // Precio en Potencia en P3 Actual
         pPP3s = Double.parseDouble(lCondicionesSimulacion[indice][10]) ;                                    // Precio en Potencia en P3 Simulado
                                            
        
          // .........................................................................
         
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
            
             aEP1 = (eEP1 * (pEP1s-pEP1))  ;                                                                     // Ahorro en el consumo de energía P1
             aEP2 = (eEP2 * (pEP2s-pEP2))  ;                                                                     // Ahorro en el consumo de energía P2
             aEP3 = (eEP3 * (pEP3s-pEP3))  ;                                                                     // Ahorro en el consumo de energía P3
             aE   = aEP1 + aEP2 + aEP3 + (eEP * (pEPs-pEPa)) ;       
         } else {                                                                                            // Sino hay que utilar una energía simulada
            aEP1 = (eEP1s * pEP1s ) - ( pEP1 * eEP1)  ;                                                       // Ahorro en el consumo de energía P1
            aEP2 = (eEP2s * pEP2s ) - ( pEP2 * eEP2)  ;                                                       // Ahorro en el consumo de energía P2
            aEP3 = (eEP3s * pEP3s ) - ( pEP3 * eEP3)  ;                                                       // Ahorro en el consumo de energía P3  
            aE   = aEP1 + aEP2 + aEP3 - (eEP * pEPa) ;       
             
         }
         
         // .........................................................................
         
         aPP1 = (PF1s * dias * pPP1s) - (PF1a * dias * pPP1) ;                                             // Ahorro en potencia contratada P1
         aPP2 = (PF2s * dias * pPP2s) - (PF2a * dias * pPP2) ;                                             // Ahorro en potencia contratada P2
         aPP3 = (PF3s * dias * pPP3s) - (PF3a * dias * pPP3) ;                                             // Ahorro en potencia contratada P3
         
         aP   = aPP1+aPP2+aPP3 ;
         
         ahorro = pBonificacion + (impuesto_electrico * ((aE + aP) + costeReactivaSim)) ;                   // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = (eEP1 * pEP1) + (PF1a * dias * pPP1 )  ;                                          // Coste con tarifa actual 
         coste_actual  += (eEP2 * pEP2) + (PF2a * dias * pPP2 )  ;
         coste_actual  += (eEP3 * pEP3) + (PF3a * dias * pPP3 )  ;
         coste_actual  += (eEP * pEPa) ;
         
          // ...................................................................
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
        
            coste_simulado =  (eEP1 * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2 * pEP2s) + (PF2s * dias * pPP2s ) ;
            coste_simulado += (eEP3 * pEP3s) + (PF3s * dias * pPP3s ) ;
            coste_simulado += costeReactivaSim ;
                              
         } else {
            coste_simulado =  (eEP1s * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2s * pEP2s) + (PF2s * dias * pPP2s ) ;
            coste_simulado += (eEP3s * pEP3s) + (PF3s * dias * pPP3s ) ;
            coste_simulado += costeReactivaSim ;
         //   coste_simulado += (eEPs * pEPs) ;
             
         }
         
         // ...................................................................
        
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................                             // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual + PR) + pAlquiler -  pBonificacion;                  // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P3 = "+formatoNumero.format(eEP3)+" kWh" +"\n" ;
         
          if (this.fEnergiaSimulada == 1) {    
            this.sMensajes = this.sMensajes + "Energía consumida en P1 SIMULADA = "+formatoNumero.format(eEP1s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P2 SIMULADA = "+formatoNumero.format(eEP2s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P3 SIMULADA = "+formatoNumero.format(eEP3s)+" kWh" +"\n" ;
          }
         
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P2= "+formatoNumero.format(pPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P2= "+formatoNumero.format(psPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P3= "+formatoNumero.format(pPC3)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P3= "+formatoNumero.format(psPC3)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P1= "+formatoNumero.format(P1M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P2= "+formatoNumero.format(P2M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P3= "+formatoNumero.format(P3M)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 ACTUAL= "+formatoNumero.format(PF1a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 ACTUAL= "+formatoNumero.format(PF2a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 ACTUAL= "+formatoNumero.format(PF3a)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 SIMULADA= "+formatoNumero.format(PF1s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 SIMULADA= "+formatoNumero.format(PF2s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 SIMULADA= "+formatoNumero.format(PF3s)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP2s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 con contrato actual = "+pEP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 Simulado (contrato anterior) = "+pEP3s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Actual = "+pPP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Simulado = "+pPP2s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Actual = "+pPP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Simulado = "+pPP3s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual   -> (((eEP1 * pEP1) + (eEP2 * pEP2) + (eEP3 * pEP3) ) + (dias * pPP1 * pPC1 ) + (dias * pPP2 * pPC2 ) +( dias * pPP3 * pPC3 )))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (((eEP1 * pEP1s) + (eEP2 * pEP2s) + (eEP3 * pEP3s) ) + (dias * pPP1s * pPC1s ) + (dias * pPP2s * pPC2s ) +( dias * pPP3s * pPC3s )))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aE) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aP) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.0A a TARIFA 2.0A INDX o DE TARIFA 2.1A a TARIFA 2.1A INDX
     //                                                                       o DE TARIFA 2.1A a TARIFA 2.0A INDX       
     // .......................................................................
     if ( (this.tipo_Act == 11 && this.tipo_Sim== 1) || (this.tipo_Act == 12 && this.tipo_Sim== 3) || (this.tipo_Act == 11 && this.tipo_Sim== 3)) {
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                 // Energía consumida en P1
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                  // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                // Precio en Energía en P1 Simulado (contrato anterior)
         pEP   = Double.parseDouble(jTextField73.getText());                             // Precio en Energía de Peaje
        
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                   // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                 // Precio en Potencia en P1 Simulado
        
         psPC1 = Double.parseDouble(jTextField26.getText());                             // Potencia contratada en P1 Simulada
         
          // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2

        // ..................................................................
        if (this.fPotenciaFacturada == 0) {                                      // Si se factura por maximetro, aplicamos cálculos formulas
         
            pPC1 = Double.parseDouble(jTextField19.getText());                              // Potencia contratada en P1 Actual
            System.out.println("-> Si se factura por maximetro, aplicamos cálculos potencias de contrato");
        // ...................................................................
                                                                            // Si no se asigna directamente la potencia facturada.
        } else {
            System.out.println("-> Voy a utilizar las potencias tal cual aparecen en la factura... ¡Aviso!");
            pPC1 = P1M ;                    
        }
        
         // .................................................................. 
         
        jTextField106.setText(String.valueOf(pPC1));                                  // Potencia a facturar P1 actual 
       
        jTextField113.setText(String.valueOf(psPC1));                                  // Potencia a facturar P1 simulada 
       
        // ..................................................................
        
         
         
         
         
         
         aEP1 = (eEP1 * (pEP1s-pEP1) -(pEP * eEP1) ) ;                                  // Ahorro en el consumo de energía
         aPP1 = (dias * ((psPC1*pPP1s)-(pPC1*pPP1)) ) ;                                 // Ahorro en potencia contratada
         
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                  // Ahorro total Energia + Potencia
        
         ahorro_total = ahorro_total + ahorro ;                                         // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (dias * pPP1 * pPC1) + (pEP * eEP1)) ;       // Coste con tarifa actual
         
         coste_simulado = impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s * psPC1)) ;     // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_iva * coste_actual ;                             // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                     // Porcentaje de ahorro
         
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual) + pAlquiler ;                                         // Precio base de factura calculado
         
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));     
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 (eEP1)= "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía de Peaje (eEP1)= "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual (pEP1)= "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) (pEP1s)= "+pEP1s+" €/kWh" +"\n \n" ;    
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio de Energia de Peaje con contrato actual = "+pEP+" €/kWh" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ;
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1)  + (pEP * eEP1) + (dias * pPP1 *pPC1))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 *pPC1 )))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
       // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.0DHA a TARIFA 2.0DHA INDEXADO
            
     // .......................................................................
      if ( this.tipo_Act == 8 && this.tipo_Sim== 2) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO     DE TARIFA 2.0DHA Indexado a TARIFA 2.0A") ;
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
        
         eEP = eEP1 + eEP2 ;   jTextField75.setText(String.valueOf(eEP));                                   // Energía de peaje                                                               // Energía de peaje
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP   = Double.parseDouble(jTextField73.getText());                                                // Precio en Energía de Peaje
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
                                            
        // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
                
         
         aEP1 = (eEP1 * (pEP1s-pEP1)) + ( eEP2 * (pEP1s-pEP2)) - (eEP * pEP) ;                              // Ahorro en el consumo de energía
         aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                             // Ahorro en potencia contratada
         
         ahorro = impuesto_electrico * (aEP1 + aPP1) ;                                                      // Ahorro total Energia + Potencia
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = ((eEP1 * pEP1) + (pPC1 * dias * pPP1 ) + (eEP2 * pEP2) + (eEP * pEP) ) ;          // Coste con tarifa actual
        
         coste_simulado = ((eEP1 * pEP1s) + (psPC1 * dias * pPP1s ) + (eEP2 * pEP1s)) ;                             // Coste con tarifa simulada
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
        pBIC    = impuesto_electrico * (coste_actual + PR) + pAlquiler ;                                         // Precio base de factura calculado
        spBIC   = formatoImporte.format(pBIC);
        pBIC    =Math.rint(pBIC*100)/100 ; 
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC));  
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje      = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía de Peaje = "+formatoNumero.format(eEP)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1-P2= "+formatoNumero.format(pPC1)+" kW" +"\n" ;       
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1 = "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en Energia de Peaje con contrato actual = "+pEP+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1-P2 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;         
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste Enegia de peaje ->(eEP * pEP)= \n"+formatoImporte.format(eEP * pEP) +"\n" ;
         
         this.sMensajes = this.sMensajes + "Coste con tarifa actual  ->(impuesto_electrico * ((eEP1 * pEP1) + (dias * pPP1 ) + (eEP2 * pEP2) +  (eEP * pEP)))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (dias * pPP1s ) + (eEP2 * pEP1s)))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aEP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aPP1) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
       // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 2.1A  a TARIFA 3.0A INDX
            
     // .......................................................................
     
      
     if ( this.tipo_Act == 10 && this.tipo_Sim== 3 ) {
         System.out.println("ESTAMOS EN  CALCULO DE AHORRO DE TARIFA 2.1A  a TARIFA 3.0A INDX") ;
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         eEP3 = Double.parseDouble(energiaP3.getText());                                                    // Energía consumida en P3
         
         eEP = eEP1 + eEP2 + eEP3 ;   jTextField75.setText(String.valueOf(eEP));     // Energía de peaje 
         
         eEP1s= Double.parseDouble(energiaP1s.getText());                                                    // Energía simulada consumida en P1
         eEP2s= Double.parseDouble(energiaP2s.getText());                                                    // Energía simulada consumida en P2
         eEP3s= Double.parseDouble(energiaP3s.getText());                                                    // Energía simulada consumida en P3
       
         eEPs= eEP1s + eEP2s + eEP3s ;   jTextField124.setText(String.valueOf(eEPs));                        // Energía de peaje simulada
         
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pPC2 = Double.parseDouble(jTextField35.getText());                                                 // Potencia contratada en P2 Actual
        
         
         pPC3 = Double.parseDouble(jTextField36.getText());                                                 // Potencia contratada en P3 Actual
         
         
         // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
         P3M = Double.parseDouble(jTextField79.getText());                                                 // Potencia de maximetro P3
         
         PR  = Double.parseDouble(jTextField85.getText());                                                 // Penalización de reactiva
         
         // ..................................................................                              COMPENSACIÓN DE REACTIVA
         costeReactivaSim = 0 ;
         
         jTextField121.setText("0");
         jTextField122.setText("0");
         jTextField123.setText("0") ;
         
         if (jCheckBox1.isSelected()) {
           
            cosfiP1 = Double.parseDouble(jTextField118.getText());                                            // cos fi P1
            cosfiP2 = Double.parseDouble(jTextField120.getText());                                            // cos fi P1
            
            eRSP1   = (Math.tan(Math.acos(cosfiP1)) * eEP1) - 0.33 * eEP1 ;                                 // KVAr P1 simulada 
            eRSP2   = (Math.tan(Math.acos(cosfiP2)) * eEP2) - 0.33 * eEP2 ;                                 // KVAr P2 simulada
            
            if (eRSP1 < 0) eRSP1 = 0 ;
            if (eRSP2 < 0) eRSP2 = 0 ;
            
            seRSP1 = formatoNumero.format(eRSP1); System.out.println(String.valueOf(Math.tan(Math.acos(cosfiP1))));
            seRSP2 = formatoNumero.format(eRSP2);
            
            costeReactivaSim = (eRSP1 * costeReactiva) + (eRSP2 * costeReactiva) ; sCRS = formatoImporte.format(costeReactivaSim);
            
            
            jTextField121.setText(String.valueOf(redondear(eRSP1,2))) ;
            jTextField122.setText(String.valueOf(redondear(eRSP2,2))) ;
            jTextField123.setText(sCRS) ;
            
         }
         
        // ..................................................................
        if (this.fPotenciaFacturada == 0) {                                      // Si se factura por maximetro, aplicamos cálculos formulas  
        if (fReglaPotenciaActual == 1 ) {                                                                     // Si la compañia aplica regla de potencia
         
        if ((P1M/pPC1)>= 1.05 ) {   PF1a =  P1M + 2 * ( P1M - 1.05 * pPC1); } else {                   // Potencia a facturar P1 actual
        if ((P1M/pPC1)>= 0.85 )   { PF1a =  P1M ;                                } else {
                                    PF1a = 0.85 * pPC1 ;
        } }  
        
        if ((P2M/pPC2)>= 1.05 ) {   PF2a =  P2M + 2 * ( P2M - 1.05 * pPC2); } else {                   // Potencia a facturar P2 actual
        if((P2M/pPC2)>= 0.85 )   {  PF2a =  P2M ;                                 } else {    
                                    PF2a = 0.85 * pPC2 ;
        }}
        
        if ((P3M/pPC3)>= 1.05 ) {   PF3a =  P3M + 2 * ( P3M - 1.05 * pPC3); } else {                   // Potencia a facturar P3 actual
        if ((P3M/pPC3)>= 0.85 )   { PF3a =  P3M ;                                } else {        
                                    PF3a = 0.85 * pPC3 ;
        }}
        } else {
                                    PF1a = pPC1 ; PF2a = pPC2 ; PF3a = pPC3 ;
        }
            // ...................................................................
                                                                            // Si no se asigna directamente la potencia facturada.
        } else {
            System.out.println("-> Voy a utilizar las potencias tal cual aparecen en la factura... ¡Aviso!");
            PF1a = P1M ;
            PF2a = P2M ;
            PF3a = P3M ;
        }
        
        
        
                                    PF1s =  psPC1 ;
        
        
         // .................................................................. 
           
        jTextField106.setText(String.valueOf(PF1a));                                  // Potencia a facturar P1 actual 
        jTextField107.setText(String.valueOf(PF2a));                                  // Potencia a facturar P2 actual
        jTextField108.setText(String.valueOf(PF3a));                                  // Potencia a facturar P3 actual
        jTextField113.setText(String.valueOf(PF1s));                                  // Potencia a facturar P1 simulada 
        jTextField114.setText("0"); 
        jTextField115.setText("0"); 
        
         // .................................................................. 
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
        
         pEP3  = Double.parseDouble(lCondicionesActuales[indice][16]) ;                                     // Precio en Energía en P3 con contrato actual
         
         pEPa   = Double.parseDouble(jTextField73.getText());                                               // Precio de energía de peaje ACTUAL
          
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
         pPP2  = Double.parseDouble(lCondicionesActuales[indice][9]) ;                                      // Precio en Potencia en P2 Actual
         
         pPP3  = Double.parseDouble(lCondicionesActuales[indice][10]) ;                                      // Precio en Potencia en P3 Actual
                                  
         
          // .........................................................................
         
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
            
            aEP1 = (eEP1 * (pEP1s-pEP1))  ;                                                                     // Ahorro en el consumo de energía P1
            aEP2 = (eEP2 * (pEP1s-pEP2))  ;                                                                     // Ahorro en el consumo de energía P2
            aEP3 = (eEP3 * (pEP1s-pEP3))  ;                                                                     // Ahorro en el consumo de energía P3
            aE   = aEP1 + aEP2 + aEP3 - (eEP * pEPa); System.out.println("aE="+aE);
            
         } else {                                                                                            // Sino hay que utilar una energía simulada
            aEP1 = (eEP1s * pEP1s ) - ( pEP1 * eEP1)  ;                                                       // Ahorro en el consumo de energía P1
            aEP2 = (eEP2s * pEP1s ) - ( pEP2 * eEP2)  ;                                                       // Ahorro en el consumo de energía P2
            aEP3 = (eEP3s * pEP1s ) - ( pEP3 * eEP3)  ;                                                       // Ahorro en el consumo de energía P3  
            aE   = aEP1 + aEP2 + aEP3 - (eEP * pEPa) ;       
             
         }         
                  
      
         aPP1 = (PF1s * dias * pPP1s) - (PF1a * dias * pPP1) - (PF2a * dias * pPP2) - (PF3a * dias * pPP3) ; // Ahorro en potencia contratada P1
      
         
         aP   = aPP1 ; System.out.println("aP="+aP);
         
         ahorro = impuesto_electrico * ((aE + aP) - PR ) + (psAlquiler - pAlquiler) ;                                                // Ahorro total Energia + Potencia - Penalización Reactiva
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = (eEP1 * pEP1) + (PF1a * dias * pPP1 )  ;                                          // Coste con tarifa actual 
         coste_actual  += (eEP2 * pEP2) + (PF2a * dias * pPP2 )  ;
         coste_actual  += (eEP3 * pEP3) + (PF3a * dias * pPP3 )  ;
         coste_actual  += (eEP * pEPa)  + PR ;
         
         
          // ...................................................................
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
        
            coste_simulado =  (eEP1 * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2 * pEP1s) ;
            coste_simulado += (eEP3 * pEP1s) ;
            coste_simulado -= (PR) ;
                              
         } else {
            coste_simulado =  (eEP1s * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2s * pEP1s)  ;
            coste_simulado += (eEP3s * pEP1s)  ;
            coste_simulado += costeReactivaSim ;
         //   coste_simulado += (eEPs * pEPs) ;
             
         }
         
        
                         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                           // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual ) + pAlquiler ;                                         // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
       
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
      
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P3 = "+formatoNumero.format(eEP3)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P2= "+formatoNumero.format(pPC2)+" kW" +"\n" ;
        
         this.sMensajes = this.sMensajes + "Potencia contratada actual P3= "+formatoNumero.format(pPC3)+" kW" +"\n" ;
        
        if (this.fEnergiaSimulada == 1) {    
            this.sMensajes = this.sMensajes + "Energía consumida en P1 SIMULADA = "+formatoNumero.format(eEP1s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P2 SIMULADA = "+formatoNumero.format(eEP2s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P3 SIMULADA = "+formatoNumero.format(eEP3s)+" kWh" +"\n" ;
        }
         
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P1= "+formatoNumero.format(P1M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P2= "+formatoNumero.format(P2M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P3= "+formatoNumero.format(P3M)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 ACTUAL= "+formatoNumero.format(PF1a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 ACTUAL= "+formatoNumero.format(PF2a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 ACTUAL= "+formatoNumero.format(PF3a)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 SIMULADA= "+formatoNumero.format(PF1s)+" kW" +"\n" ;
                
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 con contrato actual = "+pEP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía de peaje con contrato actual = "+pEPa+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Penalización Reactiva con contrato actual = "+PR+" €" +"\n" ;
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Actual = "+pPP2+" €/kWh" +"\n" ;
        
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Actual = "+pPP3+" €/kWh" +"\n" ;
       
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual   -> (impuesto_electrico * ((eEP1 * pEP1) + (eEP2 * pEP2) + (eEP3 * pEP3) ) + (eEP * pEPa) ) + (dias * pPP1 * pPC1 ) + (dias * pPP2 * pPC2 ) +( dias * pPP3 * pPC3 ))) + (PR)= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (eEP2 * pEP2s) + (eEP3 * pEP3s) ) + (dias * pPP1s * pPC1s ) + (dias * pPP2s * pPC2s ) +( dias * pPP3s * pPC3s )))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aE) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aP) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
     // .......................................................................    
     // .......................................................................................................................................  
     // .....................................           CALCULO DE AHORRO     DE TARIFA 3.1A INDX a TARIFA 3.1A INDX (CASO 19) (CASO 21) (CASO 18)
            
     // .......................................................................
     
      
     if ( this.tipo_Act == 13 && (this.tipo_Sim== 13 || this.tipo_Sim== 5 || this.tipo_Sim== 6)) {
         System.out.println("ESTAMOS EN  (CASO 19) (CASO 21) (CASO 18)") ;
         
         eEP1 = Double.parseDouble(energiaP1.getText());                                                    // Energía consumida en P1
         eEP2 = Double.parseDouble(energiaP2.getText());                                                    // Energía consumida en P2
         eEP3 = Double.parseDouble(energiaP3.getText());                                                    // Energía consumida en P3
         eEP = eEP1 + eEP2 + eEP3 ;   jTextField75.setText(String.valueOf(eEP));                            // Energía de peaje    
        
         // .....................................................
         
         eEP1s= Double.parseDouble(energiaP1s.getText());                                                    // Energía simulada consumida en P1
         eEP2s= Double.parseDouble(energiaP2s.getText());                                                    // Energía simulada consumida en P2
         eEP3s= Double.parseDouble(energiaP3s.getText());                                                    // Energía simulada consumida en P3
       
         eEPs= eEP1s + eEP2s + eEP3s ;   jTextField124.setText(String.valueOf(eEPs));                        // Energía de peaje simulada
         
         pPC1 = Double.parseDouble(jTextField19.getText());                                                 // Potencia contratada en P1 Actual
         psPC1 = Double.parseDouble(jTextField26.getText());                                                // Potencia contratada en P1 Simulada
         
         pPC2 = Double.parseDouble(jTextField35.getText());                                                 // Potencia contratada en P2 Actual
         psPC2 = Double.parseDouble(jTextField37.getText());                                                // Potencia contratada en P2 Simulada
         
         pPC3 = Double.parseDouble(jTextField36.getText());                                                 // Potencia contratada en P3 Actual
         psPC3 = Double.parseDouble(jTextField38.getText());                                                // Potencia contratada en P3 Simulada
         
         // ..................................................................
         
         pBonificacion = Double.parseDouble(jTextField126.getText());                                        // Bonficación en IE por ejemplo
         
         
          // ..................................................................                              COMPENSACIÓN DE REACTIVA
         costeReactivaSim = 0 ;
         if (jCheckBox1.isSelected() && diasCond>0 ) {
           
            cosfiP1 = Double.parseDouble(jTextField118.getText());                                            // cos fi P1
            cosfiP2 = Double.parseDouble(jTextField120.getText());                                            // cos fi P1
            
            eRSP1   = (Math.tan(Math.acos(cosfiP1)) * eEP1) - 0.33 * eEP1 ;                                 // KVAr P1 simulada 
            eRSP2   = (Math.tan(Math.acos(cosfiP2)) * eEP2) - 0.33 * eEP2 ;                                 // KVAr P2 simulada
            
            if (eRSP1 < 0) eRSP1 = 0 ;
            if (eRSP2 < 0) eRSP2 = 0 ;
            
            seRSP1 = formatoNumero.format(eRSP1); System.out.println(String.valueOf(Math.tan(Math.acos(cosfiP1))));
            seRSP2 = formatoNumero.format(eRSP2);
            
            costeReactivaSim = (eRSP1 * costeReactiva) + (eRSP2 * costeReactiva) ; sCRS = formatoImporte.format(costeReactivaSim);
            
            
            jTextField121.setText(String.valueOf(redondear(eRSP1,2))) ;
            jTextField122.setText(String.valueOf(redondear(eRSP2,2))) ;
            jTextField123.setText(sCRS) ;
            
         }
         
       
         
         // ..................................................................                              POTENCIA A FACTURAR
         
         P1M = Double.parseDouble(jTextField77.getText());                                                 // Potencia de maximetro P1
         P2M = Double.parseDouble(jTextField78.getText());                                                 // Potencia de maximetro P2
         P3M = Double.parseDouble(jTextField79.getText());                                                 // Potencia de maximetro P3
         
         PR  = Double.parseDouble(jTextField85.getText());                                                 // Penalización de reactiva
        
        if (this.fPotenciaFacturada == 0) {                                      // Si se factura por maximetro, aplicamos cálculos formulas  
            
        
        if ( jCheckBox5.isSelected()) fReglaPotenciaActual=0 ; else fReglaPotenciaActual=1; 
        if (fReglaPotenciaActual == 1 ) {                                                                     // Si la compañia aplica regla de potencia
         
        if ((P1M/pPC1)>= 1.05 ) {   PF1a = P1M + 2 * ( P1M - 1.05 * pPC1); } else {                   // Potencia a facturar P1 actual
        if ((P1M/pPC1)>= 0.85 )   { PF1a =  P1M ;                                } else {
                                    PF1a = 0.85 * pPC1 ;
        } }  
        
        if ((P2M/pPC2)>= 1.05 ) {   PF2a =  P2M + 2 * ( P2M - 1.05 * pPC2); } else {                   // Potencia a facturar P2 actual
        if((P2M/pPC2)>= 0.85 )   {  PF2a =  P2M ;                                 } else {    
                                    PF2a = 0.85 * pPC2 ;
        }}
        
        if ((P3M/pPC3)>= 1.05 ) {   PF3a =  P3M + 2 * ( P3M - 1.05 * pPC3); } else {                   // Potencia a facturar P3 actual
        if ((P3M/pPC3)>= 0.85 )   { PF3a =  P3M ;                                } else {        
                                    PF3a = 0.85 * pPC3 ;
        }}
        } else {
                                    PF1a = pPC1 ; PF2a = pPC2 ; PF3a = pPC3 ;
        }
             // ...................................................................
                                                                            // Si no se asigna directamente la potencia facturada.
        } else {
            System.out.println("-> Voy a utilizar las potencias tal cual aparecen en la factura... ¡Aviso!");
            PF1a = P1M ;
            PF2a = P2M ;
            PF3a = P3M ;
        }
        
       
        
        
        if ( jCheckBox6.isSelected()) fReglaPotenciaSimulado=0 ; else fReglaPotenciaSimulado=1; 
         if (fReglaPotenciaSimulado == 1 ) {    
         if ((P1M/psPC1)>= 1.05 ) {  PF1s =  P1M + 2 * ( P1M - 1.05 * psPC1); } else {                   // Potencia a facturar P1 simulada
         if ((P1M/psPC1)>= 0.85 )   {PF1s =  P1M ;                                  } else {   
                                    PF1s = 0.85 * psPC1 ;
        }   }
        
        if ((P2M/psPC2)>= 1.05 ) {   PF2s =  P2M + 2 * ( P2M - 1.05 * psPC2); } else {                   // Potencia a facturar P2 simulada
        if ((P2M/psPC2)>= 0.85 )    {PF2s =  P2M ;                                  } else {    
                                    PF2s = 0.85 * psPC2 ;
        }}
        
        if ((P3M/psPC3)>= 1.05 ) {   PF3s =  P3M + 2 * ( P3M - 1.05 * psPC3); } else {                   // Potencia a facturar P3 simulada
        if ((P3M/psPC3)>= 0.85 )   { PF3s =  P3M ;                                  } else {        
                                     PF3s = 0.85 * psPC3 ;
        }}
        } else {
                                    PF1s = psPC1 ; PF2s = psPC2 ; PF3s = psPC3 ;
        } 
        
         // ..................................................................  
        
        PF1a = redondear(PF1a,2);
        PF2a = redondear(PF2a,2);
        PF3a = redondear(PF3a,2); 
        PF1s = redondear(PF1s,2);
        PF2s = redondear(PF2s,2);
        PF3s = redondear(PF3s,2);
        
         // .................................................................. 
           
        jTextField106.setText(String.valueOf(PF1a));                                  // Potencia a facturar P1 actual 
        jTextField107.setText(String.valueOf(PF2a));                                  // Potencia a facturar P2 actual
        jTextField108.setText(String.valueOf(PF3a));                                  // Potencia a facturar P3 actual
        jTextField113.setText(String.valueOf(PF1s));                                  // Potencia a facturar P1 simulada 
        jTextField114.setText(String.valueOf(PF2s));                                  // Potencia a facturar P2 simulada
        jTextField115.setText(String.valueOf(PF3s));                                  // Potencia a facturar P3 simulada
        
         // .................................................................. 
         pEP1  = Double.parseDouble(lCondicionesActuales[indice][14]) ;                                     // Precio en Energía en P1 con contrato actual
         pEP1s = Double.parseDouble(lCondicionesSimulacion[indice][14]) ;                                   // Precio en Energía en P1 Simulado (contrato anterior)
         
         pEP2  = Double.parseDouble(lCondicionesActuales[indice][15]) ;                                     // Precio en Energía en P2 con contrato actual
         pEP2s  = Double.parseDouble(lCondicionesSimulacion[indice][15]) ;                                  // Precio en Energía en P2 con contrato simulado
         
         pEP3  = Double.parseDouble(lCondicionesActuales[indice][16]) ;                                     // Precio en Energía en P3 con contrato actual
         pEP3s  = Double.parseDouble(lCondicionesSimulacion[indice][16]) ;                                  // Precio en Energía en P3 con contrato simulado
         
         pEPa   = Double.parseDouble(jTextField73.getText());                                               // Precio de energía de peaje ACTUAL
         pEPs   = Double.parseDouble(jTextField74.getText());                                               // Precio de energía de peaje SIMULADO
         
         pPP1  = Double.parseDouble(lCondicionesActuales[indice][8]) ;                                      // Precio en Potencia en P1 Actual
         pPP1s = Double.parseDouble(lCondicionesSimulacion[indice][8]) ;                                    // Precio en Potencia en P1 Simulado
         
         pPP2  = Double.parseDouble(lCondicionesActuales[indice][9]) ;                                      // Precio en Potencia en P2 Actual
         pPP2s = Double.parseDouble(lCondicionesSimulacion[indice][9]) ;                                    // Precio en Potencia en P2 Simulado
         
         pPP3  = Double.parseDouble(lCondicionesActuales[indice][10]) ;                                      // Precio en Potencia en P3 Actual
         pPP3s = Double.parseDouble(lCondicionesSimulacion[indice][10]) ;                                    // Precio en Potencia en P3 Simulado
        
         // .........................................................................
         
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
            
             aEP1 = (eEP1 * (pEP1s-pEP1))  ;                                                                     // Ahorro en el consumo de energía P1
             aEP2 = (eEP2 * (pEP2s-pEP2))  ;                                                                     // Ahorro en el consumo de energía P2
             aEP3 = (eEP3 * (pEP3s-pEP3))  ;                                                                     // Ahorro en el consumo de energía P3
             aE   = aEP1 + aEP2 + aEP3 + (eEP * (pEPs-pEPa));       
         } else {                                                                                            // Sino hay que utilar una energía simulada
            aEP1 = (eEP1s * pEP1s ) - ( pEP1 * eEP1)  ;                                                       // Ahorro en el consumo de energía P1
            aEP2 = (eEP2s * pEP2s ) - ( pEP2 * eEP2)  ;                                                       // Ahorro en el consumo de energía P2
            aEP3 = (eEP3s * pEP3s ) - ( pEP3 * eEP3)  ;                                                       // Ahorro en el consumo de energía P3  
            aE   = aEP1 + aEP2 + aEP3 + ((eEPs * pEPs) -(eEP * pEPa));       
             
         }
         
                                                      // Ahorro en energía
         
          // .........................................................................
                                                  // Precio en Energía de Peaje
         
        
        // aPP1 = (psPC1 * dias * pPP1s) - (pPC1 * dias * pPP1) ;                                          // Ahorro en potencia contratada P1
        // aPP2 = (psPC2 * dias * pPP2s) - (pPC2 * dias * pPP2) ;                                          // Ahorro en potencia contratada P2
        // aPP3 = (psPC3 * dias * pPP3s) - (pPC3 * dias * pPP3) ;                                          // Ahorro en potencia contratada P3
         
         aPP1 = (PF1s * dias * pPP1s) - (PF1a * dias * pPP1) ;                                             // Ahorro en potencia contratada P1
         aPP2 = (PF2s * dias * pPP2s) - (PF2a * dias * pPP2) ;                                             // Ahorro en potencia contratada P2
         aPP3 = (PF3s * dias * pPP3s) - (PF3a * dias * pPP3) ;                                             // Ahorro en potencia contratada P3
         
         aP   = aPP1+aPP2+aPP3 ;
         
         ahorro = pBonificacion + impuesto_electrico * ((aE + aP ) + costeReactivaSim );                                      // Ahorro total Energia + Potencia
         
         System.out.println("impuesto electrico="+impuesto_electrico) ;
         System.out.println("aP ="+aP) ;
         System.out.println("(aE + aP )="+aE + aP) ;
         
         
         ahorro_total = ahorro_total + ahorro ;                                                             // Ahorro acumulado total
         
         coste_actual   = (eEP1 * pEP1) + (PF1a * dias * pPP1 )  ;                                          // Coste con tarifa actual 
         coste_actual  += (eEP2 * pEP2) + (PF2a * dias * pPP2 )  ;
         coste_actual  += (eEP3 * pEP3) + (PF3a * dias * pPP3 )  ;
         coste_actual  += (eEP * pEPa) ;
         // ...................................................................
         if (this.fEnergiaSimulada == 0) {                                                                   // Si no se han hecho mejoras de ahorro...
        
            coste_simulado =  (eEP1 * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2 * pEP2s) + (PF2s * dias * pPP2s ) ;
            coste_simulado += (eEP3 * pEP3s) + (PF3s * dias * pPP3s ) ;
            coste_simulado += (eEP * pEPs) ;
         
         } else {
            coste_simulado =  (eEP1s * pEP1s) + (PF1s * dias * pPP1s ) ;                                       // Coste con tarifa simulada
            coste_simulado += (eEP2s * pEP2s) + (PF2s * dias * pPP2s ) ;
            coste_simulado += (eEP3s * pEP3s) + (PF3s * dias * pPP3s ) ;
            coste_simulado += (eEPs * pEPs) ;
             
         }
         
         coste_actual_DI    = impuesto_electrico * impuesto_iva * coste_actual ;                                                 // Coste total de la factura después de impuestos.
         
         porcentaje       = 1 - ( coste_actual / coste_simulado ) ;                                         // Porcentaje de ahorro
         // ...................................................................         // CONTROL DE RESULTADO FINAL DE LA FACTURA
         pBIC    = impuesto_electrico * (coste_actual + PR) + pAlquiler -  pBonificacion ;                                         // Precio base de factura calculado
         spBIC   = formatoImporte.format(pBIC);
         pBIC    =Math.rint(pBIC*100)/100 ;
         if ( pBIC == pBIF) { 
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.green);
              jTextField80.setBackground(new Color(0xCCFFCC)); 
              jTextField81.setBackground(new Color(0xCCFFCC)); 
             
         } else {                                                                       // Error de cálculo. No coinciden
              jTextField82.setText(spBIC);
              jTextField82.setBackground(Color.red);     
              jTextField80.setBackground(Color.orange); 
              jTextField81.setBackground(Color.orange); 
         }
         // ...................................................................
         sAhorro         = formatoImporte.format(ahorro);
         sCoste_Actual   = formatoImporte.format(coste_actual);
         sCoste_Simulado = formatoImporte.format(coste_simulado);
         sAhorro_Total   = formatoImporte.format(ahorro_total);
         sPorcentaje     = formatoPorcentaje.format(porcentaje);
         
         diasOptimizado  = diasOptimizado + dias ;
         jTextField40.setText(String.valueOf(diasOptimizado));    
              
         jTextField30.setText(sAhorro);
         jTextField15.setText(sCoste_Actual);
         jTextField27.setText(sCoste_Simulado);
         jTextField5.setText(sAhorro_Total);         
         jTextField9.setText(String.valueOf(dias));
         jTextField47.setText(sPorcentaje);
         
         jTextField41.setText(String.valueOf(coste_actual));           // guardamos en campo oculto coste_actual_AI
         jTextField42.setText(String.valueOf(coste_actual_DI));        // guardamos en campo oculto coste_actual_DI
         jTextField43.setText(String.valueOf(ahorro));                 // guardamos en campo oculto ahorro
         jTextField44.setText(String.valueOf(ahorro_total));           // guardamos en campo oculto ahorro total
         jTextField45.setText(String.valueOf(coste_simulado));         // guardamos en campo oculto coste_simulado_AI
         jTextField46.setText(String.valueOf(porcentaje));              // guardamos en campo oculto porcentaje
         
         this.sMensajes = this.sMensajes + "............................... DATOS DE PARTIDA................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P1 = "+formatoNumero.format(eEP1)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P2 = "+formatoNumero.format(eEP2)+" kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Energía consumida en P3 = "+formatoNumero.format(eEP3)+" kWh" +"\n" ;
        
         if (this.fEnergiaSimulada == 1) {    
            this.sMensajes = this.sMensajes + "Energía consumida en P1 SIMULADA = "+formatoNumero.format(eEP1s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P2 SIMULADA = "+formatoNumero.format(eEP2s)+" kWh" +"\n" ;
            this.sMensajes = this.sMensajes + "Energía consumida en P3 SIMULADA = "+formatoNumero.format(eEP3s)+" kWh" +"\n" ;
          }
         
         this.sMensajes = this.sMensajes + "Potencia contratada actual P1= "+formatoNumero.format(pPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P1= "+formatoNumero.format(psPC1)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P2= "+formatoNumero.format(pPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P2= "+formatoNumero.format(psPC2)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada actual P3= "+formatoNumero.format(pPC3)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia contratada simulada P3= "+formatoNumero.format(psPC3)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P1= "+formatoNumero.format(P1M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P2= "+formatoNumero.format(P2M)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia de maximetro en P3= "+formatoNumero.format(P3M)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 ACTUAL= "+formatoNumero.format(PF1a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 ACTUAL= "+formatoNumero.format(PF2a)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 ACTUAL= "+formatoNumero.format(PF3a)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Potencia a facturar en P1 SIMULADA= "+formatoNumero.format(PF1s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P2 SIMULADA= "+formatoNumero.format(PF2s)+" kW" +"\n" ;
         this.sMensajes = this.sMensajes + "Potencia a facturar en P3 SIMULADA= "+formatoNumero.format(PF3s)+" kW" +"\n" ;
         
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 con contrato actual = "+pEP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P1 Simulado (contrato anterior) = "+pEP1s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 con contrato actual = "+pEP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P2 Simulado (contrato anterior) = "+pEP2s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 con contrato actual = "+pEP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Energía en P3 Simulado (contrato anterior) = "+pEP3s+" €/kWh" +"\n \n" ; 
         this.sMensajes = this.sMensajes + "Impuesto eléctrico = "+impuesto_electrico+"\n \n" ;  
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Actual = "+pPP1+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P1 Simulado = "+pPP1s+" €/kWh" +"\n \n" ;   
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Actual = "+pPP2+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P2 Simulado = "+pPP2s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Actual = "+pPP3+" €/kWh" +"\n" ;
         this.sMensajes = this.sMensajes + "Precio en Potencia en P3 Simulado = "+pPP3s+" €/kWh" +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado  = "+formatoImporte.format(ahorro_acumulado)+"\n \n" ; 
         this.sMensajes = this.sMensajes + "............................... CÁLCULOS ................................."+"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa actual   -> (impuesto_electrico * ((eEP1 * pEP1) + (eEP2 * pEP2) + (eEP3 * pEP3) ) + (dias * pPP1 * pPC1 ) + (dias * pPP2 * pPC2 ) +( dias * pPP3 * pPC3 )))= \n"+formatoImporte.format(coste_actual) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste con tarifa simulada -> (impuesto_electrico * ((eEP1 * pEP1s) + (eEP2 * pEP2s) + (eEP3 * pEP3s) ) + (dias * pPP1s * pPC1s ) + (dias * pPP2s * pPC2s ) +( dias * pPP3s * pPC3s )))= \n"+formatoImporte.format(coste_simulado) +"\n" ;
         this.sMensajes = this.sMensajes + "Coste total de la factura después de impuestos. -> (coste_actual_DI    = impuesto_iva * coste_actual)= \n"+formatoImporte.format(coste_actual_DI) +"\n \n" ;
         this.sMensajes = this.sMensajes + "Ahorro en el consumo de energía = \n"+formatoImporte.format(aE) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro en potencia contratada = \n"+formatoImporte.format(aP) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro total Energia + Potencia (con impuesto eléctrico)= \n"+formatoImporte.format(ahorro) +"\n" ;
         this.sMensajes = this.sMensajes + "Ahorro acumulado total = \n"+formatoImporte.format(ahorro_total) +"\n" ;
         this.sMensajes = this.sMensajes + "Porcentaje de ahorro = \n"+formatoPorcentaje.format(porcentaje) +"\n" ;
     }
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
   public double redondear( double numero, int decimales ) {
    return Math.round(numero*Math.pow(10,decimales))/Math.pow(10,decimales);
  }
 // ---------------------------------------------------------------------------------------------------------------------------
    public void ValidarCondicionesSimuladas() {
        
        int resp=JOptionPane.showConfirmDialog(null,"¿VALIDAR LAS NUEVAS CONDICIONES SIMULACION ?");
        String sqlStr,sFech1,sFech2,sDescripcion,strTarifa ;
        Double importe_AI, importe_DI ;
        int estadoInsert=0, id_tF=0,id_tipo_actual, id_tipo_sim,id_cond_actuales, id_cond_sim, fPotMax = 0;
     
        
        if (JOptionPane.OK_OPTION == resp){  
        
            // ..............................................................
            
             this.lCondicionesSimulacion[this.indGen][0] = jTextField29.getText();  
             this.lCondicionesSimulacion[this.indGen][1] = jTextField29.getText();     
             this.lCondicionesSimulacion[this.indGen][2] = jTextField26.getText();            // potencia contratada
             this.lCondicionesSimulacion[this.indGen][3] = jTextField37.getText();    
             this.lCondicionesSimulacion[this.indGen][4] = jTextField38.getText(); 
             this.lCondicionesSimulacion[this.indGen][5] = "0";    
             this.lCondicionesSimulacion[this.indGen][6] = "0";    
             this.lCondicionesSimulacion[this.indGen][7] = "0";    
             this.lCondicionesSimulacion[this.indGen][8] =  jTextField23.getText();           // precio potencia
             this.lCondicionesSimulacion[this.indGen][9] =  jTextField24.getText();       
             this.lCondicionesSimulacion[this.indGen][10] = jTextField25.getText();     
             this.lCondicionesSimulacion[this.indGen][11] = "0";   
             this.lCondicionesSimulacion[this.indGen][12] = "0";   
             this.lCondicionesSimulacion[this.indGen][13] = "0";   
             this.lCondicionesSimulacion[this.indGen][14] = jTextField20.getText();           // precio energia
             this.lCondicionesSimulacion[this.indGen][15] = jTextField21.getText();      
             this.lCondicionesSimulacion[this.indGen][16] = jTextField22.getText();   
             this.lCondicionesSimulacion[this.indGen][17] = "0";   
             this.lCondicionesSimulacion[this.indGen][18] = "0";   
             this.lCondicionesSimulacion[this.indGen][19] = "0";      
             this.lCondicionesSimulacion[this.indGen][20] = jTextArea3.getText();             // observaciones
             this.lCondicionesSimulacion[this.indGen][24] = jTextField112.getText();          // Alquiler
             
             if (jCheckBox6.isSelected()){
                    this.lCondicionesActuales[this.indGen][25] = "1";
                    fPotMax = 1;
             }
              else {
                    this.lCondicionesActuales[this.indGen][25] = "0" ;
                    fPotMax = 0;
             }
            // ..............................................................
             
            
            sDescripcion = this.listaPuntosSum[this.indGen][1] ; 
             
             
           
            sFech1 = jTextField29.getText(); sFech1.trim();     // fecha inicio
            sFech1 = dateToMySQLDate(sFech1);       

            sFech2 = jTextField29.getText(); sFech2.trim();     // fecha fin
            sFech2 = dateToMySQLDate(sFech2);    

           
            saepDao misaepDao3 = new saepDao();
            saepDao misaepDao4 = new saepDao();
          
            // .....................................
            System.out.println("this.lCondicionesSimulacion["+this.indGen+"][22]"+this.lCondicionesSimulacion[this.indGen][22]);
            System.out.println("this.lCondicionesSimulacion["+this.indGen+"][21] ="+this.lCondicionesSimulacion[this.indGen][21]);
            
            
            if (this.lCondicionesSimulacion[this.indGen][22] != null) {
            
                id_tipo_actual      = Integer.parseInt(this.lCondicionesSimulacion[this.indGen][22]) ;
            } else {
                id_tipo_actual      = 0 ;
            }
            if (this.lCondicionesSimulacion[this.indGen][21] != null) {
                id_cond_actuales    = Integer.parseInt(this.lCondicionesSimulacion[this.indGen][21]) ;
            } else {
                id_cond_actuales      = 0 ;
            }
            
            System.out.println("id_tipo_actual   ="+id_tipo_actual);
            System.out.println("id_cond_actuales   ="+id_cond_actuales);
        
             // .......................................................................................................................................  
             // .....................................         INSERTA FILA  DE TARIFA 2.0A a TARIFA 2.0A

            if ( this.tipo_Sim== 1) {                        // .................................   INSERTAMOS EN t_f_20a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20a (id_cliente,id_punto,descripcion,potencia_contratada,precio_potencia,precio_energia,fecha_inicio,fecha_fin,observaciones,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',"; 
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                      // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
                    
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                  
                     
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion
                     if (id_cond_actuales !=0){ 
                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion
                  
                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     }
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+jTextField39.getText()+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarSim.setVisible(false);
                 }  
             }
             // .......................................................................................................................................  
             // .....................................         INSERTA FILA  A a TARIFA 2.0ADH

            if ( this.tipo_Sim==2) {                        // .................................   INSERTAMOS EN t_C20DHA

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20dha (id_cliente,id_punto,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_p2,fecha_inicio,fecha_fin,observaciones,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField21.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',"; 
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                      // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
                    
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                  
                     
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion
                     if (id_cond_actuales !=0){ 
                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     }
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+jTextField39.getText()+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarSim.setVisible(false);
                 }  
             }
            // .......................................................................................................................................  
             // .....................................         INSERTA FILA  DE TARIFA 2.1A a TARIFA 2.1A DHA

            if ( this.tipo_Sim == 3) {                               // .................................   INSERTAMOS EN t_C_21a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21a (id_cliente,id_punto,descripcion,potencia_contratada,precio_potencia,precio_energia,fecha_inicio,fecha_fin,observaciones,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',"; 
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                      // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
                    
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                  
                     
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion
                      
                     sqlStr = "UPDATE t_c21a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+jTextField39.getText()+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarSim.setVisible(false);
                 }         


             }
             if ( this.tipo_Sim == 4 ) {                               // .................................   INSERTAMOS EN t_c_21dha

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21dha(id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_p2,fecha_inicio,fecha_fin,observaciones,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField34.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField21.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',"; 
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){
                    
                                        
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21dha ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                   
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Sim];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
               if ( this.tipo_Sim == 5 ) {                               // .................................   INSERTAMOS EN t_c3.0a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c30a(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField34.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField37.getText()+",";
                 sqlStr += jTextField38.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField24.getText()+",";
                 sqlStr += jTextField25.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField21.getText()+",";
                 sqlStr += jTextField22.getText()+",";                 
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',";   
                 sqlStr += fPotMax+",";
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 
                 System.out.println("INSERTAMOS 3.0A ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.0A ',id_tarifa_actual=5 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c30a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                //     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                //     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Sim];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea3.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    
                    
                    
                 } 
                 
                    botonValidarSim.setVisible(false);

             }
            if ( this.tipo_Sim == 6 ) {                               // .................................   INSERTAMOS EN t_c3.1a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c31a(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField34.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField37.getText()+",";
                 sqlStr += jTextField38.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField24.getText()+",";
                 sqlStr += jTextField25.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField21.getText()+",";
                 sqlStr += jTextField22.getText()+",";                 
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',";   
                 sqlStr += fPotMax+",";
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 
                 System.out.println("INSERTAMOS 3.1A ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.1A ',id_tarifa_actual=6 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c31a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                //     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                //     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Sim];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea3.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    
                    
                    
                 } 
                 
                    botonValidarSim.setVisible(false);

             }
        if ( this.tipo_Sim == 10 ) {                               // .................................   INSERTAMOS EN t_c3.0aindx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c30aindx(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField34.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField37.getText()+",";
                 sqlStr += jTextField38.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField24.getText()+",";
                 sqlStr += jTextField25.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField21.getText()+",";
                 sqlStr += jTextField22.getText()+",";     
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',";   
                 sqlStr += fPotMax+",";
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.0A INDX ',id_tarifa_actual=10 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c30aindx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                     
                                                              
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea3.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";
                    
                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    
                    
                    
                 } 
                 
                    botonValidarCAct.setVisible(false);

             }
            // .......................................................................................................................................  
             // .....................................         INSERTA FILA   TARIFA 2.0 INDX

            if ( this.tipo_Sim== 11) {                        // .................................   INSERTAMOS EN t_C20indx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20indx (id_cliente,id_punto,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField74.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',"; 
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                      // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20indx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
                    
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                  
                     
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+jTextField39.getText()+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    
                 }  
                 botonValidarSim.setVisible(false);
             }
            // .......................................................................................................................................  
             // .....................................         INSERTA FILA   TARIFA 2.1 INDX

            if ( this.tipo_Sim== 12) {                        // .................................   INSERTAMOS EN t_C21indx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21indx (id_cliente,id_punto,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField74.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',";  
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                      // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21indx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
                    
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                  
                     
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+jTextField39.getText()+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    
                 }  
                 botonValidarSim.setVisible(false);
             }
            
            if ( this.tipo_Sim == 13 ) {                               // .................................   INSERTAMOS EN t_c3.1aindx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c31aindx(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,alquiler,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField34.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField26.getText()+",";
                 sqlStr += jTextField37.getText()+",";
                 sqlStr += jTextField38.getText()+",";
                 sqlStr += jTextField23.getText()+",";
                 sqlStr += jTextField24.getText()+",";
                 sqlStr += jTextField25.getText()+",";
                 sqlStr += jTextField20.getText()+",";
                 sqlStr += jTextField21.getText()+",";
                 sqlStr += jTextField22.getText()+",";     
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea3.getText()+"',";   
                 sqlStr += fPotMax+",";
                 sqlStr += jTextField112.getText()+",";
                 sqlStr += "1"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.1A INDX ',id_tarifa_actual=13 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c31aindx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                   this.lCondicionesSimulacion[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones simulacion 
                     
                                                              
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=1" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Sim+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea3.getText()+"',"; 
                    sqlStr += "'"+jTextField34.getText()+"',";
                    sqlStr += "1"+")";
                    
                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    
                 } 
                 
                    botonValidarCAct.setVisible(false);

             }
            
        } // End if RESP=si
        
    }
// -----------------------------------------------------------------------------------------------------------------------------------------
 public void ValidarCondicionesActuales() {
        
        int resp=JOptionPane.showConfirmDialog(null,"¿VALIDAR LAS NUEVAS CONDICIONES ACTUALES ?");
        String sqlStr,sFech1,sFech2,sDescripcion,strTarifa ;
        Double importe_AI, importe_DI ;
        
        int estadoInsert=0, id_tF=0,id_tipo_actual, id_tipo_sim,id_cond_actuales, id_cond_sim,fPotMax = 0  ;
     
        
        if (JOptionPane.OK_OPTION == resp){  
        
            // ..............................................................
             this.listaContratosPuntosAct[this.indGen][8] = jTextField33.getText();         // nombre de compañia
             
            
             this.lCondicionesActuales[this.indGen][0] = jTextField28.getText();  
             this.lCondicionesActuales[this.indGen][1] = jTextField11.getText();     
             this.lCondicionesActuales[this.indGen][2] = jTextField19.getText();            // potencia contratada
             this.lCondicionesActuales[this.indGen][3] = jTextField35.getText();    
             this.lCondicionesActuales[this.indGen][4] = jTextField36.getText(); 
             this.lCondicionesActuales[this.indGen][5] = "0";    
             this.lCondicionesActuales[this.indGen][6] = "0";    
             this.lCondicionesActuales[this.indGen][7] = "0";    
             this.lCondicionesActuales[this.indGen][8] =  jTextField16.getText();           // precio potencia
             this.lCondicionesActuales[this.indGen][9] =  jTextField17.getText();       
             this.lCondicionesActuales[this.indGen][10] = jTextField18.getText();     
             this.lCondicionesActuales[this.indGen][11] = "0";   
             this.lCondicionesActuales[this.indGen][12] = "0";   
             this.lCondicionesActuales[this.indGen][13] = "0";   
             this.lCondicionesActuales[this.indGen][14] = jTextField12.getText();           // precio energia
             this.lCondicionesActuales[this.indGen][15] = jTextField13.getText();      
             this.lCondicionesActuales[this.indGen][16] = jTextField14.getText();   
             this.lCondicionesActuales[this.indGen][17] = "0";   
             this.lCondicionesActuales[this.indGen][18] = "0";   
             this.lCondicionesActuales[this.indGen][19] = "0";      
             this.lCondicionesActuales[this.indGen][20] = jTextArea2.getText();             // observaciones
             this.lCondicionesActuales[this.indGen][23] = jTextField73.getText();           // Precio energía de peaje
             this.lCondicionesActuales[this.indGen][24] = jTextField111.getText();          // Alquiler
             
             if (jCheckBox5.isSelected()){
                    this.lCondicionesActuales[this.indGen][25] = "1";
                    fPotMax = 1;
             }
              else {
                    this.lCondicionesActuales[this.indGen][25] = "0" ;
                    fPotMax = 0;
             }
             
            // ..............................................................
             
            
            sDescripcion = this.listaPuntosSum[this.indGen][1] ; 
             
             
           
            sFech1 = jTextField28.getText(); sFech1.trim();     // fecha inicio
            sFech1 = dateToMySQLDate(sFech1);       

            sFech2 = jTextField11.getText(); sFech2.trim();     // fecha fin
            sFech2 = dateToMySQLDate(sFech2);    

           
            saepDao misaepDao3 = new saepDao();
          
            saepDao misaepDao4 = new saepDao();
            // .....................................
            
            System.out.println("this.lCondicionesActuales["+this.indGen+"][22]="+this.lCondicionesActuales[this.indGen][22]);
            System.out.println("this.lCondicionesActuales["+this.indGen+"][21]="+this.lCondicionesActuales[this.indGen][21]);

           try {
                id_tipo_actual      = Integer.parseInt(this.lCondicionesActuales[this.indGen][22]) ;
                id_cond_actuales    = Integer.parseInt(this.lCondicionesActuales[this.indGen][21]) ;
            } catch (NumberFormatException ex) {
            
                 id_tipo_actual      = this.id_tipo_Actual ;
                 id_cond_actuales    = this.id_tipo_Sim ;
            }
             

            // .......................................................................................................................................  
            // .....................................         INSERTA FILA  DE TARIFA 2.0A a TARIFA 2.0A

            if ( this.tipo_Act == 1 ) {                               // .................................   INSERTAMOS EN t_f_20a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20a (id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                    // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.0 A',id_tarifa_actual=1 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 }         


             }
        if ( this.tipo_Act == 2 ) {                               // .................................   INSERTAMOS EN t_f_20a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20dha(id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_p2,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){
                    
                    // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.0DHA',id_tarifa_actual=2 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                        
                     
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20dha ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
         // .......................................................................................................................................  
             // .....................................         INSERTA FILA  DE TARIFA 2.1A a TARIFA 2.1A

            if ( this.tipo_Act == 3 ) {                               // .................................   INSERTAMOS EN t_f_20a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21a (id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                    // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.1 A',id_tarifa_actual=3 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20a SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 }         


             }
         if ( this.tipo_Act == 4 ) {                               // .................................   INSERTAMOS EN t_f_20a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21dha(id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_p2,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){
                    
                    // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.1DHA',id_tarifa_actual=4 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                        
                     
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21dha ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
        if ( this.tipo_Act == 8 ) {                               // .................................   INSERTAMOS EN t_c20dhaindx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20dhaindx(id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_p2,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.0DHA INDX',id_tarifa_actual=8 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20dhaindx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
        if ( this.tipo_Act == 9 ) {                               // .................................   INSERTAMOS EN t_c21dhaindx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21dhaindx(id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_p2,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.1DHA INDX',id_tarifa_actual=9 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21dhaindx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;   System.out.println("El nuevo identificador para tipo 9 es:"+id_tF);
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                //     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                //     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
        if ( this.tipo_Act == 5 ) {                               // .................................   INSERTAMOS EN t_c3.0a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c30a(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField35.getText()+",";
                 sqlStr += jTextField36.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField17.getText()+",";
                 sqlStr += jTextField18.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += jTextField14.getText()+",";                 
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";     
                 sqlStr += fPotMax+",";
                 sqlStr += "2"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.0A ',id_tarifa_actual=5 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c30a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                //     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                //     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
          if ( this.tipo_Act == 6 ) {                               // .................................   INSERTAMOS EN t_c3.1a

                 // .....................................

                 sqlStr  ="INSERT INTO t_c31a(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField35.getText()+",";
                 sqlStr += jTextField36.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField17.getText()+",";
                 sqlStr += jTextField18.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += jTextField14.getText()+",";                 
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";     
                 sqlStr += fPotMax+",";
                 sqlStr += "2"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.1A ',id_tarifa_actual=6 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c31a ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                //     sqlStr = "UPDATE t_c20dha SET id_estado=0 WHERE id="+id_cond_actuales ;

                //     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                                         
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
        if ( this.tipo_Act == 10 ) {                               // .................................   INSERTAMOS EN t_c3.0aindx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c30aindx(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField35.getText()+",";
                 sqlStr += jTextField36.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField17.getText()+",";
                 sqlStr += jTextField18.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += jTextField14.getText()+",";    
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',"; 
                 sqlStr += fPotMax+",";
                 sqlStr += "2"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.0A INDX ',id_tarifa_actual=10 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c30aindx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                                                              
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
            // .......................................................................................................................................  
            // .....................................         INSERTA FILA  DE TARIFA 2.0A a TARIFA 2.0A INDX

            if ( this.tipo_Act == 11) {                               // .................................   INSERTAMOS EN t_c20indx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c20indx (id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                    // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.0 INDX',id_tarifa_actual=11 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c20indx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 }         


             }
             // .......................................................................................................................................  
            // .....................................         INSERTA FILA   TARIFA 2.1A INDX

            if ( this.tipo_Act == 12) {                               // .................................   INSERTAMOS EN t_c21indx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c21indx (id_cliente,id_punto,compañia,descripcion,potencia_contratada,precio_potencia,precio_energia_p1,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',";            
                 sqlStr += "2"+")";
                 

                 System.out.println(sqlStr);
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                    // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='2.1 INDX',id_tarifa_actual=12 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c21indx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                      // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c21indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                     
                     // .....................................                                actualizo el estado de la tabla de condiciones de facturacion

                     sqlStr = "UPDATE t_c20indx SET id_estado=0 WHERE id="+id_cond_actuales ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                    
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 }         
             }
            
            if ( this.tipo_Act == 13 ) {                               // .................................   INSERTAMOS EN t_c3.1aindx

                 // .....................................

                 sqlStr  ="INSERT INTO t_c31aindx(id_cliente,id_punto,compañia,descripcion,potencia_contratada_p1,potencia_contratada_p2,potencia_contratada_p3,precio_potencia_p1,precio_potencia_p2,precio_potencia_p3,precio_energia_p1,precio_energia_p2,precio_energia_p3,precio_energia_peaje,fecha_inicio,fecha_fin,observaciones,fPotenciaMaxima,id_estado) VALUES (";
                 sqlStr += this.id_cliente_actual+",";
                 sqlStr += this.id_punto_actual+",";
                 sqlStr += "'"+jTextField33.getText()+"',";
                 sqlStr += "'"+sDescripcion+"',";
                 sqlStr += jTextField19.getText()+",";
                 sqlStr += jTextField35.getText()+",";
                 sqlStr += jTextField36.getText()+",";
                 sqlStr += jTextField16.getText()+",";
                 sqlStr += jTextField17.getText()+",";
                 sqlStr += jTextField18.getText()+",";
                 sqlStr += jTextField12.getText()+",";
                 sqlStr += jTextField13.getText()+",";
                 sqlStr += jTextField14.getText()+",";    
                 sqlStr += jTextField73.getText()+",";
                 sqlStr += "'"+sFech1+"',";
                 sqlStr += "'"+sFech2+"',";
                 sqlStr += "'"+jTextArea2.getText()+"',"; 
                 sqlStr += fPotMax+",";
                 sqlStr += "2"+")";
                 
                 System.out.println("INSERTAMOS INDEXADO ="+sqlStr);

              
                 estadoInsert= misaepDao3.registrarFila(sqlStr);

                 // .....................................
                 if (estadoInsert==0){

                     // .....................................                                actualizo el estado de la tabla de datos del punto de suministro
                     sqlStr = "UPDATE t_datos_puntos_suministro SET tarifa_actual='3.1A INDX ',id_tarifa_actual=13 WHERE idd="+this.id_punto_actual ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                   
                    // .....................................                                consultamos el último id que se ha asignado
                
                    sqlStr = "SELECT id FROM t_c31aindx ORDER BY id DESC LIMIT 1 ";

                    estadoInsert= misaepDao4.ultimoIdentificador(sqlStr);

                    id_tF               = misaepDao4.id ;
               
                    this.lCondicionesActuales[this.indGen][21] = String.valueOf(id_tF );     // Actualizo el identificador de condiciones actuales   
                    
                                                              
                      // .....................................                                actualizo el estado de la tabla de contratos puntos a 0
                     
                     sqlStr = "UPDATE t_contratos_puntos SET id_estado=0 WHERE id_punto="+this.id_punto_actual+ " AND id_estado=2" ;

                     estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                      // .....................................                                Inserto nuevo registro con los cambios producidos
                     
                     strTarifa = this.lTiposTarifas[this.tipo_Act];                          // Tipo de tarifa

                    sqlStr  ="INSERT INTO t_contratos_puntos  (id_cliente,id_punto,fecha_contrato,fecha_fin,id_tarifa,id_condiciones_contrato,descripcion,"
                            + "fecha_realizacion_cambio,observaciones,compañia,id_estado) VALUES (";
                    sqlStr += this.id_cliente_actual+",";
                    sqlStr += this.id_punto_actual+",";
                    sqlStr += "'"+sFech1+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += this.tipo_Act+",";
                    sqlStr += id_tF+",";
                    sqlStr += "'"+strTarifa+"',";
                    sqlStr += "'"+sFech2+"',";
                    sqlStr += "'"+jTextArea2.getText()+"',"; 
                    sqlStr += "'"+jTextField33.getText()+"',";
                    sqlStr += "2"+")";
                 

                    System.out.println(sqlStr);
                    estadoInsert= misaepDao3.registrarFila(sqlStr);
                     
                    botonValidarCAct.setVisible(false);
                    
                    
                 } 
                 


             }
        } // END IF resp=si
        
        
    }
// ------------------------------------------------------------------------------------------------------------------------
 private void actualizarTablaAnexoResumenAhorro(int id_cliente) {
        
     String sFecha="";
     
               
                // ...................................................
     
     
                if (this.filtrobusca == 1) {                
                    sFecha = jTextField32.getText();   sFecha = sFecha.trim();                 
                    sFecha = dateToMySQLDate(sFecha); 
                } else {
                    
                    sFecha = dateToMySQLDate(this.FechaUltimoCalculo.trim()) ;
                    
                    
                }
                   
                //....................................................
     
     
     
                DefaultTableModel model3;
		model3 = new DefaultTableModel();        // definimos el objeto tableModel
               
		miTabla03 = new JTable();                // creamos la instancia de la tabla
		miTabla03.setModel(model3);
                 
                model3.addColumn("FECHA");  
                model3.addColumn("DESDE"); 
                model3.addColumn("HASTA"); 
                model3.addColumn("NOMBRE");                
                model3.addColumn("DIRECCIÓN"); 
                model3.addColumn("LOCALIDAD"); 
                model3.addColumn("CUPS"); 
                model3.addColumn("TARIFA"); 
                model3.addColumn("DIAS DE CALCULO");
                model3.addColumn("AHORRO CONSEGUIDO");
                model3.addColumn("AHORRO TOTAL");
                model3.addColumn("AHORRO %"); 
                model3.addColumn("DIAS FACT. OPT.");
              
              
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                miTabla03.setDefaultRenderer(Double.class, formato); 
                miTabla03.setDefaultRenderer(String.class, formato); 
                miTabla03.setDefaultRenderer(Integer.class, formato);
                miTabla03.setDefaultRenderer(Object.class, formato);
		
                TableColumn columna3 = miTabla03.getColumn("NOMBRE");
                TableColumn columna4 = miTabla03.getColumn("DIRECCIÓN");
                TableColumn columna5 = miTabla03.getColumn("LOCALIDAD");
                TableColumn columna6 = miTabla03.getColumn("CUPS");
                TableColumn columna7 = miTabla03.getColumn("AHORRO CONSEGUIDO");
                TableColumn columna8 = miTabla03.getColumn("AHORRO TOTAL");
                TableColumn columna9 = miTabla03.getColumn("AHORRO %");
                TableColumn columna10 = miTabla03.getColumn("DIAS FACT. OPT.");
                
                columna3.setMinWidth(130);
                columna4.setMinWidth(220);
                columna5.setMinWidth(120);
                columna6.setMinWidth(150);
                columna7.setMinWidth(50);
                columna8.setMinWidth(50);
                columna9.setMinWidth(50);
                columna10.setMinWidth(150);
                
                
               saepDao misaepDao = new saepDao();
                
                
		misaepDao.consultaResumenAhorrosCliente(model3,id_cliente,sFecha,0);        // Genero tabla resumen
                misaepDao.consultaResumenAhorrosCliente(model3,id_cliente,sFecha,1);        // Calculo totales
                misaepDao.consultaResumenAhorrosCliente(model3,id_cliente,sFecha,2);        // Calculo SUBTOTALES AHORRO Y DIAS
                miBarra03.setViewportView(miTabla03);
                
                this.nPuntosCalAhorro = misaepDao.nPuntosCalAhorro ;        // Numero total de puntos de cálculo de ahorro para esa fecha
                jTextField76.setText(String.valueOf(this.nPuntosCalAhorro));
                
                 // ----------------------------------------------------------------------
                
                this.ahorro_total_actual = misaepDao.ahorroTotal ;             System.out.println("Ahorro total calculado ="+this.ahorro_total_actual);
                this.pAhorro             = misaepDao.pAhorro ;
               
                
                NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
                NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
               

                formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
                
     
                String sAhorro    = formatoImporte.format(this.ahorro_total_actual);
                String sporAhorro = formatoPorcentaje.format(this.pAhorro);
                
                jTextField63.setText(sAhorro);
                jTextField64.setText(sporAhorro);
                
                
                // ----------------------------------------------------------------------
                
 }    
 // ------------------------------------------------------------------------------------------------------------------------
 private void actualizarTablaAnexoDetalleAhorro(int id_cliente) throws SQLException {
                 int i, nCal, idCA, idTCA, id_punto,dias ;
                 String sAhorro,sFecha1,sFecha2 ;

                  String sFecha="";
     
               
                // ...................................................
     
     
                if (this.filtrobusca == 1) {                
                    sFecha = jTextField32.getText();   sFecha = sFecha.trim();                 
                    sFecha = dateToMySQLDate(sFecha); 
                } else {
                    
                    sFecha = dateToMySQLDate(this.FechaUltimoCalculo.trim()) ;
                    
                    
                }
                   
                //....................................................
     
                 
                 
                 
                 NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
                 NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
                 NumberFormat formatoNumero = NumberFormat.getNumberInstance();
                 formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
                 SimpleDateFormat formatDateJava = new SimpleDateFormat("dd-MM-yyyy");
            

                 DefaultTableModel model22;

                 model22 = new DefaultTableModel();                                      // definimos el objeto tableModel dumi
               
                DefaultTableModel model4;
		model4 = new DefaultTableModel();        // definimos el objeto tableModel
               
		miTabla04 = new JTable();                // creamos la instancia de la tabla
		miTabla04.setModel(model4);
                 
                
                
                
                model4.addColumn("NOMBRE"); 
                model4.addColumn("DIRECCIÓN"); 
                model4.addColumn("LOCALIDAD"); 
                model4.addColumn("CUPS"); 
                model4.addColumn("TARIFA ACTUAL"); 
                model4.addColumn("FECHA INI"); 
                model4.addColumn("FECHA FIN"); 
                model4.addColumn("DIAS");                               // 7
                model4.addColumn("DIAS DESDE OPTIMIZACIÓN");
                model4.addColumn("AHORRO CONSEGUIDO");
                model4.addColumn("AHORRO");                             // 10
                model4.addColumn("E. P1 kWh"); 
                model4.addColumn("E. P2 kWh"); 
                model4.addColumn("E. P3 kWh");
                model4.addColumn("E. Peaje kWh");
                model4.addColumn("P.Act. P1 €/kWh");
                model4.addColumn("P.Act. P2 €/kWh");
                model4.addColumn("P.Act. P3 €/kWh");
                model4.addColumn("P.Act. E peaje €/kWh");
                model4.addColumn("P.Sim. P1 €/kWh");
                model4.addColumn("P.Sim. P2 €/kWh");                  // 20 
                model4.addColumn("P.Sim. P3 €/kWh");
                model4.addColumn("P.Sim. E peaje €/kWh");
                model4.addColumn("Act. P1 kW"); 
                model4.addColumn("Sim. P1 kW"); 
                model4.addColumn("P.Act. P1 €/kW·dia");                
                model4.addColumn("P.Sim. P1 €/kW·dia");
                model4.addColumn("Act. P2 kW"); 
                model4.addColumn("Sim. P2 kW"); 
                model4.addColumn("P.Act. P2 €/kW·dia");                
                model4.addColumn("P.Sim. P2 €/kW·dia");
                model4.addColumn("Act. P3 kW"); 
                model4.addColumn("Sim. P3 kW"); 
                model4.addColumn("P.Act. P3 €/kW·dia");                
                model4.addColumn("P.Sim. P3 €/kW·dia");
                
                miTabla04.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		miTabla04.getTableHeader().setReorderingAllowed(false);
                
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                miTabla04.setDefaultRenderer(Double.class, formato); 
                miTabla04.setDefaultRenderer(String.class, formato); 
                miTabla04.setDefaultRenderer(Integer.class, formato);
                miTabla04.setDefaultRenderer(Object.class, formato);
		
               saepDao misaepDao = new saepDao();
                
                
		tablaC03 = new JTable();                                          // creamos la instancia de la tabla
		tablaC03.setModel(model22);
                 
                model22.addColumn("DATO"); 
                model22.addColumn("VALOR");       
                
                
                saepDao misaepDao5 = new saepDao();
                
                misaepDao5.consultaCalculoDetalles(model4,this.id_cliente_actual,sFecha);
                
                this.lhistoricoCalculos = misaepDao5.lhistoricoCalculos ;
                this.nPuntosCalAhorroDetalle = misaepDao5.nCalculosDetalle; System.out.println("---------------------------------------- > TENEMOS nCalculosDetalle="+this.nPuntosCalAhorroDetalle);
                                
                misaepDao5.consultaFechasFacturas();
                misaepDao5.consultaDatosActualesDetalle();
                misaepDao5.consultaDatosSimulacionDetalle();
                
                for (i=0; i<misaepDao5.nCalculosDetalle; i++){
               
                    
                        
                        // ...............................................
                       

                        model4.setValueAt(misaepDao5.lDatosCalculos[i][0] , i, 5);                     // fecha  inicio calculo
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][1] , i, 6);                     // fecha  fin calculo
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][2] , i, 11);                      // p1 energia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][3] , i, 12);                      // p2 energia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][4] , i, 13);                      // p3 energia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][5] , i, 14);  // energia peaje actual
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][7] , i, 23);                      // p1 potencia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][8] , i, 27);                      // p2 potencia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][9] , i, 31);                      // p3 potencia actual
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][10] , i, 25);                      // p1 €/kW·dia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][11] , i, 29);                      // p2 €/kW·dia actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][12] , i, 33);                      // p3 €/kW·dia actual
                        
                        
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][13] , i, 15);                      // p1 €/kWh actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][14] , i, 16);                      // p2 €/kWh actual
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][15] , i, 17);                      // p3 €/kWh actual
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][25] , i, 18);                      // precio de peaje
                      
                    //    System.out.println("Calculo fechas para i="+i);
                        sFecha1 = misaepDao5.lDatosCalculos[i][0]; sFecha1.trim();
                        sFecha2 = misaepDao5.lDatosCalculos[i][1]; sFecha2.trim();
    
                        dias = diferenciaFechas(sFecha1, sFecha2 ,1);    
                       
                        model4.setValueAt(dias , i, 7);                                                 // dias de calculo
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][16] , i, 24);                      // p1 potencia simulacion
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][17] , i, 28);                      // p2 potencia simulacion
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][18] , i, 32);                      // p3 potencia simulacion
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][19] , i, 26);                      // p1 €/kW·dia simulacion
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][20] , i, 30);                      // p2 €/kW·dia simulacion
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][21] , i, 34);                      // p3 €/kW·dia simulacion
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][22] , i, 19);                      // p1 €/kWh simulacion
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][23] , i, 20);                      // p2 €/kWh simulacion
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][24] , i, 21);                      // p3 €/kWh simulacion
                        
                        model4.setValueAt(misaepDao5.lDatosCalculos[i][25] , i, 22);                      // precio de peaje simulacion
              
                }
            
                miBarra04.setViewportView(miTabla04);
                
 }    
 // -----------------------------------------------------------------------------------------------------------
       public void GenerarExelAnexoResumen(){
           
        String col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12 ;
       
        int i,j,nR;
       
        System.out.println("APUNTO DE GENERAR EXEL RESUMEN ANEXO");
        
        int resp=JOptionPane.showConfirmDialog(null,"¿Crear EXCELs  de ANEXO RESUMEN : ?");
        
        
        if (JOptionPane.OK_OPTION == resp){
        

                    nR  = this.nPuntosCalAhorro ;
                   

            HSSFWorkbook libro = new HSSFWorkbook();        
            HSSFSheet hoja = libro.createSheet("RESUMEN");
            Row fila = hoja.createRow(0);        
            Cell celda;


            String[] titulos = { "NOMBRE","DIRECCIÓN TIENDA","LOCALIDAD","CUPS","TARIFA","FECHA INI.","FECHA FIN","DIAS DESDE OPTIMIZACIÓN",
                                 "AHORRO CONSEGUIDO","AHORRO TOTAL", "% AHORRO", "DIAS FACT. OPT." };                                      // 9 CAMPOS

            NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
            NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
                
     
            
           
            // Creamos el encabezado

            for (i = 0; i < titulos.length; i++) {
                  celda = fila.createCell(i);
                  celda.setCellValue(titulos[i]);
            }
 

            for (j=0; j<nR; j++) {

               // Nueva fila 
               i = 0 ;
               fila = hoja.createRow(j+1);

               System.out.println("Inserto celdas Exel en fila  ="+j);
               
               col10= (String) miTabla03.getValueAt(j, 1) ;                              //FECHA INI
               col11= (String) miTabla03.getValueAt(j, 2) ;                              //FECHA FIN        
               
               col1 = (String) miTabla03.getValueAt(j, 3) ;                              //NOMBRE
               col2 = (String) miTabla03.getValueAt(j, 4) ;                              // DIRECCIÓN TIENDA
               col3 = (String) miTabla03.getValueAt(j, 5);                               // LOCALIDAD
               col4 = (String) miTabla03.getValueAt(j, 6);                               // CUPS
               col5 = (String) miTabla03.getValueAt(j, 7);                               // TARIFA
               col6 = String.valueOf(miTabla03.getValueAt(j, 8));                        // DIAS DESDE OPTIMIZACIÓN       
               
               String sAhorro       = formatoNumero.format(miTabla03.getValueAt(j, 9));                              
             //  col7 = sAhorro ;                                                          //AHORRO CONSEGUIDO
               col7 = String.valueOf(miTabla03.getValueAt(j, 9)); 
              
               String sAhorroTotal = formatoNumero.format(miTabla03.getValueAt(j, 10));     
             //  col8 = sAhorroTotal ;                                                    //AHORRO TOTAL
               col8 = String.valueOf(miTabla03.getValueAt(j, 10));  
               col9 = String.valueOf(miTabla03.getValueAt(j, 11));                        // Porcentaje ahorro
               
               col12 = String.valueOf(miTabla03.getValueAt(j, 12));                        // Dias de facturación optimizada
               // ....................................................................................
               
              
               celda = fila.createCell(i);  celda.setCellValue(col1);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col2);      i++;        //   
               celda = fila.createCell(i);  celda.setCellValue(col3);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col4);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col5);      i++;        // 
               
               celda = fila.createCell(i);  celda.setCellValue(col10);     i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col11);     i++;        // 
               
               celda = fila.createCell(i);  celda.setCellValue(col6);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col7);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col8);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col9);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col12);      i++;        //
            }

            try
            {
                String nombre="";
                JFileChooser file=new JFileChooser();
                file.showSaveDialog(this);
                File guarda =file.getSelectedFile();

                if(guarda !=null)
                {
                     nombre=file.getSelectedFile().getName();
                    //guardamos el archivo y le damos el formato directamente,
                    // si queremos que se guarde en formato doc lo definimos como .doc

                     FileOutputStream elFichero = new FileOutputStream(guarda+".xls");
                     libro.write(elFichero);
                     elFichero.close();


                     JOptionPane.showMessageDialog(null,
                     "El archivo ANEXO RESUMEN se a guardado Exitosamente",
                     "Información",JOptionPane.INFORMATION_MESSAGE);
                }
             }
             catch(IOException ex)
             {
                      JOptionPane.showMessageDialog(null,
                      "Su archivo ANEXO RESUMEN no se ha guardado",
                      "Advertencia",JOptionPane.WARNING_MESSAGE);
             }

            // Se salva el libro.
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
      }
          
       }
    // -----------------------------------------------------------------------------------------------------------
       public void GenerarExelAnexoResumenDetalle(){
           
        String col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14,col15,col16,col17,col18,col19,col20 ;
        String col21,col22,col23,col24,col25,col26,col27,col28,col29,col30 ;
        String col31,col32,col33,col34,col35 ;
        
        int i,j,nR;
       
        System.out.println("APUNTO DE GENERAR EXEL DETALLE ANEXO");
        
        int resp=JOptionPane.showConfirmDialog(null,"¿Crear EXCEL  de ANEXO DETALLE : ?");
        
        
        if (JOptionPane.OK_OPTION == resp){
        

                    nR  = this.nPuntosCalAhorroDetalle ;        System.out.println("Generando "+nR+" filas de anexo de detalle en exel");
                   

            HSSFWorkbook libro = new HSSFWorkbook();        
            HSSFSheet hoja = libro.createSheet("DETALLE");
            Row fila = hoja.createRow(0);        
            Cell celda;


            String[] titulos = { "NOMBRE","DIRECCIÓN TIENDA","LOCALIDAD","CUPS","TARIFA","FECHA INI", "FECHA FIN", "DIAS",
                                 "DIAS DESDE LA OPTIMIZACION","AHORRO CONSEGUIDO","AHORRO TOTAL", "E. P1 kWh", "E. P2 kWh", "E. P3 kWh",
                                 "E. Peaje kWh","P.Act. P1 €/kWh","P.Act. P2 €/kWh","P.Act. P3 €/kWh","P.Act. E peaje €/kWh",
                                 "P.Sim. P1 €/kWh","P.Sim. P2 €/kWh","P.Sim. P3 €/kWh","P.Sim. E peaje €/kWh","Act. P1 kW","Sim. P1 kW",
                                 "P.Act. P1 €/kW·dia","P.Sim. P1 €/kW·dia","Act. P2 kW","Sim. P2 kW","P.Act. P2 €/kW·dia","P.Sim. P2 €/kW·dia",
                                 "Act. P3 kW","Sim. P3 kW","P.Act. P3 €/kW·dia","P.Sim. P3 €/kW·dia"};                                          // 35 CAMPOS
 
            NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
            NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
            
            // Creamos el encabezado

            for (i = 0; i < titulos.length; i++) {
                  celda = fila.createCell(i);
                  celda.setCellValue(titulos[i]);
            }
            for (j=0; j<nR  ; j++) {

               // Nueva fila 
               i = 0 ;
               fila = hoja.createRow(j+1);
               System.out.println("Inserto celdas Excel en fila  ="+j);
               
               col1 = (String) miTabla04.getValueAt(j, 0) ;                              //NOMBRE
               col2 = (String) miTabla04.getValueAt(j, 1) ;                              // DIRECCIÓN TIENDA
               col3 = (String) miTabla04.getValueAt(j, 2);                               // LOCALIDAD
               col4 = (String) miTabla04.getValueAt(j, 3);                               // CUPS
               col5 = (String) miTabla04.getValueAt(j, 4);                               // TARIFA
               col6 = (String) miTabla04.getValueAt(j, 5);                               // FECHA INI
               col7 = (String) miTabla04.getValueAt(j, 6);                               // FECHA FIN               
               col8 = String.valueOf(miTabla04.getValueAt(j, 7));                        // DIAS  
               col9 = String.valueOf(miTabla04.getValueAt(j, 8));                        // DIAS DESDE OPTIMIZACIÓN  
               
               System.out.println(j+"- getValueAt(j, 9)="+miTabla04.getValueAt(j, 9));
               System.out.println(j+"- getValueAt(j, 10)="+miTabla04.getValueAt(j, 10));
               System.out.println(j+"- getValueAt(j, 11)="+miTabla04.getValueAt(j, 11));
               
               col10= formatoNumero.format(miTabla04.getValueAt(j, 9));                       // AHORRO CONSEGUIDO
               col11= formatoNumero.format(miTabla04.getValueAt(j, 10));                       // AHORRO TOTAL
              
             
               col12= (String) (miTabla04.getValueAt(j, 11));                       // E. P1 Kwh
               col13= (String) (miTabla04.getValueAt(j, 12));                       // E. P2 Kwh
               col14= (String) (miTabla04.getValueAt(j, 13));                       // E. P3 Kwh
                
               col15= (String) (miTabla04.getValueAt(j, 14));                       // E. Peaje kWh
               col16= (String) (miTabla04.getValueAt(j, 15));                       // P.Act. P1 €/kWh
               col17= (String) (miTabla04.getValueAt(j, 16));                       // P.Act. P2 €/kWh
               col18= (String) (miTabla04.getValueAt(j, 17));                       // P.Act. P3 €/kWh
                
               col19= (String) (miTabla04.getValueAt(j, 18));                       // P.Act. E peaje €/kWh
               col20= (String) (miTabla04.getValueAt(j, 19));                       // P.Sim. P1 €/kWh
               col21= (String) (miTabla04.getValueAt(j, 20));                       // P.Sim. P2 €/kWh
               col22= (String) (miTabla04.getValueAt(j, 21));                       // P.Sim. P3 €/kWh
               col23= (String) (miTabla04.getValueAt(j, 22));                       // P.Sim. E peaje €/kWh
               col24= (String) (miTabla04.getValueAt(j, 23));                       // Act. P1 kW
               col25= (String) (miTabla04.getValueAt(j, 24));                       // Sim. P1 kW
               col26= (String) (miTabla04.getValueAt(j, 25));                       // P.Act. P1 €/kW·dia
               col27= (String) (miTabla04.getValueAt(j, 26));                       // P.Sim. P1 €/kW·dia
               col28= (String) (miTabla04.getValueAt(j, 27));                       // Act. P2 kW
               col29= (String) (miTabla04.getValueAt(j, 28));                       // Sim. P2 kW
               col30= (String) (miTabla04.getValueAt(j, 29));                       // P.Act. P2 €/kW·dia
               col31= (String) (miTabla04.getValueAt(j, 30));                       // P.Sim. P2 €/kW·dia
               col32= (String) (miTabla04.getValueAt(j, 31));                       // Act. P3 kW
               col33= (String) (miTabla04.getValueAt(j, 32));                       // Sim. P3 kW
               col34= (String) (miTabla04.getValueAt(j, 33));                       // P.Act. P3 €/kW·dia
               col35= (String) (miTabla04.getValueAt(j, 34));                       // P.Sim. P3 €/kW·dia
              
               // ....................................................................................
               
               celda = fila.createCell(i);  celda.setCellValue(col1);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col2);      i++;        //   
               celda = fila.createCell(i);  celda.setCellValue(col3);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col4);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col5);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col6);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col7);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col8);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col9);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col10);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col11);     i++;        //
               
               celda = fila.createCell(i);  celda.setCellValue(col12);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col13);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col14);     i++;        //
               
               
               celda = fila.createCell(i);  celda.setCellValue(col15);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col16);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col17);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col18);     i++;        //
               
               celda = fila.createCell(i);  celda.setCellValue(col19);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col20);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col21);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col22);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col23);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col24);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col25);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col26);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col27);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col28);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col29);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col30);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col31);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col32);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col33);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col34);     i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col35);     i++;        //
               
            }

            try
            {
                String nombre="";
                JFileChooser file=new JFileChooser();
                file.showSaveDialog(this);
                File guarda =file.getSelectedFile();

                if(guarda !=null)
                {
                     nombre=file.getSelectedFile().getName();
                    //guardamos el archivo y le damos el formato directamente,
                    // si queremos que se guarde en formato doc lo definimos como .doc

                     FileOutputStream elFichero = new FileOutputStream(guarda+".xls");
                     libro.write(elFichero);
                     elFichero.close();


                     JOptionPane.showMessageDialog(null,
                     "El archivo ANEXO DETALLE se a guardado Exitosamente",
                     "Información",JOptionPane.INFORMATION_MESSAGE);
                }
             }
             catch(IOException ex)
             {
                      JOptionPane.showMessageDialog(null,
                      "Su archivo ANEXO DETALLE no se ha guardado",
                      "Advertencia",JOptionPane.WARNING_MESSAGE);
             }

            // Se salva el libro.
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
      }
          
       }
// ------------------------------------------------------------------------------------------------------------------------------------
public void anularLineaAhorro() {
    // ---------------------------------------------------------------------------------   
    String sqlStr;
    int estadoInsert=1;
    int resp=JOptionPane.showConfirmDialog(null,"¿ESTÁS SEGURO DE QUERE ANULAR LA LÍNEA DE CÁLCULO DE AHORRO SELECCIONADA?");
        
     saepDao misaepDaoB = new saepDao();
       
    
        if (JOptionPane.OK_OPTION == resp){
          
                    sqlStr  = "UPDATE t_ahorros_historico SET id_cliente=0 WHERE id_cliente="+this.id_cliente_actual ;
                    sqlStr += " AND id="+this.lAhorrosHistorico[this.indiceCalculo][0] ;
                    
                 
                    System.out.println(sqlStr);
                    estadoInsert= misaepDaoB.registrarFila(sqlStr);
                    
                    if (estadoInsert==0) {
                        
                        JOptionPane.showMessageDialog(null,
                        "EL REGISTRO SE HA ANULADO CORRECTAMENTE",
                        "Información",JOptionPane.INFORMATION_MESSAGE);
                    }
            
        }
        
        
   }
 // ------------------------------------------------------------------------------------------------------------------------
 private void actualizarTablaHistoricoAhorroCliente(int id_cliente) {
     
               
                DefaultTableModel model2;
		model2 = new DefaultTableModel();        // definimos el objeto tableModel
               
		miTabla02 = new JTable();                // creamos la instancia de la tabla
		miTabla02.setModel(model2);
                
                model2.addColumn("id"); 
                model2.addColumn("Fecha"); 
                model2.addColumn("nombre"); 
                model2.addColumn("cups"); 
                model2.addColumn("t. actual");
                model2.addColumn("mercado");
                model2.addColumn("direccion"); 
                model2.addColumn("provincia"); 
                model2.addColumn("cif");                 
                model2.addColumn("D.Fct.Opt.");
		model2.addColumn("A. Conseguido €");
		model2.addColumn("A. Total €");
                model2.addColumn("Coste Total €");
                model2.addColumn("Coste Simulado €");
                model2.addColumn("% Ahorro");
                
                miTabla02.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		miTabla02.getTableHeader().setReorderingAllowed(false);
                
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                miTabla02.setDefaultRenderer(Double.class, formato); 
                miTabla02.setDefaultRenderer(String.class, formato); 
                miTabla02.setDefaultRenderer(Integer.class, formato);
                miTabla02.setDefaultRenderer(Object.class, formato);
		
               saepDao misaepDao2 = new saepDao();
                
                
		misaepDao2.consultaHistoricoAhorrosCliente(model2,id_cliente);
                this.nPuntosCalAhorroDetalle = misaepDao2.nPuntosCalAhorroDetalle ;
                
                
                miBarra02.setViewportView(miTabla02);
                
             
             
                
 }    
 // ---------------------------------------------------------------------------------
   public void generarExcelHistoricoCalculos() {
       String col0,col1,col2,col3,col4,col5,col6,col7,col8,col9,col10,col11,col12,col13,col14 ;
       
        int i,j,nR;
       
        System.out.println("APUNTO DE GENERAR EXEL RESUMEN ANEXO");
        
        int resp=JOptionPane.showConfirmDialog(null,"¿Crear EXEL  de ANEXO DETALLES CÁLCULO : ?");
        
        
        if (JOptionPane.OK_OPTION == resp){
        

                    nR  = this.nPuntosCalAhorroDetalle ;
                   

            HSSFWorkbook libro = new HSSFWorkbook();        
            HSSFSheet hoja = libro.createSheet("DETALLES");
            Row fila = hoja.createRow(0);        
            Cell celda;


            String[] titulos = { "FECHA","NOMBRE","CUPS","TARIFA","MERCADO","DIRECCIÓN TIENDA","LOCALIDAD","CIF","DIAS DESDE OPTIMIZACIÓN",
                                 "AHORRO CONSEGUIDO","AHORRO TOTAL","COSTE TOTAL","COSTE SIM","% AHORRO" };                                      // 14 CAMPOS

            NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();
            NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();
            NumberFormat formatoNumero = NumberFormat.getNumberInstance();
            formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
                
     
            
           
            // Creamos el encabezado

            for (i = 0; i < titulos.length; i++) {
                  celda = fila.createCell(i);
                  celda.setCellValue(titulos[i]);
            }
 

            for (j=0; j<this.nPuntosCalAhorroDetalle; j++) {

               // Nueva fila 
               i = 0 ;
               fila = hoja.createRow(j+1);

               System.out.println("Inserto celdas Exel en fila  ="+j);

               col0 = (String) miTabla02.getValueAt(j, 1) ;                              // FECHA
               col1 = (String) miTabla02.getValueAt(j, 2) ;                              // NOMBRE
               col2 = (String) miTabla02.getValueAt(j, 3) ;                              // CUPS 
               col3 = (String) miTabla02.getValueAt(j, 4);                               // TARIFA LOCALIDAD
               col4 = (String) miTabla02.getValueAt(j, 5);                               // MERCADO
               col5 = (String) miTabla02.getValueAt(j, 6);                               // DIRECCIÓN TIENDA
               col6 = (String) miTabla02.getValueAt(j, 7);                               // LOCALIDAD
               col7 = (String) miTabla02.getValueAt(j, 8);                               // CIF
               
               
               col8 = String.valueOf(miTabla02.getValueAt(j, 9));                        // DIAS DESDE OPTIMIZACIÓN       
                                                                                       
               col9 = String.valueOf(miTabla02.getValueAt(j, 10));                        //AHORRO CONSEGUIDO
                                                                            
               col10 = String.valueOf(miTabla02.getValueAt(j, 11));                      //AHORRO TOTAL
               col11 = String.valueOf(miTabla02.getValueAt(j, 12));                       // COSTE TOTAL
               col12 = String.valueOf(miTabla02.getValueAt(j, 13));                       // COSTE SIM
               col13 = String.valueOf(miTabla02.getValueAt(j, 14));                       // Porcentaje ahorro
               
               // ....................................................................................
               
               celda = fila.createCell(i);  celda.setCellValue(col1);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col2);      i++;        //   
               celda = fila.createCell(i);  celda.setCellValue(col3);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col4);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col5);      i++;        // 
               celda = fila.createCell(i);  celda.setCellValue(col6);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col7);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col8);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col9);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col9);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col10);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col11);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col12);      i++;        //
               celda = fila.createCell(i);  celda.setCellValue(col13);      i++;        //
              
            }

            try
            {
                String nombre="";
                JFileChooser file=new JFileChooser();
                file.showSaveDialog(this);
                File guarda =file.getSelectedFile();

                if(guarda !=null)
                {
                     nombre=file.getSelectedFile().getName();
                    //guardamos el archivo y le damos el formato directamente,
                    // si queremos que se guarde en formato doc lo definimos como .doc

                     FileOutputStream elFichero = new FileOutputStream(guarda+".xls");
                     libro.write(elFichero);
                     elFichero.close();


                     JOptionPane.showMessageDialog(null,
                     "El archivo ANEXO DETALLE se a guardado Exitosamente",
                     "Información",JOptionPane.INFORMATION_MESSAGE);
                }
             }
             catch(IOException ex)
             {
                      JOptionPane.showMessageDialog(null,
                      "Su archivo ANEXO DETALLE no se ha guardado",
                      "Advertencia",JOptionPane.WARNING_MESSAGE);
             }

            // Se salva el libro.
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
       
   }
           
   } 
// ------------------------------------------------------------------------------------------------------------
   public void  cargarDatosCliente() throws SQLException {
         int i,j;
            
         saepDao misaepDao1 = new saepDao();
         
         this.id_cliente_actual = this.id_cliente_general ;
	// .......................................................          
         misaepDao1.cargarDatosCliente(this.id_cliente_actual);                      // Hay que poner el id del cliente está 1 de prueba
         
         this.nPuntos           = misaepDao1.nPuntos;
         this.listaPuntosSum    = misaepDao1.datosPuntoSum ;
        // .......................................................  
        misaepDao1.cargarContratosPuntos(this.listaPuntosSum,this.nPuntos,this.id_cliente_actual,1);
       
        this.listaContratosPuntos = misaepDao1.contratosPuntos ;
        // ....................................................... 
        misaepDao1.cargarCondicionesSimulacion(this.listaContratosPuntos,this.nPuntos,this.id_cliente_actual,1);
       
        this.lCondicionesSimulacion = misaepDao1.condicionesSimulacion ;
        
        saepDao misaepDao2 = new saepDao();
        
        // ....................................................... 
        
        misaepDao2.cargarContratosPuntos(this.listaPuntosSum,this.nPuntos,this.id_cliente_actual,2);
       
        this.listaContratosPuntosAct = misaepDao2.contratosPuntos ;
        
         // ....................................................... 
        
        misaepDao2.cargarCondicionesSimulacion(this.listaContratosPuntosAct,this.nPuntos,this.id_cliente_actual,2);
       
        this.lCondicionesActuales = misaepDao2.condicionesSimulacion ;
        
        // ....................................................... 
        actualizarTablaHistoricoAhorroCliente(this.id_cliente_actual);
        
        
   }
   // ----------------------------------------------------------------------------------------------------------------
       public final void modificarArbolNuevos() {
          
           int i,j,k,cnt,nCUPS,dia=1,ndia=0,cdia=1,ind=0;
           String fecha ="",contrato="";
           String nombre = "";
           String sdia="",CUPS,str="";
           
           
           
           cnt = this.nPuntos ;
                      
           System.out.println("Voy a modificar el arbol nuevo tenemos un total de puntos de:"+cnt);
           
           DefaultMutableTreeNode carpetaRaiz = new DefaultMutableTreeNode("PUNTOS SUMINISTRO");
           /**Definimos el modelo donde se agregaran los nodos*/
           DefaultTreeModel modelo2;
           modelo2 = new DefaultTreeModel(carpetaRaiz);
           /**agregamos el modelo al arbol, donde previamente establecimos la raiz*/
           
           arbol = new JTree(modelo2);
           jScrollPane2.setViewportView(arbol);
           
           DefaultMutableTreeNode carpeta = new DefaultMutableTreeNode("CARGADOS ("+this.nPuntos+")");     // Comenzamos con el primer punto
           modelo2.insertNodeInto(carpeta, carpetaRaiz, 0);
           
          
            for (i=0; i<this.nPuntos; i++){
               CUPS = this.listaPuntosSum[i][2] ;                                                           // Insertamos primero el cups
               CUPS = CUPS.trim();
               nCUPS = CUPS.length();
               
               if ( nCUPS>0){
                    DefaultMutableTreeNode archivo = new DefaultMutableTreeNode(i+" "+this.listaPuntosSum[i][2]);
                    modelo2.insertNodeInto(archivo, carpeta, i);       
               } else {                                                 
                  
                   
                        DefaultMutableTreeNode archivo = new DefaultMutableTreeNode(i+" -");
                        modelo2.insertNodeInto(archivo, carpeta, i); 
                                 
              }
           }    
            // ................................................................................
           
            arbol.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
           
            public void valueChanged(TreeSelectionEvent e) {
                // se obtiene el nodo seleccionado
                DefaultMutableTreeNode nseleccionado = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
    
                int nivel = nseleccionado.getDepth() ;
                System.out.println("El nivel de campo es ="+nivel);
                
                if ( nivel == 0) {
                    
                    String nodo         = nseleccionado.getUserObject().toString() ;
                    String [] campos    = nodo.split("\\s+");
                    int indice          = Integer.parseInt(campos[0]);
                    
                    System.out.println("El indice de campo es ="+indice);
                    actualizarFormularios(indice);
                    
                    modificarArbolCálculos();
                    
                }
            }
            }); 
            
           
            
           // ................................................................................
            
            
       }
 // ----------------------------------------------------------------------------------------------------------------
       public final void modificarArbolCálculos() {
          
           int i,j,k,cnt,nCUPS,dia=1,ndia=0,cdia=1,ind=0;
           String fecha ="",contrato="";
           String nombre = "";
           String sAhorro="",str="";
           
           NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

           formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));
           
           // .............................................
           
           cnt = this.nCalculosPunto;
           
           // .............................................
         
           
           if ( cnt>0) {
                      
           System.out.println("Voy a modificar el arbol de cálculos tenemos un total de puntos de:"+cnt);
           
           DefaultMutableTreeNode carpetaRaiz = new DefaultMutableTreeNode("CÁLCULOS ("+this.listaPuntosSum[this.indGen][2]+")");
           /**Definimos el modelo donde se agregaran los nodos*/
           DefaultTreeModel modelo3;
           modelo3 = new DefaultTreeModel(carpetaRaiz);
           /**agregamos el modelo al arbol, donde previamente establecimos la raiz*/
           
           arbol02 = new JTree(modelo3);
           jScrollPane8.setViewportView(arbol02);
           
           i = 0 ;
          
            for (i=0; i<cnt; i++){
               fecha = this.lAhorrosHistorico[i][3] ;                                                           // Insertamos primero el cups
               sAhorro = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[i][5]));
               
                DefaultMutableTreeNode carpeta = new DefaultMutableTreeNode(i+" C:"+this.lAhorrosHistorico[i][3]+" - Ahorro:"+sAhorro);
                modelo3.insertNodeInto(carpeta, carpetaRaiz, i);     
             
           }    
            // ................................................................................
           
            arbol02.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
           
            public void valueChanged(TreeSelectionEvent e) {
                // se obtiene el nodo seleccionado
                DefaultMutableTreeNode nseleccionado = (DefaultMutableTreeNode) arbol02.getLastSelectedPathComponent();
    
                int nivel = nseleccionado.getDepth() ;
                System.out.println("El nivel de calculo es ="+nivel);
                
                if ( nivel == 0) {
                    
                    String nodo         = nseleccionado.getUserObject().toString() ;
                    String [] campos    = nodo.split("\\s+");
                    int indice          = Integer.parseInt(campos[0]);
                    
                    System.out.println("El indice de calculo es ="+indice);
                    try {
                        actualizarPanelConcionesActuales(indice);
                        actualizarPanelConcionesSimulacion(indice);
                        actualizarPanelConcionesFactura(indice);
                        actualizarPanelConcionesCalculo(indice);
                        
                    } catch (SQLException ex) {
                   //     Logger.getLogger(FramePrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                    
                }
            }
            }); 
            
           }
            
       }       
      public void actualizarPanelConcionesActuales(int indice) throws SQLException{
        
        int idCA, idTCA;
        
        idCA  = Integer.parseInt(lAhorrosHistorico[indice][11]) ;               // Indice de la tabla de condiciones actuales para ese cálculo
        idTCA = Integer.parseInt(lAhorrosHistorico[indice][13]) ;               // Tipo de contrato condiciones actuales para ese calculo
        
        if (idTCA>0 && idTCA <11)    jTextField56.setText(this.lTiposTarifas[idTCA]); else jTextField56.setText("");
        
        
        DefaultTableModel model10;
	model10 = new DefaultTableModel();                                      // definimos el objeto tableModel
               
		tablaC01 = new JTable();                                       // creamos la instancia de la tabla
		tablaC01.setModel(model10);
                 
                model10.addColumn("DATO"); 
                model10.addColumn("VALOR"); 
                
              
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                tablaC01.setDefaultRenderer(Double.class, formato); 
                tablaC01.setDefaultRenderer(String.class, formato); 
                tablaC01.setDefaultRenderer(Integer.class, formato);
                tablaC01.setDefaultRenderer(Object.class, formato);
		
                saepDao misaepDao = new saepDao();
                
		misaepDao.consultaDatosCondiciones(model10,idCA,idTCA);         System.out.println("ConsultaDatosCondiciones (idCA,idTCA)=("+idCA+","+idTCA+")");
              
                jScrollPane10.setViewportView(tablaC01);
                
                
    }
    // ------------------------------------------------------------------------------------------------------------------------------------------
     public void actualizarPanelConcionesSimulacion(int indice) throws SQLException{
        
        int idCA, idTCA;
        
        idCA  = Integer.parseInt(lAhorrosHistorico[indice][12]) ;               // Indice de la tabla de condiciones simulacion para ese cálculo
        idTCA = Integer.parseInt(lAhorrosHistorico[indice][14]) ;               // Tipo de contrato condiciones simulacion para ese calculo
         System.out.println("ConsultaDatosCondiciones Simulacion (idCA,idTCA)=("+idCA+","+idTCA+")");
        
         if (idTCA>0 && idTCA <11)    {jTextField57.setText(this.lTiposTarifas[idTCA]); System.out.println("ECHO");} else jTextField57.setText("");
        
         
        DefaultTableModel model11;
	model11 = new DefaultTableModel();                                      // definimos el objeto tableModel
               
		tablaC02 = new JTable();                                       // creamos la instancia de la tabla
		tablaC02.setModel(model11);
                 
                model11.addColumn("DATO"); 
                model11.addColumn("VALOR"); 
                
              
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                tablaC02.setDefaultRenderer(Double.class, formato); 
                tablaC02.setDefaultRenderer(String.class, formato); 
                tablaC02.setDefaultRenderer(Integer.class, formato);
                tablaC02.setDefaultRenderer(Object.class, formato);
		
                saepDao misaepDao = new saepDao();
                
		misaepDao.consultaDatosCondiciones(model11,idCA,idTCA);
              
                jScrollPane11.setViewportView(tablaC02);
                
                // .............................................            Pasamos datos por referencia a la ventana de generación de factura virtual
                          
                this.misDatos.pe1    = misaepDao.pe1 ;
                this.misDatos.pe2    = misaepDao.pe2 ;
                this.misDatos.pe3    = misaepDao.pe3 ;
                this.misDatos.pep    = misaepDao.pep ;
                
                this.misDatos.ppp    = misaepDao.ppp ;
                this.misDatos.ppll   = misaepDao.ppll ;
                this.misDatos.ppv    = misaepDao.ppv ;
                
                this.misDatos.pcp   = misaepDao.pcp ;
                this.misDatos.pcll  = misaepDao.pcll;
                this.misDatos.pcv   = misaepDao.pcv ;
                
                this.misDatos.descuento   = misaepDao.descuento ;
                this.misDatos.psalquiler  = misaepDao.pAlq ;
                
                // .............................................  
    }
   // ------------------------------------------------------------------------------------------------------------------------------------------
     public void actualizarPanelConcionesFactura(int indice) throws SQLException{
        
        int idCA, idTCA;
        
        idCA  = Integer.parseInt(lAhorrosHistorico[indice][10]) ;               // Indice de la tabla de facturas actuales para ese cálculo
        idTCA = Integer.parseInt(lAhorrosHistorico[indice][13]) ;               // Tipo de facturas
        
     //    if (idTCA>0 && idTCA <11)    jTextField57.setText(this.lTiposTarifas[idTCA]); else jTextField57.setText("");
        
        
        DefaultTableModel model12;
	model12 = new DefaultTableModel();                                      // definimos el objeto tableModel
               
		tablaC03 = new JTable();                                       // creamos la instancia de la tabla
		tablaC03.setModel(model12);
                 
                model12.addColumn("DATO"); 
                model12.addColumn("VALOR"); 
                
              
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                tablaC03.setDefaultRenderer(Double.class, formato); 
                tablaC03.setDefaultRenderer(String.class, formato); 
                tablaC03.setDefaultRenderer(Integer.class, formato);
                tablaC03.setDefaultRenderer(Object.class, formato);
		
                saepDao misaepDao = new saepDao();
                
		misaepDao.consultaDatosFacturas(model12,idCA,idTCA);
              
                jScrollPane6.setViewportView(tablaC03);
                
                // .............................................            Pasamos datos por referencia a la ventana de generación de factura virtual
                this.misDatos.sFecha1 = misaepDao.fecha_inicio;
                this.misDatos.sFecha2 = misaepDao.fecha_fin;
                
                this.misDatos.dias  = diferenciaFechas(this.misDatos.sFecha1,this.misDatos.sFecha2,1);
                
                System.out.println("He calculado dias="+this.misDatos.dias);
                
                this.misDatos.e1        = misaepDao.e1 ;
                this.misDatos.e2        = misaepDao.e2 ;
                this.misDatos.e3        = misaepDao.e3 ;
                this.misDatos.ep        = misaepDao.ep ;
                
                this.misDatos.pp        = misaepDao.pF1 ;
                this.misDatos.pll       = misaepDao.pF2 ;
                this.misDatos.pv        = misaepDao.pF3 ;
                               
                this.misDatos.Rp1       = misaepDao.r1 ;
                this.misDatos.Rp2       = misaepDao.r2 ;
                this.misDatos.Rp3       = misaepDao.r3 ;
                this.misDatos.TR        = misaepDao.TR ;
                
                this.misDatos.rF1        = misaepDao.rF1 ;
                this.misDatos.rF2        = misaepDao.rF2 ;
                
                this.misDatos.alquiler   = misaepDao.alquiler ;
                this.misDatos.descuento  = misaepDao.descuento ;
                
                this.misDatos.rs1        = misaepDao.rs1 ;
                this.misDatos.rs2        = misaepDao.rs2 ;
                
                this.misDatos.TR        = misaepDao.TR ;
                
                this.misDatos.e1s        = misaepDao.e1s ;
                this.misDatos.e2s        = misaepDao.e2s ;
                this.misDatos.e3s        = misaepDao.e3s ;
                
                // .............................................
                
                        
                
                
                
    }
     
     // ------------------------------------------------------------------------------------------------------------------------------------------
     public void actualizarPanelConcionesCalculo(int indice) throws SQLException{
        
        int idCA, idTCA;
        
        idTCA = Integer.parseInt(lAhorrosHistorico[indice][13]) ;               // Tipo de facturas
        this.indiceCalculo = indice ;
        System.out.println("actualizarPanelConcionesCalculo("+indice+") idTCA ="+idTCA );
        NumberFormat formatoImporte = NumberFormat.getCurrencyInstance();

        formatoImporte = NumberFormat.getCurrencyInstance(new Locale("es","ES"));

        NumberFormat formatoPorcentaje = NumberFormat.getPercentInstance();

        NumberFormat formatoNumero = NumberFormat.getNumberInstance();

        formatoNumero.setMaximumFractionDigits(2);
        
        DefaultTableModel model13;
	model13 = new DefaultTableModel();                                      // definimos el objeto tableModel
               
		tablaC04 = new JTable();                                       // creamos la instancia de la tabla
		tablaC04.setModel(model13);
                 
                model13.addColumn("DATO"); 
                model13.addColumn("VALOR"); 
                
              
                //Nueva instancia de la clase que contiene el formato
                FormatoTablaPuntos formato = new FormatoTablaPuntos();
                          
                //Se obtiene la tabla y se establece el formato para cada tipo de dato
                
                tablaC04.setDefaultRenderer(Double.class, formato); 
                tablaC04.setDefaultRenderer(String.class, formato); 
                tablaC04.setDefaultRenderer(Integer.class, formato);
                tablaC04.setDefaultRenderer(Object.class, formato);
		
                 // .......................................................... 
                        
                 Object[] fila = new Object[2];
                 
                  
                 switch (idTCA) {
                                case 1:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                    
                                break;
                                case 2:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;                         fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                    
                                break;
                                case 3:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;                         fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                       
                                break;
                                case 4:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;                         fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                       
                                break;
                                case 5:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                        
                                break;
                                 case 6:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                        
                                break;
                                case 8:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                        
                                break;
                                case 9:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                        
                                break;
                                case 10:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                        
                                break;
                                case 11:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                    
                                break;
                                case 12:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                    
                                break;
                                case 13:
                                    fila[0] = "Id" ;                            fila[1] = this.lAhorrosHistorico[indice][0];
                                    model13.addRow(fila);
                                    fila[0] = "Fecha de cálculo" ;              fila[1] = this.lAhorrosHistorico[indice][3]; 
                                    model13.addRow(fila);
                                    fila[0] = "Dias de Facturación Optimizada"; fila[1] = this.lAhorrosHistorico[indice][4];  
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro Conseguido" ;             fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][5]));       
                                    model13.addRow(fila);
                                    fila[0] = "Ahorro total" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][6]));          
                                    model13.addRow(fila);
                                    fila[0] = "Coste Actual" ;                  fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][7]));              
                                    model13.addRow(fila);
                                    fila[0] = "Coste Simulado" ;                fila[1] = formatoImporte.format(Double.parseDouble(this.lAhorrosHistorico[indice][8]));    
                                    model13.addRow(fila);
                                    fila[0] = "Porcentaje de ahorro" ;          fila[1] = formatoPorcentaje.format(Double.parseDouble(this.lAhorrosHistorico[indice][9])); 
                                    model13.addRow(fila);
                                        
                                break;
                        }
                        // .......................................................... 
              
                jScrollPane7.setViewportView(tablaC04);
                
    }      
// ---------------------------------------------------------------------------------
   public void actualizarFechaUltimoCalculo(int id_cliente)
    {
        
      saepDao misaepDao = new saepDao();  
      misaepDao.FechaultimoCalculo( id_cliente);
      this.FechaUltimoCalculo = misaepDao.fechaUltimoCalculo ;
      
      System.out.println("La fecha del ultimo cálculo para id_cliente="+id_cliente+" es="+this.FechaUltimoCalculo );
   }
}
