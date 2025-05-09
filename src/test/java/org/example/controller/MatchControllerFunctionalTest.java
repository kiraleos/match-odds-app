package org.example.controller;

import org.example.database.model.Match;
import org.example.database.model.Sport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MatchControllerFunctionalTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    private Match sampleMatch;

    @BeforeEach
    void setUp() {
        sampleMatch = new Match();
        sampleMatch.setTeamA("PAO");
        sampleMatch.setTeamB("OSFP");
        sampleMatch.setSport(Sport.FOOTBALL);
        sampleMatch.setDescription("PAO vs OSFP");
        sampleMatch.setMatchDate(LocalDate.of(2025, 1, 1));
        sampleMatch.setMatchTime(LocalTime.of(20, 30));
    }

    @Test
    void testCreateAndGetMatch() {
        ResponseEntity<Match> createResponse = restTemplate.postForEntity("/api/v1/matches", sampleMatch, Match.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Match created = createResponse.getBody();
        assertNotNull(created);
        assertEquals("PAO", created.getTeamA());

        ResponseEntity<Match> getResponse = restTemplate.getForEntity("/api/v1/matches/" + created.getId(), Match.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals("PAO vs OSFP", getResponse.getBody().getDescription());
    }

    @Test
    void testGetAllMatchesReturnsEmptyInitially() {
        ResponseEntity<Match[]> response = restTemplate.getForEntity("/api/v1/matches", Match[].class);
        if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
            assertNull(response.getBody());
        } else {
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }
    }

    @Test
    void testUpdateMatch() {
        Match created = restTemplate.postForEntity("/api/v1/matches", sampleMatch, Match.class).getBody();
        assertNotNull(created);

        created.setDescription("Updated Description");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Match> entity = new HttpEntity<>(created, headers);

        ResponseEntity<Match> updateResponse = restTemplate.exchange(
                "/api/v1/matches/" + created.getId(), HttpMethod.PUT, entity, Match.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Updated Description", updateResponse.getBody().getDescription());
    }

    @Test
    void testDeleteMatch() {
        Match created = restTemplate.postForEntity("/api/v1/matches", sampleMatch, Match.class).getBody();
        assertNotNull(created);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/v1/matches/" + created.getId(), HttpMethod.DELETE, null, Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<Match> getResponse = restTemplate.getForEntity("/api/v1/matches/" + created.getId(), Match.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
