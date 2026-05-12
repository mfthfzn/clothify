package com.github.mfthfzn.entity;

import com.github.mfthfzn.enums.UserRole;
import jakarta.persistence.*;

@Entity
@Table(
        name = "users"
)
public class User {

  @Id
  @Column (nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  private String name;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @OneToOne(mappedBy = "user")
  private RefreshToken refreshToken;

  public User(String email, String password, String name, UserRole role, RefreshToken refreshToken) {
    this.email = email;
    this.password = password;
    this.name = name;
    this.role = role;
    this.refreshToken = refreshToken;
  }

  public User() {
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public RefreshToken getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(RefreshToken refreshToken) {
    this.refreshToken = refreshToken;
  }
}
