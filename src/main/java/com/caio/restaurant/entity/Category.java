package com.caio.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is required")
  @Size(max = 50, message = "Name can have at most 50 characters")
  @Column(nullable = false, length = 50)
  private String name;

  @Size(max = 255, message = "Description can have at most 255 characters")
  @Column(length = 255)
  private String description;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Date createdAt;
}
