## bulider로 오타가 나서 수정
#FROM gradle:jdk17 AS builder
#USER root
## gradlew를 못 찾아서 추가한 부분
#RUN apt-get update && apt-get install -y dos2unix
#
#WORKDIR /builder
#
## gradlew 관련 파일 및 설정 복사
#COPY build.gradle settings.gradle gradlew /builder/
#COPY ./gradle /builder/gradle
#
## gradlew 줄바꿈 및 실행 권한 설정
#RUN dos2unix /builder/gradlew
#RUN chmod +x /builder/gradlew
#
## 소스 복사
#COPY ./src /builder/src
#
## 포맷 자동 적용 후 빌드
#RUN ./gradlew spotlessApply
#RUN ./gradlew build --no-daemon
#
## 실제 실행용 이미지
#FROM eclipse-temurin:17
#WORKDIR /app
#
## 빌드 결과물 복사
#COPY --from=builder /builder/build/libs/*.jar app.jar
#
## 실행
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]

FROM gradle:jdk17 AS bulider
WORKDIR /builder

COPY build.gradle settings.gradle gradlew /builder/
COPY ./gradle /builder/gradle

RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

COPY ./src /builder/src
RUN ./gradlew build --no-daemon

FROM eclipse-temurin:17
WORKDIR /app

COPY --from=bulider /builder/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]