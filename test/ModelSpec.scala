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
   "Models FEATURE3" should {

        "Test index vacio al insertar tarea de usuario con fecha" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){


                Task.createDate("prueba1", "admin", "1991-04-17")

                val tasks = Task.all()

                tasks.length must equalTo(0)
            }
        }

        "Test inserta 2 task a admin y 1 a anonimo con fecha y hacer getTaskUserDate(user, date)" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createDate("prueba1", "admin", "1991-04-17")
                Task.createDate("prueba2", "admin", "1976-08-20")

                Task.createDate("pruebaAnonima", "anonimo", "1902-07-17")

                val tasksAnonimo = Task.all()

                tasksAnonimo.length must equalTo(1)

                val tasksAdmin = Task.getTaskUserData("admin", "1991-04-17")
                val tasksAdmin2 = Task.getTaskUserData("admin", "1976-08-20")

                tasksAdmin.length must equalTo(1)
                tasksAdmin2.length must equalTo(1)

            }
        }

        "Test eliminar una task a usuario registrado y que tenga fecha" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createDate("prueba1", "admin", "1991-04-17")
                Task.createDate("prueba2", "admin", "1976-08-20")

                Task.createDate("pruebaAnonima", "anonimo", "1902-07-17")

                Task.delete(2)

                val tasksAnonimo = Task.getTaskUserData("anonimo", "1902-07-17")

                tasksAnonimo.length must equalTo(1)

                val tasksAdmin = Task.getTaskUserData("admin", "1976-08-20")

                tasksAdmin.length must equalTo(0) 
            }
        }

        "Test insertar 2 task con fecha a usuario registrado y hacer getTaskUser(user)" in {
                running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createDate("prueba1", "admin", "1991-04-17")
                Task.createDate("prueba2", "admin", "1976-08-20")

                val tasksAdmin = Task.getTaskUser("admin")

                tasksAdmin.length must equalTo(2)
                tasksAdmin.head.label must equalTo("prueba1")
                tasksAdmin.last.label must equalTo("prueba2")
            }
        }
    }
    "TDD Models Practica2" should {
        "Test listar todas las categorias disponibles de admin vacio" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                val categorias = Task.listarCategorias("admin")

                categorias.length must equalTo(0)
            }
        }
        "Test crear dos categorias para admin" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")
                Task.createCategoria("admin", "informatica")

                val categorias = Task.listarCategorias("admin")

                categorias.length must equalTo(2)
            }
        }
        "Test verificar si una categoria existe para un usuario" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")
                Task.createCategoria("admin", "informatica")

                val verificacion = Task.verifyCategoria("admin", "informatica")

                verificacion must equalTo(1)
            }
        }
        "Test buscar una categoria no creada para un usuario" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")
                Task.createCategoria("admin", "informatica")

                val verificacion = Task.verifyCategoria("admin", "noExistente")

                verificacion must equalTo(0)
            }
        }
        "Test listar tareas vacias con categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")

                val tasks = Task.listarTareasCategoria("admin", "medicina")

                tasks.length must equalTo(0)
            }
        }
        "Test crear dos tareas con una categoria para admin" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")
                Task.createTaskCategoria("comprar  bisturis", "admin", "medicina")
                Task.createTaskCategoria("limpiar quirofano", "admin", "medicina")

                val tasks = Task.listarTareasCategoria("admin", "medicina")

                tasks.length must equalTo(2)
            }
        }
        "Test crear una tarea para una categoria y otra para otra categoria" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")
                Task.createCategoria("admin", "informatica")

                Task.createTaskCategoria("comprar  bisturis", "admin", "medicina")
                Task.createTaskCategoria("limpiar clusters", "admin", "informatica")

                val tasks = Task.listarTareasCategoria("admin", "medicina")
                val tasks2 = Task.listarTareasCategoria("admin", "informatica")

                tasks.length must equalTo(1)
                tasks2.length must equalTo(1)
            }
        }
        "Test listar tareas de un usuario sin categorias y otro con categorias" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")

                Task.createTaskCategoria("comprar  bisturis", "admin", "medicina")

                val tasks = Task.listarTareasCategoria("admin", "medicina")
                val tasks2 = Task.listarTareasCategoria("anonimo", "noRegistrado")

                tasks.length must equalTo(1)
                tasks2.length must equalTo(0)
            }
        }
        "Test modificar campos de una tarea con categoria de un user" in {
            running(FakeApplication(additionalConfiguration = inMemoryDatabase())){

                Task.createCategoria("admin", "medicina")

                Task.createTaskCategoria("comprar  bisturis", "admin", "medicina")

                val tasks = Task.listarTareasCategoria("admin", "medicina")
                tasks.length must equalTo(1)
                tasks.head.label must equalTo("comprar bisturis")

                Task.modifyTaskCategoria("comprar bisturis", "limpiar quirofano", "admin", "medicina")

                val tasks2 = Task.listarTareasCategoria("admin", "medicina")
                tasks2.length must equalTo(1)
                tasks2.head.label must equalTo("limpiar quirofano")
            }
        }

    }
}
