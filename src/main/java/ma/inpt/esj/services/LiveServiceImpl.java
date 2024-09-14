package ma.inpt.esj.services;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import ma.inpt.esj.entities.LiveImage;
import ma.inpt.esj.entities.Responsable;
import ma.inpt.esj.entities.Theme;
import ma.inpt.esj.mappers.LiveMapper;
import ma.inpt.esj.mappers.QuestionMapper;
import ma.inpt.esj.mappers.ResponsableMapper;
import ma.inpt.esj.mappers.ThemeMapper;
import ma.inpt.esj.repositories.AdministrateurRepository;
import ma.inpt.esj.repositories.JeuneRepository;
import ma.inpt.esj.repositories.LiveImageRepository;
import ma.inpt.esj.repositories.LiveRepository;
import ma.inpt.esj.repositories.MedecinRepository;
import ma.inpt.esj.repositories.ProfessionnelRepository;
import ma.inpt.esj.repositories.ThemeRepository;
import java.time.Duration;

@Service
public class LiveServiceImpl implements LiveService {
    @Autowired
    LiveRepository liveRepository;
    @Autowired
    MailSending sender;
    @Autowired
    ThemeRepository thematiqueRepository;
    @Autowired
    ThemeMapper mapperTheme;
    @Autowired
    ResponsableMapper medecinMapper;
    @Autowired
    QuestionMapper mapperQuestions;
    @Autowired
    LiveMapper mapperLive;
    @Autowired
    JeuneService jeuneService;
    @Autowired
    JeuneRepository JR;
    @Autowired
    MedecinRepository medecinRepository;
    @Autowired
    ProfessionnelRepository professionelRepository;
    @Autowired
    AdministrateurRepository administrateurRepository;
    @Autowired
    private PdfGenerator pdfGenerator;
    @Autowired
    LiveImageRepository liveImageRepository;

    public List<LiveDTO> getAllLives(){
        List<Live> L=this.liveRepository.findAll();
        return this.mapperLive.allLivesToDtoLives(L);
    }
    
    // live passed
    public List<LiveDTO> getPassedLives(){
        LocalDateTime now=LocalDateTime.now();
        List<Live> L=this.liveRepository.findLastLiveBefore(now);
        return this.mapperLive.allLivesToDtoLives(L);
    }
    public List<LiveDTO> getAllByAdmin(int id){
        List<Live> L=this.liveRepository.findByAdminId(id);
        return this.mapperLive.allLivesToDtoLives(L);
    }
    
    // live on going
    @Transactional
    public List<LiveDTO> getongoing(int id){
        LocalDateTime Now=LocalDateTime.now();
        List<LiveDTO> LD=new ArrayList<>();
        System.out.println("Here at : "+Now);
        List<Live> L=this.liveRepository.findByAdminId(id);
        System.out.println("pass");
        for(int i=0;i<L.size();i++){
            LiveDTO DTO=this.mapperLive.liveToDTOLive(L.get(i));
            if(DTO.getDate().isAfter(Now)){
                LD.add(DTO);
            }
        }
        return  LD;
    }
    public List<LiveDTO> getongoingforuser(){
        LocalDateTime Now=LocalDateTime.now();
        List<LiveDTO> LD=new ArrayList<>();
        List<Live> L=this.liveRepository.findAll();
        for(int i=0;i<L.size();i++){
            LiveDTO DTO=this.mapperLive.liveToDTOLive(L.get(i));
            if(DTO.getDate().isAfter(Now)){
                LD.add(DTO);
            }
        }
        return  LD;
    }

