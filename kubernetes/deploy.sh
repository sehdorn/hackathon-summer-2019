#! /bin/bash

NAMESPACE="graal"

# create namespace
kubectl delete ns ${NAMESPACE}

kubectl create ns ${NAMESPACE}

echo "deploying to k8s namespace: ${NAMESPACE}"

kubectl -n ${NAMESPACE} apply -f data-consumer-graalvm.yaml
kubectl -n ${NAMESPACE} apply -f data-consumer-jvm.yaml

kubectl -n ${NAMESPACE} apply -f prometheus.yaml
kubectl -n ${NAMESPACE} apply -f grafana.yaml

sleep 10

kubectl -n ${NAMESPACE} apply -f data-producer.yaml