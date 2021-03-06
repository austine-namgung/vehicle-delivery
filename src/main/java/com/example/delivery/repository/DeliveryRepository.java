package com.example.delivery.repository;

import com.example.delivery.model.VehicleDelivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<VehicleDelivery, String> {
    
}
