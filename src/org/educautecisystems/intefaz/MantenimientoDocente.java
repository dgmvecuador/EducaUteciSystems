/*
 *  MantenimientoDocente.java
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

import java.util.ArrayList;
import java.util.List;
import org.educautecisystems.controladores.DocenteJpaController;
import org.educautecisystems.controladores.MateriaJpaController;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.core.Sistema;
import org.educautecisystems.entidades.Docente;
import org.educautecisystems.entidades.Materia;
import org.educautecisystems.intefaz.mantenimiento.DocenteMantenimiento;
import org.educautecisystems.intefaz.objects.ObjComboBoxMateria;

/**
 *
 * @author Shadow2012
 */
public class MantenimientoDocente extends javax.swing.JInternalFrame {
    private ArrayList <ObjComboBoxMateria> objMaterias = new ArrayList<ObjComboBoxMateria>();
    /**
     * Creates new form MatenimientoDocente
     */
    public MantenimientoDocente() {
        initComponents();
        DocenteJpaController controladorDocente = new DocenteJpaController(Sistema.getEmf());
        List <Docente> docentes  = controladorDocente.findDocenteEntities();
        
        for (Docente docente:docentes) {
            DocenteMantenimiento docenteMantenimiento = new DocenteMantenimiento(docente);
            cmbDocentes.addItem(docenteMantenimiento);
        }
        MateriaJpaController controladorMateria = new MateriaJpaController(Sistema.getEmf());
        List <Materia> materias = controladorMateria.findMateriaEntities();
        
        for (Materia materia:materias)
        {
            ObjComboBoxMateria objMateria = new ObjComboBoxMateria(materia);
            cmbMateria.addItem(objMateria);
            objMaterias.add(objMateria);
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
        jButElimi = new javax.swing.JButton();
        cmbDocentes = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jtextUsuario = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cmbMateria = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jBtuCanc = new javax.swing.JButton();
        jConfClave = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jPasClave = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();

        setTitle("Mantenimiento Docente");

        jBtModifi.setText("Modificar");
        jBtModifi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtModifiActionPerformed(evt);
            }
        });

        jButElimi.setText("Eliminar");
        jButElimi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButElimiActionPerformed(evt);
            }
        });

        cmbDocentes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDocentesItemStateChanged(evt);
            }
        });

        jLabel1.setText("<html><h2><b>Docentes</b></h2></html>");

        jLabel2.setText("Usuario");

        jLabel4.setText("Materia");

        jLabel5.setText("Docentes Existentes");

        jBtuCanc.setText("Cancelar");
        jBtuCanc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtuCancActionPerformed(evt);
            }
        });

        jLabel6.setText("Confirmación de Contraseña");

        jLabel3.setText("Contraseña ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbDocentes, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jBtuCanc)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jBtModifi)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jButElimi, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbMateria, javax.swing.GroupLayout.Alignment.LEADING, 0, 205, Short.MAX_VALUE)
                                            .addComponent(jtextUsuario, javax.swing.GroupLayout.Alignment.LEADING))))
                                .addGap(0, 18, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jConfClave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPasClave, javax.swing.GroupLayout.Alignment.LEADING))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(57, 57, 57))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtextUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbDocentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPasClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jConfClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cmbMateria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtModifi)
                    .addComponent(jButElimi))
                .addGap(18, 18, 18)
                .addComponent(jBtuCanc)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtModifiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtModifiActionPerformed
        // TODO add your handling code here:
//        Es para que obligue al usuario ingresar todo en los campos
        if (jtextUsuario.getText().isEmpty() || new String(jPasClave.getPassword()).isEmpty() || new String(jConfClave.getPassword()).isEmpty()) {
            Sistema.mostrarMensajeError("Por favor llene todos los campos.");
            return;
        }
          /*  cambio de un elemento */
       if (!new String (jPasClave.getPassword()).equals(new String(jConfClave.getPassword()))) {
            Sistema.mostrarMensajeError("Las contraseña no conciden");
            return;
        }
        DocenteJpaController controladorDocente = new DocenteJpaController(Sistema.getEmf());
        DocenteMantenimiento docenteMantenimiento = (DocenteMantenimiento) cmbDocentes.getSelectedItem();
        
        Docente modificador = docenteMantenimiento.getDocente();
        modificador.setUsuario(jtextUsuario.getText());
        modificador.setContrasena(new String(jPasClave.getPassword()));
        ObjComboBoxMateria maselect = (ObjComboBoxMateria) cmbMateria.getSelectedItem();
        modificador.setIdMateria(maselect.getMateria());
        try {
            controladorDocente.edit(modificador);
              Sistema.mostrarMensajeInformativo("Se a modificado el docente con exito");      
        } catch (NonexistentEntityException ex) {
            Sistema.mostrarMensajeError("No existe el docente actual");
        } catch (Exception ex) {
            Sistema.mostrarMensajeError("Error al modificar el docente");
        }
     this.dispose();  
    }//GEN-LAST:event_jBtModifiActionPerformed

    private void jBtuCancActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtuCancActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jBtuCancActionPerformed

    private void jButElimiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButElimiActionPerformed
        // TODO add your handling code here:
      DocenteJpaController controladorDocente = new DocenteJpaController(Sistema.getEmf());
      DocenteMantenimiento actual = (DocenteMantenimiento)cmbDocentes.getSelectedItem();
        try {
            controladorDocente.destroy(actual.getDocente().getIdDocente());
            Sistema.mostrarMensajeInformativo("Se ha borrado safisfactoriamente el docente");
            this.setVisible(false);
            this.dispose();
            
        } catch (NonexistentEntityException ex) {
            Sistema.mostrarMensajeError("No se puede borrar el Docente");
            return;
        }
        cmbDocentes.removeAllItems();
        List <Docente> docentes  = controladorDocente.findDocenteEntities();
        
        for (Docente docente:docentes) {
            DocenteMantenimiento docenteMantenimiento = new DocenteMantenimiento(docente);
            cmbDocentes.addItem(docenteMantenimiento);
        }
      this.dispose();        
    }//GEN-LAST:event_jButElimiActionPerformed

    private void cmbDocentesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDocentesItemStateChanged
        // TODO add your handling code here:
        DocenteMantenimiento docenteMantenimiento = (DocenteMantenimiento) cmbDocentes.getSelectedItem();
        jtextUsuario.setText(docenteMantenimiento.getDocente().getUsuario());
        ObjComboBoxMateria objMateria = null;
        
        /* Encontrar la materia que corresponde. */
        for ( ObjComboBoxMateria ocmm:objMaterias ) {
            if ( ocmm.getMateria().getIdMateria() == docenteMantenimiento.getDocente().getIdMateria().getIdMateria() ) {
                objMateria = ocmm;
            }
        }
        cmbMateria.setSelectedItem(objMateria);//      
    }//GEN-LAST:event_cmbDocentesItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDocentes;
    private javax.swing.JComboBox cmbMateria;
    private javax.swing.JButton jBtModifi;
    private javax.swing.JButton jBtuCanc;
    private javax.swing.JButton jButElimi;
    private javax.swing.JPasswordField jConfClave;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPasswordField jPasClave;
    private javax.swing.JTextField jtextUsuario;
    // End of variables declaration//GEN-END:variables
}
