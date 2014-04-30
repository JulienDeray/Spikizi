package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import core.SpectatorManager


/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 29/04/2014
 * Time: 11:41
 */

object Dashboard extends Controller {

  /**
   * Forms
   */
  val loginForm = Form(
    tuple(
      "user" -> nonEmptyText,
      "password" -> nonEmptyText
    )
  )

  lazy val token = System.currentTimeMillis()

  def login = Action { implicit request =>
    request.session.get("token").fold(
      Ok( views.html.dashboardLogin() )
    ) (
      token => Redirect( routes.Dashboard.dashboard() )
    )
  }

  def dashboard = Action { implicit request =>
    request.session.get("token").fold(
      Redirect( routes.Dashboard.dashboardLogin() )
    ) (
      admin => {
        if ( admin == token.toString )
          Ok( views.html.dashboard( SpectatorManager.getSpectators ) )
        else
          Redirect( routes.Dashboard.dashboardLogin() )
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
          Redirect( routes.Dashboard.dashboard() ).withSession( "token" -> token.toString )
        else {
          Ok( views.html.dashboardLogin() )
        }
      }
    )
  }

  def logout = Action {
    Redirect( routes.Dashboard.dashboardLogin() ).withNewSession
  }

}
