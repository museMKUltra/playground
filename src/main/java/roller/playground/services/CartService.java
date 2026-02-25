package roller.playground.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import roller.playground.dtos.CartDto;
import roller.playground.dtos.CartItemDto;
import roller.playground.entities.Cart;
import roller.playground.excpetions.CartNotFoundException;
import roller.playground.excpetions.ProductNotFoundException;
import roller.playground.mappers.CartMapper;
import roller.playground.repositories.CartRepository;
import roller.playground.repositories.ProductRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);

        if (cart == null) {
            throw new CartNotFoundException();
        }

        var product = productRepository.findById(productId).orElse(null);

        if (product == null) {
            throw new ProductNotFoundException();
        }

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public CartDto getCartDto(UUID cartId) {
        var cart = cartRepository.getCartWithProducts(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        return cartMapper.toDto(cart);
    }

    public CartItemDto updateCartItemQuantity(UUID cartId, Long productId, Integer quantity) {
        var cart = cartRepository.getCartWithProducts(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        var cartItem = cart.getItem(productId);
        if (cartItem == null) {
            throw new ProductNotFoundException();
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cart);

        return cartMapper.toDto(cartItem);
    }

    public void deleteCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithProducts(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void deleteCartItems(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException();
        }

        cart.clearItems();
        cartRepository.save(cart);
    }
}
