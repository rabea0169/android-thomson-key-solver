package org.exobel.routerkeygen;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;


/*
 * This is the algorithm to generate the WPA passphrase 
 * for the SKYv1.
 * Generate the md5 hash form the mac.
 * Use the numbers in the following positions on the hash.
 *  Position 3,7,11,15,19,23,27,31 ,
 *  Use theses numbers, modulus 26, to find the correct letter
 *  and append to the key.
 */
public class SkyV1Keygen extends KeygenThread{

	public SkyV1Keygen(Handler h, Resources res) {
		super(h, res);
	}

	final static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public void run(){
		if ( router == null)
			return;
		if ( router.getMac().length() != 12 ) 
		{
			handler.sendMessage(Message.obtain(handler, ERROR_MSG , 
					resources.getString(R.string.msg_nomac)));
			return;
		}
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			handler.sendMessage(Message.obtain(handler, ERROR_MSG , 
					resources.getString(R.string.msg_nomd5)));
			return;
		}
		md.reset();
		md.update(router.getMac().getBytes());
		byte [] hash = md.digest();
		String key ="";
		int index = hash[1] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[3] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[5] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[7] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[9] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[11] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[13] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		index = hash[15] & 0xFF;
		index %= 26;
		key += ALPHABET.substring(index,index+1 );
		
		pwList.add(key);
		handler.sendEmptyMessage(RESULTS_READY);
		return;
	}
}
