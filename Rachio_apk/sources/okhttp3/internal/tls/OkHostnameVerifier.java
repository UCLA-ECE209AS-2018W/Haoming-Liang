package okhttp3.internal.tls;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;

public final class OkHostnameVerifier implements HostnameVerifier {
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    public final boolean verify(String host, SSLSession session) {
        try {
            boolean z;
            X509Certificate x509Certificate = (X509Certificate) session.getPeerCertificates()[0];
            if (Util.verifyAsIpAddress(host)) {
                List subjectAltNames = getSubjectAltNames(x509Certificate, 7);
                int size = subjectAltNames.size();
                int i = 0;
                while (i < size) {
                    if (host.equalsIgnoreCase((String) subjectAltNames.get(i))) {
                        z = true;
                    } else {
                        i++;
                    }
                }
                return false;
            }
            String toLowerCase = host.toLowerCase(Locale.US);
            List subjectAltNames2 = getSubjectAltNames(x509Certificate, 2);
            int size2 = subjectAltNames2.size();
            int i2 = 0;
            boolean z2 = false;
            while (i2 < size2) {
                if (verifyHostname(toLowerCase, (String) subjectAltNames2.get(i2))) {
                    return true;
                }
                i2++;
                z2 = true;
            }
            if (!z2) {
                String findMostSpecific = new DistinguishedNameParser(x509Certificate.getSubjectX500Principal()).findMostSpecific("cn");
                if (findMostSpecific != null) {
                    return verifyHostname(toLowerCase, findMostSpecific);
                }
            }
            z = false;
            return z;
        } catch (SSLException e) {
            return false;
        }
    }

    public static List<String> allSubjectAltNames(X509Certificate certificate) {
        List<String> altIpaNames = getSubjectAltNames(certificate, 7);
        List<String> altDnsNames = getSubjectAltNames(certificate, 2);
        List<String> result = new ArrayList(altIpaNames.size() + altDnsNames.size());
        result.addAll(altIpaNames);
        result.addAll(altDnsNames);
        return result;
    }

    private static List<String> getSubjectAltNames(X509Certificate certificate, int type) {
        List<String> result = new ArrayList();
        try {
            Collection<?> subjectAltNames = certificate.getSubjectAlternativeNames();
            if (subjectAltNames == null) {
                return Collections.emptyList();
            }
            Iterator it = subjectAltNames.iterator();
            while (it.hasNext()) {
                List<?> entry = (List) it.next();
                if (entry != null && entry.size() >= 2) {
                    Integer altNameType = (Integer) entry.get(0);
                    if (altNameType != null && altNameType.intValue() == type) {
                        String altName = (String) entry.get(1);
                        if (altName != null) {
                            result.add(altName);
                        }
                    }
                }
            }
            return result;
        } catch (CertificateParsingException e) {
            return Collections.emptyList();
        }
    }

    private static boolean verifyHostname(String hostname, String pattern) {
        if (hostname == null || hostname.length() == 0 || hostname.startsWith(".") || hostname.endsWith("..") || pattern == null || pattern.length() == 0 || pattern.startsWith(".") || pattern.endsWith("..")) {
            return false;
        }
        if (!hostname.endsWith(".")) {
            hostname = hostname + '.';
        }
        if (!pattern.endsWith(".")) {
            pattern = pattern + '.';
        }
        pattern = pattern.toLowerCase(Locale.US);
        if (!pattern.contains("*")) {
            return hostname.equals(pattern);
        }
        if (!pattern.startsWith("*.") || pattern.indexOf(42, 1) != -1 || hostname.length() < pattern.length() || "*.".equals(pattern)) {
            return false;
        }
        String suffix = pattern.substring(1);
        if (!hostname.endsWith(suffix)) {
            return false;
        }
        int suffixStartIndexInHostname = hostname.length() - suffix.length();
        if (suffixStartIndexInHostname <= 0 || hostname.lastIndexOf(46, suffixStartIndexInHostname - 1) == -1) {
            return true;
        }
        return false;
    }
}
