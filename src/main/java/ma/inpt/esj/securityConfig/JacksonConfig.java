package ma.inpt.esj.securityConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ma.inpt.esj.entities.Responsable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)*/


@Configuration
public class JacksonConfig {

	@Bean
	public ObjectMapper objectMapper() {
	    ObjectMapper mapper = new ObjectMapper();

	    // Désactive la fonctionnalité qui échoue sur les beans vides
	    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

	    // Enregistre le module pour les types de date et heure Java 8
	    mapper.registerModule(new JavaTimeModule());

	    // Enregistre le module pour les autres classes de Java 8
	    mapper.registerModule(new Jdk8Module());

	    // Enregistre les sous-types
	    SimpleModule module = new SimpleModule();
	    module.registerSubtypes(Responsable.class);
	    // Ne pas enregistrer ConcreteUtilisateur pour Responsable
	    mapper.registerModule(module);

	    return mapper;
	}

}