package sk.itlearning.java4.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

public class MainController {

	@FXML
	AnchorPane innerPane;

	void init() {

		innerPane.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});

		innerPane.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard();
				boolean success = false;
				if (db.hasFiles()) {
					success = true;
					String filePath = null;
					for (File file : db.getFiles()) {
						filePath = file.getAbsolutePath();
						if (file.isDirectory()) {
							Alert alert = new Alert(AlertType.CONFIRMATION);
							alert.setContentText("Do you really want to delete all contents in path: " + filePath);
							alert.setHeaderText(null);
							Optional<ButtonType> result = alert.showAndWait();
							if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
								try {
									deletePathFiles(Paths.get(filePath));
									getInfoAlert("The folder: " + filePath + " has been deleted.").showAndWait();
								} catch (Throwable e1) {
									getWarningAlert("Unable to delete dir. Root cause: ", e1).showAndWait();
								}
							}
						}
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});

	}

	Alert getInfoAlert(String info) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText(info);
		alert.setHeaderText(null);
		return alert;
	}

	Alert getConfirmAlert(String info) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText(info);
		alert.setHeaderText(null);
		return alert;
	}

	Alert getWarningAlert(String info, Throwable e1) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Operation failed");
		alert.setHeaderText(info);
		alert.setContentText(e1.getMessage());
		return alert;
	}

	void deletePathFiles(Path path) {
		if (path.toFile().isDirectory() && path.toFile().list().length > 0) {
			try (Stream<Path> contentFiles = Files.list(path)) {
				contentFiles.parallel().forEach(p -> deletePathFiles(p));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		if (!path.toFile().isDirectory() || path.toFile().list().length == 0) {
			try {
				path.toFile().setWritable(true);
				Files.delete(path);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

}
