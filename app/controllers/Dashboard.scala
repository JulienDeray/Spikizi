package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import core.SpectatorManager
import play.api.Routes


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

  lazy val token = System.currentTimeMillis().toString

  def login = Action { implicit request =>
    request.session.get("token").fold(
      Ok( views.html.dashboardLogin() )
    ) (
      token => Redirect( routes.Dashboard.dashboard() )
    )
  }

  def dashboard = Action { implicit request =>
//    request.session.get("token").fold(
//      {
//        Redirect( routes.Dashboard.login() )
//      }
//    ) (
//      adminToken => {
//        if ( adminToken == token ) {
          Ok( views.html.dashboard( SpectatorManager.getSpectators ) )
//        }
//        else {
//          Redirect( routes.Dashboard.login() )
//        }
//      }
//    )
  }

  def authentification = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Ok( views.html.dashboardLogin() )
      },
      user => {
        if ( user._1 == "admin" && user._2 == "admin" )
          Redirect( routes.Dashboard.dashboard() ).withSession( session + ("token" -> token) )
        else {
          Ok( views.html.dashboardLogin() )
        }
      }
    )
  }

  def logout = Action { implicit request =>
    Redirect( routes.Dashboard.login() ).withSession( session - "token" )
  }

  def newSpeaker(userName: String) = Action {
    SpectatorManager.setSpeaker( userName )
    Ok
  }

  def userIcon(userName: String) = Action {
    Ok( views.html.dashboardStates( SpectatorManager.getSpectator( userName ) ) )
  }

  def userTemplate(userName: String) = Action {
    Ok( views.html.dashboardUser( SpectatorManager.getSpectator( userName ) ) )
  }

  def removeSpeaker(userName: String) = Action {
    SpectatorManager.removeSpeaker(userName)
    Ok
  }

  def javascriptRoutes = Action { implicit request =>
    Ok(
      Routes.javascriptRouter("jsRoutes")(
        routes.javascript.Dashboard.newSpeaker,
        routes.javascript.Dashboard.userIcon,
        routes.javascript.Dashboard.userTemplate,
        routes.javascript.Dashboard.removeSpeaker
      )
    ).as("text/javascript")
  }
}
