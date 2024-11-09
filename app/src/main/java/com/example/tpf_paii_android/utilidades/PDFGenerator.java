package com.example.tpf_paii_android.utilidades;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.properties.TextAlignment;


import java.io.File;
import java.io.IOException;

public class PDFGenerator {

    private Context context;

    public PDFGenerator(Context context) {
        this.context = context;
    }

    public void crearCertificadoPDF(String nombreCurso, String nombreUsuario, String fechaFinalizacion) {
        try {

            File path = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Certificados");
            if (!path.exists()) {
                path.mkdirs();
            }

            // crep pdf
            File file = new File(path, "Certificado_" + nombreCurso + ".pdf");
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);

            Document document = new Document(pdf, PageSize.A4);

            document.add(new Paragraph("Certificado de Finalización")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(18));

            document.add(new Paragraph("Curso: " + nombreCurso)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));

            document.add(new Paragraph("Estudiante: " + nombreUsuario)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));

            document.add(new Paragraph("Fecha de Finalización: " + fechaFinalizacion)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14));

            document.close();

            Toast.makeText(context, "Certificado generado en: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al generar el certificado", Toast.LENGTH_SHORT).show();
        }
    }
}
