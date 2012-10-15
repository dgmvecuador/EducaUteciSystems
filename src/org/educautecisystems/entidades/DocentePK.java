/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Shadow2012
 */
@Embeddable
public class DocentePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "Id_Docente")
    private int idDocente;
    @Basic(optional = false)
    @Column(name = "Id_Materia")
    private int idMateria;

    public DocentePK() {
    }

    public DocentePK(int idDocente, int idMateria) {
        this.idDocente = idDocente;
        this.idMateria = idMateria;
    }

    public int getIdDocente() {
        return idDocente;
    }

    public void setIdDocente(int idDocente) {
        this.idDocente = idDocente;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDocente;
        hash += (int) idMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DocentePK)) {
            return false;
        }
        DocentePK other = (DocentePK) object;
        if (this.idDocente != other.idDocente) {
            return false;
        }
        if (this.idMateria != other.idMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.DocentePK[ idDocente=" + idDocente + ", idMateria=" + idMateria + " ]";
    }
    
}
