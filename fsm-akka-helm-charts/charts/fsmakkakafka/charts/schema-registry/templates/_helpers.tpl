{{/* vim: set filetype=mustache: */}}
{{/*
Expand the name of the chart.
*/}}
{{- define "schema-registry.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "schema-registry.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- $name := default .Chart.Name .Values.nameOverride -}}
{{- if contains $name .Release.Name -}}
{{- .Release.Name | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "schema-registry.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Create a default fully qualified kafka headless name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "kafka-rest.kafka-headless.fullname" -}}
{{- $name := "headless" -}}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{/*
Form the Kafka URL. If Kafka is installed as part of this chart, use k8s service discovery,
else use user-provided URL
*/}}
{{- define "schema-registry.kafka.bootstrapServers" -}}
{{- if .Values.kafka.bootstrapServers -}}
{{- .Values.kafka.bootstrapServers -}}
{{- else -}}
{{- if .Values.global.ssl -}}
{{- printf "SSL://%s:9092" (include "kafka-rest.kafka-headless.fullname" .) -}}
{{- else -}}
{{- printf "PLAINTEXT://%s:9092" (include "kafka-rest.kafka-headless.fullname" .) -}}
{{- end -}}
{{- end -}}
{{- end -}}

{{/*
Default GroupId to Release Name but allow it to be overridden
*/}}
{{- define "schema-registry.groupId" -}}
{{- if .Values.overrideGroupId -}}
{{- .Values.overrideGroupId -}}
{{- else -}}
{{- .Release.Name -}}
{{- end -}}
{{- end -}}

{{- define "schema-registry.image" -}}
{{ include "fsm-akka.images.image" (dict "imageRoot" .Values.image "global" .Values.global) }}
{{- end -}}

{{- define "schema-registry.metrics.image" -}}
{{ include "fsm-akka.images.image" (dict "imageRoot" .Values.prometheus.jmx.image "global" .Values.global) }}
{{- end -}}