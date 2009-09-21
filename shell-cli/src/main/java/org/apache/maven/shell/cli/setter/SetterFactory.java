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

package org.apache.maven.shell.cli.setter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Creates {@link Setter} instances.
 *
 * @version $Rev$ $Date$
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class SetterFactory
{
    public static Setter create(final Object bean, final AnnotatedElement element) {
        assert bean != null;
        assert element != null;

        if (element instanceof Field) {
            Field field = (Field)element;

            if (Collection.class.isAssignableFrom(field.getType())) {
                return new CollectionFieldSetter(bean, field);
            }
            else {
                return new FieldSetter(bean, field);
            }
        }
        else if (element instanceof Method) {
            Method method = (Method)element;

            return new MethodSetter(bean, method);
        }
        else {
            throw new Error();
        }
    }
}