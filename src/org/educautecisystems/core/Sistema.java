/*
 *  Sistema.java
 *  Copyright (C) 2012  Guillermo Pazos <shadowguiller@hotmail.com>
 *  Copyright (C) 2013  Diego Estévez <dgmvecuador@gmail.com>
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
package org.educautecisystems.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JOptionPane;
import org.educautecisystems.core.chat.elements.FileChat;
import org.educautecisystems.core.config.ChatServerConf;
import org.educautecisystems.core.config.ChatSessionConf;
import org.educautecisystems.intefaz.Ingreso;
import org.educautecisystems.intefaz.VentanaPrincipal;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author Shadow2012
 */
public class Sistema {

    private static EntityManagerFactory emf = null;
    private static final ConfBaseDeDatos confBaseDeDatos = new ConfBaseDeDatos();
    private static final String GENERAL_CONF_XML = "config.xml";
    private static String pathGeneralConf = null;

    /* Valores por defecto de la configuración principal */
    public static final String g_host_defecto = "localhost";
    public static final String g_port_defecto = "3306";
    public static final String g_user_defecto = "root";
    public static final String g_password_defecto = "admin";
    public static final String g_esquema_defecto = "mydb";

    /* Constantes */
    public static final int VERSION_MAYOR = 1;
    public static final int VERSION_MENOR = 0;
    public static final int VERSION_PARCHE = 0;
    public static final String NOMBRE_PROGRAMA = "EducaUteciSystems";

    public static String dameVersionCompleta() {
        return NOMBRE_PROGRAMA + "-" + VERSION_MAYOR + "." + VERSION_MENOR + "." + VERSION_PARCHE;
    }

    public static final String NOMBRE_CARPETA_CONFIGURACION = "EducaUteciSystems";
    public static final String NOMBRE_CARPETA_CONF_CHAT = "Chat";
    public static final String NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS = "Compartido";
    public static final String NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_DOCUMENTOS_TEORIA = "DocumentosTeoria";
    public static final String NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_PRACTICA_LABORATORIO = "PracticaLaboratorio";
    public static final String NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_EJERCICIOS_RESUELTOS = "EjerciciosResueltos";
    public static final String NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA = "Tarea";
    public static final String NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREAS_SUBIDAS = "TareaSubidas";
    public static final String CHAT_CONF_XML = "ChatServerConf.xml";

    /* Configuración de Chat */
    private static String pathChatConf = null;
    private static String pathShareFolder = null;
    private static ChatServerConf chatServerConf;
    private static ChatSessionConf chatSessionConf;
    private static String pathShareDocumentosTeoria = null;
    private static String pathSharePracticaLaborario = null;
    private static String pathShareEjerciciosResueltos = null;
    private static String pathShareTarea = null;
    private static String pathShareTareasSubidos = null;

    /* Archivo por defecto */
    private static final String ip_defecto = "localhost";
    private static final String port_defecto = "7586";
    private static final String nickname_defecto = "nLastName";
    private static final String realName_defecto = "Name LastName";

    public static void main(String[] args) {
        seleccionadoLookAndFeel();
        cargarCarpeta();
        inicializarSistema(confBaseDeDatos.getUser(), confBaseDeDatos.getPassword(),
                confBaseDeDatos.getHost(), confBaseDeDatos.getPort(),
                confBaseDeDatos.getEsquema());
        new VentanaPrincipal().setVisible(true);
    }

    private static String generarJDBC_URL(String host, String port, String esquema) {
        System.out.println("Se conecta con el servidor:");
        System.out.println("\tHost: " + host);
        System.out.println("\tPuerto: " + port);
        System.out.println("\tEsquema: " + esquema);
        System.out.println("\tBase de datos: MySQL");
        return "jdbc:mysql://" + host + ":" + port + "/" + esquema + "?zeroDateTimeBehavior=convertToNull";
    }

