package org.jk.eSked.ui.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.backend.model.BarNotification;
import org.jk.eSked.backend.model.User;
import org.jk.eSked.backend.model.event.Event;
import org.jk.eSked.backend.service.EmailService;
import org.jk.eSked.backend.service.EventService;
import org.jk.eSked.backend.service.GroupService;
import org.jk.eSked.backend.service.UserService;
import org.jk.eSked.ui.components.login.loginExceptionDialog;

import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Route(value = "login")
@PageTitle("Logowanie")
//@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@PWA(name = "eSked", shortName = "Schedule app")
class LoginView extends VerticalLayout {

    private final UserService userService;

    public LoginView(UserService userService, GroupService groupsService, EventService eventService, EmailService emailService) {
        this.userService = userService;

        if (VaadinSession.getCurrent().getSession() == null) VaadinSession.getCurrent().close();

        add(loginLayout(eventService, groupsService, emailService));
    }

    private LoginOverlay loginLayout(EventService eventService, GroupService groupsService, EmailService emailService) {
        LoginOverlay loginOverlay = new LoginOverlay();
        H1 title = new H1();
        title.getStyle().set("color", "var(--lumo-base-color)");
        Icon icon = VaadinIcon.CALENDAR.create();
        icon.setSize("30px");
        icon.getStyle().set("top", "-4px");
        title.add(icon);
        title.add(new Text(" eSked"));
        loginOverlay.setTitle(title);

        loginOverlay.setTitle(title);
        loginOverlay.addLoginListener(login -> {
            User user = authenticate(login.getUsername(), login.getPassword());
            if (user != null) {
                if (user.isVerified()) {
                    loginOverlay.setOpened(false);
                    VaadinSession.getCurrent().setAttribute(User.class, user);

                    if (groupsService.getGroupsNames().stream().noneMatch(s -> s.equals(groupsService.getGroupName(user.getGroupCode()))))
                        userService.setGroupCode(user.getId(), 0);

                    if (userService.getGroupCode(user.getId()) != 0)
                        groupsService.synchronizeWGroup(user.getId(), user.getGroupCode());

                    Collection<Event> events = eventService.getAllEvents(user.getId());
                    List<BarNotification> notifications = new ArrayList<>();
                    for (Event event : events) {
                        if (event.getCreatedDate().isAfter(Instant.ofEpochMilli(user.getLastLoggedDate()).atZone(ZoneId.systemDefault()).toLocalDateTime())) {
                            notifications.add(new BarNotification("Temat: " + event.getTopic(), event.getDate()));
                        }
                    }
                    VaadinSession.getCurrent().setAttribute(Collection.class, notifications);
                    userService.setLastLogged(user.getId(), Instant.now().toEpochMilli());

                    UI.getCurrent().navigate("schedule");
                    if (user.isDarkTheme())
                        UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"dark\")");
                    else
                        UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute(\"theme\",\"white\")");
                } else {
                    Notification notification = new Notification("Konto nie zostało jeszcze aktywowane", 10000, Notification.Position.TOP_END);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.open();
                }
            } else {
                loginOverlay.setError(true);
            }
        });
        loginOverlay.addForgotPasswordListener(forgotPasswordEvent -> new loginExceptionDialog(userService, emailService).open());

        loginOverlay.setI18n(createPolishI18n());
        loginOverlay.setOpened(true);
        return loginOverlay;
    }

    private User authenticate(String input, String password) {
        Collection<User> users = userService.getUsers();
        for (User user : users)
            if (user.getUsername().equals(input) || user.getEmail().equals(input))
                if (user.getPassword().equals(User.encodePassword(password))) return user;
        return null;
    }

    private LoginI18n createPolishI18n() {
        final LoginI18n i18n = LoginI18n.createDefault();

        i18n.setHeader(new LoginI18n.Header());
        i18n.getForm().setTitle("Zaloguj się");
        i18n.getForm().setUsername("Nazwa użytkownika lub email");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setSubmit("Zaloguj się");
        i18n.getForm().setForgotPassword("Stwórz konto lub odzyskaj hasło");
        i18n.getErrorMessage().setTitle("Nieprawidłowe dane");
        i18n.getErrorMessage().setMessage("Sprawdź czy wprowadzone dane są prawidłowe i spróbuj ponownie.");
        i18n.setAdditionalInformation("Kontakt z admininstratorem pod adresem: eskedinfo@gmail.com");
        return i18n;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
//        ThemeService themeService = new ThemeService();
//        themeService.check();
    }
}