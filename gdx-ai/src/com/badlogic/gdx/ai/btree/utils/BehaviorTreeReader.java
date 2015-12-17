
// line 1 "BehaviorTreeReader.rl"
// Do not edit this file! Generated by Ragel.
// Ragel.exe -G2 -J -o BehaviorTreeReader.java BehaviorTreeReader.rl
/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.badlogic.gdx.ai.btree.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.StreamUtils;

/** An abstract event driven {@link BehaviorTree} parser.
 * 
 * @author davebaol */
public abstract class BehaviorTreeReader {

	private static final String LOG_TAG = "BehaviorTreeReader";

	protected boolean debug = false;
	protected int lineNumber;
	protected boolean reportsComments;

	protected abstract void startStatement (int indent, String name);

	protected abstract void attribute (String name, Object value);

	protected abstract void endStatement ();

	protected void comment (String text) {
	}

	public BehaviorTreeReader () {
		this(false);
	}

	public BehaviorTreeReader (boolean reportsComments) {
		this.reportsComments = reportsComments;
	}

	/** Parses the given string.
	 * @param string the string
	 * @throws SerializationException if the string cannot be successfully parsed. */
	public void parse (String string) {
		char[] data = string.toCharArray();
		parse(data, 0, data.length);
	}

	/** Parses the given reader.
	 * @param reader the reader
	 * @throws SerializationException if the reader cannot be successfully parsed. */
	public void parse (Reader reader) {
		try {
			char[] data = new char[1024];
			int offset = 0;
			while (true) {
				int length = reader.read(data, offset, data.length - offset);
				if (length == -1) break;
				if (length == 0) {
					char[] newData = new char[data.length * 2];
					System.arraycopy(data, 0, newData, 0, data.length);
					data = newData;
				} else
					offset += length;
			}
			parse(data, 0, offset);
		} catch (IOException ex) {
			throw new SerializationException(ex);
		} finally {
			StreamUtils.closeQuietly(reader);
		}
	}

	/** Parses the given input stream.
	 * @param input the input stream
	 * @throws SerializationException if the input stream cannot be successfully parsed. */
	public void parse (InputStream input) {
		try {
			parse(new InputStreamReader(input, "UTF-8"));
		} catch (IOException ex) {
			throw new SerializationException(ex);
		} finally {
			StreamUtils.closeQuietly(input);
		}
	}

	/** Parses the given file.
	 * @param file the file
	 * @throws SerializationException if the file cannot be successfully parsed. */
	public void parse (FileHandle file) {
		try {
			parse(file.reader("UTF-8"));
		} catch (Exception ex) {
			throw new SerializationException("Error parsing file: " + file, ex);
		}
	}

