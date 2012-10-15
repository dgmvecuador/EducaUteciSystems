/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Shadow2012
 */
@Entity
@Table(name = "clase")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Clase.findAll", query = "SELECT c FROM Clase c"),
    @NamedQuery(name = "Clase.findByIdClase", query = "SELECT c FROM Clase c WHERE c.clasePK.idClase = :idClase"),
    @NamedQuery(name = "Clase.findByMateriaIdMateria", query = "SELECT c FROM Clase c WHERE c.clasePK.materiaIdMateria = :materiaIdMateria"),
    @NamedQuery(name = "Clase.findByFecha", query = "SELECT c FROM Clase c WHERE c.fecha = :fecha")})
public class Clase implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ClasePK clasePK;
    @Basic(optional = false)
    @Column(name = "Fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clase")
    private List<ClaseHasEstudiante> claseHasEstudianteList;
    @JoinColumn(name = "Materia_Id_Materia", referencedColumnName = "Id_Materia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materia materia;

    public Clase() {
    }

    public Clase(ClasePK clasePK) {
        this.clasePK = clasePK;
    }

    public Clase(ClasePK clasePK, Date fecha) {
        this.clasePK = clasePK;
        this.fecha = fecha;
    }

    public Clase(int idClase, int materiaIdMateria) {
        this.clasePK = new ClasePK(idClase, materiaIdMateria);
    }

    public ClasePK getClasePK() {
        return clasePK;
    }

    public void setClasePK(ClasePK clasePK) {
        this.clasePK = clasePK;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @XmlTransient
    public List<ClaseHasEstudiante> getClaseHasEstudianteList() {
        return claseHasEstudianteList;
    }

    public void setClaseHasEstudianteList(List<ClaseHasEstudiante> claseHasEstudianteList) {
        this.claseHasEstudianteList = claseHasEstudianteList;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clasePK != null ? clasePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Clase)) {
            return false;
        }
        Clase other = (Clase) object;
        if ((this.clasePK == null && other.clasePK != null) || (this.clasePK != null && !this.clasePK.equals(other.clasePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.Clase[ clasePK=" + clasePK + " ]";
    }
    
}
