package com.google.android.gms.location;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api.zza;
import com.google.android.gms.common.api.Api.zze;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.internal.zzcdj;

final class zzs extends zza<zzcdj, Object> {
    zzs() {
    }

    public final /* synthetic */ zze zza(Context context, Looper looper, zzq zzq, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        return new zzcdj(context, looper, connectionCallbacks, onConnectionFailedListener, "locationServices", zzq);
    }
}
