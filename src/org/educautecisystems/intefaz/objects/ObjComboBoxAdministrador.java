/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.intefaz.objects;

import org.educautecisystems.entidades.Administrador;

/**
 *
 * @author Shadow2012
 */
public class ObjComboBoxAdministrador {
       private Administrador administrador;
    
    public ObjComboBoxAdministrador() {
        administrador = null;
    }
    
    public ObjComboBoxAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
    
    @Override
    public String toString() {
        return administrador.getUsuario();
    }

    /**
     * @return the Administrador
     */
    public Administrador getAdministrador() {
        return administrador;
    }

    /**
     * @param administrador the administrador to set
     */
    public void setAdministrador(Administrador administrador) {
        this.administrador = administrador;
    }
}
