pipeline {
    agent {
        label "lab"
    }

    parameters {
        string(name: 'KEY', defaultValue: '', description: 'enterprise license key')
    }
    stages {
        stage('Check Docker') {
            steps {
                // we may add version check here
                sh "docker version"
                sh "docker-compose version"
                sh "export ENTERPRISE_KEY=${params.KEY}"
            }
        }
        stage('Build Server Image') {
            steps {
                git changelog: false, poll: false, url: 'https://github.com/lazerion/hz-tls-verification'
                dir("server") {
                    script {
                        server = docker.build("server:tls")
                    }
                }
            }
        }
        stage('Build Client Image') {
            steps {
                dir("client") {
                    script {
                        sh "mvn clean package docker:build"
                    }
                }
            }
        }
        stage('Install Custom Verifier') {
            steps {
                dir("verifiers") {
                    script {
                        sh "mvn clean install"
                    }
                }
            }
        }
        stage('Acceptance') {
            steps {
                dir("runner")
                script {
                    sh "mvn test"
                }
            }
        }
    }
    post {
        always {
            sh "unset ENTERPRISE_KEY"
            cleanWs deleteDirs: true
            retry(3) {
                script {
                    sleep 5
                    sh "docker rmi ${server.id}"
                }
            }
            retry(3) {
                dir("client")
                script {
                    sh "mvn docker:remove"
                }
            }
        }
        failure {
            mail to: 'baris@hazelcast.com',
                    subject: "Failed Pipeline: ${currentBuild.fullDisplayName}",
                    body: "Something is wrong with ${env.BUILD_URL}"
        }
    }
}
