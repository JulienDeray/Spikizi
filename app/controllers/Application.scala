package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import model.{SpectatorManager, Spectator}

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

  var sm = new SpectatorManager()

  // --- Dashboard ---

  def index = Action {
    Ok( views.html.index() )
  }

  def dashboard = Action {
    Ok( views.html.dashboard( sm.users ) )
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

  def mobile = Action { implicit request =>
    request.session.get("user").fold(
      Ok( views.html.mobile() )
    ) (
      user => Redirect( routes.Application.mobileSpeak( user ) )
    )
  }

  def mobileSpeak(name: String) = Action { implicit request =>
    request.session.get("user").fold(
      Redirect( routes.Application.mobile() )
    ) (
      user =>
        if ( user == name )
          Ok( views.html.mobileSpeak( name ) )
        else
          Redirect( routes.Application.mobile() )
    )
  }

  def newUser = Action { implicit request =>
    request.session.get("user").fold(
      userName.bindFromRequest.fold(
        formWithErrors => {
          BadRequest( "Invalid form" )
        },
        userName => {
          sm.addSpectator( userName )
          Redirect( routes.Application.mobileSpeak( userName ) ).withSession( "user" -> userName )
        }
      )
    ) (
      userName => Redirect( routes.Application.mobileSpeak( userName ) )
    )
  }

  def logout = Action { implicit request => {
      request.session.get("user").fold(
        Redirect( routes.Application.mobile() )
      ) (
        userName => {
          sm.delSpectator( userName )
          Redirect( routes.Application.mobile() ).withNewSession
        }
      )
    }
  }
}