package ma.inpt.esj.services;

import ma.inpt.esj.dto.DiscussionRequestDto;
import ma.inpt.esj.dto.DiscussionResponseDto;
import ma.inpt.esj.dto.PageResponseDto;
import ma.inpt.esj.entities.Discussion;
import ma.inpt.esj.entities.Invitation;
import ma.inpt.esj.entities.Medecin;
import ma.inpt.esj.enums.DiscussionStatus;
import ma.inpt.esj.enums.GenreDiscussion;
import ma.inpt.esj.enums.InvitationStatus;
import ma.inpt.esj.exception.DiscussionException;
import ma.inpt.esj.exception.DiscussionNotFoundException;
import ma.inpt.esj.exception.MedecinNotFoundException;
import ma.inpt.esj.mappers.DiscussionMapper;
import ma.inpt.esj.repositories.DiscussionRepository;
import ma.inpt.esj.repositories.InvitationRepository;
import ma.inpt.esj.repositories.MedecinRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static ma.inpt.esj.enums.DiscussionStatus.EN_COURS;
import static ma.inpt.esj.enums.DiscussionStatus.PLANIFIEE;
import static ma.inpt.esj.enums.DiscussionStatus.TERMINEE;;;

@Service
public class DiscussionServiceImpl implements DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final MedecinRepository medecinRepository;
    private final DiscussionMapper discussionMapper;
    private final InvitationRepository invitationRepository;

    @Autowired
    public DiscussionServiceImpl(
        DiscussionRepository discussionRepository, 
        MedecinRepository medecinRepository, 
        DiscussionMapper discussionMapper,
        InvitationRepository invitationRepository
    ) {
        this.discussionRepository = discussionRepository;
        this.medecinRepository = medecinRepository;
        this.discussionMapper = discussionMapper;
        this.invitationRepository = invitationRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DiscussionResponseDto> getAllDiscussions() throws DiscussionException {
        try {
            List<Discussion> discussions = discussionRepository.findAll();
            List<DiscussionResponseDto> discussionResponseDtos = new ArrayList<>();
            discussions.forEach(discussion -> {
                discussionResponseDtos.add(discussionMapper.fromDiscussionToDiscussionResponseDto(discussion));
            });
            return discussionResponseDtos;
        } catch (Exception e) {
            System.out.println(e);
            throw new DiscussionException("Erreur lors de la récupération des discussions", e);
        }
    }

    @Override
    public List<DiscussionResponseDto> getOuverteDiscussions() throws DiscussionException {
        try {
            List<Discussion> discussions = discussionRepository.findByGenreAndStatusIn(
                GenreDiscussion.OUVERTE, 
                List.of(DiscussionStatus.PLANIFIEE, DiscussionStatus.EN_COURS)
            );
            List<DiscussionResponseDto> discussionResponseDtos = new ArrayList<>();
            discussions.forEach(discussion -> {
                discussionResponseDtos.add(discussionMapper.fromDiscussionToDiscussionResponseDto(discussion));
            });
            return discussionResponseDtos;
        } catch (Exception e) {
            System.out.println(e);
            throw new DiscussionException("Erreur lors de la récupération des discussions", e);
        }
    }

    @Override
    public PageResponseDto<DiscussionResponseDto> getMyDiscussions(
        Long organizerId, String keyword, DiscussionStatus status, boolean isParticipant, int page, int size
    ) throws DiscussionException {
        try {
            Medecin medecin = medecinRepository.findById(organizerId).get();
            Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
            Page<Discussion> discussionPages = null;
            
            if(isParticipant == false) {
                if (status == null || status.equals("")) {
                    discussionPages =  discussionRepository.findByMedcinResponsableAndTitreContains(
                        medecin, keyword, pageable
                    );
                } else {
                    discussionPages = discussionRepository.findByMedcinResponsableAndTitreContainsAndStatus(
                        medecin, keyword, status, pageable
                    );
                }
            } else {
                if (status == null || status.equals("")) {
                    discussionPages =  discussionRepository.findDiscussionsByParticipantIdAndTitreContains(
                        organizerId, keyword, pageable
                    );
                } else {
                    discussionPages = discussionRepository.findDiscussionsByParticipantIdAndStatusAndTitreContains(
                        organizerId, status, keyword, pageable
                    );
                }
            }

            List<DiscussionResponseDto> discussionResponsePages = discussionPages.stream().map(discussion -> {
                return discussionMapper.fromDiscussionToDiscussionResponseDto(discussion);
            }).collect(Collectors.toList());

            PageResponseDto<DiscussionResponseDto> response = new PageResponseDto<DiscussionResponseDto>();
            response.setContent(discussionResponsePages);
            response.setCurrentPage(page);
            response.setPageSize(discussionPages.getSize());
            response.setTotalPages(discussionPages.getTotalPages());
            response.setTotalElement(discussionPages.getTotalElements());

            return response;
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors de la récupération des discussions", e);
        }
    }


    @Override
    @Transactional
    public DiscussionResponseDto createDiscussion(DiscussionRequestDto discussionRequestDto, Long organizerId) throws DiscussionException {
        if (discussionRequestDto == null) {
            throw new DiscussionException("La discussion ne doit pas être nulle.");
        }
        if (!discussionRequestDto.getMedcinResponsableId().equals(organizerId)) {
            throw new DiscussionException("La discussion peut etre lancer seulement par le medecin responsable");
        }
        try {
            discussionRequestDto.setStatus(PLANIFIEE);
            Discussion discussion = discussionMapper.fromDiscussionRequestDtoToDiscussion(discussionRequestDto);

            List<Medecin> existingMedecins = medecinRepository.findAllById(discussionRequestDto.getMedecinsInvitesIds());
            discussion.setMedecinsInvites(new ArrayList<>(existingMedecins));

            existingMedecins.forEach(medecin -> {
                Invitation invitation = Invitation.builder()
                        .status(InvitationStatus.INVITEE)
                        .medecinInvite(medecin)
                        .discussion(discussion)
                        .build();
                invitationRepository.save(invitation);
                discussion.getInvitations().add(invitation);
            });

            Discussion savedDiscussion = discussionRepository.save(discussion);
            DiscussionResponseDto discussionResponseDto = discussionMapper.fromDiscussionToDiscussionResponseDto(savedDiscussion);
            return discussionResponseDto;
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors de l'enregistrement de la discussion", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Discussion getDiscussion(Long id) throws DiscussionNotFoundException {
        return discussionRepository.findById(id)
                .orElseThrow(() -> new DiscussionNotFoundException("La discussion avec l'identifiant " + id + " non trouvée."));
    }

    @Override
    public DiscussionResponseDto getDiscussionResponseDto(Long id) throws DiscussionNotFoundException {
        Discussion discussion = getDiscussion(id);
        return discussionMapper.fromDiscussionToDiscussionResponseDto(discussion);
    }

    @Override
    @Transactional
    public DiscussionResponseDto startDiscussion(Long id, Long userId) throws DiscussionNotFoundException, DiscussionException {
        Discussion discussion = getDiscussion(id);
        Long respId = discussion.getMedcinResponsable().getId();
        if (respId != userId) {
            throw new DiscussionException("Seulement le medcin responsable peut lancer la discussion.");
        }
        discussion.setStatus(EN_COURS);
        try {
            Discussion savedDiscussion = discussionRepository.save(discussion);
            return discussionMapper.fromDiscussionToDiscussionResponseDto(savedDiscussion);
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors du lancement de la discussion", e);
        }
    }

    @Override
    @Transactional
    public DiscussionResponseDto endDiscussion(Long id, Long userId)
        throws DiscussionNotFoundException, DiscussionException 
    {
        Discussion discussion = getDiscussion(id);
        Long respId = discussion.getMedcinResponsable().getId();
        if (respId != userId) {
            throw new DiscussionException("Seulement le medcin responsable peut terminer la discussion.");
        }
        discussion.setStatus(TERMINEE);
        try {
            Discussion savedDiscussion = discussionRepository.save(discussion);
            return discussionMapper.fromDiscussionToDiscussionResponseDto(savedDiscussion);
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors du lancement de la discussion", e);
        }
    }

    @Override
    @Transactional
    public Discussion joinDiscussion(Long id, Long medecinId) throws DiscussionNotFoundException, MedecinNotFoundException, DiscussionException {
        Discussion discussion = getDiscussion(id);

        Medecin medecin = medecinRepository.findById(medecinId)
                .orElseThrow(() -> new MedecinNotFoundException("Le médecin avec l'identifiant " + medecinId + " non trouvé."));

        if (discussion.getGenre().equals(GenreDiscussion.PRIVEE)) {
            throw new DiscussionException("Vous seul pouvez participer à des discussions ouvertes.");
        }
    
        discussion.addMedecin(medecin);
    
        try {
            return discussionRepository.save(discussion);
        } catch (Exception e) {
            throw new DiscussionException("Erreur lors de l'ajout du médecin à la discussion", e);
        }
    }
    
    @Override
    @Transactional
    public List<DiscussionResponseDto> getDiscussionsInMonth(Long organizerId,int year, int month) throws DiscussionException{
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, year);
        startCalendar.set(Calendar.MONTH, month - 1);
        startCalendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = startCalendar.getTime();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, year);
        endCalendar.set(Calendar.MONTH, month - 1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endDate = endCalendar.getTime();
        Medecin medecin = medecinRepository.findById(organizerId).get();
        try {
            List<Discussion> discussions = discussionRepository.findByMedcinResponsableAndDateBetween(medecin, startDate, endDate);
            List<Discussion> discussions2 = discussionRepository.findByParticipantIdAndDateBetween(organizerId, startDate, endDate);
            List<DiscussionResponseDto> discussionResponseDtos = new ArrayList<>();
            discussions.forEach(discussion -> {
                discussionResponseDtos.add(discussionMapper.fromDiscussionToDiscussionResponseDto(discussion));
            });
            discussions2.forEach(discussion -> {
                discussionResponseDtos.add(discussionMapper.fromDiscussionToDiscussionResponseDto(discussion));
            });
            return discussionResponseDtos;
        } catch (Exception e) {
            System.out.println(e);
            throw new DiscussionException("Erreur lors de la récupération des discussions", e);
        }
    }

}
