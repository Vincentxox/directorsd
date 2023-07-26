package com.consystec.sc.ca.ws.mapas;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class ClientEndPoint {
    Session session = null;

    public ClientEndPoint( String uriSidra) throws DeploymentException, IOException, URISyntaxException {
        URI uri = new URI(uriSidra);

        processOpen(session);
        session = ContainerProvider.getWebSocketContainer().connectToServer(this, uri);
        processClose(session);
    }

    @OnOpen
    public void processOpen(Session session) {
        this.session = session;
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("message: " + message);
    }

    @OnClose
    public void processClose(Session session) {
        System.out.println("Closing websocket");
    }

    public void sendMessage(String message) throws IOException {
        session.getBasicRemote().sendText(message);
    }
}