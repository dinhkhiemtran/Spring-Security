package com.khiemtran.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractEntity<Long> {
  @Column(name = "user_name")
  private String username;
  @Column(name = "password")
  private String password;
  @NotBlank
  @Email
  @Column(name = "email", unique = true)
  private String email;
  @NotBlank
  @Column(name = "zip_code")
  private String zipCode;
  @Column(name = "city")
  private String city;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;

  public User(String username, String password, String email, String zipCode, String city) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.zipCode = zipCode;
    this.city = city;
  }

  public User(String username, String password, String email, String zipCode, String city, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.zipCode = zipCode;
    this.city = city;
    this.roles = roles;
  }
}
