# Les bonnes pratiques et challenges des architectures distribuées basées sur les Micro-services 

### Content 
1. [Architecture Monolotique et contraintes](#architecture-monolotique-et-contraintes)
2. [Architecture Micro-services et contraintes](#architecture-micro-services-et-contraintes)
3. [Gateway](#gateway)
4. [Modèle de communication entre les microservices](#modèle-de-communication-entre-les-microservices)
5. []()
6. []()


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



