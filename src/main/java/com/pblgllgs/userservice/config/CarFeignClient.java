package com.pblgllgs.userservice.config;

import com.pblgllgs.userservice.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "car-service")
@RequestMapping("/car")
public interface CarFeignClient {

    @GetMapping("/byUser/{userId}")
    List<Car> findAllCarsByUserId(@PathVariable("userId") int userId);

    @PostMapping
    Car save(@RequestBody Car car);
}
