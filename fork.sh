#!/bin/bash
groupId=$(echo $2 | sed 's|\.|/|g')
#fsm-pekko-***-advice
mkdir "fsm-pekko-$1-advice"
mkdir -p "fsm-pekko-$1-advice/src/main/java/${groupId}/aspect"
sedAspectJ="s|org.salgar.pekko.fsm|$2|g;"
sed "${sedAspectJ}" "fsm-pekko-4eyes-advice/src/main/java/org/salgar/pekko/fsm/aspect/FSMAspect.aj" > "fsm-pekko-$1-advice/src/main/java/${groupId}/aspect/FSMAspect.aj"
#fsm-pekko-***-uml-model
mkdir "fsm-pekko-$1-uml-model"
mkdir -p "fsm-pekko-$1-uml-model/src/main/resources"
#fsm-pekko-***-statemachine
mkdir "fsm-pekko-$1-statemachine"
cp "fsm-pekko-4eyes-statemachine/.scalafmt.conf" "fsm-pekko-$1-statemachine"
cp "fsm-pekko-4eyes-statemachine/plugin.properties" "fsm-pekko-$1-statemachine"
mkdir -p "fsm-pekko-$1-statemachine/src/main/mwe2"
sedStatemachineMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedStatemachineMwe="${sedStatemachineMwe}s|4eyes|$1|g"
sed "${sedStatemachineMwe}" "fsm-pekko-4eyes-statemachine/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-statemachine/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-actionguard-impl
mkdir "fsm-pekko-$1-actionguard-impl"
mkdir -p "fsm-pekko-$1-actionguard-impl/src/main/java/${groupId}"
#fsm-pekko-***-event-adapter
mkdir "fsm-pekko-$1-event-adapter"
mkdir -p "fsm-pekko-$1-event-adapter/src/main/scala/${groupId}"
#fsm-pekko-***-statemachine-facade
mkdir "fsm-pekko-$1-statemachine-facade"
mkdir -p "fsm-pekko-$1-statemachine-facade/src/main/java/${groupId}"
mkdir -p "fsm-pekko-$1-statemachine-facade/src/main/scala/${groupId}"
#fsm-pekko-***-model
mkdir "fsm-pekko-$1-model"
mkdir -p "fsm-pekko-$1-model/src/main/mwe2"
sedModelMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedModelMwe="${sedModelMwe}s|4eyes|$1|g"
sed "${sedModelMwe}" "fsm-pekko-4eyes-model/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-model/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-protobuf
mkdir "fsm-pekko-$1-protobuf"
mkdir -p "fsm-pekko-$1-protobuf/src/main/mwe2"
sedProtobufMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedProtobufMwe="${sedProtobufMwe}s|4eyes|$1|g"
sed "${sedProtobufMwe}" "fsm-pekko-4eyes-protobuf/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-protobuf/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-converter
mkdir "fsm-pekko-$1-converter"
mkdir -p "fsm-pekko-$1-converter/src/main/mwe2"
sedConverterMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedConverterMwe="${sedConverterMwe}s|4eyes|$1|g"
sed "${sedConverterMwe}" "fsm-pekko-4eyes-converter/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-converter/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-****-command
mkdir "fsm-pekko-$1-command"
mkdir -p "fsm-pekko-$1-command/src/main/mwe2"
sedCommandMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedCommandMwe="${sedCommandMwe}s|4eyes|$1|g"
sed "${sedCommandMwe}" "fsm-pekko-4eyes-command/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-command/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-kafka
mkdir "fsm-pekko-$1-kafka"
mkdir -p "fsm-pekko-$1-kafka/src/main/mwe2"
cp "fsm-pekko-4eyes-kafka/.scalafmt.conf" "fsm-pekko-$1-kafka"
sedKafkaMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedKafkaMwe="${sedKafkaMwe}s|4eyes|$1|g"
sed "${sedKafkaMwe}" "fsm-pekko-4eyes-kafka/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-kafka/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-elasticsearch-statemachine-adapter
mkdir "fsm-pekko-$1-elasticsearch-statemachine-adapter"
cp "fsm-pekko-4eyes-elasticsearch-statemachine-adapter/.scalafmt.conf" "fsm-pekko-$1-elasticsearch-statemachine-adapter"
mkdir -p "fsm-pekko-$1-elasticsearch-statemachine-adapter/src/main/mwe2"
sedESSMAdapMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedESSMAdapMwe="${sedESSMAdapMwe}s|4eyes|$1|g"
sed "${sedESSMAdapMwe}" "fsm-pekko-4eyes-elasticsearch-statemachine-adapter/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-elasticsearch-statemachine-adapter/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-projections-statemachine-adapter
mkdir "fsm-pekko-$1-projections-statemachine-adapter"
cp "fsm-pekko-4eyes-projections-statemachine-adapter/.scalafmt.conf" "fsm-pekko-$1-projections-statemachine-adapter"
mkdir -p "fsm-pekko-$1-projections-statemachine-adapter/src/main/mwe2"
sedProjSMAdapMwe="s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedProjSMAdapMwe="${sedProjSMAdapMwe}s|4eyes|$1|g"
sed "${sedProjSMAdapMwe}" "fsm-pekko-4eyes-projections-statemachine-adapter/src/main/mwe2/GenerateWorkflow.mwe2" > "fsm-pekko-$1-projections-statemachine-adapter/src/main/mwe2/GenerateWorkflow.mwe2"
#fsm-pekko-***-projections
mkdir "fsm-pekko-$1-projections"
mkdir -p "fsm-pekko-$1-projections/src/main/scala/${groupId}"
#fsm-pekko-***-application
mkdir "fsm-pekko-$1-application"
mkdir -p "fsm-pekko-$1-application/src/main/java/${groupId}"
mkdir -p "fsm-pekko-$1-application/src/main/resources"
mkdir -p "fsm-pekko-4eyes-application/src/main/jib/var/lib/fsm_pekko_$1_application"
cp "fsm-pekko-4eyes-application/src/main/resources/bootstrap.yml" "fsm-pekko-$1-application/src/main/resources/bootstrap.yml"
cp "fsm-pekko-4eyes-application/src/main/resources/logback.xml" "fsm-pekko-$1-application/src/main/resources/logback.xml"
cp -R "fsm-pekko-4eyes-application/helm" "fsm-pekko-$1-application/helm"

