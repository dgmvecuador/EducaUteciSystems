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
public class ClasePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "Id_Clase")
    private int idClase;
    @Basic(optional = false)
    @Column(name = "Materia_Id_Materia")
    private int materiaIdMateria;

    public ClasePK() {
    }

    public ClasePK(int idClase, int materiaIdMateria) {
        this.idClase = idClase;
        this.materiaIdMateria = materiaIdMateria;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getMateriaIdMateria() {
        return materiaIdMateria;
    }

    public void setMateriaIdMateria(int materiaIdMateria) {
        this.materiaIdMateria = materiaIdMateria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idClase;
        hash += (int) materiaIdMateria;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClasePK)) {
            return false;
        }
        ClasePK other = (ClasePK) object;
        if (this.idClase != other.idClase) {
            return false;
        }
        if (this.materiaIdMateria != other.materiaIdMateria) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.ClasePK[ idClase=" + idClase + ", materiaIdMateria=" + materiaIdMateria + " ]";
    }
    
}
