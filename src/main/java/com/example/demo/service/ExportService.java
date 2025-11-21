package com.example.demo.service;

import com.example.demo.model.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExportService {

    private final MoodService moodService;
    private final JournalService journalService;
    private final StressService stressService;
    private final GoalService goalService;

    @Autowired
    public ExportService(MoodService moodService, JournalService journalService,
            StressService stressService, GoalService goalService) {
        this.moodService = moodService;
        this.journalService = journalService;
        this.stressService = stressService;
        this.goalService = goalService;
    }

    public byte[] exportUserDataToPDF(User user, LocalDate startDate, LocalDate endDate) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
        Paragraph title = new Paragraph("MindTrack Mental Health Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        // User info
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("User: " + user.getUsername(), normalFont));
        document.add(new Paragraph("Report Period: " + startDate + " to " + endDate, normalFont));
        document.add(new Paragraph(" "));

        // Mood Summary
        addMoodSection(document, user, startDate, endDate);

        // Stress Summary
        addStressSection(document, user, startDate, endDate);

        // Journal Entries
        addJournalSection(document, user, startDate, endDate);

        // Goals Summary
        addGoalSection(document, user);

        document.close();
        return baos.toByteArray();
    }

    private void addMoodSection(Document document, User user, LocalDate start, LocalDate end) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.BLUE);
        document.add(new Paragraph("Mood Summary", sectionFont));
        document.add(new Paragraph(" "));

        List<Mood> moods = moodService.getMoodsByUserAndDateRange(user, start, end);
        Double avgIntensity = moodService.getAverageMoodIntensity(user, start, end);

        document.add(new Paragraph("Total Mood Entries: " + moods.size()));
        document.add(new Paragraph(
                "Average Mood Intensity: " + (avgIntensity != null ? String.format("%.2f", avgIntensity) : "N/A")));
        document.add(new Paragraph(" "));

        if (!moods.isEmpty()) {
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell("Date");
            table.addCell("Mood Level");
            table.addCell("Intensity");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Mood mood : moods) {
                table.addCell(mood.getDate().format(formatter));
                table.addCell(mood.getMoodLevel());
                table.addCell(String.valueOf(mood.getIntensity()));
            }
            document.add(table);
        }
        document.add(new Paragraph(" "));
    }

    private void addStressSection(Document document, User user, LocalDate start, LocalDate end)
            throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.RED);
        document.add(new Paragraph("Stress Summary", sectionFont));
        document.add(new Paragraph(" "));

        List<Stress> stressList = stressService.getStressByUserAndDateRange(user, start, end);
        Double avgStress = stressService.getAverageStressLevel(user, start, end);

        document.add(new Paragraph("Total Stress Entries: " + stressList.size()));
        document.add(new Paragraph(
                "Average Stress Level: " + (avgStress != null ? String.format("%.2f", avgStress) : "N/A")));
        document.add(new Paragraph(" "));
    }

    private void addJournalSection(Document document, User user, LocalDate start, LocalDate end)
            throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.GREEN);
        document.add(new Paragraph("Journal Entries", sectionFont));
        document.add(new Paragraph(" "));

        List<Journal> journals = journalService.getJournalsByUserAndDateRange(user, start, end);
        document.add(new Paragraph("Total Journal Entries: " + journals.size()));
        document.add(new Paragraph(" "));
    }

    private void addGoalSection(Document document, User user) throws DocumentException {
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.ORANGE);
        document.add(new Paragraph("Goals Summary", sectionFont));
        document.add(new Paragraph(" "));

        List<Goal> allGoals = goalService.getGoalsByUser(user);
        int completed = (int) allGoals.stream().filter(g -> "completed".equals(g.getStatus())).count();
        int pending = (int) allGoals.stream().filter(g -> "pending".equals(g.getStatus())).count();

        document.add(new Paragraph("Total Goals: " + allGoals.size()));
        document.add(new Paragraph("Completed: " + completed));
        document.add(new Paragraph("Pending: " + pending));
    }

    public String exportUserDataToCSV(User user, LocalDate startDate, LocalDate endDate) {
        StringBuilder csv = new StringBuilder();
        csv.append("Type,Date,Value,Details\n");

        // Export moods
        List<Mood> moods = moodService.getMoodsByUserAndDateRange(user, startDate, endDate);
        for (Mood mood : moods) {
            csv.append("Mood,").append(mood.getDate()).append(",")
                    .append(mood.getMoodLevel()).append(",")
                    .append("Intensity: ").append(mood.getIntensity()).append("\n");
        }

        // Export stress
        List<Stress> stressList = stressService.getStressByUserAndDateRange(user, startDate, endDate);
        for (Stress stress : stressList) {
            csv.append("Stress,").append(stress.getDate()).append(",")
                    .append(stress.getStressLevel()).append(",")
                    .append(stress.getNotes() != null ? stress.getNotes() : "").append("\n");
        }

        // Export journals
        List<Journal> journals = journalService.getJournalsByUserAndDateRange(user, startDate, endDate);
        for (Journal journal : journals) {
            String text = journal.getEntryText().replace(",", ";").replace("\n", " ");
            csv.append("Journal,").append(journal.getDate()).append(",")
                    .append(journal.getEmotionTags().stream().map(com.example.demo.model.EmotionTag::getName)
                            .collect(java.util.stream.Collectors.joining(";")))
                    .append(",")
                    .append(text).append("\n");
        }

        return csv.toString();
    }
}