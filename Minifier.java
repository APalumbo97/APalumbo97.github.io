import java.net.URL;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.lang.StringBuilder;
import java.io.File;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Class used to automated CSS and JS minfication for my personal website.
 * @author Anthony Palumbo
 */
public class Minifier {

    private enum FileType {CSS, JS}
    private static URL cssUrl;
    private static URL jsUrl;

    /**
     * Initializes the urls for CSS and JS minification.
     * @throws MalformedURLException when the Url format is incorrect
     */
    private static void setupUrls() throws MalformedURLException {
        cssUrl = new URL("https://cssminifier.com/raw");
        jsUrl = new URL("https://javascript-minifier.com/raw");
    }

    /**
     * Opens each file in the input directory, minifies it, and writes the min file to the output directory.
     * @param inputDir the directory where input files are stored
     * @param outputDir the directory where output files should be written to
     * @param type the file type of the input files
     * @throws IOException when something with file reading or communicating with the web service has an issue
     * @throws Exception when the response code from the web service is bad, or something unforseen goes wrong
     */
    private static void minifyFiles(File inputDir, File outputDir, FileType type) throws IOException, Exception {
        File[] files = inputDir.listFiles();

        for (File f : files) {
            byte[] bytes = Files.readAllBytes(f.toPath());

            // encodes the data in the file into a byte array
            final StringBuilder data = new StringBuilder();
            data.append(URLEncoder.encode("input", "UTF-8"));
            data.append('=');
            data.append(URLEncoder.encode(new String(bytes), "UTF-8"));
            bytes = data.toString().getBytes("UTF-8");

            // opens the appropriate connection based on the file type
            HttpURLConnection conn = null;
            if (type == FileType.CSS) {
                conn = (HttpURLConnection) cssUrl.openConnection();
            } else if (type == FileType.JS) {
                conn = (HttpURLConnection) jsUrl.openConnection();
            }

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(bytes.length));

            // writes the encoded file data to the output stream
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(bytes);

            final int code = conn.getResponseCode();

            if (code == 200) {
                final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                PrintWriter minFile = null;

                if (type == FileType.CSS) {
                    minFile = new PrintWriter(Paths.get(outputDir.getPath(),
                                                        f.getName()).toString().replaceAll(".css", ".min.css"));
                } else if (type == FileType.JS){
                    minFile = new PrintWriter(Paths.get(outputDir.getPath(),
                                                        f.getName()).toString().replaceAll(".js", ".min.js"));
                }

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    minFile.print(inputLine);
                }

                minFile.close();
                in.close();
            } else {
                throw new Exception("Bad response code,\nStatus: " + code);
            }
        }
    }

    /**
     * Minifies all CSS files and JS files in the specified directories and writes them to the output directory.
     * @param args not used
     */
    public static void main(String[] args) {
        try {
            setupUrls();

            File cssInput = new File("styles");
            File jsInput = new File("scripts");
            File output = new File("minified");

            minifyFiles(cssInput, output, FileType.CSS);
            minifyFiles(jsInput, output, FileType.JS);
        } catch (MalformedURLException m) {
            System.err.println("An incorrect URL was given for the CSS/JS web service:");
            m.printStackTrace();
        } catch (IOException i) {
            System.err.println("An IO Exception occurred:");
            i.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
