//
def kubelabel = "kubepod-${UUID.randomUUID().toString()}"
def zone
def kubenode
def volume
def pvc = "feature-maven-us-east-1b"
def branch 

podTemplate(
    label: kubelabel,
    containers: [
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', ttyEnabled: true, command: '/bin/cat')
    ],
    serviceAccount: 'jenkins'
) {
    node(kubelabel) {
        stage('cache check') {
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

            container('kubectl'){
                kubenode=sh returnStdout: true, script: "kubectl get pod -o=custom-columns=NODE:.spec.nodeName,NAME:.metadata.name -n cistack | grep ${kubelabel} | sed -e 's/  .*//g'"
                kubenode=kubenode.trim()
                echo "${kubenode}"
                zone=sh returnStdout: true, script: "kubectl describe node \"${kubenode}\"| grep ProviderID | sed -e 's/.*aws:\\/\\/\\///g' | sed -e 's/\\/.*//g'"
                zone=zone.trim()
                echo "${zone}"
                branch=env.BRANCH_NAME
                // Sanitize the branch name so it can be made part of the pvc 
                branch=branch.replaceAll("/","-");
                echo "BRANCH: ${branch}"
                echo "${branch}-${zone}"
                def claim=readYaml file: "kube/claim.yaml"
                claim.metadata.name = "${branch}-${zone}"
                writeYaml file: 'kube/dynamicclaim.yaml', data: claim
                sh 'cat kube/dynamicclaim.yaml'
            }
        }
    }
}



//def label = "jpetstorepod-${UUID.randomUUID().toString()}"
//
//podTemplate(
//    label: label,
//    containers: [
//        containerTemplate(name: 'maven',
//            image: 'maven:3.6.3-jdk-8',
//            ttyEnabled: true,
//            command: 'cat')
//    ],
//    volumes: [
//        persistentVolumeClaim(mountPath: '/root/.m2/repository', claimName: pvc, readOnly: false)
//    ]
//) {
//    node(label) {
//        stage('Container') {
//            container('maven') {
//                configFileProvider([configFile(fileId: 'mavennexus', variable: 'MAVEN_CONFIG')]) {
//                    stage('Compile') {
//                        sh('mvn -s ${MAVEN_CONFIG} compile')
//                    }
//                    stage('Test') {
//                        sh('mvn -s ${MAVEN_CONFIG} test')
//                        junit '**/target/surefire-reports/TEST-*.xml'
//                        jacoco(
//                            execPattern: 'target/*.exec',
//                            classPattern: 'target/classes',
//                            sourcePattern: 'src/main/java',
//                            exclusionPattern: 'src/test*'
//                        )
//                    }
//                    stage ('SonarQube analysis') {
//                        withSonarQubeEnv('sonarqube') {
//                            sh 'mvn -s ${MAVEN_CONFIG} sonar:sonar'
//                        }
//                    }
//                    stage ('SonarQube quality gate') {
//                        timeout(time: 10, unit: 'MINUTES') {
//                            waitForQualityGate abortPipeline: true
//                        }
//                    }
//                    stage('Deploy') {
//                        sh('mvn -s ${MAVEN_CONFIG} deploy -DskipITs')
//                        archiveArtifacts artifacts: '**/target/*.war', onlyIfSuccessful: true
//                    }
//                }
//            }
//        }
//    }
//}
//
