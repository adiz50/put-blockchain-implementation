package put.poznan.backend.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import put.poznan.backend.TestContainersInitializer;
import put.poznan.backend.dto.authentication.request.AuthenticationRequest;
import put.poznan.backend.dto.authentication.response.AuthenticationResponse;
import put.poznan.backend.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestContainersInitializer.class)
public class AuthenticationControllerTest {

    private final TestRestTemplate restTemplate;
    private final UserRepository userRepository;

    @Autowired
    public AuthenticationControllerTest( TestRestTemplate restTemplate, UserRepository userRepository ) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void register_correct() {
        //Given
        AuthenticationRequest req = AuthenticationRequest.builder()
                .username( "test" )
                .password( "test" )
                .build();
        //When
        ResponseEntity< AuthenticationResponse > resp = restTemplate.exchange( "/api/auth/register",
                HttpMethod.POST, new HttpEntity<>( req, null ), AuthenticationResponse.class );
        //Then
        assertEquals( HttpStatus.OK, resp.getStatusCode() );
        assertTrue( userRepository.findByUsername( "test" ).isPresent() );
    }

    @Test
    public void registerAndAuthenticate_correct() {
        //Given
        AuthenticationRequest req = AuthenticationRequest.builder()
                .username( "test" )
                .password( "test" )
                .build();
        restTemplate.exchange( "/api/auth/register", HttpMethod.POST, new HttpEntity<>( req, null ),
                AuthenticationResponse.class );
        //When
        ResponseEntity< AuthenticationResponse > resp = restTemplate.exchange( "/api/auth/authenticate",
                HttpMethod.POST, new HttpEntity<>( req, null ), AuthenticationResponse.class );
        //Then
        assertEquals( HttpStatus.OK, resp.getStatusCode() );
    }
}