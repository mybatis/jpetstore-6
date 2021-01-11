
// Only one person can use the cache at a time.
properties([disableConcurrentBuilds()])

// Name of the pods
def kubelabel = "kubepod-${UUID.randomUUID().toString()}"
def zone                   // The AZ in AWS we are in
def kubenode               //The name of the kube node we are on
def pvc                    // Name of the PVC for this branch
def branch                 // Branch name
def namespace = "cistack"  // Namespace pods execute in

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
                branch=branch.replaceAll("/","-").replaceAll("[^-.a-zA-Z0-9]","").take(62-zone.length().toLowerCase();
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
    purpose: mavencache
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
            withCredentials([usernamePassword(credentialsId: 'contrast-security-ce', passwordVariable: 'CONTRAST_SERVICEKEY', usernameVariable: 'CONTRAST_USERNAME'),
                            usernamePassword(credentialsId: 'contrast-security-org', passwordVariable: 'CONTRAST_APIKEY', usernameVariable: 'CONTRAST_ORGUUID')]) {
                container('maven') {
                    configFileProvider([configFile(fileId: 'mavennexus', variable: 'MAVEN_CONFIG')]) {
                        stage('Compile') {
                            sh('mvn -s ${MAVEN_CONFIG} -P tomcat90,with-contrast -Dcontrast.username=${CONTRAST_USERNAME} -Dcontrast.serviceKey=${CONTRAST_SERVICEKEY} -Dcontrast.apiKey=${CONTRAST_APIKEY} -Dcontrast.orgUuid=${CONTRAST_ORGUUID}  compile')
                        }
                        stage('Test') {
                            sh('mvn -s ${MAVEN_CONFIG} -P tomcat90,with-contrast -Dcontrast.username=${CONTRAST_USERNAME} -Dcontrast.serviceKey=${CONTRAST_SERVICEKEY} -Dcontrast.apiKey=${CONTRAST_APIKEY} -Dcontrast.orgUuid=${CONTRAST_ORGUUID} test')
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
                                sh 'mvn -s ${MAVEN_CONFIG} -P tomcat90,with-contrast -Dcontrast.username=${CONTRAST_USERNAME} -Dcontrast.serviceKey=${CONTRAST_SERVICEKEY} -Dcontrast.apiKey=${CONTRAST_APIKEY} -Dcontrast.orgUuid=${CONTRAST_ORGUUID} sonar:sonar'
                            }
                        }
                        stage ('SonarQube quality gate') {
                            timeout(time: 10, unit: 'MINUTES') {
                                waitForQualityGate abortPipeline: true
                            }
                        }
                        stage('Deploy') {
                            sh('mvn -s ${MAVEN_CONFIG} -P tomcat90,with-contrast -Dcontrast.username=${CONTRAST_USERNAME} -Dcontrast.serviceKey=${CONTRAST_SERVICEKEY} -Dcontrast.apiKey=${CONTRAST_APIKEY} -Dcontrast.orgUuid=${CONTRAST_ORGUUID} deploy -DskipITs')
                            archiveArtifacts artifacts: '**/target/dependency-check-report.*', onlyIfSuccessful: false
                            archiveArtifacts artifacts: '**/target/*.war', onlyIfSuccessful: true
                        }
                    }
                }
            }
        }
    }
}

