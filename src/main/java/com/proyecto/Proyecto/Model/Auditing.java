package com.proyecto.Proyecto.Model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditing {
    @CreatedDate
    @Column(nullable = false,updatable = false)
    private Date creado;
    @LastModifiedDate
    @Column(insertable = false)
    private Date actualizado;
    
}
