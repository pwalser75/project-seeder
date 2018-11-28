package ${basePackage}.webservice.config.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.NoSuchElementException;

@Provider
public class NoSuchElementExceptionMapper implements ExceptionMapper<NoSuchElementException> {

    public Response toResponse(NoSuchElementException ex) {
        return Response.status(Response.Status.NOT_FOUND).entity(ex.getMessage()).build();
    }
}