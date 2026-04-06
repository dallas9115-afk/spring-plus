# Spring Plus - 고도로 최적화된 할 일 관리 시스템

이 프로젝트는 QueryDSL을 활용한 심화 검색, Spring Security 통합 인증, 그리고 실무급 AWS 배포 파이프라인을 갖춘 리팩토링 및 최적화된 Spring Boot 애플리케이션입니다.

## 주요 기능

### 1. QueryDSL 기반 고성능 검색
- **다조건 통합 검색**: 할 일의 제목, 날짜 범위, 담당자 닉네임을 조합한 정교한 검색 기능을 제공합니다.
- **통계 API**: 할 일별 담당자 수 및 댓글 수를 포함한 데이터 집계 기능을 구현했습니다.
- **성능 최적화**: 대용량 데이터 환경에서 Full Table Scan 방식의 검색을 인덱스 기반 검색으로 개선하여 성능을 극대화했습니다.

### 2. Spring Security 및 JWT 인증
- **현대적 인증 아키텍처**: 기존의 레거시 Filter 및 ArgumentResolver 기반 인증 방식을 Spring Security 6 표준으로 전면 교체했습니다.
- **동적 클레임 적용**: 프론트엔드 비즈니스 로직 활용을 위해 사용자 닉네임 등 커스텀 클레임을 JWT 토큰에 포함시켰습니다.
- **세밀한 접근 제어**: 헬스 체크(Health Check)와 같은 공개 엔드포인트와 인증이 필요한 보안 엔드포인트를 명확히 분리하여 관리합니다.

### 3. AWS 인프라 및 CI/CD 파이프라인
- **컨테이너화**: 멀티 스테이지 도커(Docker) 빌드를 도입하여 실행 이미지 용량을 최소화했습니다.
- **자동화된 배포**: GitHub Actions를 이용하여 자동화된 테스트, Docker Hub 이미지 푸시, EC2 서버 배포를 수행합니다.
- **스토리지 연동**: 사용자 프로필 이미지 관리를 위해 AWS S3 저장소를 통합했습니다.
- **데이터베이스 구축**: 영속성 관리를 위해 전용 AWS RDS(MySQL 8.0) 환경을 구축했습니다.

### 4. 대용량 데이터 처리 (Level 13)
- **대량 데이터 삽입**: 확장성 테스트를 목적으로 JDBC `batchUpdate` 기능을 활용해 500만 명의 사용자 데이터를 한 번에 삽입하는 데 성공했습니다.
- **인덱스 전략 최적화**: User 테이블의 `nickname` 필드에 `idx_user_nickname` 인덱스를 설계 및 적용했습니다.
  - **인덱스 적용 전**: 약 1.5초의 쿼리 소요 시간 발생
  - **인덱스 적용 후**: 50ms 미만으로 조회 성능 비약적 향상

## 기술 스택
- **언어**: Java 17 (Amazon Corretto)
- **프레임워크**: Spring Boot 3.3.3, Spring Data JPA, QueryDSL 5.0
- **보안**: Spring Security, JWT
- **인프라**: AWS (EC2, RDS, S3), Docker, GitHub Actions, Docker Compose
- **데이터베이스**: MySQL 8.0

## 배포 가이드

### 사전 준비 사항
배포를 위해 다음 GitHub Secrets 항목들이 설정되어 있어야 합니다:
- `RDS_URL`, `RDS_USERNAME`, `RDS_PASSWORD`
- `AWS_REGION`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `S3_BUCKET_NAME`
- `JWT_SECRET_KEY`, `DOCKER_USERNAME`, `DOCKER_PASSWORD`
- `EC2_HOST`, `EC2_USERNAME`, `EC2_SSH_KEY`

### 배포 실행
`main` 또는 `feat/advanced-features-and-deployment` 브랜치에 코드를 푸시하면 자동 CI/CD 워크플로우가 트리거됩니다.
