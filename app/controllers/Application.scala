package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import core.SpectatorManager

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

  lazy val token = System.currentTimeMillis()

  // --- Dashboard ---

  def dashboardLogin = Action { implicit request =>
    request.session.get("token").fold(
      Ok( views.html.dashboardLogin() )
    ) (
      token => Redirect( routes.Application.dashboard() )
    )
  }

  def dashboard = Action { implicit request =>
    request.session.get("token").fold(
      Redirect( routes.Application.dashboardLogin() )
    ) (
      admin => {
        if ( admin == token.toString )
          Ok( views.html.dashboard( SpectatorManager.getSpectators ) )
        else
          Redirect( routes.Application.dashboardLogin() )
      }
    )
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
        Ok( views.html.dashboardLogin() )
      },
      user => {
        if ( user._1 == "admin" && user._2 == "admin" )
          Redirect( routes.Application.dashboard() ).withSession( "token" -> token.toString )
        else {
          Ok( views.html.dashboardLogin() )
        }
      }
    )
  }

  def dashboardLogout = Action {
    Redirect( routes.Application.dashboardLogin() ).withNewSession
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
          SpectatorManager.addSpectator( userName )
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
          SpectatorManager.delSpectator( userName )
          Redirect( routes.Application.mobile() ).withNewSession
        }
      )
    }
  }
}