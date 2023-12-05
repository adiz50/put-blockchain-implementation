package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import put.poznan.backend.dto.users.AllUsersResponse;
import put.poznan.backend.entities.User;
import put.poznan.backend.exception.NotLoggedIn;
import put.poznan.backend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<AllUsersResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AllUsersResponse> resp = new ArrayList<>();
        users.forEach( u -> resp.add( new AllUsersResponse( u ) ) );
        return resp;
    }

    public Optional<User> currentlyLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Optional<User> user = userRepository.findByUsername( username );
        if ( user.isEmpty() ) throw new NotLoggedIn( "No user logged in" );
        return user;
    }

    public AllUsersResponse whoAmI() {
        Optional<User> user = currentlyLoggedInUser();
        return new AllUsersResponse( user.get() );
    }

}
