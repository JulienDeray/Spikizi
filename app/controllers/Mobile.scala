package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import core.SpectatorManager

object Mobile extends Controller {

  val userName = Form(
    "name" -> nonEmptyText
  )

  // --- Mobile ---

  def login = Action { implicit request =>
    request.session.get("user").fold(
      Ok( views.html.mobileLogin() )
    ) (
      user => Redirect( routes.Mobile.dashboard( user ) )
    )
  }

  def dashboard(name: String) = Action { implicit request =>
    request.session.get("user").fold(
      Redirect( routes.Mobile.login() )
    ) (
      user =>
        if ( user == name )
          Ok( views.html.mobileSpeak( name ) )
        else
          Redirect( routes.Mobile.login() )
    )
  }

  def newUser = Action { implicit request =>
    request.session.get("user").fold(
      userName.bindFromRequest.fold(
        formWithErrors => {
          Redirect( routes.Mobile.login() ).flashing( "error" -> "Please fill correctly the form." )
        },
        userName => {
          if ( SpectatorManager.exists( userName ) )
            Redirect( routes.Mobile.login() ).flashing( "error" -> "This name is already used by somebody. Please use another one." )
          else {
            val user = SpectatorManager.addSpectator( userName )
            Redirect( routes.Mobile.dashboard( user.name ) ).withSession( session + ("user" -> user.name) )
          }
        }
      )
    ) (
      userName => Redirect( routes.Mobile.dashboard( userName ) )
    )
  }

  def logout = Action { implicit request => {
      request.session.get("user").fold(
        Redirect( routes.Mobile.login() )
      ) (
        userName => {
          SpectatorManager.delSpectator( userName )
          Redirect( routes.Mobile.login() ).withSession( session - "user" )
        }
      )
    }
  }
}