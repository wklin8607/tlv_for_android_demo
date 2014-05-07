/*
 * @author soloicesky
 * @fileName OperatorDBHelper.java
 * @date 2013-10-11 ����14:01:44
 * @description 
 */

package com.soloicesky.tlvutils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TLVUtils {
	/**
	 * 
	 * @param tlvArray
	 *            arrayList of TLV objects
	 * @return
	 */
	public static byte[] buildTLVString(ArrayList<TLV> tlvArray) {
		byte[] desMsg = new byte[1024];
		int desMsgLen = 0;

		for (TLV tlv : tlvArray) {
			if (tlv.getLength() <= 0) {
				continue;
			}

			if ((tlv.getTag() & 0xFF00) > 0) {
				desMsg[desMsgLen++] = (byte) (tlv.getTag() >> 8);
			}

			desMsg[desMsgLen++] = (byte) (tlv.getTag() & 0x00FF);

			if (tlv.getLength() > 127) {
				desMsg[desMsgLen++] = (byte) 0x81;

			}

			desMsg[desMsgLen++] = (byte) (tlv.getTag() & 0x00EF);

			System.arraycopy(tlv.getValue(), 0, desMsg, desMsgLen,
					tlv.getLength());
			desMsgLen += tlv.getLength();
		}

		return desMsg;
	}

	/**
	 * 
	 * @param src
	 * @param srcLen
	 * @param decodeConstructedData
	 * @return
	 */
	public static List<TLV> parseTLVString(byte[] src, int srcLen){
		if(src == null || srcLen <= 0 || srcLen > src.length){
			return null;
		}
		int start = 0;
		for(int i=0; i < srcLen; i++){
			start++;
			if(src[i] == 0x00 || (src[i] & 0xFF) == 0xFF){
				continue;
			}else{
				break;
			}
		}
		if (start >= srcLen) {
			return null;
		}

		short tag;
		int len;
		byte[] value = new byte[1024];
		int lenBytes;
		List<TLV> items = new ArrayList<TLV>();

		for (int i = start; i < srcLen;) {
			// parse tag
			TLV tlv = new TLV();
			tag = 0;

			if ((src[i] & 0x1F) == 0x1F) {
				tag = (short) (src[i++] & 0xFF);
				tag = (short) (tag << 8);
			}

			tag += (short) (src[i++] & 0xFF);

			// parse length
			len = 0;

			if ((src[i] & 0x80) == 0x80) {
				lenBytes = (src[i] & 0x7F);

				for (int j = 0; j < lenBytes; j++) {
					len = len << 8 + src[++i];
				}
			} else {
				len = src[i++];
			}

			if (len > (srcLen - i)) {
				return null;
			}

			System.arraycopy(src, i, value, 0, len);
			i += len;
			tlv.setTag(tag);
			tlv.setLength(len);
			tlv.setValue(value);
			items.add(tlv);
			//savetlvobj.saveTLVOBJ(tag, len, value);
		}
		return items;
	}
	/**
	 * 
	 * @param src
	 *            tlv��ʽ��Դ��
	 * @param srcLen
	 *            ���ĳ���
	 * @param decodeConstructedData
	 *            �Ƿ����constructed��tlv����,true - ����, false-������
	 * @param savetlvobj
	 *            ��������õ�tlvԪ�صĽӿ�
	 * @return 0 �ɹ��� ��0ʧ��
	 */
	public static int parseTLVString(byte[] src, int srcLen,
			boolean decodeConstructedData, SaveTLVOBJCallback savetlvobj) {
		if (src == null || srcLen <= 0 || srcLen > src.length) {
			return -1;
		}

		int start = 0;

		for (int i = 0; i < srcLen; i++) {
			start++;
			if (src[i] == 0x00 || (src[i] & 0xFF) == 0xFF) {
				continue;
			} else {
				break;
			}
		}

		if (start >= srcLen) {
			return 0;
		}

		short tag;
		int len;
		byte[] value = new byte[1024];
		int lenBytes;

		for (int i = start; i < srcLen;) {
			// parse tag
			tag = 0;

			if ((src[i] & 0x1F) == 0x1F) {
				tag = (short) (src[i++] & 0xFF);
				tag = (short) (tag << 8);
			}

			tag += (short) (src[i++] & 0xFF);

			// parse length
			len = 0;

			if ((src[i] & 0x80) == 0x80) {
				lenBytes = (src[i] & 0x7F);

				for (int j = 0; j < lenBytes; j++) {
					len = len << 8 + src[++i];
				}
			} else {
				len = src[i++];
			}

			if (len > (srcLen - i)) {
				return -2;
			}

			System.arraycopy(src, i, value, 0, len);
			i += len;
			savetlvobj.saveTLVOBJ(tag, len, value);
		}

		return 0;
	}
}
