package ourbusinessproject;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class EnterpriseProjectService {

    @PersistenceContext
    private EntityManager entityManager;

    public Project saveProjectForEnterprise(Project project, Enterprise enterprise) {
        saveEnterprise(enterprise);
        if (project.getEnterprise() != null) {
        	project.getEnterprise().removeProject(project);
        }
        project.setEnterprise(enterprise);
        enterprise.addProject(project);
        entityManager.persist(project);
        entityManager.flush();
        return project;
    }

    public Enterprise saveEnterprise(Enterprise enterprise) {
    	Enterprise newEnterprise = entityManager.merge(enterprise);
    	entityManager.flush();
    	return newEnterprise;
    }
    
    public Project findProjectById(Long id) {
        return entityManager.find(Project.class, id);
    }

    public Enterprise findEnterpriseById(Long id) {
        return entityManager.find(Enterprise.class, id);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Project> findAllProjects() {
        TypedQuery<Project> query = entityManager.createQuery("select p from Project p join fetch p.enterprise order by p.title", Project.class);
        return query.getResultList();
    }
}
