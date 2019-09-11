package org.jk.eSked.view.events;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.jk.eSked.components.schedule.EventGrid;
import org.jk.eSked.model.User;
import org.jk.eSked.services.LoginService;
import org.jk.eSked.services.events.EventService;
import org.jk.eSked.services.schedule.ScheduleService;
import org.jk.eSked.view.MenuView;

@Route(value = "events", layout = MenuView.class)
@PageTitle("Wydarzenia")
public class EventsView extends VerticalLayout {

    public EventsView(LoginService loginService, ScheduleService scheduleService, EventService eventService) {

        if (loginService.checkIfUserIsLogged()) {
            VerticalLayout eventGrid = new EventGrid(scheduleService, eventService, VaadinSession.getCurrent().getAttribute(User.class).getId());
            Button newEventButton = new Button("Dodaj nowe wydarzenie", e -> UI.getCurrent().navigate("events/new"));
            newEventButton.setWidth("100%");
            add(eventGrid, newEventButton);
        }
    }
}