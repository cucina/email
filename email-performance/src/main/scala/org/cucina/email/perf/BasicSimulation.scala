package org.cucina.email.perf

import scala.io.Source

import io.gatling.core.Predef._
import io.gatling.core.body.Body
import io.gatling.http.Predef._

class BasicSimulation extends Simulation {

  def loadResource(scriptName: String): Body = {
    val source = Source.fromURL(getClass.getResource(scriptName)).mkString

    StringBody(source)
  }

  val httpConf = http
    .baseURL("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val scn = scenario("BasicSimulation")
    .exec(http("request_1")
      .post("/email/v1/send")
      .body(loadResource("/sample.json")))

  setUp(
    scn.inject(atOnceUsers(1))).protocols(httpConf)
}