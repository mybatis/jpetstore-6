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
                withSonarQubeEnv(credentialsId: 'sonar-cred', installationName: 'sonar-server') {
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
    
        stage ('Clean WC') {
             steps {
                 cleanWs()    
             }
        }
        
    }
}
