package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;

public interface CartService {

        CartDto addItemToCart(String userId, AddItemToCartRequest request);

        void removeItemFromCart(String userId, int carItem);

        void clearCart(String userId);

        CartDto getCartByUser(String userId);

}
