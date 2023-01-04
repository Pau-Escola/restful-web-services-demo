package com.example.restfulwebservices.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserResource
{
    private UserDaoService service;

    public UserResource(UserDaoService service)
    {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers()
    {
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveOneUser(@PathVariable Integer id)
    {
        User user = service.findById(id);

        if(user == null)
            throw new UserNotFoundException("Id: "+ id + " doesn't exist");
        return user;
    }

    @PostMapping ("/users")
    public ResponseEntity<User> createUser( @Valid @RequestBody User user)
    {
        var savedUser = service.save(user);

        //Aquesta part es per retornar la ubicacio de la entity que acabem dinstanciar
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").
                buildAndExpand(savedUser.getId()).
                toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteOneUser(@PathVariable Integer id)
    {
       service.deleteById(id);
    }

}
