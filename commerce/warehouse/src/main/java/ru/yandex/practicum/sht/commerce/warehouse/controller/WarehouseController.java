package ru.yandex.practicum.sht.commerce.warehouse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.sht.commerce.contract.WarehouseContract;
import ru.yandex.practicum.sht.commerce.dto.AddressDto;
import ru.yandex.practicum.sht.commerce.dto.cart.ShoppingCartDto;
import ru.yandex.practicum.sht.commerce.dto.warehouse.*;
import ru.yandex.practicum.sht.commerce.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.sht.commerce.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.sht.commerce.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.sht.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController implements WarehouseContract {

    private final WarehouseService warehouseService;

    @Override
    public void putNewProduct(NewProductInWarehouseRequest request)
            throws SpecifiedProductAlreadyInWarehouseException {
        warehouseService.addNewProduct(request);
    }

    @Override
    public void postTransferProductsToDelivery(ShippedToDeliveryRequest request) {
        warehouseService.transferProductsToDelivery(request);
    }

    @Override
    public void postReturnProductToWarehouse(Map<UUID, Long> products) {
        warehouseService.returnProductsToWarehouse(products);
    }

    @Override
    public BookedProductsDto postCheckProductQuantityForShoppingCart(ShoppingCartDto cart)
            throws ProductInShoppingCartLowQuantityInWarehouse {
        return warehouseService.checkProductQuantity(cart);
    }

    @Override
    public BookedProductsDto postAssemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request)
            throws ProductInShoppingCartLowQuantityInWarehouse {
        return warehouseService.assemblyProductsForOrderFromShoppingCart(request);
    }

    @Override
    public void postAddProductToWarehouse(AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException {
        warehouseService.increaseProductQuantity(request);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return warehouseService.getWarehouseAddress();
    }
}