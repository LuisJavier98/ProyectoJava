package com.proyecto.Proyecto.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Table(name = "roles")
@Builder
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false,unique = true)
    private String rol;
    @ManyToMany(mappedBy = "roles",cascade = CascadeType.ALL)
    private List<Usuario> usuarios;
}
