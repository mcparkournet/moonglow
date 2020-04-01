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
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonNumber;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.jetbrains.annotations.Nullable;

public class BsonModelValue implements ModelValue<BsonDocument, BsonArray, BsonValue> {

	private BsonValue value;

	public BsonModelValue(BsonValue value) {
		this.value = value;
	}

	@Override
	public boolean isNull() {
		return this.value.isNull();
	}

	@Override
	public boolean asBoolean() {
		BsonBoolean bsonBoolean = this.value.asBoolean();
		return bsonBoolean.getValue();
	}

	@Override
	public boolean isBoolean() {
		return this.value.isBoolean();
	}

	@Override
	public Number asNumber() {
		BsonNumber bsonNumber = this.value.asNumber();
		if (bsonNumber.isInt32()) {
			return bsonNumber.intValue();
		} else if (bsonNumber.isInt64()) {
			return bsonNumber.longValue();
		} else {
			return bsonNumber.doubleValue();
		}
	}

	@Override
	public boolean isNumber() {
		return this.value.isNumber();
	}

	@Override
	public String asString() {
		BsonString bsonString = this.value.asString();
		return bsonString.getValue();
	}

	@Override
	public boolean isString() {
		return this.value.isString();
	}

	@Override
	public BsonDocument asObject() {
		return this.value.asDocument();
	}

	@Override
	public boolean isObject() {
		return this.value.isDocument();
	}

	@Override
	public BsonArray asArray() {
		return this.value.asArray();
	}

	@Override
	public boolean isArray() {
		return this.value.isArray();
	}

	@Nullable
	@Override
	public BsonValue getValue() {
		return this.value;
	}
}
