# Spring_boot_Auth

## Run locally

The application uses the `dev` profile by default. It starts with an in-memory H2
database, so MySQL is not required for local development:

```shell
mvn -pl security spring-boot:run
```

The API is available at `http://localhost:8082`. To use MySQL instead, set
`DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` before starting the application.
For OAuth login, configure `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`,
`GITHUB_CLIENT_ID`, and `GITHUB_CLIENT_SECRET`.