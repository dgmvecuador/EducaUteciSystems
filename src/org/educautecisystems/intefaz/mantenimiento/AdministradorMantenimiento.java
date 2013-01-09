/*
 *  ChatServerInterface.java
 *  Copyright (C) 2012  Guillermo Pazos <shadowguiller@gmail.com>
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
