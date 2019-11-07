package sms.santana.reactivespringboot.cerveja;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;
import sms.santana.reactivespringboot.handler.CervejaHandler;
import sms.santana.reactivespringboot.modelo.Cerveja;
import sms.santana.reactivespringboot.repositorio.CervejaRepositorio;
import sms.santana.reactivespringboot.route.CervejaRouter;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CervejaRouter.class, CervejaHandler.class})
@WebFluxTest
public class CervejaRouterTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private CervejaRepositorio cervejaRepositorio;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }


    @Test
    public void testGetCervejasPorId() {
        Cerveja cerveja = new Cerveja("1", "BudWeiser", "Bud", "Pielsen");
        Mono<Cerveja> cervejaMono = Mono.just(cerveja);
        when(this.cervejaRepositorio.findById("1")).thenReturn(cervejaMono);
        webTestClient.get()
                .uri("/cerveja/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cerveja.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.getId()).isEqualTo("1");
                            Assertions.assertThat(userResponse.getNome()).isEqualTo("BudWeiser");
                            Assertions.assertThat(userResponse.getFabricante()).isEqualTo("Bud");
                            Assertions.assertThat(userResponse.getTipo()).isEqualTo("Pielsen");
                        }
                );
    }

    @Test
    public void testGetCervejas() {

        Cerveja cerveja = new Cerveja("1L", "Baden Baden", "Ribeirao Preto", "IPA");
        Cerveja cerveja2 = new Cerveja("2L", "Colorado", "Colorado", "IPA");

        when(this.cervejaRepositorio.findAll()).thenReturn(Flux.just(cerveja, cerveja2));

        this.webTestClient.get()
                .uri("/cerveja")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Cerveja.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.get(0).getId()).isEqualTo("1L");
                            Assertions.assertThat(userResponse.get(0).getNome()).isEqualTo("Baden Baden");
                            Assertions.assertThat(userResponse.get(1).getId()).isEqualTo("2L");
                            Assertions.assertThat(userResponse.get(1).getNome()).isEqualTo("Colorado");
                        }
                );
    }

    @Test
    public void testeCriaCerveja() {

        Cerveja cerveja = new Cerveja(null, "Perola Negra", "Perola Negra", "Larger");
        Mono<Cerveja> cervejaMono = Mono.just(cerveja);
        when(this.cervejaRepositorio.save(any())).thenReturn(cervejaMono);

        webTestClient.post()
                .uri("/cerveja")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(cerveja), Cerveja.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Cerveja.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.getId()).isNull();
                            Assertions.assertThat(userResponse.getNome()).isEqualTo("Perola Negra");
                            Assertions.assertThat(userResponse.getTipo()).isEqualTo("Larger");
                        }
                );
    }

    @Test
    public void testeAtualizaCerveja() {

        Cerveja cerveja = new Cerveja("123-456", "Perola Negra", "Perola Negra", "Larger");
        Cerveja cervejaAtualizada = new Cerveja("123-456", "Paysandu", "Cerpa", "Pielsen");
        Mono<Cerveja> cervejaMono = Mono.just(cerveja);
        Mono<Cerveja> cervejaMonoAtualizada = Mono.just(cervejaAtualizada);
        when(this.cervejaRepositorio.save(any())).thenReturn(cervejaMonoAtualizada);
        when(this.cervejaRepositorio.findById("123-456")).thenReturn(cervejaMono);

        webTestClient.put()
                .uri("/cerveja/123-456")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(cerveja), Cerveja.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cerveja.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse.getId()).isEqualTo("123-456");
                            Assertions.assertThat(userResponse.getNome()).isEqualTo("Paysandu");
                            Assertions.assertThat(userResponse.getTipo()).isEqualTo("Pielsen");
                        }
                );
    }

    @Test
    public void testeDeletaCerveja() {

        Cerveja cerveja = new Cerveja("123-456", "Perola Negra", "Perola Negra", "Larger");
        Mono<Cerveja> cervejaMono = Mono.just(cerveja);
        when(this.cervejaRepositorio.findById("123-456")).thenReturn(cervejaMono);
        //doNothing().when(this.cervejaRepositorio).delete(any());
        when(this.cervejaRepositorio.delete(any())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/cerveja/123-456")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }


    @Test
    public void testando() throws InterruptedException {

        List<Cerveja> lista = Arrays.asList(new Cerveja("1L", "Baden Baden", "Ribeirao Preto", "IPA"),
                new Cerveja("2L", "Colorado", "Colorado", "IPA"),
                new Cerveja("3L", "Colorado", "Colorado", "IPA"));

        // We use here Flux.generate to create quotes,
        // iterating on each stock starting at index 0
        Flux<Cerveja> flux = Flux.generate(() -> 0,
                (BiFunction<Integer, SynchronousSink<Cerveja>, Integer>) (index, sink) -> {
                    Cerveja updatedQuote = updateCerveja(lista.get(index));
                    sink.next(updatedQuote);
                    System.out.println("Testando......................." + (1+index % lista.size()));
                    return ++index % lista.size();
                })
                // We want to emit them with a specific period;
                // to do so, we zip that Flux with a Flux.interval
                .zipWith(Flux.interval(Duration.ofMillis(1)))
                .map(Tuple2::getT1)
                // Because values are generated in batches,
                // we need to set their timestamp after their creation
                .map(quote -> {
                    quote.setId("1L");
                    return quote;
                })
                .log("Samuel");

        flux.take(22000).subscribe(System.out::println);

        Thread.sleep(1000);
        Thread.sleep(1000);
        Thread.sleep(1000);
        Thread.sleep(1000);
    }

    private Cerveja updateCerveja(Cerveja cerveja) {
        return cerveja;
    }


}