# 🚀 Oracle Cloud 배포 빠른 시작 가이드

## ✅ 완료된 작업

1. ✅ GitHub 리포지토리 설정 완료
2. ✅ GitHub Actions 워크플로우 생성 완료
3. ✅ Oracle VM 설정 스크립트 생성 완료
4. ✅ GEMINI_API_KEY 이미 GitHub Secrets에 추가됨
5. ✅ 코드 푸시 완료

---

## 📝 남은 작업 (순서대로 진행)

### 1단계: Oracle Cloud VM 생성 (약 10분)

1. https://cloud.oracle.com 로그인
2. **Compute** > **Instances** > **Create Instance**
3. 설정:
   - Name: `jpetstore-server`
   - Image: `Oracle Linux 8`
   - Shape: `VM.Standard.E2.1.Micro` (Always Free)
   - SSH keys: 새로 생성 또는 업로드
   - ⚠️ **중요**: Private Key 저장!
4. Create 클릭
5. **Public IP** 복사 (예: `123.45.67.89`)

---

### 2단계: 방화벽 설정 (약 3분)

1. Oracle Cloud 콘솔:
   - **Networking** > **Virtual Cloud Networks**
   - VCN 선택 > **Security Lists**
   - Default Security List 선택

2. **Add Ingress Rules**:
   - Source CIDR: `0.0.0.0/0`
   - IP Protocol: `TCP`
   - Destination Port: `8080`
   - Add 클릭

---

### 3단계: VM 초기 설정 (약 5분)

**SSH 접속:**
```bash
ssh -i /path/to/your-key opc@<PUBLIC_IP>
```

**설정 스크립트 실행:**
```bash
# 스크립트 다운로드
curl -o setup.sh https://raw.githubusercontent.com/Jeong-Ryeol/jpetstore-6/master/scripts/setup-oracle-vm.sh

# 실행
chmod +x setup.sh
sudo bash setup.sh
```

스크립트가 자동으로 설치:
- Java 17
- Tomcat 9
- 방화벽 설정
- Systemd 서비스

---

### 4단계: GitHub Secrets 추가 (약 3분)

https://github.com/Jeong-Ryeol/jpetstore-6/settings/secrets/actions

**New repository secret** 클릭하여 추가:

#### 1. ORACLE_HOST
- Value: VM의 Public IP (예: `123.45.67.89`)

#### 2. ORACLE_USER
- Value: `opc`

#### 3. ORACLE_SSH_KEY
```bash
# 로컬에서 SSH key 내용 복사
cat ~/.ssh/oracle_key

# 출력된 전체 내용을 GitHub Secret에 붙여넣기
# -----BEGIN ... 부터 -----END ... 까지 전부
```

#### 4. GEMINI_API_KEY
- ✅ 이미 추가되어 있음

---

### 5단계: 배포 실행 (자동)

GitHub에서 자동 배포:

**방법 1: 코드 푸시 (자동 트리거)**
```bash
git push origin master
```

**방법 2: 수동 실행**
1. https://github.com/Jeong-Ryeol/jpetstore-6/actions
2. **Deploy to Oracle Cloud** 선택
3. **Run workflow** 클릭

---

### 6단계: 접속 확인

브라우저에서:
```
http://<PUBLIC_IP>:8080/jpetstore
```

예시:
```
http://123.45.67.89:8080/jpetstore
```

---

## 🎯 필수 체크리스트

배포 전 확인:

- [ ] Oracle Cloud VM 생성 완료
- [ ] Public IP 확인
- [ ] Security List에 8080 포트 추가
- [ ] VM에서 설정 스크립트 실행 완료
- [ ] `sudo systemctl status tomcat` 정상 작동 확인
- [ ] GitHub Secrets 4개 모두 추가:
  - [ ] GEMINI_API_KEY ✅
  - [ ] ORACLE_HOST
  - [ ] ORACLE_USER
  - [ ] ORACLE_SSH_KEY

---

## 🔧 문제 해결

### 접속이 안 되는 경우

**1. 방화벽 확인 (VM 내부):**
```bash
sudo firewall-cmd --list-all
sudo firewall-cmd --permanent --add-port=8080/tcp
sudo firewall-cmd --reload
```

**2. Oracle Cloud 보안 규칙 확인:**
- Security List에 8080 Ingress Rule 있는지 확인

**3. Tomcat 상태:**
```bash
sudo systemctl status tomcat
sudo tail -100 /opt/tomcat/logs/catalina.out
```

### GitHub Actions 실패

**로그 확인:**
- https://github.com/Jeong-Ryeol/jpetstore-6/actions
- 실패한 워크플로우 클릭하여 에러 확인

**SSH 연결 실패:**
- ORACLE_SSH_KEY가 정확한지 확인
- Private key 전체 내용이 포함되었는지 확인

---

## 📚 상세 문서

자세한 내용은 다음 문서를 참고하세요:
- [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) - 전체 배포 가이드
- [AI_SETUP_GUIDE.txt](AI_SETUP_GUIDE.txt) - AI 기능 설정
- [.github/workflows/deploy.yml](.github/workflows/deploy.yml) - 배포 워크플로우

---

## 📞 도움말

- **GitHub 리포지토리**: https://github.com/Jeong-Ryeol/jpetstore-6
- **Oracle Cloud 문서**: https://docs.oracle.com/en-us/iaas/
- **Tomcat 문서**: https://tomcat.apache.org/tomcat-9.0-doc/

---

## ⏱️ 예상 소요 시간

| 단계 | 소요 시간 |
|-----|----------|
| VM 생성 | 10분 |
| 방화벽 설정 | 3분 |
| VM 초기 설정 | 5분 |
| GitHub Secrets 추가 | 3분 |
| 배포 실행 | 3분 (자동) |
| **총합** | **약 25분** |

---

## 🎉 성공!

배포가 완료되면:
- ✅ 애플리케이션: http://<PUBLIC_IP>:8080/jpetstore
- ✅ AI 챗봇 작동 (우측 하단 💬)
- ✅ AI 상품 추천 작동 (상품 상세 페이지)
- ✅ GitHub Actions 자동 배포 설정 완료

모든 코드 변경은 `git push`만으로 자동 배포됩니다!
