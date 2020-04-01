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

package net.mcparkour.moonglow.database;

import com.mongodb.client.MongoCollection;
import net.mcparkour.common.reflection.Reflections;
import net.mcparkour.moonglow.annotation.repository.DocumentType;
import net.mcparkour.moonglow.annotation.repository.Repository;
import net.mcparkour.moonglow.converter.DocumentConverter;
import net.mcparkour.moonglow.repository.BaseRepository;
import net.mcparkour.moonglow.repository.DocumentMetadata;
import net.mcparkour.moonglow.repository.DocumentMetadataFactory;
import net.mcparkour.moonglow.repository.MongoRepositoryHandler;
import org.bson.BsonDocument;

public class MongoDatabase implements Database {

	private com.mongodb.client.MongoDatabase database;

	public MongoDatabase(com.mongodb.client.MongoDatabase database) {
		this.database = database;
	}

	@Override
	public <T extends BaseRepository<?>> T createRepository(Class<T> repositoryClass) {
		Repository repositoryAnnotation = repositoryClass.getDeclaredAnnotation(Repository.class);
		String repositoryName = repositoryAnnotation.value();
		DocumentType documentTypeAnnotation = repositoryClass.getDeclaredAnnotation(DocumentType.class);
		Class<?> documentType = documentTypeAnnotation.value();
		MongoCollection<BsonDocument> collection = this.database.getCollection(repositoryName, BsonDocument.class);
		DocumentConverter converter = new DocumentConverter();
		DocumentMetadataFactory metadataFactory = new DocumentMetadataFactory();
		DocumentMetadata metadata = metadataFactory.createMetadata(documentType);
		MongoRepositoryHandler handler = new MongoRepositoryHandler(documentType, collection, converter, metadata);
		return Reflections.newProxyInstance(repositoryClass, handler);
	}
}
