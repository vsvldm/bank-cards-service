package com.bank.cards.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "roles")
@Getter
@NoArgsConstructor
public class RoleJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Builder
    private RoleJpaEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleJpaEntity that = (RoleJpaEntity) o;
        if (id == null || that.id == null) return super.equals(o);
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {       
        return getClass().hashCode(); 
    }

    @Override
    public String toString() {
        return "RoleJpaEntity{id=" + id + ", name='" + name + "'}";
    }
}