package put.poznan.backend.testUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import put.poznan.backend.dto.authentication.request.AuthenticationRequest;
import put.poznan.backend.entities.User;
import put.poznan.backend.repository.UserRepository;
import put.poznan.backend.service.AuthenticationService;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration
public class HeaderProvider {

    private final UserRepository userRepository;
    private final AuthenticationService service;

    @Autowired
    public HeaderProvider( UserRepository userRepository, AuthenticationService service ) {
        this.userRepository = userRepository;
        this.service = service;
    }

    public HttpHeaders getUserHeader( User user ) {
        if ( userRepository.findByUsername( user.getUsername() ).isEmpty() ) {
            User requestUser = User.builder()
                    .username( user.getUsername() )
                    .password( BCrypt.hashpw( user.getPassword(), BCrypt.gensalt() ) )
                    .build();
            requestUser = userRepository.save( requestUser );
            user.setId( requestUser.getId() );
        }
        AuthenticationRequest req = AuthenticationRequest.builder()
                .username( user.getUsername() )
                .password( user.getPassword() )
                .build();
        String token = service.authenticate( req ).getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add( "authorization", "Bearer " + token );
        headers.setContentType( MediaType.APPLICATION_JSON );
        headers.setAccessControlAllowOrigin( "*" );
        headers.setAccessControlAllowMethods( new ArrayList<>( Arrays.asList( HttpMethod.GET, HttpMethod.POST
                , HttpMethod.PATCH, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.OPTIONS ) ) );

        return headers;
    }
}
