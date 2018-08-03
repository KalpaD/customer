package com.techtest.customer.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtest.customer.controllers.BaseController;
import org.junit.Ignore;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;

@Ignore
public class AbstractControllerTest extends AbstractTest {

    protected MockMvc mockMvc; // capable of simulate http interactions

    /**
     * Register only the given controller to web app context
     * @param controller    Instance of BaseController.
     */
    protected void setUp(BaseController controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }
}
