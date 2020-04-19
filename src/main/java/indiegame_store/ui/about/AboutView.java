package indiegame_store.ui.about;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import indiegame_store.ui.MainLayout;

@Route(value = AboutView.VIEW_NAME, layout = MainLayout.class)
@PageTitle("About")
public class AboutView extends HorizontalLayout {

    public static final String VIEW_NAME = "About";

    public AboutView() {
        setId("about-container");

        add(VaadinIcon.INFO_CIRCLE.create());

        add(new Label("This was made by Martin Toode"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}
