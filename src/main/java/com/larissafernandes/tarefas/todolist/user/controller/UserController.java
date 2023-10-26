package com.larissafernandes.tarefas.todolist.user.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.larissafernandes.tarefas.todolist.user.model.UserModel;
import com.larissafernandes.tarefas.todolist.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserModel> userModels() {
        return userRepository.findAll();
    }

    @PostMapping
    public ResponseEntity create(@RequestBody UserModel userModel) {

        var existingUser = this.userRepository.findByUsername(userModel.getUsername());

        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("USUÁRIO JÁ EXISTE");
        }
        var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashed);

        var userCread = this.userRepository.save(userModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCread);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserModel> atualizar(@PathVariable UUID id, @RequestBody UserModel userModel) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userModel.getPassword() != null) {
            var passwordHashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            userModel.setPassword(passwordHashed);
        }

        var userUpdated = userRepository.save(userModel);

        return ResponseEntity.ok().body(userUpdated);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<UserModel> deletar(@PathVariable UUID id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
