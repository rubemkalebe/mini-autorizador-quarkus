package solutions.b2.autorizador.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "card")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Card {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "car_code")
    private UUID code;
    
	@Column(name = "car_number")
    private String number;
    
	@Column(name = "car_password")
    private String password;
    
	@Column(name = "car_creation_date")
    private LocalDateTime creationDate;
    
	@Column(name = "car_balance")
    private BigDecimal balance;
    
}
