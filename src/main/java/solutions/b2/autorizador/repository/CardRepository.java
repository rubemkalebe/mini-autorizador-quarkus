package solutions.b2.autorizador.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import solutions.b2.autorizador.entity.Card;

@ApplicationScoped
public class CardRepository implements PanacheRepository<Card> {

	public Card findByNumber(String number) {
		return find("SELECT c FROM Card c WHERE c.number = ?1", number).firstResult();
	}

}
