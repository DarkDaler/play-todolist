package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import models.Task

object Application extends Controller {

  //FUNCION QUE DA FORMATO A NUESTRO JSON
  implicit val jsonWriter : Writes[Task] = (
    (JsPath \  "id").write[Long] and
    (JsPath \ "label").write[String]
    )(unlift(Task.unapply))

  //METODO QUE CARGA LA PAGINA PRINCIPAL CON EL FORMULARIO Y TODAS LAS TAREAS
  def index = Action {
      Ok(views.html.index(Task.all(), taskForm))
   }

  //METODO QUE DEVUELVE TODAS LAS TAREAS CREADAS POR TODOS LOS USUARIOS
  def tasks = Action {
      val json = Json.toJson(Task.all())
      Ok(json)
   }

  //METODO QUE DEVUELVE UNA TAREA DADO SU ID. SI NO EXISTE DEVUELVE 404 NOT FOUND
  def getTask(id: Long) = Action {
    if(Task.getTasks(id) != Nil){
      val json = Json.toJson(Task.getTasks(id))
      Ok(json)
    }
    else{
      NotFound("No existe la tarea")
    }
  }

  //METODO QUE DEVUELVE TODAS LAS TAREAS DE UN USER. SI NO EXISTE EL USER, 
  //DEVUELVE 404 NOT FOUND
  def getTaskUser(user: String) = Action {
    if(Task.verifyUser(user) == 1){
      val json = Json.toJson(Task.getTasksUser(user))
      Ok(json)
    }
    else{
      NotFound("Usuario no encontrado")
    }
  }

  //METODO QUE CREA UNA TAREA DADA LA RUTA /tasks, Y QUE 
  //POR DEFECTO SE LA ASIGNA A ANONIMO
  def newTask = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label, "anonimo")
        val json = Json.toJson(Task.getTask())
        Created(json)
      }
    )
  }

  //METODO QUE CREA UNA TAREA ASIGNANDOLE UN USER POR LA RUTA /users/{login}. 
  //SI NO EXISTE EL USER DEVUELVE 404 NOT FOUND
  def newTaskUser(user: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        if(Task.verifyUser(user) == 1){
          Task.create(label,user)
          val json = Json.toJson(Task.getTask())
          Created(json)
        }
        else{
          NotFound("Usuario no encontrado")
        }  
      } 
    )
  }

  def newTaskUserDate(user: String, date: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        if(Task.verifyUser(user) == 1){
          Task.createDate(label,user,date)
          val json = Json.toJson(Task.getTask())
          Created(json)
        }
        else{
          NotFound("Usuario no encontrado")
        }  
      } 
    )
  }

  //ELIMINA UNA TAREA DADO SU ID. SI NO EXISTE DEVUELVE 404 NOT FOUND
  def deleteTask(id: Long) = Action {
    if(Task.getTasks(id) != Nil){
      Task.delete(id)
      Redirect(routes.Application.tasks)    
    }
    else{
      NotFound("No existe la tarea")
    }
  }

  val taskForm = Form(
      "label" -> nonEmptyText
   )

}