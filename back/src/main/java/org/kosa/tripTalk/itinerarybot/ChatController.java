package org.kosa.tripTalk.itinerarybot;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
	private final ChatService chatService;
	private final ChatRepository chatRepository;
	
	@PostMapping
	public ChatRequest chat(@RequestBody ChatInput input) {
		//대화가 없을 경우 요청 자체를 거부하는 예외로 둠 
		if (input.getChatRequest() == null) {
	        throw new IllegalArgumentException("chatRequest는 null일 수 없습니다.");
	    }
		
		return chatService.chat(input.getChatRequest(), input.getUserInput());
	}
	
	@Data
	public static class ChatInput{
		//대화 전체 히스토리
		private ChatRequest chatRequest;
		//사용자의 입력 
		private String userInput;
	}
	
	/*
	@GetMapping("/chat/{id}")
	public ResponseEntity<String> getChatLog(@PathVariable Long id){
		Optional<Chat> chatOpt = chatRepository.findById(id);
		if(chatOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		String allLog = chatOpt.get().getAllLog();
		return ResponseEntity.ok(allLog);
	}
	*/
}



