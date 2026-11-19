# Spring_boot_Auth

The Netlify site redirects `/` and `/login` to the React application's login
page at https://reactauthf.netlify.app/login. The explicit `public` publish
directory keeps the Java source and configuration out of the static deployment.
Netlify does not run the Spring Boot Dockerfile or Java application.

The React frontend is maintained separately. Its deployed JavaScript currently
uses `http://localhost:8083/api/v1` for API requests. To enable hosted login,
update that frontend's API base URL to the deployed Spring Boot backend's HTTPS
`/api/v1` URL and redeploy the frontend. Its OAuth links currently reference
`https://spring-boot-auth-pe7s.onrender.com`; verify that backend is running
and permits `https://reactauthf.netlify.app` as a credentialed CORS origin.
