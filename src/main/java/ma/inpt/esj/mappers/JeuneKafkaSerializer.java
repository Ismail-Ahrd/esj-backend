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
//        gen.writeObject();
//        gen.writeObject(jeune.getDossierMedial());
//        gen.writeNumberField("id", jeune.getDossierMedial().getId());
//
//        // Serialize the list of AntecedentFamilial
//        gen.writeArrayFieldStart("antecedentsFamiliaux");
//        for (AntecedentFamilial antecedentFamilial : jeune.getDossierMedial().getAntecedentsFamiliaux()) {
//            gen.writeStartObject();
//            gen.writeArrayFieldStart("maladiesFamiliales");
//            if (antecedentFamilial.getMaladiesFamiliales() != null) {
//                for (String maladie : antecedentFamilial.getMaladiesFamiliales()) {
//                    gen.writeString(maladie);
//                }
//            }
//            gen.writeEndArray();
//            gen.writeStringField("typeAntFam", antecedentFamilial.getTypeAntFam());
//            gen.writeStringField("autre", antecedentFamilial.getAutre());
//            gen.writeEndObject();
//        }
//        gen.writeEndArray();
//
//        // Serialize the list of AntecedentPersonnel
//        gen.writeArrayFieldStart("antecedentsPersonnels");
//        for (AntecedentPersonnel antecedentPersonnel : jeune.getDossierMedial().getAntecedentsPersonnels()) {
//            gen.writeStartObject();
//            gen.writeArrayFieldStart("maladies");
//            if (antecedentPersonnel.getMaladies() != null) {
//                for (String maladie : antecedentPersonnel.getMaladies()) {
//                    gen.writeString(maladie);
//                }
//            }
//            gen.writeEndArray();
//            gen.writeBooleanField("utiliseMedicaments", antecedentPersonnel.getUtiliseMedicaments() != null ? antecedentPersonnel.getUtiliseMedicaments() : false);
//            gen.writeArrayFieldStart("medicaments");
//            if (antecedentPersonnel.getMedicaments() != null) {
//                for (String medicament : antecedentPersonnel.getMedicaments()) {
//                    gen.writeString(medicament);
//                }
//            }
//            gen.writeEndArray();
//            gen.writeBooleanField("chirurgicaux", antecedentPersonnel.getChirurgicaux() != null ? antecedentPersonnel.getChirurgicaux() : false);
//            // Serialize OperationChirurgicale if needed
//            // gen.writeObjectField("operationsChirurgicales", antecedentPersonnel.getOperationsChirurgicales());
//            gen.writeArrayFieldStart("habitudes");
//            if (antecedentPersonnel.getHabitudes() != null) {
//                for (String habitude : antecedentPersonnel.getHabitudes()) {
//                    gen.writeString(habitude);
//                }
//            }
//            gen.writeEndArray();
            gen.writeNumberField("cigarettesParJour", jeune.getDossierMedial().getAntecedentsPersonnels()!=null ? jeune.getDossierMedial().getAntecedentsPersonnels().get(0).getCigarettesParJour():0);
            gen.writeStringField("consommation alcool", jeune.getDossierMedial().getAntecedentsPersonnels()!=null ? jeune.getDossierMedial().getAntecedentsPersonnels().get(0).getConsommationAlcool():"pas");
            gen.writeStringField("tempsEcrans", jeune.getDossierMedial().getAntecedentsPersonnels()!=null ? jeune.getDossierMedial().getAntecedentsPersonnels().get(0).getTempsEcran():"normal");
//            gen.writeNumberField("cigarettesParJour", jeune.getDossierMedial().getAntecedentsPersonnels()!=null ? jeune.getDossierMedial().getAntecedentsPersonnels().get(0).getCigarettesParJour():0);
//            gen.writeStringField("consommationAlcool", antecedentPersonnel.getConsommationAlcool()!=null?antecedentPersonnel.getConsommationAlcool():null);
//            gen.writeStringField("tempsEcran", antecedentPersonnel.getTempsEcran()!=null?antecedentPersonnel.getTempsEcran():null);
//            gen.writeStringField("dureeFumee", antecedentPersonnel.getDureeFumee()!=null?antecedentPersonnel.getDureeFumee():null);
//        }
//        gen.writeEndArray();
        // Serialize dossierMedical
        gen.writeObjectFieldStart("dossierMedical");
//        // Serialize the list of Consultation
        gen.writeArrayFieldStart("historiqueConsultations");
        for (Consultation consultation : jeune.getDossierMedial().getHistoriqueConsultations()) {
            gen.writeStartObject();
                gen.writeNumberField("id", consultation.getId());
                gen.writeNumberField("date", consultation.getDate().getTime());
                gen.writeStringField("motif", consultation.getMotif());

                gen.writeFieldName("Antecedent Personnel");
                gen.writeStartObject();
                    gen.writeStringField("type", consultation.getAntecedentPersonnel().getType());
                    gen.writeStringField("specification", consultation.getAntecedentPersonnel().getSpecification());
                    gen.writeStringField("autres specification", consultation.getAntecedentPersonnel().getSpecificationAutre());
                gen.writeEndObject();
                gen.writeFieldName("Antecedent Familial");
                    gen.writeStartObject();
                    gen.writeStringField("type", consultation.getAntecedentFamilial().getTypeAntFam());
                    gen.writeStringField("specification", consultation.getAntecedentFamilial().getAutre());
                    gen.writeEndObject();

                gen.writeStringField("interrogatoire", consultation.getInterrogatoire());

                // Serialize "examenMedicals"
                gen.writeFieldName("ExamenMedicaux");
                    gen.writeStartArray();
                    for (ExamenMedical examen : consultation.getExamenMedicals()) {
                        gen.writeObject(examen);
                    }
                    gen.writeEndArray();

                // Serialize "conseils"
                gen.writeStringField("conseils", consultation.getConseils());


            gen.writeEndObject();//consultation
        }
        gen.writeEndArray();
//
        gen.writeEndObject();  // End of dossierMedical

        gen.writeEndObject();  // End of Jeune
    }
}
