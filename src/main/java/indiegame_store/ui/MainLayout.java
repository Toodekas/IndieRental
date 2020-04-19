package indiegame_store.ui;

import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import indiegame_store.ui.customer.CustomerList;
import indiegame_store.ui.inventory.VGameStoreInventory;
import indiegame_store.ui.order.OrderList;
import indiegame_store.ui.about.AboutView;

@HtmlImport("css/shared-styles.html")
@Theme(value = Lumo.class, variant = Lumo.DARK)
public class MainLayout extends FlexLayout implements RouterLayout {

    private Menu menu;

    public MainLayout() {
        setSizeFull();
        setId("main-layout");

        menu = new Menu();
        menu.setId("menu");
        menu.addView(VGameStoreInventory.class, VGameStoreInventory.VIEW_NAME, VaadinIcon.EDIT.create());
        menu.addView(CustomerList.class, "Customer list", VaadinIcon.USER.create());
        menu.addView(OrderList.class, "Orders list", VaadinIcon.ARCHIVES.create());
        menu.addView(AboutView.class, AboutView.VIEW_NAME, VaadinIcon.INFO_CIRCLE.create());

        add(menu);
    }
}
