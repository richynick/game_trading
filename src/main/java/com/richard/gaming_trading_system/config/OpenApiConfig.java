package com.richard.gaming_trading_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gamingTradingSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Gaming Trading System API")
                        .description("A gamified trading platform that allows users to trade virtual assets, earn gems, and compete on leaderboards.")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Richard Nnaji")
                                .email("richardnnaji@gmail.com")
                                .url("https://github.com/richynick/game_trading"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server")
                ));
    }
} 