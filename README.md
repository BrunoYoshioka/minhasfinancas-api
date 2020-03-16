# API Minhas Financas
Projeto back end

## Caso de Uso do Sistema
### •	Efetuar Login
Caso o usuário esteja cadastrado no sistema, poderá efetuar login, dessa forma o usuário o sistema irá exigir que o mesmo informe obrigatoriamente o email e senha cadastrado no sistema. Caso não esteja cadastrado, o sistema deverá disponibilizar um formulário para cadastro do usuário.
### •	Cadastro de Usuários
Sistema deverá permitir que qualquer usuário possa efetuar o seu cadastro para acesso ao sistema que são: nome, email e senha. O email deverá ser único, a fim de evitar cadastros duplos.
### •	Cadastro de Lançamentos
Deseja-se cadastrar os lançamentos de Receita e Despesa de cada mês, com seu valor e descrição. Ao cadastrar, ele ficará pendente. Haverá opção de cancelá-lo ou efetivá-lo, ele incorpora o saldo. Os usuários poderão consultar seus lançamentos, filtrando os por ano, mês, descrição ou tipo de lançamentos.

![]()

## Tecnologias
<p>•	Java (Spring Boot)<br />
•	SQL (PostgreSQL)<br />
•	Node Js (disponível no link para baixar) https://nodejs.org/en/<br />
•	React Js<br />
•	Bootstrap</p>

## Ferramentas
<p>•	Intellij IDE<br />
•	Apache Maven<br />
•	Visual Studio Code<br />
•	Github</p>

## Execução da aplicação
### 1-	Clonar o repositório API
https://github.com/BrunoYoshioka/minhasfinancas-api.git 
### 2-	Através do console, entre no diretório do projeto minhasfinancas-api
#### 2.1- Criando o Jar executável: 
mvn package
#### 2.2- Executar a aplicação API: 
java -jar target/minhasfinancas-0.0.1-SNAPSHOT.jar
