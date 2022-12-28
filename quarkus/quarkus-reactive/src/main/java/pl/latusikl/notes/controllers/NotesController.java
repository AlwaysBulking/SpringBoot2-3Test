package pl.latusikl.notes.controllers;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;
import org.jboss.resteasy.reactive.RestResponse;
import pl.latusikl.notes.dto.NoteDto;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.services.NotesService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@RequiredArgsConstructor
@Path("/notes")
public class NotesController {

	private final NotesService notesService;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Uni<RestResponse<NoteResponseDto>> createNote(final NoteDto noteDto) {
		return notesService.createNote(noteDto)
						   .map(RestResponse::ok);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(value = "/{noteId}")
	public Uni<RestResponse<String>> getNote(@PathParam("noteId") final String noteId, @QueryParam("noteKey") final String noteKey) {
		return notesService.getNote(noteId, noteKey)
						   .map(RestResponse::ok);

	}
}
