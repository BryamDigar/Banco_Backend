pipeline {
    agent any

    triggers {
        githubPush() 
    }

    environment {
        DOCKER_IMAGE = 'banco_backend'
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Usar la referencia del Pull Request
                    def changeBranch = env.CHANGE_BRANCH ?: 'master' // o tu rama por defecto
                    git branch: changeBranch, url: 'https://github.com/BryamDigar/Banco_Backend.git'
                }
            }
        }
        
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
                    sh 'docker build -t joseph888/banco_backend:latest .'
                }
            }
        }

        stage('Trivy Scan'){
            steps{
                script{
                    sh 'docker run --rm -v "/var/jenkins_home/workspace/Ic test:/root/.cache/" aquasec/trivy:latest -q image --severity CRITICAL --light joseph888/banco_backend:latest'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'DOCKERHUBPASSWORD', variable: 'DOCKERHUBPASSWORD')]) {
                        sh "docker login -u joseph888 -p $DOCKERHUBPASSWORD"
                        sh 'docker push joseph888/banco_backend:latest'
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('sonarqube') {
                        withCredentials([string(credentialsId: 'JENKINSONARURL', variable: 'SONAR_URL'),
                                         string(credentialsId: 'JENKINSONAR', variable: 'SONAR_TOKEN')]) {
                            // Guardar los secretos en variables de entorno y luego ejecutar el script
                            sh '''
                                ./gradlew sonarqube \
                                -Dsonar.projectKey=banco_backend \
                                -Dsonar.host.url=$SONAR_URL \
                                -Dsonar.login=$SONAR_TOKEN
                            '''
                        }
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
    }
}
