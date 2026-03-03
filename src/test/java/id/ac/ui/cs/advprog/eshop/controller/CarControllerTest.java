package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarReadService;
import id.ac.ui.cs.advprog.eshop.service.CarWriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(CarController.class)
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarReadService carReadService;

    @MockBean
    private CarWriteService carWriteService;

    @Test
    void createCarPage_returnsViewAndModel() throws Exception {
        mockMvc.perform(get("/car/createCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("createCar"))
                .andExpect(model().attributeExists("car"));
    }

    @Test
    void createCarPost_redirectsToList() throws Exception {
        Car created = new Car();
        created.setCarId("car-1");
        when(carWriteService.create(any(Car.class))).thenReturn(created);

        mockMvc.perform(post("/car/createCar")
                        .param("carName", "Avanza")
                        .param("carColor", "Black")
                        .param("carQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carWriteService).create(any(Car.class));
    }

    @Test
    void carListPage_returnsViewAndModel() throws Exception {
        Car car1 = new Car();
        car1.setCarId("car-1");
        Car car2 = new Car();
        car2.setCarId("car-2");

        List<Car> cars = Arrays.asList(car1, car2);
        when(carReadService.findAll()).thenReturn(cars);

        mockMvc.perform(get("/car/listCar"))
                .andExpect(status().isOk())
                .andExpect(view().name("carList"))
                .andExpect(model().attribute("cars", sameInstance(cars)));
    }

    @Test
    void editCarPage_returnsViewAndModel() throws Exception {
        Car car = new Car();
        car.setCarId("car-1");
        when(carReadService.findById("car-1")).thenReturn(car);

        mockMvc.perform(get("/car/editCar/car-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("editCar"))
                .andExpect(model().attribute("car", sameInstance(car)));
    }

    @Test
    void editCarPost_redirectsToList() throws Exception {
        mockMvc.perform(post("/car/editCar")
                        .param("carId", "car-1")
                        .param("carName", "Avanza")
                        .param("carColor", "Black")
                        .param("carQuantity", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carWriteService).update(eq("car-1"), any(Car.class));
    }

    @Test
    void deleteCar_redirectsToList() throws Exception {
        mockMvc.perform(post("/car/deleteCar")
                        .param("carId", "car-1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("listCar"));

        verify(carWriteService).deleteCarById("car-1");
    }
}
