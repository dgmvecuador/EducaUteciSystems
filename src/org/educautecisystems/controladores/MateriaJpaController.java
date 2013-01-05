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
import org.educautecisystems.entidades.Clase;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.educautecisystems.controladores.exceptions.IllegalOrphanException;
import org.educautecisystems.controladores.exceptions.NonexistentEntityException;
import org.educautecisystems.entidades.Docente;
import org.educautecisystems.entidades.Materia;

/**
 *
 * @author Shadow2012
 */
public class MateriaJpaController implements Serializable {

    public MateriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Materia materia) {
        if (materia.getClaseList() == null) {
            materia.setClaseList(new ArrayList<Clase>());
        }
        if (materia.getDocenteList() == null) {
            materia.setDocenteList(new ArrayList<Docente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Clase> attachedClaseList = new ArrayList<Clase>();
            for (Clase claseListClaseToAttach : materia.getClaseList()) {
                claseListClaseToAttach = em.getReference(claseListClaseToAttach.getClass(), claseListClaseToAttach.getClasePK());
                attachedClaseList.add(claseListClaseToAttach);
            }
            materia.setClaseList(attachedClaseList);
            List<Docente> attachedDocenteList = new ArrayList<Docente>();
            for (Docente docenteListDocenteToAttach : materia.getDocenteList()) {
                docenteListDocenteToAttach = em.getReference(docenteListDocenteToAttach.getClass(), docenteListDocenteToAttach.getDocentePK());
                attachedDocenteList.add(docenteListDocenteToAttach);
            }
            materia.setDocenteList(attachedDocenteList);
            em.persist(materia);
            for (Clase claseListClase : materia.getClaseList()) {
                Materia oldMateriaOfClaseListClase = claseListClase.getMateria();
                claseListClase.setMateria(materia);
                claseListClase = em.merge(claseListClase);
                if (oldMateriaOfClaseListClase != null) {
                    oldMateriaOfClaseListClase.getClaseList().remove(claseListClase);
                    oldMateriaOfClaseListClase = em.merge(oldMateriaOfClaseListClase);
                }
            }
            for (Docente docenteListDocente : materia.getDocenteList()) {
                Materia oldMateriaOfDocenteListDocente = docenteListDocente.getMateria();
                docenteListDocente.setMateria(materia);
                docenteListDocente = em.merge(docenteListDocente);
                if (oldMateriaOfDocenteListDocente != null) {
                    oldMateriaOfDocenteListDocente.getDocenteList().remove(docenteListDocente);
                    oldMateriaOfDocenteListDocente = em.merge(oldMateriaOfDocenteListDocente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Materia materia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Materia persistentMateria = em.find(Materia.class, materia.getIdMateria());
            List<Clase> claseListOld = persistentMateria.getClaseList();
            List<Clase> claseListNew = materia.getClaseList();
            List<Docente> docenteListOld = persistentMateria.getDocenteList();
            List<Docente> docenteListNew = materia.getDocenteList();
            List<String> illegalOrphanMessages = null;
            for (Clase claseListOldClase : claseListOld) {
                if (!claseListNew.contains(claseListOldClase)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clase " + claseListOldClase + " since its materia field is not nullable.");
                }
            }
            for (Docente docenteListOldDocente : docenteListOld) {
                if (!docenteListNew.contains(docenteListOldDocente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Docente " + docenteListOldDocente + " since its materia field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Clase> attachedClaseListNew = new ArrayList<Clase>();
            for (Clase claseListNewClaseToAttach : claseListNew) {
                claseListNewClaseToAttach = em.getReference(claseListNewClaseToAttach.getClass(), claseListNewClaseToAttach.getClasePK());
                attachedClaseListNew.add(claseListNewClaseToAttach);
            }
            claseListNew = attachedClaseListNew;
            materia.setClaseList(claseListNew);
            List<Docente> attachedDocenteListNew = new ArrayList<Docente>();
            for (Docente docenteListNewDocenteToAttach : docenteListNew) {
                docenteListNewDocenteToAttach = em.getReference(docenteListNewDocenteToAttach.getClass(), docenteListNewDocenteToAttach.getDocentePK());
                attachedDocenteListNew.add(docenteListNewDocenteToAttach);
            }
            docenteListNew = attachedDocenteListNew;
            materia.setDocenteList(docenteListNew);
            materia = em.merge(materia);
            for (Clase claseListNewClase : claseListNew) {
                if (!claseListOld.contains(claseListNewClase)) {
                    Materia oldMateriaOfClaseListNewClase = claseListNewClase.getMateria();
                    claseListNewClase.setMateria(materia);
                    claseListNewClase = em.merge(claseListNewClase);
                    if (oldMateriaOfClaseListNewClase != null && !oldMateriaOfClaseListNewClase.equals(materia)) {
                        oldMateriaOfClaseListNewClase.getClaseList().remove(claseListNewClase);
                        oldMateriaOfClaseListNewClase = em.merge(oldMateriaOfClaseListNewClase);
                    }
                }
            }
            for (Docente docenteListNewDocente : docenteListNew) {
                if (!docenteListOld.contains(docenteListNewDocente)) {
                    Materia oldMateriaOfDocenteListNewDocente = docenteListNewDocente.getMateria();
                    docenteListNewDocente.setMateria(materia);
                    docenteListNewDocente = em.merge(docenteListNewDocente);
                    if (oldMateriaOfDocenteListNewDocente != null && !oldMateriaOfDocenteListNewDocente.equals(materia)) {
                        oldMateriaOfDocenteListNewDocente.getDocenteList().remove(docenteListNewDocente);
                        oldMateriaOfDocenteListNewDocente = em.merge(oldMateriaOfDocenteListNewDocente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = materia.getIdMateria();
                if (findMateria(id) == null) {
                    throw new NonexistentEntityException("The materia with id " + id + " no longer exists.");
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
            Materia materia;
            try {
                materia = em.getReference(Materia.class, id);
                materia.getIdMateria();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The materia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Clase> claseListOrphanCheck = materia.getClaseList();
            for (Clase claseListOrphanCheckClase : claseListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Clase " + claseListOrphanCheckClase + " in its claseList field has a non-nullable materia field.");
            }
            List<Docente> docenteListOrphanCheck = materia.getDocenteList();
            for (Docente docenteListOrphanCheckDocente : docenteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Materia (" + materia + ") cannot be destroyed since the Docente " + docenteListOrphanCheckDocente + " in its docenteList field has a non-nullable materia field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(materia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Materia> findMateriaEntities() {
        return findMateriaEntities(true, -1, -1);
    }

    public List<Materia> findMateriaEntities(int maxResults, int firstResult) {
        return findMateriaEntities(false, maxResults, firstResult);
    }

    private List<Materia> findMateriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Materia.class));
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

    public Materia findMateria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Materia.class, id);
        } finally {
            em.close();
        }
    }

    public int getMateriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Materia> rt = cq.from(Materia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
