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

package net.mcparkour.moonglow.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.mcparkour.common.reflection.Modifiers;
import net.mcparkour.common.tuple.Pair;
import net.mcparkour.moonglow.annotation.Document;
import net.mcparkour.moonglow.annotation.Property;
import net.mcparkour.moonglow.annotation.document.Identifier;
import net.mcparkour.moonglow.annotation.document.Ignored;
import net.mcparkour.moonglow.annotation.document.Indexed;

public class DocumentMetadataFactory {

	private static final String IDENTIFIER_PROPERTY_NAME = "_id";

	public DocumentMetadata createMetadata(Class<?> documentType) {
		String name = getDocumentName(documentType);
		Field[] fields = documentType.getDeclaredFields();
		int length = fields.length;
		Field identifierField = null;
		List<Field> indexedFields = new ArrayList<>(length / 4);
		List<Pair<Field, String>> propertyFields = new ArrayList<>(length);
		for (Field field : fields) {
			if (!isIgnored(field)) {
				if (field.isAnnotationPresent(Identifier.class)) {
					identifierField = field;
					var pair = new Pair<>(field, IDENTIFIER_PROPERTY_NAME);
					propertyFields.add(pair);
				} else {
					String fieldName = getFieldName(field);
					var pair = new Pair<>(field, fieldName);
					propertyFields.add(pair);
				}
				if (field.isAnnotationPresent(Indexed.class)) {
					indexedFields.add(field);
				}
			}
		}
		if (identifierField == null) {
			throw new RuntimeException("Document does not have identifier property");
		}
		return new DocumentMetadata(documentType, name, identifierField, indexedFields, propertyFields);
	}

	private static boolean isIgnored(Field field) {
		return Modifiers.isStatic(field) || Modifiers.isTransient(field) || field.isAnnotationPresent(Ignored.class);
	}

	private static String getFieldName(Field field) {
		Property property = field.getAnnotation(Property.class);
		if (property != null) {
			return property.value();
		}
		return field.getName();
	}

	private static String getDocumentName(Class<?> documentType) {
		Document document = documentType.getDeclaredAnnotation(Document.class);
		if (document != null) {
			return document.value();
		}
		return documentType.getSimpleName();
	}
}
