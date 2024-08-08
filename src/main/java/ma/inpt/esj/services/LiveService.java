package ma.inpt.esj.services;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import ma.inpt.esj.exception.AdministrateurNotFoundException;
import ma.inpt.esj.exception.LiveNotFoundException;
import ma.inpt.esj.exception.ResponsableNotFoundException;
import ma.inpt.esj.Mailer.MailSending;
import ma.inpt.esj.Mailer.PdfGenerator;
import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.LiveForCreationDTO;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.entities.Live;
import ma.inpt.esj.entities.Responsable;
import ma.inpt.esj.entities.Theme;
import ma.inpt.esj.mappers.LiveMapper;
import ma.inpt.esj.mappers.QuestionMapper;
import ma.inpt.esj.mappers.ResponsableMapper;
import ma.inpt.esj.mappers.ThemeMapper;
import ma.inpt.esj.repositories.AdministrateurRepository;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.repositories.LiveRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import ma.inpt.esj.repositories.ProfessionnelRepository;
import ma.inpt.esj.repositories.ThemeRepository;

public interface LiveService {

    public List<LiveDTO> getAllLives();
    
    // live passed
    public List<LiveDTO> getPassedLives();
    
    public List<LiveDTO> getAllByAdmin(int id);
    
    // live on going
    public List<LiveDTO> getongoing(int id);
    
    public List<LiveDTO> getongoingforuser();

    public void activatdes(int id);

    // animated by doctor or infrimrie
    public List<LiveDTO> getAllAnimated(Long id);

    // on going to animate by doctor
    public  List<LiveDTO> getongoingToan(Long id);

    // live not activet yet
    public List<LiveDTO> getfileattentetoactive(int id);
    
    //live activated
    public List<LiveDTO> getfileattente(int id);
    
    public List<LiveDTO> getAllfileattenteforuser();

    public List<LiveDTO> getonquestions(int id);
    
    public List<LiveDTO> getonquestionsforuser();

    //phase j-3
    @Transactional
    //@Scheduled(cron = "0 0 0 * * *")
    public List<LiveDTO> getonfinal(int id);
    
    @Transactional
    //@Scheduled(cron = "0 0 0 * * *")
    public List<LiveDTO> getonfinalforuser();

    public LiveDTO getSingleLive(int id);
    
    @Transactional
    public void createLive(LiveForCreationDTO liveRequest, MultipartFile file, Long adminId) throws ResponsableNotFoundException, AdministrateurNotFoundException, IOException;
    
    public void updateLive(LiveDTO L,Long adminId,int liveId) throws ResponsableNotFoundException, LiveNotFoundException;
    
    public void deleteLive(int id) throws LiveNotFoundException;

	public LiveDTO getLastLive();
	
	public byte[] getLiveImage(int id) throws LiveNotFoundException;
}