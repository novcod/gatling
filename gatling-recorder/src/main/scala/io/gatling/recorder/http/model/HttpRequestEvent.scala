/**
 * Copyright 2011-2017 GatlingCorp (http://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.recorder.http.model

import scala.collection.JavaConverters._

import io.gatling.commons.util.StringHelper.Eol
import io.gatling.recorder.http.flows.Remote

import io.netty.handler.codec.http._
import org.asynchttpclient.netty.util.ByteBufUtils

object HttpRequestEvent {

  def fromNettyRequest(nettyRequest: FullHttpRequest, remote: Remote, https: Boolean, sendTimestamp: Long): HttpRequestEvent =
    HttpRequestEvent(
      nettyRequest.protocolVersion,
      nettyRequest.method,
      remote.makeAbsoluteUri(nettyRequest.uri, https),
      nettyRequest.headers,
      nettyRequest.trailingHeaders,
      ByteBufUtils.byteBuf2Bytes(nettyRequest.content),
      sendTimestamp
    )
}

case class HttpRequestEvent(
    httpVersion:     HttpVersion,
    method:          HttpMethod,
    uri:             String,
    headers:         HttpHeaders,
    trailingHeaders: HttpHeaders,
    body:            Array[Byte],
    sendTimestamp:   Long
) {

  def summary: String =
    s"""$httpVersion $method $uri
     |${(headers.asScala ++ trailingHeaders.asScala).map { entry => s"${entry.getKey}: ${entry.getValue}" }.mkString(Eol)}""".stripMargin
}