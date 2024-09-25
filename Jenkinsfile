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
                    // Verificar si Docker está instalado y en PATH
                    def dockerExists = sh(script: 'command -v docker', returnStatus: true) == 0
                    if (dockerExists) {
                        // Construir la imagen Docker usando el Dockerfile en el directorio actual
                        sh 'docker build -t joseph888/banco_backend -f Dockerfile .'
                    } else {
                        error "Docker no está instalado o no está en PATH. Por favor, instala Docker o usa un agente de Jenkins con Docker instalado."
                    }
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                script {
                    // Verificar si Docker está instalado y en PATH
                    def dockerExists = sh(script: 'command -v docker', returnStatus: true) == 0
                    if (dockerExists) {
                        withCredentials([string(credentialsId: 'e5c4c4d1-3be2-4e55-a690-e3f45a37be72', variable: 'DOCKERHUB')]) {
                            sh "docker login -u joseph888 -p $DOCKERHUB"
                            sh 'docker push joseph888/banco_backend'
                        }
                    } else {
                        error "Docker no está instalado o no está en PATH. Por favor, instala Docker o usa un agente de Jenkins con Docker instalado."
                    }
                }
            }
        }
    }
}