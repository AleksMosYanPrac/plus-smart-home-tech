package ru.yandex.practicum.sht.commerce.payment.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.sht.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.payment.model.Payment;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentDto toDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setTotalPayment(payment.getTotalPayment());
        paymentDto.setDeliveryTotal(payment.getDeliveryTotal());
        paymentDto.setFeeTotal(payment.getFeeTotal());
        return paymentDto;
    }
}