camelCase=$(echo "${1}" | awk 'BEGIN{FS="";RS="-";ORS=""} {$0=toupper(substr($0,1,1)) substr($0,2)} 1')
hyphenCase=$(echo "${1}" | awk 'BEGIN{FS="";RS="-";ORS=""} {$0=substr($0,1,1) substr($0,2)} 1')
targetFileName=$(echo "FSMPekko4EyesApplication" | sed "s|4Eyes|${camelCase}|g")

#Application.java
sedApplicationCommand="s|FSMPekko4EyesApplication|${targetFileName}|g;"
sedApplicationCommand="${sedApplicationCommand}s|org.salgar.fsm.pekko.foureyes|$2|g;"
sedApplicationCommand="${sedApplicationCommand}/Configuration/d"
sed "${sedApplicationCommand}" "fsm-pekko-4eyes-application/src/main/java/org/salgar/fsm/pekko/foureyes/FSMPekko4EyesApplication.java" > "fsm-pekko-$1-application/src/main/java/${groupId}/${targetFileName}.java"

#Starter.java
sedStarterCommand="/StreamConfig\.apply/{N;N;N;N;N;N;d;};"
sedStarterCommand="${sedStarterCommand}/StreamConfig/d;"
sedStarterCommand="${sedStarterCommand}/CreditSMProjection\.init/{N;N;N;d;};"
sedStarterCommand="${sedStarterCommand}/CreditSMProjection/d;"
sedStarterCommand="${sedStarterCommand}s|org.salgar.fsm.pekko.foureyes.credit|$2|g;"
sedStarterCommand="${sedStarterCommand}s|org.salgar.fsm.pekko.foureyes|$2|g"
sed "${sedStarterCommand}" "fsm-pekko-4eyes-application/src/main/java/org/salgar/fsm/pekko/foureyes/Starter.java" > "fsm-pekko-$1-application/src/main/java/${groupId}/Starter.java"

