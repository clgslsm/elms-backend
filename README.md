# Employee Leave Management System (ELMS)

This is a Spring Boot-based backend application for managing employee leave requests. The system provides features for user authentication, leave request management, and role-based access control.

## Features

- **Authentication**: User registration, login, and JWT-based authentication.
- **Leave Management**: Employees can create, update, and delete leave requests. Managers can approve or decline leave requests.
- **Role-Based Access Control**: Different functionalities for employees and managers.
- **Swagger API Documentation**: Integrated Swagger UI for API exploration.
- **Scheduled Tasks**: Automatically updates leave days for users monthly.
- **Docker Support**: Dockerized application for easy deployment.
- **CI/CD**: GitHub Actions workflow for building and deploying the application.

## Technologies Used

- ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) **Spring Boot**: Backend framework.
- ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) **MySQL**: Database.
- ![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white) **JWT**: Authentication and authorization.
- ![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black) **Swagger**: API documentation.
- ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white) **Docker**: Containerization.
- ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white) **GitHub Actions**: CI/CD pipeline.

## Development Methodology

This project was developed using the **Scrum Agile Methodology**. The key aspects of the process include:

- **Sprints**: The development was divided into multiple sprints, each lasting two weeks.
- **Daily Standups**: Regular team meetings were held to discuss progress, blockers, and plans for the day.
- **Sprint Planning**: At the beginning of each sprint, tasks were prioritized and assigned to team members.
- **Sprint Reviews**: At the end of each sprint, the team demonstrated the completed features to stakeholders.
- **Retrospectives**: After each sprint, the team reflected on what went well and what could be improved for the next sprint.
- **Backlog Management**: A prioritized backlog of tasks was maintained and updated regularly.

This approach ensured continuous delivery of incremental improvements and adaptability to changing requirements.

## Prerequisites

- Java 21 or higher
- Maven
- MySQL
- Docker (optional for containerized deployment)

## Getting Started

### Clone the Repository
```bash
git clone https://github.com/your-username/elms-backend.git
cd elms-backend
```

### Configure Environment Variables
Create a `.env` file in the root directory with the following variables:
```env
DB_URL=jdbc:mysql://<your-database-url>:3306/elms
DB_USERNAME=<your-database-username>
DB_PASSWORD=<your-database-password>
JWT_SECRET=<your-jwt-secret>
JWT_EXPIRATION=3600000
SHOW_SQL=false
PORT=8080
```

### Build and Run the Application
#### Using Maven
```bash
./mvnw spring-boot:run
```

#### Using Docker
```bash
docker build -t elms-backend .
docker run -p 8080:8080 --env-file .env elms-backend
```

### Access the Application
- **API Base URL**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## Running Tests
Run the following command to execute the test suite:
```bash
./mvnw test
```

## Deployment
The application is configured for deployment to DigitalOcean using GitHub Actions. Ensure the following secrets are set in your GitHub repository:
- `DIGITALOCEAN_ACCESS_TOKEN`
- `DO_REGISTRY_NAME`
- `DO_HOST`
- `DO_USERNAME`
- `DO_SSH_KEY`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`

## License
This project is licensed under the Apache License 2.0. See the `LICENSE` file for details.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request.

