package com.tukks.cogito.dto.response;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class ActionEvent extends ApplicationEvent {

	ActionCard actionCard;


	public ActionEvent(Object source, ActionCard actionCard) {
		super(source);
		this.actionCard = actionCard;
	}

}
