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

package org.apache.maven.shell.core.impl;

import junit.framework.Assert;
import org.apache.maven.shell.Shell;
import org.apache.maven.shell.VariableNames;
import org.apache.maven.shell.io.IO;
import org.apache.maven.shell.io.IOHolder;
import org.apache.maven.shell.registry.CommandRegistry;
import org.codehaus.plexus.PlexusTestCase;

/**
 * Tests that the shell can boot up.
 *
 * @version $Rev$ $Date$
 */
public class ShellImplTest
    extends PlexusTestCase
    implements VariableNames
{
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty(MVNSH_HOME, System.getProperty("user.dir"));
        
        IOHolder.set(new IO());
    }

    public void testBoot() throws Exception {
        Shell shell = lookup(Shell.class);
        Assert.assertNotNull(shell);
    }

    public void testExecuteUnknownHi() throws Exception {
        Shell shell = lookup(Shell.class);
        Assert.assertNotNull(shell);

        try {
            shell.execute("hi");
            Assert.fail();
        }
        catch (Exception ignore) {
            // expected
        }
    }

    public void testLookupSingleton() throws Exception {
        CommandRegistry r1 = lookup(CommandRegistry.class);
        Assert.assertNotNull(r1);

        CommandRegistry r2 = lookup(CommandRegistry.class);
        Assert.assertNotNull(r2);

        Assert.assertEquals(r1, r2);
    }
}