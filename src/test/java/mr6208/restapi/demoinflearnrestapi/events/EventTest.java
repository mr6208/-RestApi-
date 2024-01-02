package mr6208.restapi.demoinflearnrestapi.events;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
@SpringBootTest
public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("ㅇㅇㅇ")
                .description("dddd")
                .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {

        // given
        String name = "dkdkdk";
        String description = "asdf";

        // when
        Event event = new Event();
        event.setDescription(description);
        event.setName(name);

        // then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}