pipeline {
    agent any
    tools {
        jdk 'jdk11'
        maven 'maven3.6'
    }

    stages {
        
        stage('Git Checkout') {
            steps {
                git changelog: false, credentialsId: 'Jenkins-Cred', poll: false, url: 'https://github.com/Reddy999eee/jpetstore-6.git'
            }
        }
        stage ('Compile') {
            steps {
                sh "mvn clean compile"
            }
        }
        stage ('SonarQuality Check') {
            steps {
                withSonarQubeEnv(credentialsId: 'sonar-cred', installationName: 'Sonar-server') {
                    sh "mvn sonar:sonar"
                }
            }
        }
        stage ('OWASP Scan') {
            steps {
                dependencyCheck additionalArguments: ' --scan ./ ', odcInstallation: 'DP-check'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage ('Docker Build & Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: '4575f0b5-6c6a-4d0c-90b2-1973f3a8b3d0', toolName: 'docker') {
                        sh "docker built -t pet-store ."
                        sh "docker tag pet-store nsivareddy/pet-store:latest"
                        sh "docker push nsivareddy/pet-store:latest"
                    }
                }
            }
        }
        stage ('Clean WC') {
             steps {
                
                  cleanWS()
                
             }
        }
        
    }
}
