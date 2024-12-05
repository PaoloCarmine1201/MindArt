package com.is.mindart.gestioneSessione.model;


import com.is.mindart.gestioneMateriale.model.Materiale;
import com.is.mindart.gestioneTerapeuta.model.Terapeuta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sessione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String temaAssegnato;
    private Date data;
    private String nota;
    private TipoSessione tipo;

    @ManyToOne
    @JoinColumn(name = "materiale_id")
    private Materiale materiale;

    @ManyToOne
    @JoinColumn(name = "terapeuta_id")
    private Terapeuta terapeuta;

}
