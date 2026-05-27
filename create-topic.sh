#!/bin/bash

NAMESPACE="infra-order-dev"
BROKER="kafka-0"
BOOTSTRAP="localhost:9092"

# Liste des topics
TOPICS=(
  "inventory-reserved-events"
  "inventory-depleted-events"
  "order-created-events"
  "order-shipped-events"
  "order-updated-events"
  "order-cancelled-events"
)

echo "🔄 Suppression des topics existants..."
for topic in "${TOPICS[@]}"; do
  kubectl -n $NAMESPACE exec -it $BROKER -- \
    kafka-topics --delete \
    --topic $topic \
    --bootstrap-server $BOOTSTRAP \
    --if-exists
done

echo "⏳ Attente 3 secondes..."
sleep 3

echo "🆕 Création des topics avec replication-factor=3..."
for topic in "${TOPICS[@]}"; do
  kubectl -n $NAMESPACE exec -it $BROKER -- \
    kafka-topics --create \
    --topic $topic \
    --bootstrap-server $BOOTSTRAP \
    --replication-factor 3 \
    --partitions 3
done

echo "📋 Vérification..."
kubectl -n $NAMESPACE exec -it $BROKER -- \
  kafka-topics --bootstrap-server $BOOTSTRAP --describe

echo "✅ Terminé !"
