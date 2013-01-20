/*
 *  FacultadMantenimiento.java
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

import org.educautecisystems.entidades.Facultad;

/**
 *
 * @author Shadow2012
 */
public class FacultadMantenimiento {
    private Facultad facultad;
    
    public FacultadMantenimiento() {
        this.facultad = null;
    }
    
    public FacultadMantenimiento(Facultad facultad) {
        this.facultad = facultad;
    }
    
    @Override
    public String toString () {
        return this.getFacultad().getNombre();
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
