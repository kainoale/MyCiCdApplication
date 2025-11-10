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
        sh 'chmod +x mvnw || true'
        sh './mvnw -q -B -DskipTests package'
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
            --volumes-from jenkins \
            -e KUBECONFIG=/var/jenkins_home/.kube/config \
            -e WORKSPACE="${WORKSPACE}" \
            --entrypoint /bin/sh bitnami/kubectl:latest -c '
              set -e
              kubectl apply -f "$WORKSPACE/k8s/namespace.yaml"
              sed "s/\\\${IMAGE_TAG}/${IMAGE_TAG}/g" "$WORKSPACE/k8s/deployment.yaml" | kubectl apply -f -
              kubectl apply -f "$WORKSPACE/k8s/service.yaml"
              kubectl -n myapp rollout status deploy/myapp
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
