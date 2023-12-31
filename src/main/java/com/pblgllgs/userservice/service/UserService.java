package com.pblgllgs.userservice.service;

import com.pblgllgs.userservice.config.BikeFeignClient;
import com.pblgllgs.userservice.config.CarFeignClient;
import com.pblgllgs.userservice.entity.User;
import com.pblgllgs.userservice.exception.UserNotFoundException;
import com.pblgllgs.userservice.model.Bike;
import com.pblgllgs.userservice.model.Car;
import com.pblgllgs.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final CarFeignClient carFeignClient;
    private final BikeFeignClient bikeFeignClient;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User findUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List findAllCarsByUserId(int userId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt.getTokenValue());
        return restTemplate
                .exchange(
                        "http://car-service/car/byUser/" + userId,
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        List.class
                )
                .getBody();
    }

    public List findAllBikesByUserId(int userId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + jwt.getTokenValue());
        return restTemplate
                .exchange(
                        "http://bike-service/bike/byUser/" + userId,
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        List.class
                ).getBody();
    }

    public Car saveCar(int userId, Car car) {
        if (this.findUserById(userId) == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        car.setUserId(userId);
        return carFeignClient.save(car);
    }

    public Bike saveBike(int userId, Bike bike) {
        if (this.findUserById(userId) == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        bike.setUserId(userId);
        return bikeFeignClient.save(bike);
    }

    public Map<String, Object> findUserAndVehicles(int userId) {
        User user = this.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        Map<String, Object> vehicles = new HashMap<>();
        vehicles.put("user", user);
        List<Bike> allBikesByUserId = bikeFeignClient.findAllBikesByUserId(userId);
        List<Car> allCarsByUserId = carFeignClient.findAllCarsByUserId(userId);
        if (allCarsByUserId.isEmpty()) {
            vehicles.put("cars", "No tiene cars");
        } else {
            vehicles.put("cars", allCarsByUserId);

        }
        if (allBikesByUserId.isEmpty()) {
            vehicles.put("bikes", "No tiene bikes");
        } else {
            vehicles.put("bikes", allBikesByUserId);

        }
        return vehicles;
    }
}
