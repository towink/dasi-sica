# DASI - SICA

## Proyecto

Este proyecto esta siendo desarrollado para la asignatura "Desarrolo de Aplicaciones y Sistemas Inteligentes" del máster "Ingeniería Informática" de la Universidad Complutense de Madrid. Consiste en la elaboración de un simulador de abejas empleando múltiples agentes. Habrán cuatro tipos:

* Abeja reina: agente encargado de crear nuevas abejas. Este agente no se moverá de la cólmena y la función de las demás abejas es protegerla, ya que si esta muere, la colmena morirá.
* Abeja defensora: encargadas de permanecer en la cómena o cerca de ella para progteger a la reina de los enemigos.
* Abeja obrera: encargadas de explorar el mapa en busca de alimento y posteriormente recolectarlo. El alimento recolectado se almacenará en la colmena.
* Enemigos: Serán los agentes encargados de intentar atacar a la colmena para matar a la abeja reina.

## Herramientas

Se emplea el lenguaje de programación Java con la librería de [MASON](http://cs.gmu.edu/~eclab/projects/mason/) para el sistema multiagente y [drools](https://www.drools.org/) para el uso de reglas en la implementación de algunos de los agentes.

## Ejecución

Para ejecutar el programa se debe crear un proyecto de drools (nosotros usamos el IDE eclipse para esto) y copiar o "linkear" dentro el directorio src de este repositorio. Además, se deberá importar las librería de MASON al proyecto creado, que se puede descargar de [aquí](http://cs.gmu.edu/~eclab/projects/mason/mason.19.jar).  Con esto ya creado, solo queda ejecutar el main que se encuentra en el fichero "BeeGUI.java" .