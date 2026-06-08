pipeline {
    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t gateway-api:latest .'
            }
        }

        stage('Run Container') {
            steps {
                bat 'docker run -d -p 8081:8080 gateway-api:latest'
            }
        }
    }
}