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

import java.util.Spliterator;
import java.util.stream.StreamSupport;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import net.mcparkour.moonglow.converter.DocumentConverter;
import org.bson.BsonDocument;
import org.jetbrains.annotations.Nullable;

public class GetMethodHandler implements MethodHandler {

	private MongoCollection<BsonDocument> collection;
	private DocumentConverter converter;
	private Class<?> documentType;

	public GetMethodHandler(MongoCollection<BsonDocument> collection, DocumentConverter converter, Class<?> documentType) {
		this.collection = collection;
		this.converter = converter;
		this.documentType = documentType;
	}

	@Override
	@Nullable
	public Object handle(Object[] arguments) {
		FindIterable<BsonDocument> found = this.collection.find();
		Spliterator<BsonDocument> spliterator = found.spliterator();
		return StreamSupport.stream(spliterator, false)
			.map(document -> this.converter.fromDocument(document, this.documentType));
	}
}
