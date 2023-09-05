package com.pblgllgs.userservice.config;

import com.pblgllgs.userservice.model.Bike;
import com.pblgllgs.userservice.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "bike-service", url = "http://localhost:8003")
@RequestMapping("/bike")
public interface BikeFeignClient {

    @GetMapping("/byUser/{userId}")
    List<Bike> findAllBikesByUserId(@PathVariable("userId") int userId);

    @PostMapping
    Bike save(@RequestBody Bike bike);
}
