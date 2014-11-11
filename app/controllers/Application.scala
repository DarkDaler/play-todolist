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
      val json = Json.toJson(Task.getTaskUser(user))
      Ok(json)
    }
    else{
      NotFound("Usuario no encontrado")
    }
  }

  //METODO QUE DEVUELVE TODAS LAS TAREAS DE UN USUARIO CON UNA FECHA DADA
  def getTaskUserDate(user: String, date: String) = Action {
    if(Task.verifyUser(user) == 1){
      if(date(4) == '-' && date(7) == '-'){
        if((date.substring(5,7)).toInt >= 1 && (date.substring(5,7)).toInt <= 12){
          if((date.substring(8,10)).toInt >= 1 && (date.substring(8,10)).toInt <= 31){
            val json = Json.toJson(Task.getTaskUserData(user,date))
            Ok(json)
          }
          else{
            BadRequest("Introduce un dia valido entre 1 y 31")
          }
        }
        else{
          BadRequest("Introduce un mes valido entre 1 y 12")
        }  
      }
      else{
        BadRequest("Formato de fecha admitido YYYY-MM-DD")
      }
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

  //METODO QUE CREA UNA TAREA CON UN USER Y UNA FECHA POR 
  //LA RUTA /users/{login}/tasks/YYYY-MM-DD
  def newTaskUserDate(user: String, date: String) = Action { implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        if(Task.verifyUser(user) == 1){
          if(date(4) == '-' && date(7) == '-'){
            if((date.substring(5,7)).toInt >= 1 && (date.substring(5,7)).toInt <= 12){
              if((date.substring(8,10)).toInt >= 1 && (date.substring(8,10)).toInt <= 31){
                Task.createDate(label,user,date)
                val json = Json.toJson(Task.getTask())
                Created(json)
              }
              else{
                BadRequest("Introduce un dia valido entre 1 y 31")
              }
            }
            else{
              BadRequest("Introduce un mes valido entre 1 y 12")
            }  
          }
          else{
            BadRequest("Formato de fecha admitido YYYY-MM-DD")
          }
          
        }
        else{
          NotFound("Usuario no encontrado")
        }  
      } 
    )
  }

  def newUserCat(user: String, categoria: String) = Action {
    if(Task.verifyCategoria(user, categoria) == 0){
      if(Task.categoriaExists(categoria) == 0){
        Task.createCategoria(user, categoria)
        Created("Categoria creada correctamente")
      }
      else{
        BadRequest("HHH")
      }
    }
    else{
      BadRequest("Categoria ya creada en el usuario")
    }
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