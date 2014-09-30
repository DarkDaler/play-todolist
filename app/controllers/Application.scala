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
    val json = Json.toJson(Task.getTasks(id))
    Ok(json)
  }

  def newTask = Action { implicit request =>
  taskForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Task.all(), errors)),
    label => {
      Task.create(label)
      val json = Json.toJson(Task.getTask())
      Created(json)
    }
  )
}

  def deleteTask(id: Long) = Action {
    if(Task.getTasks(id) != Nil){
      Task.delete(id)
      Redirect(routes.Application.tasks)    
    }
    else{
      NotFound
    }
  }

  val taskForm = Form(
      "label" -> nonEmptyText
   )

}