package sms.santana.reactivespringboot.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Cerveja{

    @Id
    private String id;
    private String nome;
    private String fabricante;
    private String tipo;
}
