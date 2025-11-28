package com.example.demo.service;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.model.Notification;
import com.example.demo.model.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.email.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository repo;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository repo,
                               EmailService emailService,
                               UserRepository userRepository) {
        this.repo = repo;
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createAndDeliver(
            Long userId,
            String type,
            String message,
            String link,
            boolean emailIfOffline,
            String emailAddress
    ) {
        // 1. Get user from database
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            System.err.println("User not found: " + userId);
            return;
        }

        // 2. Save notification to database FIRST
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(type);
        notification.setMessage(message);
        notification.setLink(link);
        notification.setRead(false);

        Notification saved = repo.save(notification);
        System.out.println("💾 Notification saved to DB: ID=" + saved.getId());

        // 3. Prepare email body
        String html = buildEmailHtml(type, message, link);

        // 4. Send email if allowed and email address exists
        if (emailIfOffline && emailAddress != null && !emailAddress.isBlank()) {
            try {
                emailService.sendEmail(emailAddress, "MindTrack: " + type, html);
                System.out.println("Sent email to " + emailAddress);
            } catch (Exception e) {
                System.err.println("Failed to send email: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String buildEmailHtml(String type, String message, String link) {
        if ("CRISIS".equalsIgnoreCase(type)) {
            return """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                    .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px 8px 0 0; margin: -30px -30px 20px -30px; }
                    .severity-badge { background-color: #dc3545; color: white; padding: 5px 10px; border-radius: 4px; font-weight: bold; font-size: 14px; }
                    h3.crisis-title { color: #b02a37; }
                    .crisis-text { color: #333; font-size: 16px; line-height: 1.5; margin-bottom: 20px; }
                    .resource-title { font-weight: bold; margin-top: 20px; }
                    .resources { list-style: none; padding-left: 0; }
                    .resources li { margin-bottom: 15px; }
                    .resources a { text-decoration: none; color: #007bff; font-weight: bold; }
                    .resources div { font-size: 0.875rem; color: #6c757d; margin-top: 0.25rem; }
                    .alert-date { font-size: 12px; color: #999; margin-top: 30px; }
                    .footer { margin-top: 40px; font-size: 12px; color: #999; text-align: center; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header"><h1 style="margin: 0;">MindTrack - Crisis Alert</h1></div>
                    <span class="severity-badge">CRISIS</span>
                    <h3 class="crisis-title">You're Going Through a Hard Time</h3>
                    <p class="crisis-text">"" + message + ""</p>

                    <p class="resource-title">Helpful Resources:</p>
                    <ul class="resources">
                        <li>
                            <a href="tel:115">Call Umang 24/7 Helpline</a>
                            <div>24/7 crisis support</div>
                        </li>
                        <li>
                            <a href="https://rozan.org" target="_blank">Rozan Counseling Services</a>
                            <div>Professional mental health support</div>
                        </li>
                        <li>
                            <a href="https://taskeen.org/" target="_blank">Taskeen Mental Health Services</a>
                            <div>International emotional support</div>
                        </li>
                        <li>
                            <a href="https://counseling.pk/" target="_blank">More Pakistan Resources</a>
                            <div>Additional local support options</div>
                        </li>
                    </ul>

                    """ + (link != null ? "<p><a href=\"" + link + "\" style=\"font-weight:bold;\">View in App</a></p>" : "") + """

                    <div class="footer">
                        This is an automated notification from MindTrack. If this was unexpected, please contact support.
                    </div>
                </div>
            </body>
            </html>
            """;
        }

        // Default fallback for other types
        return """
        <!DOCTYPE html>
        <html>
        <head>
            <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                .header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; border-radius: 8px 8px 0 0; margin: -30px -30px 20px -30px; }
                .type { font-size: 14px; text-transform: uppercase; font-weight: bold; color: #667eea; margin-bottom: 10px; }
                .message { font-size: 16px; line-height: 1.6; color: #333; margin-bottom: 20px; }
                .button { display: inline-block; padding: 12px 24px; background-color: #667eea; color: white; text-decoration: none; border-radius: 6px; font-weight: bold; }
                .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #e0e0e0; font-size: 12px; color: #999; text-align: center; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="header"><h1 style="margin: 0;">MindTrack</h1></div>
                <div class="type">""" + type + """
                </div>
                <div class="message">""" + message + """
                </div>
                """ + (link != null ? "<a href=\"" + link + "\" class=\"button\">View in App</a>" : "") + """
                <div class="footer">
                    This is an automated notification from MindTrack.
                </div>
            </div>
        </body>
        </html>
        """;
    }


    public long unreadCount(Long userId) {
        return repo.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void markRead(Long id) {
        repo.findById(id).ifPresent(n -> {
            n.setRead(true);
            repo.save(n);
        });
    }
}
