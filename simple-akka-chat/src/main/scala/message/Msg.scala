package message

/**
  * Created by dylan on 2/14/16.
  */
abstract class Msg

case class Send(msg: String) extends Msg

case class NewMsg(from: String, msg: String) extends Msg

case class Info(msg: String) extends Msg

case class Connect(username: String) extends Msg

case class Broadcast(msg: String) extends Msg

case object Disconnect extends Msg

