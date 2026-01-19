package ru.yandex.practicum.sht.commerce.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.sht.commerce.dto.store.ProductCategory;
import ru.yandex.practicum.sht.commerce.dto.store.ProductDto;
import ru.yandex.practicum.sht.commerce.dto.store.ProductState;
import ru.yandex.practicum.sht.commerce.dto.store.QuantityState;
import ru.yandex.practicum.sht.commerce.exception.ProductNotFoundException;
import ru.yandex.practicum.sht.commerce.store.mapper.ProductMapper;
import ru.yandex.practicum.sht.commerce.store.model.Product;
import ru.yandex.practicum.sht.commerce.store.repository.ProductRepository;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Page<ProductDto> getProductList(ProductCategory category, Pageable pageable) {
        log.info("Get product list by Category:{}", category);
        return productRepository.findAllByProductCategory(category, pageable)
                .map(mapper::toDto);
    }

    @Override
    public ProductDto addNewProduct(ProductDto productDto) {
        log.info("Add new product ID:{}", productDto.getProductId());
        return transactionTemplate.execute(status -> {
                    return mapper.toDto(productRepository.save(mapper.toProduct(productDto)));
                });
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) throws ProductNotFoundException {
        log.info("Update product ID:{}", productDto.getProductId());
        return transactionTemplate.execute(status -> {
            Product product = productRepository.findById(productDto.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product is absent at shopping store"));
            product.setProductName(productDto.getProductName());
            product.setDescription(productDto.getDescription());
            product.setImageSrc(productDto.getImageSrc());
            product.setProductCategory(productDto.getProductCategory());
            product.setPrice(productDto.getPrice());
            return mapper.toDto(productRepository.save(product));
        });
    }

    @Override
    public boolean removeProduct(UUID productId) throws ProductNotFoundException {
        log.info("Deactivate product ID:{}", productId);
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product is absent at shopping store"));
            product.setProductState(ProductState.DEACTIVATE);
            productRepository.save(product);
            return true;
        }));
    }

    @Override
    public boolean updateProductQuantityState(UUID productId, QuantityState quantityState)
            throws ProductNotFoundException {
        log.info("Update quantity product ID:{}", productId);
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product is absent at shopping store"));
            product.setQuantityState(quantityState);
            productRepository.save(product);
            return true;
        }));
    }

    @Override
    public ProductDto getProductById(UUID productId) throws ProductNotFoundException {
        log.info("Get product ID:{}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product is absent at shopping store"));
        return mapper.toDto(product);
    }
}