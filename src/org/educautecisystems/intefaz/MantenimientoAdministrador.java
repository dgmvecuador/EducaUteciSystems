/*
 *  ChatServerInterface.java
 *  Copyright (C) 2012  Guillermo Pazos <shadowguiller@gmail.com>
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

import java.util.List;
import org.educautecisystems.controladores.AdministradorJpaController;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.core.Sistema;
import org.educautecisystems.entidades.Administrador;
import org.educautecisystems.intefaz.mantenimiento.AdministradorMantenimiento;

/**
 *
 * @author Shadow2012
 */
public class MantenimientoAdministrador extends javax.swing.JInternalFrame {

    /**
     * Creates new form MantenimientoAdministrador
     */
    public MantenimientoAdministrador() {
        initComponents();
        AdministradorJpaController controladorAdministrador = new AdministradorJpaController(Sistema.getEmf());
        List <Administrador> administradores  = controladorAdministrador.findAdministradorEntities();
        
        for (Administrador administrador:administradores) {
            AdministradorMantenimiento administradorMantenimiento = new AdministradorMantenimiento(administrador);
            cmbAdministradores.addItem(administradorMantenimiento);
         }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBtModifi = new javax.swing.JButton();
        cmbAdministradores = new javax.swing.JComboBox();
        jButElimi = new javax.swing.JButton();
        jtextUsuario = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jBtuCanc = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jConfClave = new javax.swing.JPasswordField();
        jPasClave = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();

        setTitle("Mantenimiento Administrador");

        jBtModifi.setText("Modificar");
        jBtModifi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtModifiActionPerformed(evt);
            }
        });

        cmbAdministradores.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAdministradoresItemStateChanged(evt);
            }
        });

        jButElimi.setText("Eliminar");
        jButElimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButElimiActionPerformed(evt);
            }
        });

        jLabel3.setText("Contraseña ");

        jBtuCanc.setText("Cancelar");
        jBtuCanc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtuCancActionPerformed(evt);
            }
        });

        jLabel1.setText("<html><h2><b>Administradores</b></h2></html>");

        jLabel2.setText("Usuario");

        jLabel5.setText("Administradores Existentes");

        jLabel4.setText("Confirmación de Contraseña");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbAdministradores, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jBtModifi)
                                    .addGap(51, 51, 51)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jBtuCanc, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jButElimi, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel4)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPasClave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                                .addComponent(jtextUsuario, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jConfClave, javax.swing.GroupLayout.Alignment.LEADING)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtextUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbAdministradores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPasClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jConfClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtModifi)
                    .addComponent(jButElimi))
                .addGap(18, 18, 18)
                .addComponent(jBtuCanc)
                .addGap(49, 49, 49))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtModifiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtModifiActionPerformed
//        Es para que obligue al usuario ingresar todo en los campos
        if (jtextUsuario.getText().isEmpty() || new String(jPasClave.getPassword()).isEmpty() || new String(jConfClave.getPassword()).isEmpty()) {
            Sistema.mostrarMensajeError("Por favor llene todos los campos.");
            return;
        }
        /*  cambio de un elementio */
        if (!new String (jPasClave.getPassword()).equals(new String(jConfClave.getPassword()))) {
            Sistema.mostrarMensajeError("Las contraseña no conciden");
            return;
        }
        AdministradorJpaController controladorAdministrador = new AdministradorJpaController(Sistema.getEmf());
        AdministradorMantenimiento administradorMantenimiento = (AdministradorMantenimiento) cmbAdministradores.getSelectedItem();
//      if(jtextUsuario. != " " )
//      {
        Administrador modificador = administradorMantenimiento.getAdministrador();
        modificador.setUsuario(jtextUsuario.getText());
//      if( jPasClave. != " ")
//      {
        modificador.setContrasena(new String(jPasClave.getPassword()));
//      }    
//      }
        try {
            controladorAdministrador.edit(modificador);
              Sistema.mostrarMensajeInformativo("Se a modificado el administrador con exito");      
        } catch (NonexistentEntityException ex) {
            Sistema.mostrarMensajeError("No existe el administrador actual");
        } catch (Exception ex) {
            Sistema.mostrarMensajeError("Error al modificar el administrador");
        }
        this.dispose();
    }//GEN-LAST:event_jBtModifiActionPerformed

    private void jButElimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButElimiActionPerformed
        // TODO add your handling code here:
        AdministradorJpaController controladorAdministrador = new AdministradorJpaController(Sistema.getEmf());
        AdministradorMantenimiento actual = (AdministradorMantenimiento)cmbAdministradores.getSelectedItem();
        try {
            controladorAdministrador.destroy(actual.getAdministrador().getIdAdministrador());
            Sistema.mostrarMensajeInformativo("Se ha borrado safisfactoriamente el Administrador");
            this.setVisible(false);
            this.dispose();
          
        } catch (NonexistentEntityException ex) {
            Sistema.mostrarMensajeError("No se puede borrar el Administrador");
            return;
        }
        cmbAdministradores.removeAllItems();
        List <Administrador> administradores  = controladorAdministrador.findAdministradorEntities();

        for (Administrador administrador:administradores) {
            AdministradorMantenimiento administradorMantenimiento = new AdministradorMantenimiento(administrador);
            cmbAdministradores.addItem(administradorMantenimiento);
     } 
    }//GEN-LAST:event_jButElimiActionPerformed

    private void jBtuCancActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtuCancActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jBtuCancActionPerformed

    private void cmbAdministradoresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAdministradoresItemStateChanged
        AdministradorMantenimiento administradorMantenimiento = (AdministradorMantenimiento) cmbAdministradores.getSelectedItem();
        jtextUsuario.setText(administradorMantenimiento.getAdministrador().getUsuario());        
    }//GEN-LAST:event_cmbAdministradoresItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbAdministradores;
    private javax.swing.JButton jBtModifi;
    private javax.swing.JButton jBtuCanc;
    private javax.swing.JButton jButElimi;
    private javax.swing.JPasswordField jConfClave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPasClave;
    private javax.swing.JTextField jtextUsuario;
    // End of variables declaration//GEN-END:variables
}
