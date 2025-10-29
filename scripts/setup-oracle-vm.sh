#!/bin/bash

# Oracle Cloud VM에서 실행할 초기 설정 스크립트
# 사용법:
#   1. Oracle Cloud VM에 SSH 접속
#   2. 이 스크립트를 복사하여 실행
#   3. sudo bash setup-oracle-vm.sh

set -e

echo "========================================="
echo "Oracle Cloud VM 초기 설정 시작"
echo "========================================="

# 시스템 업데이트
echo "📦 시스템 패키지 업데이트 중..."
sudo yum update -y

# Java 17 설치
echo "☕ Java 17 설치 중..."
sudo yum install -y java-17-openjdk java-17-openjdk-devel

# Java 버전 확인
java -version

# Tomcat 사용자 생성
echo "👤 Tomcat 사용자 생성 중..."
sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat || echo "Tomcat user already exists"

# Tomcat 9 다운로드 및 설치
TOMCAT_VERSION="9.0.105"
echo "🐱 Tomcat ${TOMCAT_VERSION} 다운로드 중..."

cd /tmp
wget https://dlcdn.apache.org/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz

# Tomcat 압축 해제
echo "📂 Tomcat 압축 해제 중..."
sudo tar xzvf apache-tomcat-${TOMCAT_VERSION}.tar.gz -C /opt/tomcat --strip-components=1

# 권한 설정
echo "🔐 권한 설정 중..."
sudo chown -R tomcat:tomcat /opt/tomcat/
sudo chmod +x /opt/tomcat/bin/*.sh

# Systemd 서비스 파일 생성
echo "⚙️  Systemd 서비스 설정 중..."
sudo tee /etc/systemd/system/tomcat.service > /dev/null <<EOF
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_BASE=/opt/tomcat"
Environment="CATALINA_OPTS=-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# Systemd reload
sudo systemctl daemon-reload

# Tomcat 시작 및 자동 시작 설정
echo "🚀 Tomcat 시작 중..."
sudo systemctl enable tomcat
sudo systemctl start tomcat

# 방화벽 설정 (Oracle Cloud는 보안 목록에서도 설정 필요)
echo "🔥 방화벽 설정 중..."
sudo firewall-cmd --permanent --add-port=8080/tcp || true
sudo firewall-cmd --reload || true

# iptables 설정 (Oracle Linux의 경우)
sudo iptables -I INPUT -p tcp --dport 8080 -j ACCEPT
sudo iptables-save | sudo tee /etc/iptables/rules.v4 > /dev/null

echo "========================================="
echo "✅ Oracle Cloud VM 설정 완료!"
echo "========================================="
echo ""
echo "다음 단계:"
echo "1. Oracle Cloud 콘솔에서 보안 목록 설정:"
echo "   - Networking > Virtual Cloud Networks"
echo "   - VCN 선택 > Security Lists"
echo "   - Ingress Rules 추가: 0.0.0.0/0, TCP, 8080"
echo ""
echo "2. GitHub Secrets에 다음 추가:"
echo "   - ORACLE_HOST: $(curl -s ifconfig.me)"
echo "   - ORACLE_USER: $(whoami)"
echo "   - ORACLE_SSH_KEY: (SSH private key 내용)"
echo "   - GEMINI_API_KEY: (이미 추가됨)"
echo ""
echo "3. Tomcat 상태 확인:"
echo "   sudo systemctl status tomcat"
echo ""
echo "4. 로그 확인:"
echo "   sudo tail -f /opt/tomcat/logs/catalina.out"