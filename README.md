# Spring Plus - Advanced Todo System

This project is a heavily refactored and optimized Spring Boot application, featuring advanced QueryDSL searching, Spring Security integration, and a production-grade AWS deployment pipeline.

## 🚀 Key Features

### 1. Advanced Search (QueryDSL)
- **Multi-condition Search**: Enhanced Todo search by title, date range, and manager nickname.
- **Statistics API**: Aggregated data for Todos, including manager and comment counts per Todo.
- **Performance**: Optimized from Full Table Scan to Index-based search for high-volume data.

### 2. Spring Security & JWT
- **Modern Authentication**: Migrated legacy Filter and ArgumentResolver to Spring Security 6.
- **Dynamic Claims**: JWT includes custom claims such as user nickname for frontend use.
- **Public & Private APIs**: Granular control over permit-all (e.g., health-check) and authenticated endpoints.

### 3. AWS Infrastructure & CI/CD
- **Containerization**: Multi-stage Docker builds for minimal image size.
- **CI/CD Pipeline**: GitHub Actions for automated testing, Docker Hub image pushing, and EC2 deployment.
- **Storage**: Integrated AWS S3 for profile image management.
- **Database**: Dedicated AWS RDS (MySQL) for persistence.

### 4. High-Volume Data Processing (Level 13)
- **Bulk Insertion**: Successfully inserted **5 million users** using JDBC `batchUpdate` for scalability testing.
- **Index Optimization**: Applied `idx_user_nickname` on the `User` table.
  - **Before Index**: ~1.5s query time.
  - **After Index**: < 50ms query time.

## 🛠️ Tech Stack
- **Languages**: Java 17 (Amazon Corretto)
- **Frameworks**: Spring Boot 3.3.3, Spring Data JPA, QueryDSL 5.0
- **Security**: Spring Security, JWT
- **Infrastructure**: AWS (EC2, RDS, S3), Docker, GitHub Actions, Docker Compose
- **Database**: MySQL 8.0

## 📦 Deployment Guide

### Prerequisites
Ensure the following GitHub Secrets are configured:
- `RDS_URL`, `RDS_USERNAME`, `RDS_PASSWORD`
- `AWS_REGION`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `S3_BUCKET_NAME`
- `JWT_SECRET_KEY`, `DOCKER_USERNAME`, `DOCKER_PASSWORD`
- `EC2_HOST`, `EC2_USERNAME`, `EC2_SSH_KEY`

### Trigger Deployment
Simply push to the `main` or `feat/advanced-features-and-deployment` branches to trigger the automated CI/CD pipeline.
