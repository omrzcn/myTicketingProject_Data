package com.cydeo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,updatable = false)
    private LocalDateTime insertDateTime;

    @Column(nullable = false,updatable = false)
    private Long insertUserId;

    @Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;

    @Column(nullable = false)
    private Long lastUpdateUserId;

    private Boolean isDeleted = false;


    // we created this method to see below informations on the table.
    // this method needs to be executed when we create new user
    // thats why we have a annotation for this
    @PrePersist
    private void onPrePersist(){
        this.insertDateTime = LocalDateTime.now();
        this.lastUpdateDateTime =LocalDateTime.now();
        this.insertUserId = 1L; // this information is about SECURITY . we'll learn it.
        this.lastUpdateUserId=1L; // same eith above.
    }

    // this is same methodology for update. we'll see this info.s on the table.
    // this method needs to be executed when we update the objects.
    @PreUpdate
    private void onPreUpdate(){

        this.lastUpdateDateTime = LocalDateTime.now();
        this.lastUpdateUserId=1L;
    }

}
