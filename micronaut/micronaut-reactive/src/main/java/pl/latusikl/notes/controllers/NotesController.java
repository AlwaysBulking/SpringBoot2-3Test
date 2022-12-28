package pl.latusikl.notes.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.latusikl.notes.dto.NoteDto;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.services.NotesService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@Validated
@Controller("/notes")
@RequiredArgsConstructor
public class NotesController {

	private final NotesService notesService;

	@Post
	public Mono<HttpResponse<NoteResponseDto>> createEncryptedNote(@Valid @Body final NoteDto noteDto) {
		return notesService.createNote(noteDto)
						   .map(HttpResponse::ok);
	}

	@Get(value = "/{noteId}", produces = MediaType.TEXT_PLAIN)
	public Mono<HttpResponse<String>> getQRCodeById(@PathVariable final String noteId, @QueryValue final String noteKey) {
		return notesService.getNote(noteId, noteKey)
						   .map(HttpResponse::ok);

	}
}
