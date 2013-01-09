/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.mantenimiento;

import org.educautecisystems.entidades.Docente;

/**
 *
 * @author Shadow2012
 */
public class DocenteMantenimiento {
    private Docente docente;
    
    public DocenteMantenimiento() {
        this.docente = null;
    }
    
    public DocenteMantenimiento(Docente docente) {
        this.docente = docente;
    }
    
    @Override
    public String toString () {
        return this.getDocente().getUsuario();
    }

    /**
     * @return the docente
     */
    public Docente getDocente() {
        return docente;
    }

    /**
     * @param docente the docente to set
     */
    public void setDocente(Docente docente) {
        this.docente = docente;
    }
}
