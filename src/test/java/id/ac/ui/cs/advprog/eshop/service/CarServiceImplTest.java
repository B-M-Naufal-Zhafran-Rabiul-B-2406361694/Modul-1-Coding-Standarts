package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    @Test
    void create_delegatesToRepository() {
        Car car = new Car();
        car.setCarName("Avanza");

        when(carRepository.create(any(Car.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Car created = carService.create(car);

        assertSame(car, created);
        verify(carRepository).create(car);
    }

    @Test
    void findAll_collectsCarsFromRepository() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        Car car2 = new Car();
        car2.setCarId("car-2");

        List<Car> cars = Arrays.asList(car1, car2);
        when(carRepository.findAll()).thenReturn(cars.iterator());

        List<Car> result = carService.findAll();

        assertEquals(2, result.size());
        assertSame(car1, result.get(0));
        assertSame(car2, result.get(1));
    }

    @Test
    void findById_returnsRepositoryResult() {
        Car car = new Car();
        car.setCarId("car-1");

        when(carRepository.findById("car-1")).thenReturn(car);

        Car result = carService.findById("car-1");

        assertSame(car, result);
    }

    @Test
    void update_delegatesToRepository() {
        Car car = new Car();
        car.setCarId("car-1");

        carService.update("car-1", car);

        verify(carRepository).update(eq("car-1"), eq(car));
    }

    @Test
    void delete_delegatesToRepository() {
        carService.deleteCarById("car-1");

        verify(carRepository).delete("car-1");
    }
}
