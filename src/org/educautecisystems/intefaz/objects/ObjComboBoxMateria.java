/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.objects;

import org.educautecisystems.entidades.Materia;

/**
 *
 * @author Shadow2012
 */
public class ObjComboBoxMateria {
    private Materia materia;
    
    public ObjComboBoxMateria() {
        materia = null;
    }
    
    public ObjComboBoxMateria(Materia materia) {
        this.materia = materia;
    }
    
    @Override
    public String toString() {
        return materia.getNombre();
    }

    /**
     * @return the materia
     */
    public Materia getMateria() {
        return materia;
    }

    /**
     * @param materia the materia to set
     */
    public void setMateria(Materia materia) {
        this.materia = materia;
    }
}
