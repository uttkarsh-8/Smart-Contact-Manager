package com.smart.smartContactManager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    @NotBlank(message = "Name field is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    @NotBlank(message = "Nick name field is required")
    @Size(min = 2, max = 50, message = "Nick name must be between 2 and 50 characters")
    private String secondName;
    @NotBlank(message = "Work field is required")
    @Size(max = 100, message = "Work description must not exceed 100 characters")
    private String work;
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email")
    private String email;
    @NotNull(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;
    private String image;
    @Column(length = 500)
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    @ManyToOne
    private User user;
}
