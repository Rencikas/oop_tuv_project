package ui;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;
import models.Vehicle;
import models.User;

import models.Session;
import services.AuthenticationService;
import services.VehicleService;
import services.FilterService;

import java.time.LocalDate;
import java.util.Arrays;

public class MainApp extends Application {

    private AuthenticationService authService = new AuthenticationService();
    private VehicleService vehicleService = new VehicleService();
    private FilterService filterService = new FilterService(vehicleService);

    private Stage primaryStage;
    private Session currentSession;
    private ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

    private final String BG_GRADIENT = "-fx-background-color: linear-gradient(to bottom right, #0f0c29, #302b63, #24243e);";

    private final String CARD_BG = "-fx-background-color: rgba(255,255,255,0.05);" +
            "-fx-background-radius: 20;" +
            "-fx-border-color: rgba(255,255,255,0.1);" +
            "-fx-border-radius: 20;";

    private final String PANEL_BG = "-fx-background-color: rgba(255,255,255,0.04);" +
            "-fx-background-radius: 14;" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-border-radius: 14;";

    private final String ACCENT = "#00d2ff";
    private final String TEXT_WHITE = "#ffffff";
    private final String TEXT_DIM = "#9aa0b2";
    private final String DANGER = "#ff4b2b";
    private final String SUCCESS = "#00e676";
    private final String WARNING = "#ffb300";

    private final String TABLE_CSS = ".table-view { -fx-background-color: transparent; -fx-border-color: transparent; }"
            +
            ".table-view .column-header-background { -fx-background-color: rgba(0,210,255,0.08); }" +
            ".table-view .column-header, .table-view .filler { -fx-background-color: transparent; -fx-border-color: transparent; }"
            +
            ".table-view .column-header .label { -fx-text-fill: #00d2ff; -fx-font-weight: bold; -fx-font-size: 12px; }"
            +
            ".table-view .table-row-cell { -fx-background-color: transparent; -fx-border-color: rgba(255,255,255,0.05); -fx-border-width: 0 0 1 0; }"
            +
            ".table-view .table-row-cell:odd { -fx-background-color: rgba(255,255,255,0.02); }" +
            ".table-view .table-row-cell:selected { -fx-background-color: rgba(0,210,255,0.15); }" +
            ".table-view .table-cell { -fx-text-fill: #e8eaf0; -fx-font-size: 13px; }" +
            ".table-view .scroll-bar:vertical { -fx-background-color: transparent; }" +
            ".table-view .scroll-bar:vertical .thumb { -fx-background-color: rgba(0,210,255,0.3); -fx-background-radius: 4; }";

    private final String TAB_CSS = ".tab-pane .tab-header-area .tab-header-background { -fx-background-color: rgba(0,0,0,0.35); }"
            +
            ".tab-pane .tab { -fx-background-color: transparent; -fx-padding: 10 24; }" +
            ".tab-pane .tab:selected { -fx-background-color: rgba(0,210,255,0.12); }" +
            ".tab-pane .tab .tab-label { -fx-text-fill: #9aa0b2; -fx-font-size: 13px; -fx-font-weight: bold; }" +
            ".tab-pane .tab:selected .tab-label { -fx-text-fill: #00d2ff; }" +
            ".tab-pane .tab-content-area { -fx-background-color: transparent; }";

    private final String CHOICE_CSS = ".choice-box { -fx-background-color: rgba(0,0,0,0.4); -fx-border-color: rgba(255,255,255,0.12); "
            +
            "-fx-border-radius: 8; -fx-background-radius: 8; }" +
            ".choice-box .label { -fx-text-fill: white; }" +
            ".choice-box .open-button .arrow { -fx-background-color: #9aa0b2; }" +
            ".context-menu { -fx-background-color: #1e1b3a; -fx-border-color: rgba(0,210,255,0.2); }" +
            ".menu-item .label { -fx-text-fill: white; }" +
            ".menu-item:focused { -fx-background-color: rgba(0,210,255,0.15); }";

    private final String SCROLL_CSS = ".scroll-pane { -fx-background-color: transparent; -fx-border-color: transparent; }"
            +
            ".scroll-pane > .viewport { -fx-background-color: transparent; }" +
            ".scroll-bar .thumb { -fx-background-color: rgba(0,210,255,0.3); -fx-background-radius: 4; }" +
            ".scroll-bar { -fx-background-color: transparent; }";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("CarInspec");
        try {
            java.nio.file.Path externalIcon = java.nio.file.Paths.get("data/icons/app-icon.png");
            if (java.nio.file.Files.exists(externalIcon)) {
                Image appIcon = new Image(externalIcon.toUri().toString());
                stage.getIcons().add(appIcon);
            } else {
                var is = getClass().getResourceAsStream("/icons/app-icon.png");
                if (is != null) {
                    Image appIcon = new Image(is);
                    stage.getIcons().add(appIcon);
                }
            }
        } catch (Exception ignored) {
        }
        StackPane splashRoot = new StackPane();
        splashRoot.setStyle(BG_GRADIENT);
        Label title = new Label("CarInspec");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 42px; -fx-font-weight: bold;");
        Label subtitle = new Label("Vehicle inspection management");
        subtitle.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 14px;");
        VBox v = new VBox(8, title, subtitle);
        v.setAlignment(Pos.CENTER);
        ProgressIndicator pi = new ProgressIndicator();
        pi.setPrefSize(64, 64);
        VBox wrapper = new VBox(20, v, pi);
        wrapper.setAlignment(Pos.CENTER);
        splashRoot.getChildren().add(wrapper);

