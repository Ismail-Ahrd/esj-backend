package ma.inpt.esj.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ma.inpt.esj.exception.ThemeNotFoundException;
import ma.inpt.esj.dto.ThemeDTO;
import ma.inpt.esj.services.ThemeService;

import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    @Autowired
    ThemeService themeService;
    
    @GetMapping
    public ResponseEntity<List<ThemeDTO>> getAll(){
		try {
			List<ThemeDTO> list_themes = this.themeService.getAll();
			if (list_themes.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
			return ResponseEntity.status(HttpStatus.OK).body(list_themes);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
    }
   /* @RequestMapping("/thematics/live/{id}")
    public List<DTOTHEMATIQUE> getAllByLive(@PathVariable int id){
        return this.Rest.getAllByIdLiver(id);
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<ThemeDTO> getSingleOne(@PathVariable int id){
    	try {
    		ThemeDTO theme = this.themeService.getSingle(id);
            if (theme != null) {
                return ResponseEntity.status(HttpStatus.OK).body(theme);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<String> createOne(@RequestBody ThemeDTO T) {
        try {
        	this.themeService.createOne(T);
            return ResponseEntity.status(HttpStatus.CREATED).body("Theme created successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating Theme");
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ThemeDTO> updateOne(@RequestBody ThemeDTO P,@PathVariable int id){
    	try {
    		P.setId(id);
    		this.themeService.updateOne(P,id);
    		return ResponseEntity.status(HttpStatus.OK).body(P);
    	} catch (ThemeNotFoundException e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    	}
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> deleted(@PathVariable int id){
    	try {
    		this.themeService.deleteOne(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (ThemeNotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
