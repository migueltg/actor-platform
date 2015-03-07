package im.actor.model.api;
/*
 *  Generated by the Actor API Scheme generator.  DO NOT EDIT!
 */

import im.actor.model.droidkit.bser.Bser;
import im.actor.model.droidkit.bser.BserObject;
import im.actor.model.droidkit.bser.BserValues;
import im.actor.model.droidkit.bser.BserWriter;
import static im.actor.model.droidkit.bser.Utils.*;
import java.io.IOException;
import im.actor.model.network.parser.*;
import java.util.List;
import java.util.ArrayList;

public class EncryptedDocumentV1Photo extends BserObject {

    private int width;
    private int height;

    public EncryptedDocumentV1Photo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public EncryptedDocumentV1Photo() {

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public void parse(BserValues values) throws IOException {
        this.width = values.getInt(1);
        this.height = values.getInt(2);
    }

    @Override
    public void serialize(BserWriter writer) throws IOException {
        writer.writeInt(1, this.width);
        writer.writeInt(2, this.height);
    }

    @Override
    public String toString() {
        String res = "struct EncryptedDocumentV1Photo{";
        res += "}";
        return res;
    }

}
