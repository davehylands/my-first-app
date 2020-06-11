package com.example.customlib;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FlatBufferBuilderViolator;
import com.fullstory.instrumentation.protocol.SessionRequestProto;

import java.nio.ByteBuffer;

import android.annotation.NonNull;

public class MyClass {
    public String sayHello() {
        return "Hello world!";
    }

    public static String toHexString(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }

    private static int createSessionRequestProto(FlatBufferBuilder fb) {
        SessionRequestProto.startSessionRequestProto(fb);
        SessionRequestProto.addVersion(fb, 0xDEADBEEF);
        return SessionRequestProto.endSessionRequestProto(fb);
    }

    public void DoIt() {
        android.util.Log.v("fullstory", "DoIt called");
        FlatBufferBuilder fb = new FlatBufferBuilder();
        int req2 = createSessionRequestProto(fb);
        fb.finish(req2);

        ByteBuffer bb = FlatBufferBuilderViolator.dataBuffer(fb).slice();
        int remaining = bb.remaining();
        byte[] bytes = new byte[remaining];
        bb.get(bytes, 0, remaining);

        String hexData = MyClass.toHexString(bytes);
        android.util.Log.v("fullstory", "===== startup request " + remaining + " bytes len = " + bytes.length + " hexData len =" + hexData.length() + " =====");
        for ( int i = 0; i < hexData.length(); i += 128) {
            if (i + 128 > hexData.length()) {
                android.util.Log.v("fullstory", hexData.substring(i));
            } else {
                android.util.Log.v("fullstory", hexData.substring(i, i+128));
            }
        }
        android.util.Log.v("fullstory", "======================");
    }
}

