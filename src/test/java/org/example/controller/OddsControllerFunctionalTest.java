package org.example.controller;

import org.example.database.model.Match;
import org.example.database.model.Odds;
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
class OddsControllerFunctionalTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    private Match match;
    private Odds sampleOdds;

    @BeforeEach
    void setUp() {
        match = new Match();
        match.setTeamA("PAO");
        match.setTeamB("OSFP");
        match.setSport(Sport.FOOTBALL);
        match.setDescription("PAO vs OSFP");
        match.setMatchDate(LocalDate.of(2025, 1, 1));
        match.setMatchTime(LocalTime.of(20, 30));
        match = restTemplate.postForEntity("/api/v1/matches", match, Match.class).getBody();

        sampleOdds = new Odds();
        sampleOdds.setSpecifier("1");
        sampleOdds.setOdd(1.75);
        sampleOdds.setMatchId(match.getId());
    }

    @Test
    void testCreateAndGetOdds() {
        ResponseEntity<Odds> createResponse = restTemplate.postForEntity(
                "/api/v1/matches/" + match.getId() + "/odds", sampleOdds, Odds.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        Odds created = createResponse.getBody();
        assertNotNull(created);
        assertEquals("1", created.getSpecifier());

        ResponseEntity<Odds> getResponse = restTemplate.getForEntity(
                "/api/v1/matches/" + match.getId() + "/odds/" + created.getId(), Odds.class
        );

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertEquals(1.75, getResponse.getBody().getOdd());
    }

    @Test
    void testGetAllOddsByMatch() {
        restTemplate.postForEntity("/api/v1/matches/" + match.getId() + "/odds", sampleOdds, Odds.class);
        ResponseEntity<Odds[]> response = restTemplate.getForEntity(
                "/api/v1/matches/" + match.getId() + "/odds", Odds[].class
        );

        assertTrue(
                response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.NO_CONTENT
        );
    }

    @Test
    void testUpdateOdds() {
        Odds created = restTemplate.postForEntity(
                "/api/v1/matches/" + match.getId() + "/odds", sampleOdds, Odds.class
        ).getBody();
        assertNotNull(created);

        created.setOdd(2.1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Odds> entity = new HttpEntity<>(created, headers);

        ResponseEntity<Odds> updateResponse = restTemplate.exchange(
                "/api/v1/matches/" + match.getId() + "/odds/" + created.getId(), HttpMethod.PUT, entity, Odds.class
        );

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(2.1, updateResponse.getBody().getOdd());
    }

    @Test
    void testDeleteOdds() {
        Odds created = restTemplate.postForEntity(
                "/api/v1/matches/" + match.getId() + "/odds", sampleOdds, Odds.class
        ).getBody();
        assertNotNull(created);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/v1/matches/" + match.getId() + "/odds/" + created.getId(), HttpMethod.DELETE, null, Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<Odds> getResponse = restTemplate.getForEntity(
                "/api/v1/matches/" + match.getId() + "/odds/" + created.getId(), Odds.class
        );
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}
