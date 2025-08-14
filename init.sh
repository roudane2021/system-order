#!/bin/bash

set -e

DOCKERFILE_INFRA_INVENTORY_PATH="./infra-inventory"
DOCKERFILE_INFRA_ORDER_PATH="./infra-order"
IMAGE_INFRA_ORDER_NAME="localhost:5000/infra_order"
IMAGE_INFRA_INVENTORY_NAME="localhost:5000/infra_inventory"

if [ ! -f "$DOCKERFILE_INFRA_ORDER_PATH/Dockerfile" ] || [ ! -r "$DOCKERFILE_INFRA_ORDER_PATH/Dockerfile" ]; then
    echo "Erreur: $DOCKERFILE_INFRA_ORDER_PATH n'est pas lisible ou n'existe pas"
  exit 1
fi



# 1. Vérifie et démarre minikube
if ! minikube status | grep -q "Running"; then
  echo "Minikube non démarré. Lancement..."
  minikube start
else
  echo "Minikube déjà en cours."
fi

# 2. Active l'addon
echo "Activation des addons registry et ingress..."
minikube addons enable registry
minikube addons enable ingress

#echo "Port-forward du registre local (localhost:5000)..."
#k port-forward --namespace kube-system service/registry 5000:80 >/dev/null 2>&1 &
#PORT_FORWARD_PID=$!


# 5. build Maven
#echo "Maven clean install"
#mvn clean install -Dskiptests=true -f "$DOCKERFILE_INFRA_ORDER_PATH/pom.xml"


# 5. Build de l'image
#echo "Construction de l'image Docker..."
#docker build -t $IMAGE_INFRA_ORDER_NAME -f "$DOCKERFILE_INFRA_ORDER_PATH/Dockerfile" $DOCKERFILE_INFRA_ORDER_PATH

# 6. Push de l'image
echo "Push vers le registre local..."
docker push $IMAGE_INFRA_ORDER_NAME

# 5. build Maven
#echo "Maven clean install INVENTORY"
#mvn clean install -Dskiptests=true -f "$DOCKERFILE_INFRA_INVENTORY_PATH/pom.xml"


# 5. Build de l'image
#echo "Construction de l'image Docker..."
#docker build -t $IMAGE_INFRA_INVENTORY_NAME -f "$DOCKERFILE_INFRA_INVENTORY_PATH/Dockerfile" $DOCKERFILE_INFRA_INVENTORY_PATH

# 6. Push de l'image
echo "Push vers le registre local..."
docker push $IMAGE_INFRA_INVENTORY_NAME

# 7. Tunnel
echo "Création du tunnel Minikube (ctrl+C pour stopper)..."
minikube tunnel

# Nettoyage du port-forward si tunnel se termine
kill $PORT_FORWARD_PID

minikube addons disable registry
minikube addons disable ingress
