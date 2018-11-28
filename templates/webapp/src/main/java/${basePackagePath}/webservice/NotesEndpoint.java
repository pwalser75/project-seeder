package ${basePackage}.webservice;

import ${basePackage}.api.Note;
import ${basePackage}.api.NotesService;
import ${basePackage}.service.ServiceRegistry;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Example JAX-RS webservice web service
 *
 * @author wap
 * @since 2018-11-28
 */
@Path("/notes")
public class NotesEndpoint {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Note> listNotes() {
        return notesService().list();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Note getNote(@PathParam("id") long id) {
        return notesService().get(id);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Note createNote(String text) {
        Note note = new Note();
        note.setText(text);
        return notesService().create(note);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateNote(@PathParam("id") long id, Note note) {
        note.setId(id);
        notesService().update(note);
    }

    @DELETE
    @Path("/{id}")
    public void deleteNote(@PathParam("id") long id) {
        notesService().remove(id);
    }

    private NotesService notesService() {
        return ServiceRegistry.getInstance().get(NotesService.class);
    }
}
