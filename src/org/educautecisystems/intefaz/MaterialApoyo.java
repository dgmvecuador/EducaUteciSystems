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
import javax.swing.JList;
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
    private DefaultListModel listaPracticaLaboratorio = new DefaultListModel();
    private String listaCompletaPracticaLaboratorio = "";
    private DefaultListModel listaEjerciciosResueltos = new DefaultListModel();
    private String listaCompletaEjerciciosResueltos = "";
    private DefaultListModel listaTarea = new DefaultListModel();
    private String listaCompletaTarea = "";

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
    
    private void cargarArchivo( String rutaSubida ) {
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
    
    private void borrarArchivo( JList lista ) {
        ArchivoMaterialApoyo archivo = (ArchivoMaterialApoyo) lista.getSelectedValue();
        
        if ( archivo != null ) {
            if (Sistema.confirmarSiNoPregunta("Esta seguro de borra el archivo?")) {
                if ( archivo.getArchivo().delete() ) {
                    actualizarListaArchivos();
                    Sistema.mostrarMensajeInformativo("Se ha borrado exitosamente el archivo.");
                }
            }
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
        controlListaDocumentosTeoria = new javax.swing.JList();
        btnEliminarDocumentoTeoria = new javax.swing.JButton();
        jPanelPractLab = new javax.swing.JPanel();
        jlabAvis1 = new javax.swing.JLabel();
        btnSubirPracticaLaboratorio = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        controlListaPracticaLaboratorio = new javax.swing.JList();
        btnEliminarPracticaLaboratorio = new javax.swing.JButton();
        jPanelEjerResu = new javax.swing.JPanel();
        jlabAvis2 = new javax.swing.JLabel();
        btnSubirEjerciciosResueltos = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        controlListaEjerciciosResueltos = new javax.swing.JList();
        btnEliminarEjerciciosResueltos = new javax.swing.JButton();
        jPanelTarea = new javax.swing.JPanel();
        jlabAvis3 = new javax.swing.JLabel();
        btnSubirTarea = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        controlListaTarea = new javax.swing.JList();
        btnEliminarTarea = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        barraProgresoSubidaArchivo = new javax.swing.JProgressBar();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Material de Apoyo");
        setName("MaterialApoyo"); // NOI18N

        jlabAvis.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        btnSubirDocumentoTeoria.setText("Subir");
        btnSubirDocumentoTeoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirDocumentoTeoriaActionPerformed(evt);
            }
        });

        controlListaDocumentosTeoria.setModel(listaDocumentosTeoria);
        jScrollPane1.setViewportView(controlListaDocumentosTeoria);

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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTeoLayout.setVerticalGroup(
            jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTeoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTeoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
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

        controlListaPracticaLaboratorio.setModel(listaPracticaLaboratorio);
        controlListaPracticaLaboratorio.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(controlListaPracticaLaboratorio);

        btnEliminarPracticaLaboratorio.setText("Eliminar");
        btnEliminarPracticaLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarPracticaLaboratorioActionPerformed(evt);
            }
        });

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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
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
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMaterApoytabpanel.addTab("Practica de Laboratorio", jPanelPractLab);

        jlabAvis2.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        btnSubirEjerciciosResueltos.setText("Subir");
        btnSubirEjerciciosResueltos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirEjerciciosResueltosActionPerformed(evt);
            }
        });

        controlListaEjerciciosResueltos.setModel(listaEjerciciosResueltos);
        jScrollPane3.setViewportView(controlListaEjerciciosResueltos);

        btnEliminarEjerciciosResueltos.setText("Eliminar");
        btnEliminarEjerciciosResueltos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarEjerciciosResueltosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelEjerResuLayout = new javax.swing.GroupLayout(jPanelEjerResu);
        jPanelEjerResu.setLayout(jPanelEjerResuLayout);
        jPanelEjerResuLayout.setHorizontalGroup(
            jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlabAvis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                        .addComponent(btnSubirEjerciciosResueltos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarEjerciciosResueltos)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelEjerResuLayout.setVerticalGroup(
            jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                    .addGroup(jPanelEjerResuLayout.createSequentialGroup()
                        .addComponent(jlabAvis2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelEjerResuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSubirEjerciciosResueltos)
                            .addComponent(btnEliminarEjerciciosResueltos))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jMaterApoytabpanel.addTab("Ejercicios Resueltos", jPanelEjerResu);

        jlabAvis3.setText("<html><b>Seleccione una opci&oacute;n:</b></html>");

        btnSubirTarea.setText("Subir");
        btnSubirTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubirTareaActionPerformed(evt);
            }
        });

        controlListaTarea.setModel(listaTarea);
        jScrollPane4.setViewportView(controlListaTarea);

        btnEliminarTarea.setText("Eliminar");
        btnEliminarTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarTareaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTareaLayout = new javax.swing.GroupLayout(jPanelTarea);
        jPanelTarea.setLayout(jPanelTareaLayout);
        jPanelTareaLayout.setHorizontalGroup(
            jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTareaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlabAvis3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelTareaLayout.createSequentialGroup()
                        .addComponent(btnSubirTarea)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEliminarTarea)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelTareaLayout.setVerticalGroup(
            jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTareaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                    .addGroup(jPanelTareaLayout.createSequentialGroup()
                        .addComponent(jlabAvis3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelTareaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSubirTarea)
                            .addComponent(btnEliminarTarea))
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
                .addComponent(jMaterApoytabpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraProgresoSubidaArchivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEliminarDocumentoTeoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarDocumentoTeoriaActionPerformed
        borrarArchivo(controlListaDocumentosTeoria);
    }//GEN-LAST:event_btnEliminarDocumentoTeoriaActionPerformed

    private void actualizarListaArchivos() {
        synchronized (this) {
            /* Colocar las listas. */
            File rutaDocumentosTeoria = new File(RUTA_DOCUMENTO_TEORIA);
            File rutaPracticaLaboratorio = new File(RUTA_PRACTICA_LABORATORIO);
            File rutaEjerciciosResueltos = new File(RUTA_EJERCICIOS_RESUELTOS);
            File rutaTarea = new File(RUTA_TAREA);
            String tmpListaCompletaDocumentosTeoria = "";
            String tmpListaCompletaPracticaLaboratorio = "";
            String tmpListaCompletaEjerciciosResueltos = "";
            String tmpListaCompletaTarea = "";
            
            /* Verificar los si las listas han cambiado. */
            for ( File archivo:rutaDocumentosTeoria.listFiles() ) {
                if ( archivo.isFile() && archivo.canRead() ) {
                    tmpListaCompletaDocumentosTeoria += archivo.getName()+";";
                }
            }
            
            for ( File archivo:rutaPracticaLaboratorio.listFiles() ) {
                if ( archivo.isFile() && archivo.canRead() ) {
                    tmpListaCompletaPracticaLaboratorio += archivo.getName()+";";
                }
            }
            
            for ( File archivo:rutaEjerciciosResueltos.listFiles() ) {
                if ( archivo.isFile() && archivo.canRead() ) {
                    tmpListaCompletaEjerciciosResueltos += archivo.getName()+";";
                }
            }
            
            for ( File archivo:rutaTarea.listFiles() ) {
                if ( archivo.isFile() && archivo.canRead() ) {
                    tmpListaCompletaTarea += archivo.getName()+";";
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
            
            if ( !tmpListaCompletaPracticaLaboratorio.equals(listaCompletaPracticaLaboratorio) ) {
                /* Limpiar las listas. */
                listaPracticaLaboratorio.clear();
                
                for (File archivo : rutaPracticaLaboratorio.listFiles()) {
                    if (archivo.isFile() && archivo.canRead()) {
                        listaPracticaLaboratorio.addElement(new ArchivoMaterialApoyo(archivo));
                    }
                }
                listaCompletaPracticaLaboratorio = tmpListaCompletaPracticaLaboratorio;
            }
            
            if ( !tmpListaCompletaEjerciciosResueltos.equals(listaCompletaEjerciciosResueltos) ) {
                /* Limpiar las listas. */
                listaEjerciciosResueltos.clear();
                
                for (File archivo : rutaEjerciciosResueltos.listFiles()) {
                    if (archivo.isFile() && archivo.canRead()) {
                        listaEjerciciosResueltos.addElement(new ArchivoMaterialApoyo(archivo));
                    }
                }
                listaCompletaEjerciciosResueltos = tmpListaCompletaEjerciciosResueltos;
            }
            
            if ( !tmpListaCompletaTarea.equals(listaCompletaTarea) ) {
                /* Limpiar las listas. */
                listaTarea.clear();
                
                for (File archivo : rutaTarea.listFiles()) {
                    if (archivo.isFile() && archivo.canRead()) {
                        listaTarea.addElement(new ArchivoMaterialApoyo(archivo));
                    }
                }
                listaCompletaTarea = tmpListaCompletaTarea;
            }
        }
    }
    
    private void btnSubirDocumentoTeoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirDocumentoTeoriaActionPerformed
       cargarArchivo(RUTA_DOCUMENTO_TEORIA);
    }//GEN-LAST:event_btnSubirDocumentoTeoriaActionPerformed

    private void btnSubirPracticaLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirPracticaLaboratorioActionPerformed
        cargarArchivo(RUTA_PRACTICA_LABORATORIO);
    }//GEN-LAST:event_btnSubirPracticaLaboratorioActionPerformed

    private void btnSubirEjerciciosResueltosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirEjerciciosResueltosActionPerformed
        cargarArchivo(RUTA_EJERCICIOS_RESUELTOS);
    }//GEN-LAST:event_btnSubirEjerciciosResueltosActionPerformed

    private void btnSubirTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubirTareaActionPerformed
        cargarArchivo(RUTA_TAREA);
    }//GEN-LAST:event_btnSubirTareaActionPerformed

    private void btnEliminarPracticaLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarPracticaLaboratorioActionPerformed
        borrarArchivo(controlListaPracticaLaboratorio);
    }//GEN-LAST:event_btnEliminarPracticaLaboratorioActionPerformed

    private void btnEliminarEjerciciosResueltosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarEjerciciosResueltosActionPerformed
        borrarArchivo(controlListaEjerciciosResueltos);
    }//GEN-LAST:event_btnEliminarEjerciciosResueltosActionPerformed

    private void btnEliminarTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarTareaActionPerformed
        borrarArchivo(controlListaTarea);
    }//GEN-LAST:event_btnEliminarTareaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar barraProgresoSubidaArchivo;
    private javax.swing.JButton btnEliminarDocumentoTeoria;
    private javax.swing.JButton btnEliminarEjerciciosResueltos;
    private javax.swing.JButton btnEliminarPracticaLaboratorio;
    private javax.swing.JButton btnEliminarTarea;
    private javax.swing.JButton btnSubirDocumentoTeoria;
    private javax.swing.JButton btnSubirEjerciciosResueltos;
    private javax.swing.JButton btnSubirPracticaLaboratorio;
    private javax.swing.JButton btnSubirTarea;
    private javax.swing.JList controlListaDocumentosTeoria;
    private javax.swing.JList controlListaEjerciciosResueltos;
    private javax.swing.JList controlListaPracticaLaboratorio;
    private javax.swing.JList controlListaTarea;
    private javax.swing.JLabel jLabel1;
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
    // End of variables declaration//GEN-END:variables
}
