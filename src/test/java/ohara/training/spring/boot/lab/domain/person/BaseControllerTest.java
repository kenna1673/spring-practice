package ohara.training.spring.boot.lab.domain.person;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseControllerTest {
    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
