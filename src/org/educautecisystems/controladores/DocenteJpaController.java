/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.controladores;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.educautecisystems.entidades.Materia;
import org.educautecisystems.entidades.Facultad;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.entidades.Docente;

/**
 *
 * @author Shadow2012
 */
public class DocenteJpaController implements Serializable {

    public DocenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) {
        if (docente.getFacultadList() == null) {
            docente.setFacultadList(new ArrayList<Facultad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia idMateria = docente.getIdMateria();
            if (idMateria != null) {
                idMateria = em.getReference(idMateria.getClass(), idMateria.getIdMateria());
                docente.setIdMateria(idMateria);
            }
            List<Facultad> attachedFacultadList = new ArrayList<Facultad>();
            for (Facultad facultadListFacultadToAttach : docente.getFacultadList()) {
                facultadListFacultadToAttach = em.getReference(facultadListFacultadToAttach.getClass(), facultadListFacultadToAttach.getIdFacultad());
                attachedFacultadList.add(facultadListFacultadToAttach);
            }
            docente.setFacultadList(attachedFacultadList);
            em.persist(docente);
            if (idMateria != null) {
                idMateria.getDocenteList().add(docente);
                idMateria = em.merge(idMateria);
            }
            for (Facultad facultadListFacultad : docente.getFacultadList()) {
                facultadListFacultad.getDocenteList().add(docente);
                facultadListFacultad = em.merge(facultadListFacultad);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Docente docente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente persistentDocente = em.find(Docente.class, docente.getIdDocente());
            Materia idMateriaOld = persistentDocente.getIdMateria();
            Materia idMateriaNew = docente.getIdMateria();
            List<Facultad> facultadListOld = persistentDocente.getFacultadList();
            List<Facultad> facultadListNew = docente.getFacultadList();
            if (idMateriaNew != null) {
                idMateriaNew = em.getReference(idMateriaNew.getClass(), idMateriaNew.getIdMateria());
                docente.setIdMateria(idMateriaNew);
            }
            List<Facultad> attachedFacultadListNew = new ArrayList<Facultad>();
            for (Facultad facultadListNewFacultadToAttach : facultadListNew) {
                facultadListNewFacultadToAttach = em.getReference(facultadListNewFacultadToAttach.getClass(), facultadListNewFacultadToAttach.getIdFacultad());
                attachedFacultadListNew.add(facultadListNewFacultadToAttach);
            }
            facultadListNew = attachedFacultadListNew;
            docente.setFacultadList(facultadListNew);
            docente = em.merge(docente);
            if (idMateriaOld != null && !idMateriaOld.equals(idMateriaNew)) {
                idMateriaOld.getDocenteList().remove(docente);
                idMateriaOld = em.merge(idMateriaOld);
            }
            if (idMateriaNew != null && !idMateriaNew.equals(idMateriaOld)) {
                idMateriaNew.getDocenteList().add(docente);
                idMateriaNew = em.merge(idMateriaNew);
            }
            for (Facultad facultadListOldFacultad : facultadListOld) {
                if (!facultadListNew.contains(facultadListOldFacultad)) {
                    facultadListOldFacultad.getDocenteList().remove(docente);
                    facultadListOldFacultad = em.merge(facultadListOldFacultad);
                }
            }
            for (Facultad facultadListNewFacultad : facultadListNew) {
                if (!facultadListOld.contains(facultadListNewFacultad)) {
                    facultadListNewFacultad.getDocenteList().add(docente);
                    facultadListNewFacultad = em.merge(facultadListNewFacultad);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = docente.getIdDocente();
                if (findDocente(id) == null) {
                    throw new NonexistentEntityException("The docente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getIdDocente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            Materia idMateria = docente.getIdMateria();
            if (idMateria != null) {
                idMateria.getDocenteList().remove(docente);
                idMateria = em.merge(idMateria);
            }
            List<Facultad> facultadList = docente.getFacultadList();
            for (Facultad facultadListFacultad : facultadList) {
                facultadListFacultad.getDocenteList().remove(docente);
                facultadListFacultad = em.merge(facultadListFacultad);
            }
            em.remove(docente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Docente> findDocenteEntities() {
        return findDocenteEntities(true, -1, -1);
    }

    public List<Docente> findDocenteEntities(int maxResults, int firstResult) {
        return findDocenteEntities(false, maxResults, firstResult);
    }

    private List<Docente> findDocenteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Docente.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Docente findDocente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Docente.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocenteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Docente> rt = cq.from(Docente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
