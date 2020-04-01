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

package net.mcparkour.moonglow.converter.model.object;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.mcparkour.moonglow.converter.model.value.BsonModelValue;
import net.mcparkour.unifig.model.object.ModelObject;
import net.mcparkour.unifig.model.value.ModelValue;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

public class BsonModelObject implements ModelObject<BsonDocument, BsonArray, BsonValue> {

	private BsonDocument object;

	public BsonModelObject(BsonDocument object) {
		this.object = object;
	}

	@Override
	public ModelValue<BsonDocument, BsonArray, BsonValue> getValue(String key) {
		BsonValue value = this.object.get(key);
		return new BsonModelValue(value);
	}

	@Override
	public void setValue(String key, ModelValue<BsonDocument, BsonArray, BsonValue> value) {
		BsonValue rawValue = value.getValue();
		this.object.append(key, rawValue);
	}

	@Override
	public Set<Map.Entry<String, ModelValue<BsonDocument, BsonArray, BsonValue>>> getEntries() {
		return this.object.entrySet()
			.stream()
			.<Map.Entry<String, ModelValue<BsonDocument, BsonArray, BsonValue>>>map(entry -> {
				String key = entry.getKey();
				BsonValue value = entry.getValue();
				BsonModelValue modelValue = new BsonModelValue(value);
				return Map.entry(key, modelValue);
			})
			.collect(Collectors.toSet());
	}

	@Override
	public int getSize() {
		return this.object.size();
	}

	@Override
	public BsonDocument getObject() {
		return this.object;
	}
}
