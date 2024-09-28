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
                sh './gasdsdasdasfradlew build'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                        // Construir la imagen Docker usando el Dockerfile en el directorio actual
                        sh 'docker build -t joseph888/banco_backend .'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'DOCKERHUBPASSWORD', variable: 'DOCKERHUBPASSWORD')]) {
                        sh "docker login -u joseph888 -p $DOCKERHUBPASSWORD"
                        sh 'docker push joseph888/banco_backend'
                    }
                }
            }
        }
    }
}
