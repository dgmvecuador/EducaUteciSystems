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
import org.educautecisystems.entidades.Nivel;

/**
 *
 * @author Shadow2012
 */
public class NivelJpaController implements Serializable {

    public NivelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Nivel nivel) {
        if (nivel.getEstudianteList() == null) {
            nivel.setEstudianteList(new ArrayList<Estudiante>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Estudiante> attachedEstudianteList = new ArrayList<Estudiante>();
            for (Estudiante estudianteListEstudianteToAttach : nivel.getEstudianteList()) {
                estudianteListEstudianteToAttach = em.getReference(estudianteListEstudianteToAttach.getClass(), estudianteListEstudianteToAttach.getEstudiantePK());
                attachedEstudianteList.add(estudianteListEstudianteToAttach);
            }
            nivel.setEstudianteList(attachedEstudianteList);
            em.persist(nivel);
            for (Estudiante estudianteListEstudiante : nivel.getEstudianteList()) {
                Nivel oldNivelOfEstudianteListEstudiante = estudianteListEstudiante.getNivel();
                estudianteListEstudiante.setNivel(nivel);
                estudianteListEstudiante = em.merge(estudianteListEstudiante);
                if (oldNivelOfEstudianteListEstudiante != null) {
                    oldNivelOfEstudianteListEstudiante.getEstudianteList().remove(estudianteListEstudiante);
                    oldNivelOfEstudianteListEstudiante = em.merge(oldNivelOfEstudianteListEstudiante);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Nivel nivel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel persistentNivel = em.find(Nivel.class, nivel.getIdNivel());
            List<Estudiante> estudianteListOld = persistentNivel.getEstudianteList();
            List<Estudiante> estudianteListNew = nivel.getEstudianteList();
            List<String> illegalOrphanMessages = null;
            for (Estudiante estudianteListOldEstudiante : estudianteListOld) {
                if (!estudianteListNew.contains(estudianteListOldEstudiante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Estudiante " + estudianteListOldEstudiante + " since its nivel field is not nullable.");
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
            nivel.setEstudianteList(estudianteListNew);
            nivel = em.merge(nivel);
            for (Estudiante estudianteListNewEstudiante : estudianteListNew) {
                if (!estudianteListOld.contains(estudianteListNewEstudiante)) {
                    Nivel oldNivelOfEstudianteListNewEstudiante = estudianteListNewEstudiante.getNivel();
                    estudianteListNewEstudiante.setNivel(nivel);
                    estudianteListNewEstudiante = em.merge(estudianteListNewEstudiante);
                    if (oldNivelOfEstudianteListNewEstudiante != null && !oldNivelOfEstudianteListNewEstudiante.equals(nivel)) {
                        oldNivelOfEstudianteListNewEstudiante.getEstudianteList().remove(estudianteListNewEstudiante);
                        oldNivelOfEstudianteListNewEstudiante = em.merge(oldNivelOfEstudianteListNewEstudiante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = nivel.getIdNivel();
                if (findNivel(id) == null) {
                    throw new NonexistentEntityException("The nivel with id " + id + " no longer exists.");
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
            Nivel nivel;
            try {
                nivel = em.getReference(Nivel.class, id);
                nivel.getIdNivel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The nivel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Estudiante> estudianteListOrphanCheck = nivel.getEstudianteList();
            for (Estudiante estudianteListOrphanCheckEstudiante : estudianteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Nivel (" + nivel + ") cannot be destroyed since the Estudiante " + estudianteListOrphanCheckEstudiante + " in its estudianteList field has a non-nullable nivel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(nivel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Nivel> findNivelEntities() {
        return findNivelEntities(true, -1, -1);
    }

    public List<Nivel> findNivelEntities(int maxResults, int firstResult) {
        return findNivelEntities(false, maxResults, firstResult);
    }

    private List<Nivel> findNivelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Nivel.class));
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

    public Nivel findNivel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Nivel.class, id);
        } finally {
            em.close();
        }
    }

    public int getNivelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Nivel> rt = cq.from(Nivel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