	/** Parses the given data buffer from the offset up to the specified number of characters.
	 * @param data the buffer
	 * @param offset the initial index
	 * @param length the specified number of characters to parse.
	 * @throws SerializationException if the buffer cannot be successfully parsed. */
	public void parse (char[] data, int offset, int length) {
		int cs, p = offset, pe = length, eof = pe;

		int s = 0;
		int indent = 0;
		String statementName = null;
		boolean taskProcessed = false;
		boolean needsUnescape = false;
		boolean stringIsUnquoted = false;
		RuntimeException parseRuntimeEx = null;
		String attrName = null;

		lineNumber = 1;

		try {
		
// line 140 "BehaviorTreeReader.java"
	{
	cs = btree_start;
	}

// line 144 "BehaviorTreeReader.java"
	{
	int _klen;
	int _trans = 0;
	int _acts;
	int _nacts;
	int _keys;
	int _goto_targ = 0;

	_goto: while (true) {
	switch ( _goto_targ ) {
	case 0:
	if ( p == pe ) {
		_goto_targ = 4;
		continue _goto;
	}
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
case 1:
	_match: do {
	_keys = _btree_key_offsets[cs];
	_trans = _btree_index_offsets[cs];
	_klen = _btree_single_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + _klen - 1;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + ((_upper-_lower) >> 1);
			if ( data[p] < _btree_trans_keys[_mid] )
				_upper = _mid - 1;
			else if ( data[p] > _btree_trans_keys[_mid] )
				_lower = _mid + 1;
			else {
				_trans += (_mid - _keys);
				break _match;
			}
		}
		_keys += _klen;
		_trans += _klen;
	}

	_klen = _btree_range_lengths[cs];
	if ( _klen > 0 ) {
		int _lower = _keys;
		int _mid;
		int _upper = _keys + (_klen<<1) - 2;
		while (true) {
			if ( _upper < _lower )
				break;

			_mid = _lower + (((_upper-_lower) >> 1) & ~1);
			if ( data[p] < _btree_trans_keys[_mid] )
				_upper = _mid - 2;
			else if ( data[p] > _btree_trans_keys[_mid+1] )
				_lower = _mid + 2;
			else {
				_trans += ((_mid - _keys)>>1);
				break _match;
			}
		}
		_trans += _klen;
	}
	} while (false);

	_trans = _btree_indicies[_trans];
	cs = _btree_trans_targs[_trans];

	if ( _btree_trans_actions[_trans] != 0 ) {
		_acts = _btree_trans_actions[_trans];
		_nacts = (int) _btree_actions[_acts++];
		while ( _nacts-- > 0 )
	{
			switch ( _btree_actions[_acts++] )
			{
	case 0:
// line 141 "BehaviorTreeReader.rl"
	{
				String value = new String(data, s, p - s);
				s = p;
				if (needsUnescape) value = unescape(value);
				outer:
				if (stringIsUnquoted) {
					if (debug) GdxAI.getLogger().info(LOG_TAG, "string: " + attrName + "=" + value);
					if (value.equals("true")) {
						if (debug) GdxAI.getLogger().info(LOG_TAG, "boolean: " + attrName + "=true");
						attribute(attrName, Boolean.TRUE);
						break outer;
					} else if (value.equals("false")) {
						if (debug) GdxAI.getLogger().info(LOG_TAG, "boolean: " + attrName + "=false");
						attribute(attrName, Boolean.FALSE);
						break outer;
					} else if (value.equals("null")) {
						attribute(attrName, null);
						break outer;
					} else { // number
						try {
							if (containsFloatingPointCharacters(value)) {
								if (debug) GdxAI.getLogger().info(LOG_TAG, "double: " + attrName + "=" + Double.parseDouble(value));
								attribute(attrName, new Double(value));
								break outer;
							} else {
								if (debug) GdxAI.getLogger().info(LOG_TAG, "double: " + attrName + "=" + Double.parseDouble(value));
								attribute(attrName, new Long(value));
								break outer;
							}
						} catch (NumberFormatException nfe) {
							throw new GdxRuntimeException("Attribute value must be a number, a boolean, a string or null");
						}
					}
				}
				else {
					if (debug) GdxAI.getLogger().info(LOG_TAG, "string: " + attrName + "=\"" + value + "\"");
					attribute(attrName, value);
				}
				stringIsUnquoted = false;
			}
	break;
	case 1:
// line 181 "BehaviorTreeReader.rl"
	{
				if (debug) GdxAI.getLogger().info(LOG_TAG, "unquotedChars");
				s = p;
				needsUnescape = false;
				stringIsUnquoted = true;
				outer:
				while (true) {
					switch (data[p]) {
					case '\\':
						needsUnescape = true;
						break;
					case ' ':
					case '\r':
					case '\n':
					case '\t':
						break outer;
					}
					// if (debug) GdxAI.getLogger().info(LOG_TAG, "unquotedChar (value): '" + data[p] + "'");
					p++;
					if (p == eof) break;
				}
				p--;
			}
	break;
	case 2:
// line 204 "BehaviorTreeReader.rl"
	{
				if (debug) GdxAI.getLogger().info(LOG_TAG, "quotedChars");
				s = ++p;
				needsUnescape = false;
				outer:
				while (true) {
					switch (data[p]) {
					case '\\':
						needsUnescape = true;
						p++;
						break;
					case '"':
						break outer;
					}
					// if (debug) GdxAI.getLogger().info(LOG_TAG, "quotedChar: '" + data[p] + "'");
					p++;
					if (p == eof) break;
				}
				p--;
			}
	break;
	case 3:
// line 224 "BehaviorTreeReader.rl"
	{
				indent = 0;
				statementName = null;
				taskProcessed = false;
				lineNumber++;
				if (debug) GdxAI.getLogger().info(LOG_TAG, "****NEWLINE**** "+lineNumber);
			}
	break;
	case 4:
// line 231 "BehaviorTreeReader.rl"
	{
				indent++;
			}
	break;
	case 5:
// line 234 "BehaviorTreeReader.rl"
	{
				taskProcessed = true;
				if (statementName != null)
					endStatement();
				if (debug) GdxAI.getLogger().info(LOG_TAG, "endLine: indent: " + indent + " taskName: " + statementName + " data[" + p + "] = " + (p >= eof ? "EOF" : "\"" + data[p] + "\""));
			}
	break;
	case 6:
// line 240 "BehaviorTreeReader.rl"
	{
				s = p;
			}
	break;
	case 7:
// line 243 "BehaviorTreeReader.rl"
	{
				if (reportsComments) {
					comment(new String(data, s, p - s));
				} else {
					if (debug) GdxAI.getLogger().info(LOG_TAG, "# Comment");
				}
			}
	break;
	case 8:
// line 250 "BehaviorTreeReader.rl"
	{
				statementName = new String(data, s, p - s);
				startStatement(indent, statementName);
			}
	break;
	case 9:
// line 254 "BehaviorTreeReader.rl"
	{
				attrName = new String(data, s, p - s);
			}
	break;
// line 370 "BehaviorTreeReader.java"
			}
		}
	}

case 2:
	if ( cs == 0 ) {
		_goto_targ = 5;
		continue _goto;
	}
	if ( ++p != pe ) {
		_goto_targ = 1;
		continue _goto;
	}
case 4:
	if ( p == eof )
	{
	int __acts = _btree_eof_actions[cs];
	int __nacts = (int) _btree_actions[__acts++];
	while ( __nacts-- > 0 ) {
		switch ( _btree_actions[__acts++] ) {
	case 0:
// line 141 "BehaviorTreeReader.rl"
	{
				String value = new String(data, s, p - s);
				s = p;
				if (needsUnescape) value = unescape(value);
				outer:
				if (stringIsUnquoted) {
					if (debug) GdxAI.getLogger().info(LOG_TAG, "string: " + attrName + "=" + value);
					if (value.equals("true")) {
						if (debug) GdxAI.getLogger().info(LOG_TAG, "boolean: " + attrName + "=true");
						attribute(attrName, Boolean.TRUE);
						break outer;
					} else if (value.equals("false")) {
						if (debug) GdxAI.getLogger().info(LOG_TAG, "boolean: " + attrName + "=false");
						attribute(attrName, Boolean.FALSE);
						break outer;
					} else if (value.equals("null")) {
						attribute(attrName, null);
						break outer;
					} else { // number
						try {
							if (containsFloatingPointCharacters(value)) {
								if (debug) GdxAI.getLogger().info(LOG_TAG, "double: " + attrName + "=" + Double.parseDouble(value));
								attribute(attrName, new Double(value));
								break outer;
							} else {
								if (debug) GdxAI.getLogger().info(LOG_TAG, "double: " + attrName + "=" + Double.parseDouble(value));
								attribute(attrName, new Long(value));
								break outer;
							}
						} catch (NumberFormatException nfe) {
							throw new GdxRuntimeException("Attribute value must be a number, a boolean, a string or null");
						}
					}
				}
				else {
					if (debug) GdxAI.getLogger().info(LOG_TAG, "string: " + attrName + "=\"" + value + "\"");
					attribute(attrName, value);
				}
				stringIsUnquoted = false;
			}
	break;
	case 5:
// line 234 "BehaviorTreeReader.rl"
	{
				taskProcessed = true;
				if (statementName != null)
					endStatement();
				if (debug) GdxAI.getLogger().info(LOG_TAG, "endLine: indent: " + indent + " taskName: " + statementName + " data[" + p + "] = " + (p >= eof ? "EOF" : "\"" + data[p] + "\""));
			}
	break;
	case 6:
// line 240 "BehaviorTreeReader.rl"
	{
				s = p;
			}
	break;
	case 7:
// line 243 "BehaviorTreeReader.rl"
	{
				if (reportsComments) {
					comment(new String(data, s, p - s));
				} else {
					if (debug) GdxAI.getLogger().info(LOG_TAG, "# Comment");
				}
			}
	break;
	case 8:
// line 250 "BehaviorTreeReader.rl"
	{
				statementName = new String(data, s, p - s);
				startStatement(indent, statementName);
			}
	break;
// line 466 "BehaviorTreeReader.java"
		}
	}
	}

case 5:
	}
	break; }
	}

// line 276 "BehaviorTreeReader.rl"

		} catch (RuntimeException ex) {
			parseRuntimeEx = ex;
		}

		if (p < pe || (statementName != null && !taskProcessed)) {
			throw new SerializationException("Error parsing behavior tree on line " + lineNumber + " near: " + new String(data, p, pe - p),
				parseRuntimeEx);
		} else if (parseRuntimeEx != null) {
			throw new SerializationException("Error parsing behavior tree: " + new String(data), parseRuntimeEx);
		}
	}

	
