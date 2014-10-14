package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json._
import play.api.libs.functional.syntax._

import models.Task

object Application extends Controller {

  implicit val jsonWriter : Writes[Task] = (
    (JsPath \  "id").write[Long] and
    (JsPath \ "label").write[String]
    )(unlift(Task.unapply))

  def index = Action {
      Ok(views.html.index(Task.all(), taskForm))
   }

  def tasks = Action {
      val json = Json.toJson(Task.all())
      Ok(json)
   }

  def getTask(id: Long) = Action {
    if(Task.getTasks(id) != Nil){
      val json = Json.toJson(Task.getTasks(id))
      Ok(json)
    }
    else{
      NotFound("No existe la tarea")
    }
  }

  def getTaskUser(user: String) = Action {
    if(Task.verifyUser(user) == 1){
      val json = Json.toJson(Task.getTasksUser(user))
      Ok(json)
    }
    else{
      NotFound("Usuario no encontrado")
    }
  }

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