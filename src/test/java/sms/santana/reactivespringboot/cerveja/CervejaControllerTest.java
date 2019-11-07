package sms.santana.reactivespringboot.cerveja;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import sms.santana.reactivespringboot.modelo.Cerveja;
import sms.santana.reactivespringboot.recurso.CervejaController;
import sms.santana.reactivespringboot.service.CervejaService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CervejaControllerTest {

    @Test
    public void returnCerveja() {
        Cerveja[] cervejas = {
                criarCerveja("1L"), criarCerveja("2L"),
                criarCerveja("3L"), criarCerveja("4L"),
                criarCerveja("5L"), criarCerveja("6L"),
                criarCerveja("7L"), criarCerveja("8L"),
                criarCerveja("9L"), criarCerveja("10L"),
                criarCerveja("11L"), criarCerveja("12L"),
                criarCerveja("13L"), criarCerveja("14L"),
                criarCerveja("15L"), criarCerveja("16L")};

        Flux<Cerveja> cervejaFlux = Flux.just(cervejas);

        CervejaService cervejaService = Mockito.mock(CervejaService.class);
        when(cervejaService.getCerveja()).thenReturn(cervejaFlux);

        WebTestClient testClient = WebTestClient.bindToController(
                new CervejaController(cervejaService))
                .build();

        testClient.get().uri("/api/v1/cerveja")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Cerveja.class)
                .contains(Arrays.copyOf(cervejas, 12));
    }

    @Test
    public void criarCerveja() {

        //give:
        CervejaService cervejaService = Mockito.mock(
                CervejaService.class);

        Mono<Cerveja> unsavedTacoMono = Mono.just(criarCerveja(null));

        Cerveja savedTaco = criarCerveja(null);
        savedTaco.setId("1L");

        Mono<Cerveja> savedTacoMono = Mono.just(savedTaco);
        when(cervejaService.postCerveja(any())).thenReturn(savedTacoMono);

        //when:
        WebTestClient testClient = WebTestClient.bindToController(
                new CervejaController(cervejaService)).build();

        //then:
        testClient.post()
                .uri("/api/v1/cerveja")
                .contentType(MediaType.APPLICATION_JSON)
                .body(unsavedTacoMono, Cerveja.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Cerveja.class)
                .isEqualTo(savedTaco);
    }

    private Cerveja criarCerveja(String number) {
        return new Cerveja(number,
                "Cerveja " + number,
                "Fabricante",
                "Weiss");
    }

}