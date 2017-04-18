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
package com.planet57.gshell;

import java.io.File;

import com.planet57.gshell.branding.BrandingSupport;
import com.planet57.gshell.branding.License;
import com.planet57.gshell.branding.LicenseSupport;
import com.planet57.gshell.util.PrintBuffer;
import com.planet57.gshell.variables.VariableNames;
import org.fusesource.jansi.Ansi;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Branding for <tt>gsh</tt>.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.0
 */
@Named
@Singleton
public class BrandingImpl
    extends BrandingSupport
{
  // Figlet font name: ???
  private static final String[] BANNER = {
      "   ____ ____  _          _ _ ",
      "  / ___/ ___|| |__   ___| | |",
      " | |  _\\___ \\| '_ \\ / _ \\ | |",
      " | |_| |___) | | | |  __/ | |",
      "  \\____|____/|_| |_|\\___|_|_|",
      };

  @Override
  public String getWelcomeMessage() {
    PrintBuffer buff = new PrintBuffer();

    for (String line : BANNER) {
      buff.println(Ansi.ansi().fg(Ansi.Color.CYAN).a(line).reset());
    }

    buff.println();
    buff.format("%s (%s)", getDisplayName(), getVersion()).println();
    buff.println();
    buff.println("Type '@|bold help|@' for more information.");
    buff.print(line());
    buff.flush();

    return buff.toString();
  }

  @Override
  public String getDisplayName() {
    return getMessages().format("displayName");
  }

  @Override
  public String getGoodbyeMessage() {
    return getMessages().format("goodbye");
  }

  @Override
  public File getUserContextDir() {
    return resolveFile(new File(getUserHomeDir(), ".gshell"));
  }

  @Override
  public String getPrompt() {
    return String.format("@|bold %s|@:${%s}> ", getProgramName(), VariableNames.SHELL_GROUP);
  }

  @Override
  public License getLicense() {
    return new LicenseSupport("Apache License, Version 2.0", "http://www.apache.org/licenses/LICENSE-2.0.txt");
  }
}
