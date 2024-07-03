package it.florence.assignment.rest;

import it.florence.assignment.model.MultipartBody;
import it.florence.assignment.model.UserDTO;
import it.florence.assignment.service.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    public Response getAllUsers() {
        return Response.ok(userService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        return Response.ok(userService.get(id)).build();
    }

    @GET
    @Path("/getByName")
    public Response findUsersByNameSurname(@QueryParam(value = "name") @Valid String name,
                                                                @QueryParam(value = "surname") @Valid String surname) {
        return Response.ok(userService.findByNameSurname(name,surname)).build();
    }

    @POST
    public Response createUser(@Valid UserDTO userDTO) {
        userService.create(userDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    //todo: TEST ME
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/csv")
    public Response uploadCSV(@MultipartForm MultipartBody data) {

        userService.saveFromCSV(data);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid UserDTO userDTO) {
        userService.update(id, userDTO);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        userService.delete(id);
        return Response.noContent().build();
    }

    @DELETE
    public Response deleteAll(){
        userService.deleteAll();
        return Response.noContent().build();
    }

}
