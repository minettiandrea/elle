package modules

import play.api.{Configuration, Environment}
import play.api.inject.Module
import services.{TDLService, TDLServiceImpl}

class AppModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = Seq(
    bind[TDLService].to[TDLServiceImpl]
  )
}
