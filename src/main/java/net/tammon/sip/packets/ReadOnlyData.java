/*
 * Sercos Internet Protocol (SIP) version 1 - Java Implementation
 * Copyright (c) 2017. by tammon (Tammo Schwindt)
 * This code is licensed under the GNU LGPLv2.1
 */

package net.tammon.sip.packets;

import net.tammon.sip.packets.parts.Body;
import net.tammon.sip.packets.parts.ReadOnlyDataBody;

public class ReadOnlyData extends AbstractRequestPacket {

    public ReadOnlyData(int transactionId, short slaveIndex, int slaveExtension, String idn) throws IllegalArgumentException {
        super(transactionId, new ReadOnlyDataBody(slaveIndex, slaveExtension, idn));
    }

    @Override
    public Body getPacketBody() {
        return this.body;
    }
}
