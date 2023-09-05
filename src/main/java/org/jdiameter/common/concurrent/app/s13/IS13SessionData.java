/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, TeleStax Inc. and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.jdiameter.common.concurrent.app.s13;

import org.jdiameter.api.Request;
import org.jdiameter.common.concurrent.app.IAppSessionData;

import java.io.Serializable;

public interface IS13SessionData extends IAppSessionData {

  void setS13SessionState(S13SessionState state);
  S13SessionState getS13SessionState();

  Serializable getTsTimerId();
  void setTsTimerId(Serializable tid);

  void setBuffer(Request buffer);
  Request getBuffer();
}