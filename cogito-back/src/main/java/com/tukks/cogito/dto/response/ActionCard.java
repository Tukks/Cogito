package com.tukks.cogito.dto.response;

import java.util.List;
import java.util.UUID;

import com.tukks.cogito.entity.ThingsEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class ActionCard {

	private final ActionType actionType;

	private final List<UUID> ids;

	private final List<? super ThingsEntity> cards;
	public ActionCard(ActionType actionType, List<UUID> ids, List<? super ThingsEntity> cards) {
		this.actionType = actionType;
		this.ids = ids;
		this.cards = cards;
	}
}
