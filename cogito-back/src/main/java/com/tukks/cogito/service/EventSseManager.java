package com.tukks.cogito.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class EventSseManager {

	HashMap<String, List<SseEmitter>> sseEmitters = new HashMap<>();

	public SseEmitter registerSseEmitter(String sub) {

		SseEmitter emitter = new SseEmitter(-1L);

		emitter.onCompletion(() -> sseEmitters.remove(sub));
		emitter.onTimeout(() -> sseEmitters.remove(sub));
		emitter.onError(throwable -> sseEmitters.remove(sub));

		if (this.sseEmitters.get(sub) == null) {
			List<SseEmitter> sseEmitterToAdd = new ArrayList<>();
			sseEmitterToAdd.add(emitter);
			this.sseEmitters.put(sub, sseEmitterToAdd);
		} else {
			List<SseEmitter> sseEmitterList = this.sseEmitters.get(sub);
			sseEmitterList.add(emitter);
		}

		return emitter;

	}

	public void unregisterSseEmitter(String sub) {
		// TODO
		this.sseEmitters.remove(sub);
	}

	public void sendEventToSseEmiter(String sub, Object data) {
		List<SseEmitter> sseEmitters = this.sseEmitters.get(sub);

		sseEmitters.forEach(sseEmitter -> {
			try {
				sseEmitter.send(data);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});

	}
}
