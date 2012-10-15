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
import org.educautecisystems.entidades.ClaseHasEstudiante;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.educautecisystems.controladores.exceptions.IllegalOrphanException;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.controladores.exceptions.PreexistingEntityException;
import org.educautecisystems.entidades.Clase;
import org.educautecisystems.entidades.ClasePK;

/**
 *
 * @author Shadow2012
 */
public class ClaseJpaController implements Serializable {

    public ClaseJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clase clase) throws PreexistingEntityException, Exception {
        if (clase.getClasePK() == null) {
            clase.setClasePK(new ClasePK());
        }
        if (clase.getClaseHasEstudianteList() == null) {
            clase.setClaseHasEstudianteList(new ArrayList<ClaseHasEstudiante>());
        }
        clase.getClasePK().setMateriaIdMateria(clase.getMateria().getIdMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia materia = clase.getMateria();
            if (materia != null) {
                materia = em.getReference(materia.getClass(), materia.getIdMateria());
                clase.setMateria(materia);
            }
            List<ClaseHasEstudiante> attachedClaseHasEstudianteList = new ArrayList<ClaseHasEstudiante>();
            for (ClaseHasEstudiante claseHasEstudianteListClaseHasEstudianteToAttach : clase.getClaseHasEstudianteList()) {
                claseHasEstudianteListClaseHasEstudianteToAttach = em.getReference(claseHasEstudianteListClaseHasEstudianteToAttach.getClass(), claseHasEstudianteListClaseHasEstudianteToAttach.getClaseHasEstudiantePK());
                attachedClaseHasEstudianteList.add(claseHasEstudianteListClaseHasEstudianteToAttach);
            }
            clase.setClaseHasEstudianteList(attachedClaseHasEstudianteList);
            em.persist(clase);
            if (materia != null) {
                materia.getClaseList().add(clase);
                materia = em.merge(materia);
            }
            for (ClaseHasEstudiante claseHasEstudianteListClaseHasEstudiante : clase.getClaseHasEstudianteList()) {
                Clase oldClaseOfClaseHasEstudianteListClaseHasEstudiante = claseHasEstudianteListClaseHasEstudiante.getClase();
                claseHasEstudianteListClaseHasEstudiante.setClase(clase);
                claseHasEstudianteListClaseHasEstudiante = em.merge(claseHasEstudianteListClaseHasEstudiante);
                if (oldClaseOfClaseHasEstudianteListClaseHasEstudiante != null) {
                    oldClaseOfClaseHasEstudianteListClaseHasEstudiante.getClaseHasEstudianteList().remove(claseHasEstudianteListClaseHasEstudiante);
                    oldClaseOfClaseHasEstudianteListClaseHasEstudiante = em.merge(oldClaseOfClaseHasEstudianteListClaseHasEstudiante);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findClase(clase.getClasePK()) != null) {
                throw new PreexistingEntityException("Clase " + clase + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clase clase) throws IllegalOrphanException, NonexistentEntityException, Exception {
        clase.getClasePK().setMateriaIdMateria(clase.getMateria().getIdMateria());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase persistentClase = em.find(Clase.class, clase.getClasePK());
            Materia materiaOld = persistentClase.getMateria();
            Materia materiaNew = clase.getMateria();
            List<ClaseHasEstudiante> claseHasEstudianteListOld = persistentClase.getClaseHasEstudianteList();
            List<ClaseHasEstudiante> claseHasEstudianteListNew = clase.getClaseHasEstudianteList();
            List<String> illegalOrphanMessages = null;
            for (ClaseHasEstudiante claseHasEstudianteListOldClaseHasEstudiante : claseHasEstudianteListOld) {
                if (!claseHasEstudianteListNew.contains(claseHasEstudianteListOldClaseHasEstudiante)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ClaseHasEstudiante " + claseHasEstudianteListOldClaseHasEstudiante + " since its clase field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (materiaNew != null) {
                materiaNew = em.getReference(materiaNew.getClass(), materiaNew.getIdMateria());
                clase.setMateria(materiaNew);
            }
            List<ClaseHasEstudiante> attachedClaseHasEstudianteListNew = new ArrayList<ClaseHasEstudiante>();
            for (ClaseHasEstudiante claseHasEstudianteListNewClaseHasEstudianteToAttach : claseHasEstudianteListNew) {
                claseHasEstudianteListNewClaseHasEstudianteToAttach = em.getReference(claseHasEstudianteListNewClaseHasEstudianteToAttach.getClass(), claseHasEstudianteListNewClaseHasEstudianteToAttach.getClaseHasEstudiantePK());
                attachedClaseHasEstudianteListNew.add(claseHasEstudianteListNewClaseHasEstudianteToAttach);
            }
            claseHasEstudianteListNew = attachedClaseHasEstudianteListNew;
            clase.setClaseHasEstudianteList(claseHasEstudianteListNew);
            clase = em.merge(clase);
            if (materiaOld != null && !materiaOld.equals(materiaNew)) {
                materiaOld.getClaseList().remove(clase);
                materiaOld = em.merge(materiaOld);
            }
            if (materiaNew != null && !materiaNew.equals(materiaOld)) {
                materiaNew.getClaseList().add(clase);
                materiaNew = em.merge(materiaNew);
            }
            for (ClaseHasEstudiante claseHasEstudianteListNewClaseHasEstudiante : claseHasEstudianteListNew) {
                if (!claseHasEstudianteListOld.contains(claseHasEstudianteListNewClaseHasEstudiante)) {
                    Clase oldClaseOfClaseHasEstudianteListNewClaseHasEstudiante = claseHasEstudianteListNewClaseHasEstudiante.getClase();
                    claseHasEstudianteListNewClaseHasEstudiante.setClase(clase);
                    claseHasEstudianteListNewClaseHasEstudiante = em.merge(claseHasEstudianteListNewClaseHasEstudiante);
                    if (oldClaseOfClaseHasEstudianteListNewClaseHasEstudiante != null && !oldClaseOfClaseHasEstudianteListNewClaseHasEstudiante.equals(clase)) {
                        oldClaseOfClaseHasEstudianteListNewClaseHasEstudiante.getClaseHasEstudianteList().remove(claseHasEstudianteListNewClaseHasEstudiante);
                        oldClaseOfClaseHasEstudianteListNewClaseHasEstudiante = em.merge(oldClaseOfClaseHasEstudianteListNewClaseHasEstudiante);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ClasePK id = clase.getClasePK();
                if (findClase(id) == null) {
                    throw new NonexistentEntityException("The clase with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ClasePK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clase clase;
            try {
                clase = em.getReference(Clase.class, id);
                clase.getClasePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clase with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ClaseHasEstudiante> claseHasEstudianteListOrphanCheck = clase.getClaseHasEstudianteList();
            for (ClaseHasEstudiante claseHasEstudianteListOrphanCheckClaseHasEstudiante : claseHasEstudianteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clase (" + clase + ") cannot be destroyed since the ClaseHasEstudiante " + claseHasEstudianteListOrphanCheckClaseHasEstudiante + " in its claseHasEstudianteList field has a non-nullable clase field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Materia materia = clase.getMateria();
            if (materia != null) {
                materia.getClaseList().remove(clase);
                materia = em.merge(materia);
            }
            em.remove(clase);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clase> findClaseEntities() {
        return findClaseEntities(true, -1, -1);
    }

    public List<Clase> findClaseEntities(int maxResults, int firstResult) {
        return findClaseEntities(false, maxResults, firstResult);
    }

    private List<Clase> findClaseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clase.class));
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

    public Clase findClase(ClasePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clase.class, id);
        } finally {
            em.close();
        }
    }

    public int getClaseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clase> rt = cq.from(Clase.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
