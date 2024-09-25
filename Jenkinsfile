pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/BryamDigar/Banco_Backend.git'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }
    }
}