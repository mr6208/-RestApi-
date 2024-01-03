package mr6208.restapi.demoinflearnrestapi.events;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.regex.Matcher;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext ctx;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("정우혁")
                .description("골격근량 50")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 1, 2, 22, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 1, 3, 22, 2))
                .beginEventDateTime(LocalDateTime.of(2024, 1, 2, 22, 2))
                .endEventDateTime(LocalDateTime.of(2024, 1, 3, 22, 2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("우혁이집 본가")
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string("Content-Type", "application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
        ;

    }

    @Test
    public void badRequest() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("정우혁")
                .description("골격근량 50")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 1, 2, 22, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 1, 3, 22, 2))
                .beginEventDateTime(LocalDateTime.of(2024, 1, 2, 22, 2))
                .endEventDateTime(LocalDateTime.of(2024, 1, 3, 22, 2))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("우혁이집 본가")
                .free(true)
                .offline(false)
                .build();

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("정우혁")
                .description("골격근량 50")
                .beginEnrollmentDateTime(LocalDateTime.of(2024, 1, 23, 22, 2))
                .closeEnrollmentDateTime(LocalDateTime.of(2024, 1, 22, 22, 2))
                .beginEventDateTime(LocalDateTime.of(2024, 1, 2, 23, 2))
                .endEventDateTime(LocalDateTime.of(2024, 1, 3, 22, 2))
                .basePrice(1000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("우혁이집 본가").build();

        this.mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(eventDto))
                )
                .andExpect(status().isBadRequest());

    }

}
