/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.controladores.exceptions.PreexistingEntityException;
import org.educautecisystems.entidades.Docente;
import org.educautecisystems.entidades.DocentePK;
import org.educautecisystems.entidades.Facultad;
import org.educautecisystems.entidades.Materia;

/**
 *
 * @author Shadow2012
 */
public class DocenteJpaController implements Serializable {

    public DocenteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("EducaUteciSystemsPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Docente docente) throws PreexistingEntityException, Exception {
        if (docente.getDocentePK() == null) {
            docente.setDocentePK(new DocentePK());
        }
        if (docente.getFacultadList() == null) {
            docente.setFacultadList(new ArrayList<Facultad>());
        }
        docente.getDocentePK().setIdMateria(docente.getMateria().getIdMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia = docente.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getIdMateria());
                docente.setMateria(materia);
            }
            List<Facultad> attachedFacultadList = new ArrayList<Facultad>();
            for (Facultad facultadListFacultadToAttach : docente.getFacultadList()) {
                facultadListFacultadToAttach = em.getReference(facultadListFacultadToAttach.getClass(), facultadListFacultadToAttach.getIdFacultad());
                attachedFacultadList.add(facultadListFacultadToAttach);
            }
            docente.setFacultadList(attachedFacultadList);
            em.persist(docente);
            if (materia != null) {
                materia.getDocenteList().add(docente);
                materia = em.merge(materia);
            }
            for (Facultad facultadListFacultad : docente.getFacultadList()) {
                facultadListFacultad.getDocenteList().add(docente);
                facultadListFacultad = em.merge(facultadListFacultad);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocente(docente.getDocentePK()) != null) {
                throw new PreexistingEntityException("Docente " + docente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Docente docente) throws NonexistentEntityException, Exception {
        docente.getDocentePK().setIdMateria(docente.getMateria().getIdMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente persistentDocente = em.find(Docente.class, docente.getDocentePK());
            Materia materiaOld = persistentDocente.getMateria();
            Materia materiaNew = docente.getMateria();
            List<Facultad> facultadListOld = persistentDocente.getFacultadList();
            List<Facultad> facultadListNew = docente.getFacultadList();
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getIdMateria());
                docente.setMateria(materiaNew);
            }
            List<Facultad> attachedFacultadListNew = new ArrayList<Facultad>();
            for (Facultad facultadListNewFacultadToAttach : facultadListNew) {
                facultadListNewFacultadToAttach = em.getReference(facultadListNewFacultadToAttach.getClass(), facultadListNewFacultadToAttach.getIdFacultad());
                attachedFacultadListNew.add(facultadListNewFacultadToAttach);
            }
            facultadListNew = attachedFacultadListNew;
            docente.setFacultadList(facultadListNew);
            docente = em.merge(docente);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getDocenteList().remove(docente);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getDocenteList().add(docente);
                materiaNew = em.merge(materiaNew);
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
                DocentePK id = docente.getDocentePK();
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

    public void destroy(DocentePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Docente docente;
            try {
                docente = em.getReference(Docente.class, id);
                docente.getDocentePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The docente with id " + id + " no longer exists.", enfe);
            }
            Materia materia = docente.getMateria();
            if (materia != null) {
                materia.getDocenteList().remove(docente);
                materia = em.merge(materia);
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

    public Docente findDocente(DocentePK id) {
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
