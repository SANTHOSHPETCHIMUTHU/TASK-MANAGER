pipeline {
    // 1. Define Agent
    // Use 'any' for simplicity. You can change to a specific agent label later.
    agent any

    // 2. Define Tools
    // IMPORTANT: You must configure these tools in Jenkins
    // Go to "Manage Jenkins" > "Global Tool Configuration"
    tools {
        maven 'Maven 3.8.6' // Must match the name you gave it in Jenkins
        jdk 'Java 17'       // Must match the name you gave it in Jenkins
        nodejs 'NodeJS 18'  // Must match the name you gave it in Jenkins
    }

    stages {
        // 3. Checkout Code
        stage('Checkout') {
            steps {
                // This will check out the code from the branch Jenkins is building
                checkout scm
            }
        }

        // 4. Run All Backend Tests in Parallel
        stage('Backend Tests') {
            parallel {
                stage('Test: Auth Service') {
                    steps {
                        dir('backend/auth-service') {
                            sh 'mvn -B clean test'
                        }
                    }
                }
                stage('Test: Employee Service') {
                    steps {
                        dir('backend/employee-service') {
                            sh 'mvn -B clean test'
                        }
                    }
                }
                stage('Test: Task Service') {
                    steps {
                        dir('backend/task-service') {
                            sh 'mvn -B clean test'
                        }
                    }
                }
            }
            // 5. Publish Test Results
            // This is what ma'am asked about. It collects all XML reports.
            post {
                always {
                    echo 'Archiving JUnit test results...'
                    // This glob pattern finds reports in all 3 microservices
                    junit 'backend/*/target/surefire-reports/*.xml'
                }
            }
        }

        // 6. Run Frontend Test & Build
        stage('Frontend Test & Build') {
            steps {
                dir('frontend/task-tracker-ui') {
                    sh 'npm install'
                    sh 'npm test' // This runs your frontend tests (add them!)
                    sh 'npm run build' // This builds the React app for production
                }
            }
            post {
                success {
                    echo 'Archiving frontend build artifacts...'
                    archiveArtifacts artifacts: 'frontend/task-tracker-ui/dist/**', fingerprint: true
                }
            }
        }

        // 7. Build Backend JARs
        // This stage only runs if all tests passed
        stage('Backend Package') {
            parallel {
                stage('Package: Auth') {
                    steps {
                        dir('backend/auth-service') {
                            sh 'mvn -B clean package -DskipTests'
                        }
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'backend/auth-service/target/*.jar', fingerprint: true
                        }
                    }
                }
                stage('Package: Employee') {
                    steps {
                        dir('backend/employee-service') {
                            sh 'mvn -B clean package -DskipTests'
                        }
                    }
                    post {
                        success {
                            archiveArtifacts artifacts: 'backend/employee-service/target/*.jar', fingerprint: true
                        }
                    }
                }
                stage('Package: Task') {
                    steps {
                        dir('backend/task-service') {
                            sh 'mvn -B clean package -DskipTests'
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

        // 8. Build Docker Images (Optional, for demo)
        // This only runs on the 'main' branch
        stage('Build Docker Images') {
            when {
                branch 'main'
            }
            // Assumes Dockerfile is in each service's root
            parallel {
                stage('Docker: Auth') {
                    steps {
                        dir('backend/auth-service') {
                            sh 'docker build -t santhoshpetchimuthu/auth-service:latest .'
                        }
                    }
                }
                stage('Docker: Employee') {
                    steps {
                        dir('backend/employee-service') {
                            sh 'docker build -t santhoshpetchimuthu/employee-service:latest .'
                        }
                    }
                }
                stage('Docker: Task') {
                    steps {
                        dir('backend/task-service') {
                            sh 'docker build -t santhoshpetchimuthu/task-service:latest .'
                        }
                    }
                }
            }
        }
    }
}
