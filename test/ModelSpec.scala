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

  "Models" should {

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
  
}
