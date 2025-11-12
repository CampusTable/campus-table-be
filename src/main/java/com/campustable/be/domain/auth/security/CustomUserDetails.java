package com.campustable.be.domain.auth.security;

import com.campustable.be.domain.User.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

  private final User user;

  public CustomUserDetails(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      String role = user.getRole();
      return List.of(new SimpleGrantedAuthority("Role_"+role));
  }

  @Override
  public String getUsername() {
      return user.getStudentNumber();
  }

  @Override
  public String getPassword() {
      return null;
  }

}
