package com.javanauta.notificacao.business.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record TarefaRequestDTO(
        String email,
        String nomeTarefa,
        String descricao,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "UTC")
        OffsetDateTime dataEvento
) {
}
