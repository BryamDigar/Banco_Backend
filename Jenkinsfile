pipeline {
    agent any

    environment {
        // Define variables para la imagen Docker y el Docker registry si es necesario
        DOCKER_IMAGE = 'banco_backend'
    }
    
     stage('Initialize'){
        def dockerHome = tool 'myDocker'
        env.PATH = "${dockerHome}/bin:${env.PATH}"
    }
    stages {
        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Construye la imagen Docker utilizando el bloque docker.build
                    def customImage = docker.build(DOCKER_IMAGE)
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
