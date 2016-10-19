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

		emitter.onCompletion(new CompletionHandler(emitter));
		emitter.onTimeout(new TimeoutHandler(emitter));

		emitters.add(emitter);
		log.trace("New emitter created: {}", emitter);
		return emitter;
	}

	public void sendEvent(SseResource data) {
		for (SseEmitter emitter : emitters) {
			try {
				log.trace("Sending [{}] to emitter [{}]", data, emitter);
				emitter.send(SseEmitter.event().name("message").data(data));
			} catch (Exception e) {
				emitter.complete();
				emitters.remove(emitter);
				log.trace("Sending event to [" + emitter + "] failed", e.getMessage());
			}
		}
	}

	private class CompletionHandler implements Runnable {

		SseEmitter emitter;

		CompletionHandler(SseEmitter emitter) {
			this.emitter = emitter;
		}

		@Override
		public void run() {
			log.trace("{} is no longer usable", emitter);
			emitters.remove(emitter);
		}

	}

	private class TimeoutHandler implements Runnable {

		SseEmitter emitter;

		TimeoutHandler(SseEmitter emitter) {
			this.emitter = emitter;
		}

		@Override
		public void run() {
			log.trace("{} timed out", emitter);
		}

	}

}
