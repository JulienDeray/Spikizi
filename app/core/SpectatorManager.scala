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

  def getSpectator( name: String ) : Spectator = users.find( user => user.name == name ).orNull

  def addSpectator( name: String ) : Spectator = {
    val spectator = new Spectator( name )
    users = spectator :: users
    Pusher.pushNewUser( spectator.name )
    spectator
  }

  def delSpectator( name: String ) {
    users = users.filterNot( user => user.name == name )
    Pusher.pushDelUser( name )
  }

  def getSpectators = users

  def getSpeaker : Spectator = {
    users.find( user => user.state == State.Speaking ).orNull
  }

  def setSpeaker( userName: String ) {
    users.map { user =>
      if ( user.name == userName ) {
        user.setSpeaking()
        Pusher.setSpeaker( user.name )
      }
      else if ( user.state == State.Speaking ) {
        Pusher.setUserButtonToOff( user.name )
      }
    }
  }

  def removeSpeaker( userName: String ) {
    users.map { user =>
      if ( user.state == State.Speaking && user.name == userName ) {
        user.setPassive()
        Pusher.updateUserState( user.name )
        Pusher.removeSpeaker( user.name )
        Pusher.setUserButtonToOff( user.name )
      }
    }
  }

  def exists( name: String ) : Boolean = users.exists( user => user.name == name )

  def setWaiting( userName: String ) {
    users.map { user =>
      if ( user.name == userName ) {
        user.setWaiting()
        Pusher.setWaiting( user.name )
      }
    }
  }

  def setPassive( userName: String ) {
    users.map { user =>
      if ( user.name == userName ) {
        user.setPassive()
        Pusher.setPassive( user.name )
      }
    }
  }
}
