package ma.inpt.esj.Mailer;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.UnitValue;
@Service
public class PdfGenerator {

    public byte[] generatePdf(String title, String thematiqueContenu, String formattedDate, String lienStreamYard, String lienYoutube) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);
        document.setMargins(20, 30, 30, 20);
        /*
        Image logo = new Image(ImageDataFactory.create(""));
        logo.setWidth(UnitValue.createPercentValue(20));
        document.add(logo);
        */
        document.add(new Paragraph("Informations du Live")
        		.setBold()
                .setFontSize(18)
                .setTextAlignment(com.itextpdf.layout.property.TextAlignment.CENTER)
                .setMarginBottom(20)
        );

        float[] pointColumnWidths = {150F, 350F};
        Table table = new Table(UnitValue.createPercentArray(pointColumnWidths));
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginBottom(20);
        
        table.addCell("Titre").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(title);

        table.addCell("Thématique").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(thematiqueContenu);

        table.addCell("Date").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(formattedDate);

        table.addCell("Lien StreamYard").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(lienStreamYard);

        table.addCell("Lien YouTube").setBold().setBackgroundColor(ColorConstants.LIGHT_GRAY);
        table.addCell(lienYoutube);

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}
