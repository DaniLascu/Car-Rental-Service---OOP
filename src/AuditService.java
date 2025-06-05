import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instance;
    private static final String FILE_NAME = "audit.csv";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void logAction(String actionName) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.append(actionName).append(",").append(timestamp).append("\n");
        } catch (IOException e) {
            System.out.println("Error writing to audit file: " + e.getMessage());
        }
    }
}