    private static void inicializarSistema(String usuario, String password, String host, String port, String esquema) {
        Map parametros = new HashMap();
        String url = generarJDBC_URL(host, port, esquema);
        parametros.put("javax.persistence.jdbc.url", url);
        parametros.put("javax.persistence.jdbc.password", password);
        parametros.put("javax.persistence.jdbc.user", usuario);
        
        /* Imprimir los datos de la conexión. */
        System.out.println("URL: "+url);
        System.out.println("Usuario: "+usuario);
        System.out.println("Password: ********");

        emf = Persistence.createEntityManagerFactory("EducaUteciSystemsPU", parametros);

        /* No se pudo detectar la base de datos */
        if (!emf.isOpen()) {
            System.err.println("No se pudo abrir la base de datos.");
            System.exit(-1);
        }
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

    public static String getMD5(String text) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
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

    public static void mostrarMensajeInformativo(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Información - " + dameVersionCompleta(), JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error - " + dameVersionCompleta(), JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean confirmarSiNoPregunta( String pregunta ) {
        int respuesta = JOptionPane.showConfirmDialog(null, pregunta, "", JOptionPane.YES_NO_OPTION);
        
        if ( respuesta == JOptionPane.YES_OPTION ) {
            return true;
        }
        
        return false;
    }

    private static void cargarCarpeta() {
        Properties propiedadesSistema = System.getProperties();
        String carpetaUsuario = propiedadesSistema.getProperty("user.home");

        /* Carpetas de configuraciones */
        File carpetaConfiguracion = new File(carpetaUsuario, NOMBRE_CARPETA_CONFIGURACION);
        File carpetaConfChat = new File(carpetaConfiguracion, NOMBRE_CARPETA_CONF_CHAT);
        File carpetaConfArchivos = new File(carpetaConfiguracion, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS);
        
        /* Materiales de apoyo */
        File carpetaConfArchivosDocumentosTeoria = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_DOCUMENTOS_TEORIA);
        File carpetaConfArchivosPracticaLaboratorio = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_PRACTICA_LABORATORIO);
        File carpetaConfArchivosEjercicioResueltos = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_EJERCICIOS_RESUELTOS);
        File carpetaConfArchivosTarea = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA);
        File carpetaConfArchivosTareasSubidas = new File(carpetaConfArchivos, NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREAS_SUBIDAS);

        /* Crear carpetas si no existen */
        if (!carpetaConfiguracion.exists()) {
            carpetaConfiguracion.mkdirs();
        }
        if (!carpetaConfChat.exists()) {
            carpetaConfChat.mkdirs();
        }
        if (!carpetaConfArchivos.exists()) {
            carpetaConfArchivos.mkdirs();
        }
        
        /* Añadir las nuevas carpetas, si existen */
        if (!carpetaConfArchivosDocumentosTeoria.exists()) {
            carpetaConfArchivosDocumentosTeoria.mkdirs();
        }
        if (!carpetaConfArchivosPracticaLaboratorio.exists()) {
            carpetaConfArchivosPracticaLaboratorio.mkdirs();
        }
        if (!carpetaConfArchivosEjercicioResueltos.exists()) {
            carpetaConfArchivosEjercicioResueltos.mkdirs();
        }
        if (!carpetaConfArchivosTarea.exists()) {
            carpetaConfArchivosTarea.mkdirs();
        }
        if (!carpetaConfArchivosTareasSubidas.exists()) {
            carpetaConfArchivosTareasSubidas.mkdirs();
        }

        /* Archivos de configuración */
        File archivoConfChatXML = new File(carpetaConfChat, CHAT_CONF_XML);
        File archivoConfPrincipal = new File(carpetaConfiguracion, GENERAL_CONF_XML);
        pathChatConf = archivoConfChatXML.getAbsolutePath();
        pathGeneralConf = archivoConfPrincipal.getAbsolutePath();
        pathShareFolder = carpetaConfArchivos.getAbsolutePath();
        
        pathShareDocumentosTeoria = carpetaConfArchivosDocumentosTeoria.getAbsolutePath();
        pathSharePracticaLaborario = carpetaConfArchivosPracticaLaboratorio.getAbsolutePath();
        pathShareEjerciciosResueltos = carpetaConfArchivosEjercicioResueltos.getAbsolutePath();
        pathShareTarea = carpetaConfArchivosTarea.getAbsolutePath();
        pathShareTareasSubidos = carpetaConfArchivosTareasSubidas.getAbsolutePath();

        if (archivoConfChatXML.exists() && archivoConfChatXML.isFile()) {
            cargarChatConf(archivoConfChatXML);
        } else {
            generarChatConf(archivoConfChatXML);
        }

        if (archivoConfPrincipal.exists() && archivoConfPrincipal.isFile()) {
            cargarConfPrincipal(archivoConfPrincipal);
        } else {
            confBaseDeDatos.setHost(g_host_defecto);
            confBaseDeDatos.setPort(g_port_defecto);
            confBaseDeDatos.setUser(g_user_defecto);
            confBaseDeDatos.setPassword(g_password_defecto);
            confBaseDeDatos.setEsquema(g_esquema_defecto);
            guardarConfPrincipal();
        }
    }

    private static void cargarConfPrincipal(File archivoConfPrincipal) {
        SAXBuilder builder = new SAXBuilder();
        Document documento = null;

        try {
            documento = builder.build(archivoConfPrincipal);
        } catch (JDOMException jdome) {
            System.err.println("JDOME: " + jdome);
        } catch (IOException ioe) {
            System.err.println("IOE: " + ioe);
        }

        Namespace baseNamespace = Namespace.getNamespace("eus", "http://educautecisystems.org/");
        Element root = documento.getRootElement();

        /* Información de la base de datos. */
        Element eBaseDeDatos = root.getChild("database", baseNamespace);
        confBaseDeDatos.setHost(eBaseDeDatos.getChildText("host"));
        confBaseDeDatos.setPort(eBaseDeDatos.getChildText("port"));
        confBaseDeDatos.setUser(eBaseDeDatos.getChildText("user"));
        confBaseDeDatos.setPassword(eBaseDeDatos.getChildText("password"));
        confBaseDeDatos.setEsquema(eBaseDeDatos.getChildText("esquema"));
    }

    public static void guardarConfPrincipal() {
        File archivoConfPrincipal = new File(pathGeneralConf);

        if (archivoConfPrincipal.exists()) {
            archivoConfPrincipal.delete();
        }

        Document documento = new Document();

        Namespace baseNamespace = Namespace.getNamespace("eus", "http://educautecisystems.org/");
        Element root = new Element("config", baseNamespace);
        documento.setRootElement(root);

        Element eBaseDeDatos = new Element("database", baseNamespace);
        eBaseDeDatos.addContent(new Element("host").setText(confBaseDeDatos.getHost()));
        eBaseDeDatos.addContent(new Element("port").setText(confBaseDeDatos.getPort()));
        eBaseDeDatos.addContent(new Element("user").setText(confBaseDeDatos.getUser()));
        eBaseDeDatos.addContent(new Element("password").setText(confBaseDeDatos.getPassword()));
        eBaseDeDatos.addContent(new Element("esquema").setText(confBaseDeDatos.getEsquema()));
        root.addContent(eBaseDeDatos);

        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

        try {
            FileOutputStream fis = new FileOutputStream(archivoConfPrincipal);
            outputter.output(documento, fis);
            fis.close();
        } catch (IOException ioe) {
            System.err.println("No se pudo escribor configuración principal.");
        }
    }

    private static void cargarChatConf(File archivoConfChatXML) {
        ChatServerConf lChatServerConf = new ChatServerConf();
        ChatSessionConf lChatSessionConf = new ChatSessionConf();

        SAXBuilder builder = new SAXBuilder();
        Document documento = null;

        try {
            documento = builder.build(archivoConfChatXML);
        } catch (JDOMException jdome) {
            System.err.println("JDOME: " + jdome);
        } catch (IOException ioe) {
            System.err.println("IOE: " + ioe);
        }

        Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
        Element root = documento.getRootElement();

        /* Datos del servidor */
        Element eServidor = root.getChild("server", baseNamespace);
        lChatServerConf.setIp(eServidor.getChildText("ip"));
        lChatServerConf.setPort(eServidor.getChildText("port"));

        /* Datos de la sesión */
        Element eSession = root.getChild("session", baseNamespace);
        lChatSessionConf.setNickname(eSession.getChildText("nickname"));
        lChatSessionConf.setRealName(eSession.getChildText("real_name"));

        /* Guardar información */
        Sistema.chatServerConf = lChatServerConf;
        Sistema.chatSessionConf = lChatSessionConf;
    }

    private static void generarChatConf(File archivoConfChatXML) {
        Document document = new Document();

        Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
        Element root = new Element("config", baseNamespace);

        /* Datos servidor */
        Element eServidor = new Element("server", baseNamespace);
        eServidor.addContent(new Element("ip").setText(ip_defecto));
        eServidor.addContent(new Element("port").setText(port_defecto));
        root.addContent(eServidor);

        /* Datos sesión */
        Element eSession = new Element("session", baseNamespace);
        eSession.addContent(new Element("nickname").setText(nickname_defecto));
        eSession.addContent(new Element("real_name").setText(realName_defecto));
        root.addContent(eSession);

        /* Guardar archivo */
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        document.setRootElement(root);

        try {
            outputter.output(document, new FileOutputStream(archivoConfChatXML));
        } catch (IOException ioe) {
            System.err.println("No se puedo crear archivo de configuración.");
        }

        /* Iniciar información */
        chatServerConf = new ChatServerConf(ip_defecto, port_defecto);
        chatSessionConf = new ChatSessionConf(nickname_defecto, realName_defecto);
    }

    public static void guardarChatConf() {
        File archivoConfChatXML = new File(pathChatConf);

        /* Borrar archivo, si existe. */
        if (archivoConfChatXML.exists()) {
            archivoConfChatXML.delete();
        }

        Document document = new Document();

        Namespace baseNamespace = Namespace.getNamespace("chat", "http://free.chat.com/");
        Element root = new Element("config", baseNamespace);

        /* Datos servidor */
        Element eServidor = new Element("server", baseNamespace);
        eServidor.addContent(new Element("ip").setText(chatServerConf.getIp()));
        eServidor.addContent(new Element("port").setText(chatServerConf.getPort()));
        root.addContent(eServidor);

        /* Datos sesión */
        Element eSession = new Element("session", baseNamespace);
        eSession.addContent(new Element("nickname").setText(chatSessionConf.getNickname()));
        eSession.addContent(new Element("real_name").setText(chatSessionConf.getRealName()));
        root.addContent(eSession);

        /* Guardar archivo */
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        document.setRootElement(root);

        try {
            outputter.output(document, new FileOutputStream(archivoConfChatXML));
        } catch (IOException ioe) {
            System.err.println("No se puedo crear archivo de configuración.");
        }
    }

    /**
     * @return the chatServerConf
     */
    public static ChatServerConf getChatServerConf() {
        return chatServerConf;
    }

    /**
     * @return the chatSessionConf
     */
    public static ChatSessionConf getChatSessionConf() {
        return chatSessionConf;
    }

    public static ArrayList<FileChat> getFileChatList() {
        ArrayList<FileChat> files = new ArrayList<FileChat>();
        
        File folderShareDocumentosTeoria = new File(pathShareDocumentosTeoria);
        File folderSharePracticaLaboratorio = new File(pathSharePracticaLaborario);
        File folderShareEjerciciosResueltos = new File(pathShareEjerciciosResueltos);
        File folderShareTarea = new File(pathShareTarea);
        
        File[] filesRawDocumentosTeoria = folderShareDocumentosTeoria.listFiles();
        File[] filesRawPracticaLaboratorio = folderSharePracticaLaboratorio.listFiles();
        File[] filesRawEjerciciosResueltos = folderShareEjerciciosResueltos.listFiles();
        File[] filesRawTarea = folderShareTarea.listFiles();

        for (File fileInFolder : filesRawDocumentosTeoria) {
            if (fileInFolder.isFile() && fileInFolder.canRead()) {
                FileChat fileChat = new FileChat();
                fileChat.setName(fileInFolder.getName());
                fileChat.setSize(fileInFolder.length());
                fileChat.setHidden(fileInFolder.isHidden());
                fileChat.setTipo(FileChat.TIPO_DOCUMENTO_TEORIA);
                files.add(fileChat);
            }
        }
        
        for (File fileInFolder : filesRawPracticaLaboratorio) {
            if (fileInFolder.isFile() && fileInFolder.canRead()) {
                FileChat fileChat = new FileChat();
                fileChat.setName(fileInFolder.getName());
                fileChat.setSize(fileInFolder.length());
                fileChat.setHidden(fileInFolder.isHidden());
                fileChat.setTipo(FileChat.TIPO_PRACTICA_LABORATORIO);
                files.add(fileChat);
            }
        }
        
        for (File fileInFolder : filesRawEjerciciosResueltos) {
            if (fileInFolder.isFile() && fileInFolder.canRead()) {
                FileChat fileChat = new FileChat();
                fileChat.setName(fileInFolder.getName());
                fileChat.setSize(fileInFolder.length());
                fileChat.setHidden(fileInFolder.isHidden());
                fileChat.setTipo(FileChat.TIPO_EJERCICIOS_RESUELTOS);
                files.add(fileChat);
            }
        }
        
        for (File fileInFolder : filesRawTarea) {
            if (fileInFolder.isFile() && fileInFolder.canRead()) {
                FileChat fileChat = new FileChat();
                fileChat.setName(fileInFolder.getName());
                fileChat.setSize(fileInFolder.length());
                fileChat.setHidden(fileInFolder.isHidden());
                fileChat.setTipo(FileChat.TIPO_TAREA);
                files.add(fileChat);
            }
        }

        return files;
    }

    public static File getShareFolder( String type ) {
        String subfolder = "";
        
        /* Colocar la carpeta correcta. */
        if ( type.equals(FileChat.TIPO_DOCUMENTO_TEORIA) ) {
            subfolder = NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_DOCUMENTOS_TEORIA;
        } else if ( type.equals(FileChat.TIPO_PRACTICA_LABORATORIO) ) {
            subfolder = NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_PRACTICA_LABORATORIO;
        } else if ( type.equals(FileChat.TIPO_EJERCICIOS_RESUELTOS) ) {
            subfolder = NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_EJERCICIOS_RESUELTOS;
        } else if ( type.equals(FileChat.TIPO_TAREA) ) {
            subfolder = NOMBRE_CARPETA_CONF_ARCHIVOS_COMPARTIDOS_TAREA;
        }
        
        return new File(pathShareFolder, subfolder);
    }
    
    public static String getFolderTareasSubidas() {
        return pathShareTareasSubidos;
    }
    
    public static File comprobarTareaCompartida( String fileName, String realName ) {
        File [] compartidos = new File(pathShareTareasSubidos).listFiles();
        
        /* Comprobar si coincide el nombre y el que compartio el archivo. */
        for ( File archivoLocal:compartidos ) {
            String nombreArchivo = archivoLocal.getName().replaceAll("(.*?) - (.*?) - (.*)","$3");
            String nombreReal = archivoLocal.getName().replaceAll("(.*?) - (.*?) - (.*)","$1");
            
            if ( nombreArchivo.equals(fileName) && nombreReal.equals(realName) ) {
                return archivoLocal;
            }
        }
        
        return null;
    }
    
    public static String generarNombreArchivoCompartido( String fileName, String realName ) {
        Calendar fechaActual = Calendar.getInstance();
        String fecha = String.format("%02d%02d%02d%02d%02d",
                fechaActual.get(Calendar.DAY_OF_MONTH),
                fechaActual.get(Calendar.MONTH)+1,
                fechaActual.get(Calendar.YEAR),
                fechaActual.get(Calendar.HOUR_OF_DAY),
                fechaActual.get(Calendar.MINUTE));
        
        return realName+" - "+fecha+" - "+fileName;
    }
}
