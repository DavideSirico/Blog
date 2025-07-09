package blog.sirico.Blog;

import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers(HttpMethod.POST, "/new-post").hasRole("ADMIN")
				.requestMatchers(HttpMethod.POST, "/edit/*", "/remove/*", "/post/*/edit-title", "/post/*/edit-content").hasRole("ADMIN")
                .requestMatchers("/new-post", "/edit/*", "/remove/*", "/post/*/edit-title", "/post/*/edit-content").hasRole("ADMIN")

				.anyRequest().permitAll()
			)

			.formLogin((form) -> form
				.loginPage("/login")
				.failureUrl("/login?error")
				.permitAll()
			)

			.logout((logout) -> logout
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll()
			);
		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		@SuppressWarnings("deprecation")
		UserDetails user =
			 User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user);
	}
}
