pipeline {
  agent any

  environment {
    // GKE
    GKE_PROJECT = "my-gke-poc-001"
    GKE_REGION  = "asia-south1"
    CLUSTER     = "gke-poc-cluster"

    // Artifact Registry
    AR_PROJECT  = "cwp-arun-1764754197"
    AR_REGION   = "asia-south1"
    AR_REPO     = "demo-repo"

    IMAGE_NAME  = "demo-java-app"
    IMAGE_TAG   = "${BUILD_NUMBER}"
    IMAGE       = "${AR_REGION}-docker.pkg.dev/${AR_PROJECT}/${AR_REPO}/${IMAGE_NAME}:${IMAGE_TAG}"
  }

  stages {

    stage('Checkout') {
      steps {
        git branch: 'branch-1',
            url: 'https://github.com/Arun8077/demo-java-app.git'
      }
    }

    stage('Build Application') {
      steps {
        sh 'mvn clean package -DskipTests'
      }
    }
    stage('SonarQube Analysis') {
      environment {
        SCANNER_HOME = tool 'sonar-scanner'
      }
      steps {
        withSonarQubeEnv('sonarqube') {
          sh '''
            $SCANNER_HOME/bin/sonar-scanner \
              -Dsonar.projectKey=demo-java-app \
              -Dsonar.projectName=demo-java-app \
              -Dsonar.branch.name=branch-1 \
              -Dsonar.sources=src/main/java \
              -Dsonar.java.binaries=target/classes
          '''
        }
      }
    }
    stage('Build Docker Image') {
      steps {
        sh 'docker build -t $IMAGE .'
      }
    }

    stage('Push Image') {
      steps {
        sh 'docker push $IMAGE'
      }
    }

    stage('Deploy to GKE') {
      steps {
        sh '''
          /opt/google-cloud-sdk/bin/gcloud container clusters get-credentials \
            $CLUSTER --region $GKE_REGION --project $GKE_PROJECT

          sed -i "s|image:.*|image: $IMAGE|" k8s/demo-deployment.yaml

          /usr/local/bin/kubectl apply -f k8s/demo-deployment.yaml
        '''
      }
    }
  }
}
