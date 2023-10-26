package com.larissafernandes.tarefas.todolist.user.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity(name = "tb_users")
@Data
public class UserModel {

    private String username;
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(unique = true)
    private UUID id;
    private String name;
    private String password;
    @CreationTimestamp
    private LocalDateTime createdAt;

}

