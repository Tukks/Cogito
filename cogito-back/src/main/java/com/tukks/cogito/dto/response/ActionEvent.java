package com.tukks.cogito.dto.response;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class ActionEvent extends ApplicationEvent {

	ActionCard actionCard;

	String sub;

	public ActionEvent(Object source, ActionCard actionCard, String sub) {
		super(source);
		this.actionCard = actionCard;
		this.sub = sub;
	}

}
