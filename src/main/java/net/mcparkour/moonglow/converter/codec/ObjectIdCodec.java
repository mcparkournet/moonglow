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

package net.mcparkour.moonglow.converter.codec;

import java.lang.reflect.Type;
import net.mcparkour.unifig.codec.Codec;
import net.mcparkour.unifig.codec.CodecDecodeException;
import net.mcparkour.unifig.converter.Converter;
import net.mcparkour.unifig.model.value.ModelValue;
import net.mcparkour.unifig.model.value.ModelValueFactory;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonValue;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;

public class ObjectIdCodec implements Codec<BsonDocument, BsonArray, BsonValue, ObjectId> {

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> encode(ObjectId object, Type type, Converter<BsonDocument, BsonArray, BsonValue> converter) {
		ModelValueFactory<BsonDocument, BsonArray, BsonValue> valueFactory = converter.getModelValueFactory();
		BsonObjectId bsonObjectId = new BsonObjectId(object);
		return valueFactory.createModelValue(bsonObjectId);
	}

	@Nullable
	@Override
	public ObjectId decode(ModelValue<BsonDocument, BsonArray, BsonValue> value, Type type, Converter<BsonDocument, BsonArray, BsonValue> converter) {
		BsonValue bsonValue = value.getValue();
		if (bsonValue == null || !bsonValue.isObjectId()) {
			throw new CodecDecodeException("Value is not an ObjectId");
		}
		BsonObjectId bsonObjectId = bsonValue.asObjectId();
		return bsonObjectId.getValue();
	}
}
