package com.javanauta.notificacao.business;

import com.javanauta.notificacao.business.dtos.TarefaRequestDTO;
import com.javanauta.notificacao.infrastructure.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Value("${envio.email.remetente}")
    private String remetente;

    @Value("${envio.email.nomeRemetente}")
    private String nomeRemetente;

    public void enviaEmail(TarefaRequestDTO dto) {
        try {
            MimeMessage mensagem = javaMailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mensagem, true,
                    StandardCharsets.UTF_8.name());
            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            mimeMessageHelper.setTo(InternetAddress.parse(dto.email()));
            mimeMessageHelper.setSubject("Notificação de Tarefa");


            Context context = new Context();
            context.setVariable("nomeTarefa", dto.nomeTarefa());
            context.setVariable("dataEvento", dateFormatter(dto.dataEvento()));
            context.setVariable("descricao",  dto.descricao());
            String template = templateEngine.process("notificacao", context);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mensagem);

        } catch (MessagingException | UnsupportedEncodingException e){
            throw new EmailException("Erro ao enviar email ", e);
        }
    }

    private String dateFormatter(LocalDateTime dataEvento) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // O valor chega em UTC; converte para o horário de Brasília para exibição no e-mail
        return dataEvento.atZone(ZoneOffset.UTC)                       // interpreta o valor como UTC
                .withZoneSameInstant(ZoneId.of("America/Sao_Paulo"))  // move para o mesmo instante em SP (-3h)
                .format(formatter);
    }
}
