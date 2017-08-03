package com.atp.logging;

import java.time.LocalDateTime;

public class Message {
	
	public enum MessageType { 
		INFO("Info"),
		SUCCESS("Success"),
		FAILURE("Failure"),
		TRADE_EXECUTION_SUCCESS("Trade success"), 
		TRADE_EXECUTION_FAILURE("Trade failure"), 
		WARNING("Warning");
		private String tag;
		MessageType(String tag) { this.tag = tag;} 
		public String getTag() { return tag; }
	};
	
	private MessageType messageType;
	private LocalDateTime dateTime;
	private String message;
	
	public Message(MessageType messageType, LocalDateTime dateTime,
			String message) {
		super();
		this.messageType = messageType;
		this.dateTime = dateTime;
		this.message = message;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String getMessage() {
		return message;
	}
	
}
