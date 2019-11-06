package sms.santana.reactivespringboot.route;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import sms.santana.reactivespringboot.handler.CervejaHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class CervejaRouter {

    @Bean
    public RouterFunction<?> routeCerveja(CervejaHandler cervejaHandler) {

        return RouterFunctions
                .route(GET("/cerveja")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), cervejaHandler::findAll)
                .andRoute(GET("/cerveja/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), cervejaHandler::findById)
                .andRoute(POST("/cerveja")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), cervejaHandler::save)
                .andRoute(PUT("/cerveja/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), cervejaHandler::update)
                .andRoute(DELETE("/cerveja/{id}")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), cervejaHandler::deleteById);
    }
}
