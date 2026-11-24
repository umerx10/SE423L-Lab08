
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HospitalSystem {

    private static final Map<Integer, String> patientRecords =
            new ConcurrentHashMap<>();

    public static void addPatient(int id, String status) {
        patientRecords.putIfAbsent(id, status);
    }

    public static void updatePatient(int id, String status) {
        patientRecords.computeIfPresent(id,
                (key, oldStatus) -> status);
    }

    public static void viewPatient(int id) {
        String status = patientRecords.get(id);

        System.out.println(
                Thread.currentThread().getName()
                + " - Patient " + id
                + ": " + status
        );
    }

    public static void main(String[] args)
            throws InterruptedException {

        addPatient(101, "Admitted");

        Runnable updateTask = () -> {
            for (int i = 0; i < 1000; i++) {
                updatePatient(101, "Under Treatment " + i);
            }
        };

        Runnable readTask = () -> {
            for (int i = 0; i < 1000; i++) {
                viewPatient(101);
            }
        };

        Thread t1 = new Thread(updateTask, "Doctor-1");
        Thread t2 = new Thread(updateTask, "Doctor-2");
        Thread t3 = new Thread(readTask, "Receptionist");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Final Patient Status: "
                + patientRecords.get(101));
    }
}
