package com.example.demo.scheduler;

import com.example.demo.model.User;
import com.example.demo.model.Habit;
import com.example.demo.model.Journal;
import com.example.demo.model.CheckIn;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.HabitRepository;
import com.example.demo.repository.JournalRepository;
import com.example.demo.repository.CheckInRepository;
import com.example.demo.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.List;

@Component
public class ReminderScheduler {

    private final UserRepository userRepo;
    private final HabitRepository habitRepo;
    private final JournalRepository journalRepo;
    private final CheckInRepository checkInRepo;
    private final NotificationService notifier;

    public ReminderScheduler(
            UserRepository userRepo,
            HabitRepository habitRepo,
            JournalRepository journalRepo,
            CheckInRepository checkInRepo,
            NotificationService notifier
    ) {
        this.userRepo = userRepo;
        this.habitRepo = habitRepo;
        this.journalRepo = journalRepo;
        this.checkInRepo = checkInRepo;
        this.notifier = notifier;
    }


    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminders() {

        LocalDate today = LocalDate.now();

        List<User> users = userRepo.findAll();
        for (User user : users) {
            List<Habit> habits = habitRepo.findByUserOrderByCreatedAtDesc(user);

            for (Habit habit : habits) {
                if (habit.getFrequency().equalsIgnoreCase("DAILY")) {
                    if (habit.getLastCompletedDate() == null ||
                            !habit.getLastCompletedDate().equals(today)) {

                        notifier.createAndDeliver(
                                user.getId(),
                                "REMINDER",
                                "Don't forget your daily habit: " + habit.getHabitName(),
                                null,
                                true,
                                user.getEmail()
                        );
                    }
                }

                // WEEKLY HABIT
                if (habit.getFrequency().equalsIgnoreCase("WEEKLY")) {
                    LocalDate last = habit.getLastCompletedDate();
                    LocalDate monday = today.with(DayOfWeek.MONDAY);

                    if (last == null || last.isBefore(monday)) {
                        notifier.createAndDeliver(
                                user.getId(),
                                "REMINDER",
                                "Weekly habit pending: " + habit.getHabitName(),
                                null,
                                true,
                                user.getEmail()
                        );
                    }
                }
            }

                    List<Journal> todaysJournals = journalRepo.findByUserIdAndDate(user.getId(), today);
            if (todaysJournals.isEmpty()) {
                notifier.createAndDeliver(
                        user.getId(),
                        "REMINDER",
                        "Write your journal entry for today.",
                        null,
                        true,
                        user.getEmail()
                );
            }

            List<CheckIn> todaysCheckins = checkInRepo.findByUserAndDate(user, today);
            if (todaysCheckins.isEmpty()) {
                notifier.createAndDeliver(
                        user.getId(),
                        "REMINDER",
                        "Complete your daily check-in!",
                        null,
                        true,
                        user.getEmail()
                );
            }
        }
    }
}
