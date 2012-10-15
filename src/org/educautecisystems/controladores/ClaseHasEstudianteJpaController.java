/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems.controladores;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.controladores.exceptions.PreexistingEntityException;
import org.educautecisystems.entidades.Estudiante;
import org.educautecisystems.entidades.Clase;
import org.educautecisystems.entidades.ClaseHasEstudiante;
import org.educautecisystems.entidades.ClaseHasEstudiantePK;

/**
 *
 * @author Shadow2012
 */
public class ClaseHasEstudianteJpaController implements Serializable {

    public ClaseHasEstudianteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ClaseHasEstudiante claseHasEstudiante) throws PreexistingEntityException, Exception {
        if (claseHasEstudiante.getClaseHasEstudiantePK() == null) {
            claseHasEstudiante.setClaseHasEstudiantePK(new ClaseHasEstudiantePK());
        }
        claseHasEstudiante.getClaseHasEstudiantePK().setClaseMateriaIdMateria(claseHasEstudiante.getClase().getClasePK().getMateriaIdMateria());
        claseHasEstudiante.getClaseHasEstudiantePK().setClaseIdClase(claseHasEstudiante.getClase().getClasePK().getIdClase());
        claseHasEstudiante.getClaseHasEstudiantePK().setEstudianteModalidadIdModalidad(claseHasEstudiante.getEstudiante().getEstudiantePK().getModalidadIdModalidad());
        claseHasEstudiante.getClaseHasEstudiantePK().setEstudianteNivelIdNivel(claseHasEstudiante.getEstudiante().getEstudiantePK().getNivelIdNivel());
        claseHasEstudiante.getClaseHasEstudiantePK().setEstudianteIdEstudiante(claseHasEstudiante.getEstudiante().getEstudiantePK().getIdEstudiante());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Estudiante estudiante = claseHasEstudiante.getEstudiante();
            if (estudiante != null) {
                estudiante = em.getReference(estudiante.getClass(), estudiante.getEstudiantePK());
                claseHasEstudiante.setEstudiante(estudiante);
            }
            Clase clase = claseHasEstudiante.getClase();
            if (clase != null) {
                clase = em.getReference(clase.getClass(), clase.getClasePK());
                claseHasEstudiante.setClase(clase);
            }
            em.persist(claseHasEstudiante);
            if (estudiante != null) {
                estudiante.getClaseHasEstudianteList().add(claseHasEstudiante);
                estudiante = em.merge(estudiante);
            }
            if (clase != null) {
                clase.getClaseHasEstudianteList().add(claseHasEstudiante);
                clase = em.merge(clase);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClaseHasEstudiante(claseHasEstudiante.getClaseHasEstudiantePK()) != null) {
                throw new PreexistingEntityException("ClaseHasEstudiante " + claseHasEstudiante + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ClaseHasEstudiante claseHasEstudiante) throws NonexistentEntityException, Exception {
        claseHasEstudiante.getClaseHasEstudiantePK().setClaseMateriaIdMateria(claseHasEstudiante.getClase().getClasePK().getMateriaIdMateria());
        claseHasEstudiante.getClaseHasEstudiantePK().setClaseIdClase(claseHasEstudiante.getClase().getClasePK().getIdClase());
        claseHasEstudiante.getClaseHasEstudiantePK().setEstudianteModalidadIdModalidad(claseHasEstudiante.getEstudiante().getEstudiantePK().getModalidadIdModalidad());
        claseHasEstudiante.getClaseHasEstudiantePK().setEstudianteNivelIdNivel(claseHasEstudiante.getEstudiante().getEstudiantePK().getNivelIdNivel());
        claseHasEstudiante.getClaseHasEstudiantePK().setEstudianteIdEstudiante(claseHasEstudiante.getEstudiante().getEstudiantePK().getIdEstudiante());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClaseHasEstudiante persistentClaseHasEstudiante = em.find(ClaseHasEstudiante.class, claseHasEstudiante.getClaseHasEstudiantePK());
            Estudiante estudianteOld = persistentClaseHasEstudiante.getEstudiante();
            Estudiante estudianteNew = claseHasEstudiante.getEstudiante();
            Clase claseOld = persistentClaseHasEstudiante.getClase();
            Clase claseNew = claseHasEstudiante.getClase();
            if (estudianteNew != null) {
                estudianteNew = em.getReference(estudianteNew.getClass(), estudianteNew.getEstudiantePK());
                claseHasEstudiante.setEstudiante(estudianteNew);
            }
            if (claseNew != null) {
                claseNew = em.getReference(claseNew.getClass(), claseNew.getClasePK());
                claseHasEstudiante.setClase(claseNew);
            }
            claseHasEstudiante = em.merge(claseHasEstudiante);
            if (estudianteOld != null && !estudianteOld.equals(estudianteNew)) {
                estudianteOld.getClaseHasEstudianteList().remove(claseHasEstudiante);
                estudianteOld = em.merge(estudianteOld);
            }
            if (estudianteNew != null && !estudianteNew.equals(estudianteOld)) {
                estudianteNew.getClaseHasEstudianteList().add(claseHasEstudiante);
                estudianteNew = em.merge(estudianteNew);
            }
            if (claseOld != null && !claseOld.equals(claseNew)) {
                claseOld.getClaseHasEstudianteList().remove(claseHasEstudiante);
                claseOld = em.merge(claseOld);
            }
            if (claseNew != null && !claseNew.equals(claseOld)) {
                claseNew.getClaseHasEstudianteList().add(claseHasEstudiante);
                claseNew = em.merge(claseNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ClaseHasEstudiantePK id = claseHasEstudiante.getClaseHasEstudiantePK();
                if (findClaseHasEstudiante(id) == null) {
                    throw new NonexistentEntityException("The claseHasEstudiante with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ClaseHasEstudiantePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ClaseHasEstudiante claseHasEstudiante;
            try {
                claseHasEstudiante = em.getReference(ClaseHasEstudiante.class, id);
                claseHasEstudiante.getClaseHasEstudiantePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The claseHasEstudiante with id " + id + " no longer exists.", enfe);
            }
            Estudiante estudiante = claseHasEstudiante.getEstudiante();
            if (estudiante != null) {
                estudiante.getClaseHasEstudianteList().remove(claseHasEstudiante);
                estudiante = em.merge(estudiante);
            }
            Clase clase = claseHasEstudiante.getClase();
            if (clase != null) {
                clase.getClaseHasEstudianteList().remove(claseHasEstudiante);
                clase = em.merge(clase);
            }
            em.remove(claseHasEstudiante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ClaseHasEstudiante> findClaseHasEstudianteEntities() {
        return findClaseHasEstudianteEntities(true, -1, -1);
    }

    public List<ClaseHasEstudiante> findClaseHasEstudianteEntities(int maxResults, int firstResult) {
        return findClaseHasEstudianteEntities(false, maxResults, firstResult);
    }

    private List<ClaseHasEstudiante> findClaseHasEstudianteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ClaseHasEstudiante.class));
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

    public ClaseHasEstudiante findClaseHasEstudiante(ClaseHasEstudiantePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ClaseHasEstudiante.class, id);
        } finally {
            em.close();
        }
    }

    public int getClaseHasEstudianteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ClaseHasEstudiante> rt = cq.from(ClaseHasEstudiante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
