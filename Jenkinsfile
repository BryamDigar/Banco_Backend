pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'banco_backend'
    }

    stages {
        stage('Initialize') {
            steps {
                script {
                    def dockerHome = tool 'Docker'
                    env.PATH = "${dockerHome}/bin:${env.PATH}"
                }
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                        // Construir la imagen Docker usando el Dockerfile en el directorio actual
                        sh 'sudo docker build -t banco_backend .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'DOCKERHUBPASSWORD', variable: 'DOCKERHUBPASSWORD')]) {
                        sh "sudo docker login -u joseph888 -p $DOCKERHUBPASSWORD"
                        sh 'sudo docker push joseph888/banco_backend'
                    }
                }
            }
        }
    }
}
