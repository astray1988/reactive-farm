package modules

import akka.actor.ActorSystem
import com.google.inject.{AbstractModule, Inject}

/**
  * Created by dylan on 2/13/16.
  */
class Actors @Inject()(system: ActorSystem)
  extends ApplicationActors{

  system.actorOf(
    props = StatisticsProvider.props,
    name = "StatisticsProvider"
  )


}

trait ApplicationActors


class ActorsModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ApplicationActors])
      .to(classOf[Actors]).asEagerSingleton()
  }
}