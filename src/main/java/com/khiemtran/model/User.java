package com.khiemtran.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @Column(name = "user_name")
  private String username;
  @Column(name = "password")
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
