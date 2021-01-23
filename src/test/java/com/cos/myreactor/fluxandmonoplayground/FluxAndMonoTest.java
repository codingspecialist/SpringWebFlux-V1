package com.cos.myreactor.fluxandmonoplayground;

import org.junit.jupiter.api.Test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


public class FluxAndMonoTest {
 
	//@Test
	public void fluxTest() {
		Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactice Spring")
				//.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
				.concatWith(Flux.just("After Error")) // 실행되지 않음 onError 후에는 onNext가 발생하지 않음.
				.log();
		stringFlux
			.subscribe((s)-> System.out.println(s+"~~~~~~"), (e)-> System.err.print("Exception is "+e), ()->System.out.println("Completed"));
	}
	
	//@Test
	public void fluxTestElements_withoutError() {
		Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactice Spring")
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNext("Spring")
		.expectNext("Spring boot")
		.expectNext("Reactice Spring")
		.verifyComplete();
	}
	
	//@Test
	public void fluxTestElements_withError() {
		Flux<String> stringFlux = Flux.just("Spring", "Spring boot", "Reactice Spring")
				.concatWith(Flux.error(new RuntimeException("Exception Occurred")))
				.log();
		
		StepVerifier.create(stringFlux)
		.expectNext("Spring")
		.expectNext("Spring boot")
		.expectNext("Reactice Spring")
		//.expectError(RuntimeException.class)
		.expectErrorMessage("Exception Occurred")
		.verify();
	}
	
	@Test
	public void monoTest() {
		Mono<String> stringMono = Mono.just("Spring").log();
		
		StepVerifier.create(stringMono)
			.expectNext("Spring")
			.verifyComplete();
	}
}






