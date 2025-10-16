# Storage Class
kubectl apply -f https://raw.githubusercontent.com/rancher/local-path-provisioner/master/deploy/local-path-storage.yaml

# k3d
https://k3d.io/v5.0.2/

# k8ssandra
https://docs.k8ssandra.io/install/local/
https://docs.k8ssandra.io/tasks/connect/ingress/k3d-deployment/

#k3d registry
https://k3d.io/v5.1.0/usage/registries/
k3d registry create fsm-pekko.registry --port 5555
vim /etc/hosts -> for fsm-pekko.registry

# k3d -v5.x.x Cluster
k3d cluster create poc-pekko \
--registry-use k3d-fsm-pekko.registry:5555 \
--k3s-arg "--disable=traefik@server:0" \
--port "80:32080@loadbalancer" \
--port "443:32443@loadbalancer" \
--port "9000:32090@loadbalancer" \
--port "9042:32091@loadbalancer" \
--port "9142:32092@loadbalancer" \
--port "8880:30080@loadbalancer" \
--port "8881:30082@loadbalancer" \
--port "9200:32193@loadbalancer" \
--image rancher/k3s:v1.24.13-rc1-k3s1-arm64 \
--servers-memory 16G

# Traefik
helm repo add traefik https://helm.traefik.io/traefik
helm install traefik . -n traefik --create-namespace -f values-traefik.yaml

# k8ssandra
helm repo add k8ssandra https://helm.k8ssandra.io/stable
helm install -f values-k8ssandra.yaml fsm-cassandra . -n fsmakka --create-namespace

# JFrog Artifactory
https://artifacthub.io/packages/helm/jfrog/artifactory

helm repo add jfrog https://charts.jfrog.io
helm install artifactory . -n artifactory -f values-artifactory1.yaml -f values-artifactory-small.yaml --create-namespace

1. Get the Artifactory URL by running these commands:

   NOTE: It may take a few minutes for the LoadBalancer IP to be available.
   You can watch the status of the service by running 'kubectl get svc --namespace artifactory -w artifactory-artifactory-nginx'
   export SERVICE_IP=$(kubectl get svc --namespace artifactory artifactory-artifactory-nginx -o jsonpath='{.status.loadBalancer.ingress[0].ip}')
   echo http://$SERVICE_IP/

1. Get the Artifactory URL by running these commands:
   export NODE_PORT=$(kubectl get --namespace artifactory -o jsonpath="{.spec.ports[0].nodePort}" services artifactory-artifactory-nginx)
   export NODE_IP=$(kubectl get nodes --namespace artifactory -o jsonpath="{.items[0].status.addresses[0].address}")
   echo http://$NODE_IP:$NODE_PORT/

3. Open Artifactory in your browser
   Default credential for Artifactory:
   user: admin
   password: password
##### Postgre Password Problem
https://github.com/jfrog/charts/issues/63

kubectl -n artifactory delete pvc artifactory-volume-artifactory-artifactory-0

# Add local Helm Repository to Helm
helm repo add fsmAkka http://kubernetes.docker.internal:28015/artifactory/fsm_akka_helm --username fsm_akka_management --password fsm_akka_rest

# JiB
gradle :fsm-akka-4eyes-application:jib

# minikube
https://medium.com/rahasak/replace-docker-desktop-with-minikube-and-hyperkit-on-macos-783ce4fb39e3
minikube start --memory 32768 --cpus 6 --driver=docker

#Strimzi Operator
helm install fsmpekko-strimzi . --namespace strimzi-operator --create-namespace --set watchAnyNamespace=true -f values-strimzi.yaml

helm install fsm-pekko-kafka . -n fsmpekkokafka --create-namespace -f values-kafka.yaml

# ARM64 ElasticSearch
helm repo add elasticsearch https://helm.elastic.co/
helm install fsmelasticseach . -n fsmakka -f ./values-elasticsearch.yaml

# ARM64 nexus
helm repo add nexus https://sonatype.github.io/helm3-charts/
helm install nexus . -n nexus --create-namespace -f values-nexus.yaml

# Metric Server
helm repo add metrics-server https://kubernetes-sigs.github.io/metrics-server/
helm install metrics-server . -f values-metrics.yaml

# Application
helm repo add fsmakka http://localhost:55120/repository/fsmakka/
helm install foureyes . -n fsmakka --create-namespace --set fsm-akka-4eyes-application.enabled=true