/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.whitelist

import akka.stream.Materializer
import javax.inject.{Inject, Singleton}
import org.scalatest.TestSuite
import org.scalatestplus.play.OneAppPerSuite
import play.api.Application
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.Future

trait TestAppWithCustomFailureDefault extends OneAppPerSuite {
  self: TestSuite =>

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .bindings(bind(classOf[AkamaiWhitelistFilter]).to(classOf[TestAkamaiWhitelistFilterWithCustomFailureDefault]))
    .configure("play.http.filters" -> "uk.gov.hmrc.whitelist.TestFilters")
    .routes({
      case ("GET", "/destination") => Action(Ok("destination"))
      case ("GET", "/index") => Action(Ok("success"))
      case ("GET", "/healthcheck") => Action(Ok("ping"))
    })
    .build
}

@Singleton
private class TestAkamaiWhitelistFilterWithCustomFailureDefault @Inject() (override val mat: Materializer) extends AkamaiWhitelistFilter {
  override lazy val whitelist: Seq[String] = Seq("127.0.0.1")
  override lazy val destination: Call = Call("GET", "/destination")
  override lazy val excludedPaths: Seq[Call] = Seq(Call("GET", "/healthcheck"))
  override def noHeaderAction(f: (RequestHeader) => Future[Result],
                              rh: RequestHeader): Future[Result] = f(rh)
}
