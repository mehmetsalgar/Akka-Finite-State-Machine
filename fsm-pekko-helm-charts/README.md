# Storage Class
kubectl apply -f https://raw.githubusercontent.com/rancher/local-path-provisioner/master/deploy/local-path-storage.yaml

# k3d
https://k3d.io/v5.0.2/

# k8ssandra
https://docs.k8ssandra.io/install/local/
https://docs.k8ssandra.io/tasks/connect/ingress/k3d-deployment/

#k3d registry
https://k3d.io/v5.1.0/usage/registries/
k3d registry create fsm-akka.registry --port 5555
vim /etc/hosts -> for fsm-akka.registry

# k3d v4.x.x Cluster
k3d cluster create poc-akka \
--registry-use k3d-fsm-akka.registry:5555 \
--k3s-server-arg "--no-deploy" \
--k3s-server-arg "traefik" \
--port "80:32080@loadbalancer" \
--port "443:32443@loadbalancer" \
--port "9000:32090@loadbalancer" \
--port "9042:32091@loadbalancer" \
--port "9142:32092@loadbalancer" \
--port "8880:30080@loadbalancer" \
--port "8881:30081@loadbalancer" \
--port "8881:30082@loadbalancer" \
--port "9200:32193@loadbalancer"

# k3d -v5.x.x Cluster
k3d cluster create poc-akka \
--registry-use k3d-fsm-akka.registry:5555 \
--k3s-arg "--no-deploy=traefik@server:0" \
--port "80:32080@loadbalancer" \
--port "443:32443@loadbalancer" \
--port "9000:32090@loadbalancer" \
--port "9042:32091@loadbalancer" \
--port "9142:32092@loadbalancer" \
--port "8880:30080@loadbalancer" \
--port "8881:30082@loadbalancer" \
--port "9200:32193@loadbalancer" \
--servers-memory 16Gi

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

# Kafka
helm install fsm-akka . -n fsmakka --create-namespace -f values-kafka-k3d.yaml
kafka-topics --zookeeper fsm-akka-zookeeper-headless:2181 --create --topic creditSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper fsm-akka-zookeeper-headless:2181 --create --topic multiTenantScreditScoreSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper fsm-akka-zookeeper-headless:2181 --create --topic creditScoreSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper fsm-akka-zookeeper-headless:2181 --create --topic addressCheckSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper fsm-akka-zookeeper-headless:2181 --create --topic fraudPreventionSM --partitions 1 --replication-factor 1

kafka-topics --zookeeper localhost:2181 --create --topic creditSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper localhost:2181 --create --topic multiTenantScreditScoreSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper localhost:2181 --create --topic creditScoreSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper localhost:2181 --create --topic addressCheckSM --partitions 1 --replication-factor 1
kafka-topics --zookeeper localhost:2181 --create --topic fraudPreventionSM --partitions 1 --replication-factor 1

# JiB
gradle :fsm-akka-4eyes-application:jib

# minikube
https://medium.com/rahasak/replace-docker-desktop-with-minikube-and-hyperkit-on-macos-783ce4fb39e3
minikube start --memory 32768 --cpus 6 --driver=docker

# ARM64 Kafka Images
https://nxt.engineering/blog/kafka-docker-image/

socat -d -d TCP-LISTEN:2375,reuseaddr,fork UNIX-CONNECT:/run/user/1000/podman/podman.sock &

jenv exec mvn clean package -DskipTests -Pdocker -Ddocker.registry=fsm-akka.registry:5555/nxt/
jenv exec mvn clean package -DskipTests -Pdocker -DCONFLUENT_PACKAGES_REPO='https://packages.confluent.io/rpm/6.2' -DCONFLUENT_VERSION=6.2.2 -Ddocker.registry=nxt/
jenv exec mvn clean package -DskipTests -Pdocker -DCONFLUENT_PACKAGES_REPO='https://packages.confluent.io/rpm/6.2' -DCONFLUENT_VERSION=6.2.2 -Ddocker.registry=nxt/

docker tag nxt/confluentinc/cp-schema-registry:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-schema-registry:6.2.2-ubi8
docker tag nxt/confluentinc/cp-server-connect:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-server-connect:6.2.2-ubi8
docker tag nxt/confluentinc/cp-server-connect-base:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-server-connect-base:6.2.2-ubi8
docker tag nxt/confluentinc/cp-kafka-connect:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-kafka-connect:6.2.2-ubi8
docker tag nxt/confluentinc/cp-kafka-connect-base:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-kafka-connect-base:6.2.2-ubi8
docker tag nxt/confluentinc/cp-enterprise-kafka:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-enterprise-kafka:6.2.2-ubi8
docker tag nxt/confluentinc/cp-kafka:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-kafka:6.2.2-ubi8
docker tag nxt/confluentinc/cp-server:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-server:6.2.2-ubi8
docker tag nxt/confluentinc/cp-zookeeper:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-zookeeper:6.2.2-ubi8
docker tag nxt/confluentinc/cp-base-new:6.2.2-ubi8 fsm-akka.registry:5555/nxt/confluentinc/cp-base-new:6.2.2-ubi8

docker push fsm-akka.registry:5555/nxt/confluentinc/cp-schema-registry:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-server-connect:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-server-connect-base:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-kafka-connect:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-kafka-connect-base:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-enterprise-kafka:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-kafka:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-server:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-zookeeper:6.2.2-ubi8
docker push fsm-akka.registry:5555/nxt/confluentinc/cp-base-new:6.2.2-ubi8


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