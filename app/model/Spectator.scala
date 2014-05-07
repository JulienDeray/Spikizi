package model

import core.State._

class Spectator(val name: String) {

  val hash = System.currentTimeMillis().toString

  var state: State = Passive

  def setPassive() { state = Passive }
  def setWaiting() { state = Waiting }
  def setSpeaking() { state = Speaking }
}
