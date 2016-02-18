package com.jaeckel.geth.json;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonQualifier;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.util.Set;


public final class FalseToNullFactory implements JsonAdapter.Factory {

    @Retention(RetentionPolicy.RUNTIME)
    @JsonQualifier
    public @interface FalseToNull {
    }

    @Override
    public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof FalseToNull) {
                JsonAdapter<Object> delegate = moshi.nextAdapter(this, type, annotations);
                return new BooleanToNullAdapter<>(delegate);
            }
        }
        return null;
    }

    static class BooleanToNullAdapter<T> extends JsonAdapter<T> {
        private final JsonAdapter<T> delegate;

        BooleanToNullAdapter(JsonAdapter<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T fromJson(JsonReader reader) throws IOException {
            if (reader.peek() == JsonReader.Token.BOOLEAN) {
                if (reader.nextBoolean()) {
                    throw new IllegalStateException("Expected false or object.");
                }
                return null;
            }
            return delegate.fromJson(reader);
        }

        @Override
        public void toJson(JsonWriter writer, T value) throws IOException {
            delegate.toJson(writer, value);
        }
    }
}
