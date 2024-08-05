package com.idms.base.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idms.base.api.v1.model.dto.GroupDto;
import com.idms.base.api.v1.model.dto.VTSResponseDto;
import com.idms.base.dao.entity.PermitDetailsMaster;
import com.idms.base.support.persist.ResponseStatus;

public class AES_Encryption {

	static String plainText = "PB08CX6176";
	static String enc_key = "rb!nBwXv4C%Gr^84";
	static String iv = "33cdbc3872b37897";
	static String key = "60c961b99e6c8d614bab65897482e9b7d037a97b";

	public static String getKey() throws Exception {

		byte[] cipherText = encrypt(plainText.getBytes(), enc_key.getBytes(), iv.getBytes());

		System.out.println("Encrypted Text : " + Base64.getEncoder().encodeToString(cipherText));

		String decryptedText = decrypt(cipherText, enc_key.getBytes(), iv.getBytes());

		System.out.println("DeCrypted Text : " + decryptedText);

		return Base64.getEncoder().encodeToString(cipherText);

	}

	public static byte[] encrypt(byte[] plaintext, byte[] key, byte[] IV) throws Exception {
		// Get Cipher Instance
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// Create SecretKeySpec
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

		// Create IvParameterSpec
		IvParameterSpec ivSpec = new IvParameterSpec(IV);

		// Initialize Cipher for ENCRYPT_MODE
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

		// Perform Encryption
		byte[] cipherText = cipher.doFinal(plaintext);

		return cipherText;
	}

	public static String decrypt(byte[] cipherText, byte[] key, byte[] IV) throws Exception {
		// Get Cipher Instance
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// Create SecretKeySpec
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

		// Create IvParameterSpec
		IvParameterSpec ivSpec = new IvParameterSpec(IV);

		// Initialize Cipher for DECRYPT_MODE
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		// Perform Decryption
		byte[] decryptedText = cipher.doFinal(cipherText);

		return new String(decryptedText);
	}

