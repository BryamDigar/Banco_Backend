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
                    // Check if Docker is installed and in PATH
                    def dockerExists = sh(script: 'command -v docker', returnStatus: true) == 0
                    if (dockerExists) {
                        sh 'docker build -t joseph888/banco_backend .'
                    } else {
                        error "Docker is not installed or not in PATH. Please install Docker or use a Jenkins agent with Docker installed."
                    }
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    // Check if Docker is installed and in PATH
                    def dockerExists = sh(script: 'command -v docker', returnStatus: true) == 0
                    if (dockerExists) {
                        withCredentials([string(credentialsId: 'e5c4c4d1-3be2-4e55-a690-e3f45a37be72', variable: 'DOCKERHUB')]) {
                            sh "docker login -u joseph888 -p $DOCKERHUB"
                            sh 'docker push joseph888/banco_backend'
                        }
                    } else {
                        error "Docker is not installed or not in PATH. Please install Docker or use a Jenkins agent with Docker installed."
                    }
                }
            }
        }
    }
}