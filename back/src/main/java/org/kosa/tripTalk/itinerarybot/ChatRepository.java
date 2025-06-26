package org.kosa.tripTalk.itinerarybot;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    //List<Chat> findByBotRoomOrderByCreatedAtAsc(ItineraryBotRoom botRoom);
}
