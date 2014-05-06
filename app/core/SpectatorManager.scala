package core

import controllers.Pusher
import model.Spectator

/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 28/04/2014
 * Time: 17:12
 */

object SpectatorManager {

  private var users: List[Spectator] = Nil
  private var speaker: Spectator = null

  def addSpectator( name: String ) {
    users = new Spectator( name ) :: users
    Pusher.pushNewUser( name )
  }

  def delSpectator( name: String ) {
    users = users.filterNot( user => user.name == name)
    Pusher.pushDelUser( name )
  }

  def getSpectators = users

  def setSpeaker( spectator: Spectator ) {
    speaker = spectator
  }

  def removeSpeaker() {
    setSpeaker( null )
  }

  def exists( name: String ) : Boolean = users.exists( user => user.name == name )
}
