package br.com.etechoracio.controller;

import br.com.etechoracio.dto.AvaliacaoRequest;
import br.com.etechoracio.exception.RecursoNaoEncontradoException;
import br.com.etechoracio.exception.RegraDeNegocioException;
import br.com.etechoracio.model.AtividadeComplementar;
import br.com.etechoracio.repository.AtividadeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/atividades")
@RequiredArgsConstructor
public class AtividadeController {

    private final AtividadeRepository repository;

    @GetMapping
    public ResponseEntity<List<AtividadeComplementar>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeComplementar> buscar(@PathVariable Long id) {
        AtividadeComplementar a = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade não encontrada: " + id));
        return ResponseEntity.ok(a);
    }

    @PostMapping
    public ResponseEntity<AtividadeComplementar> criar(@Valid @RequestBody AtividadeComplementar body) {
        body.setId(null);
        body.setStatus("PENDENTE");
        body.setHorasAprovadas(0);
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtividadeComplementar> editar(@PathVariable Long id,
                                                        @Valid @RequestBody AtividadeComplementar body) {
        AtividadeComplementar a = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade não encontrada: " + id));

        if (!"PENDENTE".equals(a.getStatus())) {
            throw new RegraDeNegocioException("Só é possível editar atividades com status PENDENTE.");
        }
        a.setNomeAluno(body.getNomeAluno());
        a.setTituloAtividade(body.getTituloAtividade());
        a.setInstituicao(body.getInstituicao());
        a.setHorasDeclaradas(body.getHorasDeclaradas());
        return ResponseEntity.ok(repository.save(a));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Atividade não encontrada: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/avaliar")
    public ResponseEntity<AtividadeComplementar> avaliar(@PathVariable Long id,
                                                         @Valid @RequestBody AvaliacaoRequest req) {
        AtividadeComplementar a = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade não encontrada: " + id));

        if (!"PENDENTE".equals(a.getStatus())) {
            throw new RegraDeNegocioException("Apenas atividades com status PENDENTE podem ser avaliadas.");
        }

        if (req.deferido()) {
            if (req.horasValidadas() == null) {
                throw new RegraDeNegocioException("Informe as horas validadas ao deferir.");
            }
            if (req.horasValidadas() > a.getHorasDeclaradas()) {
                throw new RegraDeNegocioException("As horas aprovadas não podem exceder as horas declaradas.");
            }
            a.setHorasAprovadas(req.horasValidadas());
            a.setStatus("DEFERIDA");
        } else {
            a.setHorasAprovadas(0);
            a.setStatus("INDEFERIDA");
        }
        return ResponseEntity.ok(repository.save(a));
    }
}