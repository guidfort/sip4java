/*
 * Sercos Internet Protocol (SIP) version 1
 * Copyright (C) 2017. tammon (Tammo Schwindt)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.tammon.sip.packets.parts;

import java.io.DataInput;

public final class ExceptionBody extends AbstractBody implements ResponseBody {
    private final static int messageType = 67;
    private short rawCommonErrorCode;
    private int specificErrorCode;
    private CommonErrorCodes commonErrorCode;
    public ExceptionBody(byte[] rawBodyDataArrays) throws Exception {
        DataInput data = DataStreamFactory.getLittleEndianDataInputStream(rawBodyDataArrays);
        this.rawCommonErrorCode = data.readShort();
        this.specificErrorCode = data.readInt();
        this.commonErrorCode = CommonErrorCodes.values()[this.rawCommonErrorCode-1];
    }

    public static int getMessageType() {
        return messageType;
    }

    public CommonErrorCodes getCommonErrorCode() {
        return commonErrorCode;
    }

    public int getSpecificErrorCode() {
        return specificErrorCode;
    }
}
