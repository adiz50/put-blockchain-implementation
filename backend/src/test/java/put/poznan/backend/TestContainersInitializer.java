package put.poznan.backend;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public class TestContainersInitializer implements ApplicationContextInitializer< ConfigurableApplicationContext > {

    @Container
    public static final PostgreSQLContainer postgresSqlContainer = new PostgreSQLContainer<>( DockerImageName.parse(
            "postgres:latest" ) )
            .withDatabaseName( "test" )
            .withUsername( "postgres" )
            .withPassword( "postgres" );

    @Override
    public void initialize( ConfigurableApplicationContext applicationContext ) {
        postgresSqlContainer.start();
        TestPropertyValues.of(
                "spring.datasource.url=" + postgresSqlContainer.getJdbcUrl(),
                "spring.datasource.username=" + postgresSqlContainer.getUsername(),
                "spring.datasource.password=" + postgresSqlContainer.getPassword()
        ).applyTo( applicationContext );
    }
}
