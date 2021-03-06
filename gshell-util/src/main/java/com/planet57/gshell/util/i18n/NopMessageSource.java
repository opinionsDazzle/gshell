/*
 * Copyright (c) 2009-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.planet57.gshell.util.i18n;

import com.google.common.annotations.VisibleForTesting;

/**
 * Nop {@link MessageSource}.
 *
 * @since 3.0
 */
public class NopMessageSource
  implements MessageSource
{
  public static final NopMessageSource INSTANCE = new NopMessageSource();

  @VisibleForTesting
  static final String MISSING_MESSAGE_FORMAT = "ERROR_MISSING_MESSAGE[%s]"; //NON-NLS

  @Override
  public String getMessage(final String code) {
    return String.format(MISSING_MESSAGE_FORMAT, code);
  }

  @Override
  public String format(final String code, final Object... args) {
    return String.format(MISSING_MESSAGE_FORMAT, code);
  }
}
