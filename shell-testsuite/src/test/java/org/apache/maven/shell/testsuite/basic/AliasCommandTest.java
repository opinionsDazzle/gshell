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

import org.apache.maven.shell.testsuite.CommandTestSupport;
import org.apache.maven.shell.registry.AliasRegistry;

/**
 * Tests for the {@link AliasCommand}.
 *
 * @version $Rev$ $Date$
 */
public class AliasCommandTest
    extends CommandTestSupport
{
    private AliasRegistry aliasRegistry;

    public AliasCommandTest() {
        super("alias");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        aliasRegistry = lookup(AliasRegistry.class);
    }

    @Override
    protected void tearDown() throws Exception {
        aliasRegistry = null;

        super.tearDown();
    }

    public void testDefineAlias() throws Exception {
        assertFalse(aliasRegistry.containsAlias("foo"));

        Object result = executeWithArgs("foo bar");
        assertEqualsSuccess(result);

        assertTrue(aliasRegistry.containsAlias("foo"));

        String alias = aliasRegistry.getAlias(("foo"));
        assertEquals(alias, "bar");
    }

    public void testRedefineAlias() throws Exception {
        testDefineAlias();
        assertTrue(aliasRegistry.containsAlias("foo"));
        
        Object result = executeWithArgs("foo baz");
        assertEqualsSuccess(result);

        assertTrue(aliasRegistry.containsAlias("foo"));

        String alias = aliasRegistry.getAlias(("foo"));
        assertEquals(alias, "baz");
    }
}