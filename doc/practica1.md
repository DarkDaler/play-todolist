Práctica 1 - API REST play-todolist
=========

En esta primera práctica vamos a hacer una primera iteración de la aplicación play-todolist en la que convertiremos la aplicación en un API REST que trabaja con objetos JSON y añadiremos algunos datos adicionales a las tareas que gestiona el API: usuario que crea la tarea y fecha opcional de finalización. Aprenderemos las metodologías para generar dichas consultas mediante peticiones web GET, POST, DELETE, que mediante el REST podremos invocar, dándonos los resultados pedidos.

También aprenderemos a tratar con el control de versiones GIT, el cual utilizaremos ramas para después al terminar nuestro trabajo, juntarlas y dejar un desarrollo lo más limpio posible.

Para las llamadas a los métodos de las features, utilizaremos la herramienta POSTMAN de Google Chrome.

FEATURE 1
----

En este feature se nos pedirán las siguientes funcionalidades:

#### -Consulta de una tarea:
    GET /tasks/{id}
Este método, accediendo a la opción GET en localhost:9000/tasks/"id", conseguimos que nos devuelva una tarea dada por su id en formato JSON. Si con dicha id no existe ninguna tarea, se nos devolverá un HTTP STATUS del tipo `404 NOT FOUND`.
#### -Creación de nueva tarea:
    POST /tasks
Este método, accediendo a la opción POST en localhost:9000/tasks, y introduciendo una descripción en el campo label, conseguimos crear una nueva tarea. Si se crea correctamente, se nos devolverá un HTTP STATUS del tipo `201 CREATED`. Se nos devolverá la tarea creada para verificarla.
#### -Listado de tareas:
    GET /tasks
Este método, accediendo a la opción GET en localhost:9000/tasks, conseguimos que nos devuelva todas las tareas creadas. Se nos devolverán en formato lista de JSON, y un HTTP STATUS del tipo `200 OK`.
#### -Borrado de una tarea:
    DELETE /tasks/{id}
Este método, accediendo a la opción DELETE en localhost:9000/tasks/{id}, elimina la tarea con ese id y devuelve una lista en JSON con las tareas restantes. El estado que devuelve si se ha borrado correctamente es el HTTP STATUS del tipo `200 OK`.

FEATURE 2
-----------

En este feature se nos pedirán las siguientes nuevas funcionalidades que cuadren con las del anterior:

#### -Consulta de tareas de un user:
    GET /users/{login}/tasks
Este método, accediendo a la opción GET en localhsot:9000/users/{login}/tasks, devuelve una lista en formato JSON de las tareas creadas por dicho usuario. Si el usuario existe, devolverá la lista y el HTTP STATUS del tipo `200 OK`, mientras que si no existe el usuario, se devolverá el estado `404 NOT FOUND`.
#### -Creación de una nueva tarea de un user
    POST /users/{login}/tasks
Este método, accediendo a la opción POST en localhost:9000/users/{login}/tasks, crea una nueva tarea asignándosela al usuario que se haya introducido por login. Si el user no existe, el programa devolverá el HTTP STATUS del tipo `404 NOT FOUND`, mientras que si existe en la BBDD, devolverá el `201 CREATED`.

Destacar que los únicos usuarios creados en la práctica son el anónimo y admin, con lo cual solo se podrán crear tareas para dichos usuarios.

```
insert into taskUser (id) values ('admin');
insert into taskUser (id) values ('anonimo');
```

Debido a estos cambios, es necesario modificar algunas funciones del feature 1, con lo cual, las funciones POST /task ahora se crearan de forma automática para el usuario "anónimo" y tasks ahora solo devuelve la lista de JSON del usuario "anonimo".


FEATURE 3
--------------

En esta ultima feature se nos pide que añadamos una fecha opcional de finalización de las tareas. Añadiremos las siguientes funcionalidades:

#### -Creación de tarea con fecha:
    POST /users/{login}/tasks/{date}
Este método, accediendo a la opción POST en localhost:9000/users/{login}/tasks/{date}, siendo el date en formato YYYY-MM-DD, crea una tarea para el usuario dado y con la fecha puesta. Si no existe el usuario, se devolverá el HTTP STATUS del tipo `404 NOT FOUND`, y si la fecha esta mal formulada, se devolverá el tipo `400 BAD REQUEST`. Si todo esta correcto, se devuelve el tipo `201 CREATED`.
#### -Consulta de tarea con fecha:
    GET /users/{login}/tasks/{date}
Este método, accediendo a la opción GET en localhost:9000/users/{login}/tasks/{date}, siendo el date en formato YYYY-MM-DD, lista todas las tareas generadas por dicho usuario y que tengan como fecha de finalización la dada. Si no existe el usuario, se devolverá el HTTP STATUS del tipo `404 NOT FOUND`, y si la fecha esta mal formulada, se devolverá el tipo `400 BAD REQUEST`. Si todo esta correcto, se devuelve la lista en JSON y el tipo `200 OK`.

Es opcional totalmente el crear una fecha de finalización, ya que si no la ponemos, se utilizará el POST y el GET del feature 2.
