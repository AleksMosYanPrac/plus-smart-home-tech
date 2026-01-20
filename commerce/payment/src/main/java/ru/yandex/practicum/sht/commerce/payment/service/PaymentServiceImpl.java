package ru.yandex.practicum.sht.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.sht.commerce.contract.OrderFeignClient;
import ru.yandex.practicum.sht.commerce.contract.ShoppingStoreFeignClient;
import ru.yandex.practicum.sht.commerce.dto.order.OrderDto;
import ru.yandex.practicum.sht.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.sht.commerce.dto.store.ProductDto;
import ru.yandex.practicum.sht.commerce.exception.NoOrderFoundException;
import ru.yandex.practicum.sht.commerce.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.sht.commerce.payment.mapper.PaymentMapper;
import ru.yandex.practicum.sht.commerce.payment.model.Payment;
import ru.yandex.practicum.sht.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static ru.yandex.practicum.sht.commerce.payment.model.PaymentStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements ru.yandex.practicum.sht.commerce.payment.service.PaymentService {

    private final ShoppingStoreFeignClient shoppingStoreClient;
    private final OrderFeignClient orderClient;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper mapper;
    private final TransactionTemplate transactionTemplate;

    @Value("${payment.fee.value}")
    private double FEE;

    @Override
    @Transactional
    public PaymentDto addPayment(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException {
        log.info("Add new payment for Order:{}", orderDto.getOrderId());
        Payment newPayment = new Payment();
        newPayment.setOrderId(orderDto.getOrderId());
        newPayment.setTotalPayment(orderDto.getTotalPrice());
        newPayment.setDeliveryTotal(orderDto.getDeliveryPrice());
        newPayment.setFeeTotal(orderDto.getTotalPrice().multiply(BigDecimal.valueOf(FEE)));
        newPayment.setStatus(PENDING);
        return mapper.toDto(paymentRepository.save(newPayment));

    }

    @Override
    public BigDecimal calculateTotalCost(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException {
        log.info("Calculate total price for Order:{}", orderDto.getOrderId());
        if (Objects.isNull(orderDto.getDeliveryPrice())) {
            throw new NotEnoughInfoInOrderToCalculateException("Delivery options required");
        }

        BigDecimal productCost = orderDto.getProductPrice();
        BigDecimal deliveryCost = orderDto.getDeliveryPrice();
        BigDecimal fee = productCost.multiply(BigDecimal.valueOf(FEE));

        return productCost.add(deliveryCost).add(fee);
    }

    @Override
    public BigDecimal calculateProductsCost(OrderDto orderDto) throws NotEnoughInfoInOrderToCalculateException {
        log.info("Calculate product price for Order{}:", orderDto.getOrderId());
        if (Objects.isNull(orderDto.getProducts())) {
            throw new NotEnoughInfoInOrderToCalculateException("Products is required");
        }
        BigDecimal productCost = BigDecimal.ZERO;
        for (Map.Entry<UUID, Long> entry : orderDto.getProducts().entrySet()) {
            UUID id = entry.getKey();
            Long count = entry.getValue();
            ProductDto product = shoppingStoreClient.getProductById(id);
            productCost = productCost.add(product.getPrice().multiply(BigDecimal.valueOf(count)));
        }
        return productCost;
    }

    @Override
    public void receivePayment(UUID paymentId, boolean isSuccess) throws NoOrderFoundException {
        Payment payment = transactionTemplate.execute(status -> {
            Payment updatedPayment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new NoOrderFoundException("No order for payment: " + paymentId));
            if (isSuccess) {
                updatedPayment.setStatus(SUCCESS);
            } else {
                updatedPayment.setStatus(FAILED);
            }
            return paymentRepository.save(updatedPayment);
        });
        orderClient.postOrderPayment(payment.getOrderId());
    }
}