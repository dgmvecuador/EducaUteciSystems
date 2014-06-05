/*
 *  Chat.java
 *  Copyright (C) 2012  Guillermo Pazos <shadowguiller@hotmail.com>
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
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.Timer;
import org.educautecisystems.core.Sistema;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONFIGURACION;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS;
import static org.educautecisystems.core.Sistema.NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA;
import org.educautecisystems.core.chat.ServidorChat;
import org.educautecisystems.core.chat.cliente.ClienteServidorChat;
import org.educautecisystems.core.chat.elements.FileChat;
import org.educautecisystems.core.chat.elements.UserChat;
import static org.educautecisystems.intefaz.MaterialApoyo.MENSAJE_SUBIENDO_ARCHIVO;

/**
 *
 * @author Shadow2012
 */
public final class Chat extends javax.swing.JInternalFrame {

    private VentanaPrincipal ventanaPrincipal;
    private boolean esDocente;
    private final StringBuffer logChat = new StringBuffer();
    private ClienteServidorChat clienteServidorChat;
    private ArrayList<UserChat> usuarios;
    private ArrayList<FileChat> archivos;
    private long actualSize = 0;
    private long actualSizeListaUsuarios = 0;
    private long actualSizeListaArchivosDocumentosTeoria = 0;
    private long actualSizeListaArchivosPracticaLaboratorio = 0;
    private long actualSizeListaArchivosEjerciciosResueltos = 0;
    private long actualSizeListaArchivosTarea = 0;
    private final PantallaProfesor pantallaProfesor = new PantallaProfesor(this);
    private BufferedImage pantallaActual = null;
    

