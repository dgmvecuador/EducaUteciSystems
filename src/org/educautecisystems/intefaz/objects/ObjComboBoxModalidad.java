/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.objects;
import org.educautecisystems.entidades.Modalidad;
/**
 *
 * @author Shadow2013
 */
public class ObjComboBoxModalidad {
    private Modalidad modalidad;
    
    public ObjComboBoxModalidad() {
        modalidad = null;
    }
    
    public ObjComboBoxModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }
    
    @Override
    public String toString() {
        return modalidad.getNombre();
    }

    /**
     * @return the materia
     */
    public Modalidad getMateria() {
        return modalidad;
    }

    /**
     * @param materia the materia to set
     */
    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }
}
