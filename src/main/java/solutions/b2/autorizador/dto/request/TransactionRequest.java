package solutions.b2.autorizador.dto.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
public class TransactionRequest {

	private String cardNumber;
	
	private String password;
	
	private BigDecimal amount;
	
}
