package com.is4tech.invoicemanagement.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class SendEmail {
  
  private final JavaMailSender mail;

  public SendEmail(JavaMailSender mail) {
    this.mail = mail;
  }

  public void sendEmailWithPdf(String destination, String from, String subject, byte[] pdfData) throws MessagingException {
    MimeMessage mimeMessage = mail.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    helper.setTo(destination);
    helper.setFrom(from);
    helper.setSubject(subject);
    helper.setText("Ver archivo PDF adjunto");

    InputStreamSource pdfSource = new ByteArrayResource(pdfData);
    helper.addAttachment("reporte.pdf", pdfSource);

    mail.send(mimeMessage);
  }
}