package it.codingjam.arquilliantests.logic.services;

import it.codingjam.arquilliantests.logic.models.Task;
import it.codingjam.arquilliantests.logic.models.User;
import it.codingjam.arquilliantests.logic.utils.TestArchive;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;

/**
 * Created by pizzo on 06/07/16.
 */
@RunWith(Arquillian.class)
public class TaskServiceIT {

    private static final Logger LOGGER = Logger.getLogger(TaskServiceIT.class.getName());

    @Deployment
    public static JavaArchive createDeployment() {
        return TestArchive.asEjbJar();
    }

    @Inject
    private TaskService taskService;

    @Inject
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @After
    public void cleanDb() throws Exception {
        try {
            userTransaction.begin();
            entityManager.createQuery("DELETE FROM Task").executeUpdate();
            entityManager.createQuery("DELETE FROM User").executeUpdate();
            userTransaction.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error cleaning db", e);
            userTransaction.rollback();
        }

    }

    @Test
    public void shouldNotFindTask() {
        Optional<Task> user = taskService.getBy(-1, "testUser");

        assertFalse(user.isPresent());
    }

    @Test
    public void shouldSaveNewTask() {
        Task task = newTask("first task", "admin");
        taskService.save(task);

        assertTrue(task.getId() > 0);
    }

    @Test
    public void shouldFindTaskById() {
        Task task = newTask("first task", "admin");
        taskService.save(task);

        Optional<Task> newTask = taskService.getBy(task.getId(), "admin");

        assertTrue(newTask.isPresent());
        assertEquals(task, newTask.get());
    }

    @Test
    public void shouldFindAllTasks() {
        taskService.save(newTask("first task", "admin1"));
        taskService.save(newTask("second task", "admin2"));
        taskService.save(newTask("third task", "admin3"));

        List<Task> allTasks = taskService.getAllTasks();

        assertFalse(allTasks.isEmpty());
        assertEquals(3, allTasks.size());
    }

    @Test
    public void shouldFindUsersTasks() {
        taskService.save(newTask("first task", "admin1"));
        taskService.save(newTask("second task", "admin2"));
        taskService.save(newTask("third task", "admin3"));

        List<Task> allUsersTasks = taskService.getAllTasks("admin1");

        assertFalse(allUsersTasks.isEmpty());
        assertEquals(1, allUsersTasks.size());
    }

    private Task newTask(String message, String userName) {
        Task task = new Task();
        task.setText(message);

        User user = new User(userName);
        user.setPassword(userName);
        task.setUser(user);

        return task;
    }
}