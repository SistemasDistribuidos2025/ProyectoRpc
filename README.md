//crea los _pb cliente gitbash
protoc --proto_path="." --js_out="import_style=commonjs:../" --grpc-web_out="import_style=commonjs,mode=grpcwebtext:../" "usuario.proto"

//crea los _pb donaciones gitbash
protoc --proto_path="."   --js_out="import_style=commonjs:../"   --grpc-web_out="import_style=commonjs,mode=grpcwebtext:../"   "donaciones.proto"

//crea los _pb del server
mvn clean compile 


Se encuentra un .pom en la ruta ProyectoRpc/Documents/Git/trabajo rpc/ que es un .pom Padre, conectado con el .pom del ServerRpc y el .pom de Kafka
