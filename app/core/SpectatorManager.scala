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

//  private var users: List[Spectator] = Nil
  private var users: List[Spectator] = List(new Spectator("Boby"), new Spectator("Renaud"), new Spectator("Chaton"), new Spectator("Boris Vian"))
  private var speaker: Spectator = null

  def getSpectatorByName( userName: String ) : Spectator = users.find( user => user.name == userName ).getOrElse(null)
  def getSpectatorByHash( userName: String ) : Spectator = users.find( user => user.name == userName ).getOrElse(null)

  def addSpectator( name: String ) = {
    val spectator = new Spectator( name )
    users = spectator :: users
    Pusher.pushNewUser( name )
    spectator
  }

  def delSpectator( userName: String ) {
    val userHash = getSpectatorByName( userName ).hash
    users = users.filterNot( user => user.name == userName )
    Pusher.pushDelUser( userHash )
  }

  def getSpectators = users

  def setSpeaker( userHash: String ) {
    getSpectatorByHash( userHash ).setSpeaking()
  }

  def removeSpeaker() {
    setSpeaker( null )
  }

  def exists( name: String ) : Boolean = users.exists( user => user.name == name )
}
