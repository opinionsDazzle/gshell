/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.sonatype.gshell.core.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.gshell.ShellHolder;
import org.sonatype.gshell.Variables;
import org.sonatype.gshell.ansi.AnsiRenderer;
import org.sonatype.gshell.cli.Printer;
import org.sonatype.gshell.cli.Processor;
import org.sonatype.gshell.command.CommandAction;
import org.sonatype.gshell.command.CommandDocumenter;
import org.sonatype.gshell.command.IO;
import org.sonatype.gshell.i18n.AggregateMessageSource;
import org.sonatype.gshell.i18n.MessageSource;
import org.sonatype.gshell.i18n.PrefixingMessageSource;
import org.sonatype.gshell.i18n.ResourceBundleMessageSource;
import org.sonatype.gshell.io.PrefixingOutputStream;

import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The default {@link CommandDocumenter} component.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 2.0
 */
public class CommandDocumenterImpl
    implements CommandDocumenter
{
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MessageSource messages = new ResourceBundleMessageSource(getClass());

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");

    private String evaluate(final CommandAction command, String input) {
        Matcher matcher = PATTERN.matcher(input);

        if (input.contains("${")) {
            while (matcher.find()) {
                String key = matcher.group(1);
                // FIXME: Add "command." prefix to access command instance
                Variables vars = ShellHolder.get().getVariables();
                Object rep = vars.get(key);
                if (rep == null) {
                    rep = System.getProperty(key);
                }
                if (rep != null) {
                    input = input.replace(matcher.group(0), rep.toString());
                    matcher.reset(input);
                }
            }
        }

        return input;
    }

    public String getDescription(final CommandAction command) {
        assert command != null;

        String text = command.getMessages().getMessage(COMMAND_DESCRIPTION);
        return evaluate(command, text);
    }

    public String getManual(final CommandAction command) {
        assert command != null;
        
        String text = command.getMessages().getMessage(COMMAND_MANUAL);
        return evaluate(command, text);
    }

    public void renderUsage(final CommandAction command, final IO io) {
        assert command != null;
        assert io != null;

        log.trace("Rendering command usage");

        Processor clp = new Processor();

        // Attach our helper to inject --help
        CommandHelpSupport help = new CommandHelpSupport();
        clp.addBean(help);

        // And then the beans options
        clp.addBean(command);

        // Render the help
        io.out.println(getDescription(command));
        io.out.println();

        Printer printer = new Printer(clp);
        AggregateMessageSource messages = new AggregateMessageSource(command.getMessages(), help.getMessages());
        printer.addMessages(new PrefixingMessageSource(messages, COMMAND_DOT));
        printer.printUsage(io.out, command.getName());
    }

    public void renderManual(final CommandAction command, final IO io) {
        assert command != null;
        assert io != null;

        log.trace("Rendering command manual");

        PrintStream out = new PrintStream(new PrefixingOutputStream(io.streams.out, "   "));
        AnsiRenderer renderer = new AnsiRenderer();

        //
        // HACK: PrefixingOutputStream has a problem with state and using io.out, so we have to
        //       add more println()s to compensate for now
        //

        io.out.format("@|bold %s|", messages.getMessage("section.name")).println();
        io.out.println();
        out.println(command.getName());
        io.out.println();

        String text;

        io.out.format("@|bold %s|", messages.getMessage("section.description")).println();
        text = getDescription(command);
        text = renderer.render(text);
        out.println();
        out.println(text);
        io.out.println();

        String manual = getManual(command);
        if (manual != null && manual.trim().length() != 0) {
            io.out.format("@|bold %s|", messages.getMessage("section.manual")).println();
            text = manual;
            text = renderer.render(text);
            out.println();
            out.println(text);
            io.out.println();
        }
    }
}