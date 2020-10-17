package com.example.delivery.controller;



import com.example.delivery.model.ResultMessage;
import com.example.delivery.model.VehicleDelivery;
import com.example.delivery.repository.DeliveryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeleveryApiController {
	private final DeliveryRepository repository;
	 

    @GetMapping("/{tx}")
    public VehicleDelivery vehicleTxInfo(@PathVariable String  tx){
        VehicleDelivery vehicleDelivery = repository.findById(tx).orElse(null);
        return vehicleDelivery;
	}
	
	


}
