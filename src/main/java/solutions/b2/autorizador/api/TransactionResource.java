package solutions.b2.autorizador.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import solutions.b2.autorizador.dto.request.TransactionRequest;
import solutions.b2.autorizador.exception.InsufficientBalanceException;
import solutions.b2.autorizador.exception.InvalidCardException;
import solutions.b2.autorizador.exception.InvalidPasswordException;
import solutions.b2.autorizador.service.CardService;
import solutions.b2.autorizador.values.TransactionEnum;

@Path("/transactions")
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

	@Inject
	private CardService cardService;
	
	@POST
	public Response execute(TransactionRequest request) {
		try {
			cardService.charge(request);
		} catch (InvalidCardException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(TransactionEnum.INVALID_CARD.toString()).build();
		} catch (InvalidPasswordException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(TransactionEnum.INVALID_PASSWORD.toString()).build();
		} catch (InsufficientBalanceException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(TransactionEnum.INSUFFICIENT_BALANCE.toString()).build();
		}
		
		return Response.status(Response.Status.CREATED).entity(TransactionEnum.OK.toString()).build();
	}
}
