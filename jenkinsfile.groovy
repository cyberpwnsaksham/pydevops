pipeline {
    agent any
    environment {
        dockerImage = 'my-py-app'
        registry = 'bsaksham/pydevops:first'
        registryCredential = 'dockerhub'
    }
    stages {
        stage('Build py') {
            steps {
                catchError {
                        bat 'py app.py'
                }
            }
        }
        stage('Test py') {
            steps {
                script {
                    bat "py -m pytest"
                }
            }
        }
        stage('Building a Docker Image')
        {
            steps {
                script {
                    dockerImage = docker.build registry
                        }
                  }
        }
        stage('Pushing the image to HUB')
            {
                steps {
                    script {
                    docker.withRegistry('', registryCredential) 
                            {
                            dockerImage.push()
                            }
                        }
                    }

        }
        stage('Deploying to LocalHost')
            {
                steps {
                    bat 'docker run -d -p 9010:5000 bsaksham/pydevops:first'
                        }

        }
}
}
