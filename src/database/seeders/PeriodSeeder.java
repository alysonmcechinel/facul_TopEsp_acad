package database.seeders;

import database.models.period.Period;
import database.service.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeriodSeeder implements Seeder {
    @Override
    public void run() {
        Service<Period> periodService = new Service<Period>(Period.class);
        Long count = periodService.count();

        if (count > 0) {
            return;
        }

        List<List<Object>> periodsList = new ArrayList<>();
        periodsList.add(Arrays.asList("Manhã", LocalTime.of(8, 0), LocalTime.of(12, 0)));
        periodsList.add(Arrays.asList("Tarde", LocalTime.of(14, 0), LocalTime.of(18, 0)));
        periodsList.add(Arrays.asList("Noite", LocalTime.of(18, 0), LocalTime.of(22, 0)));

        System.out.println("[Seeder] PeriodSeeder running.");
        try {
            for (List<Object> objects : periodsList) {
                Period period = new Period();
                period.setDescription((String) objects.get(0));
                period.setInitTime((LocalTime) objects.get(1));
                period.setEndTime((LocalTime) objects.get(2));
                period.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[Seeder] PeriodSeeder error: " + e.getMessage());
        }

        System.out.println("[Seeder] PeriodSeeder runned.");
    }
}
