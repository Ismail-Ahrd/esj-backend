package ma.inpt.esj.controllers;

import java.util.List;
import java.util.Optional;

import ma.inpt.esj.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.websocket.server.PathParam;
import ma.inpt.esj.dto.AdministrateurDTO;
import ma.inpt.esj.dto.LiveDTO;
import ma.inpt.esj.dto.LiveForCreationDTO;
import ma.inpt.esj.entities.Administrateur;
import ma.inpt.esj.services.AdministrateurService;
import ma.inpt.esj.services.LiveService;

@RestController
@RequestMapping("/admins")
public class AdministrateurController {
	@Autowired
	private AdministrateurService service;
	@Autowired
	private LiveService liveService;
	@Autowired
	private ObjectMapper objectMapper;
	
	@GetMapping
    public ResponseEntity<List<Administrateur>> getAll(){
		try {
			List<Administrateur> list_administrateurs = this.service.getAllAdmin();
			if (list_administrateurs.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return ResponseEntity.status(HttpStatus.OK).body(list_administrateurs);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getSingleOne(@PathVariable("id") Long id){
    	try {
    		Optional<Administrateur> admin = this.service.getSingleone(id);
            if (admin.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(admin.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PostMapping
    public ResponseEntity<String> createOne(@RequestBody Administrateur ad) {
        try {
            this.service.createOne(ad);
            return ResponseEntity.status(HttpStatus.CREATED).body("Administrateur created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating Administrateur");
        }
    }
    
   /* @RequestMapping(method = RequestMethod.POST,value = "/Admins/Aut")
    public ResponseEntity Authenticate(@RequestBody Administrateur ad){

        return this.Server.Authenticate(ad);

    }*/
    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurDTO> updateOne(@RequestBody AdministrateurDTO ad,@PathVariable Long id){
    	try {
    		ad.setId(id);
    		this.service.updateOne(ad);
    		return ResponseEntity.status(HttpStatus.OK).body(ad);
    	} catch (AdministrateurNotFoundException e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	}
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleted(@PathVariable Long id){
    	try {
            this.service.deleteOne(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (AdministrateurNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    /*=========================================Streams=========================*/
    // update a live
    @PutMapping("/{idadmin}/streams/{idlive}")

    public ResponseEntity<LiveDTO> UpdateLive (@RequestBody LiveDTO L,@PathVariable("idadmin") Long id,@PathVariable("idlive") int id2) throws ResponsableNotFoundException{
    	try {
    		L.setId(id2);
    		this.liveService.updateLive(L,id,id2);
    		return ResponseEntity.status(HttpStatus.OK).body(L);
    	} catch (LiveNotFoundException e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	}
    }

    @GetMapping("/{idadmin}/streams")
    public ResponseEntity<List<LiveDTO>> getAllLives(@PathVariable("idadmin") int id, @PathParam(value = "phase") String phase){
		try {
			List<LiveDTO> L;
			if (phase.equals("notactivated")) L = this.liveService.getfileattentetoactive(id);
			else if (phase.equals("question")) L = this.liveService.getonquestions(id);
			else if (phase.equals("final")) L = this.liveService.getonfinal(id);
			else L = this.liveService.getAllByAdmin(id);
			if (L.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return ResponseEntity.status(HttpStatus.OK).body(L);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
    
    
 // to add a live
    @PostMapping("/{id}/streams")

    public  ResponseEntity<String> createLive(@RequestPart("live") String live, @RequestPart("image") MultipartFile file, @PathVariable("id") Long id) {
    	try {
    		LiveForCreationDTO liveForCreationRequest = objectMapper.readValue(live, LiveForCreationDTO.class);
        	this.liveService.createLive(liveForCreationRequest, file, id);
            return ResponseEntity.status(HttpStatus.CREATED).body("Live created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating Live");
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AdministrateurDTO> saveAdmin(@RequestBody Administrateur administrateur) {
        try {
            // Use the injected 'service' instance instead of calling it statically
            AdministrateurDTO savedAdmin = service.saveAdmin(administrateur);
            return ResponseEntity.ok(savedAdmin);
        } catch (AdminException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
    
}
