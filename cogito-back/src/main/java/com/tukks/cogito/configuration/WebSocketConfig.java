package com.tukks.cogito.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.tukks.cogito.service.ThoughtMessageHandler;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

	private ThoughtMessageHandler thoughtMessageHandler;
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
		webSocketHandlerRegistry.addHandler(thoughtMessageHandler, "/ws/cards").setAllowedOriginPatterns("*");
	}

}
