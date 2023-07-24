package solutions.b2.autorizador.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
public class NewCardRequest {

	private String cardNumber;
	
	private String password;
	
}
