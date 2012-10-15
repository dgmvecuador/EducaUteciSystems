/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.entidades;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Shadow2012
 */
@Entity
@Table(name = "clase_has_estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClaseHasEstudiante.findAll", query = "SELECT c FROM ClaseHasEstudiante c"),
    @NamedQuery(name = "ClaseHasEstudiante.findByClaseIdClase", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.claseIdClase = :claseIdClase"),
    @NamedQuery(name = "ClaseHasEstudiante.findByClaseDocentehasFacultadDocenteIdDocente", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.claseDocentehasFacultadDocenteIdDocente = :claseDocentehasFacultadDocenteIdDocente"),
    @NamedQuery(name = "ClaseHasEstudiante.findByClaseDocentehasFacultadFacultadIdFacultad", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.claseDocentehasFacultadFacultadIdFacultad = :claseDocentehasFacultadFacultadIdFacultad"),
    @NamedQuery(name = "ClaseHasEstudiante.findByClaseMateriaIdMateria", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.claseMateriaIdMateria = :claseMateriaIdMateria"),
    @NamedQuery(name = "ClaseHasEstudiante.findByEstudianteIdEstudiante", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.estudianteIdEstudiante = :estudianteIdEstudiante"),
    @NamedQuery(name = "ClaseHasEstudiante.findByEstudianteModalidadIdModalidad", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.estudianteModalidadIdModalidad = :estudianteModalidadIdModalidad"),
    @NamedQuery(name = "ClaseHasEstudiante.findByEstudianteNivelIdNivel", query = "SELECT c FROM ClaseHasEstudiante c WHERE c.claseHasEstudiantePK.estudianteNivelIdNivel = :estudianteNivelIdNivel")})
public class ClaseHasEstudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClaseHasEstudiantePK claseHasEstudiantePK;
    @JoinColumns({
        @JoinColumn(name = "Estudiante_Id_Estudiante", referencedColumnName = "Id_Estudiante", insertable = false, updatable = false),
        @JoinColumn(name = "Estudiante_Modalidad_Id_Modalidad", referencedColumnName = "Modalidad_Id_Modalidad", insertable = false, updatable = false),
        @JoinColumn(name = "Estudiante_Nivel_Id_Nivel", referencedColumnName = "Nivel_Id_Nivel", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Estudiante estudiante;
    @JoinColumns({
        @JoinColumn(name = "Clase_Id_Clase", referencedColumnName = "Id_Clase", insertable = false, updatable = false),
        @JoinColumn(name = "Clase_Materia_Id_Materia", referencedColumnName = "Materia_Id_Materia", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Clase clase;

    public ClaseHasEstudiante() {
    }

    public ClaseHasEstudiante(ClaseHasEstudiantePK claseHasEstudiantePK) {
        this.claseHasEstudiantePK = claseHasEstudiantePK;
    }

    public ClaseHasEstudiante(int claseIdClase, int claseDocentehasFacultadDocenteIdDocente, int claseDocentehasFacultadFacultadIdFacultad, int claseMateriaIdMateria, int estudianteIdEstudiante, int estudianteModalidadIdModalidad, int estudianteNivelIdNivel) {
        this.claseHasEstudiantePK = new ClaseHasEstudiantePK(claseIdClase, claseDocentehasFacultadDocenteIdDocente, claseDocentehasFacultadFacultadIdFacultad, claseMateriaIdMateria, estudianteIdEstudiante, estudianteModalidadIdModalidad, estudianteNivelIdNivel);
    }

    public ClaseHasEstudiantePK getClaseHasEstudiantePK() {
        return claseHasEstudiantePK;
    }

    public void setClaseHasEstudiantePK(ClaseHasEstudiantePK claseHasEstudiantePK) {
        this.claseHasEstudiantePK = claseHasEstudiantePK;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Clase getClase() {
        return clase;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (claseHasEstudiantePK != null ? claseHasEstudiantePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClaseHasEstudiante)) {
            return false;
        }
        ClaseHasEstudiante other = (ClaseHasEstudiante) object;
        if ((this.claseHasEstudiantePK == null && other.claseHasEstudiantePK != null) || (this.claseHasEstudiantePK != null && !this.claseHasEstudiantePK.equals(other.claseHasEstudiantePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.ClaseHasEstudiante[ claseHasEstudiantePK=" + claseHasEstudiantePK + " ]";
    }
    
}
