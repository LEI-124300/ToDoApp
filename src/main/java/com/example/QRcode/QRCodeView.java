package com.example.QRcode;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Route("qrcode")
public class QRCodeView extends VerticalLayout {

    public QRCodeView() {
        setSpacing(true);
        setAlignItems(Alignment.CENTER);

        Button generateButton = new Button("Gerar QR Code desta página");
        Image qrImage = new Image();
        qrImage.setVisible(false);

        generateButton.addClickListener(e -> {
            getUI().ifPresent(ui -> {
                ui.getPage().fetchCurrentURL(currentUrl -> {
                    try {
                        // Gera o QR Code do link atual
                        BufferedImage qrCode = QRCodeGenerator.generateQRCode(currentUrl.toString(), 250, 250);

                        // Converte imagem em Base64
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(qrCode, "png", os);
                        String base64 = Base64.getEncoder().encodeToString(os.toByteArray());

                        // Mostra o QR Code no ecrã
                        qrImage.setSrc("data:image/png;base64," + base64);
                        qrImage.setVisible(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            });
        });

        add(generateButton, qrImage);
    }
}