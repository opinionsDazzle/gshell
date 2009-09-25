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

package org.apache.gshell.cli.handler;

import org.apache.gshell.cli.Descriptor;
import org.apache.gshell.cli.ProcessingException;
import org.apache.gshell.cli.setter.Setter;

/**
 * Handler for object types, simply treating them as strings.
 *
 * <p>This is for compatibility with multi-valued bits that don't use generics to
 *    indicate the class of contained elements.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ObjectHandler
    extends Handler<Object>
{
    public ObjectHandler(final Descriptor desc, Setter<? super Object> setter) {
        super(desc, setter);
    }

    @Override
    public int handle(final Parameters params) throws ProcessingException {
        assert params != null;

        getSetter().set(params.get(0));

        return 1;
    }

    @Override
    public String getDefaultToken() {
        return "OBJ";
    }
}