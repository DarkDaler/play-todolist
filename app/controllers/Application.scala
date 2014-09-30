package controllers



import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

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

  def newTask = Action { implicit request =>
  taskForm.bindFromRequest.fold(
    errors => BadRequest(views.html.index(Task.all(), errors)),
    label => {
      Task.create(label)
      Redirect(routes.Application.tasks)
    }
  )
}

  def deleteTask(id: Long) = Action {
    Task.delete(id)
    Redirect(routes.Application.tasks)
  }

  val taskForm = Form(
      "label" -> nonEmptyText
   )

}