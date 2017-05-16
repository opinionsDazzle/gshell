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
package com.planet57.gshell.command;

import com.planet57.gshell.command.CommandAction;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Event fired once a command has been registered.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.5
 */
public class CommandRegisteredEvent
{
  private final String name;

  private final CommandAction command;

  public CommandRegisteredEvent(final String name, final CommandAction command) {
    this.name = checkNotNull(name);
    this.command = checkNotNull(command);
  }

  public String getName() {
    return name;
  }

  public CommandAction getCommand() {
    return command;
  }

  @Override
  public String toString() {
    return "CommandRegisteredEvent{" +
      "name='" + name + '\'' +
      ", command=" + command +
      '}';
  }
}