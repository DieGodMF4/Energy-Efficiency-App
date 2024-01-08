import java.time.*;

public class Tests {
    public static void main(String[] args) {
        Instant instant = obtainInstantMidNight(LocalDate.now().plusDays(0));
        Instant instant1 = obtainInstantMidNight(LocalDate.now().plusDays(1));
        System.out.println(instant);
        System.out.println(instant1);

    }

    private static Instant obtainInstantMidNight(LocalDate date) {
        LocalDateTime midnight = LocalDateTime.of(date, LocalTime.MIDNIGHT);
        return midnight.atZone(ZoneId.of("UTC")).toInstant();
    }
}
