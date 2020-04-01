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
import java.util.List;
import net.mcparkour.common.reflection.Reflections;
import net.mcparkour.common.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class DocumentMetadata {

	private Class<?> documentType;
	private String name;
	private Field identifierField;
	private List<Field> indexedFields;
	private List<Pair<Field, String>> propertyFields;

	public DocumentMetadata(Class<?> documentType, String name, Field identifierField, List<Field> indexedFields, List<Pair<Field, String>> propertyFields) {
		this.documentType = documentType;
		this.name = name;
		this.identifierField = identifierField;
		this.indexedFields = indexedFields;
		this.propertyFields = propertyFields;
	}

	@Nullable
	public Object getIdentifier(Object rawDocument) {
		return Reflections.getFieldValue(this.identifierField, rawDocument);
	}

	public Class<?> getDocumentType() {
		return this.documentType;
	}

	public String getName() {
		return this.name;
	}

	public Field getIdentifierField() {
		return this.identifierField;
	}

	public List<Field> getIndexedFields() {
		return List.copyOf(this.indexedFields);
	}

	public List<Pair<Field, String>> getPropertyFields() {
		return List.copyOf(this.propertyFields);
	}
}
