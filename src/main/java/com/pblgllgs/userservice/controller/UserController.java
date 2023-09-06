package com.pblgllgs.userservice.controller;

import com.pblgllgs.userservice.entity.User;
import com.pblgllgs.userservice.model.Bike;
import com.pblgllgs.userservice.model.Car;
import com.pblgllgs.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallbackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> findAllBikesByUserId(@PathVariable("userId") int id) {
        List<Bike> bikes = userService.findAllBikesByUserId(id);
        if (bikes == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(bikes, HttpStatus.OK);
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallbackSaveBikes")
    @PostMapping("/saveBike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable("userId") int userId, @RequestBody Bike bike) {
        return new ResponseEntity<>(userService.saveBike(userId, bike), HttpStatus.CREATED);
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> findAllCarsByUserId(@PathVariable("userId") int id) {
        List<Car> cars = userService.findAllCarsByUserId(id);
        if (cars == null) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallbackSaveCars")
    @PostMapping("/saveCar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable("userId") int userId, @RequestBody Car car) {
        return new ResponseEntity<>(userService.saveCar(userId, car), HttpStatus.CREATED);
    }

    @CircuitBreaker(name = "allCB", fallbackMethod = "fallbackGetAll")
    @GetMapping("/vehicles/{userId}")
    public ResponseEntity<Map<String, Object>> findAllVehiclesByUserId(@PathVariable("userId") int id) {
        return new ResponseEntity<>(userService.findUserAndVehicles(id), HttpStatus.OK);
    }

    private ResponseEntity<List<Car>> fallbackGetCars(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("No es posible traer los autos del usuario id: " + userId, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<List<Car>> fallbackSaveCars(@PathVariable("userId") int userId, @RequestBody Car car, RuntimeException e) {
        return new ResponseEntity("No es posible guardar el auto del usuario id: " + userId, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<List<Bike>> fallbackGetBikes(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("No es posible traer las motos del usuario id: " + userId, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<List<Bike>> fallbackSaveBikes(@PathVariable("userId") int userId, @RequestBody Bike bike, RuntimeException e) {
        return new ResponseEntity("No es posible guardar la moto del usuario id: " + userId, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<Map<String, Object>> fallbackGetAll(@PathVariable("userId") int userId, RuntimeException e) {
        return new ResponseEntity("No es posible traer la información usuario id: " + userId, HttpStatus.SERVICE_UNAVAILABLE);
    }

}
