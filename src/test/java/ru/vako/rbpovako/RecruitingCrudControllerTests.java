package ru.vako.rbpovako;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class RecruitingCrudControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void vacancyCrudWorks() throws Exception {
        String created = create("/api/vacancies", """
                {
                  "title": "Java developer",
                  "description": "Spring Boot project",
                  "department": "IT",
                  "salaryFrom": 120000,
                  "salaryTo": 180000,
                  "status": "OPEN"
                }
                """);

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(get("/api/vacancies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo("Java developer")));

        mockMvc.perform(put("/api/vacancies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Senior Java developer",
                                  "description": "Spring Boot project",
                                  "department": "IT",
                                  "salaryFrom": 180000,
                                  "salaryTo": 240000,
                                  "status": "OPEN"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(id.intValue())))
                .andExpect(jsonPath("$.title", equalTo("Senior Java developer")));

        mockMvc.perform(get("/api/vacancies"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/vacancies/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/vacancies/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void candidateEndpointsWork() throws Exception {
        String created = create("/api/candidates", """
                {
                  "fullName": "Ivan Petrov",
                  "email": "ivan.petrov@example.com",
                  "phone": "+79990000000",
                  "resume": "Java, SQL"
                }
                """);

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(get("/api/candidates/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("ivan.petrov@example.com")));
    }

    @Test
    void applicationEndpointsWork() throws Exception {
        String created = create("/api/applications", """
                {
                  "vacancyId": 1,
                  "candidateId": 1,
                  "status": "NEW"
                }
                """);

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(put("/api/applications/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "vacancyId": 1,
                                  "candidateId": 1,
                                  "status": "IN_REVIEW"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("IN_REVIEW")));
    }

    @Test
    void interviewEndpointsWork() throws Exception {
        String created = create("/api/interviews", """
                {
                  "applicationId": 1,
                  "interviewer": "HR manager",
                  "startsAt": "2026-04-20T10:00:00",
                  "endsAt": "2026-04-20T11:00:00",
                  "status": "PLANNED"
                }
                """);

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(get("/api/interviews/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.interviewer", equalTo("HR manager")));
    }

    @Test
    void offerEndpointsWork() throws Exception {
        String created = create("/api/offers", """
                {
                  "applicationId": 1,
                  "candidateId": 1,
                  "salary": 190000,
                  "status": "SENT"
                }
                """);

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(delete("/api/offers/{id}", id))
                .andExpect(status().isNoContent());
    }

    private String create(String path, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        return result.getResponse().getContentAsString();
    }
}
