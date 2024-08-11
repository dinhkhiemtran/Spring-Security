package com.khiemtran.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@EqualsAndHashCode
public abstract class AbstractEntity<T extends Serializable> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private T id;
  @CreationTimestamp
  @Column(name = "create_at", nullable = false, updatable = false)
  private Date createdAt;
  @UpdateTimestamp
  @Column(name = "update_at", nullable = false)
  private Date updatedAt;
}
