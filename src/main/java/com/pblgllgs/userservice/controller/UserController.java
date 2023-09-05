package com.pblgllgs.userservice.controller;

import com.pblgllgs.userservice.entity.User;
import com.pblgllgs.userservice.model.Bike;
import com.pblgllgs.userservice.model.Car;
import com.pblgllgs.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
        if (user == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        List<User> users = userService.getAll();
        if (users == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> findAllBikesByUserId(@PathVariable("userId") int id) {
        List<Bike> bikes = userService.findAllBikesByUserId(id);
        if (bikes == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(bikes, HttpStatus.OK);
    }

    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> findAllCarsByUserId(@PathVariable("userId") int id) {
        List<Car> cars = userService.findAllCarsByUserId(id);
        if (cars == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @PostMapping("/saveCar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId,@RequestBody Car car) {
        return new ResponseEntity<>(userService.saveCar(userId,car), HttpStatus.CREATED);
    }

    @PostMapping("/saveBike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId,@RequestBody Bike bike) {
        return new ResponseEntity<>(userService.saveBike(userId,bike), HttpStatus.CREATED);
    }

    @GetMapping("/vehicles/{userId}")
    public ResponseEntity<Map<String,Object>> findAllVehiclesByUserId(@PathVariable("userId") int id) {
        return new ResponseEntity<>(userService.findUserAndVehicles(id), HttpStatus.OK);
    }
}
