package ma.inpt.esj.Mailer;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import jakarta.mail.internet.MimeMessage;
import ma.inpt.esj.entities.Live;

@Service
public class MailSending implements Runnable{
    @Autowired
    JavaMailSender mailSender;
    
    @Value(value = "${spring.mail.username}")
    public String sender;
    public String receiver;
    public String subject;
    public String body;
    private byte[] pdfContent;
    private String pdfTitle;
    
    public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender sender) {
		this.mailSender = sender;
	}
	
	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public byte[] getPdfContent() {
		return pdfContent;
	}

	public void setPdfContent(byte[] pdfContent) {
		this.pdfContent = pdfContent;
	}

	public String getPdfTitle() {
		return pdfTitle;
	}

	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	public void sendMail(String toEmail, String Subject, String body){
        SimpleMailMessage M=new SimpleMailMessage();
        M.setFrom(sender);
        M.setTo((toEmail));
        M.setText(body);
        M.setSubject(Subject);
        mailSender.send(M);
        System.out.println("message received ");
    }
	
	public void sendEmailWithPdf(String to, String subject, String body, byte[] pdfContent, String title) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);
        
        ByteArrayResource pdfResource = new ByteArrayResource(pdfContent);
        helper.addAttachment(title+".pdf", pdfResource);

        mailSender.send(message);
    }
    
    @Override
    public void run() {
        try {
			this.sendEmailWithPdf(this.getReceiver(),this.getSubject(),this.getBody(), this.getPdfContent(), this.getPdfTitle());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

