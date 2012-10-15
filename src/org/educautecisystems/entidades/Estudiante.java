/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Shadow2012
 */
@Entity
@Table(name = "estudiante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Estudiante.findAll", query = "SELECT e FROM Estudiante e"),
    @NamedQuery(name = "Estudiante.findByIdEstudiante", query = "SELECT e FROM Estudiante e WHERE e.estudiantePK.idEstudiante = :idEstudiante"),
    @NamedQuery(name = "Estudiante.findByModalidadIdModalidad", query = "SELECT e FROM Estudiante e WHERE e.estudiantePK.modalidadIdModalidad = :modalidadIdModalidad"),
    @NamedQuery(name = "Estudiante.findByNivelIdNivel", query = "SELECT e FROM Estudiante e WHERE e.estudiantePK.nivelIdNivel = :nivelIdNivel")})
public class Estudiante implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EstudiantePK estudiantePK;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estudiante")
    private List<ClaseHasEstudiante> claseHasEstudianteList;
    @JoinColumn(name = "Nivel_Id_Nivel", referencedColumnName = "Id_Nivel", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Nivel nivel;
    @JoinColumn(name = "Modalidad_Id_Modalidad", referencedColumnName = "Id_Modalidad", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Modalidad modalidad;

    public Estudiante() {
    }

    public Estudiante(EstudiantePK estudiantePK) {
        this.estudiantePK = estudiantePK;
    }

    public Estudiante(int idEstudiante, int modalidadIdModalidad, int nivelIdNivel) {
        this.estudiantePK = new EstudiantePK(idEstudiante, modalidadIdModalidad, nivelIdNivel);
    }

    public EstudiantePK getEstudiantePK() {
        return estudiantePK;
    }

    public void setEstudiantePK(EstudiantePK estudiantePK) {
        this.estudiantePK = estudiantePK;
    }

    @XmlTransient
    public List<ClaseHasEstudiante> getClaseHasEstudianteList() {
        return claseHasEstudianteList;
    }

    public void setClaseHasEstudianteList(List<ClaseHasEstudiante> claseHasEstudianteList) {
        this.claseHasEstudianteList = claseHasEstudianteList;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estudiantePK != null ? estudiantePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Estudiante)) {
            return false;
        }
        Estudiante other = (Estudiante) object;
        if ((this.estudiantePK == null && other.estudiantePK != null) || (this.estudiantePK != null && !this.estudiantePK.equals(other.estudiantePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.Estudiante[ estudiantePK=" + estudiantePK + " ]";
    }
    
}
