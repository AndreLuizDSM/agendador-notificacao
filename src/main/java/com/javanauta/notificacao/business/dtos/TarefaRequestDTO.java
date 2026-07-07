package com.javanauta.notificacao.business.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record TarefaRequestDTO(
        String email,
        String nomeTarefa,
        String descricao,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
        Instant dataEvento
) {
}
