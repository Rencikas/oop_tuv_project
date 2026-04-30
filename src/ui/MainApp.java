package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Vehicle;
import models.User;
import models.Session;
import services.AuthenticationService;
import services.VehicleService;
import services.FilterService;
import java.time.LocalDate;
import java.util.List;

public class MainApp extends Application {
	private AuthenticationService authService = new AuthenticationService();
	private VehicleService vehicleService = new VehicleService();
	private FilterService filterService = new FilterService(vehicleService);

	private Stage primaryStage;
	private Session currentSession;

	private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		this.primaryStage = stage;
		stage.setTitle("CarInspec - Frontend");
		showLoginScene();
		stage.show();
	}

	private void showLoginScene() {
		Label lblTitle = new Label("CarInspec - Login");
		lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

		TextField tfUsername = new TextField();
		tfUsername.setPromptText("Username");
		PasswordField pfPassword = new PasswordField();
		pfPassword.setPromptText("Password");

		Button btnLogin = new Button("Login");
		Button btnRegister = new Button("Register");

		HBox buttons = new HBox(10, btnLogin, btnRegister);
		buttons.setAlignment(Pos.CENTER);

		VBox root = new VBox(10, lblTitle, tfUsername, pfPassword, buttons);
		root.setPadding(new Insets(20));
		root.setAlignment(Pos.CENTER);

		btnLogin.setOnAction(e -> {
			String username = tfUsername.getText();
			String password = pfPassword.getText();
			Session session = authService.login(username, password);
			if (session != null) {
				currentSession = session;
				showDashboard();
			} else {
				showAlert(Alert.AlertType.ERROR, "Login failed", "Incorrect username or password.");
			}
		});

		btnRegister.setOnAction(e -> {
			String username = tfUsername.getText();
			String password = pfPassword.getText();
			boolean ok = authService.registerUser(username, password);
			if (ok) {
				showAlert(Alert.AlertType.INFORMATION, "Registered", "User registered successfully.");
				tfUsername.clear();
				pfPassword.clear();
			} else {
				showAlert(Alert.AlertType.ERROR, "Register failed",
						"Could not register user (maybe exists or invalid input).");
			}
		});

		primaryStage.setScene(new Scene(root, 360, 240));
	}

	private void showDashboard() {
		BorderPane root = new BorderPane();

		// Top - user info and logout
		HBox topBar = new HBox(10);
		Label lblUser = new Label("User: " + currentSession.getUsername() + " (" + currentSession.getRole() + ")");
		Button btnLogout = new Button("Logout");
		topBar.getChildren().addAll(lblUser, btnLogout);
		topBar.setPadding(new Insets(10));
		topBar.setAlignment(Pos.CENTER_LEFT);

		btnLogout.setOnAction(e -> {
			currentSession = null;
			showLoginScene();
		});

		root.setTop(topBar);

		// Center - tabs
		TabPane tabs = new TabPane();

		Tab vehiclesTab = new Tab("Vehicles");
		vehiclesTab.setClosable(false);
		vehiclesTab.setContent(createVehiclesPane());

		Tab usersTab = new Tab("Users");
		usersTab.setClosable(false);
		usersTab.setContent(createUsersPane());
		usersTab.setDisable(!currentSession.isAdmin());

		tabs.getTabs().addAll(vehiclesTab, usersTab);

		root.setCenter(tabs);

		primaryStage.setScene(new Scene(root, 900, 600));
	}

	private Pane createVehiclesPane() {
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));

		// Table of vehicles
		TableView<Vehicle> table = new TableView<>();
		TableColumn<Vehicle, String> colNr = new TableColumn<>("License");
		colNr.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
		TableColumn<Vehicle, String> colMake = new TableColumn<>("Make");
		colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
		TableColumn<Vehicle, String> colColor = new TableColumn<>("Color");
		colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
		TableColumn<Vehicle, String> colFuel = new TableColumn<>("Fuel");
		colFuel.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
		TableColumn<Vehicle, String> colCat = new TableColumn<>("Category");
		colCat.setCellValueFactory(new PropertyValueFactory<>("category"));
		TableColumn<Vehicle, Object> colReg = new TableColumn<>("Registered");
		colReg.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
		TableColumn<Vehicle, Object> colInsp = new TableColumn<>("Inspection expiry");
		colInsp.setCellValueFactory(new PropertyValueFactory<>("inspectionExpiryDate"));

		@SuppressWarnings("unchecked")
		TableColumn<Vehicle, ?>[] vehicleCols = new TableColumn[] { colNr, colMake, colColor, colFuel, colCat, colReg,
				colInsp };
		table.getColumns().addAll(vehicleCols);
		// Set resize policy (suppress warnings for deprecated API)
		setTableResizePolicy(table);

		// Load initial data
		refreshVehicleData(table);

		pane.setCenter(table);

		// Right - form to add/update
		VBox form = new VBox(8);
		form.setPadding(new Insets(10));
		form.setPrefWidth(300);

		TextField tfLicense = new TextField();
		tfLicense.setPromptText("License number");
		TextField tfMake = new TextField();
		tfMake.setPromptText("Make");
		TextField tfColor = new TextField();
		tfColor.setPromptText("Color");
		TextField tfFuel = new TextField();
		tfFuel.setPromptText("Fuel type");
		TextField tfCategory = new TextField();
		tfCategory.setPromptText("Category");

		Button btnAdd = new Button("Add/Save vehicle");
		Button btnUpdateInsp = new Button("Update inspection to today");
		Button btnRefresh = new Button("Refresh");

		btnAdd.setOnAction(e -> {
			String license = tfLicense.getText();
			String make = tfMake.getText();
			String color = tfColor.getText();
			String fuel = tfFuel.getText();
			String category = tfCategory.getText();
			boolean ok = vehicleService.registerVehicle(license, make, color, fuel, category);
			if (ok) {
				showAlert(Alert.AlertType.INFORMATION, "Saved", "Vehicle registered/updated.");
				refreshVehicleData(table);
			} else {
				showAlert(Alert.AlertType.ERROR, "Error",
						"Failed to register/update vehicle. Check inputs and categories.");
			}
		});

		btnUpdateInsp.setOnAction(e -> {
			Vehicle selected = table.getSelectionModel().getSelectedItem();
			if (selected == null) {
				showAlert(Alert.AlertType.WARNING, "Select vehicle", "Select a vehicle from the table first.");
				return;
			}
			boolean ok = vehicleService.updateInspectionDate(selected.getLicenseNumber(), LocalDate.now());
			if (ok) {
				showAlert(Alert.AlertType.INFORMATION, "Updated", "Inspection date updated to today.");
				refreshVehicleData(table);
			} else {
				showAlert(Alert.AlertType.ERROR, "Error", "Could not update inspection date.");
			}
		});

		btnRefresh.setOnAction(e -> refreshVehicleData(table));

		// Filter and search
		HBox filterBox = new HBox(8);
		Button btnValid = new Button("Valid inspection");
		Button btnExpired = new Button("Expired inspection");
		filterBox.getChildren().addAll(btnValid, btnExpired);

		btnValid.setOnAction(e -> {
			List<Vehicle> list = filterService.filterValidInspection();
			vehicleData.setAll(list);
			table.setItems(vehicleData);
		});
		btnExpired.setOnAction(e -> {
			List<Vehicle> list = filterService.filterExpiredInspection();
			vehicleData.setAll(list);
			table.setItems(vehicleData);
		});

		HBox searchBox = new HBox(8);
		TextField tfSearchField = new TextField();
		tfSearchField.setPromptText("Search value");
		ChoiceBox<String> cbField = new ChoiceBox<>(
				FXCollections.observableArrayList("numeris", "marke", "spalva", "kuras", "kategorija"));
		cbField.setValue("numeris");
		Button btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			String field = cbField.getValue();
			String value = tfSearchField.getText();
			List<Vehicle> list = filterService.searchVehicles(field, value);
			vehicleData.setAll(list);
			table.setItems(vehicleData);
		});
		searchBox.getChildren().addAll(cbField, tfSearchField, btnSearch);

		form.getChildren().addAll(new Label("Vehicle form"), tfLicense, tfMake, tfColor, tfFuel, tfCategory, btnAdd,
				btnUpdateInsp, new Separator(), new Label("Filters"), filterBox, searchBox, btnRefresh);

		pane.setRight(form);

		return pane;
	}

	private Pane createUsersPane() {
		VBox box = new VBox(8);
		box.setPadding(new Insets(10));

		TableView<User> table = new TableView<>();
		TableColumn<User, String> colName = new TableColumn<>("Username (encrypted)");
		colName.setCellValueFactory(new PropertyValueFactory<>("username"));
		TableColumn<User, String> colRole = new TableColumn<>("Role");
		colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

		@SuppressWarnings("unchecked")
		TableColumn<User, ?>[] userCols = new TableColumn[] { colName, colRole };
		table.getColumns().addAll(userCols);
		// Set resize policy (suppress warnings for deprecated API)
		setTableResizePolicy(table);

		List<User> users = authService.getAllUsers();
		ObservableList<User> userData = FXCollections.observableArrayList(users);
		table.setItems(userData);

		Button btnRefresh = new Button("Refresh");
		btnRefresh.setOnAction(e -> table.setItems(FXCollections.observableArrayList(authService.getAllUsers())));

		box.getChildren().addAll(table, btnRefresh);
		return box;
	}

	private void refreshVehicleData(TableView<Vehicle> table) {
		List<Vehicle> all = vehicleService.getAllVehicles();
		vehicleData.setAll(all);
		table.setItems(vehicleData);
	}

	private void showAlert(Alert.AlertType type, String title, String text) {
		Alert a = new Alert(type);
		a.setTitle(title);
		a.setHeaderText(null);
		a.setContentText(text);
		a.showAndWait();
	}

	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	private <T> void setTableResizePolicy(TableView<T> table) {
		Callback resizePolicy = TableView.CONSTRAINED_RESIZE_POLICY;
		table.setColumnResizePolicy(resizePolicy);
	}
}
