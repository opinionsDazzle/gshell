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

package org.apache.gshell.cli2.setter;

import org.apache.gshell.cli2.IllegalAnnotationError;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Setter for fields of collection types.  Currently supports lists and sets.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class CollectionFieldSetter
    extends FieldSetter
{
    public CollectionFieldSetter(final Object bean, final Field field) {
        super(field, bean);

        if (!Collection.class.isAssignableFrom(field.getType())) {
            throw new IllegalAnnotationError(Messages.ILLEGAL_FIELD_SIGNATURE.format(field.getType()));
        }
    }

    @Override
    public boolean isMultiValued() {
        return true;
    }

    public Class getType() {
        Type type = field.getGenericType();

        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType)type;
            type = ptype.getActualTypeArguments()[0];
            
            if (type instanceof Class) {
                return (Class)type;
            }
        }
        
        // If collection types don't have a parameter type, then the ObjectHandler will be used
        // which basically is the same as the StringHandler
        return Object.class;
    }

    protected void doSet(final Object value) throws IllegalAccessException {
        Object target = field.get(bean);

        // If the field is not set, then create a new instance of the collection and set it
        if (target == null) {
            Class type = field.getType();

            if (List.class.isAssignableFrom(type)) {
                target = new ArrayList();
            }
            else if (SortedSet.class.isAssignableFrom(type)) {
                target = new TreeSet();
            }
            else if (Set.class.isAssignableFrom(type)) {
                target = new LinkedHashSet();
            }
            else if (Collection.class.isAssignableFrom(type)) {
                target = new ArrayList();
            }
            else {
                try {
                    target = type.newInstance();
                }
                catch (Exception e) {
                    throw new IllegalAnnotationError(Messages.UNSUPPORTED_COLLECTION_TYPE.format(field.getType()), e);
                }
            }

            field.set(bean, target);
        }

        append(value, target);
    }

    @SuppressWarnings({ "unchecked" })
    private void append(final Object value, final Object target) {
        if (!(target instanceof Collection)) {
            throw new IllegalAnnotationError(Messages.FIELD_NOT_COLLECTION.format(field));
        }
        
        ((Collection)target).add(value);
    }
}