package com.idms.base.api.v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.idms.base.support.rest.RestConstants;

import lombok.extern.log4j.Log4j2;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = RestConstants.API_BASE + "/v1/sendSMS")
@Log4j2
public class SMSController {
	
	
	
	
	private static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	
	    {
	
	        MessageDigest md;
	
	        md = MessageDigest.getInstance("SHA-1");
	
	        byte[] md5 = new byte[64];
	
	        md.update(text.getBytes("iso-8859-1"), 0, text.length());
	
	        md5 = md.digest();
	
	        return convertedToHex(md5);
	
	    }
	
	private static String convertedToHex(byte[] data)

	{

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < data.length; i++)

		{

			int halfOfByte = (data[i] >>> 4) & 0x0F;

			int twoHalfBytes = 0;

			do

			{

				if ((0 <= halfOfByte) && (halfOfByte <= 9))

				{

					buf.append((char) ('0' + halfOfByte));

				}

				else

				{

					buf.append((char) ('a' + (halfOfByte - 10)));

				}

				halfOfByte = data[i] & 0x0F;

			} while (twoHalfBytes++ < 1);

		}

		return buf.toString();

	}
	 protected String hashGenerator(String userName, String senderId, String content, String secureKey) {
		 
		         // TODO Auto-generated method stub
		 
		         StringBuffer finalString=new StringBuffer();
		 
		         finalString.append(userName.trim()).append(senderId.trim()).append(content.trim()).append(secureKey.trim());
		 
		         //      logger.info("Parameters for SHA-512 : "+finalString);
		 
		         String hashGen=finalString.toString();
		 
		         StringBuffer sb = null;
		 
		         MessageDigest md;
		 
		         try {
		 
		             md = MessageDigest.getInstance("SHA-512");
		 
		             md.update(hashGen.getBytes());
		 
		             byte byteData[] = md.digest();
		 
		             //convert the byte to hex format method 1
		 
		             sb = new StringBuffer();
		 
		             for (int i = 0; i < byteData.length; i++) {
		 
		                 sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		 
		             }
		 
		  
		 
		         } catch (NoSuchAlgorithmException e) {
		 
		             // TODO Auto-generated catch block
		 
		             e.printStackTrace();
		 
		         }
		 
		         return sb.toString();
		 
		     }



	
	// NEW CODE
		@SuppressWarnings("unused")
		/**
		 * 
		 * @param mob
		 * @param msg
		 * @param contentid
		 * @return
		 * @throws Exception
		 */
		@GetMapping(path = "/pushSms")
		public String sendOTPUsingSMSNew(String mob, String msg, String contentid ) throws Exception {
			try {
                 String msg2 ="Dear Customer, mTicket No:{#var#}, PNR:{#var#}, {#var#}, {#var#}, {#var#}, Seat:{#var#}, Name:{#var#}, {#var#}, {#var#} Book online on Punbusonline.com/Travelyaari.com Helpline:080-47107878/0172 4482001 Regards Punbus";
                 String response= this.sendSingleSMS("dogrpunjab-transport", "transport@pb2", msg2, "PBGOVT", "8700571442", "b8f5e147-ca52-456a-951f-cd3838bd5a01", "1407163755728185547");
				String genratedhashKey = hashGenerator("dogrpunjab-transport", "PBGOVT", msg2, "b8f5e147-ca52-456a-951f-cd3838bd5a01");

				String msg1 = "Dear Driver/Conductor Code you are absent on ,"+new Date() + 
						      "Regards IDMS";
				
				
					int i= 0;
					StringBuilder data = new StringBuilder("mobileno=").append(URLEncoder.encode("8700571442", "ISO-8859-1"));
					data.append("&senderid=");
					data.append(URLEncoder.encode("PBGOVT", "ISO-8859-1"));
					data.append("&content=");
					data.append(URLEncoder.encode(msg2 ,"ISO-8859-1"));
					data.append("&smsservicetype=");
					data.append(URLEncoder.encode("singlemsg", "ISO-8859-1"));
					data.append("&username=");
					data.append(URLEncoder.encode("dogrpunjab-transport", "ISO-8859-1"));
					data.append("&password=");
					data.append(URLEncoder.encode("transport@pb2", "ISO-8859-1"));
					data.append("&key=");
					data.append(URLEncoder.encode(genratedhashKey, "ISO-8859-1"));
					data.append("&templateid=");
					data.append(URLEncoder.encode("1407163755728185547", "ISO-8859-1"));
					URL url = new URL("https://msdgweb.mgov.gov.in/esms/sendsmsrequestDLT");
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(data.toString());
					wr.flush();
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					
					while ((line = rd.readLine()) != null) {
						 System.out.println(line);
						 i=1;
					}
					if (i == 1) {
			            return "send";
					}
					wr.close();
					rd.close();
			} catch (Exception e) {
				log.error("Error in sendOTPUsingSMSNew", e);
				e.printStackTrace();
			
			}
			return null;

			}
		
		
		
		// OLD CODE 
		
		@SuppressWarnings("unused")
		@GetMapping(value="/sendSMS")
		public String sendOTPUsingSMS(String mob, String msg) throws Exception {
			try {
				
				Timestamp ts1 = Timestamp.valueOf("2018-09-01 09:01:15"); 
				String str=ts1.toString();  
					int i= 0;
					StringBuilder data = new StringBuilder("mobileNumber=").append(URLEncoder.encode("9814941428", "ISO-8859-1"));
					data.append("&timeStamp=");
					data.append(URLEncoder.encode(str,"ISO-8859-1"));
					data.append("&operatorName=");
					data.append(URLEncoder.encode("BSNL", "ISO-8859-1"));
					data.append("&areaCode=");
					data.append(URLEncoder.encode("0172" ,"ISO-8859-1"));
					data.append("&message=");
					data.append(URLEncoder.encode("DDAAPR", "ISO-8859-1"));
					data.append("&sc=");
					data.append(URLEncoder.encode("", "ISO-8859-1"));
					data.append("&contentid=");
					URL url = new URL("https://msdgweb.mgov.gov.in/esms/sendsmsrequestDLT");
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(data.toString());
					wr.flush();
					// Get the response
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					while ((line = rd.readLine()) != null) {
						 //Print the response output...
						 System.out.println(line);
						 i=1;
					}
					if (i == 1) {
			            return "send";
					}
					wr.close();
					rd.close();
			}  catch (Exception e) {
					log.error("Error in sendOTPUsingSMS", e);
				   e.printStackTrace();
			}
			return null;

		}
		
		
		
		
		/**
		 * Send Single text SMS
		 * @param username : Department Login User Name
		 * @param password : Department Login Password
		 * @param message  : Message e.g. 'Welcome to mobile Seva'
		 * @param senderId	: Department allocated SenderID
		 * @param mobileNumber : Single Mobile Number e.g. '99XXXXXXX' 
		 * @param secureKey :  Department key generated by login to services portal
		
		 * @param templateid :  templateId unique for each template message content
		 * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID = 150620161466003974245msdgsms' 
		 * @see <a href="https://mgov.gov.in/msdp_sms_push.jsp">Return types code details</a>
		 * 
		 */
		
		public String sendSingleSMS(String username, String password , String message , String senderId, String mobileNumber, String secureKey, String templateid){
			
			//mst_system_settings
			//sms_templates
			String responseString = "";
			String status="";
			SSLSocketFactory sf=null;
			SSLContext context=null;
			String encryptedPassword;
			try {
				//context=SSLContext.getInstance("TLSv1.1"); // Use this line for Java version 6
				context=SSLContext.getInstance("TLSv1.2"); // Use this line for Java version 7 and above
				context.init(null, null, null);
				sf=new SSLSocketFactory(context, SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
				Scheme scheme=new Scheme("https",443,(SchemeSocketFactory) sf);
				HttpClient client=new DefaultHttpClient();
				client.getConnectionManager().getSchemeRegistry().register(scheme);
				HttpPost post=new HttpPost(RestConstants.CDAC_API_PUSH);
				encryptedPassword  = MD5(password);
				String genratedhashKey = hashGenerator(username, senderId, message, secureKey);
				List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("mobileno", mobileNumber));
				nameValuePairs.add(new BasicNameValuePair("senderid", senderId));
				nameValuePairs.add(new BasicNameValuePair("content", message));
				nameValuePairs.add(new BasicNameValuePair("smsservicetype", "singlemsg"));
				nameValuePairs.add(new BasicNameValuePair("username", username));
				nameValuePairs.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("key", genratedhashKey));
	                        nameValuePairs.add(new BasicNameValuePair("templateid", templateid));
				post.setEntity(new UrlEncodedFormEntity((List<? extends NameValuePair>) nameValuePairs));
				HttpResponse response=client.execute(post);
				BufferedReader bf=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				String line="";
				while((line=bf.readLine())!=null){
					responseString = responseString+line;
					status="sent";
					
				}
				System.out.println(responseString);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return status;
		}
	
		
		
		public static void main(String[] args) {
			try {
                String time = new Timestamp(new Date().getTime()).toString();
				
                String hashh = hashGeneratorForPull("Hello I am Piyush", "8700571442", time, "b8f5e147-ca52-456a-951f-cd3838bd5a01");
					int i= 0;
					StringBuilder data = new StringBuilder("mobileNumber=").append(URLEncoder.encode("8700571442", "ISO-8859-1"));
					data.append("&timeStamp=");
					data.append(URLEncoder.encode(time, "ISO-8859-1"));
					data.append("&operatorName=");
					data.append(URLEncoder.encode("BSNL" ,"ISO-8859-1"));
					data.append("&areaCode=");
					data.append(URLEncoder.encode("0712", "ISO-8859-1"));
					data.append("&hash=");
					data.append(URLEncoder.encode(hashh, "ISO-8859-1"));
					data.append("&message=");
					data.append(URLEncoder.encode("Hello I am Piyush", "ISO-8859-1"));
					URL url = new URL("http://PBGOVT.gov.in/sms");
					URLConnection conn = url.openConnection();
					conn.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(data.toString());
					wr.flush();
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					
					while ((line = rd.readLine()) != null) {
						 System.out.println(line);
						 i=1;
					}
					if (i == 1) {
			            System.out.println("hell");
					}
					wr.close();
					rd.close();
			} catch (Exception e) {
				log.error("Error in sendOTPUsingSMSNew", e);
				e.printStackTrace();
			
			}
		}
		public String pull(String mob, String msg, String contentid ) throws Exception {
			
			return null;

			}
		
		
		
		
		protected static String hashGeneratorForPull(String message, String mob, String time, String secureKey) {
			 
	         // TODO Auto-generated method stub
	 
	         StringBuffer finalString=new StringBuffer();
	 
	         finalString.append(message.trim()).append(mob.trim()).append(time.trim()).append(secureKey.trim());
	 
	         //      logger.info("Parameters for SHA-512 : "+finalString);
	 
	         String hashGen=finalString.toString();
	 
	         StringBuffer sb = null;
	 
	         MessageDigest md;
	 
	         try {
	 
	             md = MessageDigest.getInstance("SHA-512");
	 
	             md.update(hashGen.getBytes());
	 
	             byte byteData[] = md.digest();
	 
	             //convert the byte to hex format method 1
	 
	             sb = new StringBuffer();
	 
	             for (int i = 0; i < byteData.length; i++) {
	 
	                 sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	 
	             }
	 
	  
	 
	         } catch (NoSuchAlgorithmException e) {
	 
	             // TODO Auto-generated catch block
	 
	             e.printStackTrace();
	 
	         }
	 
	         return sb.toString();
	 
	     }
		
		
		
		
		
		
		
}
