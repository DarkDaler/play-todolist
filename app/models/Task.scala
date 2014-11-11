package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {
   //GENERA LA VAriABLE TASK CON LOS CAMPOS ID Y LABEL
   val task = {
      get[Long]("id") ~
      get[String]("label") map {
         case id~label => Task(id, label)
      }
   }

   //DEVUELVE LAS TAREAS CON EL USER PASADO
   def getTaskUser(idUser: String) : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where idUser = {idUser}").on(
            'idUser -> idUser
         ).as(task *)
      }
   }

   //DEVUEVE LA TAREA CON UN ID
   def getTasks(id: Long) : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where id = {id}").on(
            'id -> id
         ).as(task *)
      }
   }

   //DEVUELVE LA TAREA CON EL ID MAS ALTO
   def getTask() : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where id = (select MAX(id) from task)").on(
            ).as(task *)
      }
   }

   //DEVUELVE UNA TAREA CON UNA FECHA ASIGNADA
   def getTaskUserData(idUser: String, fecha: String) : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where (idUser,fecha) = ({idUser},{fecha})").on(
            'idUser -> idUser,
            'fecha -> fecha
         ).as(task *)
      }
   }

   //DEVUELVE TODAS LAS TAREAS CREADAS POR EL USER ANONIMO
   def all(): List[Task] = DB.withConnection {implicit c =>
      SQL("select * from task where idUser = 'anonimo'").as(task *)
   }

   //DEVUELVE 1 SI EL USUARIO DADO EXISTE, Y 0 SI NO EXISTE
   def verifyUser(id: String) : Long = {
      DB.withConnection {implicit c =>
         SQL("select count(*) from taskUser where id = {id}").on(
            'id -> id
         ).as(scalar[Long].single)
      }
   }

   //METODO QUE CREA UNA TAREA CON UN LABEL Y ASIGNANDOSELO A UN USER
   def create(label: String, user: String){
      DB.withConnection { implicit c =>
         SQL("insert into task (label,idUser) values ({label},{user})").on(
            'label -> label,
            'user -> user
         ).executeUpdate()
      }
   }

   //METODO QUE CREA UNA TAREA CON UN LABEL, ASIGNANDOSELO A UN USER, Y PONIENDOLE
   //UNA FECHA
   def createDate(label: String, user: String, fecha: String){
      DB.withConnection { implicit c =>
         SQL("insert into task (label,idUser,fecha) values ({label},{user},{fecha})").on(
            'label -> label,
            'user -> user,
            'fecha -> fecha
         ).executeUpdate()
      }
   }

   //METODO QUE ELIMINA UNA TAREA DADO SU ID
   def delete(id: Long) {
      DB.withConnection { implicit c =>
         SQL("delete from task where id = {id}").on(
            'id -> id
         ).executeUpdate()
      }
   }

   def listarCategorias(user: String): List[String] = {
      DB.withConnection {implicit c =>
         SQL("select categoria from categoria_users where user = {user}").on(
            'user -> user
         ).as(get[String]("categoria") *)
      }
   }

   def createCategoria(user: String, categoria: String){
      DB.withConnection { implicit c =>
         SQL("insert into categoria (categoria) values ({categoria})").on(
            'categoria -> categoria
         ).executeUpdate()
         SQL("insert into categoria_users (user,categoria) values ({user},{categoria})").on(
            'user -> user,
            'categoria -> categoria
         ).executeUpdate()
      }
   }

   def verifyCategoria(user: String, categoria: String): Long = {
      DB.withConnection {implicit c =>
         SQL("select count(*) from categoria_users where (user,categoria) = ({user},{categoria})").on(
            'user -> user,
            'categoria -> categoria
         ).as(scalar[Long].single)
      }
   }

   def listarTareasCategoria(idUser: String, categoriaTask: String) : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where (idUser,categoriaTask) = ({idUser},{categoriaTask})").on(
            'idUser -> idUser,
            'categoriaTask -> categoriaTask
         ).as(task *)
      }
   }
}