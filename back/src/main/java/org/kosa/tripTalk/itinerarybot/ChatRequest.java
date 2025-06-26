package org.kosa.tripTalk.itinerarybot;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class ChatRequest {
//	@JsonIgnore
//	private Long id;
	
	//대화를 요청(보냄) 
	//: 양방향이기 때문에 List에 메세지를 담아서 보냄

	private List<Message> contents = new ArrayList<>();
	
	@Data
	public static class Message{
		private String role;
		private List<Part> parts = new ArrayList<>();
	}
	
	@Data
	public static class Part{
		private String text;
	}
	
	//user의 대화를 추가함
	public void addUserMessage(String userText) {
		Message message = new Message();
		message.setRole("user");
		
		Part part = new Part();
		part.setText(userText);
		
		message.getParts().add(part);
		contents.add(message);
	}
	
	//model의 대화를 추가함
	public void addModelMessage(String modelText) {
		Message message = new Message();
		message.setRole("model");
		
		Part part = new Part();
		part.setText(modelText);
		
		message.getParts().add(part);
		contents.add(message);
	}

}
