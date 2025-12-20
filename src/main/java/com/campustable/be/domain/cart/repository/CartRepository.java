package com.campustable.be.domain.cart.repository;

import com.campustable.be.domain.cart.entity.Cart;
import com.campustable.be.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByUser(User user);


}
