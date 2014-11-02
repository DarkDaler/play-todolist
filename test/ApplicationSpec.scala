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

  "Application" should {

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

  }
}
