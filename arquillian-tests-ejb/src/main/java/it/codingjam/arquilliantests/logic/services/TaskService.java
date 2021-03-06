package it.codingjam.arquilliantests.logic.services;

import it.codingjam.arquilliantests.logic.models.Task;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 */
@Stateless
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(TaskService.class.getName());

    @Inject
    private EntityManager entityManager;

    @PostConstruct
    void init() {
        LOGGER.info("Init task service with hash " + hashCode());
    }

    public Optional<Task> getBy(long id, String userName) {
        try {
            Task task = this.entityManager.createNamedQuery(Task.GET_FOR_USER_BY_ID, Task.class)
                    .setParameter("id", id)
                    .setParameter("userName", userName)
                    .getSingleResult();
            return Optional.of(task);
        } catch (PersistenceException e) {
            return Optional.empty();
        }
    }

    public List<Task> getAllTasks() {
        return this.entityManager.createNamedQuery(Task.GET_ALL, Task.class).getResultList();
    }

    public List<Task> getAllTasks(String userName) {
        return this.entityManager.createNamedQuery(Task.GET_ALL_FOR_USER, Task.class)
                .setParameter("userName", userName)
                .getResultList();
    }

    public void save(Task task) {
        LOGGER.info(String.format("Saving new task %s", task));
        this.entityManager.persist(task);
    }

    public boolean deleteBy(long id, String userName) {
        LOGGER.info(String.format("Removing task %s", id));
        int updatedEntities = this.entityManager.createNamedQuery("Task.remove")
                .setParameter("id", id)
                .setParameter("userName", userName)
                .executeUpdate();
        return updatedEntities > 0;
    }

    public void update(Task task) {
        LOGGER.info(String.format("Updating task %s", task.getId()));
        this.entityManager.merge(task);
    }
}
