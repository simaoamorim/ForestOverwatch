import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

class GUI {
    private Settings settings;
    private Properties localProperties = new Properties();

    GUI() throws IOException {
        LoadProperties();
        localProperties.list(System.out);
        settings = new Settings();
    }

    private void LoadProperties() throws IOException {
        FileInputStream defaultPreferencesFile = new FileInputStream( "./src/default.cfg");
        localProperties.load(defaultPreferencesFile);
        defaultPreferencesFile.close();

        try {
            FileInputStream userPreferencesFile = new FileInputStream("config.cfg");
            localProperties.load(userPreferencesFile);
            userPreferencesFile.close();
        } catch (IOException e) {
            System.err.println("No user config file found");
            System.err.println(e.getMessage());
        }
    }

}
