/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.objects;

import org.educautecisystems.entidades.Docente;

/**
 *
 * @author Shadow2012
 */
public class ObjComboBoxDocente {
     private Docente docente;
    
    public ObjComboBoxDocente() {
        docente = null;
    }
    
    public ObjComboBoxDocente(Docente docente) {
        this.docente = docente;
    }
    
    @Override
    public String toString() {
        return docente.getUsuario();
    }

    /**
     * @return the docente
     */
    public Docente getAdministrador() {
        return docente;
    }

    /**
     * @param docente the docente to set
     */
    public void setAdministrador(Docente docente) {
        this.docente = docente;
    }
}
