pipeline {
    agent any

    triggers {
        // Schedule the job to run daily at 2 AM
        cron('H 2 * * *')
    }
    
    // Define the tools to be used in the pipeline
    tools {
        jdk 'JDK17'
        maven 'Maven3'
    }
}

stages{
    stage('Checkout') {
        steps {
            // Checkout the code from the repository
            echo 'Checking out the code...'
            checkout scm
        }
    }
    stage('Build') {
        stpes {
            // Clean and package the application
            echo 'Building the application...'
            sh 'mvn clean package'
        }
    }

    stage('Test') {
        steps {
            // Run tests and generate Surefire report
            echo 'Running tests and generating Surefire report...'
            sh 'mvn test surefire-report:report'
        }
    }

    stage('Deploy') {
        steps {
            echo 'Building Docker image...'
            // Build Docker image
            sh 'docker build -t jpetstore-6:${BUILD_NUMBER} .'

            echo 'Deploying Docker container...'
            //Stop and remove any existing container
            sh 'docker stop jpetstore-6 || true'
            sh 'docker rm jpetstore-6 || true'

            //Run the new container
            sh 'docker run -d -p  8080:8080 -- name jpetstore-6 jpetstore-6:${BUILD_NUMBER}'
        }
    }

    post {
        always {
            publishHTML ([
                reportDir: 'target/site',
                reportFiles: 'surefire-report.html',
                reportName: 'Surefire HTML Test Report',
            ])
        }
        success {
            echo 'Build successful! The application has been deployed.'
        }
        failure {
            echo 'Build failed! Please check the logs for details.'
        }
    }
}