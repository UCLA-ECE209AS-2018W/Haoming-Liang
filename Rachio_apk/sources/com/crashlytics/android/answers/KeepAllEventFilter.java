package com.crashlytics.android.answers;

final class KeepAllEventFilter implements EventFilter {
    KeepAllEventFilter() {
    }

    public final boolean skipEvent(SessionEvent sessionEvent) {
        return false;
    }
}
