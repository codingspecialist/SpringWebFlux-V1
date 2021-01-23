package com.cos.myreactor.controller;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FluxAndMonoController {
	
	@GetMapping("/flux")
	public Flux<Integer> returnFlux(){
		return Flux.just(1,2,3,4) 
				.delayElements(Duration.ofSeconds(1))
				.log();
	}
	
	// 참고 : https://stackoverflow.com/questions/52098863/whats-the-difference-between-text-event-stream-and-application-streamjson
	// APPLICATION_STREAM_JSON_VALUE (브라우저를 제외한 모든 통신) - 사용금지됨.
	// TEXT_EVENT_STREAM_VALUE (브라우저와 통신 Javascript EventSource API와 상호작용 가능)
	@GetMapping(value="/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Long> returnFluxStream(){
		return Flux.interval(Duration.ofSeconds(1)) 
				.log();
	}
	
	@GetMapping("/mono")
	public Mono<Integer> returnMono(){
		return Mono.just(1).log();
	}
	
	
}






