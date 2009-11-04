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

package org.sonatype.gshell.util.converter.basic;

import org.sonatype.gshell.util.converter.collections.CollectionConverterSupport;

import java.beans.PropertyEditor;
import java.lang.reflect.Array;
import java.util.List;
import java.util.ListIterator;

/**
 * ???
 *
 * @since 2.0
 */
public final class ArrayConverter
    extends CollectionConverterSupport
{
    public ArrayConverter(Class type, PropertyEditor editor) {
        super(type, editor);

        if (!type.isArray()) {
            throw new IllegalArgumentException("type is not an array " + type.getSimpleName());
        }

        if (type.getComponentType().isArray()) {
            throw new IllegalArgumentException("type is a multi-dimensional array " + type.getSimpleName());
        }

        if (editor == null) {
            throw new NullPointerException("editor is null");
        }
    }

    protected Object createCollection(List list) {
        Object array = Array.newInstance(getType().getComponentType(), list.size());
        for (ListIterator iterator = list.listIterator(); iterator.hasNext();) {
            Object item = iterator.next();
            int index = iterator.previousIndex();
            Array.set(array, index, item);
        }
        return array;
    }
}
