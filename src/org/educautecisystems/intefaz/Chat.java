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
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.Timer;
import org.educautecisystems.core.Sistema;
import org.educautecisystems.core.chat.cliente.ClienteServidorChat;
import org.educautecisystems.core.chat.elements.FileChat;
import org.educautecisystems.core.chat.elements.UserChat;

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
    DefaultListModel listaArchivosModelo = new DefaultListModel();
    private long actualSize = 0;
    private long actualSizeListaUsuarios = 0;
    private long actualSizeListaArchivos = 0;
    private final PantallaProfesor pantallaProfesor = new PantallaProfesor(this);
    private BufferedImage pantallaActual = null;

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

                /* Exconcer los nombres cuando no son docentes. */
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
            StringBuilder comprobador = new StringBuilder();

            for (FileChat fileChat : archivos) {
                comprobador.append(fileChat.toString());
            }

            /* No actualizar la lista si no es necesario. */
            if (actualSizeListaArchivos == comprobador.toString().length()) {
                return;
            }

            listaArchivosModelo.clear();

            for (FileChat fileChat : archivos) {
                listaArchivosModelo.addElement(fileChat);
            }
            actualSizeListaArchivos = comprobador.toString().length();
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
        jCheckBox1 = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        listaArchivosDocumentosTeoria = new javax.swing.JList();
        btnDescargar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        listaArchivo1 = new javax.swing.JList();
        btnDescargar1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        btnDescargar2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        btnDescargar3 = new javax.swing.JButton();

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

        jCheckBox1.setText("Mostrar Pantalla Docente");

        jLabel5.setText("Eliga y Descarge la teoria");

        listaArchivosDocumentosTeoria.setModel(listaArchivosModelo);
        listaArchivosDocumentosTeoria.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(listaArchivosDocumentosTeoria);

        btnDescargar.setText("Descargar");
        btnDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarActionPerformed(evt);
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
                .addComponent(btnDescargar)
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
                .addComponent(btnDescargar)
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(44, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Documentos Teoria", jPanel1);

        jLabel6.setText("Eliga y Descarge la practica de laboratorio");

        jScrollPane4.setViewportView(listaArchivo1);

        btnDescargar1.setText("Descargar");
        btnDescargar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargar1ActionPerformed(evt);
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
                .addComponent(btnDescargar1)
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
                .addComponent(btnDescargar1)
                .addContainerGap())
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(31, 31, 31)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(44, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Practica de Laboratorio", jPanel2);

        jLabel7.setText("Eliga y Descarge los Ejercicios Resueltos");

        jScrollPane5.setViewportView(jList2);

        btnDescargar2.setText("Descargar");
        btnDescargar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargar2ActionPerformed(evt);
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
                        .addComponent(btnDescargar2)))
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
                .addComponent(btnDescargar2)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Ejercicios Resueltos", jPanel3);

        jLabel8.setText("Eliga y Descarge la tarea");

        jScrollPane6.setViewportView(jList3);

        btnDescargar3.setText("Descargar");
        btnDescargar3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargar3ActionPerformed(evt);
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
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDescargar3)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDescargar3)
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(jTabbedPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(191, 191, 191)
                                .addComponent(jLabel3))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnGenerarReporteAsistencia)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtTexto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEnviar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCerrarSesion)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTexto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCerrarSesion)
                    .addComponent(btnEnviar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 927, 699);
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

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed
        FileChat fileChat = (FileChat) listaArchivosDocumentosTeoria.getSelectedValue();

        /* Revisar si existe algún elemento seleccionado. */
        if (fileChat == null) {
            Sistema.mostrarMensajeError("Por favor seleccione un archivo.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), fileChat.getName()));
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            clienteServidorChat.descargarArchivo(fileChat, archivo);
        }
    }//GEN-LAST:event_btnDescargarActionPerformed

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

    private void btnDescargar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargar1ActionPerformed
           FileChat fileChat = (FileChat) listaArchivosDocumentosTeoria.getSelectedValue();

        /* Revisar si existe algún elemento seleccionado. */
        if (fileChat == null) {
            Sistema.mostrarMensajeError("Por favor seleccione un archivo.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), fileChat.getName()));
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            clienteServidorChat.descargarArchivo(fileChat, archivo);
        }
    }//GEN-LAST:event_btnDescargar1ActionPerformed

    private void btnDescargar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargar2ActionPerformed
        FileChat fileChat = (FileChat) listaArchivosDocumentosTeoria.getSelectedValue();

        /* Revisar si existe algún elemento seleccionado. */
        if (fileChat == null) {
            Sistema.mostrarMensajeError("Por favor seleccione un archivo.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), fileChat.getName()));
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            clienteServidorChat.descargarArchivo(fileChat, archivo);
        }
    }//GEN-LAST:event_btnDescargar2ActionPerformed

    private void btnDescargar3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargar3ActionPerformed
         FileChat fileChat = (FileChat) listaArchivosDocumentosTeoria.getSelectedValue();

        /* Revisar si existe algún elemento seleccionado. */
        if (fileChat == null) {
            Sistema.mostrarMensajeError("Por favor seleccione un archivo.");
            return;
        }

        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Seleccione donde guardar el archivo.");
        fc.setSelectedFile(new File(fc.getCurrentDirectory(), fileChat.getName()));
        int respuesta = fc.showSaveDialog(this);

        if (respuesta == JFileChooser.APPROVE_OPTION) {
            File archivo = fc.getSelectedFile();
            clienteServidorChat.descargarArchivo(fileChat, archivo);
        }
    }//GEN-LAST:event_btnDescargar3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnDescargar;
    private javax.swing.JButton btnDescargar1;
    private javax.swing.JButton btnDescargar2;
    private javax.swing.JButton btnDescargar3;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnGenerarReporteAsistencia;
    private javax.swing.JEditorPane contenidoChat;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
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
    private javax.swing.JList listaArchivo1;
    private javax.swing.JList listaArchivosDocumentosTeoria;
    private javax.swing.JEditorPane txtListaUsuarios;
    private javax.swing.JTextField txtTexto;
    // End of variables declaration//GEN-END:variables
}
