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
public class EstudiantePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "Id_Estudiante")
    private int idEstudiante;
    @Basic(optional = false)
    @Column(name = "Modalidad_Id_Modalidad")
    private int modalidadIdModalidad;
    @Basic(optional = false)
    @Column(name = "Nivel_Id_Nivel")
    private int nivelIdNivel;

    public EstudiantePK() {
    }

    public EstudiantePK(int idEstudiante, int modalidadIdModalidad, int nivelIdNivel) {
        this.idEstudiante = idEstudiante;
        this.modalidadIdModalidad = modalidadIdModalidad;
        this.nivelIdNivel = nivelIdNivel;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getModalidadIdModalidad() {
        return modalidadIdModalidad;
    }

    public void setModalidadIdModalidad(int modalidadIdModalidad) {
        this.modalidadIdModalidad = modalidadIdModalidad;
    }

    public int getNivelIdNivel() {
        return nivelIdNivel;
    }

    public void setNivelIdNivel(int nivelIdNivel) {
        this.nivelIdNivel = nivelIdNivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEstudiante;
        hash += (int) modalidadIdModalidad;
        hash += (int) nivelIdNivel;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstudiantePK)) {
            return false;
        }
        EstudiantePK other = (EstudiantePK) object;
        if (this.idEstudiante != other.idEstudiante) {
            return false;
        }
        if (this.modalidadIdModalidad != other.modalidadIdModalidad) {
            return false;
        }
        if (this.nivelIdNivel != other.nivelIdNivel) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.EstudiantePK[ idEstudiante=" + idEstudiante + ", modalidadIdModalidad=" + modalidadIdModalidad + ", nivelIdNivel=" + nivelIdNivel + " ]";
    }
    
}
