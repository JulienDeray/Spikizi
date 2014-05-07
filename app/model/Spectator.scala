package model

import core.State._

class Spectator(val realName: String) {
  val name: String = realName.toLowerCase.replaceAll(" ", "-")
  var state: State = Passive

  def setPassive() { state = Passive }
  def setWaiting() { state = Waiting }
  def setSpeaking() { state = Speaking }
}
