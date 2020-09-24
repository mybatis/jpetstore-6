podTemplate(
    label: 'maven',
    containers: [
        containerTemplate(name: 'maven',
            image: 'maven:3.6.3-jdk-8',
            ttyEnabled: true,
            command: 'cat')
        ]
) {
    node('maven') {
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
                stage('Compile') {
                    sh('mvn compile')
                }
                stage('Test') {
                    sh('mvn test')
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
                stage('Verify') {
                    sh('mvn verify -DskipITs')
                    archiveArtifacts artifacts: '**/target/*.war', onlyIfSuccessful: true
                }
            }
        }
    }
}
