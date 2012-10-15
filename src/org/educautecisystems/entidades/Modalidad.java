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
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "modalidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modalidad.findAll", query = "SELECT m FROM Modalidad m"),
    @NamedQuery(name = "Modalidad.findByIdModalidad", query = "SELECT m FROM Modalidad m WHERE m.idModalidad = :idModalidad"),
    @NamedQuery(name = "Modalidad.findByNombre", query = "SELECT m FROM Modalidad m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "Modalidad.findByHoraInicio", query = "SELECT m FROM Modalidad m WHERE m.horaInicio = :horaInicio"),
    @NamedQuery(name = "Modalidad.findByHoraFinal", query = "SELECT m FROM Modalidad m WHERE m.horaFinal = :horaFinal")})
public class Modalidad implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Modalidad")
    private Integer idModalidad;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "Hora_Inicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaInicio;
    @Basic(optional = false)
    @Column(name = "Hora_Final")
    @Temporal(TemporalType.TIMESTAMP)
    private Date horaFinal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modalidad")
    private List<Estudiante> estudianteList;

    public Modalidad() {
    }

    public Modalidad(Integer idModalidad) {
        this.idModalidad = idModalidad;
    }

    public Modalidad(Integer idModalidad, String nombre, Date horaInicio, Date horaFinal) {
        this.idModalidad = idModalidad;
        this.nombre = nombre;
        this.horaInicio = horaInicio;
        this.horaFinal = horaFinal;
    }

    public Integer getIdModalidad() {
        return idModalidad;
    }

    public void setIdModalidad(Integer idModalidad) {
        this.idModalidad = idModalidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(Date horaFinal) {
        this.horaFinal = horaFinal;
    }

    @XmlTransient
    public List<Estudiante> getEstudianteList() {
        return estudianteList;
    }

    public void setEstudianteList(List<Estudiante> estudianteList) {
        this.estudianteList = estudianteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModalidad != null ? idModalidad.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modalidad)) {
            return false;
        }
        Modalidad other = (Modalidad) object;
        if ((this.idModalidad == null && other.idModalidad != null) || (this.idModalidad != null && !this.idModalidad.equals(other.idModalidad))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.educautecisystems.entidades.Modalidad[ idModalidad=" + idModalidad + " ]";
    }
    
}
