pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                // Clona el repositorio en Jenkins
                git 'https://github.com/BryamDigar/Banco_Backend.git'
            }
        }
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Docker Version') {
            steps {
                script {
                    sh 'docker --version'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t banco_backend .'
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
