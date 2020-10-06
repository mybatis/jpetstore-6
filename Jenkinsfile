
def kubelabel = "kubepod-${UUID.randomUUID().toString()}"
def zone
def kubenode
def volume
def pvc = "feature-maven-us-east-1b"
def branch 
def namespace = "cistack"

podTemplate(
    label: kubelabel,
    containers: [
        containerTemplate(name: 'kubectl', image: 'lachlanevenson/k8s-kubectl', ttyEnabled: true, command: '/bin/cat')
    ],
    serviceAccount: 'jenkins',
    nodeSelector: 'role=workers'
) {
    node(kubelabel) {
        stage('cache check') {

            container('kubectl'){
                //Get the node so we can get the availability zone
                kubenode=sh returnStdout: true, script: "kubectl get pod -o=custom-columns=NODE:.spec.nodeName,NAME:.metadata.name -n cistack | grep ${kubelabel} | sed -e 's/  .*//g'"
                kubenode=kubenode.trim()
                zone=sh returnStdout: true, script: "kubectl describe node \"${kubenode}\"| grep ProviderID | sed -e 's/.*aws:\\/\\/\\///g' | sed -e 's/\\/.*//g'"
                zone=zone.trim()
                branch=env.BRANCH_NAME
                // Sanitize the branch name so it can be made part of the pvc 
                branch=branch.replaceAll("/","-");
                pvc = "${branch}-${zone}"

                echo "I am checking for a maven cache for ${branch} in ${zone}" 
                // Create a pvc base on the AZ
                def claim = readYaml text:"""
apiVersion: v1
kind: PersistentVolumeClaim
metadata: 
  name: ${branch}-${zone}
  namespace: ${namespace}
  annotations:
    type: mavencache
spec:
  accessModes:
    - ReadWriteOnce
  storageClassName: ebs-sc
  resources:
    requests:
      storage: 4Gi
"""
                sh 'rm -rf dynamicclaim.yaml'
                writeYaml file: 'dynamicclaim.yaml', data: claim
                sh 'kubectl apply -f dynamicclaim.yaml'
            }
        }
    }
}


def label = "jpetstorepod-${UUID.randomUUID().toString()}"

podTemplate(
    label: label,
    containers: [
        containerTemplate(name: 'maven',
            image: 'cdelepine/mvn3.3.9-jdk8-node10',
            ttyEnabled: true,
            command: 'cat')
    ],
    volumes: [
        persistentVolumeClaim(mountPath: '/root/.m2/repository', claimName: pvc, readOnly: false)
    ],
    nodeSelector: 'role=workers'
) {
    node(label) {
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
        stage('Container') {
            container('maven') {
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

