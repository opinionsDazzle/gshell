/*
 * Copyright (c) 2009-present the original author or authors.
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
package com.planet57.gshell.util.converter.collections;

import java.util.Map;
import java.util.Properties;

/**
 * Converter for {@link Properties} types.
 *
 * @since 2.0
 */
public class PropertiesConverter
    extends MapConverterSupport
{
  public PropertiesConverter() {
    super(Properties.class);
  }

  @Override
  @SuppressWarnings({"unchecked"})
  protected Map createMap(final Map map) throws Exception {
    Properties properties = new Properties();
    properties.putAll(map);
    return properties;
  }
}
