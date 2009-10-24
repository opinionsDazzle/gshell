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

package org.sonatype.gshell.cli.handler;

import static org.junit.Assert.*;
import org.junit.Test;
import org.sonatype.gshell.cli.Option;
import org.sonatype.gshell.cli.ProcessorTestSupport;

/**
 * Tests for the {@link org.sonatype.gshell.cli.handler.DoubleHandler} class.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class DoubleHandlerTest
    extends ProcessorTestSupport
{
    private TestBean bean;

    @Override
    protected Object createBean() {
        bean = new TestBean();
        return bean;
    }

    @Test
    public void testOptionsArgumentsSize() {
        assertOptionsArgumentsSize(1, 0);
    }

    @Test
    public void test1() throws Exception {
        clp.process("-1", "1");

        assertEquals(1.0, bean.d, 0);
    }

    @Test
    public void test2() throws Exception {
        clp.process("-1", "1.1");

        assertEquals(1.1, bean.d, 0);
    }

    @Test
    public void test3() throws Exception {
        clp.process("-1=1.1");

        assertEquals(1.1, bean.d, 0);
    }

    private static class TestBean
    {
        @Option(name="-1")
        double d;
    }
}