// line 489 "BehaviorTreeReader.java"
private static byte[] init__btree_actions_0()
{
	return new byte [] {
	    0,    1,    0,    1,    1,    1,    2,    1,    3,    1,    4,    1,
	    5,    1,    6,    1,    8,    1,    9,    2,    0,    5,    2,    5,
	    3,    2,    7,    5,    2,    8,    5,    3,    0,    5,    3,    3,
	    6,    7,    5,    3,    7,    5,    3,    3,    8,    5,    3,    4,
	    6,    7,    5,    3
	};
}

private static final byte _btree_actions[] = init__btree_actions_0();


private static short[] init__btree_key_offsets_0()
{
	return new short [] {
	    0,    0,    1,   13,   17,   24,   25,   29,   34,   46,   50,   57,
	   58,   62,   67,   77,   87,   92,   94,   96,  110,  120,  125,  130,
	  135,  140,  154,  164,  169,  174
	};
}

private static final short _btree_key_offsets[] = init__btree_key_offsets_0();


private static char[] init__btree_trans_keys_0()
{
	return new char [] {
	   10,    9,   13,   32,   58,   63,   95,   48,   57,   65,   90,   97,
	  122,    9,   13,   32,   58,    9,   10,   13,   32,   34,   35,   58,
	   34,    9,   13,   32,   58,   95,   65,   90,   97,  122,    9,   13,
	   32,   58,   63,   95,   48,   57,   65,   90,   97,  122,    9,   13,
	   32,   58,    9,   10,   13,   32,   34,   35,   58,   34,    9,   13,
	   32,   58,   95,   65,   90,   97,  122,    9,   10,   13,   32,   35,
	   95,   65,   90,   97,  122,    9,   10,   13,   32,   35,   95,   65,
	   90,   97,  122,    9,   10,   13,   32,   35,   10,   13,   10,   13,
	    9,   10,   13,   32,   35,   46,   63,   95,   48,   57,   65,   90,
	   97,  122,    9,   10,   13,   32,   35,   95,   65,   90,   97,  122,
	    9,   10,   13,   32,   35,    9,   10,   13,   32,   35,    9,   10,
	   13,   32,   35,    9,   10,   13,   32,   35,    9,   10,   13,   32,
	   35,   46,   63,   95,   48,   57,   65,   90,   97,  122,    9,   10,
	   13,   32,   35,   95,   65,   90,   97,  122,    9,   10,   13,   32,
	   35,    9,   10,   13,   32,   35,    9,   10,   13,   32,   35,    0
	};
}

