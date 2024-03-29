apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${IMAGE_NAME}
spec:
  replicas: 1
  selector:
    matchLabels:
      app: ${IMAGE_NAME}
  template:
    metadata:
      annotations:
        prometheus.io/scrape: 'true'
        prometheus.io/port: '8081'
        prometheus.io/path: '/actuator/prometheus'
        update: ${HASHCODE}
      labels:
        app: ${IMAGE_NAME}
    spec:
      serviceAccountName: cluster-admin-sa
      imagePullSecrets:
      - name: harbor-registry-secret
      containers:
      - name: ${IMAGE_NAME}
        image: ${DOCKER_REGISTRY}/${IMAGE_NAME}:${VERSION}
        imagePullPolicy: Always
        securityContext:
            runAsUser: 0
            privileged: true
        env:
        - name: LOGGING_LEVEL
          value: ${LOGGING_LEVEL}
        - name: DATASOURCE_URL
          valueFrom:
            secretKeyRef:
              name: datasource-secrets
              key: database-url
        - name: DATASOURCE_USERNAME
          valueFrom:
            secretKeyRef:
              name: datasource-secrets
              key: database-username
        - name: DATASOURCE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: datasource-secrets
              key: database-password
