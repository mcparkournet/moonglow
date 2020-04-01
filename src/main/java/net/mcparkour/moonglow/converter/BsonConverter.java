/*
 * MIT License
 *
 * Copyright (c) 2020 MCParkour
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.mcparkour.moonglow.converter;

import java.lang.reflect.Field;
import java.util.List;
import net.mcparkour.moonglow.annotation.Property;
import net.mcparkour.moonglow.annotation.document.Identifier;
import net.mcparkour.moonglow.converter.model.array.BsonModelArrayFactory;
import net.mcparkour.moonglow.converter.model.object.BsonModelObjectFactory;
import net.mcparkour.moonglow.converter.model.value.BsonModelValueFactory;
import net.mcparkour.unifig.codec.registry.CodecRegistry;
import net.mcparkour.unifig.condition.FieldCondition;
import net.mcparkour.unifig.condition.NonStaticFieldCondition;
import net.mcparkour.unifig.condition.NonTransientFieldCondition;
import net.mcparkour.unifig.converter.BasicConverter;
import net.mcparkour.unifig.options.BasicOptionsBuilder;
import net.mcparkour.unifig.options.Options;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.jetbrains.annotations.Nullable;

public class BsonConverter extends BasicConverter<BsonDocument, BsonArray, BsonValue> {

	private static final String IDENTIFIER_PROPERTY_NAME = "_id";

	public BsonConverter(CodecRegistry<BsonDocument, BsonArray, BsonValue> codecRegistry) {
		super(new BsonModelObjectFactory(), new BsonModelArrayFactory(), new BsonModelValueFactory(), createOptions(codecRegistry));
	}

	private static Options createOptions(CodecRegistry<BsonDocument, BsonArray, BsonValue> codecRegistry) {
		return new BasicOptionsBuilder()
			.codecRegistry(codecRegistry)
			.fieldConditions(createFieldConditions())
			.build();
	}

	private static List<FieldCondition> createFieldConditions() {
		return List.of(
			new IgnoredAnnotationNotPresentedFieldCondition(),
			new NonStaticFieldCondition(),
			new NonTransientFieldCondition()
		);
	}

	@Override
	public String getFieldName(Field field) {
		Identifier identifier = field.getAnnotation(Identifier.class);
		if (identifier != null) {
			return IDENTIFIER_PROPERTY_NAME;
		}
		Property property = field.getAnnotation(Property.class);
		if (property != null) {
			return property.value();
		}
		return field.getName();
	}

	public BsonDocument toDocument(Object object) {
		var modelObject = fromConfiguration(object);
		return modelObject.getObject();
	}

	public <T> T fromDocument(BsonDocument document, Class<T> objectType) {
		var modelObjectFactory = getModelObjectFactory();
		var bsonModelObject = modelObjectFactory.createModelObject(document);
		return toConfiguration(bsonModelObject, objectType);
	}

	public BsonValue toValue(Object object) {
		Class<?> type = object.getClass();
		var bsonModelValue = toModelValue(object, type);
		BsonValue value = bsonModelValue.getValue();
		if (value == null) {
			throw new RuntimeException("Value is null");
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	public <T> T fromValue(BsonValue value, Class<T> objectType) {
		var modelValueFactory = getModelValueFactory();
		var bsonModelValue = modelValueFactory.createModelValue(value);
		return (T) toObject(bsonModelValue, objectType);
	}
}
