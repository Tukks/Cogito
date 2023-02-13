package com.tukks.cogito.controller;

import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tukks.cogito.dto.request.ThingsRequest;
import com.tukks.cogito.dto.response.ActionCard;
import com.tukks.cogito.dto.response.ActionEvent;
import com.tukks.cogito.dto.response.ActionType;
import com.tukks.cogito.entity.ThingsEntity;
import com.tukks.cogito.repository.ThingsRepository;
import com.tukks.cogito.service.NoteService;

import lombok.AllArgsConstructor;
import static com.tukks.cogito.service.SecurityUtils.getSub;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ThoughtController {

	private final Logger logger = LogManager.getLogger(getClass());
	private ApplicationEventPublisher applicationEventPublisher;
	private final ThingsRepository thingsRepository;
	private final NoteService noteService;

	@GetMapping("/thoughts")
	public List<? super ThingsEntity> getAllThoughts() {
		logger.info("Get all elements");
		return thingsRepository.getAll(getSub());
	}

	@GetMapping("/thoughts/{id}")
	public Object getThought(@PathVariable UUID id) {
		logger.info("Get all elements");
		return thingsRepository.getByIdAndOidcSub(id, getSub());
	}

	@PostMapping("/save")
	public Object save(@RequestBody ThingsRequest thingsRequest) {
		logger.info("Saving new note");
		Object saved = noteService.save(thingsRequest);

		applicationEventPublisher.publishEvent(new ActionEvent(this,
			ActionCard.builder().actionType(ActionType.ADD).id(null).card(saved).build()));

		return saved;
	}

	@PatchMapping("/{id}")
	public Object editThings(@PathVariable UUID id, @RequestBody ThingsRequest thingsRequest) {
		logger.info("Edit note, id : {}", id);

		Object edited = noteService.editThings(id, thingsRequest);

		applicationEventPublisher.publishEvent(new ActionEvent(this,
			ActionCard.builder().actionType(ActionType.EDIT).id(id).card(edited).build()));

		return edited;
	}

	record BatchTagsUpdate(List<UUID> ids, String tag) {}
	@PostMapping("/batch/tags")
	public void batchTags(@RequestBody BatchTagsUpdate batchTagsUpdate) {
		logger.info("Batch add tags to, id : {}, tag: {}", batchTagsUpdate.ids, batchTagsUpdate.tag);
		List<? super ThingsEntity> edited = noteService.editBatchThings(batchTagsUpdate.ids, batchTagsUpdate.tag);

		for(Object card: edited) {
			applicationEventPublisher.publishEvent(new ActionEvent(this,
				ActionCard.builder().actionType(ActionType.EDIT).id(((ThingsEntity)card).getId()).card(card).build()));
		}
	}

	@DeleteMapping("/{id}")
	public Integer delete(@PathVariable UUID id) {
		logger.info("Delete note, id : {}", id);
		Integer removed = noteService.delete(id);

		applicationEventPublisher.publishEvent(new ActionEvent(this,
			ActionCard.builder().actionType(ActionType.DELETE).id(id).card(null).build()));
		return removed;
	}

	@PostMapping("/batch/delete")
	public void batchDelete(@RequestBody List<UUID> ids) {
		logger.info("Delete note, id : {}", ids);
		noteService.delete(ids);
		for(UUID id: ids) {
			applicationEventPublisher.publishEvent(new ActionEvent(this,
				ActionCard.builder().actionType(ActionType.DELETE).id(id).card(null).build()));
		}
	}


}
