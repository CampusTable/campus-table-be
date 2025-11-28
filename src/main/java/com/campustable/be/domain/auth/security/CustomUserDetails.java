package com.campustable.be.domain.auth.security;

import com.campustable.be.domain.user.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails {

  private final User user;

  /**
   * Create a CustomUserDetails that exposes the given User's identity and role to Spring Security.
   *
   * @param user the User whose ID and role back this UserDetails instance
   */
  public CustomUserDetails(User user) {
    this.user = user;
  }

  /**
   * Provides the granted authorities for the wrapped user.
   *
   * <p>If the user's role is null or blank, returns an empty collection. Otherwise
   * returns a collection containing a single {@link SimpleGrantedAuthority} whose
   * authority string is the user's role prefixed with "ROLE_".
   *
   * @return a collection containing one `SimpleGrantedAuthority` with "ROLE_" + user role, or an empty collection if the role is null or blank
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
      String role = user.getRole();
      if (role == null || role.isBlank()) {
        return List.of();
      }
      return List.of(new SimpleGrantedAuthority("ROLE_"+role));
  }

  /**
   * Retrieve the user's unique identifier as a string.
   *
   * @return the user's ID converted to a String
   */
  @Override
  public String getUsername() {
      return user.getUserId().toString();
  }

  /**
   * Indicates that no password is associated with this user.
   *
   * @return {@code null} to indicate no password is available
   */
  @Override
  public String getPassword() {
      return null;
  }

}