package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;

public interface CarWriteService {
    Car create(Car car);
    void update(String carId, Car car);
    void deleteCarById(String carId);
}
