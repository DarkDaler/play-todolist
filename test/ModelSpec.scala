import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

import models.Task


@RunWith(classOf[JUnitRunner])
class ModelSpec extends Specification {

   def dateIs(date: java.util.Date, str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) == str  
   def strToDate(str: String) = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(str)

  "Models FEATURE1" should {

        //TESTS FEATURE 1

        "Test tasks vacias FEATURE 1" in {  
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

                // La BD está vacía  
                val tasks = Task.all()

                tasks.isEmpty must equalTo(true)
            }
        }
        "Test busqueda task en vacia FEATURE 1" in {
             running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                //La BD está vacia
                val tasks = Task.getTasks(1)

                tasks.isEmpty must equalTo(true)
             }
        }
        "Test crear una task y checkear que se ha creado FEATURE 1" in {
             running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "anonimo")

                val tasks = Task.all()

                tasks.length must equalTo(1)

                tasks.head.id must equalTo(1)
                tasks.head.label must equalTo("prueba1")

             }
        }
        "Test checkear con getTasks(id) una tarea creada FEATURE 1" in {
             running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "anonimo")

                val tasks = Task.getTasks(1)

                tasks.length must equalTo(1)

                tasks.head.id must equalTo(1)
                tasks.head.label must equalTo("prueba1")

             }
        }
        "Test crear varias tareas FEATURE1" in {
             running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "anonimo")
                Task.create("prueba2", "anonimo")
                Task.create("prueba3", "anonimo")
                Task.create("prueba4", "anonimo")

                val tasks = Task.all()

                tasks.length must equalTo(4)

                tasks.last.label must equalTo("prueba4")

             }
        }
        "Test crear y eliminar una tarea FEATURE1" in {
             running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "anonimo")

                val tasks = Task.all()

                tasks.length must equalTo(1)

                Task.delete(1)

                val tasksNew = Task.all()

                tasksNew.length must equalTo(0)
             }
        }
        "Test eliminar una tarea inexistente FEATURE1" in {
             running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "anonimo")

                val tasks = Task.all()

                tasks.length must equalTo(1)

                Task.delete(2)

                val tasksNew = Task.all()

                tasksNew.length must equalTo(1)

             }
        }

   } 

   "Models FEATURE2" should {

        "Test task index vacia al insertarle en usuario registrado" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "admin")

                val tasks = Task.all()

                tasks.length must equalTo(0)
            }

        }

        "Test inserta 2 task a admin hacer getTaskUser(user)" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "admin")
                Task.create("prueba2", "admin")

                Task.create("pruebaAnonima", "anonimo")

                val tasksAnonimo = Task.all()

                tasksAnonimo.length must equalTo(1)

                val tasksAdmin = Task.getTaskUser("admin")

                tasksAdmin.length must equalTo(2)

                tasksAdmin.head.label must equalTo("prueba1")
                tasksAdmin.last.label must equalTo("prueba2")

            }
        }

        "Test eliminar una task a usuario registrado" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.create("prueba1", "admin")
                Task.create("prueba2", "admin")

                Task.create("pruebaAnonima", "anonimo") 

                Task.delete(2)

                val tasksAnonimo = Task.all()

                tasksAnonimo.length must equalTo(1)

                val tasksAdmin = Task.getTaskUser("admin")

                tasksAdmin.length must equalTo(1) 
                tasksAdmin.head.label must equalTo("prueba1")
                tasksAdmin.last.label must equalTo("prueba1")

            }
        }

   }
}
