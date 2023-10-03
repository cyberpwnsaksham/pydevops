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
                script {
                        bat 'start /b python app.py'
                }
            }
        }
        stage('Test py') {
            steps {
                script {
                    bat "python -m unittest app.py"
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
