package solutions.b2.autorizador.values;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum TransactionEnum {

	OK,
	INSUFFICIENT_BALANCE,
	INVALID_PASSWORD,
	INVALID_CARD;
	
}