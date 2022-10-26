# Les bonnes pratiques et challenges des architectures distribuées basées sur les Micro-services 

**Remember**:

<fieldset>

**“IOC”, “DI” mais c’est quoi au juste?**

L’inversion de contrôle ou IOC pour inversion of control est un principe général de conception dont le but est de diminuer le couplage entre les composants logiciels.

L’injection de dépendances ou DI pour “dependency injection”, est-elle une méthode permettant d’appliquer le principe d’IOC. Le principe est tout simple, il consiste à passer (injecter) un composant logiciel (une classe par exemple) à un autre composant logiciel qui l’utilise, permettant ainsi d’éviter la dépendance d’un composant à un autre et ainsi améliorer la souplesse de l’application.

[<GO TO LINK>](https://www.codeheroes.fr/2017/10/10/node-js-linversion-de-controle-ioc-et-linjection-de-dependances-di/#:~:text=L'inversion%20de%20contr%C3%B4le%20ou,appliquer%20le%20principe%20d'IOC.)

</fieldset>


### Content 
1. [Basics](#architecture-micro-services-et-contraintes)
    1. [Architecture Monolotique et contraintes](#architecture-monolotique-et-contraintes)
    2. [Architecture Micro-services et contraintes](#architecture-micro-services-et-contraintes)
    3. [Gateway](#gateway)
    4. [Modèle de communication entre les microservices](#modèle-de-communication-entre-les-microservices)
    5. [Security challenge ](#security-challenge)
    6. [How to deal with my data ?](#how-to-deal-with-my-data)
    7. [How to deal with my data : CQRS & Event Sourcing](#how-to-deal-with-my-data--cqrs--event-sourcing)
2. [Demo](#demo)
    1. [Use case](#use-case)
    2. [Customer service](#customer-service)
        1. [Dependencies](#customer-service-dependencies)
        2. [Properties file](#customer-service-properties-file)
        3. [JPA Entity](#customer-service-entity)
        4. [JPA Repository](#customer-service-repository)
        5. [Service layer](#customer-service-service-layer)
        6. [DTOs](#customer-service-dtos)
        7. [Mapping](#customer-service-mapping)
        8. [Rest Controller](#customer-service-rest-controller)
        9. [Customer Service tests](#customer-service-tests)
        10. [Documenting our service with openAPI (Swagger)](#documenting-our-service-with-openapi-swagger)
    
  



### Architecture Monolotique et contraintes:
- Basé sur plusieurs modèle qui ils sont intégrés dans la même application.
- Une seule base de données.
- Une seule technologie (Côut de formation).
- Processus Unique (Si une fonctionnalité arrête, l'application arrête completement).
- Difficiles à maintenir.
- Difficile à tester.
- Mise en production prend beaucoup de temps.
- Redéploiement à froid (obliger d'acrrêter l'app et la redéploier).
- Performances (Scalabilité).


### Architecture Micro-services et contraintes:
- Un ensembles des modèles séparés avec des bases de données séparées.
- Un ensembles des petites applications.
- Les application front-end ne doivent pas forcement savoir les microservices, ils ont besoin juste de connecter avec la `Gateway`.
- `Gateway` pour chaque requete reçus il va se charger d'acheminer la requete vers le bon Micro-service (Routage).
- Pour svoir dans quelle machine se trouve le Microservice, `Gateway` utilise `Discovery service`, pour un mappage dynamique surtout si on démarre plusieurs instance d'un microservice.
- `Discovery service` se chargé de publier ou d'enregistrer toutes les instance des Micro-services disponibles.
- Chaque microservice qu'il démarre va enregistrer son adresse IP, nom, et port dans le `discovery service`.
- Pour ne pas avoir des conflits de configuration, on utilise le `Config service` qui va se charger de cnetraliser la configuration globale en commun.
- Le microservice reçoit sa configuration à chaud c à d sans avoir besoin l'arrêter.


<fieldset>

`Microservice cherche sa configuration dans Config service` &rarr; `Microservice Démarre` &rarr; `Microservice Démarre` &rarr; `Microservice Démarre` &rarr; 
.... youtba3
<fieldset>

![vs](https://jelvix.com/wp-content/uploads/2019/02/monolith-microservices-comperative-table.jpg)


- `Event Bus`: pour une communication asynchronne entre les microservices.

**Avantages**

- **Performance (Scalabilité Horzontale)** : Si on a un problème de monté en charge il suffit démarrer d'autres instances du micro-service affecter, il va s'enregistrer dans le `discovery service` et il va etre disponible, la `Gateway` dans ce cas là va utiliser un système d'équilibrage de charge `load balancer` entre les instances.
- Processus séparés.
- Faciles à développer à tester et à maintenir.
- Mise en production rapide des micro-services.
- Redéploiement à chaud.
- Téchnologies déffirentes.
- Equipes indépendentes.
- Facile à appliquer l'agilité.
- Facile à mettre en oeuvre TDD (Test puis fonctionnalité).

### Gateway

|ZUUL|Spring Cloud Gateway|
|--|--|
|Modèle Multi thread avec des entrés sorties bloquantes|Modèle Single thread avec des entrés sorties non bloquantes|
|Thread pour chaque requête|Un thread pour toutes les requetes (Event loop)|
|Thread peut etre mobiliser pour une longue durée |Asychronisation, buffering pour servir plusieurs reqêtes|
|N'est pas scalable virticalement |Scalable virticalement|
|Limité par le nombre maximal des threads  |limité par les ressources uniquement|

### Modèle de communication entre les microservices

|Modèle synchrone|Modèle asynchrone|
|--|--|
|REST avec `Open Feign`|Par des `Event Bus`/ `Broker` avec `Kafka, RabbitMQ`|
|Utiliser l'API du service pour envoyer des requêtes HTTP |Les service fait un subscribe dans event bus et attends pour les informations arrivées|
|![1](./imgs/1.PNG) |![1](./imgs/2.PNG) |


### Security challenge 

|Modèle d'authentification `Statful`|Modèle d'authentification `Statless`|
|--|--|
|Les informations de la session s'enregistrent dans le côté serveur, le client reçoit un Session ID unique à conserver dans les Cookies pour l'envoyer avec les prochaines requêtes|Les données de la session sont enregistrées dans un `jeton`/ `token` d'authentification délivré au client |
|Utilisé dans la majorité des cas dans les applications monolitiques||
|-|Ex. JWT..|
|-|Plus adapté au Micro-services car on a pas de session partagé entre les services|


**Quelques outils à savoir en ralation avec sécurité**:
- Spring Security
- OAuth2
- JWT
- OpenID Connect
- **Keycloak** : [https://www.keycloak.org/] => Outils prêt
- Spring cloud OAuth


### How to deal with my data ?

- Garder l'architecture Microservices mais avec une seule base de données ??
- L'ideal c'est d'utiliser une DB pour chaque Micro-service, pour garder la performance et la scalabilité.
- Plus Ideal : Utiliser les deux au même temps avec un `Event Bus`: c'est d'avoir des microservices avec leurs bases de données + un qui a une bas de données globale, dans laquel il stock tous les données, Ce microservice attend les evenements du Broker (Event Bus) et mis à jour la base globale.

![3](./imgs/3.PNG)

- On peut fair la même chose si par exemple on a besoin à une base de données pour un moteur de recherche (Elasticsearch), il suffit de créer un autre service handler.


#### How to deal with my data : CQRS & Event Sourcing

- **CQRS**: Command Query Responsability Segregation
- A pattern that separates read and update operations for a data store. Implementing CQRS in your application can maximize its performance, scalability, and security.
- Séparer le bus de lecture du bus d'écriture.
- Le bus de `Command` permet de modifier les données
- Le bus de `Query` permet de la lecture des données.
- **Event Sourcing**: An patern qui consist à ne pas enregister le dernier état de l'application dans la base de donnée mais, d'enregister tous les élements (evenements) de l'application, et qui permet de retrouver l'état de notre app (Je veux l'état de ma base de données d'un mois avant...).
- Outils : `Spring cloud`, `AXION Framework`.
- 

----------------------------------

## Demo

### Use case :
- Respecter tous les bonnes pratiques:

![4](./imgs/4.PNG)

![5](./imgs/5.PNG)

<pre>
Travail à faire :
 Suivre les vidéos Bonnes pratiques de Architectures micro-services :
 1. Vidéo 1 : Concepts de bases
 2.  Vidéo 2 à 5 : Mise en oeuvre d'une application distribuée basée sur deux micro-services en utilisant les bonnes pratiques  :
  - Couches DA0, Service, Web, DTO
  - Utilisation de MapStruct pour le mapping entre les objet Entities et DTO
  - Génération des API-DOCS en utilisant SWAGGER3 (Open API)
  - Communication entre micro-services en utilisant OpenFeign
  - Spring Cloud Gateway
  - Eureka Discovery Service
Travail à rendre Dimanche 31 Octobre
  - etc...
</pre>

### Customer Service

<pre>
DTO: Data transfer object => Objects adapted to the UI layer, it is just a fransfer of the data.
</pre>

#### Customer Service Dependencies 

in `build.gradle`  file (or `pom.xml`) if we use maven we add dependencies.

```groovy
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-validation'
	implementation 'org.springframework.boot:spring-boot-starter-webflux'
	implementation 'org.springframework.cloud:spring-cloud-starter-gateway'
	implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
	compileOnly 'org.projectlombok:lombok'
	runtimeOnly 'com.h2database:h2'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testImplementation 'io.projectreactor:reactor-test'
	implementation 'org.mapstruct:mapstruct:1.5.3.Final'
	annotationProcessor 'org.mapstruct:mapstruct-processor:1.5.3.Final'
	// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
	implementation 'org.springdoc:springdoc-openapi-ui:1.6.12'
}

```

If you are a Maven user, add the following dependencies to your pom.xml file:

```xml
<dependencies>
    <!-- ... -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>1.4.2.Final</version>
    </dependency>   
</dependencies>

<!-- ... -->
<build>
    <plugins>
        <!-- ... -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>17</source> <!-- depending on your project. In this example, Java 11 is used -->
                <target>17</target> <!-- depending on your project. In this example, Java 11 is used -->
                <annotationProcessorPaths>
                    <!-- Here we can add the path for lombok -->
                    <path>
                        <groupId>org.projectlombok</groupId> 
                        <artifactId>lombok</artifactId>
                        <version>1.18.16</version>
                    </path>
                    <path>
                        <groupId>org.mapstruct</groupId> 
                        <artifactId>mapstruct-processor</artifactId>
                        <version>1.4.2.Final</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```
from: https://maven.apache.org/plugins/maven-compiler-plugin/compile-mojo.html 
<pre>
Classpath elements to supply as annotation processor path. If specified, the compiler will detect annotation processors only in those classpath elements. If omitted, the default classpath is used to detect annotation processors. The detection itself depends on the configuration of annotationProcessors.

Each classpath element is specified using their Maven coordinates (groupId, artifactId, version, classifier, type). Transitive dependencies are added automatically. Example:
</pre>

```xml
<configuration>
  <annotationProcessorPaths>
    <path>
      <groupId>org.sample</groupId>
      <artifactId>sample-annotation-processor</artifactId>
      <version>1.2.3</version>
    </path>
    <!-- ... more ... -->
  </annotationProcessorPaths>
</configuration>
```


#### Customer Service Properties file

```properties
server.port=8082
spring.application.name=CUSTOMER-SERVER
spring.h2.console.enabled=false
spring.cloud.discovery.enabled=false # prevent server to be registred to discovery service
spring.datasource.url=jdbc:h2:mem:customer-db
```

#### Customer Service Entity

```java

@Data
@Entity
@NoArgsConstructor @Builder
@AllArgsConstructor
public class Customer {
    @Id
    private String id;
    @NotBlank
    private String name;
    @Email
    private String email;
}
```

#### Customer Service Repository

```java
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
}

```

#### Customer Service Service layer

```java
@Service
@Transactional
public interface CustomerService {

    CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDTO);

    CustomerResponseDTO getCustomer(String id);

    CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO customerRequestDTO);

    boolean deleteCustomer(String id);

    List<CustomerResponseDTO> getAllCustomers(int page, int size);
}

```
Implementation

```java

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CustomerMapper customerMapper;

    @Override
    public CustomerResponseDTO saveCustomer(CustomerRequestDTO customerRequestDTO){
        Customer customer = customerMapper.toCustomer(customerRequestDTO);
        customer.setId(UUID.randomUUID().toString());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponse(savedCustomer);
    }

    @Override
    public CustomerResponseDTO getCustomer(String id){
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow( () -> {
                    return  new  RuntimeException(String.format("Customer with ID: %s Not found !", id));
                });
        return customerMapper.toCustomerResponse(customer);
    }

    @Override
    public CustomerResponseDTO updateCustomer(String id, CustomerRequestDTO customerRequestDTO){
        Customer customer = customerRepository
                .findById(id)
                .orElseThrow( () -> {
                    return  new  RuntimeException(String.format("Customer with ID: %s Not found !", id));
                });

        Customer customerMap = customerMapper.toCustomer(customerRequestDTO);
        customerMap.setId(id);
        Customer savedCustomer = customerRepository.save(customerMap);
        return customerMapper.toCustomerResponse(savedCustomer);
    }

    @Override
    public boolean deleteCustomer(String id){
        customerRepository.deleteById(id);
        return true;
    }

    @Override
    public List<CustomerResponseDTO> getAllCustomers(int page, int size){
        Page<Customer> customerPage = customerRepository.findAll(PageRequest.of(page, size));
        List<CustomerResponseDTO> customerResponseDTOList =
                customerPage.getContent().stream().map(customer -> customerMapper.toCustomerResponse(customer)).collect(Collectors.toList());
        return customerResponseDTOList;
    }

}
```

#### Customer Service DTOs

```java

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CustomerRequestDTO {
    private String id;
    private String name;
    private String email;
}

```

```java

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class CustomerResponseDTO {
    private String id;
    private String name;
    private String email;
}

```

#### Customer Service Mapping

Documentation: https://mapstruct.org/documentation/stable/reference/html/ 
Get Statted with mapstruct : https://mapstruct.org/ 

`CustomerMapper` interface in the mappers package
```java

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerResponseDTO toCustomerResponse(Customer customer);
    Customer toCustomer(CustomerRequestDTO customerRequestDTO);
}

```

#### Customer Service Rest Controller

```java

@RestController
@RequestMapping(path = "/api-v1/customer-service")
@AllArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @GetMapping("/")
    public List<CustomerResponseDTO> getCustomerList(@RequestParam(name = "page", defaultValue = "0") int page,
                                                     @RequestParam(name = "size", defaultValue = "10") int size ){

        return customerService.getAllCustomers(page, size);
    }

    @GetMapping ("/{customerId}")
    public CustomerResponseDTO getCustomer(@PathVariable(name = "customerId") String id){
        return customerService.getCustomer(id);
    }

    @PostMapping("/")
    public CustomerResponseDTO insertCustomer(@RequestBody CustomerRequestDTO customerRequestDTO){
        return customerService.saveCustomer(customerRequestDTO);
    }

    @PutMapping("/{customerId}")
    public CustomerResponseDTO updateCustomer(@PathVariable(name = "customerId") String id,
                                              @RequestBody CustomerRequestDTO customerRequestDTO){
        return customerService.updateCustomer(id, customerRequestDTO);
    }

    @DeleteMapping("/{customerId}")
    public boolean updateCustomer(@PathVariable(name = "customerId") String id){
        return customerService.deleteCustomer(id);
    }

}

```

#### Customer Service tests

Adding 2 Customers at the begining 

```java
@Bean
	CommandLineRunner start(CustomerRepository customerRepository){
		return args -> {
			// Create just 2 customers

			List.of("Ahmed", "Essadeq").forEach( c ->{
				Customer customer = new Customer(null, c, String.format("%s_email@gmail.com", c));
				customerRepository.save(customer);
			});
		};
	}
```

H2-console consulting:

**Problem**:
- Do not have access to `http://localhost:8082/h2-console`

<pre>
Whitelabel Error Page
This application has no configured error view, so you are seeing this as a fallback.

Wed Oct 26 15:39:39 WEST 2022
[bb486a63-3] There was an unexpected error (type=Not Found, status=404).
org.springframework.web.server.ResponseStatusException: 404 NOT_FOUND
	at org.springframework.web.reactive.resource.ResourceWebHandler.lambda$handle$1(ResourceWebHandler.java:408)
	Suppressed: The stacktrace has been enhanced by Reactor, refer to additional information below: 
Error has been observed at the following site(s):
	*__checkpoint ⇢ org.springframework.cloud.gateway.filter.WeightCalculatorWebFilter [DefaultWebFilterChain]
	*__checkpoint ⇢ HTTP GET "/h2-console" [ExceptionHandlingWebHandler]
Original Stack Trace: ...
</pre>

- Tried to install `devtools`, but did not resolve the problem.
- `spring.h2.console.enabled=true` did not resolve the problem.

But the application works fine:

Visiting `http://localhost:8082/api-v1/customers/`

```json
[{"id":"96fe8548-ceda-420c-8a50-b4d4ab3b75c1","name":"Ahmed","email":"Ahmed_email@gmail.com"},{"id":"d62da59d-6483-4535-948b-63fd2751e81d","name":"Essadeq","email":"Essadeq_email@gmail.com"}]
```
Visiting : `http://localhost:8082/api-v1/customers/96fe8548-ceda-420c-8a50-b4d4ab3b75c1`

```json
{"id":"96fe8548-ceda-420c-8a50-b4d4ab3b75c1","name":"Ahmed","email":"Ahmed_email@gmail.com"}
```

Tested with `restClient` VScode extension :
Usage : https://marketplace.visualstudio.com/items?itemName=MadsKristensen.RestClient
Usage 2: https://medium.com/refinitiv-developer-community/how-to-test-rest-api-with-visual-studio-code-rest-client-extensions-9f2e061d0299

File:  [FILE_LINK](./restClient.http)

```http
@hostname = localhost
@port = 8082
@contentType = application/json
@baseUrl = http://{{hostname}}:{{port}}/api-v1/customers/
POST {{baseUrl}} HTTP/1.1
Content-Type: {{contentType}}

{
    "name":"Laila",
    "email":"laila@gmail.com"
}

# edit
PUT {{baseUrl}}e07ba26b-6620-4733-a9d0-47cc9b2c2f59 HTTP/1.1
Content-Type: {{contentType}}
{
    "id" : null,
    "name":"Laila_edited",
    "email":"laila@gmail.com"
}

# list all 

GET {{baseUrl}} HTTP/1.1

```

Tested also with Postman ==> everything works fine [ :smile: DONE]

#### Documenting our service with openAPI (Swagger)

Adding the dependency 
```groovy
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
implementation 'org.springdoc:springdoc-openapi-ui:1.6.12'

```

Visiting : `http://localhost:8082/v3/api-docs` we will have our API documentation, which can be used with any testion tool (Postman ...).

INFO: https://www.baeldung.com/spring-rest-openapi-documentation 

To use a custom path, we can indicate in the application.properties file:

```properties
springdoc.api-docs.path=/api-docs
```
Now we'll be able to access the docs at:

`http://localhost:8080/api-docs/`

The OpenAPI definitions are in JSON format by default. For yaml format, we can obtain the definitions at:

`http://localhost:8080/api-docs.yaml`

Besides generating the OpenAPI 3 specification itself, we can integrate springdoc-openapi with Swagger UI so that we can interact with our API specification and exercise the endpoints.

The springdoc-openapi dependency already includes Swagger UI, so we're all set here.

We can simply access the API documentation at:

`http://localhost:8080/swagger-ui.html`

- `OpenAPI` is the equivalent of `WSDL` for `SOAP` based  services
- What's that ?

<pre>
REST versus SOAP. It’s been an issue for a while now. And really, they’re just two answers to the 
same question: how to access web services.

But deciding one over the other can be surprisingly difficult.

SOAP (Simple Object Access Protocol) is a standards-based web services access protocol that has 
been around for a long time. Originally developed by Microsoft, SOAP isn’t as simple as the 
acronym would suggest.

REST (Representational State Transfer) is another standard, made in response to SOAP’s 
shortcomings. It seeks to fix the problems with SOAP and provide a simpler method of accessing web 
services. 

What about GraphQL?
Of course, GraphQL has recently made a huge splash, which we’ve spoken of at length in other 
articles. But it’s still not as standardized as REST and SOAP, so in this article we’re just going 
to focus on those two.
</pre>

**Problems**
404 Error on `http://localhost:8082/h2-console` and `http://localhost:8082/v3/api-docs`

<pre>
Whitelabel Error Page
This application has no configured error view, so you are seeing this as a fallback.

Wed Oct 26 17:03:21 WEST 2022
[75afbc90-6] There was an unexpected error (type=Not Found, status=404).
</pre>

Raison:  

<pre>
As per your logs, I found that, you are using an embedded server, other than tomcat, i.e, spring-boot-starter-reactor-netty that comes along with spring webflux dependency.

H2ConsoleAutoConfiguration will not be executed for spring webflux & netty(reactor based), as H2 console will only be available to servlet based applications. So, you have to configure H2 server manually in this spring boot application, with spring webflux & netty.
</pre> 

Suggested solutions:  
https://stackoverflow.com/questions/63646864/spring-boot-h2-console-returns-404

https://stackoverflow.com/questions/52949088/h2-db-not-accessible-at-localhost8080-h2-console-when-using-webflux

**Solution for** : `http://localhost:8082/v3/api-docs`

- Adding the dependency to `duild.gradle` :

```groovy
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-webflux-ui
implementation 'org.springdoc:springdoc-openapi-webflux-ui:1.6.12'
```
Instead of 

```groovy
// https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-ui
implementation 'org.springdoc:springdoc-openapi-ui:1.6.12'
```

- Adding the config in the `Application`

```java

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "APIs", version = "1.0", description = "Documentation APIs v1.0"))
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(CustomerRepository customerRepository){
		return args -> {
			// Create just 2 customers

			List.of("Ahmed", "Essadeq").forEach( c ->{
				Customer customer = new Customer(UUID.randomUUID().toString(), c, String.format("%s_email@gmail.com", c));
				customerRepository.save(customer);
			});
		};
	}
}

```

// here is the description of our API

<details>

```json
{
   "openapi":"3.0.1",
   "info":{
      "title":"APIs",
      "description":"Documentation APIs v1.0",
      "version":"1.0"
   },
   "servers":[
      {
         "url":"http://localhost:8082",
         "description":"Generated server url"
      }
   ],
   "paths":{
      "/api-v1/customers/{customerId}":{
         "get":{
            "tags":[
               "customer-controller"
            ],
            "operationId":"getCustomer",
            "parameters":[
               {
                  "name":"customerId",
                  "in":"path",
                  "required":true,
                  "schema":{
                     "type":"string"
                  }
               }
            ],
            "responses":{
               "200":{
                  "description":"OK",
                  "content":{
                     "*/*":{
                        "schema":{
                           "$ref":"#/components/schemas/CustomerResponseDTO"
                        }
                     }
                  }
               }
            }
         },
         "put":{
            "tags":[
               "customer-controller"
            ],
            "operationId":"updateCustomer",
            "parameters":[
               {
                  "name":"customerId",
                  "in":"path",
                  "required":true,
                  "schema":{
                     "type":"string"
                  }
               }
            ],
            "requestBody":{
               "content":{
                  "application/json":{
                     "schema":{
                        "$ref":"#/components/schemas/CustomerRequestDTO"
                     }
                  }
               },
               "required":true
            },
            "responses":{
               "200":{
                  "description":"OK",
                  "content":{
                     "*/*":{
                        "schema":{
                           "$ref":"#/components/schemas/CustomerResponseDTO"
                        }
                     }
                  }
               }
            }
         },
         "delete":{
            "tags":[
               "customer-controller"
            ],
            "operationId":"updateCustomer_1",
            "parameters":[
               {
                  "name":"customerId",
                  "in":"path",
                  "required":true,
                  "schema":{
                     "type":"string"
                  }
               }
            ],
            "responses":{
               "200":{
                  "description":"OK",
                  "content":{
                     "*/*":{
                        "schema":{
                           "type":"boolean"
                        }
                     }
                  }
               }
            }
         }
      },
      "/api-v1/customers/":{
         "get":{
            "tags":[
               "customer-controller"
            ],
            "operationId":"getCustomerList",
            "parameters":[
               {
                  "name":"page",
                  "in":"query",
                  "required":false,
                  "schema":{
                     "type":"integer",
                     "format":"int32",
                     "default":0
                  }
               },
               {
                  "name":"size",
                  "in":"query",
                  "required":false,
                  "schema":{
                     "type":"integer",
                     "format":"int32",
                     "default":10
                  }
               }
            ],
            "responses":{
               "200":{
                  "description":"OK",
                  "content":{
                     "*/*":{
                        "schema":{
                           "type":"array",
                           "items":{
                              "$ref":"#/components/schemas/CustomerResponseDTO"
                           }
                        }
                     }
                  }
               }
            }
         },
         "post":{
            "tags":[
               "customer-controller"
            ],
            "operationId":"insertCustomer",
            "requestBody":{
               "content":{
                  "application/json":{
                     "schema":{
                        "$ref":"#/components/schemas/CustomerRequestDTO"
                     }
                  }
               },
               "required":true
            },
            "responses":{
               "200":{
                  "description":"OK",
                  "content":{
                     "*/*":{
                        "schema":{
                           "$ref":"#/components/schemas/CustomerResponseDTO"
                        }
                     }
                  }
               }
            }
         }
      }
   },
   "components":{
      "schemas":{
         "CustomerRequestDTO":{
            "type":"object",
            "properties":{
               "id":{
                  "type":"string"
               },
               "name":{
                  "type":"string"
               },
               "email":{
                  "type":"string"
               }
            }
         },
         "CustomerResponseDTO":{
            "type":"object",
            "properties":{
               "id":{
                  "type":"string"
               },
               "name":{
                  "type":"string"
               },
               "email":{
                  "type":"string"
               }
            }
         }
      }
   }
}
```
</details>

Visiting : `http://localhost:8082/webjars/swagger-ui/index.html`

![6](./imgs/6.PNG)

--------------------

### Billing Service


#### Billing Service Dependencies 

```xml

```

#### Billing Service Properties file

#### Billing Service Entity

#### Billing Service Repository


#### Billing Service Service layer

#### Billing Service DTOs

#### Billing Service Mapping

#### Billing Service Rest Controller

#### Billing Service tests
