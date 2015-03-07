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

public class EncryptedMessageV1 extends BserObject {

    private long rid;
    private int contentType;
    private byte[] content;

    public EncryptedMessageV1(long rid, int contentType, byte[] content) {
        this.rid = rid;
        this.contentType = contentType;
        this.content = content;
    }

    public EncryptedMessageV1() {

    }

    public long getRid() {
        return this.rid;
    }

    public int getContentType() {
        return this.contentType;
    }

    public byte[] getContent() {
        return this.content;
    }

    @Override
    public void parse(BserValues values) throws IOException {
        this.rid = values.getLong(1);
        this.contentType = values.getInt(2);
        this.content = values.getBytes(3);
    }

    @Override
    public void serialize(BserWriter writer) throws IOException {
        writer.writeLong(1, this.rid);
        writer.writeInt(2, this.contentType);
        writer.writeBytes(3, this.content);
    }

    @Override
    public String toString() {
        String res = "struct EncryptedMessageV1{";
        res += "}";
        return res;
    }

}
