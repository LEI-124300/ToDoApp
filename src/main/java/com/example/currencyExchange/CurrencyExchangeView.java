package com.example.currencyExchange;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("currencyExchange")
public class CurrencyExchangeView extends VerticalLayout {

    private final CurrencyExchangeService service;

    @Autowired
    public CurrencyExchangeView(CurrencyExchangeService service) {
        this.service = service;

        TextField fromCurrency = new TextField("From Currency (e.g. USD)");
        TextField toCurrency = new TextField("To Currency (e.g. EUR)");
        NumberField amount = new NumberField("Amount");

        Button exchangeButton = new Button("Exchange", event -> {
            Double result = service.exchange(
                    fromCurrency.getValue(),
                    toCurrency.getValue(),
                    amount.getValue()
            );
            if (result != null) {
                Notification.show(amount.getValue() + " " + fromCurrency.getValue()
                        + " = " + result + " " + toCurrency.getValue());
            } else {
                Notification.show("Error converting currencies.");
            }
        });

        add(fromCurrency, toCurrency, amount, exchangeButton);
    }
}