private static final char _btree_trans_keys[] = init__btree_trans_keys_0();


private static byte[] init__btree_single_lengths_0()
{
	return new byte [] {
	    0,    1,    6,    4,    7,    1,    4,    1,    6,    4,    7,    1,
	    4,    1,    6,    6,    5,    2,    2,    8,    6,    5,    5,    5,
	    5,    8,    6,    5,    5,    5
	};
}

private static final byte _btree_single_lengths[] = init__btree_single_lengths_0();


private static byte[] init__btree_range_lengths_0()
{
	return new byte [] {
	    0,    0,    3,    0,    0,    0,    0,    2,    3,    0,    0,    0,
	    0,    2,    2,    2,    0,    0,    0,    3,    2,    0,    0,    0,
	    0,    3,    2,    0,    0,    0
	};
}

private static final byte _btree_range_lengths[] = init__btree_range_lengths_0();


private static short[] init__btree_index_offsets_0()
{
	return new short [] {
	    0,    0,    2,   12,   17,   25,   27,   32,   36,   46,   51,   59,
	   61,   66,   70,   79,   88,   94,   97,  100,  112,  121,  127,  133,
	  139,  145,  157,  166,  172,  178
	};
}

private static final short _btree_index_offsets[] = init__btree_index_offsets_0();


