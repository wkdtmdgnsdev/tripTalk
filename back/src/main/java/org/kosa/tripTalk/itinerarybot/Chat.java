package org.kosa.tripTalk.itinerarybot;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chatbot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long messageid;

    @Column
    private String role;

    @Column
    private LocalDateTime createdAt;
    
	@Lob
	@Column
	private String userMessage;
	
	@Lob
	@Column
	private String modelMessage;

}
