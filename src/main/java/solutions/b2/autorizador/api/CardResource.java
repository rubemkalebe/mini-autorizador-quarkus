package solutions.b2.autorizador.api;

import java.net.URI;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import solutions.b2.autorizador.dto.request.NewCardRequest;
import solutions.b2.autorizador.entity.Card;
import solutions.b2.autorizador.exception.CardNumberAlreadyInUseException;
import solutions.b2.autorizador.service.CardService;

@Path("/cards")
@Consumes(MediaType.APPLICATION_JSON)
public class CardResource {

	@Inject
	private CardService cardService;
	
	@POST
	public Response createCard(NewCardRequest request, @Context UriInfo uriInfo) {
		Card card = Card.builder()
				.number(request.getCardNumber())
				.password(request.getPassword())
				.build();
		
		try {
			card = cardService.create(card);
		} catch(CardNumberAlreadyInUseException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
		
		URI uri = uriInfo.getAbsolutePathBuilder().path(card.getCode().toString()).build();
		return Response.created(uri).entity(card).build();
	}
	
	@GET
	@Path("/{cardNumber}")
	public Response getBalance(@PathParam("cardNumber") String cardNumber) {
		Card card = cardService.findByNumber(cardNumber);
		
		if(card == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		return Response.ok(card.getBalance()).build();
	}
	
}
