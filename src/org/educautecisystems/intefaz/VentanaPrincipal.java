/*
 *  VentanaPrincipal.java
 *  Copyright (C) 2012  Guillermo Pazos shadowguiller@hotmail.com>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.educautecisystems.intefaz;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

/**
 *
 * @author Shadow2012
 */
public class VentanaPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        MenuDoce.setVisible(false);
        MenuAdmi.setVisible(false);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public void MostrarAdministrador()
    {
        MenuAdmi.setVisible(true);
        MenuEstu.setVisible(false);
        MenuItemIngreso.setEnabled(false);
    }
    
    public void MostrarDocente()
    {
        MenuDoce.setVisible(true);
        MenuEstu.setVisible(false);
        MenuItemIngreso.setEnabled(false);
    }
    
    public void insertarNuevaVentana( JInternalFrame jInternalFrame ) {
        escritorioPrincipal.add(jInternalFrame);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        escritorioPrincipal = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        MenuItemArchivo = new javax.swing.JMenu();
        MenuItemIngreso = new javax.swing.JMenuItem();
        MenuItemSalir = new javax.swing.JMenuItem();
        MenuEstu = new javax.swing.JMenu();
        MenuItemChatEst = new javax.swing.JMenuItem();
        MenuDoce = new javax.swing.JMenu();
        MenuItemInSerCha = new javax.swing.JMenuItem();
        MenuItemChatDoc = new javax.swing.JMenuItem();
        MenuAdmi = new javax.swing.JMenu();
        MenuItemNuePer = new javax.swing.JMenuItem();
        MenManteni = new javax.swing.JMenu();
        MenManteAdmin = new javax.swing.JMenuItem();
        MenManteDoce = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        MenuItemIngFac = new javax.swing.JMenuItem();
        MenuItemIngMat = new javax.swing.JMenuItem();
        MenuAyuda = new javax.swing.JMenu();
        mnItemAyuda = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EducaUteciSystems");

        escritorioPrincipal.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        MenuItemArchivo.setText("Archivo");

        MenuItemIngreso.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        MenuItemIngreso.setText("Ingresar");
        MenuItemIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemIngresoActionPerformed(evt);
            }
        });
        MenuItemArchivo.add(MenuItemIngreso);

        MenuItemSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        MenuItemSalir.setText("Salir");
        MenuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemSalirActionPerformed(evt);
            }
        });
        MenuItemArchivo.add(MenuItemSalir);

        jMenuBar1.add(MenuItemArchivo);

        MenuEstu.setText("Estudiante");

        MenuItemChatEst.setText("Chat");
        MenuItemChatEst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemChatEstActionPerformed(evt);
            }
        });
        MenuEstu.add(MenuItemChatEst);

        jMenuBar1.add(MenuEstu);

        MenuDoce.setText("Docente");

        MenuItemInSerCha.setText("Iniciar Servidor Chat");
        MenuItemInSerCha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemInSerChaActionPerformed(evt);
            }
        });
        MenuDoce.add(MenuItemInSerCha);

        MenuItemChatDoc.setText("Chat");
        MenuItemChatDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemChatDocActionPerformed(evt);
            }
        });
        MenuDoce.add(MenuItemChatDoc);

        jMenuBar1.add(MenuDoce);

        MenuAdmi.setText("Administración");

        MenuItemNuePer.setText("Nuevo Personal");
        MenuItemNuePer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemNuePerActionPerformed(evt);
            }
        });
        MenuAdmi.add(MenuItemNuePer);

        MenManteni.setText("Mantenimiento");

        MenManteAdmin.setText("Administador");
        MenManteAdmin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenManteAdminActionPerformed(evt);
            }
        });
        MenManteni.add(MenManteAdmin);

        MenManteDoce.setText("Docente");
        MenManteDoce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenManteDoceActionPerformed(evt);
            }
        });
        MenManteni.add(MenManteDoce);

        MenuAdmi.add(MenManteni);

        jMenu1.setText("Ingreso Nuevos Datos");

        MenuItemIngFac.setText("Facultad");
        MenuItemIngFac.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemIngFacActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemIngFac);

        MenuItemIngMat.setText("Materia");
        MenuItemIngMat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemIngMatActionPerformed(evt);
            }
        });
        jMenu1.add(MenuItemIngMat);

        MenuAdmi.add(jMenu1);

        jMenuBar1.add(MenuAdmi);

        MenuAyuda.setText("Ayuda");

        mnItemAyuda.setText("Manual de Usuario");
        mnItemAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnItemAyudaActionPerformed(evt);
            }
        });
        MenuAyuda.add(mnItemAyuda);

        jMenuBar1.add(MenuAyuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorioPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorioPrincipal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
        );

        escritorioPrincipal.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuItemIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemIngresoActionPerformed
        Ingreso usuarios = new Ingreso(this);
        escritorioPrincipal.add(usuarios);
        usuarios.setVisible(true);
    }//GEN-LAST:event_MenuItemIngresoActionPerformed

    private void MenuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_MenuItemSalirActionPerformed

    private void MenuItemChatEstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemChatEstActionPerformed
        ChatOptions chatOptions = new ChatOptions(this, false);
        escritorioPrincipal.add(chatOptions);
        chatOptions.setVisible(true);
    }//GEN-LAST:event_MenuItemChatEstActionPerformed

    private void MenuItemNuePerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemNuePerActionPerformed
        CrearNuevoUsuario Est = new CrearNuevoUsuario();
        escritorioPrincipal.add(Est);
        Est.setVisible(true);
    }//GEN-LAST:event_MenuItemNuePerActionPerformed

    private void MenManteDoceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenManteDoceActionPerformed
        MantenimientoDocente Est = new MantenimientoDocente();
        escritorioPrincipal.add(Est);
        Est.setVisible(true);
    }//GEN-LAST:event_MenManteDoceActionPerformed

    private void MenuItemIngMatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemIngMatActionPerformed
        Ingreso_Materia Est = new Ingreso_Materia();
        escritorioPrincipal.add(Est);
        Est.setVisible(true);
    }//GEN-LAST:event_MenuItemIngMatActionPerformed

    private void MenuItemIngFacActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemIngFacActionPerformed
        Ingreso_Facultad Fac = new Ingreso_Facultad();
        escritorioPrincipal.add(Fac);
        Fac.setVisible(true);
    }//GEN-LAST:event_MenuItemIngFacActionPerformed

    private void MenuItemChatDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemChatDocActionPerformed
        ChatOptions chatOptions = new ChatOptions(this, true);
        escritorioPrincipal.add(chatOptions);
        chatOptions.setVisible(true);
    }//GEN-LAST:event_MenuItemChatDocActionPerformed

    private void MenuItemInSerChaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemInSerChaActionPerformed
        ChatServerInterface csi = new ChatServerInterface();
        escritorioPrincipal.add(csi);
        csi.setVisible(true);
    }//GEN-LAST:event_MenuItemInSerChaActionPerformed

    private void MenManteAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenManteAdminActionPerformed
        // TODO add your handling code here:
        MantenimientoAdministrador dea = new MantenimientoAdministrador();
        escritorioPrincipal.add(dea);
        dea.setVisible(true);
    }//GEN-LAST:event_MenManteAdminActionPerformed

    private void mnItemAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnItemAyudaActionPerformed
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec("explorer \"DiagramaCasosDeUso.pdf\"");
        } catch (IOException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnItemAyudaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MenManteAdmin;
    private javax.swing.JMenuItem MenManteDoce;
    private javax.swing.JMenu MenManteni;
    private javax.swing.JMenu MenuAdmi;
    private javax.swing.JMenu MenuAyuda;
    private javax.swing.JMenu MenuDoce;
    private javax.swing.JMenu MenuEstu;
    private javax.swing.JMenu MenuItemArchivo;
    private javax.swing.JMenuItem MenuItemChatDoc;
    private javax.swing.JMenuItem MenuItemChatEst;
    private javax.swing.JMenuItem MenuItemInSerCha;
    private javax.swing.JMenuItem MenuItemIngFac;
    private javax.swing.JMenuItem MenuItemIngMat;
    private javax.swing.JMenuItem MenuItemIngreso;
    private javax.swing.JMenuItem MenuItemNuePer;
    private javax.swing.JMenuItem MenuItemSalir;
    private javax.swing.JDesktopPane escritorioPrincipal;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem mnItemAyuda;
    // End of variables declaration//GEN-END:variables
}
