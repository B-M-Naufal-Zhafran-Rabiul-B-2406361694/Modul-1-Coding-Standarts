package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void create_setsIdWhenNull() {
        Car car = new Car();
        car.setCarName("Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(5);

        Car created = carRepository.create(car);

        assertSame(car, created);
        assertNotNull(car.getCarId());
        assertFalse(car.getCarId().isEmpty());

        Iterator<Car> iterator = carRepository.findAll();
        assertTrue(iterator.hasNext());
        assertSame(car, iterator.next());
    }

    @Test
    void create_preservesExistingId() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Avanza");

        carRepository.create(car);

        assertEquals("car-1", car.getCarId());
    }

    @Test
    void findAll_emptyReturnsNoElements() {
        Iterator<Car> iterator = carRepository.findAll();
        assertFalse(iterator.hasNext());
    }

    @Test
    void findById_returnsCarWhenFound() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Avanza");
        carRepository.create(car);

        Car found = carRepository.findById("car-1");

        assertSame(car, found);
    }

    @Test
    void findById_returnsNullWhenMissing() {
        Car found = carRepository.findById("missing-id");
        assertNull(found);
    }

    @Test
    void update_updatesExistingCar() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Old");
        car.setCarColor("White");
        car.setCarQuantity(1);
        carRepository.create(car);

        Car update = new Car();
        update.setCarName("New");
        update.setCarColor("Black");
        update.setCarQuantity(10);

        Car updated = carRepository.update("car-1", update);

        assertSame(car, updated);
        assertEquals("New", car.getCarName());
        assertEquals("Black", car.getCarColor());
        assertEquals(10, car.getCarQuantity());
    }

    @Test
    void update_returnsNullWhenMissing() {
        Car update = new Car();
        update.setCarName("New");

        Car updated = carRepository.update("missing-id", update);

        assertNull(updated);
    }

    @Test
    void delete_removesMatchingCar() {
        Car car1 = new Car();
        car1.setCarId("car-1");
        car1.setCarName("First");
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("car-2");
        car2.setCarName("Second");
        carRepository.create(car2);

        carRepository.delete("car-1");

        assertNull(carRepository.findById("car-1"));
        assertSame(car2, carRepository.findById("car-2"));
    }

    @Test
    void delete_missingIdKeepsData() {
        Car car = new Car();
        car.setCarId("car-1");
        car.setCarName("Avanza");
        carRepository.create(car);

        carRepository.delete("missing-id");

        assertSame(car, carRepository.findById("car-1"));
    }
}
