def label = "jpetstorePod-${UUID.randomUUID().toString()}"

podTemplate(
    label: label,
    containers: [
        containerTemplate(name: 'maven',
            image: 'maven:3.6.3-jdk-8',
            ttyEnabled: true,
            command: 'cat')
        ]
) {
    node(label) {
        stage('Container') {
            container('maven') {
                stage('Clone') {
                    checkout(
                        [
                            $class                           : 'GitSCM',
                            branches                         : scm.branches,
                            doGenerateSubmoduleConfigurations: scm.doGenerateSubmoduleConfigurations,
                            extensions                       : scm.extensions,
                            submoduleCfg                     : [],
                            userRemoteConfigs                : scm.userRemoteConfigs
                        ]
                    )
                }
                configFileProvider([configFile(fileId: 'mavennexus', variable: 'MAVEN_CONFIG')]) {
                    stage('Compile') {
                        sh('mvn -s ${MAVEN_CONFIG} compile')
                    }
                    stage('Test') {
                        sh('mvn -s ${MAVEN_CONFIG} test')
                        junit '**/target/surefire-reports/TEST-*.xml'
                        jacoco(
                            execPattern: 'target/*.exec',
                            classPattern: 'target/classes',
                            sourcePattern: 'src/main/java',
                            exclusionPattern: 'src/test*'
                        )
                    }
                    stage ('SonarQube analysis') {
                        withSonarQubeEnv('sonarqube') {
                            sh 'mvn -s ${MAVEN_CONFIG} sonar:sonar'
                        }
                    }
                    stage ('SonarQube quality gate') {
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                    stage('Deploy') {
                        sh('mvn -s ${MAVEN_CONFIG} deploy -DskipITs')
                        archiveArtifacts artifacts: '**/target/*.war', onlyIfSuccessful: true
                    }
                }
            }
        }
    }
}
