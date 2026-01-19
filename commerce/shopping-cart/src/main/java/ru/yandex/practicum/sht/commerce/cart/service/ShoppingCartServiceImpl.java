package ru.yandex.practicum.sht.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yandex.practicum.sht.commerce.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.sht.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.sht.commerce.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.sht.commerce.contract.WarehouseFeignClient;
import ru.yandex.practicum.sht.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.sht.commerce.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.sht.commerce.exception.NotAuthorizedUserException;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final WarehouseFeignClient warehouseClient;
    private final ShoppingCartRepository repository;
    private final ShoppingCartMapper mapper;
    private final TransactionTemplate transactionTemplate;

    @Override
    public ShoppingCartDto getUserShoppingCart(String username) throws NotAuthorizedUserException {
        log.info("Get shopping cart for User:{}", username);
        return transactionTemplate.execute(status -> {
            ShoppingCart shoppingCart = repository.findByUsername(username)
                    .orElseGet(() -> {
                        ShoppingCart newShoppingCart = new ShoppingCart();
                        newShoppingCart.setUsername(username);
                        newShoppingCart.setActive(true);
                        return repository.save(newShoppingCart);
                    });
            return mapper.toDto(shoppingCart);
        });
    }

    @Override
    public ShoppingCartDto addProductToCart(String username, Map<UUID, Long> productByQuantity)
            throws NotAuthorizedUserException {
        log.info("Add products:{} by user:{}", productByQuantity, username);
        ShoppingCartDto shoppingCartDto = getUserShoppingCart(username);
        log.debug("Send request to warehouse for cart:{}", shoppingCartDto);
        shoppingCartDto.setProducts(productByQuantity);
        BookedProductsDto bookedProductsDto = warehouseClient.postCheckProductQuantityForShoppingCart(shoppingCartDto);
        log.debug("Get response from warehouse:{}", bookedProductsDto);
        return transactionTemplate.execute(status -> {
            ShoppingCart shoppingCart = repository.findByUsername(username).get();
            shoppingCart.addProducts(productByQuantity);
            return mapper.toDto(repository.save(shoppingCart));
        });
    }

    @Override
    public void deactivateCart(String username) throws NotAuthorizedUserException {
        log.info("Deactivate shopping cart user:{}", username);
        transactionTemplate.execute(status -> {
            ShoppingCart shoppingCart = repository.findByUsername(username)
                    .orElseGet(() -> {
                        ShoppingCart newShoppingCart = new ShoppingCart();
                        newShoppingCart.setUsername(username);
                        return newShoppingCart;
                    });
            shoppingCart.setActive(false);
            return repository.save(shoppingCart);
        });
    }

    @Override
    public ShoppingCartDto deleteProductFromCart(String username, Set<UUID> productList)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException {
        log.info("Delete products:{} from cart by user:{}", productList, username);
        return transactionTemplate.execute(status -> {
            ShoppingCart shoppingCart = repository.findByUsername(username)
                    .orElseThrow(() -> new NoProductsInShoppingCartException("Products is absent in shopping cart"));
            if (shoppingCart.isContainProducts(productList)) {
                shoppingCart.removeProducts(productList);
                return mapper.toDto(repository.save(shoppingCart));
            } else {
                throw new NoProductsInShoppingCartException("Products is absent in shopping cart");
            }
        });
    }

    @Override
    public ShoppingCartDto changeProductQuantityInCart(String username, ChangeProductQuantityRequest request)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException {
        log.info("Change quantity products:{} from cart by user:{}", request, username);
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseThrow(() -> new NoProductsInShoppingCartException("Products is absent in shopping cart"));
        warehouseClient.postCheckProductQuantityForShoppingCart(mapper.toDto(shoppingCart));
        if (shoppingCart.isContainProducts(Set.of(request.getProductId()))) {
            return transactionTemplate.execute(status -> {
                shoppingCart.updateProductQuantity(request.getProductId(), request.getNewQuantity());
                return mapper.toDto(repository.save(shoppingCart));
            });
        } else {
            throw new NoProductsInShoppingCartException("Products is absent in shopping cart");
        }
    }
}