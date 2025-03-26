package com.example.elms.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "relationship_users")
public class RelationshipUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "id_employee")
    private Long idEmployee;

    @Column(nullable = false, name = "id_manager")
    private Long idManager;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public Long getIdManager() {
        return idManager;
    }

    public void setIdManager(Long idManager) {
        this.idManager = idManager;
    }
}
