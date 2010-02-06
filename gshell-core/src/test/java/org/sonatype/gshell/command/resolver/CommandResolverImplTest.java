/*
 * Copyright (C) 2010 the original author or authors.
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

package org.sonatype.gshell.command.resolver;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

/**
 * Tests for the {@link CommandResolverImpl}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Ignore
public class CommandResolverImplTest
{
    private CommandResolver resolver;

    @Before
    public void setUp() throws Exception {
        Injector injector = Guice.createInjector(Stage.DEVELOPMENT, new AbstractModule() {
            @Override
            protected void configure() {
                bind(CommandResolver.class).to(CommandResolverImpl.class);
            }
        });
        resolver= injector.getInstance(CommandResolver.class);
    }

    @After
    public void tearDown() {
        resolver = null;
    }
}