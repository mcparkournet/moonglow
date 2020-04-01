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

package net.mcparkour.moonglow.converter.model.value;

import net.mcparkour.unifig.model.value.ModelValue;
import net.mcparkour.unifig.model.value.ModelValueFactory;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonDouble;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.bson.BsonNull;
import org.bson.BsonString;
import org.bson.BsonValue;

public class BsonModelValueFactory implements ModelValueFactory<BsonDocument, BsonArray, BsonValue> {

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createNullModelValue() {
		return createModelValue(BsonNull.VALUE);
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createBooleanModelValue(boolean value) {
		BsonBoolean bsonBoolean = new BsonBoolean(value);
		return createModelValue(bsonBoolean);
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createNumberModelValue(Number value) {
		if (value instanceof Byte || value instanceof Short || value instanceof Integer) {
			int intValue = value.intValue();
			BsonInt32 bsonInt32 = new BsonInt32(intValue);
			return createModelValue(bsonInt32);
		} else if (value instanceof Long) {
			long longValue = value.longValue();
			BsonInt64 bsonInt64 = new BsonInt64(longValue);
			return createModelValue(bsonInt64);
		} else {
			double doubleValue = value.doubleValue();
			BsonDouble bsonDouble = new BsonDouble(doubleValue);
			return createModelValue(bsonDouble);
		}
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createStringModelValue(String value) {
		BsonString bsonString = new BsonString(value);
		return createModelValue(bsonString);
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createObjectModelValue(BsonDocument object) {
		return createModelValue(object);
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createArrayModelValue(BsonArray array) {
		return createModelValue(array);
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> createModelValue(BsonValue value) {
		return new BsonModelValue(value);
	}
}
