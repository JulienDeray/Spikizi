package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import model.Spectator


object Application extends Controller {

  // Forms
  val loginForm = Form(
    tuple(
      "user" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  val userName = Form(
    "name" -> nonEmptyText
  )

  // -----

  var users: List[Spectator] = Nil

  // --- Dashboard ---

  def index = Action {
    Ok( views.html.index() )
  }

  def dashboard = Action {
    Ok( views.html.dashboard( users ) )
  }

  def webSocket() = Action {
    Ok( views.html.dashboardWebsocket() )
  }

  def interpretedJS() = Action {
    Ok( views.html.interpretedJS() )
  }

  def authentification = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Ok( views.html.index() )
      },
      user => {
        if ( user._1 == "admin" && user._2 == "admin" )
          Redirect( routes.Application.dashboard() )
        else {
          Ok( views.html.index() )
        }
      }
    )
  }

  // --- Mobile ---

  def mobile = Action {
    Ok( views.html.mobile() )
  }

  def mobileSpeak(name: String) = Action {
    Ok( views.html.mobileSpeak( name ) )
  }

  def newUser = Action { implicit request =>
    userName.bindFromRequest.fold(
      formWithErrors => {
        BadRequest( "Invalid form" )
      },
      userName => {
        users = new Spectator( userName ) :: users
        Pusher.pushNewUser(userName)
        Redirect( routes.Application.mobileSpeak( userName ) )
      }
    )
  }

}