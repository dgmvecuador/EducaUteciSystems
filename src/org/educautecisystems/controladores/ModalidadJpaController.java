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
import org.educautecisystems.entidades.Estudiante;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.educautecisystems.controladores.exceptions.IllegalOrphanException;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.entidades.Modalidad;

/**
 *
 * @author Shadow2012
 */
public class ModalidadJpaController implements Serializable {

    public ModalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modalidad modalidad) {
        if (modalidad.getEstudianteList() == null) {
            modalidad.setEstudianteList(new ArrayList<Estudiante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudiante> attachedEstudianteList = new ArrayList<Estudiante>();
            for (Estudiante estudianteListEstudianteToAttach : modalidad.getEstudianteList()) {
                estudianteListEstudianteToAttach = em.getReference(estudianteListEstudianteToAttach.getClass(), estudianteListEstudianteToAttach.getEstudiantePK());
                attachedEstudianteList.add(estudianteListEstudianteToAttach);
            }
            modalidad.setEstudianteList(attachedEstudianteList);
            em.persist(modalidad);
            for (Estudiante estudianteListEstudiante : modalidad.getEstudianteList()) {
                Modalidad oldModalidadOfEstudianteListEstudiante = estudianteListEstudiante.getModalidad();
                estudianteListEstudiante.setModalidad(modalidad);
                estudianteListEstudiante = em.merge(estudianteListEstudiante);
                if (oldModalidadOfEstudianteListEstudiante != null) {
                    oldModalidadOfEstudianteListEstudiante.getEstudianteList().remove(estudianteListEstudiante);
                    oldModalidadOfEstudianteListEstudiante = em.merge(oldModalidadOfEstudianteListEstudiante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Modalidad modalidad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modalidad persistentModalidad = em.find(Modalidad.class, modalidad.getIdModalidad());
            List<Estudiante> estudianteListOld = persistentModalidad.getEstudianteList();
            List<Estudiante> estudianteListNew = modalidad.getEstudianteList();
            List<String> illegalOrphanMessages = null;
            for (Estudiante estudianteListOldEstudiante : estudianteListOld) {
                if (!estudianteListNew.contains(estudianteListOldEstudiante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudiante " + estudianteListOldEstudiante + " since its modalidad field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Estudiante> attachedEstudianteListNew = new ArrayList<Estudiante>();
            for (Estudiante estudianteListNewEstudianteToAttach : estudianteListNew) {
                estudianteListNewEstudianteToAttach = em.getReference(estudianteListNewEstudianteToAttach.getClass(), estudianteListNewEstudianteToAttach.getEstudiantePK());
                attachedEstudianteListNew.add(estudianteListNewEstudianteToAttach);
            }
            estudianteListNew = attachedEstudianteListNew;
            modalidad.setEstudianteList(estudianteListNew);
            modalidad = em.merge(modalidad);
            for (Estudiante estudianteListNewEstudiante : estudianteListNew) {
                if (!estudianteListOld.contains(estudianteListNewEstudiante)) {
                    Modalidad oldModalidadOfEstudianteListNewEstudiante = estudianteListNewEstudiante.getModalidad();
                    estudianteListNewEstudiante.setModalidad(modalidad);
                    estudianteListNewEstudiante = em.merge(estudianteListNewEstudiante);
                    if (oldModalidadOfEstudianteListNewEstudiante != null && !oldModalidadOfEstudianteListNewEstudiante.equals(modalidad)) {
                        oldModalidadOfEstudianteListNewEstudiante.getEstudianteList().remove(estudianteListNewEstudiante);
                        oldModalidadOfEstudianteListNewEstudiante = em.merge(oldModalidadOfEstudianteListNewEstudiante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = modalidad.getIdModalidad();
                if (findModalidad(id) == null) {
                    throw new NonexistentEntityException("The modalidad with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modalidad modalidad;
            try {
                modalidad = em.getReference(Modalidad.class, id);
                modalidad.getIdModalidad();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modalidad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Estudiante> estudianteListOrphanCheck = modalidad.getEstudianteList();
            for (Estudiante estudianteListOrphanCheckEstudiante : estudianteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Modalidad (" + modalidad + ") cannot be destroyed since the Estudiante " + estudianteListOrphanCheckEstudiante + " in its estudianteList field has a non-nullable modalidad field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(modalidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Modalidad> findModalidadEntities() {
        return findModalidadEntities(true, -1, -1);
    }

    public List<Modalidad> findModalidadEntities(int maxResults, int firstResult) {
        return findModalidadEntities(false, maxResults, firstResult);
    }

    private List<Modalidad> findModalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modalidad.class));
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

    public Modalidad findModalidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modalidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getModalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modalidad> rt = cq.from(Modalidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
