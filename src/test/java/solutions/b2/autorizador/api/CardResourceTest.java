package solutions.b2.autorizador.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import solutions.b2.ConfigurateTestEnvironment;
import solutions.b2.autorizador.dto.request.NewCardRequest;
import solutions.b2.autorizador.entity.Card;
import solutions.b2.autorizador.service.CardService;

@QuarkusTest
@QuarkusTestResource(ConfigurateTestEnvironment.class)
@TestMethodOrder(OrderAnnotation.class)
public class CardResourceTest {

	@Inject
	private CardService cardService;
	
	@Test
	@Order(1)
	public void tentaCriarNovoCartaoSemNumero() {
		NewCardRequest request = NewCardRequest.builder()
				.password("1234")
				.build();
		
		given()
			.contentType(ContentType.JSON)
			.body(request)
			.accept(ContentType.JSON)
		.when()
			.post("/cards")
		.then()
			.assertThat()
				.statusCode(HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	@Order(2)
	public void tentaCriarNovoCartaoSemSenha() {
		NewCardRequest request = NewCardRequest.builder()
				.cardNumber("6549873025634501")
				.build();
		
		given()
			.contentType(ContentType.JSON)
			.body(request)
			.accept(ContentType.JSON)
		.when()
			.post("/cards")
		.then()
			.assertThat()
				.statusCode(HttpStatus.SC_BAD_REQUEST);
	}
	
	@Test
	@Order(3)
	public void criarNovoCartao() {
		NewCardRequest request = NewCardRequest.builder()
				.cardNumber("6549873025634501")
				.password("1234")
				.build();
		
		given()
			.contentType(ContentType.JSON)
			.body(request)
			.accept(ContentType.JSON)
		.when()
			.post("/cards")
		.then()
			.assertThat()
				.statusCode(HttpStatus.SC_CREATED)
				.body("number", equalTo("6549873025634501"));
		
		Card found = cardService.findByNumber("6549873025634501");
		assertTrue(BcryptUtil.matches("1234", found.getPassword()));
		assertEquals("500.00", found.getBalance().toPlainString());
	}
	
	@Test
	@Order(4)
	public void tentaCriarCartaoComMesmoNumero() {
		NewCardRequest request = NewCardRequest.builder()
				.cardNumber("6549873025634501")
				.password("1234")
				.build();
		
		given()
			.contentType(ContentType.JSON)
			.body(request)
			.accept(ContentType.JSON)
		.when()
			.post("/cards")
		.then()
			.assertThat()
				.statusCode(HttpStatus.SC_BAD_REQUEST)
				.body("cardNumber", equalTo("6549873025634501"))
				.body("password", equalTo("1234"));
	}
	
	@Test
	@Order(5)
	public void tentaObterSaldoMasCartaoInexistente() {
		String cardNumber = "4501256387306548";
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/cards/" + cardNumber)
		.then()
			.assertThat()
				.statusCode(HttpStatus.SC_NOT_FOUND);
	}
	
	@Test
	@Order(6)
	public void obtemSaldoDeCartao() {
		String cardNumber = "4501256387306549";
		
		
		String balance = given()
				.when()
					.get("/cards/" + cardNumber)
					.asString();
		
		assertEquals("495.15", balance);
	}
	
}
