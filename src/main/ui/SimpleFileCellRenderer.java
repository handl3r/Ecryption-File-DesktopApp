package main.ui;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class SimpleFileCellRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {


    @Override
    public Component getListCellRendererComponent(
            JList<? extends Object> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        Path relative = ((File) value).toPath().getFileName();

        return super.getListCellRendererComponent(list, relative, index, isSelected, cellHasFocus);
    }

}
