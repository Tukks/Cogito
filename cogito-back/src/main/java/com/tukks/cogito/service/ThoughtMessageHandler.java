package com.tukks.cogito.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import org.jetbrains.annotations.NotNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tukks.cogito.dto.response.ActionEvent;
import com.tukks.cogito.entity.UserEntity;

import static com.tukks.cogito.service.SecurityUtils.getSub;

@Service
public class ThoughtMessageHandler extends TextWebSocketHandler implements ApplicationListener<ActionEvent> {
	private final Logger logger = LogManager.getLogger(getClass());

	@Autowired
	ObjectMapper objectMapper;
	final ConcurrentHashMap<String, List<WebSocketSession>> webSocketSessionsEmitters = new ConcurrentHashMap<>();

	@Override
	public synchronized void afterConnectionEstablished(@NotNull WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		if (webSocketSessionsEmitters.get(getSubFromSession(session)) == null) {
			List<WebSocketSession> webSocketSessionToAdd = Collections.synchronizedList(new ArrayList<>());
			webSocketSessionToAdd.add(session);

			webSocketSessionsEmitters.put(getSubFromSession(session), webSocketSessionToAdd);
		} else {
			webSocketSessionsEmitters.get(getSubFromSession(session)).add(session);
		}

	}

	@Override
	public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);

		webSocketSessionsEmitters.get(getSubFromSession(session)).removeIf(webSocketSession -> webSocketSession.getId().equals(session.getId()));
	}

	@Override
	public void handleMessage(@NotNull WebSocketSession session, @NotNull WebSocketMessage<?> message) throws Exception {
		super.handleMessage(session, message);
	}

	private void sendMessageToAll(ActionEvent actionEvent) throws JsonProcessingException {
		TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(actionEvent.getActionCard()));

		webSocketSessionsEmitters.get(getSub()).forEach((value) -> {
			try {
				value.sendMessage(textMessage);
			} catch (IOException e) {
				logger.error("Problem sending message to WS", e);
			}
		});
	}

	@Override
	public void onApplicationEvent(@NotNull ActionEvent actionEvent) {
		try {
			sendMessageToAll(actionEvent);
		} catch (JsonProcessingException e) {
			logger.error("Problem  message to WS", e);

		}

	}

	private String getSubFromSession(WebSocketSession webSocketSession) {
		return ((UserEntity)((UsernamePasswordAuthenticationToken)webSocketSession.getPrincipal()).getPrincipal()).getId().toString();
	}

}
