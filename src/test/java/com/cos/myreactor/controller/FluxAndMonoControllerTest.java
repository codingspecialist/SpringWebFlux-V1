package com.cos.myreactor.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@WebFluxTest
public class FluxAndMonoControllerTest {
	
	@Autowired
	WebTestClient webTestClient;
	
	//@Test
	public void flux_approach1() {
		Flux<Integer> integerFlux = webTestClient.get().uri("/flux")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange() // 실제 호출
			.expectStatus().isOk()
			.returnResult(Integer.class)
			.getResponseBody();
		
		StepVerifier.create(integerFlux)
			.expectSubscription() // 구독이 되었다고 예상
			.expectNext(1)
			.expectNext(2)
			.expectNext(3)
			.expectNext(4)
			.verifyComplete();	
	}
	
	//@Test
	public void flux_approach2() {
		webTestClient.get().uri("/flux")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange() // 실제 호출
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBodyList(Integer.class)
				.hasSize(4);
	}
	
	//@Test
	public void flux_approach3() {
		
		List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);
		
		EntityExchangeResult<List<Integer>> entityExchangeResult =  webTestClient.get().uri("/flux")
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange() // 실제 호출
		.expectStatus().isOk()
		.expectBodyList(Integer.class)
		.returnResult();
		
		assertEquals(expectedIntegerList, entityExchangeResult.getResponseBody());
	}
	
	//@Test
	public void flux_approach4() {
		
		List<Integer> expectedIntegerList = Arrays.asList(1,2,3,4);
		
		webTestClient.get().uri("/flux")
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.exchange() // 실제 호출
		.expectStatus().isOk()
		.expectBodyList(Integer.class)
		.consumeWith((r)-> assertEquals(expectedIntegerList, r.getResponseBody()));
	}
	
	//@Test
	public void fluxstream() {
		Flux<Long> longStreamFlux = webTestClient.get().uri("/fluxstream")
				.accept(MediaType.APPLICATION_STREAM_JSON)
				.exchange() // 실제 호출
				.expectStatus().isOk()
				.returnResult(Long.class)
				.getResponseBody();
		
		StepVerifier.create(longStreamFlux)
		.expectNext(0L)
		.expectNext(1L)
		.expectNext(2L)
		.thenCancel() // 구독 중지 시키기 (Flux니까)
		.verify();
	}

	@Test
	public void mono() {
		
		Integer expectedValue = new Integer(1);
		
		webTestClient.get().uri("/mono")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.exchange() // 실제 호출
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
				.expectBody(Integer.class)
				.consumeWith((r)-> assertEquals(expectedValue, r.getResponseBody()));
	}
}







