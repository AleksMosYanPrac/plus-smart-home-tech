package ru.yandex.practicum.sht.commerce.cart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.sht.commerce.cart.mapper.ShoppingCartMapper;
import ru.yandex.practicum.sht.commerce.cart.model.ShoppingCart;
import ru.yandex.practicum.sht.commerce.cart.repository.ShoppingCartRepository;
import ru.yandex.practicum.sht.commerce.cart.client.WarehouseFeignClient;
import ru.yandex.practicum.sht.commerce.dto.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
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

    @Override
    public ShoppingCartDto getUserShoppingCart(String username) throws NotAuthorizedUserException {
        log.info("Get shopping cart for User:{}", username);
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUsername(username);
                    newShoppingCart.setActive(true);
                    return repository.save(newShoppingCart);
                });
        return mapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addProductToCart(String username, Map<UUID, Long> productByQuantity)
            throws NotAuthorizedUserException {
        log.info("Add products:{} by user:{}", productByQuantity, username);
        ShoppingCartDto shoppingCartDto = getUserShoppingCart(username);
        shoppingCartDto.setProducts(productByQuantity);
        warehouseClient.postCheckProductQuantityForShoppingCart(shoppingCartDto);
        ShoppingCart shoppingCart = repository.findByUsername(username).get();
        shoppingCart.addProducts(productByQuantity);
        return mapper.toDto(repository.save(shoppingCart));
    }

    @Override
    public void deactivateCart(String username) throws NotAuthorizedUserException {
        log.info("Deactivate shopping cart user:{}", username);
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUsername(username);
                    return newShoppingCart;
                });
        shoppingCart.setActive(false);
        repository.save(shoppingCart);
    }

    @Override
    public ShoppingCartDto deleteProductFromCart(String username, Set<UUID> productList)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException {
        log.info("Delete products:{} from cart by user:{}", productList, username);
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseThrow(() -> new NoProductsInShoppingCartException("Products is absent in shopping cart"));
        if (shoppingCart.isContainProducts(productList)) {
            shoppingCart.removeProducts(productList);
            return mapper.toDto(repository.save(shoppingCart));
        } else {
            throw new NoProductsInShoppingCartException("Products is absent in shopping cart");
        }
    }

    @Override
    public ShoppingCartDto changeProductQuantityInCart(String username, ChangeProductQuantityRequest request)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException {
        log.info("Change quantity products:{} from cart by user:{}", request, username);
        ShoppingCart shoppingCart = repository.findByUsername(username)
                .orElseThrow(() -> new NoProductsInShoppingCartException("Products is absent in shopping cart"));
        if (shoppingCart.isContainProducts(Set.of(request.getProductId()))) {
            shoppingCart.updateProductQuantity(request.getProductId(), request.getNewQuantity());
            warehouseClient.postCheckProductQuantityForShoppingCart(mapper.toDto(shoppingCart));
            return mapper.toDto(repository.save(shoppingCart));
        } else {
            throw new NoProductsInShoppingCartException("Products is absent in shopping cart");
        }
    }
}