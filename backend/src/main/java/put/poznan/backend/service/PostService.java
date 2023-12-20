package put.poznan.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import put.poznan.backend.dto.comment.request.PostCommentRequest;
import put.poznan.backend.dto.post.GetAllPostsResponse;
import put.poznan.backend.entities.Comment;
import put.poznan.backend.entities.Post;
import put.poznan.backend.entities.User;
import put.poznan.backend.exception.AccessDenied;
import put.poznan.backend.repository.CommentRepository;
import put.poznan.backend.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public GetAllPostsResponse getPosts( String query ) {
        if ( query == null ) {
            return new GetAllPostsResponse( postRepository.findAll() );
        }
        return new GetAllPostsResponse( postRepository.getPostsContaining( "%" + query + "%" ) );
    }

    @EventListener(ApplicationReadyEvent.class)
    public void addPosts() {
        if ( postRepository.getCountOfPosts() >= 2 ) return;
        String article1 = "Blockchain technology, a decentralized and secure ledger system, has revolutionized " +
                "various industries. At its core, blockchain is a distributed database that records transactions " +
                "across a network of computers, ensuring transparency and immutability. This tamper-resistant " +
                "technology is the backbone of cryptocurrencies like Bitcoin, providing a transparent and efficient " +
                "way to verify transactions without the need for intermediaries. Beyond finance, blockchain finds " +
                "applications in supply chain management, healthcare, and voting systems, enhancing data integrity " +
                "and reducing fraud. As a decentralized ledger, blockchain fosters trust in a trustless environment, " +
                "enabling a new era of secure and transparent digital transactions across diverse sectors.";
        String article2 = "Blockchain, a transformative technology, has emerged as a decentralized solution shaping " +
                "the digital landscape. It functions as a distributed ledger, recording transactions across a network" +
                " in a secure and transparent manner. At its forefront is the guarantee of immutability, ensuring " +
                "that once data is added, it cannot be altered. Blockchain's prominence extends beyond " +
                "cryptocurrencies, finding applications in supply chain, healthcare, and smart contracts. Its " +
                "decentralized nature eliminates the need for intermediaries, reducing costs and enhancing efficiency" +
                ". With the potential to revolutionize industries by fostering trust and transparency, blockchain " +
                "stands as a cornerstone for the future of secure and decentralized digital transactions.";
        String title1 = "Unleashing the Power of Blockchain: Transforming Industries with Decentralized Trust and " +
                "Transparency";
        String title2 = "Blockchain: Pioneering Trust and Transparency in the Digital Era";
        List<Post> posts = List.of(
                Post.builder().body( article1 ).title( title1 ).date( LocalDateTime.now() ).build(),
                Post.builder().body( article2 ).title( title2 ).date( LocalDateTime.now() ).build()
        );
        postRepository.saveAll( posts );
    }

    public Comment addComment( PostCommentRequest req ) {
        Optional<User> user = userService.currentlyLoggedInUser();
        if ( user.isEmpty() ) throw new AccessDenied( "You must be logged in" );
        Optional<Post> post = postRepository.findById( req.getPostId() );
        if ( post.isEmpty() ) throw new RuntimeException( "Post not found" );
        List<Comment> comments = post.get().getComments();
        Comment comment = Comment.builder()
                .date( LocalDateTime.now() )
                .text( req.getBody() )
                .user( user.get() )
                .post( post.get() )
                .build();
        comments.add( comment );
        post.get().setComments( comments );
        postRepository.save( post.get() );
        return comment;
    }
}
