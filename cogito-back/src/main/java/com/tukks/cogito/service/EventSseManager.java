package com.tukks.cogito.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class EventSseManager {

	private final Logger logger = LogManager.getLogger(getClass());

	final ConcurrentHashMap<String, Map<UUID, SseEmitter>> sseEmitters = new ConcurrentHashMap<>();

	public SseEmitter registerSseEmitter(String sub) {
		final UUID id = UUID.randomUUID();
		SseEmitter emitter = new SseEmitter(-1L);

		if (this.sseEmitters.get(sub) == null) {
			Map<UUID, SseEmitter> sseEmitterToAdd = new HashMap<>();
			sseEmitterToAdd.put(id, emitter);
			this.sseEmitters.put(sub, sseEmitterToAdd);
		} else {
			Map<UUID, SseEmitter> sseEmitterList = this.sseEmitters.get(sub);
			sseEmitterList.put(id, emitter);
		}

		return emitter;

	}

//	public synchronized void unregisterSseEmitter(String sub, UUID id) {
//		this.sseEmitters.get(sub).remove(id);
//	}

	public void sendEventToSseEmitter(String sub, Object data) {
		Map<UUID, SseEmitter> sseEmittersMap = this.sseEmitters.get(sub);

		List<UUID> toRemove = new ArrayList<>();

		sseEmittersMap.forEach((id, sseEmitter) -> {
			try {
				sseEmitter.send(data);
			} catch (Exception e) {
				toRemove.add(id);
			}
		});

		toRemove.forEach(uuid -> this.sseEmitters.get(sub).remove(uuid));

	}
}
