package com.khiemtran.security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "username")
  private String userName;
  @Column(name = "password")
  @Size(max = 50)
  private String password;
  @NotBlank
  @Size(max = 40)
  @Email
  @Column(name = "email", unique = true)
  private String email;
  @NotBlank
  @Size(max = 4)
  @Column(name = "zip_code")
  private String zipCode;
  private String city;

  public User(String userName, String password, String email, String zipCode, String city) {
    this.userName = userName;
    this.password = password;
    this.email = email;
    this.zipCode = zipCode;
    this.city = city;
  }
}