private static byte[] init__btree_indicies_0()
{
	return new byte [] {
	    0,    1,    2,    2,    2,    4,    5,    3,    3,    3,    3,    1,
	    6,    6,    6,    7,    1,    7,    1,    7,    7,    9,    1,    1,
	    8,   10,    1,    2,    2,    2,    4,    1,   11,   11,   11,    1,
	   12,   12,   12,   14,   15,   13,   13,   13,   13,    1,   16,   16,
	   16,   17,    1,   17,    1,   17,   17,   19,    1,    1,   18,   20,
	    1,   12,   12,   12,   14,    1,   21,   21,   21,    1,   22,   23,
	   24,   22,   25,   26,   26,   26,    1,   27,   23,   28,   27,   25,
	   29,   29,   29,    1,   28,   23,   28,   28,   25,    1,   31,   32,
	   30,   34,   35,   33,   36,   37,   36,   36,   38,   39,   40,   11,
	   11,   11,   11,    1,   41,   23,   41,   41,   25,   42,   42,   42,
	    1,   43,   44,   43,   43,   45,    1,   41,   23,   41,   41,   25,
	    1,   36,   37,   36,   36,   38,    1,   46,   23,   24,   46,   25,
	    1,   47,   37,   48,   47,   38,   49,   50,   21,   21,   21,   21,
	    1,   51,   23,   52,   51,   25,   53,   53,   53,    1,   54,   44,
	   55,   54,   45,    1,   51,   23,   52,   51,   25,    1,   47,   37,
	   48,   47,   38,    1,    0
	};
}

private static final byte _btree_indicies[] = init__btree_indicies_0();


private static byte[] init__btree_trans_targs_0()
{
	return new byte [] {
	   15,    0,    3,    2,    4,    6,    3,    4,   21,    5,   22,   19,
	    9,    8,   10,   12,    9,   10,   27,   11,   28,   25,   14,   15,
	   24,   17,   25,   15,   16,   19,   18,   15,    1,   18,   15,    1,
	   20,   15,   17,    7,   23,   20,    2,   20,   15,   17,   24,   26,
	   26,   13,   29,   26,   26,    8,   26,   26
	};
}

private static final byte _btree_trans_targs[] = init__btree_trans_targs_0();


private static byte[] init__btree_trans_actions_0()
{
	return new byte [] {
	    7,    0,   17,    0,   17,    0,    0,    0,    3,    5,    1,    0,
	   17,    0,   17,    0,    0,    0,    3,    5,    1,    0,    9,   22,
	   11,    0,   13,    9,    0,   13,   13,   47,   35,    0,   39,   25,
	   15,   43,   15,    0,    0,    0,   13,    1,   31,    1,    0,   15,
	   28,    0,    0,    0,   11,   13,    1,   19
	};
}

private static final byte _btree_trans_actions[] = init__btree_trans_actions_0();


private static byte[] init__btree_eof_actions_0()
{
	return new byte [] {
	    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
	    0,    0,   11,   11,   11,   35,   25,   28,   11,   19,   11,   28,
	   11,   28,   11,   19,   11,   28
	};
}

private static final byte _btree_eof_actions[] = init__btree_eof_actions_0();


static final int btree_start = 14;
static final int btree_first_final = 14;
static final int btree_error = 0;

static final int btree_en_main = 14;


// line 290 "BehaviorTreeReader.rl"

	private static boolean containsFloatingPointCharacters (String value) {
		for (int i = 0, n = value.length(); i < n; i++) {
			switch (value.charAt(i)) {
			case '.':
			case 'E':
			case 'e':
				return true;
			}
		}
		return false;
	}

	private static String unescape (String value) {
		int length = value.length();
		StringBuilder buffer = new StringBuilder(length + 16);
		for (int i = 0; i < length;) {
			char c = value.charAt(i++);
			if (c != '\\') {
				buffer.append(c);
				continue;
			}
			if (i == length) break;
			c = value.charAt(i++);
			if (c == 'u') {
				buffer.append(Character.toChars(Integer.parseInt(value.substring(i, i + 4), 16)));
				i += 4;
				continue;
			}
			switch (c) {
			case '"':
			case '\\':
			case '/':
				break;
			case 'b':
				c = '\b';
				break;
			case 'f':
				c = '\f';
				break;
			case 'n':
				c = '\n';
				break;
			case 'r':
				c = '\r';
				break;
			case 't':
				c = '\t';
				break;
			default:
				throw new SerializationException("Illegal escaped character: \\" + c);
			}
			buffer.append(c);
		}
		return buffer.toString();
	}
}