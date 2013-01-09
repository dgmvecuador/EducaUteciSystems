/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.mantenimiento;

import org.educautecisystems.entidades.Administrador;

/**
 *
 * @author Shadow2012
 */
public class AdministradorMantenimiento {
     private Administrador administrador;
    
    public AdministradorMantenimiento() {
        this.administrador = null;
    }
    
    public AdministradorMantenimiento(Administrador administrador) {
        this.administrador = administrador;
    }
    
    @Override
    public String toString () {
        return this.getAdministrador().getUsuario();
    }

    /**
     * @return the administrador
     */
    public Administrador getAdministrador() {
        return administrador;
    }

    /**
     * @param administrador the docente to set
     */
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
}
