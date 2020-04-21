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

import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.play.OneAppPerSuite
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

trait AkamaiWhitelistFilterCommonSpec extends PlaySpec with ScalaFutures { this: OneAppPerSuite =>

  "AkamaiWhitelistFilter (Common)" must {
    "return successfully when a valid `True-Client-IP` header is found" in {
      val Some(result) = route(app, FakeRequest("GET", "/index").withHeaders(
        "True-Client-IP" -> "127.0.0.1"
      ))
      status(result) must be(OK)
      contentAsString(result) must be("success")
    }

    "return a `Redirect` when an invalid `True-Client-IP` header is found" in {
      val Some(result) = route(app, FakeRequest("GET", "/index").withHeaders(
        "True-Client-IP" -> "someotherip"
      ))
      status(result) must be(SEE_OTHER)
      redirectLocation(result) must be(Some("/destination"))
    }

    "return `Forbidden` if the user would end up in a redirect loop" in {
      val Some(result) = route(app, FakeRequest("GET", "/destination").withHeaders(
        "True-Client-IP" -> "someotherip"
      ))

      status(result) mustBe FORBIDDEN
    }

    "return `Ok` if the route to be accessed is an excluded path" in {
      val Some(result) = route(app, FakeRequest("GET", "/healthcheck"))

      status(result) mustBe OK
    }
  }
}
