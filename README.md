# E-COMMERCE API
<p>This is a e-commerce api project made with Spring Boot, Offering functions like mange products, users, orders and more...</p>

### 🚀 Stack

- Java 21
- Spring Boot 3.4.4
- Maven
- Spring Web
- Spring JPA
- Lombok
- JWT
- WebSocket
- Spring Security
- PostgreSQL
- Swagger/OpenAPI

### ✉️ Project Structure

```declarative
Ecommerce_API/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── frasato/
│   │   │           └── ecommerce/
│   │   │               ├── config/
│   │   │               ├── controllers/
│   │   │               ├── dtos/
│   │   │               ├── exceptions/
│   │   │               ├── models/
│   │   │               ├── repositories/
│   │   │               ├── security/
│   │   │               ├── services/
│   │   │               ├── utils/
│   │   │               └── ApiApplication.java
│   │   └── resources/
│   │       └── application.properties
├── pom.xml
└── README.md

```

### ⚙️ Config and Execution

1º Clone the repository
```bash
git clone https://github.com/Frasato/Ecommerce_API.git
cd Ecommerce_API
```
2º Config database
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```
3º Exe application
```bash
./mvnw spring-boot:run
```
4º Access the documentation
<p>After starting the application, access http://localhost:8080/swagger-ui.html</p>
