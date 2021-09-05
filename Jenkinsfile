pipeline {
  agent {
    docker {
      image "registry.gmasil.de/docker/maven-build-container"
      args "-v /maven:/maven -v /root/.docker/config.json:/root/.docker/config.json -e JAVA_TOOL_OPTIONS='-Duser.home=/maven'"
    }
  }
  environment {
    MAVEN_ARTIFACT = sh(script: "mvn -q -Dexec.executable=echo -Dexec.args='\${project.groupId}:\${project.artifactId}' --non-recursive exec:exec", returnStdout: true).trim()
    MAVEN_PROJECT_NAME = sh(script: "mvn -q -Dexec.executable=echo -Dexec.args='\${project.name}' --non-recursive exec:exec", returnStdout: true).trim()
  }
  stages {
    stage("build") {
      steps {
        sh "mvn clean deploy --no-transfer-progress"
      }
    }
    stage("analyze") {
      environment {
        SONAR_TOKEN = credentials("SONAR_TOKEN")
      }
      steps {
        sh "mvn -f webproject-backend jacoco:report sonar:sonar --no-transfer-progress -Dsonar.host.url=https://sonar.gmasil.de -Dsonar.login=${env.SONAR_TOKEN} -Dsonar.projectKey=${env.MAVEN_ARTIFACT}:${env.GIT_BRANCH} \"-Dsonar.projectName=${env.MAVEN_PROJECT_NAME} (${env.GIT_BRANCH})\""
      }
    }
  }
  post {
    always {
      junit testResults: '**/surefire-reports/**/*.xml', allowEmptyResults: true
      archiveArtifacts artifacts: '**/selenium-screenshots/*.png', fingerprint: true, allowEmptyArchive: true
    }
  }
}