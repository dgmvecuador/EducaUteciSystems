/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.objects;

import org.educautecisystems.entidades.Facultad;


/**
 *
 * @author Shadow2012
 */
public class ObjComboBoxFacultad {
     private Facultad facultad;
    
    public ObjComboBoxFacultad() {
        facultad = null;
    }
    
    public ObjComboBoxFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
    
    @Override
    public String toString() {
        return facultad.getNombre();
    }

    /**
     * @return the facultad
     */
    public Facultad getFacultad() {
        return facultad;
    }

    /**
     * @param facultad the facultad to set
     */
    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
}