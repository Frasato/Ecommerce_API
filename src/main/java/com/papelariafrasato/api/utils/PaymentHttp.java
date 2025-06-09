package com.papelariafrasato.api.utils;

import com.papelariafrasato.api.dtos.ResponsePaymentCardDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Component
public class PaymentHttp {

    @Value("${api.payment.url}")
    private String apiUrl;

    private final WebClient webClient;

    public PaymentHttp() {
        this.webClient = WebClient.builder()
                .defaultHeader("Content-Type", "text/xml; charset=UTF-8")
                .build();
    }

    public String sendXmlRequest(String xmlRequest) {
        try {
            return webClient.post()
                    .uri(apiUrl)
                    .bodyValue(xmlRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Error on send request! ", e);
        }
    }

    public ResponsePaymentCardDto parseXmlResponse(String xmlResponse) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlResponse)));

            String transectionalId = getElementValue(doc, "transactionID");
            String orderId = getElementValue(doc, "orderID");
            String referenceNum = getElementValue(doc, "referenceNum");
            String authCode = getElementValue(doc, "authCode");

            String status;
            String message;

            String responseCode = getElementValue(doc, "responseCode");
            String responseMessage = getElementValue(doc, "responseMessage");

            if ("0".equals(responseCode)) {
                status = "APPROVED";
                message = "Transação aprovada: " + responseMessage;
            } else {
                status = "DECLINED";
                message = responseMessage;
            }

            String processorName = getElementValue(doc, "processorName");
            String creditCardLast4 = getElementValue(doc, "creditCardLast4");

            return new ResponsePaymentCardDto(status, transectionalId, orderId, referenceNum, authCode, message, processorName, creditCardLast4);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getElementValue(Document doc, String tagName) {
        NodeList nodeList = doc.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node != null ? node.getTextContent() : "";
        }
        return "";
    }

}
