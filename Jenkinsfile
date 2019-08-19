pipeline {
agent any
stages {
stage('One'){
steps{
echo 'this is pipeline' }
}
stage('two'){
steps{
input('do you want to proceed?')
}
}
stage('three') {
when {
not {
branch "master"  
}
}
steps {
echo 'Hello'
}
}
}
}
