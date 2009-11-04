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

package org.sonatype.gshell.util.converter.collections;

import org.sonatype.gshell.util.converter.ConverterSupport;
import org.sonatype.gshell.util.converter.basic.StringConverter;

import java.beans.PropertyEditor;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Array;

/**
 * ???
 *
 * @since 2.0
 */
public abstract class CollectionConverterSupport
    extends ConverterSupport
{
    private final PropertyEditor editor;

    public CollectionConverterSupport(Class type) {
        super(type);
        this.editor = new StringConverter();
    }

    public CollectionConverterSupport(Class type, PropertyEditor editor) {
        super(type);

        if (editor == null) {
            throw new NullPointerException("editor is null");
        }
        this.editor = editor;
    }

    protected final Object toObjectImpl(String text) throws Exception {
        List list = CollectionUtil.toList(text, editor);
        if (list == null) {
            return null;
        }

        return createCollection(list);
    }

    protected abstract Object createCollection(List list) throws Exception;

    protected final String toStringImpl(Object value) {
        Collection values;
        if (value.getClass().isArray()) {
            values = new ArrayList(Array.getLength(value));
            for (int i = 0; i < Array.getLength(value); i++) {
                values.add(Array.get(value, i));
            }
        }
        else {
            values = (Collection) value;
        }

        String text = CollectionUtil.toString(values, editor);
        return text;
    }
}
