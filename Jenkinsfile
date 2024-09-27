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

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    withCredentials([string(credentialsId: 'jenkinSonar', variable: 'SONAR_TOKEN')]) {
                        sh "sonar-scanner -Dsonar.projectKey=mi_proyecto_sonar -Dsonar.sources=./src -Dsonar.java.binaries=./build -Dsonar.login=$SONAR_TOKEN"
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
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
                    withCredentials([string(credentialsId: 'DOCKERHUBPASSWORD', variable: 'DOCKERHUBPASSWORD')]) {
                        sh "docker login -u joseph888 -p $DOCKERHUBPASSWORD"
                        sh 'docker push joseph888/banco_backend'
                    }
                }
            }
        }
    }
}
