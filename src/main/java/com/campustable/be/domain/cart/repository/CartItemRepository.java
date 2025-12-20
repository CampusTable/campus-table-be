package com.campustable.be.domain.cart.repository;

import com.campustable.be.domain.cart.entity.Cart;
import com.campustable.be.domain.cart.entity.CartItem;
import com.campustable.be.domain.menu.entity.Menu;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  Optional<CartItem> findByCartAndMenu(Cart cart, Menu menu);


  List<CartItem> findByCart(Cart cart);
}
