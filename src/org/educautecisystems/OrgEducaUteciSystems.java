/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.educautecisystems;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.educautecisystems.controladores.AdministradorJpaController;

/**
 *
 * @author Shadow2012
 */
public class OrgEducaUteciSystems {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        EntityManagerFactory emf = null;
        emf = Persistence.createEntityManagerFactory("org.EducaUteciSystemsPU");
        AdministradorJpaController controladorAdministrador = new AdministradorJpaController(emf);
        
    }
}
