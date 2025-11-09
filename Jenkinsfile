pipeline {
    agent any

    tools {
        jdk 'JDK25'
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/kumarujjawal05/Java_Automation_Framework.git'
            }
        }

        stage('Build') {
            steps {
                echo "Building project..."
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running tests on Chrome in headless mode..."
                //  Corrected Maven syntax — no need for -DargLine
                sh 'mvn test -Dbrowser=chrome -Dheadless=true'
            }
        }

        stage('Publish Reports') {
            steps {
                echo "Publishing Surefire XML report..."
                testng '**/target/surefire-reports/*.xml'
            }
        }
    }

    post {
        always {
            echo "Publishing Extent HTML Report..."
            publishHTML([
                reportDir: 'Reports',
                reportFiles: 'extent-report.html',
                reportName: 'Extent Report',
                keepAll: true,
                alwaysLinkToLastBuild: true
            ])
        }
    }
}
