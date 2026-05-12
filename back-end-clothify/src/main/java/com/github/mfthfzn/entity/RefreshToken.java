package com.github.mfthfzn.entity;

import jakarta.persistence.*;

@Table(name = "refresh_tokens")
@Entity
public class RefreshToken {

  @Id
  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String token;

  @OneToOne
  @MapsId
  @JoinColumn(name = "email", referencedColumnName = "email")
  private User user;

  public RefreshToken() {
  }

  public RefreshToken(String email, String token, User user) {
    this.email = email;
    this.token = token;
    this.user = user;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
