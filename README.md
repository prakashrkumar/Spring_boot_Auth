# Spring Boot Auth

Java 21 Spring Boot authentication API with MySQL, JWT sessions, and Google/GitHub OAuth.

## Why the Netlify URL returns 404

This repository contains a Java API, with no static website for Netlify to publish. Netlify does not run JVM applications or Docker containers. Publishing the repository there does not start Spring Boot. Changing a publish directory or adding an index page cannot make the API run.

Keep the Spring Boot application on a host that supports Java 21 or Docker. A separate frontend can be hosted on Netlify and call the deployed API over HTTPS.

## Container deployment

Use the repository root as the Docker build context and the root `Dockerfile`. Alternatively, use `security/` as the build context with its own `Dockerfile`. Both images select the production profile by default. The production server listens on the host-provided `PORT`, falling back to 8080, and honors proxy forwarding headers. Only expose the application through a trusted reverse proxy that replaces incoming forwarding headers.

Configure the following environment variables through the backend host's secret/environment settings; do not commit their values:

- Database: `DB_URL` (a JDBC MySQL URL), `DB_USERNAME`, `DB_PASSWORD`.
- JWT: `JWT_SECRET`, `JWT_ISSUER`, `JWT_ACCESS_TTL_SECONDS`, `JWT_REFRESH_TTL_SECONDS`.
- Refresh cookie: `JWT_REFRESH_COOKIE_NAME`, `JWT_COOKIE_SECURE`, `JWT_COOKIE_HTTP_ONLY`, `JWT_COOKIE_SAME_SITE`, `JWT_COOKIE_DOMAIN`. Use secure, HTTP-only cookies in production and choose the SameSite policy and domain for the actual frontend/backend domains.
- Google OAuth: `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`.
- GitHub OAuth: `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET`.
- Frontend: `FRONT_END_URL` (comma-separated allowed origins), `FRONT_END_SUCCESS_REDIRECT`, `FRONT_END_FAILURE_REDIRECT`.

Register the public backend callback URLs `/login/oauth2/code/google` and `/login/oauth2/code/github` with the corresponding OAuth providers. Allow the actual HTTPS frontend origin in `FRONT_END_URL`. Both OAuth providers are configured by the application, so their credentials are required at startup.

After deploying, verify `/swagger-ui/index.html` and test registration and login through `/api/v1/auth/register` and `/api/v1/auth/login`. The API does not define a homepage at `/`; use its documented routes to check it.

The production database configuration currently uses Hibernate schema updates. Review that setting and establish a migration strategy before using the service with important data.

## Deployment status

The Docker configuration is prepared for a Java-capable host. A backend host and its runtime configuration are still required; the Netlify URL alone cannot serve this Java API.

`http://localhost:8082` is a local development address, not a public backend URL. In a visitor's browser, `localhost` refers to that visitor's computer. Netlify redirects or frontend configuration cannot make your local server publicly accessible. Deploy the backend to a Java-capable host and use its public HTTPS URL for any Netlify-hosted frontend. The production container now defaults to port 8080; set `PORT=8082` explicitly if that local production port is needed.
