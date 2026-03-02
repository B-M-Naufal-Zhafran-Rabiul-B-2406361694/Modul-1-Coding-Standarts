package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class EshopApplicationMainTest {

    @Test
    void main_runsSpringApplication() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(eq(EshopApplication.class), any(String[].class)))
                    .thenReturn(null);

            EshopApplication.main(new String[]{});

            mocked.verify(() -> SpringApplication.run(eq(EshopApplication.class), any(String[].class)));
        }
    }
}
