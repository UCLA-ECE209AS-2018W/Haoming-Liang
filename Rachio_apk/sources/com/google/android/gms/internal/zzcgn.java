package com.google.android.gms.internal;

import java.util.concurrent.Callable;

final class zzcgn implements Callable<String> {
    private /* synthetic */ String zzbjh;
    private /* synthetic */ zzcgl zzbsY;

    zzcgn(zzcgl zzcgl, String str) {
        this.zzbsY = zzcgl;
        this.zzbjh = str;
    }

    public final /* synthetic */ Object call() throws Exception {
        zzceg zzdQ = this.zzbsY.zzwz().zzdQ(this.zzbjh);
        if (zzdQ != null) {
            return zzdQ.getAppInstanceId();
        }
        this.zzbsY.zzwF().zzyz().log("App info was null when attempting to get app instance id");
        return null;
    }
}
