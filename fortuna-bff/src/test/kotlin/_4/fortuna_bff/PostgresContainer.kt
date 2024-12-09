package _4.fortuna_bff

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

internal class PostgresContainer: ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
            postgres.start()
            TestPropertyValues.of(
                    "spring.datasource.url=${postgres.jdbcUrl}",
                    "spring.datasource.username=${postgres.username}",
                    "spring.datasource.password=${postgres.password}"
            ).applyTo(applicationContext.environment)
    }

    companion object {
        private val postgres: KPostgres by lazy {
            KPostgres("postgres:16-alpine")
        }
    }
}


class KPostgres(image: String) : PostgreSQLContainer<KPostgres>(image)
