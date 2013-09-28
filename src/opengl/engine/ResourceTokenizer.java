/*
 * TokHelper.java
 *
 * Created on 31 maj 2006, 22:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package opengl.engine;

import java.io.IOException;
import java.io.StreamTokenizer;

/**
 *
 * @author Jurij Kowal
 */
public class ResourceTokenizer {
    final static char[][] WORD_CHARS       = { { '\\', '\\' }, { 'a', 'z' }, { 'A', 'Z' }, { '#', '#' }, { '0', '9' }, { '-', '-' }, { '.', '.' }, { ':', ':' }, { 128 + 32, 255 } };
    final static char[][] WHITESPACE_CHARS = { { 0, ' ' }, { ',', ',' }, { '{', '{' }, { '}', '}' } };
    final static char[]   COMMENT_CHARS    = { '/' };
    final static char[]   QUOTE_CHARS      = { '"', '\'' };

    private StreamTokenizer tokenizer;
    private boolean eof;

    public ResourceTokenizer(StreamTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public static void setupTokenizer(StreamTokenizer tokenizer, char[][] wordChars, char[][] whiteSpaceChars, char[] commentChars, char[] quoteChars) {
        int i = 0;

        // Konfiguracja tokenizera
        tokenizer.resetSyntax();
        for (i=0; i<wordChars.length; i++)       tokenizer.wordChars( wordChars[i][0], wordChars[i][1] );
        for (i=0; i<whiteSpaceChars.length; i++) tokenizer.whitespaceChars( whiteSpaceChars[i][0], whiteSpaceChars[i][1] );
        for (i=0; i<commentChars.length; i++)    tokenizer.commentChar( commentChars[i] );
        for (i=0; i<quoteChars.length; i++)      tokenizer.quoteChar( quoteChars[i] );
        tokenizer.slashSlashComments(true);
        tokenizer.slashStarComments(true);
    }

    public static void setupTokenizer(StreamTokenizer tokenizer) {
        setupTokenizer(tokenizer, WORD_CHARS, WHITESPACE_CHARS, COMMENT_CHARS, QUOTE_CHARS);
    }

    public String nextToken() throws IOException {
        int token = tokenizer.nextToken();

        if (token == StreamTokenizer.TT_EOF) {
            eof = true;
        } else if (token != StreamTokenizer.TT_WORD) {
            throw new IOException("Parse error reading token at line " + tokenizer.lineno());
        }

        return String.valueOf(tokenizer.sval);
    }

    public String nextString() throws IOException {
        if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
            throw new IOException("Parse error reading string at line " + tokenizer.lineno());
        }

        return String.valueOf(tokenizer.sval);
    }

    // DO POPRAWIENIA !!!
    // DO POPRAWIENIA !!!
    // DO POPRAWIENIA !!!
    // DO POPRAWIENIA !!!
    // DO POPRAWIENIA !!!
    public String nextQuote() throws IOException {
        try {
            tokenizer.nextToken();
        } catch (IOException e) {
            throw new IOException("Parse error reading quote at line " + tokenizer.lineno());
        }
        return String.valueOf(tokenizer.sval);
    }

    public int nextInt() throws IOException {
        if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
            throw new IOException("Parse error reading int at line " + tokenizer.lineno());
        }

        try {
            return Integer.parseInt(tokenizer.sval);
        } catch (NumberFormatException e) {
            throw new IOException("Wrong number format reading int at line " + tokenizer.lineno());
        }
    }

    public float nextFloat() throws IOException {
        if (tokenizer.nextToken() != StreamTokenizer.TT_WORD) {
            throw new IOException("Parse error reading float at line " + tokenizer.lineno());
        } try {
            return Float.parseFloat(tokenizer.sval);
        } catch (NumberFormatException e) {
            throw new IOException("Wrong number format reading float at line " + tokenizer.lineno());
        }
    }

    public boolean eof() {
        return eof;
    }

}
