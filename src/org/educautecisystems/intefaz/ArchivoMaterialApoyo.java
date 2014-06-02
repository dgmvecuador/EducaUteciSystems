/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.educautecisystems.intefaz;

import java.io.File;

/**
 *
 * @author dgmv
 */
public class ArchivoMaterialApoyo {
    private File archivo;
    
    public ArchivoMaterialApoyo( File archivo ) {
        this.archivo = archivo;
    }

    /**
     * Get the value of archivo
     *
     * @return the value of archivo
     */
    public File getArchivo() {
        return archivo;
    }

    /**
     * Set the value of archivo
     *
     * @param archivo new value of archivo
     */
    public void setArchivo(File archivo) {
        this.archivo = archivo;
    }
    
    @Override
    public String toString() {
        if ( archivo == null ) {
            return "";
        }
        return "<html><b>["+devolverTipo(archivo.getName().toLowerCase())+"]</b> - <font color=\"black\">"+archivo.getName()+"</font></html>";
    }
    
    private String devolverTipo( String nombreArchivo ) {
        if ( nombreArchivo.endsWith(".mp4") || nombreArchivo.endsWith(".avi") || 
                nombreArchivo.endsWith(".flv") || nombreArchivo.endsWith(".mpeg") ||
                nombreArchivo.endsWith(".webm") || nombreArchivo.endsWith(".mpg") ||
                nombreArchivo.endsWith(".mov") || nombreArchivo.endsWith(".mkv") ||
                nombreArchivo.endsWith(".ogv") || nombreArchivo.endsWith(".wmv")) {
            return "Video";
        }
        
        if ( nombreArchivo.endsWith(".mp3") || nombreArchivo.endsWith(".mp2") ||
                nombreArchivo.endsWith(".wav") || nombreArchivo.endsWith(".ogg") ||
                nombreArchivo.endsWith(".aac")) {
            return "Audio";
        }
        
        if ( nombreArchivo.endsWith(".png") || nombreArchivo.endsWith(".jpg") ||
                nombreArchivo.endsWith(".jpeg") || nombreArchivo.endsWith(".gif") ||
                nombreArchivo.endsWith(".bmp")) {
            return "Imagen";
        }
        
        if ( nombreArchivo.endsWith(".doc") || nombreArchivo.endsWith(".docx") ) {
            return "Documento de Word";
        }
        
        if ( nombreArchivo.endsWith(".ppt") || nombreArchivo.endsWith(".pptx") ) {
            return "Documento de Power Point";
        }
        
        if ( nombreArchivo.endsWith(".xls") || nombreArchivo.endsWith(".xlsx") ) {
            return "Hoja de calculo excel";
        }
        
        if ( nombreArchivo.endsWith(".exe") ) {
            return "Archivo Ejecutable";
        }
        
        if ( nombreArchivo.endsWith(".pdf") ) {
            return "Documento PDF";
        }
         
        if ( nombreArchivo.endsWith(".java") ) {
            return "Fuente Java";
        }
        
        if ( nombreArchivo.endsWith(".cpp") ) {
            return "Fuente C++";
        }
        
        if ( nombreArchivo.endsWith(".cs") ) {
            return "Fuente C#";
        }
        
        if ( nombreArchivo.endsWith(".c") ) {
            return "Fuente C";
        }
        
        if ( nombreArchivo.endsWith(".sql") ) {
            return "Fuente SQL";
        }
        
        if ( nombreArchivo.endsWith(".jar") ) {
            return "Binario Java";
        }
        
        if ( nombreArchivo.endsWith(".zip") ) {
            return "Comprimido ZIP";
        }
        
        if ( nombreArchivo.endsWith(".rar") ) {
            return "Comprimido RAR";
        }
        
        if ( nombreArchivo.endsWith(".7z") ) {
            return "Comprimido 7zip";
        }
            
        return "Desconocido";
    }
    
    @Override
    public boolean equals(Object obj) {
        return ((ArchivoMaterialApoyo) obj).archivo.getAbsolutePath().equals(this.archivo.getAbsolutePath());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.archivo != null ? this.archivo.hashCode() : 0);
        return hash;
    }
    
    
}
