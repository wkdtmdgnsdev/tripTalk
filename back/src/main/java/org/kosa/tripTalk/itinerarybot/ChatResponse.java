package org.kosa.tripTalk.itinerarybot;

import java.util.List;

import org.kosa.tripTalk.itinerarybot.ChatRequest.Message;

import lombok.Data;

@Data
public class ChatResponse {
	
	    private List<Candidate> candidates;

	    @Data
	    public static class Candidate {
	        private Message content;
	    }

	    @Data
	    public static class Message {
	        private String role;
	        private List<Part> parts;
	    }

	    @Data
	    public static class Part {
	        private String text;
	    }
	
}
