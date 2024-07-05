package com.khiemtran.security.model;

import com.khiemtran.security.dto.request.UserRequest;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;
  @Column(name = "user_name", unique = true)
  private String username;
  @Column(name = "email", unique = true)
  private String email;
  @Column(name = "password")
  private String password;
  @Column(name = "zip_code")
  private String zipCode;
  @Column(name = "city")
  private String city;

  public User() {
  }

  public User(UserRequest userRequest) {
    this.username = userRequest.username();
    this.email = userRequest.email();
    this.password = userRequest.password();
    this.zipCode = userRequest.zipCode();
    this.city = userRequest.city();
  }

  public Long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }
}
