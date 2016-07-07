package it.codingjam.arquilliantests.web.api;

import it.codingjam.arquilliantests.logic.models.Task;
import it.codingjam.arquilliantests.logic.services.TaskService;
import it.codingjam.arquilliantests.web.api.utils.API;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

/**
 *
 */
@Path(API.V1 + "/todos")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class TaskResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    private TaskService taskService;

    @GET
    @Path("/all")
    public List<Task> getAllOfAllUsers() {
        return taskService.getAllTasks();
    }

    @GET
    public List<Task> getAll(@QueryParam("user") String user) {
        return taskService.getAllTasks(user);
    }

    @GET
    @Path("{taskId}")
    public Task getTask(@NotNull @Min(1) @PathParam("taskId") long id, @QueryParam("user") String user) {
        return taskService.getBy(id, user)
                .orElseThrow(() -> new NotFoundException(String.format("Task with id %d not found", id)));
    }

    @POST
    public Response newTask(@Valid Task task) {
        taskService.save(task);
        URI location = URI.create(uriInfo.getAbsolutePath() + "/" + task.getId());
        return Response.created(location).entity(task).build();
    }

    @PUT
    @Path("{taskId}")
    public void updateTask(@NotNull @Min(1) @PathParam("taskId") long id, @Valid Task task) {
        if (task.getId() == 0) {
            task.setId(id);
        }
        taskService.update(task);
    }

    @DELETE
    @Path("{taskId}")
    public Response removeTask(@NotNull @Min(1) @PathParam("taskId") long id, @QueryParam("user") String user) {
        boolean deleted = taskService.deleteBy(id, user);
        if (deleted) {
            return Response.noContent().build();
        } else {
            throw new IllegalArgumentException(String.format("Task id %d cannot be deleted for user %s", id, user));
        }
    }

}
