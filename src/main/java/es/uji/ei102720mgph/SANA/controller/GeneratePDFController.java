package es.uji.ei102720mgph.SANA.controller;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import es.uji.ei102720mgph.SANA.model.NaturalArea;
import es.uji.ei102720mgph.SANA.model.NuevaReserva;
import es.uji.ei102720mgph.SANA.model.RegisteredCitizen;
import es.uji.ei102720mgph.SANA.model.ReservaDatos;
import org.apache.catalina.connector.Request;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class GeneratePDFController {

    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLD);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);

    private static final BaseColor granate = new BaseColor(203, 62, 62);
    private static final BaseColor verde = new BaseColor(105, 127, 64);
    private static final BaseColor gris = new BaseColor(215, 215, 215);


    private static final Font categoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font subcategoryFont = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, verde);
    private static final Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, verde);


    private static final String linea = "img/linea-verde.png";


    /**
     * We create a PDF document with iText using different elements to learn
     * to use this library.
     * Creamos un documento PDF con iText usando diferentes elementos para aprender
     * a usar esta librería.
     *
     * @param pdfNewFile <code>String</code>
     *                   pdf File we are going to write.
     *                   Fichero pdf en el que vamos a escribir.
     * @param qr

     */
    public void createPDF(File pdfNewFile, RegisteredCitizen registeredCitizen, NuevaReserva nuevaReserva, NaturalArea naturalArea, String qr, String[] zonas) {
        // Aquí introduciremos el código para crear el PDF.

        // Creamos el documento e indicamos el nombre del fichero.
        try {
            Document document = new Document();
            PdfWriter writer = null;
            try {
                writer = com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No such file was found to generate the PDF "
                        + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
            }
            document.open();

            // Creamos el manejador de evento de pagina, el cual agregara
            // el encabezado y pie de pagina
            //EventoPagina evento = new EventoPagina(document);
            // Indicamos que el manejador se encargara del evento END_PAGE
            //pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, evento);
            // Establecemos los margenes

            // Añadimos los metadatos del PDF
            document.addTitle("Reserva-" + nuevaReserva.getCitizenEmail());
            document.addSubject("Usando iText");
            document.addKeywords("Java, PDF, iText, SANA, HTML, CSS");
            document.addAuthor("SANA");
            document.addCreator("SANA");

            document.setMargins(70, 50, 35, 35);

            // Primera página
            // AÑADIMOS LA FRANJA GRANATE DE ARRIBA
            Chapter chapter = new Chapter(0);
            Image lineaArriba;
            try {
                lineaArriba = Image.getInstance(linea);
                lineaArriba.setAbsolutePosition(0, 822);
                chapter.add(lineaArriba);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            // AÑADIMOS EL LOGO DE LA EMPRESA

            Image image;
            try {
               image = Image.getInstance("img/LOGO_mono.png");
               image.setAbsolutePosition(58, 685);
               image.scalePercent(10,10);
               chapter.add(image);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }


            Paragraph fact = new Paragraph("Reserva", chapterFont);
            fact.setAlignment(Element.ALIGN_RIGHT);
            chapter.add(fact);

            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setAlignment(Element.ALIGN_RIGHT);
            lineSeparator.setOffset(-2);
            lineSeparator.setLineWidth(1);
            lineSeparator.setLineColor(BaseColor.BLACK);
            lineSeparator.setPercentage(20);

            // PONEMOS LA FECHA Y EL Nº DE FACTURA
            Paragraph fecha = new Paragraph("\nFecha", smallBold);
            fecha.setAlignment(Element.ALIGN_RIGHT);
            fecha.add(lineSeparator);
            chapter.add(fecha);

            Paragraph fechaInvoice = new Paragraph(nuevaReserva.getReservationDate().toString());
            fechaInvoice.setAlignment(Element.ALIGN_RIGHT);
            chapter.add(fechaInvoice);

            Paragraph hReserva = new Paragraph("\nLugar", smallBold);
            hReserva.setAlignment(Element.ALIGN_RIGHT);
            hReserva.add(lineSeparator);
            chapter.add(hReserva);

            String texto = naturalArea.getName() + "\nZonas: ";
            for (String z: zonas) {
                texto += z + ", ";
            }
            texto = texto.substring(0, texto.length()-2);
            texto += "\n" + nuevaReserva.getNumberOfPeople() + " personas";
            Paragraph begginningTime = new Paragraph(texto);
            begginningTime.setAlignment(Element.ALIGN_RIGHT);
            chapter.add(begginningTime);

            LineSeparator lineCliente = new LineSeparator();
            lineCliente.setAlignment(0);
            lineCliente.setOffset(-2);
            lineCliente.setLineWidth(1);
            lineCliente.setLineColor(BaseColor.BLACK);
            lineCliente.setPercentage(100);

            // DATOS DEL CLIENTE
            Paragraph cliente = new Paragraph("\n\nDatos Cliente", smallBold);
            cliente.setAlignment(Element.ALIGN_LEFT);
            cliente.add(lineCliente);
            cliente.setFont(paragraphFont);
            cliente.add("\n" + registeredCitizen.getName() + " " + registeredCitizen.getSurname() + "\n");
            cliente.add(registeredCitizen.getMobilePhoneNumber() + "\n");
            cliente.add(registeredCitizen.getEmail());
            chapter.add(cliente);

            // AÑADIMOS EL LOGO DE LA EMPRESA
            Image qrCode;
            try {
                qrCode = Image.getInstance("img/qrCodes/"+qr);
                qrCode.setAlignment(Element.ALIGN_LEFT);
                qrCode.scalePercent(80, 80);
                qrCode.setAbsolutePosition(180, 300);
                chapter.add(qrCode);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }



            // AÑADIMOS LA FRANJA GRANATE ABAJO IGUAL QUE ARRIBA
            try {
                lineaArriba = Image.getInstance(linea);
                lineaArriba.setAbsolutePosition(0, 0);
                chapter.add(lineaArriba);
            } catch (BadElementException ex) {
                System.out.println("Image BadElementException" + ex);
            } catch (IOException ex) {
                System.out.println("Image IOException " + ex);
            }

            document.add(chapter);


            //Añadimos los datos de la empresa
            PdfContentByte over = writer.getDirectContent();
            try {
                over.saveState();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 740);
                over.showText("Conselleria de Medioambiente");
                over.endText();


                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 720);
                over.showText("12100 Castelló de la Plana, Castelló");
                over.endText();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 710);
                over.showText("964 22 11 00");
                over.endText();

                over.beginText();
                over.setFontAndSize(paragraphFont.getBaseFont(), 9);
                over.setTextMatrix(140, 700);
                over.showText("http://www.sana.gva.es");
                over.endText();

                over.restoreState();
            } catch (Exception e) {
                e.printStackTrace();
            }

            document.close();
            System.out.println("Your PDF file has been generated!");


        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        }

    }
}
