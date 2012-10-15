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
import org.educautecisystems.entidades.Nivel;
import org.educautecisystems.entidades.Modalidad;
import org.educautecisystems.entidades.ClaseHasEstudiante;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.educautecisystems.controladores.exceptions.IllegalOrphanException;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.controladores.exceptions.PreexistingEntityException;
import org.educautecisystems.entidades.Estudiante;
import org.educautecisystems.entidades.EstudiantePK;

/**
 *
 * @author Shadow2012
 */
public class EstudianteJpaController implements Serializable {

    public EstudianteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estudiante estudiante) throws PreexistingEntityException, Exception {
        if (estudiante.getEstudiantePK() == null) {
            estudiante.setEstudiantePK(new EstudiantePK());
        }
        if (estudiante.getClaseHasEstudianteList() == null) {
            estudiante.setClaseHasEstudianteList(new ArrayList<ClaseHasEstudiante>());
        }
        estudiante.getEstudiantePK().setNivelIdNivel(estudiante.getNivel().getIdNivel());
        estudiante.getEstudiantePK().setModalidadIdModalidad(estudiante.getModalidad().getIdModalidad());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Nivel nivel = estudiante.getNivel();
            if (nivel != null) {
                nivel = em.getReference(nivel.getClass(), nivel.getIdNivel());
                estudiante.setNivel(nivel);
            }
            Modalidad modalidad = estudiante.getModalidad();
            if (modalidad != null) {
                modalidad = em.getReference(modalidad.getClass(), modalidad.getIdModalidad());
                estudiante.setModalidad(modalidad);
            }
            List<ClaseHasEstudiante> attachedClaseHasEstudianteList = new ArrayList<ClaseHasEstudiante>();
            for (ClaseHasEstudiante claseHasEstudianteListClaseHasEstudianteToAttach : estudiante.getClaseHasEstudianteList()) {
                claseHasEstudianteListClaseHasEstudianteToAttach = em.getReference(claseHasEstudianteListClaseHasEstudianteToAttach.getClass(), claseHasEstudianteListClaseHasEstudianteToAttach.getClaseHasEstudiantePK());
                attachedClaseHasEstudianteList.add(claseHasEstudianteListClaseHasEstudianteToAttach);
            }
            estudiante.setClaseHasEstudianteList(attachedClaseHasEstudianteList);
            em.persist(estudiante);
            if (nivel != null) {
                nivel.getEstudianteList().add(estudiante);
                nivel = em.merge(nivel);
            }
            if (modalidad != null) {
                modalidad.getEstudianteList().add(estudiante);
                modalidad = em.merge(modalidad);
            }
            for (ClaseHasEstudiante claseHasEstudianteListClaseHasEstudiante : estudiante.getClaseHasEstudianteList()) {
                Estudiante oldEstudianteOfClaseHasEstudianteListClaseHasEstudiante = claseHasEstudianteListClaseHasEstudiante.getEstudiante();
                claseHasEstudianteListClaseHasEstudiante.setEstudiante(estudiante);
                claseHasEstudianteListClaseHasEstudiante = em.merge(claseHasEstudianteListClaseHasEstudiante);
                if (oldEstudianteOfClaseHasEstudianteListClaseHasEstudiante != null) {
                    oldEstudianteOfClaseHasEstudianteListClaseHasEstudiante.getClaseHasEstudianteList().remove(claseHasEstudianteListClaseHasEstudiante);
                    oldEstudianteOfClaseHasEstudianteListClaseHasEstudiante = em.merge(oldEstudianteOfClaseHasEstudianteListClaseHasEstudiante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEstudiante(estudiante.getEstudiantePK()) != null) {
                throw new PreexistingEntityException("Estudiante " + estudiante + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estudiante estudiante) throws IllegalOrphanException, NonexistentEntityException, Exception {
        estudiante.getEstudiantePK().setNivelIdNivel(estudiante.getNivel().getIdNivel());
        estudiante.getEstudiantePK().setModalidadIdModalidad(estudiante.getModalidad().getIdModalidad());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante persistentEstudiante = em.find(Estudiante.class, estudiante.getEstudiantePK());
            Nivel nivelOld = persistentEstudiante.getNivel();
            Nivel nivelNew = estudiante.getNivel();
            Modalidad modalidadOld = persistentEstudiante.getModalidad();
            Modalidad modalidadNew = estudiante.getModalidad();
            List<ClaseHasEstudiante> claseHasEstudianteListOld = persistentEstudiante.getClaseHasEstudianteList();
            List<ClaseHasEstudiante> claseHasEstudianteListNew = estudiante.getClaseHasEstudianteList();
            List<String> illegalOrphanMessages = null;
            for (ClaseHasEstudiante claseHasEstudianteListOldClaseHasEstudiante : claseHasEstudianteListOld) {
                if (!claseHasEstudianteListNew.contains(claseHasEstudianteListOldClaseHasEstudiante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClaseHasEstudiante " + claseHasEstudianteListOldClaseHasEstudiante + " since its estudiante field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (nivelNew != null) {
                nivelNew = em.getReference(nivelNew.getClass(), nivelNew.getIdNivel());
                estudiante.setNivel(nivelNew);
            }
            if (modalidadNew != null) {
                modalidadNew = em.getReference(modalidadNew.getClass(), modalidadNew.getIdModalidad());
                estudiante.setModalidad(modalidadNew);
            }
            List<ClaseHasEstudiante> attachedClaseHasEstudianteListNew = new ArrayList<ClaseHasEstudiante>();
            for (ClaseHasEstudiante claseHasEstudianteListNewClaseHasEstudianteToAttach : claseHasEstudianteListNew) {
                claseHasEstudianteListNewClaseHasEstudianteToAttach = em.getReference(claseHasEstudianteListNewClaseHasEstudianteToAttach.getClass(), claseHasEstudianteListNewClaseHasEstudianteToAttach.getClaseHasEstudiantePK());
                attachedClaseHasEstudianteListNew.add(claseHasEstudianteListNewClaseHasEstudianteToAttach);
            }
            claseHasEstudianteListNew = attachedClaseHasEstudianteListNew;
            estudiante.setClaseHasEstudianteList(claseHasEstudianteListNew);
            estudiante = em.merge(estudiante);
            if (nivelOld != null && !nivelOld.equals(nivelNew)) {
                nivelOld.getEstudianteList().remove(estudiante);
                nivelOld = em.merge(nivelOld);
            }
            if (nivelNew != null && !nivelNew.equals(nivelOld)) {
                nivelNew.getEstudianteList().add(estudiante);
                nivelNew = em.merge(nivelNew);
            }
            if (modalidadOld != null && !modalidadOld.equals(modalidadNew)) {
                modalidadOld.getEstudianteList().remove(estudiante);
                modalidadOld = em.merge(modalidadOld);
            }
            if (modalidadNew != null && !modalidadNew.equals(modalidadOld)) {
                modalidadNew.getEstudianteList().add(estudiante);
                modalidadNew = em.merge(modalidadNew);
            }
            for (ClaseHasEstudiante claseHasEstudianteListNewClaseHasEstudiante : claseHasEstudianteListNew) {
                if (!claseHasEstudianteListOld.contains(claseHasEstudianteListNewClaseHasEstudiante)) {
                    Estudiante oldEstudianteOfClaseHasEstudianteListNewClaseHasEstudiante = claseHasEstudianteListNewClaseHasEstudiante.getEstudiante();
                    claseHasEstudianteListNewClaseHasEstudiante.setEstudiante(estudiante);
                    claseHasEstudianteListNewClaseHasEstudiante = em.merge(claseHasEstudianteListNewClaseHasEstudiante);
                    if (oldEstudianteOfClaseHasEstudianteListNewClaseHasEstudiante != null && !oldEstudianteOfClaseHasEstudianteListNewClaseHasEstudiante.equals(estudiante)) {
                        oldEstudianteOfClaseHasEstudianteListNewClaseHasEstudiante.getClaseHasEstudianteList().remove(claseHasEstudianteListNewClaseHasEstudiante);
                        oldEstudianteOfClaseHasEstudianteListNewClaseHasEstudiante = em.merge(oldEstudianteOfClaseHasEstudianteListNewClaseHasEstudiante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                EstudiantePK id = estudiante.getEstudiantePK();
                if (findEstudiante(id) == null) {
                    throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(EstudiantePK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante estudiante;
            try {
                estudiante = em.getReference(Estudiante.class, id);
                estudiante.getEstudiantePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estudiante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ClaseHasEstudiante> claseHasEstudianteListOrphanCheck = estudiante.getClaseHasEstudianteList();
            for (ClaseHasEstudiante claseHasEstudianteListOrphanCheckClaseHasEstudiante : claseHasEstudianteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estudiante (" + estudiante + ") cannot be destroyed since the ClaseHasEstudiante " + claseHasEstudianteListOrphanCheckClaseHasEstudiante + " in its claseHasEstudianteList field has a non-nullable estudiante field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Nivel nivel = estudiante.getNivel();
            if (nivel != null) {
                nivel.getEstudianteList().remove(estudiante);
                nivel = em.merge(nivel);
            }
            Modalidad modalidad = estudiante.getModalidad();
            if (modalidad != null) {
                modalidad.getEstudianteList().remove(estudiante);
                modalidad = em.merge(modalidad);
            }
            em.remove(estudiante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estudiante> findEstudianteEntities() {
        return findEstudianteEntities(true, -1, -1);
    }

    public List<Estudiante> findEstudianteEntities(int maxResults, int firstResult) {
        return findEstudianteEntities(false, maxResults, firstResult);
    }

    private List<Estudiante> findEstudianteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estudiante.class));
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

    public Estudiante findEstudiante(EstudiantePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estudiante.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstudianteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estudiante> rt = cq.from(Estudiante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
