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

import play.api.mvc.{Call, Result, RequestHeader, Filter}
import play.api.mvc.Results._

import scala.concurrent.Future

trait AkamaiWhitelistFilter extends Filter {

  val trueClient = "True-Client-IP"

  private def isCircularDestination(requestHeader: RequestHeader): Boolean  =
    requestHeader.uri == destination.url

  private def toCall(rh: RequestHeader): Call =
    Call(rh.method, rh.uri)

  def whitelist: Seq[String]

  def excludedPaths: Seq[Call] = Seq.empty

  def destination: Call

  def noHeaderAction(f: (RequestHeader) => Future[Result],
                    rh: RequestHeader): Future[Result] = Future.successful(NotImplemented)

  override def apply
  (f: (RequestHeader) => Future[Result])
  (rh: RequestHeader): Future[Result] =
    if (excludedPaths contains toCall(rh)) {
      f(rh)
    } else {
      rh.headers.get(trueClient) map {
        ip =>
          if (whitelist.contains(ip))
            f(rh)
          else if (isCircularDestination(rh))
            Future.successful(Forbidden)
          else
            Future.successful(Redirect(destination))
      } getOrElse noHeaderAction(f, rh)
    }
}
