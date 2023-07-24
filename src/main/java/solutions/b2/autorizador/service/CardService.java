package solutions.b2.autorizador.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import solutions.b2.autorizador.dto.request.TransactionRequest;
import solutions.b2.autorizador.entity.Card;
import solutions.b2.autorizador.exception.CardNumberAlreadyInUseException;
import solutions.b2.autorizador.exception.InsufficientBalanceException;
import solutions.b2.autorizador.exception.InvalidCardException;
import solutions.b2.autorizador.exception.InvalidPasswordException;
import solutions.b2.autorizador.repository.CardRepository;

@ApplicationScoped
public class CardService {

	@Inject
	private CardRepository cardRepository;

	public Card findByNumber(String number) {
		return cardRepository.findByNumber(number);
	}
	
	@Transactional
	public Card create(Card card) throws CardNumberAlreadyInUseException {
		if(findByNumber(card.getNumber()) != null) {
			throw new CardNumberAlreadyInUseException();
		}
		
		card.setCreationDate(LocalDateTime.now());
		card.setBalance(BigDecimal.valueOf(500.00)); // every card is created with this balance
		card.setPassword(BcryptUtil.bcryptHash(card.getPassword())); // encrypt password before persist
		
		cardRepository.persist(card);
		if(cardRepository.isPersistent(card)) {
			return card;
		}
		
		return null;
	}
	
	public boolean validatePassword(Card card, String password) {
		return BcryptUtil.matches(password, card.getPassword());
	}
	
	@Transactional
	public Card charge(TransactionRequest request) throws InvalidCardException, InvalidPasswordException, InsufficientBalanceException {
		Card card = findByNumber(request.getCardNumber());
		
		if(card == null) {
			throw new InvalidCardException();
		}
		
		if(!validatePassword(card, request.getPassword())) {
			throw new InvalidPasswordException();
		}
		
		if(request.getAmount().compareTo(card.getBalance()) == 1) {
			throw new InsufficientBalanceException();
		}
		
		card.setBalance(card.getBalance().subtract(request.getAmount()));
		
		cardRepository.persist(card);
		return cardRepository.isPersistent(card) ? card : null;
	}

}
