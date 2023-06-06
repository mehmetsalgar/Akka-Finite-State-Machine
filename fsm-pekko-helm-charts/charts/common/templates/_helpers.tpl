{{- define "name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "kafka.properties" -}}
{{- if .Values.ssl -}}
- name: kafka_ssl_keystore_location
  value: /etc/{{ include "name" . }}/secrets/kafka.broker.keystore.jks
- name: kafka_ssl_keystore_credential
  value: keystore-creds
- name: kafka_ssl_truststore_location
  value: /etc/{{ include "name" . }}/secrets/kafka.broker.truststore.jks
- name: kafka_ssl_truststore_credential
  value: truststore-creds
- name: schema_registry_ssl_keystore_location
  value: /etc/{{ include "name" . }}/secrets/kafka.broker.keystore.jks
- name: schema_registry_ssl_keystore_credential
  value: keystore-creds
- name: schema_registry_ssl_truststore_location
  value: /etc/{{ include "name" . }}/secrets/kafka.broker.truststore.jks
- name: schema_registry_ssl_truststore_credential
  value: truststore-creds
{{- end -}}
{{- end -}}

{{- define "kafka-ssl.properties" -}}
{{- if .Values.ssl -}}
export kafka_ssl_keystore_credential_location=/etc/{{ include "name" . }}/secrets/${kafka_ssl_keystore_credential} && \
export kafka_ssl_keystore_password && \
kafka_ssl_keystore_password=$(cat $kafka_ssl_keystore_credential_location) && \
export kafka_ssl_truststore_credential_location=/etc/{{ include "name" . }}/secrets/${kafka_ssl_truststore_credential} && \
export kafka_ssl_truststore_password && \
kafka_ssl_truststore_password=$(cat $kafka_ssl_truststore_credential_location) && \
export schema_registry_ssl_keystore_credential_location=/etc/{{ include "name" . }}/secrets/${schema_registry_ssl_keystore_credential} && \
export schema_registry_ssl_keystore_password && \
schema_registry_ssl_keystore_password=$(cat $schema_registry_ssl_keystore_credential_location) && \
export schema_registry_ssl_truststore_credential_location=/etc/{{ include "name" . }}/secrets/${schema_registry_ssl_truststore_credential} && \
export schema_registry_ssl_truststore_password && \
schema_registry_ssl_truststore_password=$(cat $schema_registry_ssl_truststore_credential_location) && \
{{- end -}}
{{- end -}}

{{- define "kafka-ssl-volumemount" -}}
- name: secrets-vol
  mountPath: /etc/{{ include "name" . }}/secrets
{{- end -}}

{{- define "kafka-ssl-volume" -}}
- name: secrets-vol
  secret:
    secretName: {{.Release.Name}}-ssl-config
{{- end -}}

{{- define "truststore.properties" -}}
- name: client_ssl_truststore
  value: /etc/{{ include "name" . }}/secrets/truststore.jks
{{- end -}}

{{- define "truststore-volumemount" -}}
- name: secrets-vol
  mountPath: /etc/{{ include "name" . }}/secrets
{{- end -}}

{{- define "truststore-volume" -}}
- name: secrets-vol
  secret:
    secretName: {{.Release.Name}}-truststore-config
{{- end -}}

{{- define "prometheus-annotations" -}}
prometheus.io/scrape: "true"
prometheus.io/path: /mgmt/prometheus
prometheus.io/port: {{ .Values.service.port | quote }}
{{- end -}}

{{/*
Selector labels
*/}}
{{- define "fsm-akka.common.selectorLabels" -}}
app.kubernetes.io/name: {{ include "fsm-akka.common.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Expand the name of the chart.
*/}}
{{- define "fsm-akka.common.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}