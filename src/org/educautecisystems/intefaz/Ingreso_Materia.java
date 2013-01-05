/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz;
import java.util.List;
import org.educautecisystems.controladores.MateriaJpaController;
import org.educautecisystems.core.Sistema;
import org.educautecisystems.entidades.Materia;
import org.educautecisystems.intefaz.objects.ObjComboBoxMateria;


/**
 *
 * @author Shadow2013
 */
public class Ingreso_Materia extends javax.swing.JInternalFrame {

    /**
     * Creates new form Ingreso_Materia
     */
    public Ingreso_Materia() {
        initComponents();
        cargarMaterias();     
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void cargarMaterias() {
        MateriaJpaController controladorMateria = new MateriaJpaController(Sistema.getEmf());
        List<Materia> materias = controladorMateria.findMateriaEntities();
        
        /* Cerrar ventana cuando no hay materias */
        if (materias.isEmpty()) {
            Sistema.mostrarMensajeError("No se han ingresado materias.");
            this.dispose();
            return;
        }
        
        for ( Materia materia:materias ) {
            ComboIngTi.addItem(new ObjComboBoxMateria(materia));
        }
    }
    
     
    private boolean comprobarNombreMateria () {
        MateriaJpaController controladorMateria = new MateriaJpaController(Sistema.getEmf());
        List <Materia> materias = controladorMateria.findMateriaEntities();
        
        for ( Materia materia:materias ) {
            if ( materia.getNombre().equals(TextIngreso.getText()) ) {
                return false;
            }
        }
        return true;  
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ComboIngTi = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        TextIngreso = new javax.swing.JTextField();
        ButtonIngreso = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();

        setTitle("Ingreso de Nueva Materia");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel1.setText("Ingreso de Nueva Facultad");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel5.setText("Facultades Existentes");

        ComboIngTi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "" }));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 14)); // NOI18N
        jLabel4.setText("Ingrese Nuevo:");

        ButtonIngreso.setText("Ingresar");
        ButtonIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonIngresoActionPerformed(evt);
            }
        });

        jButCancelar.setText("Cancelar");
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(33, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TextIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, 472, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(ButtonIngreso)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButCancelar)
                                .addGap(8, 8, 8))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel4))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ComboIngTi, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(ComboIngTi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextIngreso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonIngreso)
                    .addComponent(jButCancelar))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void ButtonIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonIngresoActionPerformed
                   if ( TextIngreso.getText().isEmpty()) {
                        Sistema.mostrarMensajeError("Por favor llene el campo.");
                        return;
                    }
        
                    if ( !comprobarNombreMateria() ) {
                            Sistema.mostrarMensajeError("La Materia ya existe.");
                            return;
                        }
        
                    Materia nuevoMateria = new Materia();
        
                    nuevoMateria.setNombre(TextIngreso.getText());
        
                    MateriaJpaController controlandorMateria =
                            new MateriaJpaController(Sistema.getEmf());
        
                    try {
                            controlandorMateria.create(nuevoMateria);
                        } catch ( Exception e ) {
                            Sistema.mostrarMensajeError("No se pudo crear la nueva materia.");
                        }
        
                    Sistema.mostrarMensajeInformativo("Se ha ingresado satisfactoriamente la materia.");
                   
    }//GEN-LAST:event_ButtonIngresoActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButCancelarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonIngreso;
    private javax.swing.JComboBox ComboIngTi;
    private javax.swing.JTextField TextIngreso;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}