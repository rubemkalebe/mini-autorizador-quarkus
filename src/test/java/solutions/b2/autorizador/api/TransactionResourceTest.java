package solutions.b2.autorizador.api;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import solutions.b2.ConfigurateTestEnvironment;
import solutions.b2.autorizador.dto.request.TransactionRequest;
import solutions.b2.autorizador.entity.Card;
import solutions.b2.autorizador.service.CardService;
import solutions.b2.autorizador.values.TransactionEnum;

@QuarkusTest
@QuarkusTestResource(ConfigurateTestEnvironment.class)
@TestMethodOrder(OrderAnnotation.class)
public class TransactionResourceTest {

	@Inject
	private CardService cardService;
	
	@Test
	public void tentaUsarCartaoInexistente() {
		TransactionRequest request = TransactionRequest.builder()
				.cardNumber("6549873025634502")
				.password("1234")
				.amount(BigDecimal.valueOf(10.00))
				.build();
		
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.accept(ContentType.JSON)
			.when()
				.post("/transactions");
			
			assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
			assertEquals(TransactionEnum.INVALID_CARD.toString(), response.getBody().asString());
	}
	
	@Test
	public void tentaUsarCartaoComSenhaInvalida() {
		TransactionRequest request = TransactionRequest.builder()
				.cardNumber("6549873025634501")
				.password("4321")
				.amount(BigDecimal.valueOf(10.00))
				.build();
		
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.accept(ContentType.JSON)
			.when()
				.post("/transactions");
			
			assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
			assertEquals(TransactionEnum.INVALID_PASSWORD.toString(), response.getBody().asString());
	}
	
	@Test
	public void tentaUsarCartaoComSaldoInsuficiente() {
		TransactionRequest request = TransactionRequest.builder()
				.cardNumber("6549873025634501")
				.password("1234")
				.amount(BigDecimal.valueOf(500.01))
				.build();
		
		Response response = given()
			.contentType(ContentType.JSON)
			.body(request)
			.accept(ContentType.JSON)
		.when()
			.post("/transactions");
		
		assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusCode());
		assertEquals(TransactionEnum.INSUFFICIENT_BALANCE.toString(), response.getBody().asString());
	}
	
	@Test
	public void debitaComSucesso() {
		TransactionRequest request = TransactionRequest.builder()
				.cardNumber("6549873025634501")
				.password("1234")
				.amount(BigDecimal.valueOf(10.01))
				.build();
		
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.accept(ContentType.JSON)
			.when()
				.post("/transactions");
			
			assertEquals(HttpStatus.SC_CREATED, response.getStatusCode());
			assertEquals(TransactionEnum.OK.toString(), response.getBody().asString());
		
		Card found = cardService.findByNumber("6549873025634501");
		assertEquals("489.99", found.getBalance().toPlainString());
	}
	
}
