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

  def getSpectator( name: String ) : Spectator = users.find( user => user.name == name ).getOrElse(null)

  def addSpectator( name: String ) = {
    val spectator = new Spectator( name )
    users = spectator :: users
    Pusher.pushNewUser( name )
    spectator
  }

  def delSpectator( name: String ) {
    users = users.filterNot( user => user.name == name )
    Pusher.pushDelUser( name )
  }

  def getSpectators = users

  def setSpeaker( spectator: String ) {
    speaker = getSpectator( spectator )
    speaker.setSpeaking()
  }

  def removeSpeaker() {
    setSpeaker( null )
  }

  def exists( name: String ) : Boolean = users.exists( user => user.name == name )
}
