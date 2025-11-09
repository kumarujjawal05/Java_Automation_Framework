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
                sh 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test -Dbrowser=chrome -Dheadless=true -DargLine="-Dheadless=true"'
            }
        }

        stage('Publish Report') {
            steps {
                junit '**/target/surefire-reports/*.xml'
            }
        }
    }
}
