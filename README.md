//crea los _pb cliente gitbash
protoc --proto_path="." --js_out="import_style=commonjs:../" --grpc-web_out="import_style=commonjs,mode=grpcwebtext:../" "usuario.proto"

//crea los _pb donaciones gitbash
protoc --proto_path="."   --js_out="import_style=commonjs:../"   --grpc-web_out="import_style=commonjs,mode=grpcwebtext:../"   "donaciones.proto"

//Levantar servidor Kafka + zookeper
docker-compose up -d

//crea los _pb del server
mvn clean compile 

//Crea contenedor Kafka UI
docker run -d \
  --name kafka-ui \
  -p 8082:8080 \
  -e KAFKA_CLUSTERS_0_NAME=local \
  -e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=host.docker.internal:9092 \
  provectuslabs/kafka-ui


Se encuentra un .pom en la ruta ProyectoRpc/Documents/Git/trabajo rpc/ que es un .pom Padre, conectado con el .pom del ServerRpc y el .pom de Kafka
