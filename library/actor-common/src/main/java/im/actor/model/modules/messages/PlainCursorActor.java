package im.actor.model.modules.messages;

import java.io.IOException;
import java.util.HashSet;

import im.actor.model.droidkit.engine.SyncKeyValue;
import im.actor.model.entity.Peer;
import im.actor.model.modules.Modules;
import im.actor.model.modules.messages.entity.PlainCursor;
import im.actor.model.modules.messages.entity.PlainCursorsStorage;
import im.actor.model.modules.utils.ModuleActor;

/**
 * Created by ex3ndr on 17.02.15.
 */
public abstract class PlainCursorActor extends ModuleActor {

    private PlainCursorsStorage plainCursorsStorage;
    private HashSet<Peer> inProgress = new HashSet<Peer>();
    private long cursorId;
    private SyncKeyValue keyValue;

    public PlainCursorActor(long cursorId, Modules messenger) {
        super(messenger);
        this.cursorId = cursorId;
        this.keyValue = messenger.getMessagesModule().getCursorStorage();
    }

    @Override
    public void preStart() {
        super.preStart();

        plainCursorsStorage = new PlainCursorsStorage();
        byte[] data = keyValue.get(cursorId);
        if (data != null) {
            try {
                plainCursorsStorage = PlainCursorsStorage.fromBytes(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (PlainCursor cursor : plainCursorsStorage.getAllCursors()) {
            if (cursor.getSortDate() < cursor.getPendingSortDate()) {
                inProgress.add(cursor.getPeer());
                perform(cursor.getPeer(), cursor.getPendingSortDate());
            }
        }
    }

    protected final void moveCursor(Peer peer, long date) {
        PlainCursor cursor = plainCursorsStorage.getCursor(peer);
        if (date <= cursor.getSortDate()) {
            return;
        }
        if (date <= cursor.getPendingSortDate()) {
            return;
        }

        date = Math.max(cursor.getPendingSortDate(), date);

        plainCursorsStorage.putCursor(cursor.changePendingSortDate(date));

        saveCursorState();

        if (inProgress.contains(peer)) {
            return;
        }

        inProgress.add(peer);
        perform(peer, date);
    }

    protected final void onMoved(Peer peer, long date) {
        inProgress.remove(peer);

        PlainCursor cursor = plainCursorsStorage.getCursor(peer);
        cursor = cursor.changeSortDate(Math.max(date, cursor.getSortDate()));
        plainCursorsStorage.putCursor(cursor);
        saveCursorState();

        if (cursor.getSortDate() < cursor.getPendingSortDate()) {
            inProgress.add(peer);
            perform(peer, cursor.getPendingSortDate());
        }
    }

    // Execution
    protected abstract void perform(Peer peer, long date);

    protected void onCompleted(Peer peer, long date) {
        self().send(new OnCompleted(peer, date));
    }

    protected void onError(Peer peer, long date) {
        // Ignore
    }


    private void saveCursorState() {
        keyValue.put(cursorId, plainCursorsStorage.toByteArray());
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof OnCompleted) {
            OnCompleted completed = (OnCompleted) message;
            onMoved(completed.getPeer(), completed.getDate());
        } else {
            drop(message);
        }
    }

    private static class OnCompleted {
        private Peer peer;
        private long date;

        private OnCompleted(Peer peer, long date) {
            this.peer = peer;
            this.date = date;
        }

        public Peer getPeer() {
            return peer;
        }

        public long getDate() {
            return date;
        }
    }
}
