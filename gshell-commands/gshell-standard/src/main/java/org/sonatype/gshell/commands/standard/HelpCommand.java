/*
 * Copyright (C) 2009 the original author(s).
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

package org.sonatype.gshell.commands.standard;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import jline.console.Completer;
import jline.console.completers.AggregateCompleter;
import org.sonatype.gshell.command.Command;
import org.sonatype.gshell.command.CommandActionSupport;
import org.sonatype.gshell.command.CommandContext;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.help.HelpPage;
import org.sonatype.gshell.help.HelpPageFilter;
import org.sonatype.gshell.help.HelpPageManager;
import org.sonatype.gshell.help.MetaHelpPage;
import org.sonatype.gshell.util.cli2.Argument;

import java.util.Collection;

/**
 * Display help pages.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @since 2.5
 */
@Command(name="help")
public class HelpCommand
    extends CommandActionSupport
{
    private final HelpPageManager helpPages;

    @Argument
    private String name;

    @Inject
    public HelpCommand(final HelpPageManager helpPages) {
        assert helpPages != null;
        this.helpPages = helpPages;
    }

    @Inject
    /**
     * @since 2.5
     */
    public HelpCommand installCompleters(final @Named("alias-name") Completer c1, final @Named("command-name") Completer c2, final @Named("meta-help-page-name") Completer c3) {
        assert c1 != null;
        setCompleters(new AggregateCompleter(c1, c2, c3), null);
        return this;
    }

    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        // If there is no argument given, display all help pages in context
        if (name == null) {
            displayCommands(context);
            displayMetaPages(context);
            return Result.SUCCESS;
        }

        // First try a direct match
        HelpPage page = helpPages.getPage(name);

        // if not direct match, then look for similar pages
        if (page == null) {
            Collection<HelpPage> pages = helpPages.getPages(new HelpPageFilter()
            {
                public boolean accept(final HelpPage page) {
                    assert page != null;
                    return page.getName().contains(name) || page.getDescription().contains(name);
                }
            });

            if (pages.size() == 1) {
                // if there is only one match, treat as a direct match
                page = pages.iterator().next();
            }
            else if (pages.size() > 1) {
                // else show matching pages
                io.out.println(getMessages().format("info.matching-pages"));
                renderPages(context, pages);
                return Result.SUCCESS;
            }
        }

        // if not page matched, complain
        if (page == null) {
            io.err.println(getMessages().format("error.help-not-found", name));
            return Result.FAILURE;
        }

        // else render the matched page
        page.render(io.out);
        return Result.SUCCESS;
    }

    private void renderPages(final CommandContext context, final Collection<? extends HelpPage> pages) {
        assert context != null;
        assert pages != null;

        int max = 0;
        for (HelpPage page : pages) {
            int len = page.getName().length();
            max = Math.max(len, max);
        }
        String nameFormat = "%-" + max + 's';

        IO io = context.getIo();
        for (HelpPage page : pages) {
            String formattedName = String.format(nameFormat, page.getName());
            io.out.format("  @|bold %s|@", formattedName);

            String description = page.getDescription();
            if (description != null) {
                io.out.print("  ");
                io.out.println(description);
            }
            else {
                io.out.println();
            }
        }
    }

    private void displayCommands(final CommandContext context) {
        assert context != null;

        Collection<HelpPage> pages = helpPages.getPages();

        IO io = context.getIo();
        io.out.println(getMessages().format("info.available-commands"));

        renderPages(context, pages);
    }

    private void displayMetaPages(final CommandContext context) {
        assert context != null;

        Collection<MetaHelpPage> pages = helpPages.getMetaPages();
        if (!pages.isEmpty()) {
            IO io = context.getIo();
            io.out.println();
            io.out.println(getMessages().format("info.additional-doc"));

            renderPages(context, pages);
        }
    }
}