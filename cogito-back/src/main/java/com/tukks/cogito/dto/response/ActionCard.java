package com.tukks.cogito.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class ActionCard {

	private final ActionType actionType;

	private final UUID id;

	private final Object card;
	public ActionCard(ActionType actionType, UUID id, Object card) {
		this.actionType = actionType;
		this.id = id;
		this.card = card;
	}
}