        Scene initialScene = new Scene(splashRoot, 1280, 720);
        initialScene.setFill(Color.web("#0f0c29"));
        stage.setScene(initialScene);
        stage.setMaximized(true);
        stage.show();
        animateSplash(splashRoot, wrapper, stage, initialScene);
    }

    private void animateSplash(StackPane splashRoot, VBox wrapper, Stage stage, Scene splashScene) {
        FadeTransition ft = new FadeTransition(javafx.util.Duration.millis(900), wrapper);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setInterpolator(Interpolator.EASE_OUT);
        ft.play();

        new Thread(() -> {
            try {
                Thread.sleep(900);
            } catch (InterruptedException ignored) {
            }
            Platform.runLater(() -> {
                StackPane overlay = new StackPane();
                overlay.setStyle(BG_GRADIENT);
                overlay.setOpacity(0);
                overlay.prefWidthProperty().bind(splashScene.widthProperty());
                overlay.prefHeightProperty().bind(splashScene.heightProperty());
                splashRoot.getChildren().add(overlay);

                FadeTransition fin = new FadeTransition(javafx.util.Duration.millis(300), overlay);
                fin.setFromValue(0);
                fin.setToValue(1);
                fin.setOnFinished(ev -> {
                    showLoginScene();
                    Scene sc = stage.getScene();
                    if (sc != null && sc.getRoot() instanceof Pane) {
                        Pane newRoot = (Pane) sc.getRoot();
                        if (overlay.getParent() instanceof Pane) {
                            ((Pane) overlay.getParent()).getChildren().remove(overlay);
                        }
                        newRoot.getChildren().add(overlay);
                        FadeTransition fout = new FadeTransition(javafx.util.Duration.millis(320), overlay);
                        fout.setFromValue(1);
                        fout.setToValue(0);
                        fout.setDelay(javafx.util.Duration.millis(80));
                        fout.setOnFinished(e2 -> newRoot.getChildren().remove(overlay));
                        fout.play();
                    } else {
                        if (overlay.getParent() instanceof Pane) {
                            ((Pane) overlay.getParent()).getChildren().remove(overlay);
                        }
                    }
                });
                fin.play();
            });
        }).start();
    }

    private void showLoginScene() {
        StackPane root = new StackPane();
        root.setStyle(BG_GRADIENT);

        VBox card = new VBox(25);
        card.setMaxSize(380, 470);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setStyle(CARD_BG);
        card.setOpacity(0);

        Label lblHeader = new Label("LOGIN");
        lblHeader.setStyle("-fx-text-fill: " + TEXT_WHITE + "; -fx-font-size: 26px; -fx-font-weight: bold;");

        HBox toggleContainer = new HBox(0);
        toggleContainer.setAlignment(Pos.CENTER);
        toggleContainer.setMaxWidth(200);
        toggleContainer.setStyle("-fx-background-color: #1a1a2e; -fx-background-radius: 25;");

        Button btnToggleLogin = new Button("Login");
        Button btnToggleSign = new Button("Sign In");
        styleToggleBtn(btnToggleLogin, true);
        styleToggleBtn(btnToggleSign, false);
        toggleContainer.getChildren().addAll(btnToggleLogin, btnToggleSign);

        TextField tfUser = new TextField();
        tfUser.setPromptText("Username");
        styleField(tfUser);

        PasswordField pfPass = new PasswordField();
        pfPass.setPromptText("Password");
        styleField(pfPass);

        Label lblLoginMsg = new Label("");
        lblLoginMsg.setWrapText(true);
        lblLoginMsg.setStyle("-fx-font-size: 12px;");

        Button btnAction = new Button("SIGN IN");
        btnAction.setPrefWidth(Double.MAX_VALUE);
        btnAction.setPrefHeight(45);
        btnAction.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: #0f0c29; " +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand;");

        final boolean[] isLoginMode = { true };

        Runnable flipAnimation = () -> {
            RotateTransition r1 = new RotateTransition(Duration.millis(250), card);
            r1.setAxis(Rotate.Y_AXIS);
            r1.setFromAngle(0);
            r1.setToAngle(90);
            r1.setInterpolator(Interpolator.EASE_IN);
            r1.setOnFinished(e -> {
                if (isLoginMode[0]) {
                    lblHeader.setText("LOGIN");
                    btnAction.setText("SIGN IN");
                    styleToggleBtn(btnToggleLogin, true);
                    styleToggleBtn(btnToggleSign, false);
                } else {
                    lblHeader.setText("REGISTER");
                    btnAction.setText("CREATE ACCOUNT");
                    styleToggleBtn(btnToggleLogin, false);
                    styleToggleBtn(btnToggleSign, true);
                }
                RotateTransition r2 = new RotateTransition(Duration.millis(250), card);
                r2.setAxis(Rotate.Y_AXIS);
                r2.setFromAngle(90);
                r2.setToAngle(0);
                r2.setInterpolator(Interpolator.EASE_OUT);
                r2.play();
            });
            r1.play();
        };

        btnToggleLogin.setOnAction(e -> {
            if (!isLoginMode[0]) {
                isLoginMode[0] = true;
                flipAnimation.run();
            }
        });
        btnToggleSign.setOnAction(e -> {
            if (isLoginMode[0]) {
                isLoginMode[0] = false;
                flipAnimation.run();
            }
        });

        Runnable doAction = () -> {
            String u = tfUser.getText();
            String p = pfPass.getText();
            if (isLoginMode[0]) {
                Session session = authService.login(u, p);
                if (session != null) {
                    fadeToOverlayThen(() -> {
                        currentSession = session;
                        showDashboard();
                    });
                } else {
                    lblLoginMsg.setText("Invalid username or password. Please check your credentials and try again.");
                    lblLoginMsg.setStyle("-fx-text-fill: " + DANGER + "; -fx-font-size: 12px;");
                }
            } else {
                if (authService.registerUser(u, p)) {
                    lblLoginMsg.setText("Account created successfully. You can now sign in.");
                    lblLoginMsg.setStyle("-fx-text-fill: " + SUCCESS + "; -fx-font-size: 12px;");
                    new Thread(() -> {
                        try {
                            Thread.sleep(900);
                        } catch (InterruptedException ignored) {
                        }
                        Platform.runLater(() -> {
                            isLoginMode[0] = true;
                            flipAnimation.run();
                            lblLoginMsg.setText("");
                        });
                    }).start();
                }
            }
        };

        btnAction.setOnAction(e -> doAction.run());
        card.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                doAction.run();
        });

        tfUser.setOnKeyTyped(ev -> lblLoginMsg.setText(""));
        pfPass.setOnKeyTyped(ev -> lblLoginMsg.setText(""));

        card.getChildren().addAll(toggleContainer, lblHeader, tfUser, pfPass, lblLoginMsg, btnAction);
        root.getChildren().add(card);
        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(root, 1280, 720));
        } else {
            primaryStage.getScene().setRoot(root);
        }

        FadeTransition fin = new FadeTransition(Duration.millis(360), card);
        fin.setFromValue(0.0);
        fin.setToValue(1.0);
        fin.play();
    }

    private void showDashboard() {
        BorderPane root = new BorderPane();
        root.setStyle(BG_GRADIENT);

        HBox topBar = new HBox(20);
        topBar.setPadding(new Insets(14, 28, 14, 28));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: rgba(0,0,0,0.45);" +
                "-fx-border-color: rgba(0,210,255,0.1);" +
                "-fx-border-width: 0 0 1 0;");

        Label lblApp = new Label("CarInspec");
        lblApp.setStyle("-fx-text-fill: " + ACCENT + "; -fx-font-size: 20px; -fx-font-weight: bold;");

        Label lblSep = new Label("│");
        lblSep.setStyle("-fx-text-fill: rgba(255,255,255,0.2); -fx-font-size: 20px;");

        Label lblUser = new Label(
                currentSession.getUsername().toUpperCase() + "  ·  "
                        + currentSession.getRole().toString().toUpperCase());
        lblUser.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 12px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnLogout = new Button("LOGOUT");
        btnLogout.setPrefHeight(34);
        btnLogout.setPadding(new Insets(0, 18, 0, 18));
        String logoutIdle = "-fx-background-color: rgba(255,75,43,0.15);" +
                "-fx-border-color: " + DANGER + ";" +
                "-fx-border-radius: 6; -fx-background-radius: 6;" +
                "-fx-text-fill: " + DANGER + "; -fx-font-weight: bold; -fx-cursor: hand;";
        String logoutHover = "-fx-background-color: " + DANGER + ";" +
                "-fx-border-color: " + DANGER + ";" +
                "-fx-border-radius: 6; -fx-background-radius: 6;" +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;";
        btnLogout.setStyle(logoutIdle);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle(logoutHover));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle(logoutIdle));
        btnLogout.setOnAction(e -> {
            fadeToOverlayThen(() -> {
                currentSession = null;
                showLoginScene();
            });
        });

        topBar.getChildren().addAll(lblApp, lblSep, lblUser, spacer, btnLogout);

        TabPane tabs = new TabPane();
        tabs.getStylesheets().add(buildDataUri(TAB_CSS));

        Tab tabVehicles = new Tab("Vehicle Info", createVehicleInfoPane());
        Tab tabRegister = new Tab("Register Vehicle", createRegisterVehiclePane());
        Tab tabUsers = new Tab("System Users", createUsersPane());

        tabVehicles.setClosable(false);
        tabRegister.setClosable(false);
        tabUsers.setClosable(false);

        if (!currentSession.isAdmin()) {
            tabUsers.setDisable(true);
        }

        tabs.getTabs().addAll(tabVehicles, tabRegister, tabUsers);

        tabs.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab != null && newTab.getContent() != null) {
                Node content = newTab.getContent();
                content.setOpacity(0);
                FadeTransition tf = new FadeTransition(Duration.millis(300), content);
                tf.setFromValue(0.0);
                tf.setToValue(1.0);
                tf.play();
            }
        });

        root.setTop(topBar);
        root.setCenter(tabs);

        primaryStage.getScene().setRoot(root);

        setSceneRootWithReveal(root);
    }

    private Pane createVehicleInfoPane() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: transparent;");
        pane.setPadding(new Insets(24, 28, 24, 28));

        VBox leftSection = new VBox(18);
        leftSection.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(leftSection, Priority.ALWAYS);

        Label lblTitle = new Label("Vehicle Registry");
        lblTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(4, lblTitle);

        HBox searchRow = new HBox(12);
        searchRow.setAlignment(Pos.CENTER_LEFT);
        searchRow.setPadding(new Insets(14, 18, 14, 18));
        searchRow.setStyle(PANEL_BG);

        TextField tfSearch = new TextField();
        tfSearch.setPromptText("Search vehicles...");
        styleField(tfSearch);
        HBox.setHgrow(tfSearch, Priority.ALWAYS);

        ChoiceBox<String> cbField = new ChoiceBox<>(
                FXCollections.observableArrayList("Number", "Make", "Color", "Fuel", "Category"));
        cbField.setValue("Number");
        cbField.setPrefWidth(130);
        cbField.setPrefHeight(42);
        styleChoiceBox(cbField);

        Button btnSearch = buildAccentButton("SEARCH", ACCENT);
        btnSearch.setPrefHeight(42);
        btnSearch.setPadding(new Insets(0, 22, 0, 22));

        Button btnClear = buildOutlineButton("CLEAR", TEXT_DIM);
        btnClear.setPrefHeight(42);
        btnClear.setPadding(new Insets(0, 16, 0, 16));

        searchRow.getChildren().addAll(tfSearch, cbField, btnSearch, btnClear);

        HBox filterRow = new HBox(10);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        Label lblFilterLbl = new Label("FILTER:");
        lblFilterLbl.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 11px; -fx-font-weight: bold;");

        Button btnAll = buildChipButton("All Vehicles", true);
        Button btnValid = buildChipButton("Valid", false);
        Button btnExpired = buildChipButton("Expired", false);

        filterRow.getChildren().addAll(lblFilterLbl, btnAll, btnValid, btnExpired);

        TableView<Vehicle> table = new TableView<>();
        applyTableCSS(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Vehicle, String> colLic = new TableColumn<>("License No.");
        colLic.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        colLic.setMinWidth(130);

        TableColumn<Vehicle, String> colMake = new TableColumn<>("Make");
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colMake.setMinWidth(110);

        TableColumn<Vehicle, String> colColor = new TableColumn<>("Color");
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colColor.setMinWidth(90);

        TableColumn<Vehicle, String> colFuel = new TableColumn<>("Fuel Type");
        colFuel.setCellValueFactory(new PropertyValueFactory<>("fuelType"));
        colFuel.setMinWidth(100);

        TableColumn<Vehicle, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(new PropertyValueFactory<>("category"));
        colCat.setMinWidth(100);

        TableColumn<Vehicle, Object> colReg = new TableColumn<>("Registered");
        colReg.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        colReg.setMinWidth(110);

        TableColumn<Vehicle, Object> colInsp = new TableColumn<>("Inspection Exp.");
        colInsp.setCellValueFactory(new PropertyValueFactory<>("inspectionExpiryDate"));
        colInsp.setMinWidth(130);

        colInsp.setCellFactory(col -> new TableCell<Vehicle, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item.toString());
                LocalDate expiry = null;
                try {
                    expiry = (LocalDate) item;
                } catch (ClassCastException ignored) {
                }
                if (expiry != null) {
                    if (expiry.isBefore(LocalDate.now())) {
                        setStyle("-fx-text-fill: " + DANGER + "; -fx-font-weight: bold;");
                    } else if (expiry.isBefore(LocalDate.now().plusDays(30))) {
                        setStyle("-fx-text-fill: " + WARNING + "; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: " + SUCCESS + ";");
                    }
                } else {
                    setStyle("-fx-text-fill: " + TEXT_DIM + ";");
                }
            }
        });

        table.getColumns().addAll(Arrays.asList(colLic, colMake, colColor, colFuel, colCat, colReg, colInsp));
        refreshVehicleData(table);

        btnSearch.setOnAction(e -> {
            vehicleData.setAll(filterService.searchVehicles(cbField.getValue(), tfSearch.getText()));
            table.setItems(vehicleData);
            setActiveChip(btnAll, btnValid, btnExpired, null);
        });

        tfSearch.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                btnSearch.fire();
        });

        btnClear.setOnAction(e -> {
            tfSearch.clear();
            refreshVehicleData(table);
            setActiveChip(btnAll, btnValid, btnExpired, btnAll);
        });

        btnAll.setOnAction(e -> {
            refreshVehicleData(table);
            setActiveChip(btnAll, btnValid, btnExpired, btnAll);
        });

        btnValid.setOnAction(e -> {
            vehicleData.setAll(filterService.filterValidInspection());
            table.setItems(vehicleData);
            setActiveChip(btnAll, btnValid, btnExpired, btnValid);
        });

        btnExpired.setOnAction(e -> {
            vehicleData.setAll(filterService.filterExpiredInspection());
            table.setItems(vehicleData);
            setActiveChip(btnAll, btnValid, btnExpired, btnExpired);
        });

        leftSection.getChildren().addAll(titleBox, searchRow, filterRow, table);

        VBox rightPanel = new VBox(20);
        rightPanel.setPrefWidth(280);
        rightPanel.setPadding(new Insets(24, 20, 24, 20));
        rightPanel.setStyle(PANEL_BG);
        rightPanel.setAlignment(Pos.TOP_LEFT);

        Label lblActionTitle = new Label("Inspection Update");
        lblActionTitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");

        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: rgba(255,255,255,0.08);");

        Label lblSelected = new Label("");
        lblSelected.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 12px;");
        lblSelected.setWrapText(true);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                lblSelected.setText(newV.getLicenseNumber() + "\n" + newV.getMake());
                lblSelected.setStyle("-fx-text-fill: " + ACCENT + "; -fx-font-size: 12px; -fx-font-weight: bold;");
            } else {
                lblSelected.setText("");
                lblSelected.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 12px;");
            }
        });

        Label lblDateLabel = makeSectionLabel("NEW INSPECTION DATE");

        DatePicker dpInsp = new DatePicker(LocalDate.now());
        dpInsp.setPrefWidth(Double.MAX_VALUE);
        dpInsp.setPrefHeight(42);
        dpInsp.setStyle("-fx-background-color: rgba(0,0,0,0.4);" +
                "-fx-border-color: rgba(255,255,255,0.12);" +
                "-fx-border-radius: 8; -fx-background-radius: 8;" +
                "-fx-text-fill: white;");
        dpInsp.getEditor().setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        Button btnUpdateInsp = buildAccentButton("UPDATE INSPECTION", ACCENT);
        btnUpdateInsp.setPrefWidth(Double.MAX_VALUE);
        btnUpdateInsp.setPrefHeight(44);

        Label lblInspResult = new Label("");
        lblInspResult.setWrapText(true);
        lblInspResult.setStyle("-fx-font-size: 11px;");

        btnUpdateInsp.setOnAction(e -> {
            Vehicle selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                lblInspResult.setText("Warning: Please select a vehicle first.");
                lblInspResult.setStyle("-fx-text-fill: " + WARNING + "; -fx-font-size: 11px;");
                return;
            }
            LocalDate date = dpInsp.getValue() != null ? dpInsp.getValue() : LocalDate.now();
            if (vehicleService.updateInspectionDate(selected.getLicenseNumber(), date)) {
                refreshVehicleData(table);
                lblInspResult.setText("Success: Inspection updated for\n" + selected.getLicenseNumber());
                lblInspResult.setStyle("-fx-text-fill: " + SUCCESS + "; -fx-font-size: 11px;");
            } else {
                lblInspResult.setText("Error: Update failed. Please try again.");
                lblInspResult.setStyle("-fx-text-fill: " + DANGER + "; -fx-font-size: 11px;");
            }
        });

        Separator sep2 = new Separator();
        sep2.setStyle("-fx-background-color: rgba(255,255,255,0.08);");

        Label lblStatsTitle = makeSectionLabel("STATS");

        int totalVehicles = vehicleService.getAllVehicles().size();
        int validCount = filterService.filterValidInspection().size();
        int expiredCount = filterService.filterExpiredInspection().size();

        VBox statsBox = new VBox(12,
                buildStatRow("Total Vehicles", String.valueOf(totalVehicles), TEXT_WHITE),
                buildStatRow("Valid", String.valueOf(validCount), SUCCESS),
                buildStatRow("Expired", String.valueOf(expiredCount), DANGER));

        Region spacerR = new Region();
        spacerR.setPrefHeight(4);

        rightPanel.getChildren().addAll(
                lblActionTitle, sep1,
                lblSelected,
                spacerR,
                lblDateLabel,
                dpInsp,
                btnUpdateInsp,
                lblInspResult,
                sep2,
                lblStatsTitle,
                statsBox);

        BorderPane.setMargin(rightPanel, new Insets(0, 0, 0, 24));
        pane.setCenter(leftSection);
        pane.setRight(rightPanel);
        return pane;
    }

    private Pane createRegisterVehiclePane() {
        StackPane wrapper = new StackPane();
        wrapper.setStyle("-fx-background-color: transparent;");
        wrapper.setPadding(new Insets(30));

        ScrollPane scroll = new ScrollPane();
        scroll.setFitToWidth(true);
        scroll.getStylesheets().add(buildDataUri(SCROLL_CSS));

        VBox form = new VBox(28);
        form.setPadding(new Insets(32, 60, 40, 60));
        form.setMaxWidth(760);
        form.setStyle("-fx-background-color: transparent;");
        form.setFillWidth(true);

        Label lblTitle = new Label("Register New Vehicle");
        lblTitle.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(5, lblTitle);

        TextField tfLic = new TextField();
        tfLic.setPromptText("License");
        styleField(tfLic);

        TextField tfMake = new TextField();
        tfMake.setPromptText("Make");
        styleField(tfMake);

        TextField tfColor = new TextField();
        tfColor.setPromptText("Color");
        styleField(tfColor);

        ChoiceBox<String> cbFuel = new ChoiceBox<>(FXCollections.observableArrayList(
                "Please select...", "Petrol", "Diesel", "Electric", "Hybrid", "LPG", "Hydrogen", "CNG"));
        cbFuel.setValue("Please select...");
        cbFuel.setPrefHeight(46);
        cbFuel.setMaxWidth(Double.MAX_VALUE);
        styleChoiceBox(cbFuel);

        ChoiceBox<String> cbCategory = new ChoiceBox<>(FXCollections.observableArrayList(
                "Please select...", "Sedan", "SUV", "Hatchback", "Coupe", "Convertible",
                "Pickup Truck", "Van", "Minivan", "Wagon", "Motorcycle", "Bus", "Truck"));
        cbCategory.setValue("Please select...");
        cbCategory.setPrefHeight(46);
        cbCategory.setMaxWidth(Double.MAX_VALUE);
        styleChoiceBox(cbCategory);

        VBox fieldsBox = new VBox(20);
        fieldsBox.setPadding(new Insets(28));
        fieldsBox.setStyle(PANEL_BG);

        fieldsBox.getChildren().addAll(
                buildNumberedRow(1, "License Number", "Unique plate number for this vehicle", tfLic),
                buildFormDivider(),
                buildNumberedRow(2, "Make / Brand", "Manufacturer or brand name", tfMake),
                buildFormDivider(),
                buildNumberedRow(3, "Color", "Primary body color of the vehicle", tfColor),
                buildFormDivider(),
                buildNumberedRow(4, "Fuel Type", "Type of fuel used by the vehicle", cbFuel),
                buildFormDivider(),
                buildNumberedRow(5, "Category", "Vehicle body type / class", cbCategory));

        Label lblResult = new Label("");
        lblResult.setWrapText(true);
        lblResult.setStyle("-fx-font-size: 12px;");

        Button btnRegister = buildAccentButton("REGISTER VEHICLE", ACCENT);
        btnRegister.setPrefHeight(50);
        btnRegister.setPrefWidth(Double.MAX_VALUE);

        Button btnReset = buildOutlineButton("RESET FORM", TEXT_DIM);
        btnReset.setPrefHeight(50);
        btnReset.setPrefWidth(Double.MAX_VALUE);

        HBox btnRow = new HBox(14, btnRegister, btnReset);
        btnRow.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(btnRegister, Priority.ALWAYS);
        HBox.setHgrow(btnReset, Priority.ALWAYS);
        btnRegister.setMaxWidth(Double.MAX_VALUE);
        btnReset.setMaxWidth(Double.MAX_VALUE);
        btnRegister.setMinWidth(220);
        btnReset.setMinWidth(160);
        btnRegister.setPrefHeight(50);
        btnReset.setPrefHeight(50);

        btnRegister.setOnAction(e -> {
            String lic = tfLic.getText().trim();
            String make = tfMake.getText().trim();
            String col = tfColor.getText().trim();
            String fuel = cbFuel.getValue();
            String cat = cbCategory.getValue();

            if (lic.isEmpty() || make.isEmpty() || col.isEmpty() || "Please select...".equals(fuel)
                    || "Please select...".equals(cat)) {
                lblResult.setText("Warning: Please fill in all required fields before submitting.");
                lblResult.setStyle("-fx-text-fill: " + WARNING + "; -fx-font-size: 12px;");
                return;
            }

            if (vehicleService.registerVehicle(lic, make, col, fuel, cat)) {
                lblResult.setText("Success: Vehicle '" + lic + "' has been successfully registered.");
                lblResult.setStyle("-fx-text-fill: " + SUCCESS + "; -fx-font-size: 12px;");
                tfLic.clear();
                tfMake.clear();
                tfColor.clear();
                cbFuel.setValue("Please select...");
                cbCategory.setValue("Please select...");
            } else {
                lblResult.setText("Error: Registration failed. License number may already exist.");
                lblResult.setStyle("-fx-text-fill: " + DANGER + "; -fx-font-size: 12px;");
            }
        });

        btnReset.setOnAction(e -> {
            tfLic.clear();
            tfMake.clear();
            tfColor.clear();
            cbFuel.setValue("Please select...");
            cbCategory.setValue("Please select...");
            lblResult.setText("");
        });

        form.getChildren().addAll(titleBox, fieldsBox, btnRow, lblResult);

        StackPane centered = new StackPane(form);
        StackPane.setAlignment(form, Pos.TOP_CENTER);
        scroll.setContent(centered);

        wrapper.getChildren().add(scroll);
        return wrapper;
    }

    private Pane createUsersPane() {
        BorderPane pane = new BorderPane();
        pane.setStyle("-fx-background-color: transparent;");
        pane.setPadding(new Insets(24, 28, 24, 28));

        VBox mainBox = new VBox(20);
        mainBox.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(mainBox, Priority.ALWAYS);

        Label lblTitle = new Label("System Users");
        lblTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");
        VBox titleBox = new VBox(6, lblTitle);

        TableView<User> table = new TableView<>();
        table.setPlaceholder(buildEmptyLabel("No users found"));
        applyTableCSS(table);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<User, String> colUsername = new TableColumn<>("Username");
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUsername.setCellFactory(col -> new TableCell<User, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                setText(decryptShift(item));
                setStyle("-fx-text-fill: " + ACCENT + "; -fx-font-weight: bold;");
            }
        });

        TableColumn<User, String> colPassword = new TableColumn<>("Password");
        colPassword.setCellValueFactory(new PropertyValueFactory<>("passwordHash"));
        colPassword.setCellFactory(col -> new TableCell<User, String>() {
            private boolean showing = false;
            private String decrypted = "";
            private final Label lbl = new Label();

            {
                lbl.setStyle("-fx-text-fill: " + TEXT_WHITE + ";");
                lbl.setMaxWidth(Double.MAX_VALUE);
                lbl.setWrapText(false);
                setGraphic(lbl);
                setOnMouseClicked(e -> {
                    if (getItem() == null)
                        return;
                    showing = !showing;
                    lbl.setText(showing ? decrypted : mask(decrypted.length()));
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                decrypted = decryptShift(item);
                showing = false;
                lbl.setText(mask(decrypted.length()));
                setGraphic(lbl);
            }

            private String mask(int len) {
                if (len <= 0)
                    return "";
                return "*".repeat(Math.max(0, len));
            }
        });

        TableColumn<User, String> colRole = new TableColumn<>("Role");
        colRole.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue() != null && cell.getValue().getRole() != null ? cell.getValue().getRole().name() : ""));

        colRole.setCellFactory(col -> new TableCell<User, String>() {
            private final ComboBox<String> combo = new ComboBox<>(FXCollections.observableArrayList("USER", "ADMIN"));

            {
                combo.setMaxWidth(Double.MAX_VALUE);
                combo.setOnAction(e -> {
                    String v = combo.getValue();
                    if (v != null)
                        commitEdit(v);
                });
            }

            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                    combo.setValue(getItem());
                    setText(null);
                    setGraphic(combo);
                    combo.show();
                }
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setGraphic(null);
                updateItem(getItem(), false);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }
                if (isEditing()) {
                    combo.setValue(item);
                    setText(null);
                    setGraphic(combo);
                } else {
                    setGraphic(null);
                    setText(item.toUpperCase());
                    if ("ADMIN".equalsIgnoreCase(item)) {
                        setStyle("-fx-text-fill: " + WARNING + "; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: " + TEXT_DIM + ";");
                    }
                }
            }
        });
        table.setEditable(true);
        Runnable loadUsers = () -> table.setItems(FXCollections.observableArrayList(authService.getAllUsers()));
        ObservableList<String> roleOptions = FXCollections.observableArrayList("USER", "ADMIN");
        colRole.setCellFactory(col -> new javafx.scene.control.cell.ComboBoxTableCell<>(roleOptions));
        colRole.setOnEditCommit(evt -> {
            if (currentSession == null || !currentSession.isAdmin()) {
                showAlert(Alert.AlertType.WARNING, "Permission", "Only admins can change roles.");
                loadUsers.run();
                return;
            }
            User u = evt.getRowValue();
            String newVal = evt.getNewValue();
            if (u == null || newVal == null)
                return;
            User.UserRole newRole;
            try {
                newRole = User.UserRole.valueOf(newVal);
            } catch (IllegalArgumentException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Role", "Unknown role: " + newVal);
                loadUsers.run();
                return;
            }
            boolean ok = authService.updateUserRole(u.getUsername(), newRole);
            if (!ok) {
                showAlert(Alert.AlertType.ERROR, "Save Failed", "Failed to update user role.");
            }
            loadUsers.run();
        });

        table.getColumns().addAll(Arrays.asList(colUsername, colPassword, colRole));
        table.widthProperty().addListener((obs, oldW, newW) -> {
            double w = newW.doubleValue();
            if (w <= 0)
                return;
            colUsername.setPrefWidth(w * 0.30);
            colPassword.setPrefWidth(w * 0.30);
            colRole.setPrefWidth(w * 0.30);
        });
        loadUsers.run();

        HBox toolbar = new HBox(14);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        mainBox.getChildren().addAll(titleBox, table, toolbar);
        pane.setCenter(mainBox);
        return pane;
    }

    private String decryptShift(String encrypted) {
        if (encrypted == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (char c : encrypted.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                sb.append((char) (base + (c - base - 2 + 26) % 26));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private void applyHoverEffect(Button btn) {
        btn.setOnMouseEntered(e -> {
            btn.setScaleX(1.02);
            btn.setScaleY(1.02);
            btn.setStyle(btn.getStyle() + " -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8,0,0,2);");
        });
        btn.setOnMouseExited(e -> {
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
            btn.setStyle(btn.getStyle().replace(" -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8,0,0,2);", ""));
        });
        btn.setOnMousePressed(e -> {
            btn.setScaleX(0.98);
            btn.setScaleY(0.98);
        });
        btn.setOnMouseReleased(e -> {
            btn.setScaleX(1.02);
            btn.setScaleY(1.02);
        });
    }

    private void fadeToOverlayThen(Runnable action) {
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            action.run();
            return;
        }
        StackPane overlay = new StackPane();
        overlay.setStyle(BG_GRADIENT);
        overlay.setOpacity(0);
        overlay.prefWidthProperty().bind(scene.widthProperty());
        overlay.prefHeightProperty().bind(scene.heightProperty());

        Pane rootPane = (Pane) scene.getRoot();
        rootPane.getChildren().add(overlay);

        FadeTransition fin = new FadeTransition(Duration.millis(260), overlay);
        fin.setFromValue(0.0);
        fin.setToValue(1.0);
        fin.setOnFinished(e -> {
            try {
                action.run();
            } finally {
                FadeTransition fout = new FadeTransition(Duration.millis(260), overlay);
                fout.setFromValue(1.0);
                fout.setToValue(0.0);
                fout.setDelay(javafx.util.Duration.millis(80));
                fout.setOnFinished(ev -> rootPane.getChildren().remove(overlay));
                fout.play();
            }
        });
        fin.play();
    }

    private void setSceneRootWithReveal(Parent newRoot) {
        Scene scene = primaryStage.getScene();
        if (scene == null) {
            primaryStage.setScene(new Scene(newRoot, 1280, 720));
            return;
        }
        Pane oldRoot = (Pane) scene.getRoot();

        StackPane overlay = new StackPane();
        overlay.setStyle(BG_GRADIENT);
        overlay.prefWidthProperty().bind(scene.widthProperty());
        overlay.prefHeightProperty().bind(scene.heightProperty());
        overlay.setOpacity(1.0);

        oldRoot.getChildren().add(overlay);

        scene.setRoot(newRoot);

        FadeTransition fout = new FadeTransition(Duration.millis(320), overlay);
        fout.setFromValue(1.0);
        fout.setToValue(0.0);
        fout.setOnFinished(e -> {
            if (newRoot instanceof Pane) {
                ((Pane) newRoot).getChildren().remove(overlay);
            } else {
                oldRoot.getChildren().remove(overlay);
            }
        });
        fout.play();
    }

    private void refreshVehicleData(TableView<Vehicle> table) {
        vehicleData.setAll(vehicleService.getAllVehicles());
        table.setItems(vehicleData);
    }

    private void styleToggleBtn(Button btn, boolean active) {
        btn.setPrefWidth(100);
        if (active) {
            btn.setStyle("-fx-background-color: " + ACCENT + "; -fx-text-fill: #1a1a2e;" +
                    "-fx-background-radius: 25; -fx-font-weight: bold;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        }
    }

    private void styleField(Control field) {
        field.setPrefHeight(46);
        field.setMaxWidth(Double.MAX_VALUE);
        field.setStyle("-fx-background-color: rgba(0,0,0,0.35);" +
                "-fx-text-fill: white;" +
                "-fx-prompt-text-fill: #555;" +
                "-fx-border-color: rgba(255,255,255,0.12);" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;" +
                "-fx-font-size: 13px;");
    }

    private void styleChoiceBox(ChoiceBox<?> cb) {
        cb.getStylesheets().add(buildDataUri(CHOICE_CSS));
    }

    private Button buildAccentButton(String text, String color) {
        Button btn = new Button(text);
        String idle = "-fx-background-color: " + color + "; -fx-text-fill: #0f0c29;" +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 12px;";
        String hover = "-fx-background-color: derive(" + color + ",-15%); -fx-text-fill: #0f0c29;" +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-size: 12px;";
        btn.setStyle(idle);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e -> btn.setStyle(idle));
        applyHoverEffect(btn);
        return btn;
    }

    private Button buildOutlineButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent;" +
                "-fx-border-color: rgba(255,255,255,0.18);" +
                "-fx-border-radius: 8; -fx-background-radius: 8;" +
                "-fx-text-fill: " + color + ";" +
                "-fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 12px;");
        applyHoverEffect(btn);
        return btn;
    }

    private Button buildChipButton(String text, boolean active) {
        Button btn = new Button(text);
        applyChipStyle(btn, active);
        applyHoverEffect(btn);
        return btn;
    }

    private void applyChipStyle(Button btn, boolean active) {
        if (active) {
            btn.setStyle("-fx-background-color: rgba(0,210,255,0.15);" +
                    "-fx-border-color: " + ACCENT + ";" +
                    "-fx-border-radius: 20; -fx-background-radius: 20;" +
                    "-fx-text-fill: " + ACCENT + ";" +
                    "-fx-font-size: 11px; -fx-font-weight: bold; -fx-cursor: hand; -fx-padding: 5 14;");
        } else {
            btn.setStyle("-fx-background-color: rgba(255,255,255,0.06);" +
                    "-fx-border-color: rgba(255,255,255,0.1);" +
                    "-fx-border-radius: 20; -fx-background-radius: 20;" +
                    "-fx-text-fill: " + TEXT_DIM + ";" +
                    "-fx-font-size: 11px; -fx-cursor: hand; -fx-padding: 5 14;");
        }
    }

    private void setActiveChip(Button all, Button valid, Button expired, Button active) {
        for (Button b : new Button[] { all, valid, expired })
            applyChipStyle(b, b == active);
    }

    private void applyTableCSS(TableView<?> table) {
        table.getStylesheets().add(buildDataUri(TABLE_CSS));
        table.setStyle("-fx-background-color: transparent;");
    }

    private VBox buildNumberedRow(int num, String label, String hint, Control control) {
        Label numBadge = new Label(String.valueOf(num));
        numBadge.setPrefSize(28, 28);
        numBadge.setAlignment(Pos.CENTER);
        numBadge.setStyle("-fx-background-color: rgba(0,210,255,0.15);" +
                "-fx-border-color: " + ACCENT + ";" +
                "-fx-border-radius: 50; -fx-background-radius: 50;" +
                "-fx-text-fill: " + ACCENT + ";" +
                "-fx-font-weight: bold; -fx-font-size: 12px;");

        Label lblMain = new Label(label);
        lblMain.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");
        VBox labelBox = new VBox(2, lblMain);

        HBox labelRow = new HBox(14, numBadge, labelBox);
        labelRow.setAlignment(Pos.CENTER_LEFT);

        return new VBox(10, labelRow, control);
    }

    private Separator buildFormDivider() {
        Separator sep = new Separator();
        sep.setStyle("-fx-background-color: rgba(255,255,255,0.06);");
        return sep;
    }

    private Label makeSectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 10px; -fx-font-weight: bold;");
        return lbl;
    }

    private HBox buildStatRow(String label, String value, String color) {
        Label lblL = new Label(label);
        lblL.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 12px;");
        Region sp = new Region();
        HBox.setHgrow(sp, Priority.ALWAYS);
        Label lblV = new Label(value);
        lblV.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");
        HBox row = new HBox(lblL, sp, lblV);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Label buildEmptyLabel(String msg) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-text-fill: " + TEXT_DIM + "; -fx-font-size: 13px;");
        return lbl;
    }

    private String buildDataUri(String css) {
        return "data:text/css," + css
                .replace("%", "%25")
                .replace(" ", "%20")
                .replace("#", "%23")
                .replace("(", "%28")
                .replace(")", "%29")
                .replace(",", "%2C")
                .replace(":", "%3A")
                .replace(";", "%3B")
                .replace("{", "%7B")
                .replace("}", "%7D")
                .replace(".", "%2E")
                .replace(">", "%3E")
                .replace("+", "%2B")
                .replace("~", "%7E")
                .replace("'", "%27")
                .replace("\"", "%22")
                .replace("[", "%5B")
                .replace("]", "%5D")
                .replace("*", "%2A");
    }

    private void showAlert(Alert.AlertType type, String title, String text) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(text);
        a.showAndWait();
    }
}
