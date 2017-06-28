/*
 * Copyright 2017 HM Revenue & Customs
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

package controllers

import javax.inject.Inject

import config.ConfigDecorator
import connectors.{FrontEndDelegationConnector, PertaxAuditConnector, PertaxAuthConnector}
import error.LocalErrorHandler
import play.api.i18n.{Lang, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import util.LocalPartialRetriever
import util.Tools.isRelative

import scala.concurrent.Future

class LanguageController @Inject() (
  val auditConnector: PertaxAuditConnector,
  val authConnector: PertaxAuthConnector,
  val partialRetriever: LocalPartialRetriever,
  val delegationConnector: FrontEndDelegationConnector,
  val messagesApi: MessagesApi,
  val configDecorator: ConfigDecorator,
  val localErrorHandler: LocalErrorHandler
) extends PertaxBaseController {

  def enGb(redirectUrl: String): Action[AnyContent] = changeLanguageIfRelativeRedirectUrl(redirectUrl=redirectUrl, language="en")
  def cyGb(redirectUrl: String): Action[AnyContent] = changeLanguageIfRelativeRedirectUrl(redirectUrl=redirectUrl, language="cy")

  def changeLanguageIfRelativeRedirectUrl(redirectUrl: String, language: String): Action[AnyContent] = PublicAction { implicit pertaxContext =>
    if (isRelative(redirectUrl)) {
      Future.successful(Redirect(redirectUrl).withLang(Lang(language)))
    } else {
      Future.successful(BadRequest(views.html.error("global.error.BadRequest.title",
        Some("global.error.BadRequest.title"),
        Some("global.error.BadRequest.message"), false)))
    }
  }
}
