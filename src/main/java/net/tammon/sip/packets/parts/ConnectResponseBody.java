/*
 * Sercos Internet Protocol (SIP) version 1
 * Copyright (c) 2017. tammon (Tammo Schwindt)
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.tammon.sip.packets.parts;

import java.io.DataInput;
import java.io.IOException;

public final class ConnectResponseBody extends AbstractBody implements ResponseBody {

    private static final int messageType = 64;
    private int sipVersion, busyTimeout, leaseTimeout, noSupportedMessageTypes;
    private int[] supportedMessageTypes;

    public ConnectResponseBody(int sipVersion, int busyTimeout, int leaseTimeout, int noSupportedMessageTypes, int[] supportedMessageTypes) {
        this.sipVersion = sipVersion;
        this.busyTimeout = busyTimeout;
        this.leaseTimeout = leaseTimeout;
        this.noSupportedMessageTypes = noSupportedMessageTypes;
        this.supportedMessageTypes = supportedMessageTypes;
    }

    public ConnectResponseBody(byte[] rawBodyData) throws IllegalArgumentException, IOException {

        DataInput data = DataStreamFactory.getLittleEndianDataInputStream(rawBodyData);

        this.sipVersion = data.readInt();
        this.busyTimeout = data.readInt();
        this.leaseTimeout = data.readInt();
        this.noSupportedMessageTypes = data.readInt();
        this.supportedMessageTypes = new int[noSupportedMessageTypes];
        for (int i = 0; i < noSupportedMessageTypes; i++){
            try {
                this.supportedMessageTypes[i] = data.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getMessageType(){
        return messageType;
    }

    public int getSipVersion() {
        return sipVersion;
    }

    public int getBusyTimeout() {
        return busyTimeout;
    }

    public int getLeaseTimeout() {
        return leaseTimeout;
    }

    public int getNoSupportedMessageTypes() {
        return noSupportedMessageTypes;
    }

    public int[] getSupportedMessageTypes() {
        return supportedMessageTypes;
    }
}
