pipeline {
    agent any

    tools {
        maven 'Maven 3.8.6'
        jdk 'Java 17'
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Backend Tests') {
            parallel {
                stage('Auth Service Tests') {
                    steps {
                        dir('backend/auth-service') {
                            bat 'mvn -B clean test'
                        }
                    }
                }
                stage('Employee Service Tests') {
                    steps {
                        dir('backend/employee-service') {
                            bat 'mvn -B clean test'
                        }
                    }
                }
                stage('Task Service Tests') {
                    steps {
                        dir('backend/task-service') {
                            bat 'mvn -B clean test'
                        }
                    }
                }
            }
            post {
                always {
                    echo 'Collecting backend test reports...'
                    junit 'backend/*/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Backend JARs') {
            parallel {
                stage('Auth Package') {
                    steps {
                        dir('backend/auth-service') {
                            bat 'mvn -B clean package -DskipTests'
                        }
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'backend/auth-service/target/*.jar', fingerprint: true
                        }
                    }
                }
                stage('Employee Package') {
                    steps {
                        dir('backend/employee-service') {
                            bat 'mvn -B clean package -DskipTests'
                        }
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'backend/employee-service/target/*.jar', fingerprint: true
                        }
                    }
                }
                stage('Task Package') {
                    steps {
                        dir('backend/task-service') {
                            bat 'mvn -B clean package -DskipTests'
                        }
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'backend/task-service/target/*.jar', fingerprint: true
                        }
                    }
                }
            }
        }

        stage('Frontend (Optional Skip)') {
            steps {
                echo 'Skipping frontend build - NodeJS setup not yet configured.'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully. All backend tests passed and artifacts packaged.'
        }
        failure {
            echo 'Pipeline failed. Check logs for Maven or test errors.'
        }
    }
}