    /* Modelos de las listas. */
    DefaultListModel listaArchivosModeloDocumentosTeoria = new DefaultListModel();
    DefaultListModel listaArchivosModeloPracticaLaboratorio = new DefaultListModel();
    DefaultListModel listaArchivosModeloEjerciciosResueltos = new DefaultListModel();
    DefaultListModel listaArchivosModeloTarea = new DefaultListModel();
    private final String RUTA_TAREA;
    /**
     * Creates new form ChaPrueba
     */
    public Chat(VentanaPrincipal ventanaPrincipal, boolean esDocente) {
        initComponents();
        this.ventanaPrincipal = ventanaPrincipal;
        this.esDocente = esDocente;
        clienteServidorChat = new ClienteServidorChat(this);
        activarBotones(false);
        clienteServidorChat.start();
        usuarios = null;

        if (!esDocente) {
            /* Generar pantalla. */
            ventanaPrincipal.insertarNuevaVentana(pantallaProfesor);
            pantallaProfesor.setVisible(true);
            btnGenerarReporteAsistencia.setVisible(false);

            try {
                pantallaProfesor.setMaximum(true);
            } catch (PropertyVetoException ex) {

            }

            Timer actualizarPantallaProfesor = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    actualizarPantallaProfessor();
                }
            });
            actualizarPantallaProfesor.start();
            
            /* No mostrar opción. */
            ckbxPantallaDocente.setVisible(false);
        } else {
            /* Mostrar la pantalla y su contenido. */
            ckbxPantallaDocente.setVisible(true);
            ckbxPantallaDocente.setSelected(ServidorChat.mostrarPantallaDocente);
        }

        Timer actualizadorChat = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarChat();
            }
        });
        actualizadorChat.start();
        Timer actualizarListaUsuariosTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarListaUsuarios();
            }
        });
        actualizarListaUsuariosTimer.start();
        Timer actualizarListaArchivosTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarListaArchivos();
            }
        });
        
        Properties propiedadesSistema = System.getProperties();
        String carpetaUsuario = propiedadesSistema.getProperty("user.home");
        File carpetaConfiguracion = new File(carpetaUsuario, NOMBRE_CARPETA_CONFIGURACION);
        File carpetaConfArchivos = new File(carpetaConfiguracion, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS);
        File carpetaConfArchivosTarea = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA);
        RUTA_TAREA = carpetaConfArchivosTarea.getAbsolutePath();
        
        actualizarListaArchivosTimer.start();
    }

    public boolean esChatDocente() {
        return esDocente;
    }

    private void actualizarPantallaProfessor() {
        synchronized (pantallaProfesor) {
            if (pantallaActual == null) {
                return;
            }
            pantallaProfesor.actualizarPantallaProfesor(pantallaActual);
        }
    }

    public void nuevaPantallaProfesor(BufferedImage nuevaPantalla) {
        synchronized (pantallaProfesor) {
            this.pantallaActual = nuevaPantalla;
        }
    }

    public void mostrarError(String txt) {
        synchronized (logChat) {
            String mensaje = "<font color=\"red\"><b>Error: </b>" + txt + "</font><br/>";
            logChat.append(mensaje);
        }
    }

    public void mostrarInfo(String txt) {
        synchronized (logChat) {
            String mensaje = "<font color=\"blue\"><b>Info: </b>" + txt + "</font><br/>";
            logChat.append(mensaje);
        }
    }

    public void activarBotones(boolean b) {
        txtTexto.setEnabled(b);
        btnEnviar.setEnabled(b);
    }

    public void recibirPregunta(String preguntaId, String pregunta) {
        if (esDocente) {
            return;
        }
        boolean respuestaPregunta = Sistema.confirmarSiNoPregunta(pregunta);
        clienteServidorChat.enviarRespuestaPregunta(respuestaPregunta, preguntaId);
    }

    public void recibirMensaje(String userIdString, String mensaje) {
        String userName = null;
        synchronized (this) {
            int userId = 0;
            try {
                userId = Integer.parseInt(userIdString);
            } catch (NumberFormatException nfe) {
                this.mostrarError("Id de usuario no encontrado.");
                return;
            }

            /* Buascar el nombre del usuario. */
            for (UserChat userChat : usuarios) {
                if (userChat.getId() == userId) {
                    userName = userChat.getNickName();
                }
            }
        }

        synchronized (logChat) {
            String directorioActual = dameDiretorioActual();
            File imgs = new File(directorioActual, "img");

            /* Direccciones de los emoticones base. */
            File emoticonSorpresa = new File(imgs, "sorpresa.png");
            File emoticonTriste = new File(imgs, "triste.png");
            File emoticonAburrido = new File(imgs, "aburrido.png");
            File emoticonDormir = new File(imgs, "dormir.png");
            File emoticonEnojado = new File(imgs, "enojado.png");
            File emoticonEntimidado = new File(imgs, "entimidado.png");
            File emoticonJajajaja = new File(imgs, "jajaja.png");
            File emoticonMuybien = new File(imgs, "muybien.png");
            File emoticonQuee = new File(imgs, "quee.png");
            File emoticonQuemal = new File(imgs, "quemal.png");
            File emoticonSonrisa = new File(imgs, "sonrisa.png");
            File emoticonSonrisacondientes = new File(imgs, "sonrisacondientes.png");

            /* Comprobar si existen. */
            if (!emoticonSorpresa.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonSorpresa.getAbsolutePath());
                return;
            }
            if (!emoticonTriste.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonTriste.getAbsolutePath());
                return;
            }
            if (!emoticonAburrido.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonAburrido.getAbsolutePath());
                return;
            }
            if (!emoticonDormir.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonDormir.getAbsolutePath());
                return;
            }
            if (!emoticonEnojado.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonEnojado.getAbsolutePath());
                return;
            }
            if (!emoticonEntimidado.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonEntimidado.getAbsolutePath());
                return;
            }
            if (!emoticonJajajaja.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonJajajaja.getAbsolutePath());
                return;
            }
            if (!emoticonMuybien.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonMuybien.getAbsolutePath());
                return;
            }
            if (!emoticonQuee.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonQuee.getAbsolutePath());
                return;
            }
            if (!emoticonQuemal.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonQuemal.getAbsolutePath());
                return;
            }
            if (!emoticonSonrisa.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonSonrisa.getAbsolutePath());
                return;
            }
            if (!emoticonSonrisacondientes.exists()) {
                System.err.println("No existe imagen.\n\t" + emoticonEnojado.getAbsolutePath());
                return;
            }

            /* Dirección real para el chat. */
            String direccionEmoticonSorpresa = emoticonSorpresa.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonTriste = emoticonTriste.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonAburrido = emoticonAburrido.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonDormir = emoticonDormir.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonEnojado = emoticonEnojado.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonEntimidado = emoticonEntimidado.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonJajajaja = emoticonJajajaja.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonMuybien = emoticonMuybien.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonQuee = emoticonQuee.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonQuemal = emoticonQuemal.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonSonrisa = emoticonSonrisa.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");
            String direccionEmoticonSonrisacondientes = emoticonSonrisacondientes.getAbsolutePath().
                    replaceAll("\\\\", "\\\\\\\\").replaceAll(":", "|");

            String salida = mensaje.
                    replaceAll(":o", "<img src=\"file:///" + direccionEmoticonSorpresa + "\"/>").
                    replaceAll(":\\(", "<img src=\"file:///" + direccionEmoticonTriste + "\"/>").
                    replaceAll(":~", "<img src=\"file:///" + direccionEmoticonAburrido + "\"/>").
                    replaceAll(":zzzzz", "<img src=\"file:///" + direccionEmoticonDormir + "\"/>").
                    replaceAll(">\\(", "<img src=\"file:///" + direccionEmoticonEnojado + "\"/>").
                    replaceAll("oO\\(", "<img src=\"file:///" + direccionEmoticonEntimidado + "\"/>").
                    replaceAll(":\\)jajajajaja", "<img src=\"file:///" + direccionEmoticonJajajaja + "\"/>").
                    replaceAll(":\\)ok", "<img src=\"file:///" + direccionEmoticonMuybien + "\"/>").
                    replaceAll(":queeee", "<img src=\"file:///" + direccionEmoticonQuee + "\"/>").
                    replaceAll(":malmalmal", "<img src=\"file:///" + direccionEmoticonQuemal + "\"/>").
                    replaceAll(":\\)", "<img src=\"file:///" + direccionEmoticonSonrisa + "\"/>").
                    replaceAll(":D", "<img src=\"file:///" + direccionEmoticonSonrisacondientes + "\"/>").
                    replaceAll("\\b(www\\.[^ ]+\\.com)\\b", "<a href=\"http://$1\">$1</a>").
                    replaceAll("\\bN[iI]ck\\b", "<b>$0</b>").
                    replace(" ", "&nbsp;");
            logChat.append("<font color=\"black\"><b><i>").append(userName).append(":</i></b>&nbsp;").append(salida).append("</font><br>\n");
        }
    }

    private String dameDiretorioActual() {
        return System.getProperty("user.dir");
    }

    private void enviarMensaje() {
        synchronized (logChat) {
            String texto = txtTexto.getText();
            //recibirMensaje("Nick", texto);
            clienteServidorChat.enviarMensaje(texto);
            txtTexto.setText("");
        }
    }

    private void actualizarChat() {
        synchronized (logChat) {
            if (actualSize != logChat.length()) {
                contenidoChat.setText("<html><body>" + logChat.toString() + "</body></html>");
                contenidoChat.setCaretPosition(contenidoChat.getDocument().getLength());
                actualSize = logChat.length();
            }
        }
    }

    private void actualizarListaUsuarios() {
        synchronized (this) {
            StringBuilder listaUsuarios = new StringBuilder();
            for (UserChat usuarioChat : usuarios) {
                listaUsuarios.append("<font color=\"green\"><i><b>" + usuarioChat.getNickName() + "&nbsp;</b></i><font>");

                /* Esconder los nombres cuando no son docentes. */
//                if (esDocente) {
//                    listaUsuarios.append("(<font color=\"blue\">" + usuarioChat.getRealName() + ")</font><br/>");
//                } else {
                listaUsuarios.append("<br/>");
//                }

            }

            /* No hacer nada hasta que se actualice el mensaje. */
            if (actualSizeListaUsuarios == listaUsuarios.toString().length()) {
                return;
            }

            txtListaUsuarios.setText(listaUsuarios.toString());
            actualSizeListaUsuarios = listaUsuarios.toString().length();
        }
    }

    private void actualizarListaArchivos() {
        synchronized (this) {
            StringBuilder comprobadorDocumentosTeoria = new StringBuilder();
            StringBuilder comprobadorPracticasLaboratorio = new StringBuilder();
            StringBuilder comprobadorEjerciciosResueltos = new StringBuilder();
            StringBuilder comprobadorTarea = new StringBuilder();
            

            for (FileChat fileChat : archivos) {
                if ( fileChat.getTipo().equals(FileChat.TIPO_DOCUMENTO_TEORIA) ) {
                    comprobadorDocumentosTeoria.append(fileChat.toString());
                }
                if ( fileChat.getTipo().equals(FileChat.TIPO_PRACTICA_LABORATORIO) ) {
                    comprobadorPracticasLaboratorio.append(fileChat.toString());
                }
                if ( fileChat.getTipo().equals(FileChat.TIPO_EJERCICIOS_RESUELTOS) ) {
                    comprobadorEjerciciosResueltos.append(fileChat.toString());
                }
                if ( fileChat.getTipo().equals(FileChat.TIPO_TAREA) ) {
                    comprobadorTarea.append(fileChat.toString());
                }
            }

            /* Actualizar la lista de archivos. */
            if (actualSizeListaArchivosDocumentosTeoria != comprobadorDocumentosTeoria.toString().length()) {
                listaArchivosModeloDocumentosTeoria.clear();

                for (FileChat fileChat : archivos) {
                    if ( fileChat.getTipo().equals(FileChat.TIPO_DOCUMENTO_TEORIA) ) {
                        listaArchivosModeloDocumentosTeoria.addElement(fileChat);
                    }
                }
                
                actualSizeListaArchivosDocumentosTeoria = comprobadorDocumentosTeoria.toString().length();
            }
            
            if (actualSizeListaArchivosPracticaLaboratorio != comprobadorPracticasLaboratorio.toString().length()) {
                listaArchivosModeloPracticaLaboratorio.clear();

                for (FileChat fileChat : archivos) {
                    if ( fileChat.getTipo().equals(FileChat.TIPO_PRACTICA_LABORATORIO) ) {
                        listaArchivosModeloPracticaLaboratorio.addElement(fileChat);
                    }
                }
                
                actualSizeListaArchivosPracticaLaboratorio = comprobadorPracticasLaboratorio.toString().length();
            }
            
            if (actualSizeListaArchivosEjerciciosResueltos != comprobadorEjerciciosResueltos.toString().length()) {
                listaArchivosModeloEjerciciosResueltos.clear();

                for (FileChat fileChat : archivos) {
                    if ( fileChat.getTipo().equals(FileChat.TIPO_EJERCICIOS_RESUELTOS) ) {
                        listaArchivosModeloEjerciciosResueltos.addElement(fileChat);
                    }
                }
                
                actualSizeListaArchivosEjerciciosResueltos = comprobadorEjerciciosResueltos.toString().length();
            }
            
            if (actualSizeListaArchivosTarea != comprobadorTarea.toString().length()) {
                listaArchivosModeloTarea.clear();

                for (FileChat fileChat : archivos) {
                    if ( fileChat.getTipo().equals(FileChat.TIPO_TAREA) ) {
                        listaArchivosModeloTarea.addElement(fileChat);
                    }
                }
                
                actualSizeListaArchivosTarea = comprobadorTarea.toString().length();
            }
        }
    }

    public void nuevaLista(ArrayList<UserChat> usuarios) {
        synchronized (this) {
            this.usuarios = usuarios;
        }
    }

    public void nuevaListaArchivos(ArrayList<FileChat> archivos) {
        synchronized (this) {
            this.archivos = archivos;
        }
    }
    
    public void descargarArchivo( JList lista ) {
        FileChat fileChat = (FileChat) lista.getSelectedValue();

        /* Revisar si existe algún elemento seleccionado. */
        if (fileChat == null) {
            Sistema.mostrarMensajeError("Por favor seleccione un archivo.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), fileChat.getName().
                replaceAll("\\[", "_").replaceAll("\\]", "_")));
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            clienteServidorChat.descargarArchivo(fileChat, archivo);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contenidoChat = new javax.swing.JEditorPane();
        txtTexto = new javax.swing.JTextField();
        btnEnviar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtListaUsuarios = new javax.swing.JEditorPane();
        btnCerrarSesion = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnGenerarReporteAsistencia = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        ckbxPantallaDocente = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaArchivosDocumentosTeoria = new javax.swing.JList();
        btnDescargarDocumentoTeoria = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaArchivoPracticaLaboratorio = new javax.swing.JList();
        btnDescargarPracticaLaboratorio = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        listaArchivoEjerciciosResueltos = new javax.swing.JList();
        btnDescargarEjerciciosResueltos = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        listaArchivoTarea = new javax.swing.JList();
        btnDescargarTarea = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Chat");

        jLabel1.setText("Charla:");

        contenidoChat.setEditable(false);
        contenidoChat.setContentType("text/html"); // NOI18N
        jScrollPane1.setViewportView(contenidoChat);

        txtTexto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTextoKeyPressed(evt);
            }
        });

        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        jLabel2.setText("Lista de usuarios:");

        txtListaUsuarios.setEditable(false);
        txtListaUsuarios.setContentType("text/html"); // NOI18N
        jScrollPane3.setViewportView(txtListaUsuarios);

        btnCerrarSesion.setText("Cerrar Sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });

        jLabel3.setText("Lista de archivos:");

        btnGenerarReporteAsistencia.setText("Generar Reporte");
        btnGenerarReporteAsistencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerarReporteAsistenciaActionPerformed(evt);
            }
        });

        jLabel4.setText("Escriba aqui el mensaje");

        ckbxPantallaDocente.setText("Mostrar Pantalla Docente");
        ckbxPantallaDocente.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ckbxPantallaDocenteStateChanged(evt);
            }
        });

        jLabel5.setText("Eliga y Descarge la teoria");

        listaArchivosDocumentosTeoria.setModel(listaArchivosModeloDocumentosTeoria);
        listaArchivosDocumentosTeoria.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listaArchivosDocumentosTeoria);

        btnDescargarDocumentoTeoria.setText("Descargar");
        btnDescargarDocumentoTeoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarDocumentoTeoriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addContainerGap(349, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDescargarDocumentoTeoria)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                .addComponent(btnDescargarDocumentoTeoria)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(44, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Documentos Teoria", jPanel1);

        jLabel6.setText("Eliga y Descarge la practica de laboratorio");

        listaArchivoPracticaLaboratorio.setModel(listaArchivosModeloPracticaLaboratorio);
        jScrollPane4.setViewportView(listaArchivoPracticaLaboratorio);

        btnDescargarPracticaLaboratorio.setText("Descargar");
        btnDescargarPracticaLaboratorio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarPracticaLaboratorioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addContainerGap(242, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDescargarPracticaLaboratorio)
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 224, Short.MAX_VALUE)
                .addComponent(btnDescargarPracticaLaboratorio)
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(44, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Practica de Laboratorio", jPanel2);

        jLabel7.setText("Eliga y Descarge los Ejercicios Resueltos");

        listaArchivoEjerciciosResueltos.setModel(listaArchivosModeloEjerciciosResueltos);
        jScrollPane5.setViewportView(listaArchivoEjerciciosResueltos);

        btnDescargarEjerciciosResueltos.setText("Descargar");
        btnDescargarEjerciciosResueltos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarEjerciciosResueltosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDescargarEjerciciosResueltos)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDescargarEjerciciosResueltos)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ejercicios Resueltos", jPanel3);

        jLabel8.setText("Eliga y Descarge la tarea");

        listaArchivoTarea.setModel(listaArchivosModeloTarea);
        jScrollPane6.setViewportView(listaArchivoTarea);

        btnDescargarTarea.setText("Descargar");
        btnDescargarTarea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarTareaActionPerformed(evt);
            }
        });

        jButton1.setText("Subir Tarea");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDescargarTarea)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDescargarTarea)
                    .addComponent(jButton1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Tarea", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(0, 856, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(191, 191, 191)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGenerarReporteAsistencia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ckbxPantallaDocente))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTexto)
                        .addGap(2, 2, 2)
                        .addComponent(btnEnviar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCerrarSesion)
                        .addGap(4, 4, 4)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGenerarReporteAsistencia)
                            .addComponent(jCheckBox1)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(ckbxPantallaDocente)))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEnviar)
                    .addComponent(btnCerrarSesion))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 927, 676);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        enviarMensaje();
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void txtTextoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTextoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            enviarMensaje();
        }
    }//GEN-LAST:event_txtTextoKeyPressed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        clienteServidorChat.cerrarSesion();

        /* Cerrar pantalla, si tiene. */
        if (!esDocente) {
            pantallaProfesor.setVisible(false);
            pantallaProfesor.dispose();
        }

        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnDescargarDocumentoTeoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarDocumentoTeoriaActionPerformed
        descargarArchivo(listaArchivosDocumentosTeoria);
    }//GEN-LAST:event_btnDescargarDocumentoTeoriaActionPerformed

    private void btnGenerarReporteAsistenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerarReporteAsistenciaActionPerformed
        String defaultReport = "Asistencia.html";

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), defaultReport));
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            StringBuilder htmlAsistencia = new StringBuilder();
            Date fechaActual = new Date();

            htmlAsistencia.append("<html>");
            htmlAsistencia.append("<head><meta name=\"tipo_contenido\" content=\"text/html;\" http-equiv=\"content-type\" charset=\"utf-8\"><title>Reporte Asistencia - " + fechaActual + "</title></head>");
            htmlAsistencia.append("<body>");
            htmlAsistencia.append("<div align=\"center\">");
            htmlAsistencia.append("<h1>Reporte de asistencia sistema EducaUteciSystems</h1></br></br>");
            htmlAsistencia.append("<b>Creado en: </b>" + fechaActual + "</br></br>");
            htmlAsistencia.append("<table border=\"1\" width=\"500px\">");
            htmlAsistencia.append("<tr><td><b>Nombre Real</b></td><td><b>Apodo en Chat</b></td></tr>");
            synchronized (this) {
                for (UserChat usuario : usuarios) {
                    htmlAsistencia.append("<tr><td>" + usuario.getRealName() + "</td><td>" + usuario.getNickName() + "</td></tr>");
                }
            }
            htmlAsistencia.append("</table>");
            htmlAsistencia.append("</div>");
            htmlAsistencia.append("</body>");
            htmlAsistencia.append("</html>");

            /* Borrar si se tiene ptoblemas. */
            if (archivo.exists()) {
                if (!archivo.delete()) {
                    mostrarError("No se pudo guardar resporte de asistencia.");
                    return;
                }
            }

            /* Guardar archivo. */
            try {
                FileOutputStream fos = new FileOutputStream(archivo);
                fos.write(htmlAsistencia.toString().getBytes());
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                mostrarError("No se ha encontrado la ruta para guardar.");
                return;
            } catch (IOException ioe) {
                mostrarError("Error de estrada/salida al escribir archivo.");
                return;
            }
            Sistema.mostrarMensajeInformativo("Se ha guardado información con éxito.");
        }
    }//GEN-LAST:event_btnGenerarReporteAsistenciaActionPerformed

    private void btnDescargarPracticaLaboratorioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarPracticaLaboratorioActionPerformed
        descargarArchivo(listaArchivoPracticaLaboratorio);
    }//GEN-LAST:event_btnDescargarPracticaLaboratorioActionPerformed

    private void btnDescargarEjerciciosResueltosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarEjerciciosResueltosActionPerformed
        descargarArchivo(listaArchivoEjerciciosResueltos);
    }//GEN-LAST:event_btnDescargarEjerciciosResueltosActionPerformed

    private void btnDescargarTareaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarTareaActionPerformed
        descargarArchivo(listaArchivoTarea);
    }//GEN-LAST:event_btnDescargarTareaActionPerformed

    private void ckbxPantallaDocenteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ckbxPantallaDocenteStateChanged
        ServidorChat.mostrarPantallaDocente = ckbxPantallaDocente.isSelected();
    }//GEN-LAST:event_ckbxPantallaDocenteStateChanged
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
              // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnDescargarDocumentoTeoria;
    private javax.swing.JButton btnDescargarEjerciciosResueltos;
    private javax.swing.JButton btnDescargarPracticaLaboratorio;
    private javax.swing.JButton btnDescargarTarea;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnGenerarReporteAsistencia;
    private javax.swing.JCheckBox ckbxPantallaDocente;
    private javax.swing.JEditorPane contenidoChat;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JList listaArchivoEjerciciosResueltos;
    private javax.swing.JList listaArchivoPracticaLaboratorio;
    private javax.swing.JList listaArchivoTarea;
    private javax.swing.JList listaArchivosDocumentosTeoria;
    private javax.swing.JEditorPane txtListaUsuarios;
    private javax.swing.JTextField txtTexto;
    // End of variables declaration//GEN-END:variables
}
