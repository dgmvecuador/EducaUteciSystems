/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.core;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import org.educautecisystems.Esquemas.Ingreso;
import org.educautecisystems.intefaz.VentanaPrincipal;

/**
 *
 * @author Shadow2012
 */
public class Sistema {
    private static EntityManagerFactory emf = null;
    
    /* Constantes */
    public static final int VERSION_MAYOR = 1;
    public static final int VERSION_MENOR = 0;
    public static final int VERSION_PARCHE = 0;
    public static final String NOMBRE_PROGRAMA = "EducaUteciSystems";
    
    public static String dameVersionCompleta () {
        return NOMBRE_PROGRAMA+"-"+VERSION_MAYOR+"."+VERSION_MENOR+"."+VERSION_PARCHE;
    }
    
    public static void main( String []args ) {
        String usuario = "root";
        String password = "admin";
        
        inicializarSistema( usuario,password );
        seleccionadoLookAndFeel();
        new VentanaPrincipal().setVisible(true);
    }
    
    private static void inicializarSistema( String usuario, String password ) {
        Map parametros = new HashMap();
        parametros.put("javax.persistence.jdbc.password", password);
        parametros.put("javax.persistence.jdbc.user", usuario);
        
        emf = Persistence.createEntityManagerFactory("EducaUteciSystemsPU", parametros);
    }
    
    public static void cerrarSistema() {
        System.out.println("Cerrando Sistema..");
        emf.close();
        System.exit(0);
    }

    /**
     * @return the emf
     */
    public static EntityManagerFactory getEmf() {
        return emf;
    }
    
    private static void seleccionadoLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ingreso.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    public static void mostrarMensajeInformativo( String mensaje ) {
        JOptionPane.showMessageDialog(null, mensaje, "Información - "+dameVersionCompleta(), JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mostrarMensajeError( String mensaje ) {
        JOptionPane.showMessageDialog(null, mensaje, "Error - "+dameVersionCompleta(), JOptionPane.ERROR_MESSAGE);
    }
}
