package sms.santana.reactivespringboot.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sms.santana.reactivespringboot.modelo.Cerveja;
import sms.santana.reactivespringboot.repositorio.CervejaRepositorio;

@Service
public class CervejaService {

    private final CervejaRepositorio cervejaRepositorio;

    public CervejaService(CervejaRepositorio cervejaRepositorio) {
        this.cervejaRepositorio = cervejaRepositorio;
    }

    public Flux<Cerveja> getCerveja() {
        return cervejaRepositorio.findAll();
    }

    public Mono<Cerveja> postCerveja(Cerveja cerveja) {
       return Mono.just(cerveja);
        /*return ServerResponse
                .created(URI.create(
                        "http://localhost:8080/design/taco/" +
                                savedTaco.getId()))
                .body(savedTaco, Taco.class);*/
    }
}
