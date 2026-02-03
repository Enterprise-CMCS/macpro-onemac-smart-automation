package gov.cms.smart.utils;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailOtpFetcher {

    /**
     * Fetches OTP from Salesforce Sandbox email, waits if necessary.
     *
     * @param host                IMAP host
     * @param email               Email address
     * @param password            Email password
     * @param timeoutSeconds      Maximum time to wait for OTP
     * @param pollIntervalSeconds Interval between inbox checks
     * @return OTP as String or null if timeout
     * @throws Exception
     */
    public static String fetchOtp(String host, String email, String password,
                                  int timeoutSeconds, int pollIntervalSeconds) throws Exception {

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.ssl.enable", "true");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        store.connect(host, email, password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_WRITE);

        long startTime = System.currentTimeMillis();
        long timeoutMillis = timeoutSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeoutMillis) {

            Message[] messages = inbox.getMessages();

            for (int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];

                String subject = message.getSubject();
                if (subject == null) continue;

                if (subject.toLowerCase().contains("sandbox")) {
                    String content = extractText(message);

                    Pattern pattern = Pattern.compile("Verification Code:\\s*(\\d{6})");
                    Matcher matcher = pattern.matcher(content);

                    if (matcher.find()) {
                        String otp = matcher.group(1);
                        // Delete the OTP email after reading
                        message.setFlag(Flags.Flag.DELETED, true);
                        inbox.close(true); // expunge deleted messages
                        store.close();

                        return otp;
                    }
                }
            }

            // Wait before polling again
            Thread.sleep(pollIntervalSeconds * 1000L);

            // Refresh inbox to get newly arrived emails
            inbox.close(false);
            inbox.open(Folder.READ_WRITE);
        }

        // Timeout reached
        inbox.close(false);
        store.close();
        return null;
    }

    /**
     * Recursively extracts text from plain, HTML, and multipart emails
     */
    private static String extractText(Part part) throws Exception {
        if (part.isMimeType("text/plain")) {
            return part.getContent().toString();
        }

        if (part.isMimeType("text/html")) {
            String html = part.getContent().toString();
            return html.replaceAll("<[^>]+>", " ");
        }

        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < multipart.getCount(); i++) {
                result.append(extractText(multipart.getBodyPart(i)));
            }
            return result.toString();
        }

        return "";
    }
}
