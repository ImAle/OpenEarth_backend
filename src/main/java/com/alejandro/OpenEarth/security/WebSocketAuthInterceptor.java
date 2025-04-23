package com.alejandro.OpenEarth.security;

import com.alejandro.OpenEarth.entity.User;
import com.alejandro.OpenEarth.serviceImpl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component("webSocketAuthInterceptor")
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    @Autowired
    @Qualifier("jwtService")
    private JwtService jwtService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = servletRequest.getServletRequest().getParameter("token");

            if (token == null) {
                token = servletRequest.getServletRequest().getHeader("Authorization");
            }

            if (token != null && token.startsWith("Bearer ")) {
                try {
                    User user = jwtService.getUser(token);
                    System.out.println("User role: " + user.getRole());
                    if (!jwtService.isGuest(token) && !jwtService.isHostess(token)) {
                        System.out.println("Only Guests and Hostess are allowed to chat");
                        return false;
                    }
                    attributes.put("username", user.getUsername());
                    return true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Invalid token");
                    return false;
                }
            }
        }

        System.out.println("No token. Access denied.");
        return false;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
