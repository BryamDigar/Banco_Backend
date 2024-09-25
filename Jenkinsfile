pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t joseph888/banco_backend .'
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'e5c4c4d1-3be2-4e55-a690-e3f45a37be72', variable: 'DOCKERHUB')]) {
                        sh "docker login -u joseph888 -p $DOCKERHUB"
                        sh 'docker push joseph888/banco_backend'
                    }
                }
            }
        }
    }
}