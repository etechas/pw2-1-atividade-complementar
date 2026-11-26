package br.com.etechoracio.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AvaliacaoRequest(
        @NotNull(message = "O campo 'deferido' é obrigatório") Boolean deferido,
        @Min(value = 0, message = "As horas validadas não podem ser negativas") Integer horasValidadas
) {}