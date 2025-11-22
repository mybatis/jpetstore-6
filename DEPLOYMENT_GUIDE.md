# Oracle Cloud 프리티어 배포 가이드

JPetStore 애플리케이션을 Oracle Cloud 프리티어에 배포하는 방법입니다.

## 📋 사전 준비

1. **Oracle Cloud 계정** (프리티어 사용 가능)
2. **GitHub 계정** 및 리포지토리 (이미 설정됨: https://github.com/Jeong-Ryeol/jpetstore-6.git)
3. **Google Gemini API 키** (이미 GitHub Secrets에 추가됨)

---

## 1️⃣ Oracle Cloud VM 인스턴스 생성

### 1-1. Oracle Cloud 콘솔 로그인
- https://cloud.oracle.com 접속
- 계정 로그인

### 1-2. Compute Instance 생성
1. **메뉴** > **Compute** > **Instances** 클릭
2. **Create Instance** 버튼 클릭
3. 다음 설정:
   - **Name**: `jpetstore-server` (또는 원하는 이름)
   - **Image**: `Oracle Linux 8` (또는 최신 버전)
   - **Shape**: `VM.Standard.E2.1.Micro` (프리티어 Always Free)
   - **SSH keys**: 새로 생성하거나 기존 SSH 키 업로드
     - ⚠️ **중요**: Private Key를 반드시 저장하세요!
4. **Create** 클릭

### 1-3. 공인 IP 확인
- 인스턴스 생성 완료 후 **Public IP Address** 확인
- 예: `123.45.67.89`

---

## 2️⃣ 방화벽 규칙 설정 (8080 포트 열기)

### 2-1. Security List 설정
1. Oracle Cloud 콘솔에서:
   - **Networking** > **Virtual Cloud Networks** 클릭
   - VCN 선택 (인스턴스가 있는 VCN)
   - **Security Lists** 클릭
   - Default Security List 선택

2. **Ingress Rules** 추가:
   - **Add Ingress Rules** 클릭
   - 다음 입력:
     - **Source CIDR**: `0.0.0.0/0`
     - **IP Protocol**: `TCP`
     - **Destination Port Range**: `8080`
   - **Add Ingress Rules** 클릭

---

## 3️⃣ VM 초기 설정

### 3-1. SSH로 VM 접속
```bash
ssh -i /path/to/your-private-key opc@<PUBLIC_IP>
```

예시:
```bash
ssh -i ~/.ssh/oracle_key opc@123.45.67.89
```

### 3-2. 설정 스크립트 실행
```bash
# 스크립트 다운로드
curl -o setup-oracle-vm.sh https://raw.githubusercontent.com/Jeong-Ryeol/jpetstore-6/master/scripts/setup-oracle-vm.sh

# 실행 권한 부여
chmod +x setup-oracle-vm.sh

# 실행 (sudo 필요)
sudo bash setup-oracle-vm.sh
```

스크립트가 자동으로 다음을 설치합니다:
- ✅ Java 17
- ✅ Apache Tomcat 9.0.105
- ✅ 방화벽 설정
- ✅ Systemd 서비스 등록

### 3-3. 설치 확인
```bash
# Tomcat 상태 확인
sudo systemctl status tomcat

# 로그 확인
sudo tail -f /opt/tomcat/logs/catalina.out
```

---

## 4️⃣ GitHub Secrets 설정

GitHub 리포지토리에 다음 Secrets를 추가해야 합니다:

1. **GitHub 리포지토리** 접속: https://github.com/Jeong-Ryeol/jpetstore-6
2. **Settings** > **Secrets and variables** > **Actions** 클릭
3. **New repository secret** 클릭하여 다음 추가:

| Secret 이름 | 설명 | 예시 |
|------------|------|------|
| `GEMINI_API_KEY` | ✅ 이미 추가됨 | `AIzaSy...` |
| `ORACLE_HOST` | VM의 공인 IP | `123.45.67.89` |
| `ORACLE_USER` | SSH 사용자 (보통 `opc`) | `opc` |
| `ORACLE_SSH_KEY` | SSH Private Key 전체 내용 | `-----BEGIN RSA PRIVATE KEY-----\n...` |

### ORACLE_SSH_KEY 추가 방법:
```bash
# 로컬 PC에서 SSH private key 내용 복사
cat ~/.ssh/oracle_key

# 출력된 전체 내용을 GitHub Secret에 붙여넣기
# -----BEGIN RSA PRIVATE KEY----- 부터
# -----END RSA PRIVATE KEY----- 까지 모두 포함
```

---

## 5️⃣ 배포 실행

### 5-1. 자동 배포 (GitHub Actions)
- `master` 브랜치에 코드를 푸시하면 자동으로 배포됩니다:

```bash
git add .
git commit -m "Deploy to Oracle Cloud"
git push origin master
```

### 5-2. 수동 배포
GitHub 리포지토리에서:
1. **Actions** 탭 클릭
2. **Deploy to Oracle Cloud** 워크플로우 선택
3. **Run workflow** 버튼 클릭
4. `master` 브랜치 선택 후 **Run workflow**

### 5-3. 배포 상태 확인
- **Actions** 탭에서 워크플로우 실행 상태 확인
- 로그에서 각 단계 확인 가능

---

## 6️⃣ 애플리케이션 접속

배포가 완료되면 브라우저에서 접속:

```
http://<PUBLIC_IP>:8080/jpetstore
```

예시:
```
http://123.45.67.89:8080/jpetstore
```

---

## 🔧 문제 해결

### 1. 애플리케이션에 접속이 안 되는 경우

**방화벽 확인:**
```bash
# VM에 SSH 접속 후
sudo firewall-cmd --list-all

# 8080 포트가 없으면 추가
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

**Oracle Cloud Security List 확인:**
- Oracle Cloud 콘솔에서 Ingress Rule에 8080 포트가 있는지 확인

**Tomcat 상태 확인:**
```bash
sudo systemctl status tomcat

# 로그 확인
sudo tail -100 /opt/tomcat/logs/catalina.out
```

### 2. 배포가 실패하는 경우

**GitHub Actions 로그 확인:**
- Actions 탭에서 실패한 워크플로우 클릭
- 각 단계의 로그 확인

**SSH 연결 문제:**
- `ORACLE_SSH_KEY` Secret이 올바르게 설정되었는지 확인
- SSH 키에 줄바꿈(`\n`)이 포함되어 있는지 확인

**권한 문제:**
```bash
# VM에서 Tomcat 디렉토리 권한 확인
ls -la /opt/tomcat/webapps/

# 권한 수정
sudo chown -R tomcat:tomcat /opt/tomcat/
```

### 3. AI 챗봇이 작동하지 않는 경우

**환경 변수 확인:**
```bash
# VM에서
cat /opt/tomcat/bin/setenv.sh

# GEMINI_API_KEY가 설정되어 있어야 함
```

**Tomcat 재시작:**
```bash
sudo systemctl restart tomcat
```

**로그 확인:**
```bash
sudo grep -i "gemini" /opt/tomcat/logs/catalina.out
```

---

## 📊 모니터링

### Tomcat 로그 실시간 확인
```bash
sudo tail -f /opt/tomcat/logs/catalina.out
```

### 시스템 리소스 확인
```bash
# CPU/메모리 사용량
top

# 디스크 사용량
df -h
```

### 애플리케이션 상태 확인
```bash
curl http://localhost:8080/jpetstore/
```

---

## 🔄 업데이트 배포

코드를 수정한 후:

```bash
git add .
git commit -m "Update feature"
git push origin master
```

GitHub Actions가 자동으로:
1. 코드 빌드
2. Oracle Cloud VM에 배포
3. Tomcat 재시작
4. Health Check 수행

---

## 📝 주요 명령어 요약

```bash
# Tomcat 관리
sudo systemctl start tomcat      # 시작
sudo systemctl stop tomcat       # 중지
sudo systemctl restart tomcat    # 재시작
sudo systemctl status tomcat     # 상태 확인

# 로그 확인
sudo tail -f /opt/tomcat/logs/catalina.out

# 애플리케이션 확인
curl http://localhost:8080/jpetstore/

# 방화벽 확인
sudo firewall-cmd --list-all
```

---

## 🎉 완료!

이제 JPetStore 애플리케이션이 Oracle Cloud에 배포되었습니다!

- **애플리케이션**: http://<PUBLIC_IP>:8080/jpetstore
- **AI 챗봇**: 우측 하단 💬 버튼
- **GitHub 리포지토리**: https://github.com/Jeong-Ryeol/jpetstore-6

문제가 발생하면 위의 **문제 해결** 섹션을 참고하세요.
