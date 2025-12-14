# Local Development

## Build

```bash
./gradlew build
```

## Test

```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스
./gradlew test --tests "*JavaCopyAnswerServiceTest"
```

## Run IDE with Plugin

플러그인이 설치된 IntelliJ IDEA 인스턴스 실행

```bash
./gradlew runIde
```

## Build Plugin

배포용 플러그인 zip 파일 생성

```bash
./gradlew buildPlugin
# 결과: build/distributions/programmers_helper-{version}.zip
```
