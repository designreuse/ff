package org.ff.sse.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.ff.sse.resource.SseResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SseService {

	private List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	public SseEmitter getEmitter() {
		SseEmitter emitter = new SseEmitter();
		emitter.onCompletion(() -> emitters.remove(emitter));
		emitters.add(emitter);
		log.debug("New emitter created: {}", emitter);
		return emitter;
	}

	public void sendEvent(SseResource data) {
		for (SseEmitter emitter : emitters) {
			try {
				log.debug("Sending [{}] to emitter [{}]", data, emitter);
				emitter.send(SseEmitter.event().name("FundFinder").data(data));
			} catch (Exception e) {
				log.warn("Sending event to [" + emitter + "] failed", e.getMessage());
			}
		}
	}

}
