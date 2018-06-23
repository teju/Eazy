package eazi.com.eazi.model;

import java.util.List;

public class Messages {
    private List<InternalMessage> mInternalMessages;

    public Messages(final List<InternalMessage> internalMessages) {
        this.mInternalMessages = internalMessages;
    }

    public InternalMessage get(final int position) {
        return mInternalMessages.get(position);
    }

    public int size() {
        return mInternalMessages.size();
    }
}
