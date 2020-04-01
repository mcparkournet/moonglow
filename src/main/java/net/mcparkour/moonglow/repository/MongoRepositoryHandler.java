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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import com.mongodb.client.MongoCollection;
import net.mcparkour.common.reflection.Reflections;
import net.mcparkour.moonglow.converter.DocumentConverter;
import net.mcparkour.moonglow.repository.handler.CountMethodHandler;
import net.mcparkour.moonglow.repository.handler.DeleteMethodHandler;
import net.mcparkour.moonglow.repository.handler.DropMethodHandler;
import net.mcparkour.moonglow.repository.handler.GetMethodHandler;
import net.mcparkour.moonglow.repository.handler.InsertMethodHandler;
import net.mcparkour.moonglow.repository.handler.MethodHandler;
import net.mcparkour.moonglow.repository.handler.UpdateMethodHandler;
import org.bson.BsonDocument;
import org.jetbrains.annotations.Nullable;

public class MongoRepositoryHandler implements InvocationHandler {

	private static final Method INSERT_METHOD = Reflections.getMethod(BaseRepository.class, "insert", Object.class);
	private static final Method UPDATE_METHOD = Reflections.getMethod(BaseRepository.class, "update", Object.class);
	private static final Method DELETE_METHOD = Reflections.getMethod(BaseRepository.class, "delete", Object.class);
	private static final Method GET_METHOD = Reflections.getMethod(BaseRepository.class, "get");
	private static final Method COUNT_METHOD = Reflections.getMethod(BaseRepository.class, "count");
	private static final Method DROP_METHOD = Reflections.getMethod(BaseRepository.class, "drop");

	private Class<?> documentType;
	private MongoCollection<BsonDocument> collection;
	private DocumentConverter converter;
	private DocumentMetadata metadata;
	private Map<Method, MethodHandler> handlers;

	public MongoRepositoryHandler(Class<?> documentType, MongoCollection<BsonDocument> collection, DocumentConverter converter, DocumentMetadata metadata) {
		this.documentType = documentType;
		this.collection = collection;
		this.converter = converter;
		this.metadata = metadata;
		this.handlers = createHandlers(collection, converter, metadata);
	}

	private Map<Method, MethodHandler> createHandlers(MongoCollection<BsonDocument> collection, DocumentConverter converter, DocumentMetadata metadata) {
		Map<Method, MethodHandler> handlers = new HashMap<>(6);
		handlers.put(INSERT_METHOD, new InsertMethodHandler(collection, converter));
		handlers.put(UPDATE_METHOD, new UpdateMethodHandler(collection, converter, metadata));
		handlers.put(DELETE_METHOD, new DeleteMethodHandler(collection, converter, metadata));
		handlers.put(GET_METHOD, new GetMethodHandler(collection, converter, this.documentType));
		handlers.put(COUNT_METHOD, new CountMethodHandler(collection));
		handlers.put(DROP_METHOD, new DropMethodHandler(collection));
		return handlers;
	}

	@Override
	@Nullable
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		MethodHandler handler = this.handlers.get(method);
		if (handler != null) {
			return handler.handle(args);
		}
		return null;
	}
}
