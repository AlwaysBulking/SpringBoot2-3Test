package pl.latusikl.notes.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.latusikl.notes.dto.NoteDto;
import pl.latusikl.notes.dto.NoteResponseDto;
import pl.latusikl.notes.services.NotesService;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NotesController {

	private final NotesService notesService;

	@PostMapping
	public Mono<ResponseEntity<NoteResponseDto>> createEncryptedNote(@Valid @RequestBody final NoteDto codeDto) {
		return notesService.createNote(codeDto)
						   .map(ResponseEntity::ok);
	}

	@GetMapping(path = "/{codeId}", produces = MediaType.TEXT_PLAIN_VALUE)
	public Mono<ResponseEntity<String>> getQRCodeById(@PathVariable final String codeId, @RequestParam final String noteKey) {
		return notesService.getNote(codeId, noteKey)
						   .map(ResponseEntity::ok);

	}

}
