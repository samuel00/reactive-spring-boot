package sms.santana.reactivespringboot.handler;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import sms.santana.reactivespringboot.modelo.Cerveja;
import sms.santana.reactivespringboot.repositorio.CervejaRepositorio;

import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class CervejaHandler {

    private final CervejaRepositorio cervejaRepositorio;

    public CervejaHandler(CervejaRepositorio cervejaRepositorio) {
        this.cervejaRepositorio = cervejaRepositorio;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok()
                .body(cervejaRepositorio.findAll(), Cerveja.class)
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        final String id = request.pathVariable("id");
        final Mono<Cerveja> cerveja = cervejaRepositorio.findById(id);
        return cerveja.flatMap(cvj -> ok().contentType(APPLICATION_JSON)
                .body(fromPublisher(cerveja, Cerveja.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> save(ServerRequest request) {
        Mono<Cerveja> cerveja = request.bodyToMono(Cerveja.class);
        final UUID id = UUID.randomUUID();
        return created(UriComponentsBuilder.fromPath("people/" + id).build().toUri())
                .contentType(APPLICATION_JSON)
                .body(
                        fromPublisher(
                                cerveja.map(c -> new Cerveja(id.toString(),
                                        c.getNome(),
                                        c.getFabricante(),
                                        c.getTipo()))
                                        .flatMap(this.cervejaRepositorio::save), Cerveja.class));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        final String id = request.pathVariable("id");
        final Mono<Cerveja> cerveja = request.bodyToMono(Cerveja.class);
        return this.cervejaRepositorio
                .findById(id)
                .flatMap(
                        cervejaAntiga ->
                                ok().contentType(APPLICATION_JSON)
                                        .body(
                                                fromPublisher(
                                                        cerveja
                                                                .map(c -> new Cerveja(id, c.getNome(), c.getFabricante(), c.getTipo()))
                                                                .flatMap(this.cervejaRepositorio::save),
                                                        Cerveja.class)))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> deleteById(ServerRequest request) {
        final String id = request.pathVariable("id");
        return this.cervejaRepositorio.findById(id)
                .flatMap(p -> noContent().build(this.cervejaRepositorio.delete(p)))
                .switchIfEmpty(notFound().build());
    }
}
