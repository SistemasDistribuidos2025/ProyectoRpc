//crea los _pb cliente gitbash
protoc --proto_path="." --js_out="import_style=commonjs:../" --grpc-web_out="import_style=commonjs,mode=grpcwebtext:../" "usuario.proto"


protoc --proto_path="."   --js_out="import_style=commonjs:../"   --grpc-web_out="import_style=commonjs,mode=grpcwebtext:../"   "donaciones.proto"


//crea los _pb del server
mvn clean compile 
