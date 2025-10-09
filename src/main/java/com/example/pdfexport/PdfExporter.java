package com.example.pdfexport;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Classe utilitária para gerar PDFs com iText7 (kernel + layout).
 * Versão final sem exceções desnecessárias.
 */
public class PdfExporter {

    /**
     * Gera um PDF em memória com a lista de tarefas.
     *
     * @param title título do documento
     * @param lines texto com as tarefas (uma por linha)
     * @return bytes do ficheiro PDF
     */
    public byte[] generateTodoPdf(String title, String lines) {
        // separar as tarefas por linha e limpar vazios
        java.util.List<String> items = Arrays.stream(lines.split("\\R"))
                .map(s -> new String(s.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Criar o PDF (sem try-with-resources, mas com fecho manual)
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document doc = new Document(pdf);

        doc.add(new Paragraph(title).setBold().setFontSize(18));

        if (items.isEmpty()) {
            doc.add(new Paragraph("Sem tarefas para exportar."));
        } else {
            List list = new List().setSymbolIndent(12).setListSymbol("\u2022 ");
            for (String item : items) {
                list.add(new ListItem(item));
            }
            doc.add(list);
        }

        doc.close(); // fecha o documento e grava o PDF
        return baos.toByteArray();
    }
}
