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
public class ClaseHasEstudiantePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "Clase_Id_Clase")
    private int claseIdClase;
    @Basic(optional = false)
    @Column(name = "Clase_Docente_has_Facultad_Docente_Id_Docente")
    private int claseDocentehasFacultadDocenteIdDocente;
    @Basic(optional = false)
    @Column(name = "Clase_Docente_has_Facultad_Facultad_Id_Facultad")
    private int claseDocentehasFacultadFacultadIdFacultad;
    @Basic(optional = false)
    @Column(name = "Clase_Materia_Id_Materia")
    private int claseMateriaIdMateria;
    @Basic(optional = false)
    @Column(name = "Estudiante_Id_Estudiante")
    private int estudianteIdEstudiante;
    @Basic(optional = false)
    @Column(name = "Estudiante_Modalidad_Id_Modalidad")
    private int estudianteModalidadIdModalidad;
    @Basic(optional = false)
    @Column(name = "Estudiante_Nivel_Id_Nivel")
    private int estudianteNivelIdNivel;

    public ClaseHasEstudiantePK() {
    }

    public ClaseHasEstudiantePK(int claseIdClase, int claseDocentehasFacultadDocenteIdDocente, int claseDocentehasFacultadFacultadIdFacultad, int claseMateriaIdMateria, int estudianteIdEstudiante, int estudianteModalidadIdModalidad, int estudianteNivelIdNivel) {
        this.claseIdClase = claseIdClase;
        this.claseDocentehasFacultadDocenteIdDocente = claseDocentehasFacultadDocenteIdDocente;
        this.claseDocentehasFacultadFacultadIdFacultad = claseDocentehasFacultadFacultadIdFacultad;
        this.claseMateriaIdMateria = claseMateriaIdMateria;
        this.estudianteIdEstudiante = estudianteIdEstudiante;
        this.estudianteModalidadIdModalidad = estudianteModalidadIdModalidad;
        this.estudianteNivelIdNivel = estudianteNivelIdNivel;
    }

    public int getClaseIdClase() {
        return claseIdClase;
    }

    public void setClaseIdClase(int claseIdClase) {
        this.claseIdClase = claseIdClase;
    }

    public int getClaseDocentehasFacultadDocenteIdDocente() {
        return claseDocentehasFacultadDocenteIdDocente;
    }

    public void setClaseDocentehasFacultadDocenteIdDocente(int claseDocentehasFacultadDocenteIdDocente) {
        this.claseDocentehasFacultadDocenteIdDocente = claseDocentehasFacultadDocenteIdDocente;
    }

    public int getClaseDocentehasFacultadFacultadIdFacultad() {
        return claseDocentehasFacultadFacultadIdFacultad;
    }

    public void setClaseDocentehasFacultadFacultadIdFacultad(int claseDocentehasFacultadFacultadIdFacultad) {
        this.claseDocentehasFacultadFacultadIdFacultad = claseDocentehasFacultadFacultadIdFacultad;
    }

    public int getClaseMateriaIdMateria() {
        return claseMateriaIdMateria;
    }

    public void setClaseMateriaIdMateria(int claseMateriaIdMateria) {
        this.claseMateriaIdMateria = claseMateriaIdMateria;
    }

    public int getEstudianteIdEstudiante() {
        return estudianteIdEstudiante;
    }

    public void setEstudianteIdEstudiante(int estudianteIdEstudiante) {
        this.estudianteIdEstudiante = estudianteIdEstudiante;
    }

    public int getEstudianteModalidadIdModalidad() {
        return estudianteModalidadIdModalidad;
    }

    public void setEstudianteModalidadIdModalidad(int estudianteModalidadIdModalidad) {
        this.estudianteModalidadIdModalidad = estudianteModalidadIdModalidad;
    }

    public int getEstudianteNivelIdNivel() {
        return estudianteNivelIdNivel;
    }

    public void setEstudianteNivelIdNivel(int estudianteNivelIdNivel) {
        this.estudianteNivelIdNivel = estudianteNivelIdNivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) claseIdClase;
        hash += (int) claseDocentehasFacultadDocenteIdDocente;
        hash += (int) claseDocentehasFacultadFacultadIdFacultad;
        hash += (int) claseMateriaIdMateria;
        hash += (int) estudianteIdEstudiante;
        hash += (int) estudianteModalidadIdModalidad;
        hash += (int) estudianteNivelIdNivel;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClaseHasEstudiantePK)) {
            return false;
        }
        ClaseHasEstudiantePK other = (ClaseHasEstudiantePK) object;
        if (this.claseIdClase != other.claseIdClase) {
            return false;
        }
        if (this.claseDocentehasFacultadDocenteIdDocente != other.claseDocentehasFacultadDocenteIdDocente) {
            return false;
        }
        if (this.claseDocentehasFacultadFacultadIdFacultad != other.claseDocentehasFacultadFacultadIdFacultad) {
            return false;
        }
        if (this.claseMateriaIdMateria != other.claseMateriaIdMateria) {
            return false;
        }
        if (this.estudianteIdEstudiante != other.estudianteIdEstudiante) {
            return false;
        }
        if (this.estudianteModalidadIdModalidad != other.estudianteModalidadIdModalidad) {
            return false;
        }
        if (this.estudianteNivelIdNivel != other.estudianteNivelIdNivel) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.ClaseHasEstudiantePK[ claseIdClase=" + claseIdClase + ", claseDocentehasFacultadDocenteIdDocente=" + claseDocentehasFacultadDocenteIdDocente + ", claseDocentehasFacultadFacultadIdFacultad=" + claseDocentehasFacultadFacultadIdFacultad + ", claseMateriaIdMateria=" + claseMateriaIdMateria + ", estudianteIdEstudiante=" + estudianteIdEstudiante + ", estudianteModalidadIdModalidad=" + estudianteModalidadIdModalidad + ", estudianteNivelIdNivel=" + estudianteNivelIdNivel + " ]";
    }
    
}
