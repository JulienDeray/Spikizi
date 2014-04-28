package model

import controllers.Pusher

/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 28/04/2014
 * Time: 17:12
 */

class SpectatorManager {

  var users: List[Spectator] = Nil

  def addSpectator( name: String ) {
    users = new Spectator( name ) :: users
    Pusher.pushNewUser( name )
  }

  def delSpectator( name: String ) {
    users = users.filterNot( user => user.name == name)
    Pusher.pushDelUser( name )
  }

}
