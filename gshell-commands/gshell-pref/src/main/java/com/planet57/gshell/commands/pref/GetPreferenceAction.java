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
package com.planet57.gshell.commands.pref;

import com.planet57.gshell.command.Command;
import com.planet57.gshell.command.CommandContext;
import com.planet57.gshell.util.io.IO;
import com.planet57.gshell.util.cli2.Argument;

import javax.annotation.Nonnull;

/**
 * Get a preference value.
 *
 * @since 2.0
 */
@Command(name = "pref/get", description = "Get a preference")
public class GetPreferenceAction
    extends PreferenceNodeActionSupport
{
  @Argument(index = 1, required = true, description = "Preference key", token = "KEY")
  private String key;

  @Override
  public Object execute(@Nonnull final CommandContext context) throws Exception {
    IO io = context.getIo();

    log.debug("Getting preference: {}", key);
    Object value = node().get(key, null);
    io.println(value);

    return value;
  }
}
