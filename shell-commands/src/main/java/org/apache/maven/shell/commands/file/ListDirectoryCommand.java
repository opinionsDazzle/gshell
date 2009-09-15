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

package org.apache.maven.shell.commands.file;

import jline.Completor;
import jline.ConsoleReader;
import org.apache.maven.shell.cli.Argument;
import org.apache.maven.shell.cli.Option;
import org.apache.maven.shell.command.Command;
import org.apache.maven.shell.command.CommandContext;
import org.apache.maven.shell.console.completer.AggregateCompleter;
import org.apache.maven.shell.io.IO;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * List the contents of a file or directory.
 *
 * @version $Rev$ $Date$
 */
@Component(role=Command.class, hint="ls", instantiationStrategy="per-lookup")
public class ListDirectoryCommand
    extends FileCommandSupport
{
    @Requirement(role=Completor.class, hints={"file-name"})
    private List<Completor> completers;

    @Argument
    private String path;

    @Option(name="-l", aliases={"--long"})
    private boolean longList;

    @Option(name="-a", aliases={"--all"})
    private boolean includeHidden;

    @Option(name="-r", aliases={"--recursive"})
    private boolean recursive;

    @Override
    public Completor[] getCompleters() {
        assert completers != null;

        return new Completor[] {
            new AggregateCompleter(completers),
            null
        };
    }
    
    public Object execute(final CommandContext context) throws Exception {
        assert context != null;
        IO io = context.getIo();

        File file = resolveFile(context, path);

        if (!file.exists()) {
            io.error(getMessages().format("error.file-not-found", file));
            return Result.FAILURE;
        }

        if (file.isDirectory()) {
            listChildren(io, file);
        }
        else {
            io.info(file.getPath());
        }

        return Result.SUCCESS;
    }

    private void listChildren(final IO io, final File dir) throws Exception {
        assert io != null;
        assert dir != null;

        File[] files;

        if (includeHidden) {
            files = dir.listFiles();
        }
        else {
            files = dir.listFiles(new FileFilter() {
                public boolean accept(final File file) {
                    assert file != null;
                    return !file.isHidden();
                }
            });
        }
        
        ConsoleReader reader = io.createConsoleReader();
        reader.setUsePagination(false);

        List<String> names = new ArrayList<String>(files.length);
        List<File> dirs = new LinkedList<File>();

        for (File file : files) {
            String fileName = file.getName();

            if (hasChildren(file)) {
                fileName += File.separator;

                if (recursive) {
                    dirs.add(file);
                }
            }

            names.add(fileName);
        }

        if (longList) {
            for (String name : names) {
                io.out.println(name);
            }
        }
        else {
            reader.printColumns(names);
        }

        if (!dirs.isEmpty()) {
            for (File subdir : dirs) {
                io.out.println();
                io.out.print(subdir.getName());
                io.out.print(":");
                listChildren(io, subdir);
            }
        }
    }
}