#Application.yml
sedApplicationYmlCommand="s|FourEyes|${camelCase}|g;"
sedApplicationYmlCommand="${sedApplicationYmlCommand}s|org\.salgar\.fsm\.pekko\.foureyes|$2.${hyphenCase}|g;"
sedApplicationYmlCommand="${sedApplicationYmlCommand}s|foureyes|$1|g;"
sedApplicationYmlCommand="${sedApplicationYmlCommand}/multi\-credit\-score-sm\:/{N;N;N;N;N;N;d;};"
sedApplicationYmlCommand="${sedApplicationYmlCommand}/credit\-score-sm\:/{N;N;N;N;N;N;d;};"
sedApplicationYmlCommand="${sedApplicationYmlCommand}/adress\-check\-sm\:/{N;N;N;N;N;N;d;};"
sedApplicationYmlCommand="${sedApplicationYmlCommand}/fraud\-prevention\-sm\:/{N;N;N;N;N;N;d;}"
sed "${sedApplicationYmlCommand}" "fsm-pekko-4eyes-application/src/main/resources/application.yml" > "fsm-pekko-$1-application/src/main/resources/application.yml"

#Helm
sed "s|4eyes|$1|g" "fsm-pekko-4eyes-application/helm/values.yaml" > "fsm-pekko-$1-application/helm/values.yaml"
sed "s|foureyes|$1|g" "fsm-pekko-4eyes-application/helm/Chart.yaml" > "fsm-pekko-$1-application/helm/Chart.yaml"

#settings.gradle
sedCommand="s|fsm-pekko-4eyes-uml-model|fsm-pekko-$1-uml-model|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-advice|fsm-pekko-$1-advice|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-statemachine|fsm-pekko-$1-statemachine|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-actionguard-impl|fsm-pekko-$1-actionguard-impl|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-event-adapter|fsm-pekko-$1-event-adapter|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-statemachine-facade|fsm-pekko-$1-statemachine-facade|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-model|fsm-pekko-$1-model|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-protobuf|fsm-pekko-$1-protobuf|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-converter|fsm-pekko-$1-converter|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-command|fsm-pekko-$1-command|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-kafka|fsm-pekko-$1-kafka|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-elasticsearch-statemachine-adapter|fsm-pekko-$1-elasticsearch-statemachine-adapter|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-projections-statemachine-adapter|fsm-pekko-$1-projections-statemachine-adapter|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-projections|fsm-pekko-$1-projections|g;"
sedCommand="${sedCommand}s|fsm-pekko-4eyes-application|fsm-pekko-$1-application|g;"
sedCommand="${sedCommand}s|fsm_pekko|$1|g"

sed "${sedCommand}" settings.gradle > settings_fork.gradle

#build.gradle
sedBuildCommand="s|fsm-pekko-4eyes-uml-model|fsm-pekko-$1-uml-model|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-advice|fsm-pekko-$1-advice|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-statemachine|fsm-pekko-$1-statemachine|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-actionguard-impl|fsm-pekko-$1-actionguard-impl|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-event-adapter|fsm-pekko-$1-event-adapter|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-statemachine-facade|fsm-pekko-$1-statemachine-facade|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-model|fsm-pekko-$1-model|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-protobuf|fsm-pekko-$1-protobuf|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-converter|fsm-pekko-$1-converter|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-command|fsm-pekko-$1-command|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-kafka|fsm-pekko-$1-kafka|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-elasticsearch-statemachine-adapter|fsm-pekko-$1-elasticsearch-statemachine-adapter|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-projections-statemachine-adapter|fsm-pekko-$1-projections-statemachine-adapter|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-projections|fsm-pekko-$1-projections|g;"
sedBuildCommand="${sedBuildCommand}s|fsm-pekko-4eyes-application|fsm-pekko-$1-application|g"

sed "${sedBuildCommand}" build.gradle > build_fork.gradle

#gradle.properties
sed "s|FourEyes|${camelCase}|g" gradle.properties > gradle_fork.properties
