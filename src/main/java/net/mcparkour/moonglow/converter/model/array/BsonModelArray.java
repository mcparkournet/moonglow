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

package net.mcparkour.moonglow.converter.model.array;

import java.util.Iterator;
import net.mcparkour.moonglow.converter.model.value.BsonModelValue;
import net.mcparkour.unifig.model.array.ModelArray;
import net.mcparkour.unifig.model.value.ModelValue;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

public class BsonModelArray implements ModelArray<BsonDocument, BsonArray, BsonValue> {

	private BsonArray array;

	public BsonModelArray(BsonArray array) {
		this.array = array;
	}

	@Override
	public void addValue(ModelValue<BsonDocument, BsonArray, BsonValue> value) {
		BsonValue rawValue = value.getValue();
		this.array.add(rawValue);
	}

	@Override
	public int getSize() {
		return this.array.size();
	}

	@Override
	public BsonArray getArray() {
		return this.array;
	}

	@Override
	public Iterator<ModelValue<BsonDocument, BsonArray, BsonValue>> iterator() {
		Iterator<BsonValue> iterator = this.array.iterator();
		return new BsonModelArrayIterator(iterator);
	}

	private static final class BsonModelArrayIterator implements Iterator<ModelValue<BsonDocument, BsonArray, BsonValue>> {

		private Iterator<BsonValue> iterator;

		private BsonModelArrayIterator(Iterator<BsonValue> iterator) {
			this.iterator = iterator;
		}

		@Override
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public ModelValue<BsonDocument, BsonArray, BsonValue> next() {
			BsonValue next = this.iterator.next();
			return new BsonModelValue(next);
		}
	}
}
