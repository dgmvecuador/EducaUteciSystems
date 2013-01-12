/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Shadow2012
 */
@Entity
@Table(name = "docente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Docente.findAll", query = "SELECT d FROM Docente d"),
    @NamedQuery(name = "Docente.findByIdDocente", query = "SELECT d FROM Docente d WHERE d.docentePK.idDocente = :idDocente"),
    @NamedQuery(name = "Docente.findByUsuario", query = "SELECT d FROM Docente d WHERE d.usuario = :usuario"),
    @NamedQuery(name = "Docente.findByContrasena", query = "SELECT d FROM Docente d WHERE d.contrasena = :contrasena"),
    @NamedQuery(name = "Docente.findByIdMateria", query = "SELECT d FROM Docente d WHERE d.docentePK.idMateria = :idMateria")})
public class Docente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DocentePK docentePK;
    @Basic(optional = false)
    @Column(name = "Usuario")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "Contrasena")
    private String contrasena;
    @JoinTable(name = "docente_has_facultad", joinColumns = {
        @JoinColumn(name = "Docente_Id_Docente", referencedColumnName = "Id_Docente"),
        @JoinColumn(name = "Docente_Id_Materia", referencedColumnName = "Id_Materia")}, inverseJoinColumns = {
        @JoinColumn(name = "Facultad_Id_Facultad", referencedColumnName = "Id_Facultad")})
    @ManyToMany
    private List<Facultad> facultadList;
    @JoinColumn(name = "Id_Materia", referencedColumnName = "Id_Materia", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Materia materia;

    public Docente() {
    }

    public Docente(DocentePK docentePK) {
        this.docentePK = docentePK;
    }

    public Docente(DocentePK docentePK, String usuario, String contrasena) {
        this.docentePK = docentePK;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public Docente(int idDocente, int idMateria) {
        this.docentePK = new DocentePK(idDocente, idMateria);
    }

    public DocentePK getDocentePK() {
        return docentePK;
    }

    public void setDocentePK(DocentePK docentePK) {
        this.docentePK = docentePK;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @XmlTransient
    public List<Facultad> getFacultadList() {
        return facultadList;
    }

    public void setFacultadList(List<Facultad> facultadList) {
        this.facultadList = facultadList;
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
        hash += (docentePK != null ? docentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Docente)) {
            return false;
        }
        Docente other = (Docente) object;
        if ((this.docentePK == null && other.docentePK != null) || (this.docentePK != null && !this.docentePK.equals(other.docentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.Docente[ docentePK=" + docentePK + " ]";
    }
    
}
