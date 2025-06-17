package com.papelariafrasato.api.utils;

import com.papelariafrasato.api.dtos.RequestPaymentCardDto;
import com.papelariafrasato.api.exceptions.OrderNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Address;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.OrderRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BuildXml {

    @Value("${api.payment.merchant.id}")
    private String merchantId;
    @Value("${api.payment.merchant.key}")
    private String merchantKey;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    public String build(RequestPaymentCardDto paymentCardDto){

        User user = userRepository.findById(paymentCardDto.userId())
                .orElseThrow(() -> new UserNotFoundException(paymentCardDto.userId()));
        Address address = user.getAddress();
        Order order = orderRepository.findById(paymentCardDto.orderId())
                .orElseThrow(() -> new OrderNotFoundException(paymentCardDto.orderId()));

        String street = address.getStreet() + " " + address.getNumber();
        BigDecimal price = BigDecimal.valueOf(order.getTotalPrice() / 100);

        return String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <transaction-request>
                    <version>3.1.1.15</version>
                    <verification>
                        <merchantId>%s</merchantId>
                        <merchantKey>%s</merchantKey>
                    </verification>
                    <order>
                        <sale>
                            <processorID>1</processorID>
                            <referenceNum>%s</referenceNum>
                            <fraudCheck>N</fraudCheck>
                            <ipAddress>192.168.0.10</ipAddress>
                            <customerIdExt>%s</customerIdExt>
                            <billing>
                                <name>%s</name>
                                <address>%s</address>
                                <district>%s</district>
                                <city>%s</city>
                                <state>%s</state>
                                <postalcode>%s</postalcode>
                                <country>BR</country>
                                <phone>%s</phone>
                                <email>%s</email>
                            </billing>
                            <shipping>
                                <name>%s</name>
                                <address>%s</address>
                                <district>%s</district>
                                <city>%s</city>
                                <state>%s</state>
                                <postalcode>%s</postalcode>
                                <country>BR</country>
                                <phone>%s</phone>
                                <email>%s</email>
                            </shipping>
                            <transactionDetail>
                                <payType>
                                    <creditCard>
                                        <number>%s</number>
                                        <expMonth>%s</expMonth>
                                        <expYear>%s</expYear>
                                        <cvvNumber>%s</cvvNumber>
                                    </creditCard>
                                </payType>
                            </transactionDetail>
                            <payment>
                                <chargeTotal>%.2f</chargeTotal>
                                <currencyCode>BRL</currencyCode>
                                <creditInstallment>
                                    <numberOfInstallments>%d</numberOfInstallments>
                                    <chargeInterest>N</chargeInterest>
                                </creditInstallment>
                            </payment>
                        </sale>
                    </order>
                </transaction-request>
                """,
                merchantId, merchantKey, paymentCardDto.orderId(), user.getCpf(),
                paymentCardDto.cardName(), street, address.getDistrict(),
                address.getCity(), address.getCountryState(), address.getCEP(), user.getPhone(),
                user.getEmail(),
                paymentCardDto.cardName(), street, address.getDistrict(),
                address.getCity(), address.getCountryState(), address.getCEP(),
                user.getPhone(), user.getEmail(),
                paymentCardDto.cardNumber(), paymentCardDto.expirationMonth(),
                paymentCardDto.expirationYear(), paymentCardDto.cvv(),
                price, paymentCardDto.installments()
        );
    }

}
