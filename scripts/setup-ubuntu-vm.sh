#!/bin/bash

# Ubuntu VM에서 실행할 초기 설정 스크립트
# 사용법:
#   curl -s https://raw.githubusercontent.com/Jeong-Ryeol/jpetstore-6/master/scripts/setup-ubuntu-vm.sh | sudo bash

set -e

echo "========================================="
echo "Ubuntu VM 초기 설정 시작"
echo "========================================="

# 시스템 업데이트 (간단하게)
echo "📦 패키지 목록 업데이트 중..."
apt-get update -qq

# Java 17 설치
echo "☕ Java 17 설치 중..."
apt-get install -y openjdk-17-jdk wget

# Java 버전 확인
java -version

# Tomcat 사용자 생성
echo "👤 Tomcat 사용자 생성 중..."
useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat 2>/dev/null || echo "Tomcat user already exists"

# Tomcat 디렉토리 생성
mkdir -p /opt/tomcat

# Tomcat 9 다운로드
TOMCAT_VERSION="9.0.105"
echo "🐱 Tomcat ${TOMCAT_VERSION} 다운로드 중..."
cd /tmp
wget -q https://dlcdn.apache.org/tomcat/tomcat-9/v${TOMCAT_VERSION}/bin/apache-tomcat-${TOMCAT_VERSION}.tar.gz

# Tomcat 압축 해제
echo "📂 Tomcat 설치 중..."
tar xzf apache-tomcat-${TOMCAT_VERSION}.tar.gz -C /opt/tomcat --strip-components=1

# 권한 설정
echo "🔐 권한 설정 중..."
chown -R tomcat:tomcat /opt/tomcat/
chmod +x /opt/tomcat/bin/*.sh

# Systemd 서비스 파일 생성
echo "⚙️  Systemd 서비스 설정 중..."
tee /etc/systemd/system/tomcat.service > /dev/null <<EOF
[Unit]
Description=Apache Tomcat Web Application Container
After=network.target

[Service]
Type=forking

User=tomcat
Group=tomcat

Environment="JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64"
Environment="CATALINA_HOME=/opt/tomcat"
Environment="CATALINA_BASE=/opt/tomcat"
Environment="CATALINA_OPTS=-Xms256M -Xmx512M -server"

ExecStart=/opt/tomcat/bin/startup.sh
ExecStop=/opt/tomcat/bin/shutdown.sh

Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target
EOF

# Systemd reload
systemctl daemon-reload

# Tomcat 시작
echo "🚀 Tomcat 시작 중..."
systemctl enable tomcat
systemctl start tomcat

# UFW 방화벽 설정 (Ubuntu용)
echo "🔥 방화벽 설정 중..."
ufw allow 8080/tcp 2>/dev/null || true

# 상태 확인
sleep 5
systemctl status tomcat --no-pager || true

echo "========================================="
echo "✅ Ubuntu VM 설정 완료!"
echo "========================================="
echo ""
echo "다음 단계:"
echo "1. Oracle Cloud 콘솔에서 보안 목록 설정:"
echo "   - Ingress Rules 추가: 0.0.0.0/0, TCP, 8080"
echo ""
echo "2. GitHub Secrets 업데이트:"
echo "   - ORACLE_HOST: $(curl -s ifconfig.me)"
echo ""
echo "3. Tomcat 상태 확인:"
echo "   sudo systemctl status tomcat"
echo ""
echo "4. 로그 확인:"
echo "   sudo tail -f /opt/tomcat/logs/catalina.out"
