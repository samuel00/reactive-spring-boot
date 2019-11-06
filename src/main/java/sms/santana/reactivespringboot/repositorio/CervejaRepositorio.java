package sms.santana.reactivespringboot.repositorio;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import sms.santana.reactivespringboot.modelo.Cerveja;

@CrossOrigin(origins="*")
public interface CervejaRepositorio extends ReactiveMongoRepository<Cerveja, String> {
}
