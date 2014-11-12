package test

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application FEATURE1" should {

     //FEATURE 1

     "Envio 404 de peticion erronea" in {
        running(FakeApplication()){
          route(FakeRequest(GET, "/erronea")) must beNone
        }
     }

     "Prueba pagina index" in {
        running(FakeApplication()){
          val Some(home) = route(FakeRequest(GET, "/"))

          status(home) must equalTo(OK)  
          contentType(home) must beSome.which(_ == "text/html")   
        }
     }

     "Prueba pagina task vacia FEATURE 1" in {
        running(FakeApplication()){
          val Some(tasks) = route(FakeRequest(GET, "/tasks"))

          status(tasks) must equalTo(OK)
          val json = contentAsJson(tasks)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[]")
        }
     }

     "Prueba introducir 1 tarea FEATURE 1" in {
        running(FakeApplication()){

          val Some(tasks) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba"))

          status(tasks) must equalTo(CREATED)
          val json =contentAsJson(tasks)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":1,\"label\":\"prueba\"}]")
        }
     } 

     "Prueba introducir varias tareas FEATURE 1" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)

          val Some(post2) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba2"))
          status(post2) must equalTo(CREATED)

          val Some(post3) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba3"))
          status(post3) must equalTo(CREATED)

          val Some(post4) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba4"))
          status(post4) must equalTo(CREATED)
          
          val Some(tasks) = route(FakeRequest(GET, "/tasks"))

          status(tasks) must equalTo(OK)
          val json =contentAsJson(tasks)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":1,\"label\":\"prueba1\"},{\"id\":2,\"label\":\"prueba2\"},{\"id\":3,\"label\":\"prueba3\"},{\"id\":4,\"label\":\"prueba4\"}]")
        }
      }

      "Prueba getTask(id) de tarea existente FEATURE1" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)

          val Some(getTask) = route(FakeRequest(GET, "/tasks/1"))

          status(getTask) must equalTo(OK)
          val json =contentAsJson(getTask)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")
        }
      }

      "Prueba getTask(id) de tarea no existente FEATURE1" in {
        running(FakeApplication()){

          val Some(getTask) = route(FakeRequest(GET, "/tasks/1"))

          status(getTask) must equalTo(404)
          contentAsString(getTask) must contain("No existe la tarea")
        }
      }

      "Prueba eliminar una tarea FEATURE1" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)

          val Some(post2) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba2"))
          status(post2) must equalTo(CREATED)

          val Some(post3) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba3"))
          status(post3) must equalTo(CREATED)

          val Some(deleteTask) = route(FakeRequest(DELETE, "/tasks/1"))
          redirectLocation(deleteTask) must beSome.which(_ == "/tasks")

          val Some(tasks) = route(FakeRequest(GET, "/tasks"))
          val json =contentAsJson(tasks)
          var jsonString = Json.stringify(json)
          jsonString must equalTo("[{\"id\":2,\"label\":\"prueba2\"},{\"id\":3,\"label\":\"prueba3\"}]")
        }
      }

      "Prueba a eliminar una tarea inexistente FEATURE1" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)

          val Some(deleteTask) = route(FakeRequest(DELETE, "/tasks/2"))
          status(deleteTask) must equalTo(404)
          contentAsString(deleteTask) must contain("No existe la tarea")

          val Some(tasks) = route(FakeRequest(GET, "/tasks"))
          val json =contentAsJson(tasks)
          var jsonString = Json.stringify(json)
          jsonString must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")
        }
      }

  }

  "Application FEATURE2" should {
      "Prueba tasks vacias de user admin y anonimo FEATURE 2" in{
        running(FakeApplication()){

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[]")

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[]")

        }
      }

      "Prueba insertar un usuario en admin FEATURE 2" in {
         running(FakeApplication()){

          val Some(post) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post) must equalTo(CREATED)

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[]")

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")

         }
      }

      "Prueba buscar tareas de un usuario no registrado FEATURE2" in {
        running(FakeApplication()){

          val Some(post) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post) must equalTo(CREATED)

          val Some(tasks) = route(FakeRequest(GET, "/users/noRegistrado/tasks"))
          status(tasks) must equalTo(404)
          contentAsString(tasks) must contain("Usuario no encontrado")
        }

      }

      "Prueba insertar 2 usuarios en admin y 1 en anonimo FEATURE2" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)
          
          val Some(post2) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba2"))
          status(post2) must equalTo(CREATED)
          
          val Some(post3) = route(FakeRequest(POST, "/users/anonimo/tasks").withFormUrlEncodedBody("label" -> "prueba3"))
          status(post3) must equalTo(CREATED)

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":3,\"label\":\"prueba3\"}]")

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[{\"id\":1,\"label\":\"prueba1\"},{\"id\":2,\"label\":\"prueba2\"}]")
        }
      }

      "Prueba insertar 2 en admin, 2 en anonimo, y eliminar 1 de admin FEATURE2" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)
          
          val Some(post2) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba2"))
          status(post2) must equalTo(CREATED)
          
          val Some(post3) = route(FakeRequest(POST, "/users/anonimo/tasks").withFormUrlEncodedBody("label" -> "prueba3"))
          status(post3) must equalTo(CREATED)

          val Some(post4) = route(FakeRequest(POST, "/users/anonimo/tasks").withFormUrlEncodedBody("label" -> "prueba4"))
          status(post4) must equalTo(CREATED)

          val Some(deleteAdmin) = route(FakeRequest(DELETE, "/tasks/2"))
          redirectLocation(deleteAdmin) must beSome.which(_ == "/tasks")

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":3,\"label\":\"prueba3\"},{\"id\":4,\"label\":\"prueba4\"}]")

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")
          
        }
      }
  }
  "Application FEATURE3" should {

      "Prueba insertar una task para admin FEATURE 3" in{
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks/1991-04-17").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks/1991-04-17"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[]")
        }
      }
      "Prueba insertar una task con una fecha de formato incorrecto FEATURE 3" in{
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks/04-1991-17").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(400)
          contentAsString(post1) must contain("Formato de fecha admitido YYYY-MM-DD")
        }
      }
      "Prueba insertar una task con una fecha con día incorrecto FEATURE 3" in{
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks/1991-04-50").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(400)
          contentAsString(post1) must contain("Introduce un dia valido entre 1 y 31")
        }
      }
      "Prueba insertar una task con una fecha con mes incorrecto FEATURE 3" in{
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks/1991-13-20").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(400)
          contentAsString(post1) must contain("Introduce un mes valido entre 1 y 12")
        }
      }
      "Prueba insertar una task con una fecha con user incorrecto FEATURE 3" in{
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/malUser/tasks/1991-10-20").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(404)
          contentAsString(post1) must contain("Usuario no encontrado")
        }
      }
      "Prueba buscar tareas con date de un usuario no registrado FEATURE3" in {
        running(FakeApplication()){

          val Some(post) = route(FakeRequest(POST, "/users/admin/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post) must equalTo(CREATED)

          val Some(tasks) = route(FakeRequest(GET, "/users/noRegistrado/tasks/1991-10-20"))
          status(tasks) must equalTo(404)
          contentAsString(tasks) must contain("Usuario no encontrado")
        }
      }
      "Prueba insertar 2 usuarios en admin y 1 en anonimo FEATURE3" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks/1991-10-20").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)
          
          val Some(post2) = route(FakeRequest(POST, "/users/admin/tasks/1991-08-17").withFormUrlEncodedBody("label" -> "prueba2"))
          status(post2) must equalTo(CREATED)
          
          val Some(post3) = route(FakeRequest(POST, "/users/anonimo/tasks/1991-06-10").withFormUrlEncodedBody("label" -> "prueba3"))
          status(post3) must equalTo(CREATED)

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":3,\"label\":\"prueba3\"}]")

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[{\"id\":1,\"label\":\"prueba1\"},{\"id\":2,\"label\":\"prueba2\"}]")
        }
      }
      "Prueba insertar 2 en admin, 2 en anonimo, y eliminar 1 de admin FEATURE3" in {
        running(FakeApplication()){

          val Some(post1) = route(FakeRequest(POST, "/users/admin/tasks/1991-10-20").withFormUrlEncodedBody("label" -> "prueba1"))
          status(post1) must equalTo(CREATED)
          
          val Some(post2) = route(FakeRequest(POST, "/users/admin/tasks/1991-09-09").withFormUrlEncodedBody("label" -> "prueba2"))
          status(post2) must equalTo(CREATED)
          
          val Some(post3) = route(FakeRequest(POST, "/users/anonimo/tasks/1991-08-08").withFormUrlEncodedBody("label" -> "prueba3"))
          status(post3) must equalTo(CREATED)

          val Some(post4) = route(FakeRequest(POST, "/users/anonimo/tasks/1991-07-07").withFormUrlEncodedBody("label" -> "prueba4"))
          status(post4) must equalTo(CREATED)

          val Some(deleteAdmin) = route(FakeRequest(DELETE, "/tasks/2"))
          redirectLocation(deleteAdmin) must beSome.which(_ == "/tasks")

          val Some(tasksAnonimo) = route(FakeRequest(GET, "/users/anonimo/tasks"))
          status(tasksAnonimo) must equalTo(OK)

          val json = contentAsJson(tasksAnonimo)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":3,\"label\":\"prueba3\"},{\"id\":4,\"label\":\"prueba4\"}]")

          val Some(tasksAdmin) = route(FakeRequest(GET, "/users/admin/tasks"))
          status(tasksAdmin) must equalTo(OK)

          val json2 = contentAsJson(tasksAdmin)
          var jsonString2 = Json.stringify(json2)

          jsonString2 must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")

          val Some(tasksAdmin2) = route(FakeRequest(GET, "/users/admin/tasks/1991-09-09"))
          status(tasksAdmin2) must equalTo(OK)

          val json3 = contentAsJson(tasksAdmin2)
          var jsonString3 = Json.stringify(json3)

          jsonString3 must equalTo("[]")
        }
      }
  }
  "TDD Application Practica2" should {
    "Prueba crear categoria no existente al usuario anonimo" in {
        running(FakeApplication()){

          val Some(categoriaAnonimo) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina"))
          status(categoriaAnonimo) must equalTo(CREATED)

          contentAsString(categoriaAnonimo) must contain("Categoria creada correctamente")
        }
    }
    "Prueba crear categoria ya existente al usuario anonimo" in {
        running(FakeApplication()){
          val Some(categoriaAnonimo) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina"))
          status(categoriaAnonimo) must equalTo(CREATED)
          val Some(categoriaAnonimo2) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina"))
          status(categoriaAnonimo2) must equalTo(400)

          contentAsString(categoriaAnonimo2) must contain("Categoria ya creada en el usuario")
        }
    }
    "Prueba a insertar una categoria a 2 usuarios distintos" in {
        running(FakeApplication()){
          val Some(categoriaAnonimo) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina"))
          status(categoriaAnonimo) must equalTo(CREATED)
          val Some(categoriaAnonimo2) = route(FakeRequest(POST, "/users/admin/categoria/medicina"))
          status(categoriaAnonimo2) must equalTo(CREATED)

          contentAsString(categoriaAnonimo2) must contain("Categoria creada correctamente")
        }
    }
    "Prueba a listar tareas de una categoria vacia" in {
        running(FakeApplication()){

          val Some(categoriaAnonimo) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina"))
          status(categoriaAnonimo) must equalTo(CREATED)
          
          val Some(taskCategoria) = route(FakeRequest(GET, "/users/anonimo/categoria/medicina/tasks"))     
          status(taskCategoria) must equalTo(OK)

          val json = contentAsJson(taskCategoria)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[]")
        }
    }
    "Prueba a listar tareas de una categoria no existente" in {
        running(FakeApplication()){
          
          val Some(taskCategoria) = route(FakeRequest(GET, "/users/anonimo/categoria/medicina/tasks"))     
          status(taskCategoria) must equalTo(400)
          contentAsString(taskCategoria) must contain("Categoria no encontrada o no vinculada al usuario")
        }
    }
    "Prueba a listar tareas de un usuario no existente" in {
        running(FakeApplication()){
          
          val Some(taskCategoria) = route(FakeRequest(GET, "/users/noExiste/categoria/medicina/tasks"))     
          status(taskCategoria) must equalTo(404)
          contentAsString(taskCategoria) must contain("Usuario no encontrado")
        }
    }
    "Prueba a crear 1 tarea y listarla en la categoria medicina del user anonimo" in {
        running(FakeApplication()){

          val Some(categoriaAnonimo) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina"))
          status(categoriaAnonimo) must equalTo(CREATED)

          val Some(postTaskCategoria) = route(FakeRequest(POST, "/users/anonimo/categoria/medicina/tasks").withFormUrlEncodedBody("label" -> "prueba1"))
          status(postTaskCategoria) must equalTo(CREATED)
          
          val Some(taskCategoria) = route(FakeRequest(GET, "/users/anonimo/categoria/medicina/tasks"))     
          status(taskCategoria) must equalTo(OK)

          val json = contentAsJson(taskCategoria)
          var jsonString = Json.stringify(json)

          jsonString must equalTo("[{\"id\":1,\"label\":\"prueba1\"}]")
        }
    }    
  }
}
