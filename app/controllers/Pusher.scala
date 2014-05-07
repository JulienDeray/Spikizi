package controllers

import play.api.mvc._
import play.api.libs.iteratee.{Iteratee, Concurrent}
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

object Pusher extends Controller {

  val (outDash, channelDash) = Concurrent.broadcast[String]
  val (outMob, channelMob) = Concurrent.broadcast[String]

  def sseDash() = WebSocket.using[String] {
    request =>
      //log the message to stdout and send response back to client
      val in = Iteratee.foreach[String] {
          msg => println(msg)
          //the Enumerator returned by Concurrent.broadcast subscribes to the channel and will
          //receive the pushed messages
          channelDash push ("RESPONSE: " + msg)
      }
      (in, outDash)
  }

  def sseMob() = WebSocket.using[String] {
    request =>
      //log the message to stdout and send response back to client
      val in = Iteratee.foreach[String] {
          msg => println(msg)
          //the Enumerator returned by Concurrent.broadcast subscribes to the channel and will
          //receive the pushed messages
          channelMob push ("RESPONSE: " + msg)
      }
      (in, outMob)
  }

  def pushNewUser(userName: String) = {
    channelDash push "{ \"command\" : \"newUser\", \"userName\" : \"" + userName + "\" }"
  }

  def pushDelUser(userName: String) = {
    channelDash push "{ \"command\" : \"delUser\", \"userName\" : \"" + userName + "\" }"
  }

  def updateUserState(userName: String) = {
    channelDash push "{ \"command\" : \"updateUserState\", \"userName\" : \"" + userName + "\" }"
  }
}
