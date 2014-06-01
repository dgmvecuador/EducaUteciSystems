/*
 *  ChatServerInterface.java
 *  Copyright (C) 2014  Guillermo Pazos <shadowguiller@gmail.com>
 *  Copyright (C) 2014  Diego Estévez <dgmvecuador@gmail.com>
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import org.educautecisystems.core.Sistema;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONFIGURACION;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_DOCUMENTOS_TEORIA;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_EJERCICIOS_RESUELTOS;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_PRACTICA_LABORATORIO;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA;

/**
 *
 * @author Guillermo
 */
public class MaterialApoyo extends javax.swing.JInternalFrame {
    private VentanaPrincipal ventanaPrincipal;
    private final JFileChooser fc;
    
    /* Direcciones de carpatas */
    private final String RUTA_DOCUMENTO_TEORIA;
    private final String RUTA_PRACTICA_LABORATORIO;
    private final String RUTA_EJERCICIOS_RESUELTOS;
    private final String RUTA_TAREA;
    
    /* Constantes */
    public static final String MENSAJE_SUBIENDO_ARCHIVO = "Subiendo archivo";
    
    /* Lista de archivos */
    private DefaultListModel listaDocumentosTeoria = new DefaultListModel();
    private String listaCompletaDocumentosTeoricas = "";

