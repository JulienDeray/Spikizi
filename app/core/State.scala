package core

/**
 * Created with IntelliJ IDEA.
 * Autor: julienderay
 * Company : SERLI
 * Date: 06/05/2014
 * Time: 12:01
 */
object State extends Enumeration {
  type State = Value
  val Waiting, Passive, Speaking = Value
}
