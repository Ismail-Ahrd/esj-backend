package ma.inpt.esj.services;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import ma.inpt.esj.exception.AdministrateurNotFoundException;
import ma.inpt.esj.exception.LiveNotFoundException;
import ma.inpt.esj.exception.ResponsableNotFoundException;
import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.LiveForCreationDTO;


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

	public LiveDTO getOngoingLive();
	
	public byte[] getLiveImage(int id) throws LiveNotFoundException;
}
