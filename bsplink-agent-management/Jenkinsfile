#!/usr/bin/env groovy

env.sourceBranch = env.gitlabSourceBranch ?: 'develop'
env.gitlabRepositoryUrl = env.gitlabSourceRepoHttpUrl ?: 'http://a2lgit1/bsplink/bsplink-agent-management.git'

echo "[INFO] Source Branch: ${env.sourceBranch}"
echo "[INFO] Source Repository URL: ${env.gitlabRepositoryUrl}"

// add source branch to current build description
currentBuild.description = (currentBuild.description ?: 'Started directly through Jenkins') + "\nSource Branch: ${sourceBranch}"

pipeline {

    agent {label 'master'}

	//configuration for returning the pipeline execution status to Gitlab
	options {
        gitLabConnection('A2LGIT1')
        gitlabCommitStatus(name: 'jenkins')
    }

    stages {
        stage('Build') {
            steps {
		    	checkout poll: false, scm: [
					$class: 'GitSCM',
					branches: [[name: "*/${env.sourceBranch}" ]],
					doGenerateSubmoduleConfigurations: false,
						extensions: [],
						submoduleCfg: [],
						userRemoteConfigs: [[
							credentialsId: env.gitlabCredentialsId,
							url: "${env.gitlabRepositoryUrl}"
					]]]

				configFileProvider([configFile(fileId: 'gradle.properties', targetLocation: '.')]) {
                    sh 'push_to_iata2.bash $WORKSPACE $JOB_BASE_NAME'
                    sh './gradlew clean build -x test -x itest'
                }
				dir('build/libs') {
                    stash name: 'agent-management-artifact'
                }
            }
        }
        
        stage('Test') {
            steps {
		    	sh './gradlew test itest'
            }
            //publish unit tests and unit test coverage reports
         	post {
                always {
                    junit '**/test-results/**/*.xml'
                    step([$class: 'JacocoPublisher'])
                }
            }
        }
		
		stage('SonarQube') {
            steps {
            	withSonarQubeEnv('a2lsonarq1') {
                	sh './gradlew sonarqube'
             	}
            }
        }
        
        stage('Nexus')  {
    		when{
				expression{
					return env.sourceBranch == 'master' || env.sourceBranch == 'develop'
				}
  			}
			steps {
				sh './gradlew uploadArchives'
			}
		}
        
    	stage('Push to IATA repository') {
			when{
				expression{
					return env.sourceBranch == 'master'
				}
  			}
            steps {
            	// this shell script is located in <JENKINS_HOME>/bin folder and the PATH is configured through jenkins
                sh 'push_to_iata.bash $WORKSPACE $JOB_BASE_NAME'
            }
        }
        stage('Push to IATA Develop_repository') {
            steps {
            	// this shell script is located in <JENKINS_HOME>/bin folder and the PATH is configured through jenkins
                sh 'push_to_iata2.bash $WORKSPACE $JOB_BASE_NAME'
			}
        }
		
		stage('Promote artifact to deploy DEV')
		{
		    
		  agent {
                label 'a2lyadesch1'
            }   
		    
		  steps {
		      
		    dir('build/libs') {
                    unstash name: 'agent-management-artifact'
                }    
              withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'credentials-openshift',
                    usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                     
                      script {
                        docker.image('atsistemas/openshift-cli:1.2').withRun('-v $WORKSPACE/build/libs:$WORKSPACE') { c ->
                            sh "oc login https://$clusterOpenShiftDev -u $USERNAME -p $PASSWORD --insecure-skip-tls-verify=true"
                            sh "oc project nfedesarrollo"
                            sh "oc start-build bsplink-agent-management --from-dir=$WORKSPACE/build/libs --follow"
                        }
                      }   
                        
                  
               }    
                    
                
		  }
			
		 }
		
  	}
}