    // to active / desactive a live
    /*
    public void activatdes(int id){
        Live l=this.liveRepository.findById(id).get();
        if(l.isActive()){
            l.setActive(false);
        }
        else{
            System.out.println(("you will receive the informations"));
            l.setActive(true);
            this.sender.setReceiver(l.getResponsable().getInfoUser().getMail());
            this.sender.setSubject("test");
            String body="GET YOUR JOB THEMATIC"+l.getThematiques().getContenu()+"FOR SUBJECT"+l.getSubject()+"for the link"+l.getLienYoutube();
            this.sender.setBody(body);
            Thread Thread=new Thread(this.sender);
            Thread.start();

        }
        this.liveRepository.save(l);
    }
    */
    public void activatdes(int id){
        Live l=this.liveRepository.findById(id).get();
        if(l.isActive()){
            l.setActive(false);
        }
        else{
            System.out.println(("you will receive the informations"));
            l.setActive(true);
            this.sender.setReceiver(l.getResponsable().hasInfoUser().getMail());
            this.sender.setSubject("Nouveau live assigné");
            String thematiqueContenu = l.getThematique().getContenu();
            LocalDateTime liveDate = l.getDate(); 
            String formattedDate = liveDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm"));
            String body = String.format(
            		"Bonjour,\n\n" +
                    "Vous êtes responsable de l'organisation du live sur la thématique : %s prévu pour le %s.\n\n" +
                    "Veuillez consulter le PDF ci-joint pour toutes les informations concernant le live, y compris le lien Streamyard qui vous redirigera vers la plateforme où vous organiserez votre live.\n\n" +
                    "Il est également important de vérifier votre espace sur la plateforme IES trois jours avant la date prévue du live. Vous y trouverez les questions des jeunes concernant cette thématique.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe d'organisation",
                    thematiqueContenu,
                    formattedDate
            );
            this.sender.setBody(body);
            // Generate PDF content
            try {
                byte[] pdfContent = this.pdfGenerator.generatePdf(
                    l.getSubject(),
                    thematiqueContenu,
                    formattedDate,
                    l.getLienStreamYard(),
                    l.getLienYoutube()
                );
                this.sender.setPdfContent(pdfContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.sender.setPdfTitle(l.getSubject());
            Thread Thread=new Thread(this.sender);
            Thread.start();

        }
        this.liveRepository.save(l);
    }

    // animated by doctor or infrimrie
    public List<LiveDTO> getAllAnimated(Long id){
        List<Live> L=this.liveRepository.findByResponsableId(id);
        List<LiveDTO> L1=this.mapperLive.allLivesToDtoLives(L);
        List<LiveDTO> L2=new ArrayList<>();
        LocalDateTime D=LocalDateTime.now();
        for(int i=0;i<L1.size();i++){
            if(L1.get(i).isActive()){
                if(L1.get(i).getDate().isBefore(D)){
                    L2.add(L1.get(i));
                }
            }
        }
        return L2;
    }

    // on going to animate by doctor
    public  List<LiveDTO> getongoingToan(Long id){
        List<Live> L=this.liveRepository.findByResponsableId(id);
        List<LiveDTO> L1=this.mapperLive.allLivesToDtoLives(L);
        //System.out.println(L1);
        List<LiveDTO> L2=new ArrayList<>();
        LocalDateTime D=LocalDateTime.now();
        for(int i=0;i<L1.size();i++){
            if(L1.get(i).isActive()){
                if(L1.get(i).getDate().isAfter(D)){
                    L2.add(L1.get(i));
                }
            }
        }
        return L2;
    }

    // live not activet yet
    public List<LiveDTO> getfileattentetoactive(int id){
        List<LiveDTO> L1=this.getongoing(id);
        List<LiveDTO> L2=new ArrayList<>();
        for(int i=0;i<L1.size();i++){
            if(!L1.get(i).isActive()){
                L2.add(L1.get(i));
            }
        }
        return L2;
    }
    //live activated
    public List<LiveDTO> getfileattente(int id){
        List<LiveDTO> L1=this.getongoing(id);
        List<LiveDTO> L2=new ArrayList<>();
        for(int i=0;i<L1.size();i++){
            if(L1.get(i).isActive()){
                L2.add(L1.get(i));

            }

        }
        return L2;
    }
    public List<LiveDTO> getAllfileattenteforuser(){
        List<LiveDTO> L1=this.getongoingforuser();
        List<LiveDTO> L2=new ArrayList<>();
        for(int i=0;i<L1.size();i++){
            if(L1.get(i).isActive()){
                L2.add(L1.get(i));

            }

        }
        return L2;
    }

    public List<LiveDTO> getonquestions(int id){
        List<LiveDTO> L1=this.getfileattente(id);
        List<LiveDTO> Sending=new ArrayList<>();
        LocalDateTime D=LocalDateTime.now();

        for(int i=0;i<L1.size();i++){
            long daysBetween = ChronoUnit.DAYS.between(D, L1.get(i).getDate());

            if(daysBetween>3 && daysBetween<=28){
                Sending.add(L1.get(i));
            }
        }
        return Sending;
    }
    public List<LiveDTO> getonquestionsforuser(){
        List<LiveDTO> L1=this.getAllfileattenteforuser();
        List<LiveDTO> Sending=new ArrayList<>();
        LocalDate D=LocalDate.now();

        for(int i=0;i<L1.size();i++){
            //Period P=Period.between(D,L1.get(i).getDate().toLocalDate());
            //int days=P.getDays();
            long daysBetween = ChronoUnit.DAYS.between(D, L1.get(i).getDate().toLocalDate());

            System.out.println(daysBetween);
            if(daysBetween>3&&daysBetween<=28){
                Sending.add(L1.get(i));
            }
        }
        return Sending;
    }

    //phase j-3
    @Transactional
    //@Scheduled(cron = "0 0 0 * * *")
    public List<LiveDTO> getonfinal(int id){
        List<LiveDTO> L1=this.getfileattente(id);
        List<LiveDTO> L2=new ArrayList<>();
        LocalDateTime D=LocalDateTime.now();
        for(int i=0;i<L1.size();i++){

            long daysBetween = ChronoUnit.DAYS.between(D, L1.get(i).getDate());

            if(daysBetween==3){
                System.out.println("a mesg will be sended to you");
                L2.add(L1.get(i));
            }
            if(daysBetween<3){
                L2.add(L1.get(i));
            }
        }

        return L2;
    }
    @Transactional
    //@Scheduled(cron = "0 0 0 * * *")
    public List<LiveDTO> getonfinalforuser(){
        List<LiveDTO> L1=this.getAllfileattenteforuser();
        List<LiveDTO> L2=new ArrayList<>();
        LocalDateTime D=LocalDateTime.now();
        for(int i=0;i<L1.size();i++){
            long daysBetween = ChronoUnit.DAYS.between(D, L1.get(i).getDate());

            if(daysBetween<=3){
                L2.add(L1.get(i));
            }
        }

        return L2;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void checkisActivated(){
        LocalDateTime D=LocalDateTime.now();
        List<LiveDTO> L=this.getongoingforuser();
        for(int i=0;i<L.size();i++){
            long daysBetween = ChronoUnit.DAYS.between(D, L.get(i).getDate());

            if(L.get(i).isActive()==false) {
                if (daysBetween <= 45) {
                    this.activatdes(L.get(i).getId());
                }
            }
        }
    }

    public LiveDTO getSingleLive(int id){
        Live l=this.liveRepository.findById(id).get();
        System.out.println(l.getResponsable().hasInfoUser().getMail());
        return this.mapperLive.liveToDTOLive(l);
    }
    
    @Transactional
    public void createLive(LiveForCreationDTO liveRequest, MultipartFile file, Long adminId) throws ResponsableNotFoundException, AdministrateurNotFoundException, IOException{
    	Responsable responsable;
    	String role = liveRequest.getResponsable().getRole();
    	Long responsableId = liveRequest.getResponsable().getId();
    	if (role.equals("PROFESSIONELSANTE")) {
            responsable = professionelRepository.findById(responsableId)
                    .orElseThrow(() -> new ResponsableNotFoundException("Le professionnel de santé est introuvable"));
        } else {
            responsable = medecinRepository.findById(responsableId)
                    .orElseThrow(() -> new ResponsableNotFoundException("Le médecin est introuvable"));
        }
    	Administrateur admin = administrateurRepository.findById(adminId)
                .orElseThrow(() -> new AdministrateurNotFoundException("L'administrateur est introuvable"));
    	Live live = this.mapperLive.dtoLiveForCreationToLive(liveRequest, responsable, admin);

    	LocalDateTime D=LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(D, live.getDate());
        Optional<Theme> P=this.thematiqueRepository.findByContenu(live.getThematique().getContenu());
        if(P.isPresent()){
            live.setThematique(P.get());
        }
        else{
            this.thematiqueRepository.save(live.getThematique());
            Theme P2=this.thematiqueRepository.findByContenu(live.getThematique().getContenu()).get();
            live.setThematique(P2);
        }
        
        LiveImage img = new LiveImage();
        img.setImageData(file.getBytes());
        LiveImage savedImg = this.liveImageRepository.save(img);
        
        live.setImg(savedImg.getId());
        
        this.liveRepository.save(live);
        
        if(daysBetween<45)
        	this.activatdes(live.getId());
    }
    public void updateLive(LiveDTO L,Long adminId,int liveId) throws ResponsableNotFoundException, LiveNotFoundException{
    	Live existingLive = liveRepository.findById(liveId)
    			.orElseThrow(() -> new LiveNotFoundException("Le live est introuvable"));
    	Long responsableId = L.getResponsable().getId();
    	if (responsableId != existingLive.getResponsable().getId()) {
    		Responsable responsable;
        	String role = L.getResponsable().getRole();
        	if (role.equals("Professionnel de Santé")) {
                responsable = professionelRepository.findById(responsableId)
                        .orElseThrow(() -> new ResponsableNotFoundException("Le professionnel de santé est introuvable"));
            } else {
                responsable = medecinRepository.findById(responsableId)
                        .orElseThrow(() -> new ResponsableNotFoundException("Le médecin est introuvable"));
            }
        	existingLive.setResponsable(responsable);
    	}
    	existingLive.setLienStreamYard(L.getLienStreamYard());
    	existingLive.setLienYoutube(L.getLienYoutube());

        this.liveRepository.save(existingLive);
    }
    public void deleteLive(int id) throws LiveNotFoundException{
    	Live l = this.liveRepository.findById(id)
    			.orElseThrow(() -> new LiveNotFoundException("Le live d'id "+id+" est introuvable."));
    	this.liveImageRepository.deleteById(l.getImg());
        this.liveRepository.deleteById(id);
    }

	public LiveDTO getLastLive() {
		List<Live> outdatedLives = this.liveRepository.findLastLiveBefore(LocalDateTime.now());
		
		if (!outdatedLives.isEmpty()) {
            return this.mapperLive.liveToDTOLive(outdatedLives.get(0));
        } else {
            return null; 
        }
	}

    @Override
    public LiveDTO getOngoingLive() {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.ofMinutes(90);
        LocalDateTime startTime = now.minus(duration);
        LocalDateTime endTime = now;

        List<Live> airingLives = liveRepository.findByStartTimeAndEndTime(startTime, endTime);

        if (!airingLives.isEmpty()) {
            return mapperLive.liveToDTOLive(airingLives.get(0));
        }

        return null;
    }
	
	public byte[] getLiveImage(int id) throws LiveNotFoundException{
		Optional<Live> liveOptional = this.liveRepository.findById(id);
		if (liveOptional.isEmpty()) 
			throw new LiveNotFoundException("Le live d'id "+id+" est introuvable.");
		Optional<LiveImage> liveImage = this.liveImageRepository.findById(liveOptional.get().getImg());
		if (liveImage.isPresent()) 
			return liveImage.get().getImageData();
		else 
			return null;
	}
	
}
