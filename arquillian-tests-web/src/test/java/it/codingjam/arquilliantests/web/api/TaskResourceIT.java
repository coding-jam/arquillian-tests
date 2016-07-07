package it.codingjam.arquilliantests.web.api;

import it.codingjam.arquilliantests.logic.models.Task;
import it.codingjam.arquilliantests.logic.models.User;
import it.codingjam.arquilliantests.logic.services.TaskService;
import it.codingjam.arquilliantests.utils.TestArchive;
import it.codingjam.arquilliantests.utils.TestDbUtils;
import it.codingjam.arquilliantests.web.api.utils.API;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by pizzo on 07/07/16.
 */
@RunWith(Arquillian.class)
public class TaskResourceIT {

    @Inject
    private TestDbUtils testDbUtils;

    @Inject
    private TaskService taskService;

    @Deployment
    public static WebArchive createDeployment() {
        return TestArchive.asWar();
    }

    @Before
    public void beforeTest() {
        taskService.save(newTask("first task", "user1"));
        taskService.save(newTask("first task", "user2"));
        taskService.save(newTask("first task", "user3"));
        taskService.save(newTask("first task", "user4"));
    }

    @After
    public void afterTest() throws Exception {
        testDbUtils.cleanDb();
    }

    @Test
    public void shouldGetAllTasksForAllUsers() {
        Response response = getTarget().path("todos/all")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertNotNull(response);
        assertEquals(200, response.getStatus());

        List<Task> tasks = response.readEntity(new GenericType<List<Task>>() {
        });

        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(4, tasks.size());
    }

    @Test
    public void shouldGetTasksForUser1() {
        Response response = getTarget().path("todos/1").queryParam("user", "user1")
                .request()
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .get();

        assertNotNull(response);
        assertEquals(200, response.getStatus());

        Task task = response.readEntity(Task.class);

        assertNotNull(task);
        assertEquals(1, task.getId());
        assertEquals("first task", task.getText());
        assertEquals("user1", task.getUser().getUserName());
    }

    private WebTarget getTarget() {
        return ClientBuilder.newClient().target("http://localhost:8080/"
                + TestArchive.CONTEXT_PATH
                + API.CONTEXT
                + API.V1);
    }

    private Task newTask(String message, String userName) {
        User user = new User(userName);
        user.setPassword(userName);

        Task task = new Task();
        task.setText(message);
        task.setUser(user);

        return task;
    }
}