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

        stage('SonarQube Analysis') {
            steps {
                script {
                    withSonarQubeEnv('SonarQube') {
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
