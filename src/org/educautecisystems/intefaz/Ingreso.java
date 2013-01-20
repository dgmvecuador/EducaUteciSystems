/*
 *  Ingreso.java
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
import org.educautecisystems.controladores.DocenteJpaController;
import org.educautecisystems.core.Sistema;
import org.educautecisystems.entidades.Administrador;
import org.educautecisystems.entidades.Docente;

/**
 *
 * @author Shadow2012
 */
public class Ingreso extends javax.swing.JInternalFrame {
private static final int TIPO_USUARIO_ADMINISTRADOR =0;
private static final int TIPO_USUARIO_DOCENTE =1;  
private VentanaPrincipal principal;
    /**
     * Creates new form Ingreso
     */
   
    public Ingreso(VentanaPrincipal principal) {
        initComponents();
        this.principal = principal;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
     private boolean comprobarUsuarioAdministrador() {
        AdministradorJpaController controladorAdministrador = 
                new AdministradorJpaController(Sistema.getEmf());
        List <Administrador> administradores = controladorAdministrador.findAdministradorEntities();
        
        for ( Administrador administrador:administradores ) {
            if ( administrador.getUsuario().equals(txtUsuario.getText()) && administrador.getContrasena().equals(txtClave.getText())) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean comprobarUsuarioDocente () {
        DocenteJpaController controladorDocente = new DocenteJpaController(Sistema.getEmf());
        List <Docente> docentes = controladorDocente.findDocenteEntities();
        
        for ( Docente docente:docentes ) {
            if ( docente.getUsuario().equals(txtUsuario.getText()) && docente.getContrasena().equals(txtClave.getText())) {
                return false;
            }
        }
        return true;  
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ButtonIngreso = new javax.swing.JButton();
        buttonCancelar = new javax.swing.JButton();
        txtUsuario = new javax.swing.JTextField();
        txtClave = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        tipoUsuarioCombobox = new javax.swing.JComboBox();

        setTitle("Ingreso Personal");

        jLabel1.setFont(new java.awt.Font("Traditional Arabic", 1, 24)); // NOI18N
        jLabel1.setText("EducaUteciSystems");

        jLabel2.setText("Usuario:");

        jLabel3.setText("Clave:");

        ButtonIngreso.setText("Ingresar");
        ButtonIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonIngresoActionPerformed(evt);
            }
        });

        buttonCancelar.setText("Cancelar");
        buttonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCancelarActionPerformed(evt);
            }
        });

        jLabel4.setText("Tipo:");

        tipoUsuarioCombobox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Admistrador", "Docente" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(ButtonIngreso)
                                .addGap(18, 18, 18)
                                .addComponent(buttonCancelar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                    .addComponent(jLabel4))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtUsuario)
                                    .addComponent(txtClave)
                                    .addComponent(tipoUsuarioCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(tipoUsuarioCombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonIngreso)
                    .addComponent(buttonCancelar))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ButtonIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonIngresoActionPerformed
        
        if (txtUsuario.getText().isEmpty() || new String(txtClave.getPassword()).isEmpty()) {
            Sistema.mostrarMensajeError("Por favor llene todos los campos.");
            return;
        }
        int idTipoUsuario = tipoUsuarioCombobox.getSelectedIndex();

        if (idTipoUsuario == TIPO_USUARIO_ADMINISTRADOR) {
            String user = txtUsuario.getText();
            String password = new String (txtClave.getPassword());
            
            AdministradorJpaController controladorAdministrador = new AdministradorJpaController(Sistema.getEmf());
            List <Administrador> administradores = controladorAdministrador.findAdministradorEntities();
            
            for ( Administrador administrador:administradores ) {
                if ( administrador.getUsuario().equals(user) && administrador.getContrasena().equals(password) ) {
                    principal.MostrarAdministrador();
                    this.setVisible(false);
                    this.dispose();
                    Sistema.mostrarMensajeInformativo("Sesión de administrador ingresada correctamente.");
                    return;
                }
            }
            Sistema.mostrarMensajeError("Contraseña incorrecta o usuario no existen.");
            return;
            
        } else if (idTipoUsuario == TIPO_USUARIO_DOCENTE) {
            String user = txtUsuario.getText();
            String password = new String (txtClave.getPassword());
            
            DocenteJpaController controladorDocente = new DocenteJpaController(Sistema.getEmf());
            List <Docente> docentes = controladorDocente.findDocenteEntities();
            
            for ( Docente docente:docentes) {
                if (docente.getUsuario().equals(user) && docente.getContrasena().equals(password)){
                    principal.MostrarDocente();
                    this.setVisible(false);
                    this.dispose();
                    Sistema.mostrarMensajeInformativo("Sesión de Docente ingresada correctamente");
                    return;
                }
            }
           Sistema.mostrarMensajeError("Contraseña incorrecta o usuario no existen.");
           return;
        } else {
            Sistema.mostrarMensajeError("Tipo de usuario no soportado.");
         
        }
    }//GEN-LAST:event_ButtonIngresoActionPerformed

    private void buttonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_buttonCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonIngreso;
    private javax.swing.JButton buttonCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JComboBox tipoUsuarioCombobox;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
