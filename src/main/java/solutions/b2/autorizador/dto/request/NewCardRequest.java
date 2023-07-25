package solutions.b2.autorizador.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Builder @Getter @Setter
public class NewCardRequest {

	@NotNull
	private String cardNumber;
	
	@NotNull
	private String password;
	
}
