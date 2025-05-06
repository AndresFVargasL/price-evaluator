package com.inditex.jpa.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class H2ConsoleConfiguration {

    private final AtomicReference<Server> webServer = new AtomicReference<>();
    private final AtomicReference<Server> tcpServer = new AtomicReference<>();

    private static final String WEB_PORT = "8082";
    private static final String TCP_PORT = "9092";

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws SQLException {
        webServer.set(Server.createWebServer("-webPort", WEB_PORT, "-tcpAllowOthers").start());
        tcpServer.set(Server.createTcpServer("-tcpPort", TCP_PORT, "-tcpAllowOthers").start());
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        if (tcpServer.get() != null) {
            tcpServer.get().stop();
        }
        if (webServer.get() != null) {
            webServer.get().stop();
        }
    }
}
