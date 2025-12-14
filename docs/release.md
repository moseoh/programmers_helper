# Release

main 브랜치가 보호되어 있으므로 PR을 통해 릴리즈 진행

## 릴리즈 프로세스

```bash
# 1. release 브랜치 생성 및 버전 업데이트
git branch -D chore/release 2>/dev/null
git checkout -b chore/release
./gradlew bumpPatch  # bumpPatch | bumpMinor | bumpMajor
git add build.gradle.kts
git commit -m "chore: bump version to $(./gradlew printVersion -q)"
git push -u origin chore/release

# 2. PR 생성 → 머지
gh pr create --title "chore: release v$(./gradlew printVersion -q)" --label release --fill
gh pr merge --admin --squash --delete-branch

# 3. main에서 태그 생성 및 푸시
git checkout main
git pull
git tag "v$(./gradlew printVersion -q)"
git push --tags
```

GitHub Actions가 태그 푸시 감지 → 자동으로 빌드 + JetBrains Marketplace 퍼블리시 + GitHub Release 생성

| 버전          | 언제                  | 예시                  |
| ------------- | --------------------- | --------------------- |
| patch (0.2.1) | 버그 수정, 내부 변경  | 오타 수정, 버그 픽스  |
| minor (0.3.0) | 기능 추가 (하위 호환) | 새 언어 지원, 새 옵션 |
| major (1.0.0) | 호환 깨지는 변경      | API 변경, 기능 삭제   |