	// public static void main(String args[]) {
	// try {
	// String encryptedkey = getKey();
	// System.out.println(key);
	// URL url = new
	// URL("https://punbus.itracking.in/api_sms/api_get_vts_kms.php?reg_no=" +
	// encryptedkey);
	// HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	// conn.setRequestMethod("GET");
	// conn.setRequestProperty("Accept", "application/json");
	// conn.setRequestProperty("key", key);
	// conn.setRequestProperty("user", "pb_User");
	// conn.setRequestProperty("pwd", "pb_uSer#@80");
	//
	// if (conn.getResponseCode() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : " +
	// conn.getResponseCode());
	// }
	//
	// BufferedReader br = new BufferedReader(new
	// InputStreamReader((conn.getInputStream())));
	//
	// String output = "";
	// String message = new String();
	// System.out.println("Output from Server .... \n");
	// while ((output = br.readLine()) != null) {
	// System.out.println(output);
	// message += output;
	// }
	//
	// VTSResponseDto vtsResponse = new ObjectMapper().readValue(message,
	// VTSResponseDto.class);
	// System.out.println(vtsResponse);
	// conn.disconnect();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void main(String args[]) {
		try {
			AES_Encryption obj = new AES_Encryption();
//			String response = obj.getToken("etmintegration", "admin@123");
//			String etmJSONString = "{\"wayBillNo\":\"100\"}";
//			StringBuffer response = obj.postETMData(etmJSONString, "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJqaGN6N2VSY1p5Wl9NYWFlY25iOUlzN1EwYVQwanAtS3FMSldnUWxLWTdZIn0.eyJleHAiOjE2NDAzNDcwMjUsImlhdCI6MTY0MDM0NTIyNSwianRpIjoiMDY0YTU4MTQtZTYwZC00NDViLWI4OWEtMmY0ZDE2YWM4Mzk0IiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo4MTgwL2F1dGgvcmVhbG1zL0lETVNQdW5qYWIiLCJhdWQiOlsicmVhbG0tbWFuYWdlbWVudCIsIkNBTVBVU0xBQiIsImFjY291bnQiXSwic3ViIjoiOTYwNzMzZWMtOTUyOS00MGJlLWJlZWYtMzM1Mjk2MTE0N2RmIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiSURNUzNJIiwic2Vzc2lvbl9zdGF0ZSI6ImZmODIzNTYyLThhZTUtNDg3OS1hYzY4LTU1NjMyMWFlNTAyNiIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InJlYWxtLW1hbmFnZW1lbnQiOnsicm9sZXMiOlsidmlldy1pZGVudGl0eS1wcm92aWRlcnMiLCJ2aWV3LXJlYWxtIiwibWFuYWdlLWlkZW50aXR5LXByb3ZpZGVycyIsImltcGVyc29uYXRpb24iLCJyZWFsbS1hZG1pbiIsImNyZWF0ZS1jbGllbnQiLCJtYW5hZ2UtdXNlcnMiLCJxdWVyeS1yZWFsbXMiLCJ2aWV3LWF1dGhvcml6YXRpb24iLCJxdWVyeS1jbGllbnRzIiwicXVlcnktdXNlcnMiLCJtYW5hZ2UtZXZlbnRzIiwibWFuYWdlLXJlYWxtIiwidmlldy1ldmVudHMiLCJ2aWV3LXVzZXJzIiwidmlldy1jbGllbnRzIiwibWFuYWdlLWF1dGhvcml6YXRpb24iLCJtYW5hZ2UtY2xpZW50cyIsInF1ZXJ5LWdyb3VwcyJdfSwiSURNUzNJIjp7InJvbGVzIjpbIlNVUEVSQURNSU4iXX0sIkNBTVBVU0xBQiI6eyJyb2xlcyI6WyJTVVBFUkFETUlOIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwicm9sZXMiOlsiU1VQRVJBRE1JTiJdLCJncm91cHMiOlsiMSIsIjIiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2EtcHVuYnVzIn0.Gzu0MVeF3WtjAj71BY8P3b-AI_DLAozlvJRel-mmOKzCV6ThNVg1rUWy1WrU6j7xo4DbpgsL2Ig6dFnjTRjOayQXTf3-UcYageZrDCWz9uC6jZLL9Q9MawPuaJBqByE6lkjICt3Y3r1DNi9pL8_lbV-c4ZuI5cVM4DhTBaJxesWy7GJZXtoluMbzrgTN-6jfIeBd0pOn6ZRLvF32sVfoKyUJFCafWRC87X1KdNUHTomvyY7uijwpjsJNcsdGF2BCDUV5De9-W17bS6CPdnhdwgUkV6J3EIdz6OEWkrehl0jzhK2oNToPsZdw3L6Fpo9awC8EM8BOOc0w8TQWRJ3TtQ");
//			System.out.println(response);
			Integer abc = 1234567890;
			
			System.out.println(abc.toString().substring(abc.toString().length()-5));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getToken(String username, String password) {
		String output = null;
		try {

			URL url = new URL(
					"http://punbus-frontdoor.azurefd.net/IDMS/api/v1/keycloak/getLogin?username="+username+"&password="+password);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.getOutputStream().close();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			output = br.readLine();
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}

	public StringBuffer postETMData(String etmJsonDataString, String tokenString) {
		StringBuffer response = new StringBuffer();
		ResponseEntity<ResponseStatus> res = null;
		try {
			URL url = new URL("http://localhost:8081/api/v1/Integration/ETM/submitETMData");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", "Bearer " + tokenString);
			conn.setRequestProperty("Content-Length", Integer.toString(etmJsonDataString.length()));
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = etmJsonDataString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String readLine = null;
				while ((readLine = in.readLine()) != null) {
					response.append(readLine);
				}
				in.close();
				System.out.println(response);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
