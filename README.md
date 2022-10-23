# Les bonnes pratiques et challenges des architectures distribuées basées sur les Micro-services 

### Content 
1. [Architecture Monolotique et contraintes](#architecture-monolotique-et-contraintes)
2. [Architecture Micro-services et contraintes](#architecture-micro-services-et-contraintes)
3. []()
4. []()
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

- 27:18