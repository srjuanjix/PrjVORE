/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.myorg.informes;




/**
 *
 * @author acondaqua
 */
public class informeSimpleFactura {
    
    public int idTipo=0,dias=0 ;
    public String sFecha1,sFecha2 ;
    
    public double ie    = 0.051127 ;
    public double iva   = 0.21 ;
    
    public double pp=0,pll=0,pv=0;                                      // potencias facturadas 
    public double pcp=0,pcll=0,pcv=0;                                   // potencias contratadas  
    public double ppp=0,ppll=0,ppv=0;                                    // precio potencias por dia    
    public double e1=0,e2=0,e3=0,ep=0,etot=0,e1s=0,e2s=0,e3s=0;         // energias
    public double pe1=0,pe2=0,pe3=0,pep=0;                              // precio de energias    
    public double Rp1=0,Rp2=0,Rp3=0,TR=0;                               // Energía Reactiva    
    public double alquiler=0, descuento=0;
    public double psalquiler=0;
    
    public double sppp=0,sppll=0,sppv=0;        // precios potencias facturadas 
    public double spe1=0,spe2=0,spe3=0,spep=0;  // precios energias facturadas 
    
    public double rF1,rF2 ;                     // reactiva facturada
    public double pF1,pF2,pF3 ;                 // potencias facturadasa
    
    public double rTF1=0,rTF2=0 ;               // subtotales de reactiva
    public double rs1=0,rs2=0 ;                 // Reactiva simulada
   
         
    // -------------------------------------------------------------------------------------------------------
    public double calculaSubTotalPotencia() {
        double dPotencia=0;
       
     
       
        this.sppp  = this.ppp * this.pp * this.dias ;
        this.sppll = this.ppll * this.pll * this.dias ;
        this.sppv  = this.ppv * this.pv * this.dias ;
        
        dPotencia = this.sppp + this.sppll + this.sppv ;
        
         System.out.println("Subtotal potencia:"+dPotencia);
        
        return dPotencia;
    }
     // -------------------------------------------------------------------------------------------------------
    public double calculaSubTotalPotenciaT02() {
        double dPotencia=0;
       
        this.sppp  = this.ppp * this.pp * this.dias ;
        
        dPotencia = this.sppp  ;
        
        System.out.println("Subtotal potencia:"+dPotencia);
        
        return dPotencia;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaSubTotalEnergia() {
        double dEnergia=0;
        this.etot = 0 ;
       
        System.out.println("this.e1s="+this.e1s+" - y this.e1="+this.e1);
        
        if ( (this.e1s!=0 ) ){                                      // si son diferentes DE 0 tenemos energía simulada
           
            System.out.println("Tenemos energia simulada");
            
            this.spe1  = this.pe1 * this.e1s  ;
            this.spe2  = this.pe2 * this.e2s  ;
            this.spe3  = this.pe3 * this.e3s  ;

            this.etot = this.e1s+this.e2s+this.e3s ;
            this.ep   = this.etot ;

            this.spep  = this.pep * this.ep  ;

            dEnergia = this.spe1 + this.spe2 + this.spe3 + this.spep ;

            System.out.println("Subtotal energia:"+dEnergia);
            
        } else {
        
            this.spe1  = this.pe1 * this.e1  ;
            this.spe2  = this.pe2 * this.e2  ;
            this.spe3  = this.pe3 * this.e3  ;

            this.etot = this.e1+this.e2+this.e3 ;
            this.ep   = this.etot ;

            this.spep  = this.pep * this.ep  ;

            dEnergia = this.spe1 + this.spe2 + this.spe3 + this.spep ;

            System.out.println("Subtotal energia:"+dEnergia);
        }
        
        return dEnergia;
    }
     // -------------------------------------------------------------------------------------------------------
    public double calculaSubTotalEnergiaT02() {
        double dEnergia=0;
        this.etot = 0 ;
        
          if ( (this.e1s!=0 ) ){                                      // si son diferentes DE 0 tenemos energía simulada
                System.out.println("Tenemos energia simulada en T02");
           
                
                this.spe1  = this.pe1 * this.e1s  ;
                this.spe2  = this.pe1 * this.e2s  ;
                this.spe3  = this.pe1 * this.e3s ;

                this.etot = this.e1s+this.e2s+this.e3s ;
               // this.e1   = this.etot ;

                dEnergia = this.spe1 + this.spe2 + this.spe3  ;

                System.out.println("Subtotal energia:"+dEnergia);
                
                
          } else {
            
            this.spe1  = this.pe1 * this.e1  ;
            this.spe2  = this.pe1 * this.e2  ;
            this.spe3  = this.pe1 * this.e3  ;

            this.etot = this.e1+this.e2+this.e3 ;
           // this.e1   = this.etot ;

            dEnergia = this.spe1 + this.spe2 + this.spe3  ;

            System.out.println("Subtotal energia:"+dEnergia);
              
              
          }
        return dEnergia;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaSubTotalImpuesto(double potencia,double energia, double reactiva,double restaDescuento) {
        double dImpuesto=0; 
        
        dImpuesto = this.ie * (potencia+energia+reactiva-restaDescuento);
        
        return dImpuesto;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaTotalEnergia(double potencia,double energia, double reactiva,double dImpuesto,double descuento) {
        double dTotalEnergia=0; 
        
        dTotalEnergia = (potencia+energia+reactiva+dImpuesto-descuento);
        
        return dTotalEnergia;
    }
     // -------------------------------------------------------------------------------------------------------
    public double calculaImporteTotal(double subtotalEnergia,double servicios) {
        double dTotal=0; 
        
        dTotal = (subtotalEnergia+servicios);
        
        return dTotal;
    }
    
    // -------------------------------------------------------------------------------------------------------
    public double calculaImporteIVA(double importeTotal) {
        double dTotal=0; 
        
        dTotal = this.iva*(importeTotal);
        
        return dTotal;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaImporteTotalFactura(double importeTotal,double iva) {
        double dTotal=0; 
        
        dTotal = importeTotal+iva;
        
        return dTotal;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaEnergiaPeaje(double E1,double E2, double E3) {
        double dTotal=0; 
        
        dTotal = E1+E2+E3;
        
        return dTotal;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaPenalizacionReactiva(double r1,double r2, double precioR1, double precioR2) {
        double rt1=0,rt2=0,dTotal=0; 
        
        rt1 = r1 * precioR1 ; this.rTF1 = rt1 ; System.out.println("Penalizacion R1 ("+r1+","+precioR1+") ="+this.rTF1) ;
        rt2 = r2 * precioR2 ; this.rTF2 = rt2 ; System.out.println("Penalizacion R2 ("+r2+","+precioR2+")="+this.rTF2) ;
        dTotal = (rt1+rt2);                      System.out.println("Penalizacion Reactiva  ="+dTotal) ;
        
        return dTotal;
    }
    // -------------------------------------------------------------------------------------------------------
    public double calculaDescuento(double E1,double descuento) {
        double dTotal=0; 
        
        dTotal = (descuento/100)*(E1);
        
        return dTotal;
    }
    
    
}
