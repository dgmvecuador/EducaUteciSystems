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
        return archivo.getName();
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
