package indiegame_store.ui;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import indiegame_store.ui.database.CurrentDatabase;
import indiegame_store.ui.database.DatabaseSelectionView;

public class GameStoreInitListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent initEvent) {
        initEvent.getSource().addUIInitListener(uiInitEvent -> uiInitEvent.getUI().addBeforeEnterListener(enterEvent -> {
            if (CurrentDatabase.get() == null && !DatabaseSelectionView.class.equals(enterEvent.getNavigationTarget())) {
                enterEvent.rerouteTo(DatabaseSelectionView.class);
            }
        }));
    }
}