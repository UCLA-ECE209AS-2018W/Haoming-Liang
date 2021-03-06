package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.io.IOException;
import java.lang.annotation.Annotation;

public final class ObjectIdValueProperty extends SettableBeanProperty {
    private static final long serialVersionUID = 1;
    protected final ObjectIdReader _objectIdReader;

    public ObjectIdValueProperty(ObjectIdReader objectIdReader, PropertyMetadata metadata) {
        super(objectIdReader.propertyName, objectIdReader.getIdType(), metadata, objectIdReader.getDeserializer());
        this._objectIdReader = objectIdReader;
    }

    protected ObjectIdValueProperty(ObjectIdValueProperty src, JsonDeserializer<?> deser) {
        super((SettableBeanProperty) src, (JsonDeserializer) deser);
        this._objectIdReader = src._objectIdReader;
    }

    protected ObjectIdValueProperty(ObjectIdValueProperty src, PropertyName newName) {
        super((SettableBeanProperty) src, newName);
        this._objectIdReader = src._objectIdReader;
    }

    public final ObjectIdValueProperty withName(PropertyName newName) {
        return new ObjectIdValueProperty(this, newName);
    }

    public final ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser) {
        return new ObjectIdValueProperty(this, (JsonDeserializer) deser);
    }

    public final <A extends Annotation> A getAnnotation(Class<A> cls) {
        return null;
    }

    public final AnnotatedMember getMember() {
        return null;
    }

    public final void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        deserializeSetAndReturn(p, ctxt, instance);
    }

    public final Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        if (p.hasToken(JsonToken.VALUE_NULL)) {
            return null;
        }
        Object id = this._valueDeserializer.deserialize(p, ctxt);
        ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver).bindItem(instance);
        SettableBeanProperty idProp = this._objectIdReader.idProperty;
        if (idProp != null) {
            return idProp.setAndReturn(instance, id);
        }
        return instance;
    }

    public final void set(Object instance, Object value) throws IOException {
        setAndReturn(instance, value);
    }

    public final Object setAndReturn(Object instance, Object value) throws IOException {
        SettableBeanProperty idProp = this._objectIdReader.idProperty;
        if (idProp != null) {
            return idProp.setAndReturn(instance, value);
        }
        throw new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
    }
}
