package ru.vako.rbpovako;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import ru.vako.rbpovako.model.UserRole;
import ru.vako.rbpovako.service.UserAccountService;

@SpringBootTest
@AutoConfigureMockMvc
class RecruitingCrudControllerTests {
    private static final String ADMIN_PASSWORD = "Admin!234";
    private static final String HR_PASSWORD = "HrUser!234";
    private static final String CANDIDATE_PASSWORD = "Candidate!234";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountService userAccountService;

    private String adminUsername;
    private String hrUsername;
    private String candidateUsername;

    @BeforeEach
    void setUpUsers() {
        String suffix = String.valueOf(System.nanoTime());
        adminUsername = "admin" + suffix;
        hrUsername = "hr" + suffix;
        candidateUsername = "candidate" + suffix;
        userAccountService.register(adminUsername, adminUsername + "@example.com", ADMIN_PASSWORD, UserRole.ADMIN);
        userAccountService.register(hrUsername, hrUsername + "@example.com", HR_PASSWORD, UserRole.HR);
        userAccountService.register(candidateUsername, candidateUsername + "@example.com", CANDIDATE_PASSWORD, UserRole.CANDIDATE);
    }

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
                        .with(httpBasic(hrUsername, HR_PASSWORD))
                        .with(csrf())
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

        mockMvc.perform(delete("/api/vacancies/{id}", id)
                        .with(httpBasic(adminUsername, ADMIN_PASSWORD))
                        .with(csrf()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/vacancies/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void candidateEndpointsWork() throws Exception {
        String created = createAs("/api/candidates", """
                {
                  "fullName": "Ivan Petrov",
                  "email": "ivan.petrov.test@example.com",
                  "phone": "+79990000000",
                  "resume": "Java, SQL"
                }
                """);

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(get("/api/candidates/{id}", id)
                        .with(httpBasic(hrUsername, HR_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", equalTo("ivan.petrov.test@example.com")));
    }

    @Test
    void applicationEndpointsWork() throws Exception {
        Long vacancyId = createVacancy("Backend developer");
        Long candidateId = createCandidate("Petr Ivanov", "petr.ivanov@example.com");

        String created = create("/api/applications", """
                {
                  "vacancyId": %d,
                  "candidateId": %d,
                  "status": "NEW"
                }
                """.formatted(vacancyId, candidateId));

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(put("/api/applications/{id}", id)
                        .with(httpBasic(hrUsername, HR_PASSWORD))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "vacancyId": %d,
                                  "candidateId": %d,
                                  "status": "IN_REVIEW"
                                }
                                """.formatted(vacancyId, candidateId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("IN_REVIEW")));
    }

    @Test
    void interviewEndpointsWork() throws Exception {
        Long applicationId = createApplication();

        String created = create("/api/interviews", """
                {
                  "applicationId": %d,
                  "interviewer": "HR manager",
                  "startsAt": "2026-04-20T10:00:00",
                  "endsAt": "2026-04-20T11:00:00",
                  "status": "PLANNED"
                }
                """.formatted(applicationId));

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(get("/api/interviews/{id}", id)
                        .with(httpBasic(hrUsername, HR_PASSWORD)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.interviewer", equalTo("HR manager")));
    }

    @Test
    void offerEndpointsWork() throws Exception {
        Long applicationId = createApplication();
        Long candidateId = JsonTestHelper.longValueFrom(
                mockMvc.perform(get("/api/applications/{id}", applicationId)
                                .with(httpBasic(hrUsername, HR_PASSWORD)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                "candidateId"
        );

        String created = create("/api/offers", """
                {
                  "applicationId": %d,
                  "candidateId": %d,
                  "salary": 190000,
                  "status": "SENT"
                }
                """.formatted(applicationId, candidateId));

        Long id = JsonTestHelper.idFrom(created);

        mockMvc.perform(delete("/api/offers/{id}", id)
                        .with(httpBasic(adminUsername, ADMIN_PASSWORD))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void businessScenarioWorks() throws Exception {
        Long vacancyId = createVacancy("Team lead");
        Long candidateId = createCandidate("Anna Volkova", "anna.volkova@example.com");

        String application = create("/api/business/applications/apply", """
                {
                  "vacancyId": %d,
                  "candidateId": %d
                }
                """.formatted(vacancyId, candidateId));
        Long applicationId = JsonTestHelper.idFrom(application);

        String interview = create("/api/business/interviews/schedule", """
                {
                  "applicationId": %d,
                  "interviewer": "Tech lead",
                  "startsAt": "2026-04-21T10:00:00",
                  "endsAt": "2026-04-21T11:00:00"
                }
                """.formatted(applicationId));
        Long interviewId = JsonTestHelper.idFrom(interview);

        mockMvc.perform(post("/api/business/interviews/{id}/complete", interviewId)
                        .with(httpBasic(hrUsername, HR_PASSWORD))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "successful": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("COMPLETED")));

        String offer = create("/api/business/offers/create", """
                {
                  "applicationId": %d,
                  "salary": 250000
                }
                """.formatted(applicationId));
        Long offerId = JsonTestHelper.idFrom(offer);

        mockMvc.perform(post("/api/business/offers/{id}/accept", offerId)
                        .with(httpBasic(candidateUsername, CANDIDATE_PASSWORD))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo("ACCEPTED")));
    }

    @Test
    void securityRulesWork() throws Exception {
        mockMvc.perform(get("/api/candidates"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "weak-user",
                                  "email": "weak-user@example.com",
                                  "password": "password"
                                }
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "new-candidate",
                                  "email": "new-candidate@example.com",
                                  "password": "Strong!234"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", equalTo("CANDIDATE")));

        mockMvc.perform(post("/api/vacancies")
                        .with(httpBasic(candidateUsername, CANDIDATE_PASSWORD))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "Closed role",
                                  "description": "No access",
                                  "department": "IT",
                                  "status": "OPEN"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    private String create(String path, String body) throws Exception {
        return createAs(path, body, hrUsername, HR_PASSWORD);
    }

    private String createAs(String path, String body) throws Exception {
        return createAs(path, body, adminUsername, ADMIN_PASSWORD);
    }

    private String createAs(String path, String body, String username, String password) throws Exception {
        MvcResult result = mockMvc.perform(post(path)
                        .with(httpBasic(username, password))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    private Long createApplication() throws Exception {
        Long vacancyId = createVacancy("QA engineer");
        Long candidateId = createCandidate("Sergey Fedorov " + System.nanoTime(), "sergey" + System.nanoTime() + "@example.com");
        String application = create("/api/applications", """
                {
                  "vacancyId": %d,
                  "candidateId": %d,
                  "status": "NEW"
                }
                """.formatted(vacancyId, candidateId));
        return JsonTestHelper.idFrom(application);
    }

    private Long createVacancy(String title) throws Exception {
        String vacancy = createAs("/api/vacancies", """
                {
                  "title": "%s",
                  "description": "Test vacancy",
                  "department": "IT",
                  "salaryFrom": 100000,
                  "salaryTo": 150000,
                  "status": "OPEN"
                }
                """.formatted(title));
        return JsonTestHelper.idFrom(vacancy);
    }

    private Long createCandidate(String fullName, String email) throws Exception {
        String candidate = createAs("/api/candidates", """
                {
                  "fullName": "%s",
                  "email": "%s",
                  "phone": "+79990000001",
                  "resume": "Test resume"
                }
                """.formatted(fullName, email));
        return JsonTestHelper.idFrom(candidate);
    }
}
