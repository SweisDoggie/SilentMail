package com.autentia.plugin.sendmail;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendMail extends CordovaPlugin {

	public static final String ACTION_SEND = "send";

	public boolean execute(
		String action,
		JSONArray jsonArgs,
		final CallbackContext callbackContext) throws JSONException {

		if (ACTION_SEND.equals(action)) {
			// Get the json arguments as final for thread usage.
			final JSONObject args = jsonArgs.getJSONObject(0);

			// Run in a thread to not block the webcore thread.
			cordova.getThreadPool().execute(new Runnable() {
				// Thread method.
				public void run() {
					// Try to send the the mail.
					try {
						// Get the arguments.
						String subject = args.getString("subject");
						String body = args.getString("body");
						String sender = args.getString("sender");
						String password = args.getString("password");
						String recipients = args.getString("recipients");
                                                
                                                
                                                	String[] list = null;
                                                        if (args.has("attachFiles")) {
                                                                JSONArray json;
                                                                try {
                                                                        json = args.getJSONArray("attachFiles");
                                                                        int lenFeatures = json.length();
                                                                        list = new String[lenFeatures];
                                                                        for (int j = 0; j < lenFeatures; j++) {
                                                                                String f = json.getString(j);
                                                                                list[j] = f;
                                                                        }
                                                                } catch (JSONException e) {
                                                                      e.printStackTrace();
                                                                }

                                                        }
                                                        
                                                 String attachFiles[] = list;
                                                
                                                
						//String attachFiles[] = args.getJSONArray("attachFiles");
						

						// Create the sender
						GMailSender gmailSender = new GMailSender(sender, password);

						// Send the mail.
						gmailSender.sendMail(subject, body, sender, recipients, attachFiles);

						// Thread safe callback.
						callbackContext.success();
					} catch (Exception e) {
						// Catch error.
						callbackContext.error(e.getMessage());
						callbackContext.error(e.toString());
					}
				}
			});

			return true;
		}

		return false;
	}
}
