/*
 * Copyright (C) 2015 AppTik Project
 * Copyright (C) 2013 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apptik.comm.jus.converter;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.apptik.comm.jus.Converter;
import io.apptik.comm.jus.NetworkRequest;
import io.apptik.comm.jus.NetworkResponse;

/**
 * A {@linkplain Converter.Factory converter} which uses Simple Framework for XML.
 * <p/>
 * This converter only applies for class types. Parameterized types (e.g., {@code List<Foo>}) are
 * not handled.
 */
public final class SimpleXmlConverterFactory extends Converter.Factory {
    /**
     * Create an instance using a default {@link Persister} instance for conversion.
     */
    public static SimpleXmlConverterFactory create() {
        return create(new Persister());
    }

    /**
     * Create an instance using {@code serializer} for conversion.
     */
    public static SimpleXmlConverterFactory create(Serializer serializer) {
        return new SimpleXmlConverterFactory(serializer, true);
    }

    /**
     * Create an instance using a default {@link Persister} instance for non-strict conversion.
     */
    public static SimpleXmlConverterFactory createNonStrict() {
        return createNonStrict(new Persister());
    }

    /**
     * Create an instance using {@code serializer} for non-strict conversion.
     */
    public static SimpleXmlConverterFactory createNonStrict(Serializer serializer) {
        return new SimpleXmlConverterFactory(serializer, false);
    }

    private final Serializer serializer;
    private final boolean strict;

    private SimpleXmlConverterFactory(Serializer serializer, boolean strict) {
        if (serializer == null) throw new NullPointerException("serializer == null");
        this.serializer = serializer;
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    @Override
    public Converter<NetworkResponse, ?> fromResponse(Type type, Annotation[] annotations) {
        if (!(type instanceof Class)) {
            return null;
        }
        Class<?> cls = (Class<?>) type;
        return new SimpleXmlResponseConverter<>(cls, serializer, strict);
    }

    @Override
    public Converter<?, NetworkRequest> toRequest(Type type, Annotation[] annotations) {
        if (!(type instanceof Class)) {
            return null;
        }
        return new SimpleXmlRequestConverter<>(serializer);
    }
}
