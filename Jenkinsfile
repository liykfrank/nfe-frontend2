#!/usr/bin/env groovy

pipeline {

    agent any

    stages {
    	stage('Build') {
            steps { 
	                dir('bsplink-file-manager') {
	                    configFileProvider([configFile(fileId: 'gradle.properties', targetLocation: '.')]) {
    					    checkout([$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'dc8e2ba2-b5d9-4dad-acce-67e12bcb71be', url: 'http://gitlab.nfedev.accelya.com/bsplink/bsplink-file-manager.git']]])
						    sh 'pwd'
						    sh 'ls -lrt'
						    sh './gradlew clean build'
						    sh 'rm -rf build/'
					}
	          	}
            }
        }
    
        stage('Publish') {
            steps { 
                sh 'echo PUBLISH'
            }
        }

		stage('SonarQube') {
            steps {
            	withSonarQubeEnv('a2lsonarq1 - IATA') {
	            	sh './gradlew sonarqube'
	            }
	        }
	  	}
	}
}