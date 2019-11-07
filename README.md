# reactive-spring-boot


## Execução da Aplicação


1. Subindo o banco (Mongo DB):

		# Executando Dockerfile
		$ docker build .
		# Subindo a imagem do Mongo DB
		$ docker run --name mongodb -d -p 27017:27017 -p 28017:28017 -e AUTH=no mongo

2. Subindo a aplicação:

		# Executando Spring-Boot (Porta:8080)
		$ mvn spring-boot:run
		
## Testando a Aplicação

* Existe um arquivo Json, que pode ser importado pelo Postman, que contem as collections necessárias para testar todos os end-points do serviço, em localhost.