package org.configurator;

import org.msgInterceptors.MyChannelInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class serverConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${messaging.rmq: false}")
    boolean isRmqActive;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOrigins("*")
        // spring by default uses the sessionID generated at the servlet container level in the absence of any auth mechanism that sets user principal
             /*   .setHandshakeHandler(new DefaultHandshakeHandler()
                {
                    public boolean beforeHandshake(
                            ServerHttpRequest request,
                            ServerHttpResponse response,
                            WebSocketHandler wsHandler,
                            Map attributes) throws Exception {

                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest
                                    = (ServletServerHttpRequest) request;
                            HttpSession session = servletRequest
                                    .getServletRequest().getSession();
                            attributes.put("sessionId", session.getId());
                        }
                        return true;
                }})*/;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue","/topic");
        config.setApplicationDestinationPrefixes("/app");
        // setting up connection to an external broker - rmq in this case over stomp
        // the sys login and sys passcode is for connection between spring and rmq - basically for pushing messages from app to rmq
        // the client login and client passcode is for connection between each websocket client session and rmq (tunneled through spring actually)
        // each ws client has a separate connection
        // the system connection (for pushing message from app to rmq) is only 1
        // rmq for stomp has a bunch of predefined endpoints
        // 'exchange' endpoint means for SEND from client, messages go to the bound queue with specific routing key. ex, queue and routing key defined statically in rmq
        // on client side, do send in the form of /exchange/<ex name>/<routing key>
        // 'exchange' endpoint for SUBSCRIBE works differently. an exclusive auto delete  queue is created for each client. ex defined statically in rmq
        // on client side, subscribe in the form of /exchange/<ex name>/<pattern>
        // rmq will create a queue and bind that to routing key 'pattern' automatically
        // 'amq' endpoint means for SEND from client, messages go to a pre-existing queue bound to def.exchange.
        // on client side , do send in the form of /amq/queue/<name of queue>
        // 'amq' endpoint for SUBSCRIBE is a shared queue for all subscribers/clients
        // on client side, subscribe in the form of /<queue name> . the queue must be bound to default exchange
        // TBD - notes for 'queue', 'topic' and 'temp queue'
        if(isRmqActive)
        config.enableStompBrokerRelay("/exchange","/amq").setRelayHost("localhost").setRelayPort(61613).setSystemLogin("stomp").setSystemPasscode("stomp")
                .setClientLogin("stomp").setClientPasscode("stomp");

    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new MyChannelInterceptor());
    }
}
