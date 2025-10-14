package com.example.pdfexport;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Janela/rota Vaadin para exportar tarefas em PDF.
 * Acede em: http://localhost:8080/pdf-export
 */
@PageTitle("Exportação em PDF")
@Route("pdf-export") // podes trocar para: @Route(value = "pdf-export", layout = MainLayout.class)
public class PdfExportView extends VerticalLayout {

    private final PdfExporter exporter = new PdfExporter();

    public PdfExportView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Exportar To-Do para PDF");

        TextArea tasks = new TextArea("Tarefas (uma por linha)");
        tasks.setPlaceholder("Exemplo:\nComprar leite\nEnviar relatório ao cliente\nEstudar ES – Sprint");
        tasks.setWidthFull();
        tasks.setMinHeight("220px");

        Button generate = new Button("Gerar PDF");
        generate.addClickListener(e -> {
            String content = String.valueOf(tasks.getValue());
            byte[] pdfBytes = exporter.generateTodoPdf("Lista de Tarefas", content);

            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
            String filename = "todo-list-" + ts + ".pdf";

            // criar recurso PDF
            StreamResource resource = new StreamResource(filename,
                    () -> new ByteArrayInputStream(pdfBytes));
            resource.setContentType("application/pdf");

            // criar link de download visível
            Anchor download = new Anchor(resource, "Clique aqui para descarregar o PDF");
            download.getElement().setAttribute("download", true);
            download.getStyle().set("margin-top", "10px");

            // adicionar o link ao layout
            add(download);

            Notification.show("PDF gerado com sucesso. Clique no link para descarregar.",
                    4000, Notification.Position.MIDDLE);
        });

        Button goHome = new Button("Voltar à Home", evt -> UI.getCurrent().navigate(""));

        add(title, tasks, generate, goHome);
    }
}
