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

package net.tammon.sip.packets;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SipByteUtils {

    public static byte[] getByteArray(short... inputShorts){
        byte[] outputArray = new byte[0];
        List<Byte> outputList = new ArrayList<>();
        for (short inputShort : inputShorts) {
            byte[] inputByteArray = SipByteUtils.getSipByteBuffer(2).putShort(inputShort).array();
            outputArray = SipByteUtils.concatenate(outputArray, inputByteArray);
        }
        return outputArray;
    }

    public static byte[] getByteArray(byte... inputBytes){
        byte[] outputArray = new byte[0];
        List<Byte> outputList = new ArrayList<>();
        for (byte inputByte : inputBytes) {
            byte[] inputByteArray = SipByteUtils.getSipByteBuffer(1).put(inputByte).array();
            outputArray = SipByteUtils.concatenate(outputArray, inputByteArray);
        }
        return outputArray;
    }

    public static byte[] getByteArray(int... inputIntegers){
        byte[] outputArray = new byte[0];
        List<Byte> outputList = new ArrayList<>();
        for (int inputInteger : inputIntegers) {
            byte[] inputByteArray = SipByteUtils.getSipByteBuffer(4).putInt(inputInteger).array();
            outputArray = SipByteUtils.concatenate(outputArray, inputByteArray);
        }
        return outputArray;
    }

    public static byte[] getByteArray(long... inputLongs){
        byte[] outputArray = new byte[0];
        List<Byte> outputList = new ArrayList<>();
        for (long inputLong : inputLongs) {
            byte[] inputByteArray = SipByteUtils.getSipByteBuffer(8).putLong(inputLong).array();
            outputArray = SipByteUtils.concatenate(outputArray, inputByteArray);
        }
        return outputArray;
    }

    public static byte[] concatenate(byte[]... inputByteArrays){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            for (byte[] inputByteArray : inputByteArrays) {
                outputStream.write(inputByteArray);
            }
            return outputStream.toByteArray();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concatenate(byte... inputByteArrays){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try{
            for (byte inputByteArray : inputByteArrays) {
                outputStream.write(inputByteArray);
            }
            return outputStream.toByteArray();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static ByteBuffer getSipByteBuffer (int allocation){
        ByteBuffer byteBuffer = ByteBuffer.allocate(allocation);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        return byteBuffer;
    }

    public static short getSipPrimitive(short tcpShort){
        return Short.reverseBytes(tcpShort);
    }

    public static int getSipPrimitive(int tcpInt){
        return Integer.reverseBytes(tcpInt);
    }

    public static long getSipPrimitive(long tcpLong){
        return Long.reverseBytes(tcpLong);
    }

    public static DataInputStream getDataInputStreamOfRawData (byte[]... rawInputBytes){
        byte[] rawInputByteArray = SipByteUtils.concatenate(rawInputBytes);
        return new DataInputStream(new ByteArrayInputStream(rawInputByteArray));
    }

    //todo: check, buggy!
    public static byte[] getIdnAsByteArray(String idn) throws IllegalArgumentException {
        //todo: maybe some optimization without duplicate code
        if (idn.matches("^[SP]-\\d-\\d\\d\\d\\d[.]\\d[.]\\d$")){
            //decode byte[4]
            byte byte1 = Byte.parseByte(idn.substring(9,10));
            byte byte2 = Byte.parseByte(idn.substring(11));
            byte byte3 = (byte)(Byte.parseByte(idn.substring(2,3)) | ((idn.charAt(0) == 'P') ? (0x01 << 3) : 0x00));
            byte[] intermediateByte4 = SipByteUtils.getByteArray(Short.parseShort(idn.substring(4,8)));
            byte[] byte4 = Arrays.copyOfRange(intermediateByte4, 0, 3);
            return SipByteUtils.concatenate(byte4,
                    SipByteUtils.concatenate(byte3, byte2, byte1));
        } else if (idn.matches("^[SP]-\\d-\\d\\d\\d\\d$")){
            //decode byte[2]
            byte byte3 = (byte)((Byte.parseByte(idn.substring(2,3)) | ((idn.charAt(0) == 'P') ? (0x01 << 3) : 0x00)) << 4);
            byte[] parameterNo = SipByteUtils.getByteArray(Short.parseShort(idn.substring(4,8)));
            byte3 = (byte) (byte3 | parameterNo[1]);
            byte byte4 = parameterNo[0];
            return SipByteUtils.concatenate(byte4, byte3, (byte) 0, (byte) 0);
        } else throw new IllegalArgumentException("The specified idn is not a valid drive Parameter");
    }
}