package com.mendel.service_transaction.integration.infraestructure.entrypoint.controller;

import static com.mendel.service_transaction.integration.infraestructure.entrypoint.utils.ReaderUtil.readResource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mendel.service_transaction.infraestructure.entrypoint.adapter.InMemoryTransactionRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class TransactionControllerIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private InMemoryTransactionRepository inMemoryTransactionRepository;

	@BeforeEach
	void setUp() {
		inMemoryTransactionRepository.clear();
	}

	@ParameterizedTest
	@CsvSource({ "/transactions/1,json/create/create-cars.json,ok",
				 "/transactions/2,json/create/create-shopping.json,ok" })
	void shouldCreateTransactionSuccessfully(
			String transactionPath,
			String jsonPath,
			String expecteStatus) {
		
		webTestClient.put()
			.uri(transactionPath)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(jsonPath))
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.jsonPath("$.status")
		.isEqualTo(expecteStatus);
	}

	@ParameterizedTest
	@CsvSource({ "/transactions/3,json/create/create-invalid-parent.json,400" })
	void shouldReturnBadRequestWhenParentDoesNotExist(
			String transactionPath,
			String jsonPath,
			int expecteStatus) {
		
		webTestClient.put()
			.uri(transactionPath)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(jsonPath))
			.exchange()
			.expectStatus()
			.isEqualTo(expecteStatus)
			.expectBody()
			.jsonPath("$.message")
		.exists();
	}

	@ParameterizedTest
	@CsvSource({ "/transactions/10,json/create/create-cars.json,json/create/create-shopping.json,409" })
	void shouldReturnConflictWhenTransactionAlreadyExists(
			String transactionPath,
			String firstJsonPath,
			String secondJsonPath,
			int expectedStatus) {
		
		webTestClient.put()
			.uri(transactionPath)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(firstJsonPath))
			.exchange()
			.expectStatus()
			.isOk();

		webTestClient.put()
			.uri(transactionPath)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(secondJsonPath))
			.exchange()
			.expectStatus()
			.isEqualTo(expectedStatus)
			.expectBody()
			.jsonPath("$.message")
		.exists();
	}
	
	@ParameterizedTest
	@CsvSource({
	    "/transactions/types/cars,'[10,12]',/transactions/10,json/setup/tx-10-cars.json,/transactions/11,json/setup/tx-11-shopping.json,/transactions/12,json/setup/tx-12-cars-child.json",
	    "/transactions/types/shopping,'[11]',/transactions/10,json/setup/tx-10-cars.json,/transactions/11,json/setup/tx-11-shopping.json,/transactions/12,json/setup/tx-12-cars-child.json",
	    "/transactions/types/travel,'[]',/transactions/10,json/setup/tx-10-cars.json,/transactions/11,json/setup/tx-11-shopping.json,/transactions/12,json/setup/tx-12-cars-child.json"
	})
	void shouldReturnTransactionIdsByType(
	        String typePath,
	        String expectedJson,
	        String txPath1,
	        String txJson1,
	        String txPath2,
	        String txJson2,
	        String txPath3,
	        String txJson3
	) {
	    webTestClient.put()
	            .uri(txPath1)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(readResource(txJson1))
	            .exchange()
	            .expectStatus().isOk();

	    webTestClient.put()
	            .uri(txPath2)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(readResource(txJson2))
	            .exchange()
	            .expectStatus().isOk();

	    webTestClient.put()
	            .uri(txPath3)
	            .contentType(MediaType.APPLICATION_JSON)
	            .bodyValue(readResource(txJson3))
	            .exchange()
	            .expectStatus().isOk();

	    webTestClient.get()
	            .uri(typePath)
	            .exchange()
	            .expectStatus().isOk()
	            .expectBody()
	            .json(expectedJson);
	}

	@ParameterizedTest
	@CsvSource({
			"/transactions/sum/20,10000.0,/transactions/20,json/setup/tx-20.json,/transactions/21,json/setup/tx-21.json,/transactions/22,json/setup/tx-22.json",
			"/transactions/sum/21,5000.0,/transactions/20,json/setup/tx-20.json,/transactions/21,json/setup/tx-21.json,/transactions/22,json/setup/tx-22.json" })
	void shouldReturnTransitiveSum(
			String sumPath,
			double expectedSum,
			String transactionPath1,
			String jsonPath1,
			String transactionPath2,
			String jsonPath2,
			String transactionPath3,
			String jsonPath3) {
		
		webTestClient.put()
			.uri(transactionPath1)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(jsonPath1))
			.exchange()
			.expectStatus()
			.isOk();

		webTestClient.put()
			.uri(transactionPath2)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(jsonPath2))
			.exchange()
			.expectStatus()
			.isOk();

		webTestClient.put()
			.uri(transactionPath3)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(readResource(jsonPath3))
			.exchange()
			.expectStatus()
			.isOk();

		webTestClient.get()
			.uri(sumPath)
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody()
			.jsonPath("$.sum")
		.isEqualTo(expectedSum);
	}
}