package com.example.restfull_web_services.bootstrap;

import com.example.restfull_web_services.models.Post;
import com.example.restfull_web_services.models.User;
import com.example.restfull_web_services.repositories.PostRepository;
import com.example.restfull_web_services.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
            PostRepository postRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Dev Profile Active: Seeding database with initial users...");
            List<User> users = loadUserData();

            log.info("Dev Profile Active: Seeding database with initial posts...");
            loadPostData(users);
        } else {
            log.info("Users already seeded in dev profile.");
        }
    }

    private List<User> loadUserData() {
        User user1 = User.builder()
                .name("Alice Smith")
                .email("alice@example.com")
                .password(passwordEncoder.encode("password123"))
                .role("ADMIN")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now().toString())
                .updatedAt(LocalDateTime.now().toString())
                .build();

        User user2 = User.builder()
                .name("Bob Johnson")
                .email("bob@example.com")
                .password(passwordEncoder.encode("testpass456"))
                .role("USER")
                .status("INACTIVE")
                .createdAt(LocalDateTime.now().toString())
                .updatedAt(LocalDateTime.now().toString())
                .build();

        User user3 = User.builder()
                .name("Charlie Brown")
                .email("charlie@example.com")
                .password(passwordEncoder.encode("secureword789"))
                .role("USER")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now().toString())
                .updatedAt(LocalDateTime.now().toString())
                .build();

        List<User> saved = userRepository.saveAll(Arrays.asList(user1, user2, user3));
        log.info("Database seeding completed. Saved {} users.", saved.size());
        return saved;
    }

    private void loadPostData(List<User> users) {
        User alice = users.get(0);
        User bob = users.get(1);
        User charlie = users.get(2);

        List<Post> posts = List.of(
                Post.builder()
                        .title("Getting Started with Spring Boot")
                        .content("Spring Boot makes it easy to create stand-alone, production-grade "
                                + "Spring-based Applications that you can 'just run'. In this post we "
                                + "walk through setting up a project from scratch.")
                        .user(alice)
                        .build(),

                Post.builder()
                        .title("Understanding REST APIs")
                        .content("REST (Representational State Transfer) is an architectural style "
                                + "for designing networked applications. This post covers the core "
                                + "constraints and HTTP verbs that make REST tick.")
                        .user(alice)
                        .build(),

                Post.builder()
                        .title("HATEOAS in Practice")
                        .content("Hypermedia as the Engine of Application State (HATEOAS) allows "
                                + "clients to navigate your API dynamically using links embedded "
                                + "in responses. We explore how Spring HATEOAS simplifies this.")
                        .user(bob)
                        .build(),

                Post.builder()
                        .title("JPA Relationships Explained")
                        .content("JPA supports OneToOne, OneToMany, ManyToOne, and ManyToMany "
                                + "relationships. This post digs into how lazy vs eager loading "
                                + "affects performance and how to avoid the N+1 problem.")
                        .user(bob)
                        .build(),

                Post.builder()
                        .title("Spring Boot Filtering with @JsonView")
                        .content("@JsonView lets you control which fields are serialized per endpoint "
                                + "at runtime. It is the recommended replacement for the deprecated "
                                + "MappingJacksonValue + @JsonFilter pattern in Spring Framework 7.")
                        .user(charlie)
                        .build(),

                Post.builder()
                        .title("H2 Console Tips for Dev Profiles")
                        .content("The H2 in-memory console is a great development tool. With the "
                                + "Spring Boot dev profile active you can inspect your schema, run "
                                + "ad-hoc SQL, and debug JPA mappings in seconds.")
                        .user(charlie)
                        .build());

        postRepository.saveAll(posts);
        log.info("Database seeding completed. Saved {} posts.", posts.size());
    }
}
