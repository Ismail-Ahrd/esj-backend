package ma.inpt.esj.mappers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import ma.inpt.esj.entities.*;

import java.io.IOException;

public class JeuneKafkaSerializer extends JsonSerializer<Jeune> {

    @Override
    public void serialize(Jeune jeune, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", jeune.getId());

        // Serialize infoUser
        gen.writeObjectFieldStart("infoUser");
        gen.writeNumberField("id", jeune.getInfoUser().getId());
        gen.writeStringField("nom", jeune.getInfoUser().getNom());
        gen.writeStringField("prenom", jeune.getInfoUser().getPrenom());
        gen.writeStringField("numTel", jeune.getInfoUser().getNumTel());
        gen.writeStringField("mail", jeune.getInfoUser().getMail());
        gen.writeEndObject();

        // Serialize other Jeune fields
        gen.writeStringField("sexe", jeune.getSexe().name());
        gen.writeNumberField("dateNaissance", jeune.getDateNaissance().getTime());
        gen.writeNumberField("age", jeune.getAge());
        gen.writeNumberField("identifiantPatient", jeune.getIdentifiantPatient());
        gen.writeBooleanField("scolarise", jeune.isScolarise());
        gen.writeStringField("cin", jeune.getCin());
        gen.writeBooleanField("favorite", jeune.isFavorite());

        // Serialize dossierMedical
        gen.writeObjectFieldStart("dossierMedical");
        gen.writeNumberField("id", jeune.getDossierMedial().getId());

        // Serialize the list of AntecedentFamilial
        gen.writeArrayFieldStart("antecedentsFamiliaux");
        for (AntecedentFamilial antecedentFamilial : jeune.getDossierMedial().getAntecedentsFamiliaux()) {
            gen.writeStartObject();
            gen.writeArrayFieldStart("maladiesFamiliales");
            if (antecedentFamilial.getMaladiesFamiliales() != null) {
                for (String maladie : antecedentFamilial.getMaladiesFamiliales()) {
                    gen.writeString(maladie);
                }
            }
            gen.writeEndArray();
            gen.writeStringField("typeAntFam", antecedentFamilial.getTypeAntFam());
            gen.writeStringField("autre", antecedentFamilial.getAutre());
            gen.writeEndObject();
        }
        gen.writeEndArray();

        // Serialize the list of AntecedentPersonnel
        gen.writeArrayFieldStart("antecedentsPersonnels");
        for (AntecedentPersonnel antecedentPersonnel : jeune.getDossierMedial().getAntecedentsPersonnels()) {
            gen.writeStartObject();
            gen.writeArrayFieldStart("maladies");
            if (antecedentPersonnel.getMaladies() != null) {
                for (String maladie : antecedentPersonnel.getMaladies()) {
                    gen.writeString(maladie);
                }
            }
            gen.writeEndArray();
            gen.writeBooleanField("utiliseMedicaments", antecedentPersonnel.getUtiliseMedicaments());
            gen.writeArrayFieldStart("medicaments");
            if (antecedentPersonnel.getMedicaments() != null) {
                for (String medicament : antecedentPersonnel.getMedicaments()) {
                    gen.writeString(medicament);
                }
            }
            gen.writeEndArray();
            gen.writeBooleanField("chirurgicaux", antecedentPersonnel.getChirurgicaux());
            // Serialize OperationChirurgicale if needed
            // gen.writeObjectField("operationsChirurgicales", antecedentPersonnel.getOperationsChirurgicales());
            gen.writeArrayFieldStart("habitudes");
            if (antecedentPersonnel.getHabitudes() != null) {
                for (String habitude : antecedentPersonnel.getHabitudes()) {
                    gen.writeString(habitude);
                }
            }
            gen.writeEndArray();
            gen.writeNumberField("cigarettesParJour", antecedentPersonnel.getCigarettesParJour());
            gen.writeStringField("consommationAlcool", antecedentPersonnel.getConsommationAlcool());
            gen.writeStringField("tempsEcran", antecedentPersonnel.getTempsEcran());
            gen.writeStringField("dureeFumee", antecedentPersonnel.getDureeFumee());
            gen.writeStringField("type", antecedentPersonnel.getType());
            gen.writeStringField("specification", antecedentPersonnel.getSpecification());
            gen.writeStringField("specificationAutre", antecedentPersonnel.getSpecificationAutre());
            gen.writeNumberField("nombreAnnee", antecedentPersonnel.getNombreAnnee());
            gen.writeEndObject();
        }
        gen.writeEndArray();

        // Serialize the list of Consultation
        gen.writeArrayFieldStart("historiqueConsultations");
        for (Consultation consultation : jeune.getDossierMedial().getHistoriqueConsultations()) {
            gen.writeStartObject();
            gen.writeNumberField("id", consultation.getId());
            gen.writeNumberField("date", consultation.getDate().getTime());
            gen.writeStringField("motif", consultation.getMotif());
            gen.writeStringField("historiqueClinique", consultation.getHistoriqueClinique());
            gen.writeStringField("examenClinique", consultation.getExamenClinique());
            gen.writeStringField("Diagnostic", consultation.getDiagnostic());
            gen.writeStringField("Ordonnance", consultation.getOrdonnance());
            gen.writeEndObject();
        }
        gen.writeEndArray();

        gen.writeEndObject();  // End of dossierMedical

        gen.writeEndObject();  // End of Jeune
    }
}
