package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Task(id: Long, label: String)

object Task {

   val task = {
      get[Long]("id") ~
      get[String]("label") map {
         case id~label => Task(id, label)
      }
   }

   def getTasksUser(idUser: String) : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where idUser = {idUser}").on(
            'idUser -> idUser
            ).as(task *)
      }
   }
   def getTasks(id: Long) : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where (id,idUser) = ({id},'anonimo')").on(
            'id -> id
            ).as(task *)
      }
   }

   def getTask() : List[Task] = {
      DB.withConnection {implicit c =>
         SQL("select * from task where id = (select MAX(id) from task)").on(
            ).as(task *)
      }
   }



   def all(): List[Task] = DB.withConnection {implicit c =>
      SQL("select * from task").as(task *)
   }

   def create(label: String, user: String){
      DB.withConnection { implicit c =>
         SQL("insert into task (label,idUser) values ({label},{user})").on(
            'label -> label,
            'user -> user
         ).executeUpdate()
      }
   }

   def delete(id: Long) {
      DB.withConnection { implicit c =>
         SQL("delete from task where id = {id}").on(
            'id -> id
         ).executeUpdate()
      }
   }
}