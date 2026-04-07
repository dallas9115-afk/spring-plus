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

### 3. AWS 인프라 및 서비스 구현
- **EC2 (Elastic Compute Cloud)**:
  - AWS EC2 인스턴스에 애플리케이션을 배포하여 운영 중입니다.
  - **Health Check API**: `/health` 경로를 통해 서버의 실시간 Live 상태를 확인할 수 있습니다. (누구나 접근 가능)
- **RDS (Relational Database Service)**:
  - AWS RDS(MySQL 8.0)를 구축하고 EC2 애플리케이션과 안정적으로 연동했습니다.
- **S3 (Simple Storage Service)**:
  - 유저 프로필 이미지 업로드 및 관리를 위한 API를 구현했습니다.
  - S3 버킷을 연동하여 이미지 파일을 안전하게 보관 및 서빙합니다.
- **CI/CD 파이프라인**: GitHub Actions를 이용하여 자동 테스트, Docker Hub 이미지 푸시, EC2 배포 자동화를 구축했습니다.

### 4. 대용량 데이터 처리 및 성능 최적화 (Level 13)
- **대량 데이터 삽입**: 확장성 테스트를 위해 `JdbcTemplate`의 `batchUpdate`를 활용하여 **500만 명**의 랜덤 닉네임 유저를 삽입했습니다.
- **성능 비교 결과**:
  | 구분 | 소요 시간 | 비고 |
  |:---:|:---:|:---|
  | **인덱스 미적용** | 547ms | Full Table Scan 수행 |
  | **인덱스 적용** | 152ms | `idx_user_nickname` 활용 |

- **증빙 자료**:
  - 인덱스 미적용 결과: <img width="559" height="368" alt="인덱스 전 1회차" src="https://github.com/user-attachments/assets/5a7a9323-bbc7-4c55-8b8e-e377d44d365f" />

  - 인덱스 적용 결과: <img width="502" height="346" alt="인덱스 1회차" src="https://github.com/user-attachments/assets/51eb3bc2-9f86-4ffa-bcf9-ad5f0b844aec" />


## 기술 스택
- **언어**: Java 17 (Amazon Corretto)
- **프레임워크**: Spring Boot 3.3.3, Spring Data JPA, QueryDSL 5.0
- **보안**: Spring Security, JWT
- **인프라**: AWS (EC2, RDS, S3), Docker, GitHub Actions, Docker Compose
- **데이터베이스**: MySQL 8.0

## 개발 및 테스트 가이드

### 벌크 데이터 삽입 테스트 (Local)
500만 건의 대용량 데이터를 로컬 환경에서 테스트하려면 `src/test` 폴더의 JUnit 테스트를 실행하세요.
1. `src/test/java/.../UserBulkInsertTest.java` 파일 열기
2. `bulkInsertUsers()` 메서드 옆의 재생 버튼(▶️) 클릭
* **참고**: 대용량 처리를 위해 Gradle 테스트 힙 메모리가 **4GB**로 최적화되어 있어, 메모리 부족 없이 완주가 가능합니다. (`build.gradle` 참조)

### 배포 사전 준비 사항 (CI/CD)
배포를 위해 다음 GitHub Secrets 항목들이 설정되어 있어야 합니다:
- `RDS_URL`, `RDS_USERNAME`, `RDS_PASSWORD`
- `AWS_REGION`, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `S3_BUCKET_NAME`
- `JWT_SECRET_KEY`, `DOCKER_USERNAME`, `DOCKER_PASSWORD`
- `EC2_HOST`, `EC2_USERNAME`, `EC2_SSH_KEY`
