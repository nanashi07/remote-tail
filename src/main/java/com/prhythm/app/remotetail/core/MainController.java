package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.data.ListLineItem;
import com.prhythm.app.remotetail.data.RemoteLogReaderList;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * 主介面
 * Created by nanashi07 on 15/12/30.
 */
public class MainController {

    final public static String LAYOUT_MAIN = "/com/prhythm/app/remotetail/core/main.fxml";
    final public static String ICON_SERVER = "/com/prhythm/app/icons/icon_server.png";
    final public static String ICON_LOG = "/com/prhythm/app/icons/icon_log.png";

    @FXML
    public ListView contents;
    @FXML
    public TreeView areas;

    public void load(DataWrapper wrapper) {
        // 載入設定
        TreeItem<Object> root = new TreeItem<>();
        //noinspection unchecked
        areas.setRoot(root);
        if (wrapper != null) {
            ObservableList<TreeItem<Object>> rootChildren = root.getChildren();
            for (Server server : wrapper.getServers()) {
                // 顯示 server 設定
                TreeItem<Object> treeItem = new TreeItem<>(server, getIcon(ICON_SERVER));
                treeItem.setExpanded(server.isExpanded());
                rootChildren.add(treeItem);

                // 記錄展開／折疊的變更
                treeItem.expandedProperty().addListener((observable, oldValue, newValue) -> {
                    BooleanProperty property = (BooleanProperty) observable;
                    TreeItem item = (TreeItem) property.getBean();
                    Object value = item.getValue();
                    if (value == null || !(value instanceof Server)) return;
                    Server server1 = (Server) value;
                    server1.setExpanded(newValue);
                });

                final ObservableList<TreeItem<Object>> serverChildren = treeItem.getChildren();

                // 處理 log 路徑設定
                for (LogPath logPath : server.getLogPaths()) {
                    TreeItem<Object> logPathTreeItem = new TreeItem<>(logPath, getIcon(ICON_LOG));
                    serverChildren.add(logPathTreeItem);
                }
            }
        }

        // 註冊點選 log 事件
        areas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            if (item == null) return;
            Object value = item.getValue();
            if (value == null || !(value instanceof LogPath)) return;
            LogPath path = (LogPath) value;

            TreeItem parent = item.getParent();
            Server server = (Server) parent.getValue();

            RemoteLogReaderList list = new RemoteLogReaderList(server, path);
            list.addListener((Observable observable) -> {
                // 自動重繪
                contents.refresh();
            });
            //noinspection unchecked
            contents.setItems(list);
            // 連接時移至底部
            contents.scrollTo(list.size() - 1);

        });

        // 變更呈現
        //noinspection unchecked
        contents.setCellFactory(param -> new ListLineItem());

        // copy
        contents.addEventFilter(KeyEvent.KEY_TYPED, event -> {

        });

        // 多選
        contents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    Node getIcon(String path) {
        return new ImageView(new Image(getClass().getResourceAsStream(path)));
    }

}
