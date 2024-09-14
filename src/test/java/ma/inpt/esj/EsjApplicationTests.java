package ma.inpt.esj;

import ma.inpt.esj.entities.AntecedentPersonnel;
import ma.inpt.esj.entities.Jeune;
import ma.inpt.esj.services.JeuneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EsjApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private JeuneService jeuneService;
	@Test
	void createAntecedantPersonnel(){
		AntecedentPersonnel ap = AntecedentPersonnel.builder().nombreAnnee(7)
				.type("un type ")
				.specification("une specification")
				.build();
		System.out.println("ap created "+ap.toString());
		Jeune jeune = new Jeune();
		jeune.setAge(99);
		jeuneService.saveOrUpdate(jeune);
		jeuneService.addAntecedentPersonnelToJeune(jeune.getId(),ap);
	}

}