    /**
     * Creates new form MaterialApoyo
     */
    public MaterialApoyo( VentanaPrincipal ventanaPrincipal ) {
        initComponents();
        
        /* Iniciar el diálogo */
        fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), "*.*"));
        
        Timer actualizarListaArchivosTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarListaArchivos();
            }
        });
        actualizarListaArchivosTimer.start();
        
        /* Borra la barra de progreso */
        barraProgresoSubidaArchivo.setToolTipText("");
        barraProgresoSubidaArchivo.setString(MENSAJE_SUBIENDO_ARCHIVO);
        barraProgresoSubidaArchivo.setStringPainted(false);
        
        /* Obtener los directorios */
        Properties propiedadesSistema = System.getProperties();
        String carpetaUsuario = propiedadesSistema.getProperty("user.home");
        
        /* Carpetas de configuraciones */
        File carpetaConfiguracion = new File(carpetaUsuario, NOMBRE_CARPETA_CONFIGURACION);
        File carpetaConfArchivos = new File(carpetaConfiguracion, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS);
        
        /* Materiales de apoyo */
        File carpetaConfArchivosDocumentosTeoria = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_DOCUMENTOS_TEORIA);
        File carpetaConfArchivosPracticaLaboratorio = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_PRACTICA_LABORATORIO);
        File carpetaConfArchivosEjercicioResueltos = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_EJERCICIOS_RESUELTOS);
        File carpetaConfArchivosTarea = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA);
        
        /* Guardar los resuiltados. */
        RUTA_DOCUMENTO_TEORIA = carpetaConfArchivosDocumentosTeoria.getAbsolutePath();
        RUTA_PRACTICA_LABORATORIO = carpetaConfArchivosPracticaLaboratorio.getAbsolutePath();
        RUTA_EJERCICIOS_RESUELTOS = carpetaConfArchivosEjercicioResueltos.getAbsolutePath();
        RUTA_TAREA = carpetaConfArchivosTarea.getAbsolutePath();
        
        this.ventanaPrincipal = ventanaPrincipal;
        
        /* Actualizar la lista por pimera vez. */
        actualizarListaArchivos();
    }
    
    private static void copiarArchivos( File origen, File destino ) throws Exception {
        InputStream in = new FileInputStream(origen);
        OutputStream out = new FileOutputStream(destino);
        
        byte[] buf = new byte[1024];
        int len;

        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        
        out.flush();
        in.close();
        out.close();
    }
    
    private void cargarArchivoDocumentoTeoria( String rutaSubida ) {
         /* Leer un archivo, respetar el resto. */
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            final File origen = fc.getSelectedFile();
            final File destino = new File(rutaSubida, origen.getName());
            
            if ( destino.exists() ) {
                Sistema.mostrarMensajeError("El archivo ya existe.");
                return;
            }
            barraProgresoSubidaArchivo.setIndeterminate(true);
            ventanaPrincipal.setEnabled(false);
            barraProgresoSubidaArchivo.setStringPainted(true);
            
            Thread hiloCopia = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MaterialApoyo.copiarArchivos(origen, destino);
                        MaterialApoyo.this.actualizarListaArchivos();
                    } catch ( Exception e ) {
                        Sistema.mostrarMensajeError("No se pudo subir archivo.");
                    }
                    MaterialApoyo.this.barraProgresoSubidaArchivo.setToolTipText("");
                    MaterialApoyo.this.ventanaPrincipal.setEnabled(true);
                    MaterialApoyo.this.barraProgresoSubidaArchivo.setIndeterminate(false);
                    MaterialApoyo.this.barraProgresoSubidaArchivo.setStringPainted(false);
                    
                    Sistema.mostrarMensajeInformativo("Se ha sibido exitosamente el/los archivos.");
                }
            });
            hiloCopia.start();
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

        jMaterApoytabpanel = new javax.swing.JTabbedPane();
        jPanelTeo = new javax.swing.JPanel();
        jlabAvis = new javax.swing.JLabel();
        btnSubirDocumentoTeoria = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaDocumentos = new javax.swing.JList();
        btnEliminarDocumentoTeoria = new javax.swing.JButton();
        jPanelPractLab = new javax.swing.JPanel();
        jlabAvis1 = new javax.swing.JLabel();
        btnSubirPracticaLaboratorio = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListDoc1 = new javax.swing.JList();
        btnEliminarPracticaLaboratorio = new javax.swing.JButton();
        jPanelEjerResu = new javax.swing.JPanel();
        jlabAvis2 = new javax.swing.JLabel();
        jButSubir2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListDoc2 = new javax.swing.JList();
        jButEliminar2 = new javax.swing.JButton();
        jPanelTarea = new javax.swing.JPanel();
        jlabAvis3 = new javax.swing.JLabel();
        jButSubir3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jListDoc3 = new javax.swing.JList();
        jButEliminar3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        barraProgresoSubidaArchivo = new javax.swing.JProgressBar();

        setClosable(true);
        setTitle("Material de Apoyo");
        setName("MaterialApoyo"); // NOI18N

        jlabAvis.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        btnSubirDocumentoTeoria.setText("Subir");
        btnSubirDocumentoTeoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirDocumentoTeoriaActionPerformed(evt);
            }
        });

        listaDocumentos.setModel(listaDocumentosTeoria);
        jScrollPane1.setViewportView(listaDocumentos);

        btnEliminarDocumentoTeoria.setText("Eliminar");
        btnEliminarDocumentoTeoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarDocumentoTeoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTeoLayout = new javax.swing.GroupLayout(jPanelTeo);
        jPanelTeo.setLayout(jPanelTeoLayout);
        jPanelTeoLayout.setHorizontalGroup(
            jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTeoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlabAvis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelTeoLayout.createSequentialGroup()
                        .addComponent(btnSubirDocumentoTeoria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarDocumentoTeoria)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTeoLayout.setVerticalGroup(
            jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTeoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addGroup(jPanelTeoLayout.createSequentialGroup()
                        .addComponent(jlabAvis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSubirDocumentoTeoria)
                            .addComponent(btnEliminarDocumentoTeoria))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMaterApoytabpanel.addTab("Documento Teoria", jPanelTeo);

        jlabAvis1.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        btnSubirPracticaLaboratorio.setText("Subir");
        btnSubirPracticaLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirPracticaLaboratorioActionPerformed(evt);
            }
        });

        jListDoc1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(jListDoc1);

        btnEliminarPracticaLaboratorio.setText("Eliminar");

        javax.swing.GroupLayout jPanelPractLabLayout = new javax.swing.GroupLayout(jPanelPractLab);
        jPanelPractLab.setLayout(jPanelPractLabLayout);
        jPanelPractLabLayout.setHorizontalGroup(
            jPanelPractLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPractLabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPractLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlabAvis1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelPractLabLayout.createSequentialGroup()
                        .addComponent(btnSubirPracticaLaboratorio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarPracticaLaboratorio)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelPractLabLayout.setVerticalGroup(
            jPanelPractLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPractLabLayout.createSequentialGroup()
                .addGroup(jPanelPractLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPractLabLayout.createSequentialGroup()
                        .addComponent(jlabAvis1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPractLabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminarPracticaLaboratorio)
                            .addComponent(btnSubirPracticaLaboratorio))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPractLabLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMaterApoytabpanel.addTab("Practica de Laboratorio", jPanelPractLab);

        jlabAvis2.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        jButSubir2.setText("Subir");
        jButSubir2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSubir2ActionPerformed(evt);
            }
        });

        jScrollPane3.setViewportView(jListDoc2);

        jButEliminar2.setText("Eliminar");

        javax.swing.GroupLayout jPanelEjerResuLayout = new javax.swing.GroupLayout(jPanelEjerResu);
        jPanelEjerResu.setLayout(jPanelEjerResuLayout);
        jPanelEjerResuLayout.setHorizontalGroup(
            jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlabAvis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                        .addComponent(jButSubir2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButEliminar2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelEjerResuLayout.setVerticalGroup(
            jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                        .addComponent(jlabAvis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButSubir2)
                            .addComponent(jButEliminar2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMaterApoytabpanel.addTab("Ejercicios Resueltos", jPanelEjerResu);

        jlabAvis3.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        jButSubir3.setText("Subir");
        jButSubir3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSubir3ActionPerformed(evt);
            }
        });

        jScrollPane4.setViewportView(jListDoc3);

        jButEliminar3.setText("Eliminar");

        javax.swing.GroupLayout jPanelTareaLayout = new javax.swing.GroupLayout(jPanelTarea);
        jPanelTarea.setLayout(jPanelTareaLayout);
        jPanelTareaLayout.setHorizontalGroup(
            jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTareaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlabAvis3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelTareaLayout.createSequentialGroup()
                        .addComponent(jButSubir3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButEliminar3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTareaLayout.setVerticalGroup(
            jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTareaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addGroup(jPanelTareaLayout.createSequentialGroup()
                        .addComponent(jlabAvis3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButSubir3)
                            .addComponent(jButEliminar3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMaterApoytabpanel.addTab("Tarea", jPanelTarea);

        jLabel1.setText("<html><b>Por favor, escoja el tipo de material que desea subir:</b></html>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jMaterApoytabpanel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(barraProgresoSubidaArchivo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jMaterApoytabpanel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(barraProgresoSubidaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarDocumentoTeoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarDocumentoTeoriaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEliminarDocumentoTeoriaActionPerformed

    private void actualizarListaArchivos() {
        synchronized (this) {
            /* Colocar las listas. */
            File rutaDocumentosTeoria = new File(RUTA_DOCUMENTO_TEORIA);
            String tmpListaCompletaDocumentosTeoria = "";
            
            for ( File archivo:rutaDocumentosTeoria.listFiles() ) {
                if ( archivo.isFile() && archivo.canRead() ) {
                    tmpListaCompletaDocumentosTeoria += archivo.getName()+";";
                }
            }
            
            /* Actualizar la lista, si es necesario. */
            if ( !tmpListaCompletaDocumentosTeoria.equals(listaCompletaDocumentosTeoricas) ) {
                /* Limpiar las listas. */
                listaDocumentosTeoria.clear();
                
                for (File archivo : rutaDocumentosTeoria.listFiles()) {
                    if (archivo.isFile() && archivo.canRead()) {
                        listaDocumentosTeoria.addElement(new ArchivoMaterialApoyo(archivo));
                    }
                }
                listaCompletaDocumentosTeoricas = tmpListaCompletaDocumentosTeoria;
            }
            
            
        }
    }
    
    private void btnSubirDocumentoTeoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirDocumentoTeoriaActionPerformed
       cargarArchivoDocumentoTeoria(RUTA_DOCUMENTO_TEORIA);
    }//GEN-LAST:event_btnSubirDocumentoTeoriaActionPerformed

    private void btnSubirPracticaLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirPracticaLaboratorioActionPerformed
        /* TODO: copy code */
    }//GEN-LAST:event_btnSubirPracticaLaboratorioActionPerformed

    private void jButSubir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSubir2ActionPerformed
        /* TODO: copy code */
    }//GEN-LAST:event_jButSubir2ActionPerformed

    private void jButSubir3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSubir3ActionPerformed
        /* TODO: copy code */
    }//GEN-LAST:event_jButSubir3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgresoSubidaArchivo;
    private javax.swing.JButton btnEliminarDocumentoTeoria;
    private javax.swing.JButton btnEliminarPracticaLaboratorio;
    private javax.swing.JButton btnSubirDocumentoTeoria;
    private javax.swing.JButton btnSubirPracticaLaboratorio;
    private javax.swing.JButton jButEliminar2;
    private javax.swing.JButton jButEliminar3;
    private javax.swing.JButton jButSubir2;
    private javax.swing.JButton jButSubir3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jListDoc1;
    private javax.swing.JList jListDoc2;
    private javax.swing.JList jListDoc3;
    private javax.swing.JTabbedPane jMaterApoytabpanel;
    private javax.swing.JPanel jPanelEjerResu;
    private javax.swing.JPanel jPanelPractLab;
    private javax.swing.JPanel jPanelTarea;
    private javax.swing.JPanel jPanelTeo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel jlabAvis;
    private javax.swing.JLabel jlabAvis1;
    private javax.swing.JLabel jlabAvis2;
    private javax.swing.JLabel jlabAvis3;
    private javax.swing.JList listaDocumentos;
    // End of variables declaration//GEN-END:variables
}
