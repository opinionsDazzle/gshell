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

package org.apache.maven.shell.testsuite.basic;

import org.apache.maven.shell.cli.ProcessingException;
import org.apache.maven.shell.testsuite.CommandTestSupport;
import org.apache.maven.shell.Variables;

/**
 * Tests for the {@link UnsetCommand}.
 *
 * @version $Rev$ $Date$
 */
public class UnsetCommandTest
    extends CommandTestSupport
{
    private Variables vars;

    public UnsetCommandTest() {
        super("unset");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        vars = getShell().getContext().getVariables();
    }

    @Override
    protected void tearDown() throws Exception {
        vars = null;

        super.tearDown();
    }

    @Override
    public void testDefault() throws Exception {
        try {
            super.testDefault();
            fail();
        }
        catch (ProcessingException e) {
            // expected
        }
    }

    public void testUndefineVariable() throws Exception {
        vars.set("foo", "bar");
        assertTrue(vars.contains("foo"));
        Object result = executeWithArgs("foo");
        assertEqualsSuccess(result);
        assertFalse(vars.contains("foo"));
    }

    public void testUndefineUndefinedVariable() throws Exception {
        assertFalse(vars.contains("foo"));
        Object result = executeWithArgs("foo");

        // Unsetting undefined should not return any errors
        assertEqualsSuccess(result);
    }
}