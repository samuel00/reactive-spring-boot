package sms.santana.reactivespringboot.recurso;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sms.santana.reactivespringboot.modelo.Cerveja;
import sms.santana.reactivespringboot.service.CervejaService;

@RestController
@RequestMapping("/api/v1")
public class CervejaController {

    private final CervejaService cervejaService;

    @Autowired
    public CervejaController(CervejaService cervejaService) {
        this.cervejaService = cervejaService;
    }

    @GetMapping(value = "/cerveja")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Cerveja> cerveja() {
        return cervejaService.getCerveja();
    }

    @PostMapping(value = "/cerveja")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Cerveja> criarCerveja(@RequestBody Mono<Cerveja> cerveja) {
        return cervejaService.postCerveja(cerveja.block());
    }


}
