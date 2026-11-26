package br.com.etechoracio.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atividade_complementar")
@Getter @Setter @NoArgsConstructor
public class AtividadeComplementar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do aluno é obrigatório")
    @Column(name = "nome_aluno", nullable = false, length = 100)
    private String nomeAluno;

    @NotBlank(message = "O título da atividade é obrigatório")
    @Column(name = "titulo_atividade", nullable = false, length = 150)
    private String tituloAtividade;

    @NotBlank(message = "A instituição é obrigatória")
    @Column(nullable = false, length = 100)
    private String instituicao;

    @NotNull(message = "As horas declaradas são obrigatórias")
    @Min(value = 1, message = "As horas declaradas devem ser no mínimo 1")
    @Column(name = "horas_declaradas", nullable = false)
    private Integer horasDeclaradas;

    @Column(name = "horas_aprovadas", nullable = false)
    private Integer horasAprovadas = 0;

    @Column(nullable = false, length = 20)
    private String status = "PENDENTE";
}
