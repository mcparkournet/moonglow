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

package net.mcparkour.moonglow.repository.handler;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import net.mcparkour.moonglow.converter.DocumentConverter;
import net.mcparkour.moonglow.repository.DocumentMetadata;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.Nullable;

public class DeleteMethodHandler implements MethodHandler {

	private MongoCollection<BsonDocument> collection;
	private DocumentConverter converter;
	private DocumentMetadata metadata;

	public DeleteMethodHandler(MongoCollection<BsonDocument> collection, DocumentConverter converter, DocumentMetadata metadata) {
		this.collection = collection;
		this.converter = converter;
		this.metadata = metadata;
	}

	@Override
	@Nullable
	public Object handle(Object[] arguments) {
		Object argument = arguments[0];
		Object identifier = this.metadata.getIdentifier(argument);
		if (identifier == null) {
			return DeleteResult.unacknowledged();
		}
		BsonValue bsonValue = this.converter.toValue(identifier);
		Bson filter = Filters.eq(bsonValue);
		return this.collection.deleteOne(filter);
	}
}
