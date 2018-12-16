/*
 * Decompiled with CFR 0_132.
 */
package main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class PassDoc
extends PlainDocument {
    PassDoc() {
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        if (this.getLength() > len) {
            super.remove(offs, len);
        }
    }

    @Override
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        int newLen = this.getLength() + str.length();
        if (newLen > 0 && newLen <= 3) {
            super.insertString(offset, str, a);
        }
    }
}

