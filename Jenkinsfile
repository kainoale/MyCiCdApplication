pipeline {
  agent any

  environment {
    REGISTRY = 'localhost:5000'
    APP_NAME = 'myapp'
    IMAGE_TAG = "${env.BUILD_NUMBER}"   // можно заменить на GIT_COMMIT
    KUBE_NS  = 'myapp'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Build app') {
      steps {
        sh '''
          docker run --rm \
            -v /var/jenkins_home/.m2:/root/.m2 \
            -v "$PWD":/app -w /app \
            maven:3.9-eclipse-temurin-21 \
            mvn -q -B -DskipTests package
        '''
      }
    }

    stage('Docker build & push') {
      steps {
        sh """
          docker build -t ${REGISTRY}/${APP_NAME}:${IMAGE_TAG} .
          docker push  ${REGISTRY}/${APP_NAME}:${IMAGE_TAG}
        """
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        sh """
          docker run --rm \
            -v /var/jenkins_home/.kube:/root/.kube \
            -v ${WORKSPACE}/k8s:/k8s \
            -e KUBECONFIG=/root/.kube/config \
            -e IMAGE_TAG=${IMAGE_TAG} \
            bitnami/kubectl:latest sh -c '
              kubectl apply -f /k8s/namespace.yaml &&
              sed "s/\\\${IMAGE_TAG}/${IMAGE_TAG}/g" /k8s/deployment.yaml | kubectl apply -f - &&
              kubectl apply -f /k8s/service.yaml &&
              kubectl -n ${KUBE_NS} rollout status deploy/myapp
            '
        """
      }
    }
  }

  post {
    success {
      echo "Open: http://localhost:30080/api/hello"
      echo "Image: ${REGISTRY}/${APP_NAME}:${IMAGE_TAG}"
    }
  }
}
