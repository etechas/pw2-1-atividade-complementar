package br.com.etechoracio.repository;

import br.com.etechoracio.model.AtividadeComplementar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AtividadeRepository extends JpaRepository<AtividadeComplementar, Long> {
}
