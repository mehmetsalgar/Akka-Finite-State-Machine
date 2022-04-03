{{- define "fsm-akka.images.image" -}}
{{- $registryName := .imageRoot.registry -}}
{{- $repositoryName := .imageRoot.repository -}}
{{- $tag := .imageRoot.tag | toString -}}
{{- if .imageRoot.registry -}}
    {{- if .global }}
        {{- if .global.imageRegistryDontOverride -}}
{{- printf "%s:%s" $repositoryName $tag -}}
        {{- else -}}
            {{- if .global.imageRegistry }}
                {{- $registryName = .global.imageRegistry -}}
            {{- end -}}
        {{- end -}}
    {{- end -}}
{{- printf "%s/%s:%s" $registryName $repositoryName $tag -}}
{{- else -}}
{{- printf "%s:%s" $repositoryName $tag -}}
{{- end -}}
{{- end -}}