pipeline {
    agent any 
    stages {
        stage('compile stage') { 
            steps {
             git 'https://github.com/mybatis/jpetstore-6.git'
            sh 'mvn clean compile'
             }
             
            }

        stage('testing stage') { 
            steps {
            sh 'mvn  test'
             }
            
            }
            
        stage('Deployment stage') { 
            steps {
            sh 'mvn  deploy'
            }
        }
    }
}
